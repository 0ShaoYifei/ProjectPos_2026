$ErrorActionPreference='Stop'
Set-Location (Join-Path $PSScriptRoot '..')
if(!$env:DB_PASSWORD -and (Test-Path -LiteralPath '.local\database-password.txt')){
 $env:DB_PASSWORD=[IO.File]::ReadAllText((Join-Path (Get-Location) '.local\database-password.txt'))
}
if(!$env:DB_PASSWORD){throw 'Set the DB_PASSWORD environment variable before running the application.'}
& .\mvnw.cmd spring-boot:run
exit $LASTEXITCODE
