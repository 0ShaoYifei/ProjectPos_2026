param([switch]$SlidesOnly)
$ErrorActionPreference='Stop'
$folder=Join-Path (Get-Location) 'deliverables\presentation'
if(!$SlidesOnly){
$word=$null;$document=$null
try{
 $word=New-Object -ComObject Word.Application
 $word.Visible=$false;$word.DisplayAlerts=0
 foreach($name in @('Video_Script_EN','Project_Explanation_ZH','Recording_Guide_ZH','Defense_Questions_Bilingual')){
  $source=Join-Path $folder ($name+'.docx');$pdf=Join-Path $folder ($name+'.pdf')
  $document=$word.Documents.Open($source,$false,$false)
  if($name -eq 'Project_Explanation_ZH'){
   $tocStyle=$document.Styles.Item(-20);$tocStyle.Font.Name='Arial';$tocStyle.Font.NameFarEast='Microsoft YaHei';$tocStyle.Font.Size=10.5
   $tocStyle.ParagraphFormat.SpaceAfter=3;$tocStyle.ParagraphFormat.LineSpacingRule=0
   foreach($toc in $document.TablesOfContents){$toc.Update()}
  }
  $document.Repaginate();$document.Fields.Update() | Out-Null
  foreach($toc in $document.TablesOfContents){$toc.UpdatePageNumbers()}
  $document.Save();$document.ExportAsFixedFormat($pdf,17)
  Write-Output "$name | pages=$($document.ComputeStatistics(2)) | exported actual Word PDF"
  $document.Close(0);[void][Runtime.InteropServices.Marshal]::FinalReleaseComObject($document);$document=$null
 }
}finally{
 if($document){$document.Close(0);[void][Runtime.InteropServices.Marshal]::FinalReleaseComObject($document)}
 if($word){$word.Quit();[void][Runtime.InteropServices.Marshal]::FinalReleaseComObject($word)}
 [GC]::Collect();[GC]::WaitForPendingFinalizers()
}
}

$powerPoint=$null;$deck=$null;$existingCount=0
try{
 $powerPoint=New-Object -ComObject PowerPoint.Application
 $existingCount=$powerPoint.Presentations.Count
 $deck=$powerPoint.Presentations.Open((Join-Path $folder 'POS_Presentation.pptx'),$true,$false,$false)
 $deck.SaveAs((Join-Path $folder 'POS_Presentation.pdf'),32)
 $preview=Join-Path (Get-Location) 'tmp\phase4-powerpoint';[IO.Directory]::CreateDirectory($preview) | Out-Null
 $deck.Export($preview,'PNG',1280,720)
 Write-Output "PowerPoint | slides=$($deck.Slides.Count) | exported actual PDF and slide PNGs without a presentation window"
}finally{
 if($deck){$deck.Close();[void][Runtime.InteropServices.Marshal]::FinalReleaseComObject($deck)}
 if($powerPoint){if($existingCount -eq 0 -and $powerPoint.Presentations.Count -eq 0){$powerPoint.Quit()};[void][Runtime.InteropServices.Marshal]::FinalReleaseComObject($powerPoint)}
 [GC]::Collect();[GC]::WaitForPendingFinalizers()
}
