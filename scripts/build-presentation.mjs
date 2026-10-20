import fs from 'node:fs/promises';
import path from 'node:path';
import {Presentation,PresentationFile} from 'file:///C:/Users/ASUS/.cache/codex-runtimes/codex-primary-runtime/dependencies/node/node_modules/@oai/artifact-tool/dist/artifact_tool.mjs';
import {finalizePresentation,applyPresentationChartFont,makeNativeBulletParagraphs} from 'file:///C:/Users/ASUS/.codex/plugins/cache/openai-primary-runtime/presentations/26.1015.12040/skills/presentations/container_tools/artifact_tool_utils.mjs';

const workspaceDir=process.cwd(),build=path.join(workspaceDir,'tmp/phase4-deck'),out=path.join(workspaceDir,'deliverables/presentation');
process.env.RUNTIME_NODE_MODULES??='C:/Users/ASUS/.cache/codex-runtimes/codex-primary-runtime/dependencies/node/node_modules';
const SKILL_DIR='C:/Users/ASUS/.codex/plugins/cache/openai-primary-runtime/presentations/26.1015.12040/skills/presentations';
await fs.mkdir(build,{recursive:true});await fs.mkdir(out,{recursive:true});
const content=JSON.parse(await fs.readFile(path.join(out,'presentation_content.json'),'utf8'));
const identity=JSON.parse(await fs.readFile('.local/student-info.json','utf8'));
const p=Presentation.create({slideSize:{width:1280,height:720}});
const c={navy:'#173449',teal:'#0D726D',muted:'#4C626E',line:'#D9E2E5',pale:'#EFF5F5',white:'#FFFFFF'};
function text(s,value,x,y,w,h,size=28,color=c.navy,bold=false,name='text'){
 const a=s.shapes.add({geometry:'textbox',name,position:{left:x,top:y,width:w,height:h},fill:'none',line:{fill:'none',width:0}});
 a.text=value;a.text.style={typeface:'Arial',fontSize:size,color,bold,insets:0,autoFit:'none',verticalAlignment:'top'};return a;
}
function base(n){const s=p.slides.add();s.background.fill=c.white;text(s,content.slides[n-1].title,64,42,1150,68,44,c.navy,true,'slide-title');text(s,String(n).padStart(2,'0'),1160,665,55,24,17,c.muted,false,'slide-number');return s;}
function note(s,n){const d=content.slides[n-1];s.speakerNotes.text=`Slide ${n}\nTarget: ${d.seconds} seconds\n\nNarration\n${d.narration}\n\nScreen actions\n${d.actions}\n\nTransition\n${d.transition}\n\nSources\n${d.sources.join('\n')}`;}
function bullets(s,items,x,y,w,h,size=28){const a=text(s,'',x,y,w,h,size);a.text=makeNativeBulletParagraphs(items,{marginLeftPoints:18,hangingPoints:9,spaceAfterPoints:15});a.text.style={typeface:'Arial',fontSize:size,color:c.navy,autoFit:'none',insets:0};return a;}
function table(s,values,x,y,w,h,widths){const t=s.tables.add({rows:values.length,columns:values[0].length,left:x,top:y,width:w,height:h,columnWidths:widths,values});
 t.borders.assign({style:'solid',fill:c.line,width:1});
 for(let r=0;r<values.length;r++){t.rows[r].height=h/values.length;for(let col=0;col<values[0].length;col++){
  const cell=t.getCell(r,col);cell.fill=r===0?c.navy:(r%2===0?c.pale:c.white);
  cell.text.style={typeface:'Arial',fontSize:24,color:r===0?c.white:c.navy,bold:r===0,insets:{left:14,right:12,top:10,bottom:8},verticalAlignment:'middle'};
 }}return t;}
function node(s,label,x,y,w=290,h=80,geometry='rect'){const a=s.shapes.add({geometry,name:label.replaceAll('\n',' '),position:{left:x,top:y,width:w,height:h},fill:c.pale,line:{fill:c.teal,width:1.4}});a.text=label;a.text.style={typeface:'Arial',fontSize:27,color:c.navy,bold:false,alignment:'center',verticalAlignment:'middle',insets:10};return a;}
function connect(s,a,b,fromSide,toSide,arrow=true){return s.shapes.connect(a,b,{kind:'straight',fromSide,toSide,line:{fill:c.muted,width:1.6},...(arrow?{tail:{type:'arrow'}}:{})});}
async function img(s,file,x,y,w,h,alt){const bytes=await fs.readFile(file);return s.images.add({blob:bytes.buffer.slice(bytes.byteOffset,bytes.byteOffset+bytes.byteLength),contentType:'image/png',alt,fit:'contain',position:{left:x,top:y,width:w,height:h}});}
const assets='deliverables/report/report_assets';

// 1: restrained academic cover.
{
 const s=p.slides.add();s.background.fill=c.navy;
 text(s,'Analysis and Design\nof a PoS System',70,115,1120,190,66,c.white,true,'presentation-title');
 text(s,'ProjectPos_2026',75,345,1100,50,33,'#BDE7DF');
 text(s,identity.name+'\nSoftware Engineering Practice\nIndividual course project',75,455,1050,130,26,c.white);
 text(s,'20 October 2026',75,637,750,30,20,'#BDE7DF');note(s,1);
}
{
 const s=base(2);text(s,'Process Sale',70,165,520,55,38,c.teal,true);
 bullets(s,['Correct item totals and change','A saved receipt and transaction','Stock reduced after payment'],75,250,500,290,29);
 text(s,'Handle Returns',680,165,500,55,38,c.teal,true);
 bullets(s,['A return against the original purchase','A recorded cash refund','Accepted quantities restored to stock'],685,250,500,300,29);
 text(s,'Consistent records across each completed business operation',75,585,1120,55,28,c.navy,true);note(s,2);
}
{
 const s=base(3);table(s,[['Area','Required behavior','Safeguard'],['Products','Create, edit and deactivate','Unique barcode; valid price and stock'],['Sales','Cart, cash, change and receipt','Stock and payment checked again at checkout'],['Returns','Original-price refund and restocking','Remaining purchased quantity limits each return'],['Records','Receipt lookup and refund ledger','Saved item name and price snapshots']],65,155,1150,430,[190,400,560]);
 text(s,'English forms and errors support every workflow',75,615,1100,32,25,c.muted);note(s,3);
}
{
 const s=base(4);table(s,[['Observed increment','Verified outcome'],['Products','5 passing tests at the product checkpoint'],['Cash sales','13 passing tests after sale processing'],['Returns','21 passing tests after return processing'],['Integration','21 tests on MySQL; 13 browser checks']],75,160,1125,385,[335,790]);
 text(s,'Individual Scrum adaptation',75,580,1030,38,30,c.teal,true);
 text(s,'One recorded development session; retained failures supplied feedback',75,625,1100,32,24,c.muted);note(s,4);
}
{
 const s=base(5);table(s,[['Backlog group','Original item IDs'],['Products and inventory','P01–P02'],['Sale, payment and receipts','P03–P08'],['Returns and validation','P09–P12'],['Verification and documentation','P13–P18']],65,155,580,375,[355,225]);
 text(s,'Proposed review timeboxes',705,160,490,65,32,c.teal,true);
 text(s,'20–22 Oct 2026\nApplication review',705,255,470,80,27);
 text(s,'23–25 Oct 2026\nQuizzes, UML and report review',705,370,470,85,27);
 text(s,'26–28 Oct 2026\nPresentation and video submission',705,490,470,85,27);
 text(s,'All 18 original items have verification records. The review dates are plans.',70,615,1130,36,24,c.muted);note(s,5);
}
{
 const s=base(6);const rows=(await fs.readFile('docs/evidence/progress.csv','utf8')).trim().split(/\r?\n/).slice(1).map(line=>line.split(','));
 const first=new Date(rows[0][0]);const xs=rows.map(r=>Number(((new Date(r[0])-first)/60000).toFixed(4))),ys=rows.map(r=>Number(r[2]));
 const chart=s.charts.add('scatter',{position:{left:80,top:145,width:1110,height:430},series:[{name:'Remaining original backlog items',xValues:xs,values:ys,smooth:false,line:{fill:c.teal,width:3},marker:{symbol:'circle',size:7,fill:c.teal}}],scatterOptions:{style:'lineWithMarkers'},hasLegend:false,xAxis:{min:0,max:100,majorUnit:20,title:{text:'Elapsed minutes since 19:09 (UTC+08:00)',textStyle:{fontSize:22}},textStyle:{fontSize:21},numberFormatCode:'0',majorGridlines:null},yAxis:{min:0,max:18,majorUnit:3,title:{text:'Remaining backlog items',textStyle:{fontSize:22}},textStyle:{fontSize:21},numberFormatCode:'0',majorGridlines:{fill:c.line,width:1}},chartFill:c.white,plotAreaFill:c.white});
 applyPresentationChartFont(chart,{fontFamily:'Arial'});
 text(s,'20 October 2026: actual intraday checkpoints for the original 18 items',75,595,1120,35,25,c.navy,true);
 text(s,'Item counts have unequal effort. Phase 4 work is outside this original backlog.',75,640,1120,30,22,c.muted);note(s,6);
}
{
 const s=base(7);const boundary=s.shapes.add({geometry:'rect',name:'POS system boundary',position:{left:350,top:155,width:840,height:460},fill:'none',line:{fill:c.line,width:1.5}});
 text(s,'ProjectPos_2026',385,170,730,40,28,c.navy,true);
 const actor=node(s,'<<actor>>\nCashier',75,330,220,110);
 for(const [label,y] of [['Process Sale',235],['Handle Returns',335],['Manage Products',435],['Review Transactions',535]]){const use=node(s,label,575,y,530,65,'ellipse');connect(s,actor,use,'right','left',false);}
 text(s,'The customer supplies goods, cash and receipts; the cashier operates the software.',75,650,1080,28,23,c.muted);note(s,7);
}
{
 const s=base(8);const view=node(s,'Thymeleaf views',70,165,290,80),controller=node(s,'Controllers',475,165,290,80),service=node(s,'Services',475,330,290,80),repository=node(s,'Repositories',475,500,290,80),entity=node(s,'Entities',900,330,285,80),mysql=node(s,'MySQL',900,500,285,80);
 connect(s,view,controller,'right','left');connect(s,controller,service,'bottom','top');connect(s,service,repository,'bottom','top');connect(s,service,entity,'right','left');connect(s,repository,mysql,'right','left');
 text(s,'English forms\nand page rendering',75,285,290,95,27,c.muted);text(s,'Request binding\nand page flow',850,155,340,70,24,c.muted);text(s,'Business validation\nand transaction boundaries',75,430,335,100,27,c.muted);text(s,'JPA mappings\nand domain rules',900,235,290,70,25,c.muted);
 text(s,'Spring Data JPA and Hibernate persist entities and acquire database row locks.',75,625,1110,50,25,c.muted);note(s,8);
}
{
 const s=base(9);const sale=node(s,'Sale\nDate and time\nTotal, cash, change',520,145,270,140),item=node(s,'SaleItem\nName and unit price\nBought quantity\nReturned quantity',520,430,270,150),product=node(s,'Product\nBarcode, name, price\nStock and active state',65,430,310,150),ret=node(s,'ReturnRecord\nQuantity and refund\nReason and time',935,430,280,150);
 item.text.style={typeface:'Arial',fontSize:25,color:c.navy,alignment:'center',verticalAlignment:'middle',insets:8};
 connect(s,sale,item,'bottom','top',false);connect(s,product,item,'right','left',false);connect(s,item,ret,'right','left',false);
 text(s,'1',600,300,40,35,25);text(s,'1..*',580,380,90,35,25);text(s,'contains',690,335,130,40,24,c.muted);
 text(s,'1',390,455,40,35,24);text(s,'0..*',445,455,70,35,24);text(s,'1',810,455,40,35,24);text(s,'0..*',870,455,70,35,24);
 text(s,'One purchased line can support several partial returns.',75,625,1090,40,26,c.muted);note(s,9);
}
{
 const s=base(10);table(s,[['Table','Primary key','Foreign keys and role'],['products','id','Catalog and current stock'],['sales','id','Cash transaction header'],['sale_items','id','sale_id; product_id; original-price snapshot'],['return_records','id','sale_item_id; accepted quantity and refund']],65,155,1150,375,[260,190,700]);
 text(s,'pos_2026_db',75,570,470,45,34,c.teal,true);text(s,'Four verified InnoDB tables',600,575,600,40,28);
 text(s,'Stock updates and saved records commit in one business transaction.',75,630,1100,35,25,c.muted);note(s,10);
}
{
 const s=base(11);text(s,'2 rice + 1 milk',70,170,420,55,34,c.teal,true);text(s,'CNY 16.80',70,255,455,85,64,c.navy,true);
 text(s,'Cash 20.00\nChange 3.20',75,365,460,125,35);text(s,'Recording stock\n10 / 10 becomes 8 / 9',75,535,460,90,29,c.muted);
 await img(s,assets+'/12-receipt-detail.png',565,145,650,500,'Genuine POS-1 receipt showing total 16.80, cash 20.00 and change 3.20');
 text(s,'Verified example POS-1. Live recording creates a separate receipt.',575,650,620,30,19,c.muted);note(s,11);
}
{
 const s=base(12);text(s,'Return 1 rice unit',70,160,1060,55,38,c.teal,true);text(s,'CNY 6.50 refund',75,235,1080,80,58,c.navy,true);
 text(s,'Returnable quantity: 2 becomes 1\nRecording stock: 8 / 9 becomes 9 / 9',75,335,1120,100,31);
 await img(s,'deliverables/presentation/assets/refund-ledger-detail.png',75,455,1125,180,'Unaltered crop of the genuine RET-1 refund ledger');
 text(s,'Verified example RET-1. Excessive and repeated returns are rejected or deduplicated.',75,645,1110,28,21,c.muted);note(s,12);
}
{
 const s=base(13);table(s,[['Executed verification','Observed result'],['Automated tests on H2','21 passed; 0 failures, errors or skips'],['Same tests on real MySQL','21 passed; 0 failures, errors or skips'],['Live browser checks','13 passed; no page JavaScript errors'],['Maven clean package','BUILD SUCCESS; runnable JAR created']],65,155,1150,375,[420,730]);
 text(s,'Includes two-thread sales and returns, invalid cash, stock and return quantities.',75,565,1100,65,27,c.navy,true);text(s,'21 scenarios run on two databases; this is not 42 distinct test scenarios.',75,645,1110,28,23,c.muted);note(s,13);
}
{
 const s=base(14);text(s,'0ShaoYifei / ProjectPos_2026',70,150,1110,55,34,c.teal,true);
 table(s,[['Retained branch','Verified implementation commit'],['feature/product-management','9f587a0'],['feature/process-sale','5183641'],['feature/handle-returns','a1fb0aa'],['feature/documentation','ce62819']],70,245,1130,305,[700,430]);
 text(s,'main 1316a7c contains the verified documentation merge.',75,585,1100,40,28);text(s,'Individual development; preserved initial remote history and feature branches.',75,640,1110,30,23,c.muted);note(s,14);
}
{
 const s=base(15);text(s,'Completed',75,170,500,55,38,c.teal,true);bullets(s,['Cash sale and receipt','Original-price refund','Consistent stock and saved records'],80,255,500,260,30);
 text(s,'Future work',700,170,480,55,38,c.teal,true);bullets(s,['Cashier roles and authentication','Damaged-goods return policy','Dedicated receipt hardware'],705,255,475,260,30);
 text(s,'Thank you',75,590,1000,60,44,c.navy,true);note(s,15);
}

const candidate=path.join(build,'candidate.pptx');await(await PresentationFile.exportPptx(p)).save(candidate);
for(const [i,s]of p.slides.items.entries()){
 const b=await p.export({slide:s,format:'png',scale:1});await fs.writeFile(path.join(build,`slide-${String(i+1).padStart(2,'0')}.png`),new Uint8Array(await b.arrayBuffer()));
}
const version=process.argv[2]||'v2';if(!/^v\d+$/.test(version))throw Error('Use a fresh v-number for each finalized revision');
const finalPath=path.join(workspaceDir,'.local','POS_Presentation-'+version+'.pptx');
await finalizePresentation({workspaceDir,candidatePath:candidate,finalPath,pythonExecutable:'C:/Users/ASUS/.cache/codex-runtimes/codex-primary-runtime/dependencies/python/python.exe',integrityValidatorPath:path.join(SKILL_DIR,'container_tools/inspect_presentation_package_integrity.py'),layoutValidatorPath:path.join(SKILL_DIR,'container_tools/inspect_presentation_layout_geometry.py'),layoutArgs:['--expected-slide-size-emu','12192000,6858000','--validate-bullet-geometry','--validate-heading-fit',...[3,4,5,10,13,14].flatMap(n=>['--require-native-table-slide',String(n)])],explicitTotalSlideCount:15,requiredNativeTableOwnerSlides:[3,4,5,10,13,14],requiredNativeChartOwnerSlides:[6],materializeLiteralChartWorkbooks:true,fontPolicy:{basis:'design',families:['Arial']},verifyArtifactToolImport:true,receiptPath:path.join(build,'validation-'+version+'.json')});
await fs.copyFile(finalPath,path.join(out,'POS_Presentation.pptx'));
console.log('Final editable 15-slide PowerPoint:',path.join(out,'POS_Presentation.pptx'));
