# Development log

## 20 October 2026

Inspected source, Maven configuration, assignment and Git state before implementation. Established an 18-item verification backlog. MySQL exists with no tables. Baseline offline test attempt failed on a missing plugin dependency. Historical task completion data is unavailable. Current records begin today; feature increments completed during this session must not be represented as completed multi-day sprints.

Remaining: product management, inventory, cart, totals, payment, change, receipts, transaction history, return lookup, refunds, inventory restoration, validation, business tests, MySQL integration, live HTTP evidence, Git workflow, quizzes and report.

Product increment: implemented product creation and editing, barcode uniqueness, stock and price validation, deactivation and stale edit protection. Wrapper uses available Maven 3.8.5. Actual clean test: 5 tests passed on H2. Shared business exceptions moved to the exception package before integration.

Sales increment: shopping cart, exact decimal totals, cash payment, change, saved receipts, transaction history, stock locks and checkout idempotency implemented. Actual clean test: 13 tests passed. Verified insufficient stock and payment leave database state unchanged, and price changes do not rewrite receipts.
