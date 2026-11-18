# 全量 Block 实装矩阵

基线 main c399b3b，静态 registerBlock 调用逐项复核 **49**，另有开发方块1个；13个 Fluid 名称单列，不计作额外旧 Block。无旧实例 registry dump，数量代表可证实调用，不承诺旧环境加载结果。完整属性、缺口和置信度见 [CSV](block-implementation-matrix.csv) 与 [证据数据](catalog-evidence.json)。

Checkpoint A 只对注册和基础类别负责；PARTIAL 不代表完整复刻。所有 registerBlock 使用默认或显式 ItemBlock 类，未见 null ItemBlock；技术方块保留物品形式但隐藏创造栏。无新增自然生成。

|旧ID / 类|现代ID|类别；形状/碰撞|状态|缺口|
|---|---|---|---|---|
|Mud / BlockMud|`mfqm:mud`|SINKING; h=1; none|PARTIAL|Legacy curve/meta, special drops and interactions need per-class parity validation; vanilla texture placeholder.|
|Bog / BlockBog|`mfqm:bog`|VISCOUS; h=1; none|PARTIAL|Legacy curve/meta, special drops and interactions need per-class parity validation; vanilla texture placeholder.|
|SoftSnow / BlockSoftSnow|`mfqm:soft_snow`|SINKING; h=1; none|PARTIAL|Legacy curve/meta, special drops and interactions need per-class parity validation; vanilla texture placeholder.|
|DryQuicksand / BlockSand|`mfqm:dry_quicksand`|VISCOUS; h=1; none|PARTIAL|Legacy curve/meta, special drops and interactions need per-class parity validation; vanilla texture placeholder.|
|SoftQuicksand / BlockSoftQuicksand|`mfqm:soft_quicksand`|SINKING; h=0.85; none|PARTIAL|Legacy curve/meta, special drops and interactions need per-class parity validation; vanilla texture placeholder.|
|Morass / BlockMorass|`mfqm:morass`|SINKING; h=1; none|PARTIAL|Legacy curve/meta, special drops and interactions need per-class parity validation; vanilla texture placeholder.|
|WetPeat / BlockWetPeat|`mfqm:wet_peat`|SINKING; h=1; none|PARTIAL|Legacy curve/meta, special drops and interactions need per-class parity validation; vanilla texture placeholder.|
|HPeat / BlockPeat|`mfqm:h_peat`|SOLID; h=1; solid|PARTIAL|Legacy curve/meta, special drops and interactions need per-class parity validation; vanilla texture placeholder.|
|BrownClay / BlockBrownClay|`mfqm:brown_clay`|SINKING; h=1; none|PARTIAL|Legacy curve/meta, special drops and interactions need per-class parity validation; vanilla texture placeholder.|
|Wax / BlockWax|`mfqm:wax`|SINKING; h=1; none|PARTIAL|Legacy curve/meta, special drops and interactions need per-class parity validation; vanilla texture placeholder.|
|Quicksand / BlockQuicksand|`mfqm:quicksand`|SINKING; h=1; none|PARTIAL|Legacy curve/meta, special drops and interactions need per-class parity validation; vanilla texture placeholder.|
|SandstoneTrapBlock / BlockSandstoneTrap|`mfqm:sandstone_trap`|TRAP; h=1; solid|PARTIAL|Legacy curve/meta, special drops and interactions need per-class parity validation; vanilla texture placeholder.|
|JungleQuicksand / BlockJungleQuicksand|`mfqm:jungle_quicksand`|VISCOUS; h=1; none|PARTIAL|Legacy curve/meta, special drops and interactions need per-class parity validation; vanilla texture placeholder.|
|LiquidMire0 / BlockLiquidMire|`mfqm:liquid_mire`|VISCOUS; h=1; none|PARTIAL|Legacy curve/meta, special drops and interactions need per-class parity validation; vanilla texture placeholder.|
|LiquidMire1 / BlockStableLiquidMire|`mfqm:stable_liquid_mire`|VISCOUS; h=1; none|PARTIAL|Legacy curve/meta, special drops and interactions need per-class parity validation; vanilla texture placeholder.|
|SinkyLiquid / BlockSinkyLiquid|`mfqm:sinky_liquid`|VISCOUS; h=1; none|PARTIAL|Legacy curve/meta, special drops and interactions need per-class parity validation; vanilla texture placeholder.|
|SinkingSlime / BlockSlime|`mfqm:sinking_slime`|VISCOUS; h=1; none|PARTIAL|Legacy curve/meta, special drops and interactions need per-class parity validation; vanilla texture placeholder.|
|Mucus / BlockMucus|`mfqm:mucus`|VISCOUS; h=1; none|PARTIAL|Legacy curve/meta, special drops and interactions need per-class parity validation; vanilla texture placeholder.|
|Mire / BlockMire|`mfqm:mire`|SINKING; h=1; none|PARTIAL|Legacy curve/meta, special drops and interactions need per-class parity validation; vanilla texture placeholder.|
|Moor / BlockMoor|`mfqm:moor`|SOLID; h=1; solid|PARTIAL|Legacy curve/meta, special drops and interactions need per-class parity validation; vanilla texture placeholder.|
|HardenedClay / BlockHardenedClay|`mfqm:hardened_clay`|SOLID; h=1; solid|PARTIAL|Legacy curve/meta, special drops and interactions need per-class parity validation; vanilla texture placeholder.|
|SinkingClay / BlockSinkingClay|`mfqm:sinking_clay`|SINKING; h=1; none|PARTIAL|Legacy curve/meta, special drops and interactions need per-class parity validation; vanilla texture placeholder.|
|TangleRootMoss / BlockMoss|`mfqm:tangle_root_moss`|SINKING; h=1; none|PARTIAL|Legacy curve/meta, special drops and interactions need per-class parity validation; vanilla texture placeholder.|
|DenseWebbing / BlockDenseWeb|`mfqm:dense_webbing`|SINKING; h=1; none|PARTIAL|Legacy curve/meta, special drops and interactions need per-class parity validation; vanilla texture placeholder.|
|Tar / BlockTar|`mfqm:tar`|VISCOUS; h=1; none|PARTIAL|Legacy curve/meta, special drops and interactions need per-class parity validation; vanilla texture placeholder.|
|Larvae / BlockLarvae|`mfqm:larvae`|DEPENDENT; h=1; solid|PARTIAL|TileEntity TileEntityLarvae and its associated entity/tool interactions are not implemented yet.|
|CorruptedSand / BlockCorruptedSand|`mfqm:corrupted_sand`|SINKING; h=1; none|PARTIAL|Legacy curve/meta, special drops and interactions need per-class parity validation; vanilla texture placeholder.|
|SwallowingFlesh / BlockSwallowingFlesh|`mfqm:swallowing_flesh`|SINKING; h=1; none|PARTIAL|Legacy curve/meta, special drops and interactions need per-class parity validation; vanilla texture placeholder.|
|Acid / BlockAcid|`mfqm:acid`|VISCOUS; h=1; none|PARTIAL|Legacy curve/meta, special drops and interactions need per-class parity validation; vanilla texture placeholder.|
|Slurry / BlockSlurry|`mfqm:slurry`|VISCOUS; h=1; none|PARTIAL|Legacy curve/meta, special drops and interactions need per-class parity validation; vanilla texture placeholder.|
|Gas / BlockGas|`mfqm:gas`|GAS; h=1; none|REGISTERED_ONLY|Gas spread/explosion/effect parity not implemented; non-solid temporary gas placeholder only.|
|SoftGravel / BlockSoftGravel|`mfqm:soft_gravel`|SINKING; h=1; none|PARTIAL|Legacy curve/meta, special drops and interactions need per-class parity validation; vanilla texture placeholder.|
|Honey / BlockHoney|`mfqm:honey`|VISCOUS; h=1; none|PARTIAL|Legacy curve/meta, special drops and interactions need per-class parity validation; vanilla texture placeholder.|
|SolidHoney / BlockSolidHoney|`mfqm:solid_honey`|SOLID; h=1; inset .025|PARTIAL|Legacy curve/meta, special drops and interactions need per-class parity validation; vanilla texture placeholder.|
|Honeycomb / BlockHoneycomb|`mfqm:honeycomb`|SOLID; h=1; solid|PARTIAL|Legacy curve/meta, special drops and interactions need per-class parity validation; vanilla texture placeholder.|
|LiquidChocolate / BlockLiquidChocolate|`mfqm:liquid_chocolate`|VISCOUS; h=1; none|PARTIAL|Legacy curve/meta, special drops and interactions need per-class parity validation; vanilla texture placeholder.|
|ChocolateBlock / BlockChocolate|`mfqm:chocolate`|SOLID; h=1; solid|PARTIAL|Legacy curve/meta, special drops and interactions need per-class parity validation; vanilla texture placeholder.|
|SinkingRug / BlockSinkingWool|`mfqm:sinking_rug`|SINKING; h=1; none|PARTIAL|Legacy curve/meta, special drops and interactions need per-class parity validation; vanilla texture placeholder.|
|LureBlock / BlockLure|`mfqm:lure_block`|DEPENDENT; h=1; solid|PARTIAL|TileEntity TileEntityLure and its associated entity/tool interactions are not implemented yet.|
|BlossomBlock / BlockBlossom|`mfqm:blossom`|DEPENDENT; h=1; solid|PARTIAL|TileEntity TileEntityBlossom and its associated entity/tool interactions are not implemented yet.|
|BlossomBlockSlab / BlockBlossomSlab|`mfqm:blossom_slab`|SLAB; h=0.5; slab|PARTIAL|Legacy curve/meta, special drops and interactions need per-class parity validation; vanilla texture placeholder.|
|TempVore / BlockVoreHole|`mfqm:temp_vore`|SINKING; h=1; none|PARTIAL|Legacy curve/meta, special drops and interactions need per-class parity validation; vanilla texture placeholder.|
|MeatWall / BlockMeat|`mfqm:meat_wall`|DEPENDENT; h=1; solid|PARTIAL|TileEntity TileEntityMeat and its associated entity/tool interactions are not implemented yet.|
|TempMeat / BlockMeatHole|`mfqm:temp_meat`|SINKING; h=1; none|PARTIAL|Legacy curve/meta, special drops and interactions need per-class parity validation; vanilla texture placeholder.|
|WaxWood / BlockWaxWood|`mfqm:wax_wood`|SOLID; h=1; solid|PARTIAL|Legacy curve/meta, special drops and interactions need per-class parity validation; vanilla texture placeholder.|
|CustomLilyPad / BlockCustomLilyPad|`mfqm:custom_lily_pad`|LILY; h=0.0625; vanilla lily|PARTIAL|Legacy curve/meta, special drops and interactions need per-class parity validation; vanilla texture placeholder.|
|MoorGrass / BlockMoorGrass|`mfqm:moor_grass`|PLANT; h=1; none|PARTIAL|Legacy curve/meta, special drops and interactions need per-class parity validation; vanilla texture placeholder.|
|Tendrils / BlockTent|`mfqm:tendrils`|PLANT; h=1; none|PARTIAL|Legacy curve/meta, special drops and interactions need per-class parity validation; vanilla texture placeholder.|
|LeavesPile / BlockLeaves|`mfqm:leaves_pile`|LEAVES; h=0.015625; none|PARTIAL|Legacy curve/meta, special drops and interactions need per-class parity validation; vanilla texture placeholder.|

## 13个旧 Fluid：独立核对

|旧 Fluid 名称|旧 Block / 现代关系|当前状态|
|---|---|---|
|Bog|BlockBog / `mfqm:bog`|A: 尚无现代 FluidType/Fluid/桶；静态黏稠方块不算流体完成|
|SandFluid|BlockSand / `mfqm:dry_quicksand`|A: 尚无现代 FluidType/Fluid/桶；静态黏稠方块不算流体完成|
|JungleQuicksand|BlockJungleQuicksand / `mfqm:jungle_quicksand`|A: 尚无现代 FluidType/Fluid/桶；静态黏稠方块不算流体完成|
|LiquidMire|BlockLiquidMire / `mfqm:liquid_mire`|A: 尚无现代 FluidType/Fluid/桶；静态黏稠方块不算流体完成|
|LiquidMireS|BlockStableLiquidMire / `mfqm:stable_liquid_mire`|A: 尚无现代 FluidType/Fluid/桶；静态黏稠方块不算流体完成|
|SinkyFluid|BlockSinkyLiquid / `mfqm:sinky_liquid`|A: 尚无现代 FluidType/Fluid/桶；静态黏稠方块不算流体完成|
|SlimeFluid|BlockSlime / `mfqm:sinking_slime`|A: 尚无现代 FluidType/Fluid/桶；静态黏稠方块不算流体完成|
|MucusFluid|BlockMucus / `mfqm:mucus`|A: 尚无现代 FluidType/Fluid/桶；静态黏稠方块不算流体完成|
|Tar|BlockTar / `mfqm:tar`|A: 尚无现代 FluidType/Fluid/桶；静态黏稠方块不算流体完成|
|AcidFluid|BlockAcid / `mfqm:acid`|A: 尚无现代 FluidType/Fluid/桶；静态黏稠方块不算流体完成|
|Slurry|BlockSlurry / `mfqm:slurry`|A: 尚无现代 FluidType/Fluid/桶；静态黏稠方块不算流体完成|
|Honey|BlockHoney / `mfqm:honey`|A: 尚无现代 FluidType/Fluid/桶；静态黏稠方块不算流体完成|
|LiquidChocolate|BlockLiquidChocolate / `mfqm:liquid_chocolate`|A: 尚无现代 FluidType/Fluid/桶；静态黏稠方块不算流体完成|
