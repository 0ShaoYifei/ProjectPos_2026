# Development log

## 20 October 2026

Inspected source, Maven configuration, assignment and Git state before implementation. Established an 18-item verification backlog. MySQL exists with no tables. Baseline offline test attempt failed on a missing plugin dependency. Historical task completion data is unavailable. Current records begin today; feature increments completed during this session must not be represented as completed multi-day sprints.

Remaining: product management, inventory, cart, totals, payment, change, receipts, transaction history, return lookup, refunds, inventory restoration, validation, business tests, MySQL integration, live HTTP evidence, Git workflow, quizzes and report.

Product increment: implemented product creation and editing, barcode uniqueness, stock and price validation, deactivation and stale edit protection. Wrapper uses available Maven 3.8.5. Actual clean test: 5 tests passed on H2. Shared business exceptions moved to the exception package before integration.

Sales increment: shopping cart, exact decimal totals, cash payment, change, saved receipts, transaction history, stock locks and checkout idempotency implemented. Actual clean test: 13 tests passed. Verified insufficient stock and payment leave database state unchanged, and price changes do not rewrite receipts.

Returns and validation increment: implemented receipt lookup, partial and cumulative returns, original-price refunds, stock restoration, duplicate request protection, English form errors and CSRF rejection. Actual clean test passed 21 tests on H2; actual MySQL profile passed the same 21 tests in pos_2026_test_db. First MySQL invocation had a PowerShell argument parsing failure; quoted retry passed. First browser attempt had a dropdown selector timeout; corrected selector passed all 13 browser checks. Four InnoDB application tables and live sale POS-1/refund RET-1 were verified by SQL. Actual clean package passed 21 tests and generated the runnable JAR. Word PDF conversion was verified after the packaged LibreOffice renderer was unavailable. Remaining measured items: Git workflow finalization, quizzes, report.

Quiz increment: completed all 30 answers using the supplied 960-page course PDF. Preserved exact numbering and question wording, produced a page/section source map, confirmed student details from the prior report at the user's direction, and visually checked all eight exported quiz pages. One unavailable source figure is disclosed; its surrounding text adequately supports the answer. POS repository and new video URLs remain unavailable; older robot-project URLs are not reused.
