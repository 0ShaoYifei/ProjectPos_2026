# ProjectPos_2026

A small supermarket POS for an individual Software Engineering Practice project. The two core use cases are **Process Sale** and **Handle Returns**. The existing Spring Boot starter was extended in place.

## Run locally

Requirements: Java 17 or later (verified here with Oracle JDK 21), MySQL 8 and internet access for the first Maven wrapper build. Maven 3.8.5 is supplied by the wrapper. The original Spring Boot 4.1.1 parent is retained.

1. Ensure the MySQL database `pos_2026_db` exists. For a new installation: `CREATE DATABASE pos_2026_db CHARACTER SET utf8mb4;`.
2. Set `$env:DB_USERNAME='root'` and `$env:DB_PASSWORD='your_local_password'` in your own PowerShell session. Never commit the value.
3. Run `.\mvnw.cmd spring-boot:run` and open http://127.0.0.1:8080.

The connection URL can be overridden using `DB_URL`, and the port using `PORT`. Hibernate creates missing tables with `ddl-auto=update`; it does not seed or delete application data. The local launcher `scripts/run-local.ps1` also supports the private, ignored password file retained during the initial inspection.

## Use the application

- **Products:** add a unique barcode, name, price and initial stock. Edit stock or deactivate a product. Deactivation keeps sale history available.
- **New sale:** add products and quantities, review the running total, enter cash and complete payment. Stock is rechecked under database locks. The receipt records original product names, prices, totals, cash and change.
- **Transactions and returns:** find a receipt by sale number, select a purchased line, enter the return quantity and reason, and record the refund. Refunds use the original price and cannot exceed the remaining purchased quantity. Resalable returned items are put back into stock. Each return is one product line; repeat for another product.
- Use **Print receipt** for the browser print dialog or Save as PDF.

Prices and payments use CNY and exact `BigDecimal` arithmetic. No separate tax, discount, card payment, login, supplier or accounting integration is implemented. The application binds to localhost by default and is intended for a trusted classroom demonstration. Physical cash collection and refund delivery are cashier responsibilities.

## Tests and build

```powershell
.\mvnw.cmd clean test
.\mvnw.cmd clean package
```

Default automated tests use isolated H2 data. To test real MySQL, create the separate database `pos_2026_test_db`, set `DB_PASSWORD`, and run:

```powershell
.\mvnw.cmd test '-Dspring.profiles.active=mysql-test'
```

The MySQL test profile uses `pos_2026_test_db`, not the application's `pos_2026_db`. It retains generated test records for inspection. Never point this profile at valuable data. The tests cover products, cash sale atomicity, invalid requests, historical prices, partial/full refunds, cumulative over-returns, duplicate requests and two-thread inventory races. `scripts/verify-live.cjs` records browser checks and screenshots; its original verification uses the labelled DEMO products shown in the evidence.

## Structure

- `controller`: web page flow, form binding, session cart and CSRF validation
- `service`: business use cases, validation and transaction boundaries
- `repository`: Spring Data JPA access and pessimistic lock queries
- `entity`: Product, Sale, SaleItem and ReturnRecord
- `exception`: shared business exception
- `src/main/resources/templates`: English Thymeleaf views
- `docs/evidence`: authentic build logs, SQL verification, screenshots, Git records and measured progress
- `deliverables`: quiz answers, technical report, presentation and recording preparation

## Development and submission evidence

Local Git was initialized only after confirming that no repository existed. Feature branches are preserved and merged with merge commits. No earlier commits or historical sprint measurements are claimed. `docs/evidence/progress.csv` measures verified backlog items at actual session checkpoints; it is not a historical daily sprint record. Reference course files and local credentials are excluded from Git. See the report evidence checklist for external submission items still required.

Repository: https://github.com/0ShaoYifei/ProjectPos_2026. GitHub Desktop was used to publish the preserved branches after command-line authentication failed. The initial remote README history remains in the merged graph.

The local `deliverables/quizzes` directory contains both completed quizzes in Word and PDF plus the source map. `deliverables/report` contains the editable report, its 32-page PDF, UML sources/images, measured chart data and evidence checklist. `deliverables/presentation` contains the 15-slide editable English deck, a matching English script, Chinese project explanation and recording guide, 20 bilingual defense questions, and five actual Office PDF exports. Its README explains the learning and recording sequence. Personalized Office/PDF submission files remain local; the public repository contains their reusable builders, source text and supporting technical evidence. Rebuilding documents requires the student's ignored `.local/student-info.json` and original reference documents. Separate recording products were added without resetting earlier transactions. No video has been recorded or uploaded by these preparation steps; its URL remains pending.
