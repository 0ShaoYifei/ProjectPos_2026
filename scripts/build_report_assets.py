from pathlib import Path
from datetime import datetime
from reportlab.graphics.shapes import Drawing,String,Line
from reportlab.graphics.charts.lineplots import LinePlot
from reportlab.lib import colors
from reportlab.graphics import renderPDF,renderSVG
from PIL import Image
import csv,shutil,subprocess,json,hashlib
assets=Path('deliverables/report/report_assets');assets.mkdir(parents=True,exist_ok=True)
shutil.copyfile('docs/evidence/progress.csv',assets/'burndown_data.csv')
rows=list(csv.DictReader((assets/'burndown_data.csv').open(encoding='utf-8-sig')))
times=[datetime.fromisoformat(r['timestamp']) for r in rows];minutes=[(t-times[0]).total_seconds()/60 for t in times]
d=Drawing(650,360);d.add(String(56,332,'Recorded remaining work',fontName='Helvetica-Bold',fontSize=18));d.add(String(56,310,'20 October 2026   Intraday checkpoints   Unit: backlog items',fontSize=10,fillColor=colors.HexColor('#425b62')))
chart=LinePlot();chart.x=65;chart.y=75;chart.width=540;chart.height=210;chart.data=[list(zip(minutes,[int(r['remaining_work_items']) for r in rows]))]
chart.lines[0].strokeColor=colors.HexColor('#176d65');chart.lines[0].strokeWidth=2;chart.joinedLines=1
from reportlab.graphics.widgets.markers import makeMarker
chart.lines[0].symbol=makeMarker('FilledCircle');chart.lines[0].symbol.size=6;chart.lines[0].symbol.fillColor=colors.HexColor('#176d65')
chart.xValueAxis.valueMin=0;chart.xValueAxis.valueMax=max(minutes)+3;chart.xValueAxis.labels.fontSize=9
chart.yValueAxis.valueMin=0;chart.yValueAxis.valueMax=18;chart.yValueAxis.valueSteps=[0,3,6,9,12,15,18];chart.yValueAxis.labels.fontSize=10
chart.yValueAxis.gridStrokeColor=colors.HexColor('#dce4e5');chart.yValueAxis.visibleGrid=1;d.add(chart)
d.add(String(65,45,'Minutes since '+times[0].strftime('%H:%M')+' (UTC+08:00)',fontSize=10));d.add(String(65,23,'Only observed checkpoints are plotted; no historical daily values are inferred.',fontSize=9,fillColor=colors.HexColor('#425b62')))
renderPDF.drawToFile(d,str(assets/'10-recorded-burndown.pdf'));renderSVG.drawToFile(d,str(assets/'10-recorded-burndown.svg'))
poppler=r'C:/Users/ASUS/.cache/codex-runtimes/codex-primary-runtime/dependencies/native/poppler/Library/bin/pdftoppm.exe'
subprocess.run([poppler,'-singlefile','-scale-to','1800','-png',str(assets/'10-recorded-burndown.pdf'),str(assets/'10-recorded-burndown')],check=True)
# Crops preserve the original screenshot pixels and are identified as detail views.
crops=[('01-products.png','11-products-detail.png',(170,100,1270,580)),('05-paid-receipt.png','12-receipt-detail.png',(320,150,1120,790)),('07-return-refund.png','13-return-detail.png',(320,815,1120,1740)),('04-insufficient-payment.png','14-payment-error-detail.png',(170,105,1270,810))]
provenance=[]
for source,target,box in crops:
 f=Path('docs/evidence/screenshots')/source;im=Image.open(f);box=(box[0],box[1],min(box[2],im.width),min(box[3],im.height));im.crop(box).save(assets/target);provenance.append({'output':target,'source':str(f),'crop_box':box,'source_sha256':hashlib.sha256(f.read_bytes()).hexdigest(),'pixel_content':'unaltered crop'})
(assets/'screenshot-provenance.json').write_text(json.dumps(provenance,indent=2),encoding='utf-8')
print('Created measured chart and four genuine screenshot details')
