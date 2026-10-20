// Create separate recording products through the actual application forms.
// Existing products and transactions are never reset or deleted.
const {chromium}=require('C:/Users/ASUS/.cache/codex-runtimes/codex-primary-runtime/dependencies/node/node_modules/playwright');
const fs=require('node:fs');
(async()=>{
 const tag=process.argv[2]||'20261020';if(!/^[A-Za-z0-9-]{1,20}$/.test(tag))throw Error('Use an alphanumeric recording tag of at most 20 characters.');
 const browser=await chromium.launch({headless:true,executablePath:'C:/Program Files/Google/Chrome/Application/chrome.exe'});
 const results=[];
 try{
  const page=await browser.newPage({viewport:{width:1440,height:1050}});
  const home=await page.goto('http://127.0.0.1:8080/');if(home.status()!==200)throw Error('POS homepage unavailable');
  for(const [suffix,name,price] of [['RICE','Recording Rice 1kg','6.50'],['MILK','Recording Milk 1L','3.80']]){
   const barcode=`REC-${tag}-${suffix}`;await page.goto('http://127.0.0.1:8080/products');
   let row=page.getByRole('row').filter({hasText:barcode});let created=false;
   if(await row.count()===0){
    await page.goto('http://127.0.0.1:8080/products/new');
    await page.getByLabel('Barcode',{exact:true}).fill(barcode);await page.getByLabel('Product name',{exact:true}).fill(name);
    await page.getByLabel('Unit price (CNY)',{exact:true}).fill(price);await page.getByLabel('Stock quantity',{exact:true}).fill('10');
    await page.getByRole('button',{name:'Save product',exact:true}).click();await page.waitForURL('**/products');
    row=page.getByRole('row').filter({hasText:barcode});created=true;
   }
   const edit=await row.getByRole('link',{name:'Edit',exact:true}).getAttribute('href');
   results.push({barcode,name,expectedUnitPrice:price,expectedInitialStock:10,created,observedRow:await row.innerText(),editPath:edit});
  }
  await page.screenshot({path:'docs/evidence/screenshots/11-recording-products.png',fullPage:true});
  const record={recordedAt:new Date().toISOString(),baseUrl:'http://127.0.0.1:8080',tag,products:results,transactionsCreated:0,existingDataReset:false};
  fs.writeFileSync('docs/evidence/phase4-recording-data.json',JSON.stringify(record,null,2));console.log(JSON.stringify(record,null,2));
 }finally{await browser.close();}
})().catch(e=>{console.error(e);process.exitCode=1;});
