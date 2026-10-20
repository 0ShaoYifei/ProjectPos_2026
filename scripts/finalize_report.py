"""Restore immutable template parts after Word updates fields; audit actual outputs."""
from pathlib import Path
from zipfile import ZipFile, ZIP_DEFLATED
from lxml import etree
import hashlib, json

root=Path('deliverables/report')
source=Path('references/Tech Report Cover Page (3).docx')
output=root/'POS_Technical_Report.docx'
inventory=json.loads(Path('docs/evidence/report-template-preservation.json').read_text())
with ZipFile(source) as z: original={n:z.read(n) for n in z.namelist()}
with ZipFile(output) as z: parts={n:z.read(n) for n in z.namelist()}
for entry in inventory['preserved_parts']:
    parts[entry['path']]=original[entry['path']]
# Word copies the cover template's grid setting directly into generated TOC
# paragraphs. Disable that grid only for the generated contents entries.
ns={'w':'http://schemas.openxmlformats.org/wordprocessingml/2006/main'}
tree=etree.fromstring(parts['word/document.xml'])
for para in tree.xpath('//w:p[w:pPr/w:pStyle[starts-with(@w:val,"TOC")]]',namespaces=ns):
    pp=para.find('w:pPr',ns)
    for old in pp.findall('w:snapToGrid',ns):pp.remove(old)
    snap=etree.SubElement(pp,'{'+ns['w']+'}snapToGrid');snap.set('{'+ns['w']+'}val','0')
parts['word/document.xml']=etree.tostring(tree,xml_declaration=True,encoding='UTF-8',standalone=True)
with ZipFile(output,'w',ZIP_DEFLATED) as z:
    for name,data in parts.items():z.writestr(name,data)
with ZipFile(output) as z:
    for entry in inventory['preserved_parts']:
        assert z.read(entry['path'])==original[entry['path']],entry['path']
assert hashlib.sha256(source.read_bytes()).hexdigest()==inventory['reference_sha256']
inventory['final_template_parts_verified']=True
inventory['word_field_update_followed_by_template_restoration']=True
Path('docs/evidence/report-template-preservation.json').write_text(json.dumps(inventory,indent=2),encoding='utf-8')
print('Original template parts and logo preserved; editable report field results retained.')
