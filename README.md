# MFQM Reborn

More Fun Quicksand Mod 的 Minecraft Java 1.20.1 Forge 移植研究项目。

当前已有研究、Forge 工程和三核心材料的近似实现。原代码及素材的许可证与再发布授权仍待核查；公开工程不分发旧素材。

## 任务 001：只读逆向盘点与移植设计

[报告导航与审计说明](research/task-001/README.md) · [六份报告](research/task-001/reports) · [字节码及审计证据](research/task-001/evidence)

本次仅提交研究文档与分析证据。未包含原始 JAR、PNG、OGG，也未创建或发布新版 Mod。

## 任务 002：Forge 1.20.1 最小工程

[工程与启动说明](forge-1.20.1/README.md) · [完成报告与实测边界](forge-1.20.1/TASK_002_REPORT.md) · [完整构建/测试日志](forge-1.20.1/logs/task-002) · [开发测试 JAR](deliverables/task-002/mfqm-reborn-0.0.2-dev.jar)

仅包含开发方块与工程基础；没有流沙物理。默认使用 Minecraft 自带沙纹理，不包含旧作者图片/音频。

## 任务 003：三核心材料与服务端陷落机制

[完成报告与验证边界](forge-1.20.1/TASK_003_REPORT.md) · [证据追踪/差异表](forge-1.20.1/docs/task-003/EVIDENCE_TRACEABILITY.md) · [重现步骤](forge-1.20.1/docs/task-003/REPRODUCE.md) · [日志](forge-1.20.1/logs/task-003) · [开发测试 JAR](deliverables/task-003/mfqm-reborn-0.0.3-dev.jar)

普通流沙、泥浆、松软流沙具有独立接触/阻力/支撑行为；服务端聚合状态、耗氧、伤害和生命周期。属于任务001静态证据指导下的近似实现，未取得1.7.10实机曲线，不宣称原版数值一致。默认占位为 Minecraft 自带16×16风格；没有使用AI生成贴图。任务004未开始。
