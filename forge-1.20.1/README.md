# More Fun Quicksand Reborn — Task 002

Minecraft **1.20.1** · Forge **47.2.0** · ForgeGradle **6.0.24** · Gradle **8.1.1** · Java **17** · modid **mfqm**

仅包含工程基础与普通实体方块 `mfqm:dev_test_block`，**没有流沙物理、氧气、救援、生物或世界生成**。开发测试版本 `0.0.2-dev`，不是完整移植版。

## 构建

安装 JDK 17（本次使用 Eclipse Temurin 17.0.21+9），确认 `JAVA_HOME` 指向 JDK 17，`java -version` 输出 17。

```sh
cd forge-1.20.1
./gradlew --version
./gradlew --no-daemon --console=plain build
python3 tools/verify_project.py build/libs/mfqm-reborn-0.0.2-dev.jar
```

Windows 使用 `gradlew.bat`，并将 `JAVA_HOME` 指向 JDK 17。首次构建需要网络下载 Gradle、Forge、Minecraft、映射和依赖；Wrapper含发行包SHA-256校验。可分发的测试模组在 `build/libs/mfqm-reborn-0.0.2-dev.jar`，`build`会执行reobfJar。普通 `test` 任务无 JUnit 测试（NO-SOURCE）；真正的集成测试见下文。

## 单人客户端

```sh
./gradlew --no-daemon --console=plain runClient
```

需要可用图形桌面和 OpenGL 3.2。启动后在 Mods 页确认 MFQM；创建允许作弊的创造模式世界，执行：

```mcfunction
/give @s mfqm:dev_test_block
/setblock ~2 ~ ~ mfqm:dev_test_block
```

物品也在 Building Blocks 创造栏。名称标注“开发测试方块（无陷落物理）”。选择物品右键放置、破坏确认掉落。默认模型仅引用 Minecraft 自带 `minecraft:block/sand`，不复制任何PNG，不做16/32缩放。它是普通固体Block，既不下沉，也不是原生重力沙块。

## 专用服务器

```sh
./gradlew --no-daemon --console=plain runServer
```

首次会生成 `run/server/eula.txt` 并要求阅读 Minecraft EULA。**由运行者阅读并自行决定是否接受**；只有同意后才将其中 `eula=false` 改为 `eula=true`，再次启动。不要把EULA接受或本地世界提交到仓库。

默认端口25565。本次本地离线开发测试的参数见 `docs/local-test-server.properties.example`，仅绑定127.0.0.1，不能直接用于公开服务器。使用相同版本客户端/Forge/本模组连接 `localhost:25565`；若跨设备访问，按自己的网络策略配置服务器，不要为测试公开端口。控制台输入 `stop` 正常关闭。开发客户端可能使用未验证的开发身份；验证联网登录需要合法Minecraft账户和真实登录会话，不能据此宣称正式认证联机已验收。

客户端、专服和GameTest分别使用 `run/client`、`run/server`、`run/gameTestServer`，避免混用世界和配置。

## 自动集成测试

```sh
./gradlew --no-daemon --console=plain runGameTestServer
```

`FoundationGameTests.registeredBlockPlacesAndDrops` 在本项目自建的3×3×3空结构中检查真实注册的BlockItem关联、在世界放置方块以及生存掉落表。该测试不验证图形、玩家手动操作、网络连接或旧版物理。

`python3 tools/verify_project.py [JAR路径]` 检查JSON引用链、语言、掉落、162行映射、路径重名、48份元数据记录、无旧PNG/OGG、公共代码无客户端类引用，以及构建产物的metadata/Java17 class版本。它不是Minecraft渲染器加载测试。

## 私有旧贴图测试

公开工程和测试JAR使用自带Minecraft纹理作为占位；旧素材授权待确认，不能放进公开源码、提交、发布包。162张旧图的完整草案见 [docs/texture_mapping_draft.csv](docs/texture_mapping_draft.csv)，均为 `integrated=false`。

如本人有权在本地测试，可使用原JAR只读生成**被git忽略**的私有资源包：

```sh
python3 tools/private_legacy_pack.py --jar '/absolute/path/to/original.jar'
# 默认只读复制 quicksand.png；可用 --texture 指定草案中的精确原路径
```

工具校验任务001原JAR哈希，复制所选PNG及存在的mcmeta原始字节，不重新编码/缩放/裁剪；只输出到 `private-resources/legacy-test-pack.zip`，不会写入src或构建JAR。将此ZIP手动复制到 `run/client/resourcepacks/`，在游戏资源包菜单启用，然后在本地查看开发方块。禁用资源包恢复Minecraft沙纹理。不得提交或分享这个私有ZIP。当前草案路径是未来映射，不等于已批准的最终美术方案。

`Mucus_Flowing.png` 原始32×304保留不动；工具不会修复帧布局，加载前须自行检查旧mcmeta与现代解析兼容性。特殊尺寸、透明度、帧序/frametime均不得一刀切转换。默认公开占位并未使用此文件。

## 分端与模块

入口与registry只使用共同侧API；`ClientLifecycle`以 `Dist.CLIENT` 注册。physics/state/network/worldgen/rescue仅package-info边界，没有空壳功能宣称，也没有自定义网络通道。GameTest是开发集成测试入口。

完整实测状态、构建日志和限制见 [TASK_002_REPORT.md](TASK_002_REPORT.md) 和 `logs/task-002/`。旧研究在仓库 `research/task-001/`，本任务不更改该目录。
