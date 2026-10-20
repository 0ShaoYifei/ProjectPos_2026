# Phase 4 delivery and publication status

Recorded on 20 October 2026, Asia/Shanghai.

The five requested editable artifacts and five PDFs exist locally in `deliverables/presentation`. Final verification records their actual SHA-256 hashes, page counts and content checks. The application code is unchanged. The final live check returned HTTP 200 at `http://127.0.0.1:8080/`, and Windows service `MySQL80` was Running.

The actual presentation source commit is `e2c7561b9b8929a95253fd16a570fb79b4f4435f` on preserved branch `feature/presentation`. Merge commit `32052f90733e2252653ebdbc43b95e21b2050ad5` integrated it into main. An additional evidence-only commit records this delivery outcome; these checkpoint hashes are not claimed to be the eventual latest main hash.

At 22:59:48 local, real `git ls-remote --heads origin` still showed remote main `1316a7c19873c52b678277958483220ca2ac18dc` and the four previously published feature branches. At 23:00:20 local, the normal push of main and feature/presentation exited 128: Git could not obtain a password with interactive prompting disabled. This command did not complete publication. No credentials were extracted from GitHub Desktop, no login window was opened, and no force-push was attempted.

The student can finish publication in the already authenticated GitHub Desktop: select this repository and main, click **Push origin**, then select feature/presentation and click **Publish branch**. Verify the new commits and branch on the actual GitHub repository afterward. Earlier branch publication is already verified; Phase 4 remote publication is pending.

Actual video recording, review, Bilibili upload and course submission remain the student's actions. All video URL fields remain `[Pending upload]` until a real published URL is supplied. The local Office/PDF delivery files remain excluded from public Git under the established project policy; source files, builders and technical evidence are committed.
