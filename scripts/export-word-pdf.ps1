param([Parameter(Mandatory=$true)][string]$InputPath,[Parameter(Mandatory=$true)][string]$OutputPdf,[switch]$RefreshFields)
$ErrorActionPreference='Stop'
$source=(Resolve-Path -LiteralPath $InputPath).Path
$pdf=[IO.Path]::GetFullPath((Join-Path (Get-Location) $OutputPdf))
[IO.Directory]::CreateDirectory([IO.Path]::GetDirectoryName($pdf)) | Out-Null
$word=$null;$document=$null
try{
 $word=New-Object -ComObject Word.Application
 $word.Visible=$false;$word.DisplayAlerts=0
 $document=$word.Documents.Open($source,$false,(-not $RefreshFields.IsPresent))
 if($RefreshFields){ $document.Repaginate(); foreach($toc in $document.TablesOfContents){$toc.Update()}; $document.Fields.Update() | Out-Null; $document.Repaginate(); foreach($toc in $document.TablesOfContents){$toc.UpdatePageNumbers()}; $document.Save() }
 $document.ExportAsFixedFormat($pdf,17)
 Write-Output "Exported $pdf | pages=$($document.ComputeStatistics(2))"
}finally{
 if($document){$document.Close(0);[void][Runtime.InteropServices.Marshal]::FinalReleaseComObject($document)}
 if($word){$word.Quit();[void][Runtime.InteropServices.Marshal]::FinalReleaseComObject($word)}
 [GC]::Collect();[GC]::WaitForPendingFinalizers()
}
