# 第四阶段交付与使用顺序

本目录已生成并校验五份可编辑材料及五份 PDF。PDF 均由本机 Microsoft Office 实际导出，逐页查看过文字、图表和分页。

| 文件 | 用途 | PDF 页数 |
| --- | --- | --- |
| [Project_Explanation_ZH.docx](Project_Explanation_ZH.docx) / [PDF](Project_Explanation_ZH.pdf) | 先理解业务、真实代码调用、数据库、事务、工程概念和运行方法 | 12 |
| [Defense_Questions_Bilingual.docx](Defense_Questions_Bilingual.docx) / [PDF](Defense_Questions_Bilingual.pdf) | 20 组英语问题、英语回答和中文解析 | 10 |
| [POS_Presentation.pptx](POS_Presentation.pptx) / [PDF](POS_Presentation.pdf) | 15 页英文演示文稿，含真实截图、可编辑 UML、表格及燃尽图；备注包含讲稿与操作提示 | 15 |
| [Video_Script_EN.docx](Video_Script_EN.docx) / [PDF](Video_Script_EN.pdf) | 与每页 PPT 对应的英语口播、屏幕动作和过渡 | 9 |
| [Recording_Guide_ZH.docx](Recording_Guide_ZH.docx) / [PDF](Recording_Guide_ZH.pdf) | 中文录屏步骤、按钮、切换顺序、故障处理及可选中文口播 | 6 |

建议按上表先学习项目、练习问答，再配合 PPT 试读英语讲稿，最后照录屏指南操作。763 个英语口播词与操作合计计划约 7 分 30 秒，属于估计，正式录制前需要自己试读计时。

## 录屏数据

已经通过实际网页新增独立商品 `REC-20261020-RICE` 与 `REC-20261020-MILK`，价格 6.50 和 3.80，核验时库存各为 10。尚未用它们创建销售或退款。原有 DEMO 商品、POS-1 与 RET-1 保留；SQL 结果见 `docs/evidence/phase4-database-verification.log`。

本轮操作：米 2 + 牛奶 1 = 16.80；先展示 16.79 现金不足，再收 20.00、找零 3.20，库存变成 8/9；先尝试退米 3 件被拒，再接受 1 件、退款 6.50，库存变成 9/9。必须使用实际生成的新小票编号。PPT 中截图属于之前真实验证的 POS-1、RET-1。

练习会真正改变库存。下一轮完整演示请用新条码后缀创建另一组商品，不要清库或覆盖历史交易。辅助脚本 `scripts/prepare-recording-data.cjs TAKE2` 只新增缺失商品，不重置已存在商品。

## 证据与重建

逐页讲稿和来源索引在 `presentation_content.json`；问答来源线索在 `defense_content.json`；退款截图裁切范围及原始图像哈希在 `assets/provenance.json`。最终内容与文件哈希记录在 `docs/evidence/phase4-final-verification.json`。本阶段新增工作单独记载，不修改原十八项待办的历史进度。

`scripts/build-phase4-docs.py` 生成四份 Word；`scripts/build-presentation-assets.py` 重建真实截图裁切；`scripts/build-presentation.mjs` 使用 artifact-tool 生成可编辑 PPT；`scripts/export-phase4-office.ps1` 后台导出 PDF；`scripts/verify-phase4.py` 核对最终内容。构建脚本使用当前电脑的已验证运行时路径，换电脑时需调整依赖路径。PPT 每次重新定稿要使用未用过的版本参数，例如 `v6`，保留上一个定稿版本供核对。

可编辑 Office 文件与 PDF 作为本地交付保留，源材料、构建脚本与技术证据纳入 Git。幻灯片的 Git 哈希是开始制作时已核验的历史检查点，后续提交自然会产生新哈希。

## 尚需本人完成

实际录制、检查并上传视频到 Bilibili，然后用真实发布地址替换报告中的 `[Pending upload]`。当前没有生成或上传视频，也没有虚构视频链接。
