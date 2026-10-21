// Read-only browser checks for local startup; no products or transactions are changed.
const {chromium}=require('C:/Users/ASUS/.cache/codex-runtimes/codex-primary-runtime/dependencies/node/node_modules/playwright');
const fs=require('node:fs');
const assert=require('node:assert/strict');

(async()=>{
 const browser=await chromium.launch({headless:true,executablePath:'C:/Program Files/Google/Chrome/Application/chrome.exe'});
 const errors=[],results=[];
 try{
  const page=await browser.newPage({viewport:{width:1440,height:1050}});
  page.on('pageerror',error=>errors.push(error.message));
  const routes=[
   ['http://127.0.0.1:8080/','Products'],
   ['http://localhost:8080/','Products'],
   ['http://127.0.0.1:8080/products','REC-20261020-RICE'],
   ['http://127.0.0.1:8080/sales/new','Complete sale'],
   ['http://127.0.0.1:8080/sales','Transactions and returns'],
   ['http://127.0.0.1:8080/sales/1','Handle returns']
  ];
  for(const [url,text] of routes){
   const response=await page.goto(url);
   assert.equal(response.status(),200,url);
   const body=await page.locator('body').innerText();
   assert.ok(body.includes(text),`${url} missing expected visible content`);
   if(url.endsWith('/sales/1')){
    for(const amount of ['16.80','20.00','3.20','6.50'])assert.ok(body.includes(amount),amount);
   }
   if(url.endsWith('/products')){
    for(const barcode of ['REC-20261020-RICE','REC-20261020-MILK']){
     const row=await page.getByRole('row').filter({hasText:barcode}).innerText();
     assert.ok(/\t10\tActive/.test(row),`Recording product stock changed: ${barcode}`);
    }
    await page.screenshot({path:'docs/evidence/screenshots/12-startup-fixed.png',fullPage:true});
   }
   results.push({url,status:response.status(),finalUrl:page.url(),expectedContent:text});
  }
  assert.deepEqual(errors,[]);
  const result={recordedAt:new Date().toISOString(),checks:results,pageErrors:errors,mode:'read-only GET navigation; no form submissions',transactionsCreated:0};
  fs.writeFileSync('docs/evidence/2026-10-21-startup-browser-verification.json',JSON.stringify(result,null,2));
  console.log(`PASS: ${results.length} real browser checks, both localhost origins; no JavaScript errors or data changes.`);
 }finally{await browser.close()}
})().catch(error=>{console.error(error.message);process.exitCode=1});
