$ErrorActionPreference='Stop'
$previousPassword=$env:MYSQL_PWD
try {
 if(!$env:DB_PASSWORD){$env:DB_PASSWORD=(Get-Content -Raw .local\database-password.txt).Trim();$loadedLocalPassword=$true}
 $env:MYSQL_PWD=$env:DB_PASSWORD
 $query=@'
SELECT VERSION(), DATABASE(), NOW();
SHOW TABLES;
SELECT id,barcode,price,stock,active+0 AS active FROM products ORDER BY id;
SELECT id,total,cash_paid,change_due FROM sales ORDER BY id;
SELECT id,sale_id,product_name,unit_price,quantity,returned_quantity FROM sale_items ORDER BY id;
SELECT id,sale_item_id,quantity,refund,reason FROM return_records ORDER BY id;
SELECT TABLE_NAME,ENGINE FROM information_schema.TABLES WHERE TABLE_SCHEMA=DATABASE() ORDER BY TABLE_NAME;
'@
 & 'C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe' --user=root --host=127.0.0.1 --database=pos_2026_db --batch --execute=$query 2>&1 | Tee-Object -FilePath docs\evidence\phase4-database-verification.log
 if($LASTEXITCODE -ne 0){throw 'Read-only MySQL verification failed'}
} finally {
 if($null -eq $previousPassword){Remove-Item Env:\MYSQL_PWD -ErrorAction SilentlyContinue}else{$env:MYSQL_PWD=$previousPassword}
 if($loadedLocalPassword){Remove-Item Env:\DB_PASSWORD -ErrorAction SilentlyContinue}
}
