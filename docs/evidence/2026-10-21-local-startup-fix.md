# Local IDEA startup repair — 21 October 2026

## Observed failure

The student's actual Java 18.0.2 startup log ended with MySQL error 1045, `Access denied for user 'root'@'localhost'`, application-context cancellation, Tomcat shutdown and process exit 1. Therefore there was no successful web server to accept the browser connection. The subsequent Hibernate dialect error followed the failed metadata connection; changing the dialect was not the repair.

The saved local credential authenticated successfully against MySQL 8.0.44 and `pos_2026_db`. Inspection of the stored IDEA `ProjectPos2026Application` run configuration found no environment-variable entries. The previous launcher loaded the ignored password file only into its own child process, so it did not configure a direct IDEA run.

## Change

`application.properties` now optionally imports `./.local/database.properties`. The datasource password resolves `DB_PASSWORD` first, then private `pos.local.database-password`. The correct local setting was prepared from the already saved credential without displaying its value. The private files remain under the existing ignored `.local/` directory. A credential-free example and Chinese startup instructions are provided in `docs`. No database password is embedded in tracked source or the application JAR.

Business code, schema design, original products, stock, sale and return records were not changed. Existing environment-based launches still take precedence.

## Actual verification

- The first Maven attempt in the restricted environment stalled after the test JVM reported that its attach pipe could not be opened (Windows error 5). It was interrupted; no test success is claimed for that attempt. Its actual output and diagnostic are retained.
- Re-ran `mvnw.cmd -o clean package` using JDK 21 and the cached Mockito 5.23.0 premain agent through the command-line `argLine`. This avoids the blocked dynamic attach operation without changing the POM or application dependencies. Actual result: **21 tests, 0 failures, 0 errors, 0 skipped; BUILD SUCCESS**. The full invocation is recorded in the verification JSON.
- Started the newly built JAR from the project root using the student's Amazon Corretto **18.0.2**, explicitly without `DB_PASSWORD` in that process environment. It successfully authenticated via the private local configuration and logged `Started ProjectPos2026Application` at **16:28:26 local**. PID at this checkpoint: **10612**.
- Six real headless-browser GET checks passed: `127.0.0.1` homepage, `localhost` homepage, products, new sale, transactions and the existing sale/return receipt. Each returned HTTP 200 and expected page content. No page JavaScript errors occurred. These checks submitted no forms.
- At **16:30:25 local**, read-only SQL still showed all four application tables, original product stocks **29/19/25**, recording stocks **10/10**, original sale total **16.80**, cash **20.00**, change **3.20**, and original refund **6.50**. There remains one sale and one refund.
- A real product-page screenshot is retained as `screenshots/12-startup-fixed.png`. Active process output is excluded from Git; an immutable startup snapshot is retained separately.

The successful background instance is left running for the student to view. Only one instance can use port 8080 at a time. A later direct IDEA launch should use the project root as its working directory and read the same private configuration; it should be started after the existing instance is stopped. Nothing was recorded or uploaded as a demonstration video during this repair.
