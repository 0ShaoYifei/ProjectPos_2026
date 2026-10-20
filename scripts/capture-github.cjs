// Capture the public repository as observed; this script does not authenticate or mutate GitHub.
const {chromium}=require('C:/Users/ASUS/.cache/codex-runtimes/codex-primary-runtime/dependencies/node/node_modules/playwright');
const fs=require('node:fs');
(async()=>{
 const browser=await chromium.launch({headless:true,executablePath:'C:/Program Files/Google/Chrome/Application/chrome.exe',proxy:{server:'http://127.0.0.1:7890'}});
 try {
  const page=await browser.newPage({viewport:{width:1440,height:1050},deviceScaleFactor:1});
  const url='https://github.com/0ShaoYifei/ProjectPos_2026';
  const response=await page.goto(url,{waitUntil:'domcontentloaded',timeout:60000});
  if(response.status()!==200)throw Error('Unexpected GitHub HTTP status '+response.status());
  await page.getByRole('heading',{name:'ProjectPos_2026',exact:true}).waitFor({timeout:15000});
  await page.locator('a[href*="/commit/8cb0771"]').first().waitFor({timeout:45000});
  await page.screenshot({path:'docs/evidence/screenshots/10-github-repository.png',fullPage:false});
  fs.writeFileSync('docs/evidence/github-browser-verification.json',JSON.stringify({recordedAt:new Date().toISOString(),url:page.url(),status:response.status(),title:await page.title(),screenshot:'screenshots/10-github-repository.png'},null,2));
  console.log('Captured real public GitHub repository; HTTP '+response.status());
 } finally {await browser.close();}
})().catch(e=>{console.error(e);process.exitCode=1;});
