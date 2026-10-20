# POS demonstration guide

Use this guide to prepare the later presentation and genuine video. It is a rehearsal plan, not evidence of an uploaded video.

1. Start with the project objectives and the Process Sale / Handle Returns use cases. Explain that this is individual development.
2. Show Products. Create a clearly labelled rehearsal product with a unique barcode, a positive price and known stock. Existing DEMO records are retained verification evidence.
3. Open New sale. Add a quantity within stock, add another product and explain the running total.
4. Submit cash below the total and show the English rejection. Then submit sufficient cash and show the saved receipt, cash and change.
5. Show the updated stock and transaction history. Open the saved sale for returns.
6. Request more than the remaining purchased quantity and show the rejection. Accept a valid partial return with a reason, then show the refund ledger and restored stock.
7. Explain the controller/service/repository/entity responsibilities using the actual UML diagrams. Highlight the atomic sale and return transactions and original-price receipt snapshots.
8. Show the real Maven logs and MySQL verification. The same 21 tests passed on H2 and MySQL, including two-thread inventory races. Do not present additional rehearsals as the original recorded test run.
9. Show GitHub main and the four retained feature branches. Explain that the initial remote README commit was preserved by merging.
10. Close with limitations and future work. Review and upload the real video to Bilibili, then replace `[Pending upload]` on the report cover and in Appendix A with its genuine URL.

The application runs at `http://127.0.0.1:8080`. Use `scripts/run-local.ps1` with `DB_PASSWORD` configured. Rehearsal sales and returns are real transactions and change stock. The original browser verification script assumes its documented seed quantities; reuse it only with an appropriately prepared test dataset.
