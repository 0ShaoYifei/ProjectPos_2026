from pathlib import Path
import subprocess,sys,hashlib
poppler=Path(r'C:/Users/ASUS/.cache/codex-runtimes/codex-primary-runtime/dependencies/native/poppler/Library/bin/pdftoppm.exe')
for arg in sys.argv[1:]:
 pdf=Path(arg);digest=hashlib.sha256(pdf.read_bytes()).hexdigest()[:10];out=Path('tmp/qa')/(pdf.stem+'-'+digest);out.mkdir(parents=True,exist_ok=True)
 subprocess.run([str(poppler),'-scale-to','1600','-png',str(pdf),str(out/'page')],check=True)
 print(pdf, 'rendered pages',len(list(out.glob('page-*.png'))),'output',out)
