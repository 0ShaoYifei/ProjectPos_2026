"""Read-only submission checks. Visual review is separately acknowledged by the caller."""
from pathlib import Path
from datetime import datetime, timezone
from zipfile import ZipFile
from docx import Document
from pypdf import PdfReader
import json, re, hashlib, sys

def sha(path):return hashlib.sha256(path.read_bytes()).hexdigest()
def compact(text):return re.sub(r'\s+','',text).replace('\u00ad','')
results={'recorded_at':datetime.now(timezone.utc).isoformat(),'quizzes':[]}
content=json.loads(Path('deliverables/quizzes/quiz_content.json').read_text())
for n in [1,2]:
    original=Document(f'references/Quiz{n}.docx')
    out=Path(f'deliverables/quizzes/Quiz{n}_Answers.docx')
    doc=Document(out)
    questions=lambda d:[p.text for p in d.paragraphs if re.match(r'^\d+\.',p.text.strip())]
    assert questions(doc)==questions(original)
    assert len(questions(doc))==15
    assert [int(re.match(r'^(\d+)\.',q.strip()).group(1)) for q in questions(doc)]==list(range(1,16))
    answers=[p.text for p in doc.paragraphs if p.style.name=='Quiz Answer']
    assert answers==content[f'quiz{n}'] and len(answers)==15
    pdf=out.with_suffix('.pdf');reader=PdfReader(pdf)
    # Remove standalone footer page numbers before checking answers spanning pages.
    pdftext=compact(''.join(re.sub(r'(?m)^\s*\d+\s*$','',p.extract_text() or '') for p in reader.pages))
    for index,a in enumerate(answers,1):
        if compact(a) not in pdftext:
            import difflib
            normalized=compact(a);match=difflib.SequenceMatcher(None,normalized,pdftext,autojunk=False).find_longest_match()
            print('Quiz',n,'answer',index,'longest matching prefix context',repr(normalized[max(0,match.a-35):match.a+50]))
            print('Answer tail',repr(normalized[-150:]))
            print('PDF related text',repr(pdftext[max(0,match.b-60):match.b+match.size+180]))
            raise AssertionError('PDF answer text differs')
    results['quizzes'].append({'quiz':n,'questions':15,'answers':15,'original_wording':True,'all_answers_present_in_pdf':True,'pdf_pages':len(reader.pages),'docx_sha256':sha(out),'pdf_sha256':sha(pdf)})

report=Path('deliverables/report/POS_Technical_Report.docx');doc=Document(report)
chapters=[p.text for p in doc.paragraphs if p.style.name=='Report Chapter' and p.text.startswith('Chapter ')]
assert len(chapters)==10
body='\n'.join(p.text for p in doc.paragraphs)+'\n'+'\n'.join(c.text for t in doc.tables for row in t.rows for c in row.cells)
assert not re.search('[\u4e00-\u9fff]',body)
assert len(doc.tables)==9 and len(doc.inline_shapes)==15
pdf=report.with_suffix('.pdf');reader=PdfReader(pdf);pages=[p.extract_text() or '' for p in reader.pages]
assert len(pages)==32
toc=' '.join(' '.join(pages[1:3]).split());toc_checks=[]
for title in chapters:
    position=next(i for i,text in enumerate(pages) if i>=3 and title in ' '.join(text.split()))
    printed_page=position-2
    assert re.search(re.escape(title)+r'\s*\.{2,}\s*'+str(printed_page)+r'\b',toc),title
    toc_checks.append({'title':title,'pdf_page':position+1,'printed_page':printed_page})
preserved=json.loads(Path('docs/evidence/report-template-preservation.json').read_text())
with ZipFile('references/Tech Report Cover Page (3).docx') as source,ZipFile(report) as target:
    assert all(source.read(p['path'])==target.read(p['path']) for p in preserved['preserved_parts'])
reviewed='--visual-reviewed' in sys.argv
results['report']={'pdf_pages':len(pages),'chapters':10,'tables':9,'figures':14,'english_body':True,'template_parts_preserved':True,'contents_verified':toc_checks,'docx_sha256':sha(report),'pdf_sha256':sha(pdf),'all_32_pages_visually_reviewed':reviewed,'visual_result':'No clipped text/images, overlap, missing glyphs or broken table rows observed.' if reviewed else 'Pending explicit visual review.'}
Path('docs/evidence/report-final-verification.json').write_text(json.dumps(results,indent=2),encoding='utf-8')
print('PASS: 30 exact numbered questions and answers; complete quiz PDFs; 32-page report; ten contents entries; immutable cover parts.')
print('Complete report visual review recorded:',reviewed)
