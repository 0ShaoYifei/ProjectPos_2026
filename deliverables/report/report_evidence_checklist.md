# Report evidence checklist

Project: ProjectPos_2026. Recorded development date: 20 October 2026. This checklist distinguishes completed verification from external coursework submissions.

| Requirement | Evidence and result |
| --- | --- |
| Authoritative assignment | Read `references/Software Engineering Practice Assignment.docx`; the two required use cases and requested models are covered. |
| Mandatory cover | Original cover template used. Logos and fixed OOXML parts preserved byte for byte; see `docs/evidence/report-template-preservation.json`. |
| Personal fields | Name, both student IDs and email taken from the prior report at the student's explicit direction. The prior project's repository/video was not reused. |
| English report | Ten substantive chapters and Appendix A. No Chinese body paragraphs; the original university logo remains unchanged. |
| Editable report and PDF | Word source and actual Microsoft Word PDF export created. Field-based contents and body page numbering included. |
| Use cases and UML | Fully dressed Process Sale and Handle Returns; use case, two SSDs, domain, package, design and internal interaction diagrams. Editable PlantUML and PNG/SVG files are in `report_assets`. |
| Implementation | Existing Spring Boot project extended in place. Product, inventory, cash sales, receipts, transaction lookup and returns implemented. |
| Real tests | 21 H2 tests and the same 21 tests independently on real MySQL passed with no failures/errors/skips. `clean package` also passed all 21 tests. Logs remain in `docs/evidence`. |
| Running application | Actual `spring-boot:run` startup, 13 passing live browser checks and genuine screenshots preserved. Insufficient stock/payment and excessive returns were exercised. |
| Database | MySQL 8.0.44; four InnoDB application tables verified in `pos_2026_db`. Sale POS-1 and return RET-1 checked by SQL. Integration tests use separate `pos_2026_test_db`. |
| Progress and Scrum | Dated log, 18-item backlog and real intraday measurements retained. Report chart and editable CSV freeze the explicitly stated earlier checkpoint. Proposed three-day review timeboxes are identified as plans. No earlier daily burndown or team events invented. |
| GitHub | Actual repository `https://github.com/0ShaoYifei/ProjectPos_2026`. Initial remote README history merged without rewriting. GitHub Desktop successfully published main and four feature branches; `ls-remote` matched all hashes. Real public repository screenshot included. Final documentation merge/push is recorded in the final delivery log. |
| Secrets and public source | Database password resides only in ignored local storage/environment. References, temporary files and personalized submission binaries are excluded from public Git. |
| Quizzes | Exactly 15 original questions answered in each quiz. Both editable Word and four-page PDFs verified; page/section mappings are in `deliverables/quizzes/Quiz_Source_Map.md`. |

## Final verification record

The final PDF page inventory, preserved template parts and visual verification outcome are recorded in `docs/evidence/report-final-verification.json`. Successful output generation alone is not treated as completed visual verification.

## External submission items still required

- Prepare and review the course PowerPoint and genuine demonstration video. The requested current phases prepare the application, quizzes and report; they do not establish a completed video presentation.
- Upload the real presentation/demo to Bilibili and supply its genuine URL. The report cover and appendix explicitly contain `[Pending upload]` until then.
- Student review and course submission remain the student's actions. No unrecorded user acceptance, submission or upload is claimed.

## Source limitations

Historical Git commits and multi-day progress measurements were absent at initial inspection. Actual records start on 20 October 2026. The course PDF was extracted/indexed end to end and all relevant sections were inspected; source mappings identify the exact PDF page convention. One unavailable source figure is disclosed in the quiz map; its surrounding text supports the answer. No unresolved quiz source gap requires supplementary general-knowledge answers.
