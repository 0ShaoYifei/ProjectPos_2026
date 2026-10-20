"""Crop an existing genuine screenshot without editing its recorded contents."""
from pathlib import Path
from PIL import Image
import json, hashlib

out=Path('deliverables/presentation/assets');out.mkdir(parents=True,exist_ok=True)
source=Path('deliverables/report/report_assets/13-return-detail.png')
box=(20,770,780,890)
Image.open(source).crop(box).save(out/'refund-ledger-detail.png')
(out/'provenance.json').write_text(json.dumps({'source':str(source),'source_sha256':hashlib.sha256(source.read_bytes()).hexdigest(),'output':'refund-ledger-detail.png','crop_box':box,'content':'unaltered pixels from genuine earlier return verification'},indent=2),encoding='utf-8')
