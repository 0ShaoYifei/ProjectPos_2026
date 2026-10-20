param([Parameter(ValueFromRemainingArguments=$true)][string[]]$GitArguments)
$gitExecutable='C:\Users\ASUS\.cache\codex-runtimes\codex-primary-runtime\dependencies\native\git\cmd\git.exe'
$gitLog=Join-Path $PSScriptRoot '..\.local\git-commands-current.log'
$stamp=[DateTimeOffset]::Now.ToOffset([TimeSpan]::FromHours(8)).ToString('o')
Add-Content -LiteralPath $gitLog -Value "`n[$stamp] git $($GitArguments -join ' ')"
& $gitExecutable @GitArguments 2>&1 | Tee-Object -FilePath $gitLog -Append
$code=$LASTEXITCODE
Add-Content -LiteralPath $gitLog -Value "exit=$code"
if($code -ne 0){ throw "Git exited with $code" }
