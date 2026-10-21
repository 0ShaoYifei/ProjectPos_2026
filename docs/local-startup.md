# 本机网页启动说明

本项目的网页由 Spring Boot 提供，不需要单独启动前端。

1. 确认 MySQL 正在运行，数据库 `pos_2026_db` 存在。
2. 在 IDEA 中打开项目，运行入口为 `com.example.projectpos_2026.ProjectPos2026Application`。运行配置的工作目录应为 `C:\Users\ASUS\IdeaProjects\ProjectPos_2026`，即含 `pom.xml` 的项目根目录。
3. 本机已配置被 Git 忽略的 `.local/database.properties`。程序会自动读取其中的本机数据库密码，因此直接点击 IDEA 绿色运行按钮即可。新电脑可参考 `docs/local-database.properties.example` 创建自己的私有配置，或设置 `DB_PASSWORD` 环境变量。
4. 等待日志显示 `Started ProjectPos2026Application`。仅看到 Spring Boot 标志或 `Tomcat initialized` 还不能证明启动完成。
5. 在浏览器打开 `http://127.0.0.1:8080/` 或 `http://localhost:8080/`，首页会跳转到商品列表。通过 `New sale` 和 `Transactions and returns` 操作销售及退货。

密码读取顺序：如果设置了 `DB_PASSWORD` 环境变量，就以该变量为准；未设置时使用 `.local/database.properties` 中的 `pos.local.database-password`。若在 IDEA 运行配置里手动填过错误或空的 `DB_PASSWORD`，应删除该变量或改为正确值；不要把占位符文字本身当成密码。

2026 年 10 月 21 日用户提供的日志显示 `Access denied for user 'root'@'localhost'`，然后 `Application run failed`、`Stopping service [Tomcat]`，最终退出码为 1。因此网页拒绝连接的直接原因是服务已退出；日志末尾无法推断 Hibernate 方言是数据库登录失败后的后续错误。应先修正密码来源，而不是修改数据库表或添加方言配置。

浏览器提示拒绝连接时，先看启动日志是否已经退出。若提示端口 8080 被占用，应确认是否已有本项目运行，只保留一个实例。不要为了恢复演示清空或重建已有数据库。密码配置不要展示在录屏中，也不要上传 GitHub。
