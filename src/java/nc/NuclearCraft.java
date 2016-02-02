package nc;

import java.io.File;

import nc.armour.BoronArmour;
import nc.armour.BronzeArmour;
import nc.armour.DUArmour;
import nc.armour.ToughArmour;
import nc.block.accelerator.BlockElectromagnet;
import nc.block.accelerator.BlockSupercooler;
import nc.block.accelerator.BlockSynchrotron;
import nc.block.basic.BlockBlock;
import nc.block.basic.BlockMachineBlock;
import nc.block.basic.BlockOre;
import nc.block.basic.NCBlocks;
import nc.block.crafting.BlockNuclearWorkspace;
import nc.block.fluid.BlockHelium;
import nc.block.fluid.FluidHelium;
import nc.block.generator.BlockFissionReactor;
import nc.block.generator.BlockFusionReactor;
import nc.block.generator.BlockFusionReactorBlock;
import nc.block.generator.BlockRTG;
import nc.block.generator.BlockReactionGenerator;
import nc.block.generator.BlockSolarPanel;
import nc.block.generator.BlockWRTG;
import nc.block.itemblock.ItemBlockBlock;
import nc.block.itemblock.ItemBlockOre;
import nc.block.machine.BlockCollector;
import nc.block.machine.BlockCooler;
import nc.block.machine.BlockCrusher;
import nc.block.machine.BlockElectricCrusher;
import nc.block.machine.BlockElectricFurnace;
import nc.block.machine.BlockElectrolyser;
import nc.block.machine.BlockFactory;
import nc.block.machine.BlockFurnace;
import nc.block.machine.BlockHastener;
import nc.block.machine.BlockHeliumExtractor;
import nc.block.machine.BlockIoniser;
import nc.block.machine.BlockIrradiator;
import nc.block.machine.BlockNuclearFurnace;
import nc.block.machine.BlockOxidiser;
import nc.block.machine.BlockSeparator;
import nc.block.nuke.BlockNuke;
import nc.block.nuke.BlockNukeExploding;
import nc.block.quantum.BlockSimpleQuantum;
import nc.block.reactor.BlockBlastBlock;
import nc.block.reactor.BlockCellBlock;
import nc.block.reactor.BlockCoolerBlock;
import nc.block.reactor.BlockGraphiteBlock;
import nc.block.reactor.BlockReactorBlock;
import nc.block.reactor.BlockSpeedBlock;
import nc.block.reactor.BlockTubing1;
import nc.block.reactor.BlockTubing2;
import nc.entity.EntityBullet;
import nc.entity.EntityNuclearGrenade;
import nc.entity.EntityNuclearMonster;
import nc.entity.EntityNukePrimed;
import nc.entity.EntityPaul;
import nc.gui.GuiHandler;
import nc.handler.EntityHandler;
import nc.handler.FuelHandler;
import nc.item.ItemEnderChest;
import nc.item.ItemFuel;
import nc.item.ItemMaterial;
import nc.item.ItemNuclearGrenade;
import nc.item.ItemPart;
import nc.item.ItemPistol;
import nc.item.ItemToughBow;
import nc.item.NCAxe;
import nc.item.NCHoe;
import nc.item.NCItems;
import nc.item.NCPaxel;
import nc.item.NCPickaxe;
import nc.item.NCShovel;
import nc.item.NCSword;
import nc.proxy.CommonProxy;
import nc.tile.accelerator.TileElectromagnet;
import nc.tile.accelerator.TileSupercooler;
import nc.tile.accelerator.TileSynchrotron;
import nc.tile.crafting.TileNuclearWorkspace;
import nc.tile.generator.TileFissionReactor;
import nc.tile.generator.TileFusionReactor;
import nc.tile.generator.TileFusionReactorBlock;
import nc.tile.generator.TileRTG;
import nc.tile.generator.TileReactionGenerator;
import nc.tile.generator.TileSolarPanel;
import nc.tile.generator.TileWRTG;
import nc.tile.machine.TileCollector;
import nc.tile.machine.TileCooler;
import nc.tile.machine.TileCrusher;
import nc.tile.machine.TileElectricCrusher;
import nc.tile.machine.TileElectricFurnace;
import nc.tile.machine.TileElectrolyser;
import nc.tile.machine.TileFactory;
import nc.tile.machine.TileFurnace;
import nc.tile.machine.TileHastener;
import nc.tile.machine.TileHeliumExtractor;
import nc.tile.machine.TileIoniser;
import nc.tile.machine.TileIrradiator;
import nc.tile.machine.TileNuclearFurnace;
import nc.tile.machine.TileOxidiser;
import nc.tile.machine.TileSeparator;
import nc.tile.other.TileTubing1;
import nc.tile.other.TileTubing2;
import nc.tile.quantum.TileSimpleQuantum;
import nc.worldgen.OreGen;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = NuclearCraft.modid, version = NuclearCraft.version)

public class NuclearCraft {
	public static final String modid = "NuclearCraft";
	public static final String version = "1.6c";
	
	public static final CreativeTabs tabNC = new CreativeTabs("tabNC") {
		// Creative Tab Shown Item
		public Item getTabIconItem() {
			return NCItems.tabItem;
		}
	};
	
		// Tool Materials
	public static final ToolMaterial Bronze = EnumHelper.addToolMaterial("Bronze", 2, 300, 8.0F, 2.0F, 12);
	public static final ToolMaterial ToughAlloy = EnumHelper.addToolMaterial("ToughAlloy", 4, 2500, 14.0F, 10.0F, 10);
	public static final ToolMaterial ToughPaxel = EnumHelper.addToolMaterial("ToughPaxel", 4, 15000, 15.0F, 12.0F, 10);
	public static final ToolMaterial dU = EnumHelper.addToolMaterial("dU", 5, 6000, 21.0F, 10.0F, 50);
	public static final ToolMaterial dUPaxel = EnumHelper.addToolMaterial("dUPaxel", 5, 36000, 22.0F, 12.0F, 40);
	public static final ToolMaterial Boron = EnumHelper.addToolMaterial("Boron", 2, 1200, 10.0F, 3.0F, 5);
	
	public static final ArmorMaterial ToughArmorMaterial = EnumHelper.addArmorMaterial("ToughArmorMaterial", 50, new int [] {4, 8, 5, 3}, 10);
	public static final ArmorMaterial BoronArmorMaterial = EnumHelper.addArmorMaterial("BoronArmorMaterial", 30, new int [] {3, 7, 5, 3}, 5);
	public static final ArmorMaterial BronzeArmorMaterial = EnumHelper.addArmorMaterial("BronzeArmorMaterial", 20, new int [] {2, 6, 5, 2}, 9);
	public static final ArmorMaterial dUArmorMaterial = EnumHelper.addArmorMaterial("dUArmorMaterial", 85, new int [] {5, 9, 5, 4}, 40);
	
		// Mod Checker
	public static boolean isIC2Loaded;
	
		// Mod Hooks
	public static ModRecipes IC2Hook;
	
	@Instance(modid)
	public static NuclearCraft instance;
	
	public static final int guiIdNuclearFurnace = 0;
	public static final int guiIdFurnace = 1;
	public static final int guiIdCrusher = 2;
	public static final int guiIdElectricCrusher = 3;
	public static final int guiIdElectricFurnace = 4;
	public static final int guiIdReactionGenerator = 5;
	public static final int guiIdSeparator = 6;
	public static final int guiIdHastener = 7;
	public static final int guiIdFissionReactorGraphite = 8;
	public static final int guiIdNuclearWorkspace = 9;
	public static final int guiIdCollector = 10;
	public static final int guiIdFusionReactor = 11;
	public static final int guiIdElectrolyser = 12;
	public static final int guiIdOxidiser = 13;
	public static final int guiIdIoniser = 14;
	public static final int guiIdIrradiator = 15;
	public static final int guiIdCooler = 16;
	public static final int guiIdFactory = 17;
	public static final int guiIdHeliumExtractor = 18;
	public static final int guiIdSynchrotron = 19;
	
	// Config File
	public static int nuclearFurnaceCookSpeed;
	public static int nuclearFurnaceCookEfficiency;
	public static int metalFurnaceCookSpeed;
	public static int metalFurnaceCookEfficiency;
	public static int crusherCrushSpeed;
	public static int crusherCrushEfficiency;
	public static int electricCrusherCrushSpeed;
	public static int electricFurnaceSmeltSpeed;
	public static int separatorSpeed;
	public static int hastenerSpeed;
	public static int collectorSpeed;
	public static int electrolyserSpeed;
	public static int oxidiserSpeed;
	public static int ioniserSpeed;
	public static int irradiatorSpeed;
	public static int coolerSpeed;
	public static int factorySpeed;
	public static int heliumExtractorSpeed;
	
	public static boolean oreGenPitchblende;
	public static int oreSizePitchblende;
	public static int oreRarityPitchblende;
	public static boolean oreGenCopper;
	public static int oreSizeCopper;
	public static int oreRarityCopper;
	public static boolean oreGenTin;
	public static int oreSizeTin;
	public static int oreRarityTin;
	public static boolean oreGenLead;
	public static int oreSizeLead;
	public static int oreRarityLead;
	public static boolean oreGenSilver;
	public static int oreSizeSilver;
	public static int oreRaritySilver;
	public static boolean oreGenThorium;
	public static int oreSizeThorium;
	public static int oreRarityThorium;
	public static boolean oreGenLithium;
	public static int oreSizeLithium;
	public static int oreRarityLithium;
	public static boolean oreGenBoron;
	public static int oreSizeBoron;
	public static int oreRarityBoron;
	public static boolean oreGenMagnesium;
	public static int oreSizeMagnesium;
	public static int oreRarityMagnesium;
	public static boolean oreGenPlutonium;
	public static int oreSizePlutonium;
	public static int oreRarityPlutonium;
	
	public static int reactionGeneratorRF;
	public static int reactionGeneratorEfficiency;
	public static int fissionRF;
	public static int fissionEfficiency;
	public static boolean nuclearMeltdowns;
	public static int fusionRF;
	public static int fusionEfficiency;
	public static int RTGRF;
	public static int WRTGRF;
	public static int solarRF;
	
	public static int colliderRF;
	public static int colliderProduction;
	public static int synchrotronRF;
	public static int synchrotronProduction;
	public static int electromagnetRF;
	public static int electromagnetHe;
	public static int acceleratorProduction;
	
	public static boolean enablePaul;
	public static boolean enableNuclearMonster;
	
	public static boolean enableNukes;
	public static boolean enableLoot;
	public static int lootModifier;
	
	//Armor
	public static int toughHelmID;
	public static int toughChestID;
	public static int toughLegsID;
	public static int toughBootsID;
	
	public static int boronHelmID;
	public static int boronChestID;
	public static int boronLegsID;
	public static int boronBootsID;
	
	public static int bronzeHelmID;
	public static int bronzeChestID;
	public static int bronzeLegsID;
	public static int bronzeBootsID;
	
	public static int dUHelmID;
	public static int dUChestID;
	public static int dULegsID;
	public static int dUBootsID;
	
	public static Fluid liquidHelium;
	
	@SidedProxy(clientSide = "nc.proxy.ClientProxy", serverSide = "nc.proxy.CommonProxy")
	public static CommonProxy NCProxy;
	
	public static final Material liquidhelium = (new MaterialLiquid(MapColor.tntColor));
	public static DamageSource heliumfreeze = (new DamageSource("heliumfreeze")).setDamageBypassesArmor();
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {	
		//config
		Configuration config = new Configuration(new File("config/NuclearCraft/NCConfig.cfg"));
		config.load();
		
		oreGenCopper = config.getBoolean("Generation", "0.0: Copper Ore", true, "");
		oreSizeCopper = config.getInt("Chunk Size", "0.0: Copper Ore", 8, 1, 100, "");
		oreRarityCopper = config.getInt("Gen Rate", "0.0: Copper Ore", 12, 1, 100, "");
		oreGenTin = config.getBoolean("Generation", "0.1: Tin Ore", true, "");
		oreSizeTin = config.getInt("Chunk Size", "0.1: Tin Ore", 8, 1, 100, "");
		oreRarityTin = config.getInt("Gen Rate", "0.1: Tin Ore", 11, 1, 100, "");
		oreGenLead = config.getBoolean("Generation", "0.2: Lead Ore", true, "");
		oreSizeLead = config.getInt("Chunk Size", "0.2: Lead Ore", 7, 1, 100, "");
		oreRarityLead = config.getInt("Gen Rate", "0.2: Lead Ore", 11, 1, 100, "");
		oreGenSilver = config.getBoolean("Generation", "0.3: Silver Ore", true, "");
		oreSizeSilver = config.getInt("Chunk Size", "0.3: Silver Ore", 7, 1, 100, "");
		oreRaritySilver = config.getInt("Gen Rate", "0.3: Silver Ore", 10, 1, 100, "");
		oreGenPitchblende = config.getBoolean("Generation", "0.4: Uranium Ore", true, "");
		oreSizePitchblende = config.getInt("Chunk Size", "0.4: Uranium Ore", 5, 1, 100, "");
		oreRarityPitchblende = config.getInt("Gen Rate", "0.4: Uranium Ore", 5, 1, 100, "");
		oreGenThorium = config.getBoolean("Generation", "0.5: Thorium Ore", true, "");
		oreSizeThorium = config.getInt("Chunk Size", "0.5: Thorium Ore", 5, 1, 100, "");
		oreRarityThorium = config.getInt("Gen Rate", "0.5: Thorium Ore", 5, 1, 100, "");
		oreGenLithium = config.getBoolean("Generation", "0.6: Lithium Ore", true, "");
		oreSizeLithium = config.getInt("Chunk Size", "0.6: Lithium Ore", 7, 1, 100, "");
		oreRarityLithium = config.getInt("Gen Rate", "0.6: Lithium Ore", 8, 1, 100, "");
		oreGenBoron = config.getBoolean("Generation", "0.7: Boron Ore", true, "");
		oreSizeBoron = config.getInt("Chunk Size", "0.7: Boron Ore", 7, 1, 100, "");
		oreRarityBoron = config.getInt("Gen Rate", "0.7: Boron Ore", 8, 1, 100, "");
		oreGenPlutonium = config.getBoolean("Generation", "0.8: Plutonium Ore", true, "");
		oreSizePlutonium = config.getInt("Chunk Size", "0.8: Plutonium Ore", 4, 1, 100, "");
		oreRarityPlutonium = config.getInt("Gen Rate", "0.8: Plutonium Ore", 5, 1, 100, "");
		oreGenMagnesium = config.getBoolean("Generation", "0.9: Magnesium Ore", true, "");
		oreSizeMagnesium = config.getInt("Chunk Size", "0.9: Magnesium Ore", 7, 1, 100, "");
		oreRarityMagnesium = config.getInt("Gen Rate", "0.9: Magnesium Ore", 8, 1, 100, "");
		
		electricCrusherCrushSpeed = config.getInt("Electic Crusher Speed Multiplier", "1.0: RF Machines", 100, 10, 1000, "");
		electricFurnaceSmeltSpeed = config.getInt("Electic Furnace Speed Multiplier", "1.0: RF Machines", 100, 10, 1000, "");
		separatorSpeed = config.getInt("Isotope Separator Speed Multiplier", "1.0: RF Machines", 100, 10, 1000, "");
		hastenerSpeed = config.getInt("Decay Hastener Speed Multiplier", "1.0: RF Machines", 100, 10, 1000, "");
		electrolyserSpeed = config.getInt("Electrolyser Speed Multiplier", "1.0: RF Machines", 100, 10, 1000, "");
		oxidiserSpeed = config.getInt("Oxidiser Speed Multiplier", "1.0: RF Machines", 100, 10, 1000, "");
		ioniserSpeed = config.getInt("Ioniser Speed Multiplier", "1.0: RF Machines", 100, 10, 1000, "");
		irradiatorSpeed = config.getInt("Neutron Irradiator Speed Multiplier", "1.0: RF Machines", 100, 10, 1000, "");
		coolerSpeed = config.getInt("Supercooler Speed Multiplier", "1.0: RF Machines", 100, 10, 1000, "");
		factorySpeed = config.getInt("Manufactory Speed Multiplier", "1.0: RF Machines", 100, 10, 1000, "");
		heliumExtractorSpeed = config.getInt("Helium Extractor Speed Multiplier", "1.0: RF Machines", 100, 10, 1000, "");
		
		colliderRF = config.getInt("Collider RF Requirement Multiplier", "1.0: RF Machines", 100, 10, 1000, "");
		colliderProduction = config.getInt("Collider Production Multiplier", "1.0: RF Machines", 100, 10, 1000, "");
		synchrotronRF = config.getInt("Synchrotron RF Requirement Multiplier", "1.0: RF Machines", 100, 10, 1000, "");
		synchrotronProduction = config.getInt("Synchrotron Production Multiplier", "1.0: RF Machines", 100, 10, 1000, "");
		electromagnetRF = config.getInt("Electromagnet RF/t Requirement", "1.0: RF Machines", 400, 0, 10000, "");
		electromagnetHe = config.getInt("Electromagnet Liquid Helium mB/t Requirement", "1.0: RF Machines", 1, 0, 100, "");
		acceleratorProduction = config.getInt("Synchrotron Production Multiplier", "1.0: RF Machines", 100, 10, 1000, "");
		
		reactionGeneratorRF = config.getInt("Reaction Generator RF Production Multiplier", "1.1: RF Generators", 100, 10, 1000, "");
		reactionGeneratorEfficiency = config.getInt("Reaction Generator Efficiency Multiplier", "1.1: RF Generators", 100, 10, 1000, "");
		fissionRF = config.getInt("Fission Reactor RF Production Multiplier", "1.1: RF Generators", 100, 10, 1000, "");
		fissionEfficiency = config.getInt("Fission Reactor Fuel Efficiency Multiplier", "1.1: RF Generators", 100, 10, 1000, "");
		RTGRF = config.getInt("RTG RF/t", "1.1: RF Generators", 150, 10, 1000, "");
		WRTGRF = config.getInt("WRTG RF/t", "1.1: RF Generators", 2, 0, 10, "");
		fusionRF = config.getInt("Fusion Reactor RF Production Multiplier", "1.1: RF Generators", 100, 10, 1000, "");
		fusionEfficiency = config.getInt("Fusion Reactor Fuel Efficiency Multiplier", "1.1: RF Generators", 100, 10, 1000, "");
		nuclearMeltdowns = config.getBoolean("Enable Fission Reactor Meltdowns", "1.1: RF Generators", true, "");
		solarRF = config.getInt("Solar Panel RF/t", "1.1: RF Generators", 4, 1, 50, "");
		
		nuclearFurnaceCookSpeed = config.getInt("Nuclear Furnace Speed Multiplier", "1.2: Non-RF Machines", 100, 10, 1000, "");
		nuclearFurnaceCookEfficiency = config.getInt("Nuclear Furnace Fuel Usage Multiplier", "1.2: Non-RF Machines", 100, 10, 1000, "");
		metalFurnaceCookSpeed = config.getInt("Metal Furnace Speed Multiplier", "1.2: Non-RF Machines", 100, 10, 1000, "");
		metalFurnaceCookEfficiency = config.getInt("Metal Furnace Fuel Usage Multiplier", "1.2: Non-RF Machines", 100, 10, 1000, "");
		crusherCrushSpeed = config.getInt("Crusher Speed Multiplier", "1.2: Non-RF Machines", 100, 10, 1000, "");
		crusherCrushEfficiency = config.getInt("Crusher Fuel Usage Multiplier", "1.2: Non-RF Machines", 100, 10, 1000, "");
		collectorSpeed = config.getInt("Helium Collector Speed Multiplier", "1.2: Non-RF Machines", 100, 10, 1000, "");
		
		enableNuclearMonster = config.getBoolean("Enable Nuclear Monsters Spawning", "2.0: Mobs", true, "");
		enablePaul = config.getBoolean("Enable Paul", "2.0: Mobs", true, "");
		
		enableNukes = config.getBoolean("Enable Nuclear Weapons", "2.1: Other", true, "");
		enableLoot = config.getBoolean("Enable Loot in Generated Chests", "2.1: Other", true, "");
		lootModifier = config.getInt("Loot Gen Rate Modifier", "2.1: Other", 10, 1, 100, "");

		config.save();
		
		// Fusion
		//TileEntityFusionReactor.registerReactions();
		
		// Fluid Registry
		liquidHelium = new FluidHelium().setLuminosity(0).setDensity(125).setViscosity(0).setTemperature(4).setUnlocalizedName("liquidHelium").setRarity(net.minecraft.item.EnumRarity.rare);
		FluidRegistry.registerFluid(liquidHelium);
		NCBlocks.blockHelium = new BlockHelium(liquidHelium, liquidhelium.setReplaceable()).setBlockName("liquidHeliumBlock");
		GameRegistry.registerBlock(NCBlocks.blockHelium, "liquidHeliumBlock");
		
		// Ore Registry
		GameRegistry.registerBlock(NCBlocks.blockOre = new BlockOre("blockOre", Material.rock), ItemBlockOre.class, "blockOre");
		
		// Block Registry
		GameRegistry.registerBlock(NCBlocks.blockBlock = new BlockBlock("blockBlock", Material.iron), ItemBlockBlock.class, "blockBlock");
		
		NCBlocks.simpleQuantumUp = new BlockSimpleQuantum(true).setBlockName("simpleQuantumUp").setCreativeTab(tabNC).setStepSound(Block.soundTypeMetal)
				.setResistance(5.0F).setHardness(2.0F);
		GameRegistry.registerBlock(NCBlocks.simpleQuantumUp, "simpleQuantumUp");
		
		NCBlocks.simpleQuantumDown = new BlockSimpleQuantum(false).setBlockName("simpleQuantumDown").setCreativeTab(tabNC).setStepSound(Block.soundTypeMetal)
				.setResistance(5.0F).setHardness(2.0F);
		GameRegistry.registerBlock(NCBlocks.simpleQuantumDown, "simpleQuantumDown");
		
		NCBlocks.graphiteBlock = new BlockGraphiteBlock().setBlockName("graphiteBlock").setCreativeTab(tabNC).setStepSound(Block.soundTypeStone)
				.setResistance(5.0F).setHardness(2.0F);
		GameRegistry.registerBlock(NCBlocks.graphiteBlock, "graphiteBlock");
		NCBlocks.cellBlock = new BlockCellBlock().setBlockName("cellBlock").setCreativeTab(tabNC).setStepSound(Block.soundTypeMetal)
				.setResistance(5.0F).setHardness(2.0F);
		GameRegistry.registerBlock(NCBlocks.cellBlock, "cellBlock");
		NCBlocks.reactorBlock = new BlockReactorBlock().setBlockName("reactorBlock").setCreativeTab(tabNC).setStepSound(Block.soundTypeMetal)
				.setResistance(5.0F).setHardness(3.0F);
		GameRegistry.registerBlock(NCBlocks.reactorBlock, "reactorBlock");
		NCBlocks.coolerBlock = new BlockCoolerBlock().setBlockName("coolerBlock").setCreativeTab(tabNC).setStepSound(Block.soundTypeMetal)
				.setResistance(5.0F).setHardness(2.0F);
		GameRegistry.registerBlock(NCBlocks.coolerBlock, "coolerBlock");
		NCBlocks.speedBlock = new BlockSpeedBlock().setBlockName("speedBlock").setCreativeTab(tabNC).setStepSound(Block.soundTypeMetal)
				.setResistance(5.0F).setHardness(2.0F);
		GameRegistry.registerBlock(NCBlocks.speedBlock, "speedBlock");
		NCBlocks.blastBlock = new BlockBlastBlock().setBlockName("blastBlock").setCreativeTab(tabNC).setStepSound(Block.soundTypeMetal)
				.setResistance(4000.0F).setHardness(30.0F);
		GameRegistry.registerBlock(NCBlocks.blastBlock, "blastBlock");
		NCBlocks.machineBlock = new BlockMachineBlock().setBlockName("machineBlock").setCreativeTab(tabNC).setStepSound(Block.soundTypeMetal)
				.setResistance(4000.0F).setHardness(30.0F);
		GameRegistry.registerBlock(NCBlocks.machineBlock, "machineBlock");
		
		NCBlocks.electromagnetIdle = new BlockElectromagnet(false).setBlockName("electromagnetIdle").setCreativeTab(tabNC).setStepSound(Block.soundTypeMetal)
				.setResistance(8.0F).setHardness(3.0F);
		GameRegistry.registerBlock(NCBlocks.electromagnetIdle, "electromagnetIdle");
		NCBlocks.electromagnetActive = new BlockElectromagnet(true).setBlockName("electromagnetActive").setCreativeTab(tabNC).setStepSound(Block.soundTypeMetal)
				.setResistance(8.0F).setHardness(3.0F);
		GameRegistry.registerBlock(NCBlocks.electromagnetActive, "electromagnetActive");
		NCBlocks.supercoolerIdle = new BlockSupercooler(false).setBlockName("supercoolerIdle").setCreativeTab(tabNC).setStepSound(Block.soundTypeMetal)
				.setResistance(8.0F).setHardness(3.0F);
		GameRegistry.registerBlock(NCBlocks.supercoolerIdle, "supercoolerIdle");
		NCBlocks.supercoolerActive = new BlockSupercooler(true).setBlockName("supercoolerActive").setCreativeTab(tabNC).setStepSound(Block.soundTypeMetal)
				.setResistance(8.0F).setHardness(3.0F);
		GameRegistry.registerBlock(NCBlocks.supercoolerActive, "supercoolerActive");
		
		NCBlocks.synchrotronIdle = new BlockSynchrotron(false).setBlockName("synchrotronIdle").setCreativeTab(tabNC).setStepSound(Block.soundTypeMetal)
				.setResistance(8.0F).setHardness(3.0F);
		GameRegistry.registerBlock(NCBlocks.synchrotronIdle, "synchrotronIdle");
		NCBlocks.synchrotronActive = new BlockSynchrotron(true).setBlockName("synchrotronActive").setCreativeTab(tabNC).setStepSound(Block.soundTypeMetal)
				.setResistance(8.0F).setHardness(3.0F);
		GameRegistry.registerBlock(NCBlocks.synchrotronActive, "synchrotronActive");
		
		NCBlocks.tubing1 = new BlockTubing1().setBlockName("tubing1").setCreativeTab(tabNC).setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F);
		GameRegistry.registerBlock(NCBlocks.tubing1, "tubing1");
		NCBlocks.tubing2 = new BlockTubing2().setBlockName("tubing2").setCreativeTab(tabNC).setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F);
		GameRegistry.registerBlock(NCBlocks.tubing2, "tubing2");
	
		// Machine Registry
			// Block
		NCBlocks.nuclearWorkspace = new BlockNuclearWorkspace(Material.iron).setBlockName("nuclearWorkspace").setCreativeTab(tabNC).setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F);
		GameRegistry.registerBlock(NCBlocks.nuclearWorkspace, "nuclearWorkspace");
		
		NCBlocks.fusionReactor = new BlockFusionReactor(Material.iron).setBlockName("fusionReactor").setCreativeTab(tabNC).setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F);
		GameRegistry.registerBlock(NCBlocks.fusionReactor, "fusionReactor");
		NCBlocks.fusionReactorBlock = new BlockFusionReactorBlock().setBlockName("fusionReactorBlock").setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F);
		GameRegistry.registerBlock(NCBlocks.fusionReactorBlock, "fusionReactorBlock");
		
		NCBlocks.nuclearFurnaceIdle = new BlockNuclearFurnace(false).setBlockName("nuclearFurnaceIdle").setCreativeTab(tabNC).setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F);
		GameRegistry.registerBlock(NCBlocks.nuclearFurnaceIdle, "nuclearFurnaceIdle");
		NCBlocks.nuclearFurnaceActive = new BlockNuclearFurnace(true).setBlockName("nuclearFurnaceActive").setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F).setLightLevel(0.750F);
		GameRegistry.registerBlock(NCBlocks.nuclearFurnaceActive, "nuclearFurnaceActive");
		NCBlocks.furnaceIdle = new BlockFurnace(false).setBlockName("furnaceIdle").setCreativeTab(tabNC).setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F);
		GameRegistry.registerBlock(NCBlocks.furnaceIdle, "furnaceIdle");
		NCBlocks.furnaceActive = new BlockFurnace(true).setBlockName("furnaceActive").setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F).setLightLevel(0.750F);
		GameRegistry.registerBlock(NCBlocks.furnaceActive, "furnaceActive");
		NCBlocks.crusherIdle = new BlockCrusher(false).setBlockName("crusherIdle").setCreativeTab(tabNC).setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F);
		GameRegistry.registerBlock(NCBlocks.crusherIdle, "crusherIdle");
		NCBlocks.crusherActive = new BlockCrusher(true).setBlockName("crusherActive").setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F).setLightLevel(0.750F);
		GameRegistry.registerBlock(NCBlocks.crusherActive, "crusherActive");
		NCBlocks.electricCrusherIdle = new BlockElectricCrusher(false).setBlockName("electricCrusherIdle").setCreativeTab(tabNC).setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F);
		GameRegistry.registerBlock(NCBlocks.electricCrusherIdle, "electricCrusherIdle");
		NCBlocks.electricCrusherActive = new BlockElectricCrusher(true).setBlockName("electricCrusherActive").setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F).setLightLevel(0.750F);
		GameRegistry.registerBlock(NCBlocks.electricCrusherActive, "electricCrusherActive");
		NCBlocks.electricFurnaceIdle = new BlockElectricFurnace(false).setBlockName("electricFurnaceIdle").setCreativeTab(tabNC).setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F);
		GameRegistry.registerBlock(NCBlocks.electricFurnaceIdle, "electricFurnaceIdle");
		NCBlocks.electricFurnaceActive = new BlockElectricFurnace(true).setBlockName("electricFurnaceActive").setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F).setLightLevel(0.750F);
		GameRegistry.registerBlock(NCBlocks.electricFurnaceActive, "electricFurnaceActive");
		NCBlocks.reactionGeneratorIdle = new BlockReactionGenerator(false).setBlockName("reactionGeneratorIdle").setCreativeTab(tabNC).setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F);
		GameRegistry.registerBlock(NCBlocks.reactionGeneratorIdle, "reactionGeneratorIdle");
		NCBlocks.reactionGeneratorActive = new BlockReactionGenerator(true).setBlockName("reactionGeneratorActive").setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F).setLightLevel(0.750F);
		GameRegistry.registerBlock(NCBlocks.reactionGeneratorActive, "reactionGeneratorActive");
		NCBlocks.separatorIdle = new BlockSeparator(false).setBlockName("separatorIdle").setCreativeTab(tabNC).setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F);
		GameRegistry.registerBlock(NCBlocks.separatorIdle, "separatorIdle");
		NCBlocks.separatorActive = new BlockSeparator(true).setBlockName("separatorActive").setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F).setLightLevel(0.750F);
		GameRegistry.registerBlock(NCBlocks.separatorActive, "separatorActive");
		NCBlocks.hastenerIdle = new BlockHastener(false).setBlockName("hastenerIdle").setCreativeTab(tabNC).setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F);
		GameRegistry.registerBlock(NCBlocks.hastenerIdle, "hastenerIdle");
		NCBlocks.hastenerActive = new BlockHastener(true).setBlockName("hastenerActive").setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F).setLightLevel(0.750F);
		GameRegistry.registerBlock(NCBlocks.hastenerActive, "hastenerActive");
		NCBlocks.electrolyserIdle = new BlockElectrolyser(false).setBlockName("electrolyserIdle").setCreativeTab(tabNC).setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F);
		GameRegistry.registerBlock(NCBlocks.electrolyserIdle, "electrolyserIdle");
		NCBlocks.electrolyserActive = new BlockElectrolyser(true).setBlockName("electrolyserActive").setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F).setLightLevel(0.750F);
		GameRegistry.registerBlock(NCBlocks.electrolyserActive, "electrolyserActive");
		NCBlocks.fissionReactorGraphiteIdle = new BlockFissionReactor(false).setBlockName("fissionReactorGraphiteIdle").setCreativeTab(tabNC).setStepSound(Block.soundTypeMetal)
				.setResistance(5.0F).setHardness(5.0F);
		GameRegistry.registerBlock(NCBlocks.fissionReactorGraphiteIdle, "fissionReactorGraphiteIdle");
		NCBlocks.fissionReactorGraphiteActive = new BlockFissionReactor(true).setBlockName("fissionReactorGraphiteActive").setStepSound(Block.soundTypeMetal)
				.setResistance(5.0F).setHardness(5.0F).setLightLevel(0.750F);
		GameRegistry.registerBlock(NCBlocks.fissionReactorGraphiteActive, "fissionReactorGraphiteActive");
		NCBlocks.RTG = new BlockRTG().setBlockName("RTG").setCreativeTab(tabNC).setStepSound(Block.soundTypeMetal)
				.setResistance(5.0F).setHardness(5.0F).setLightLevel(0.250F);
		GameRegistry.registerBlock(NCBlocks.RTG, "RTG");
		NCBlocks.WRTG = new BlockWRTG().setBlockName("WRTG").setCreativeTab(tabNC).setStepSound(Block.soundTypeMetal)
				.setResistance(5.0F).setHardness(5.0F).setLightLevel(0.250F);
		GameRegistry.registerBlock(NCBlocks.WRTG, "WRTG");
		NCBlocks.solarPanel = new BlockSolarPanel().setBlockName("solarPanel").setCreativeTab(tabNC).setStepSound(Block.soundTypeMetal)
				.setResistance(5.0F).setHardness(5.0F).setLightLevel(0.250F);
		GameRegistry.registerBlock(NCBlocks.solarPanel, "solarPanel");
		NCBlocks.collectorIdle = new BlockCollector(false).setBlockName("collectorIdle").setCreativeTab(tabNC).setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F);
		GameRegistry.registerBlock(NCBlocks.collectorIdle, "collectorIdle");
		NCBlocks.collectorActive = new BlockCollector(true).setBlockName("collectorActive").setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F).setLightLevel(0.750F);
		GameRegistry.registerBlock(NCBlocks.collectorActive, "collectorActive");
		NCBlocks.oxidiserIdle = new BlockOxidiser(false).setBlockName("oxidiserIdle").setCreativeTab(tabNC).setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F);
		GameRegistry.registerBlock(NCBlocks.oxidiserIdle, "oxidiserIdle");
		NCBlocks.oxidiserActive = new BlockOxidiser(true).setBlockName("oxidiserActive").setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F).setLightLevel(0.750F);
		GameRegistry.registerBlock(NCBlocks.oxidiserActive, "oxidiserActive");
		NCBlocks.ioniserIdle = new BlockIoniser(false).setBlockName("ioniserIdle").setCreativeTab(tabNC).setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F);
		GameRegistry.registerBlock(NCBlocks.ioniserIdle, "ioniserIdle");
		NCBlocks.ioniserActive = new BlockIoniser(true).setBlockName("ioniserActive").setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F).setLightLevel(0.750F);
		GameRegistry.registerBlock(NCBlocks.ioniserActive, "ioniserActive");
		NCBlocks.irradiatorIdle = new BlockIrradiator(false).setBlockName("irradiatorIdle").setCreativeTab(tabNC).setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F);
		GameRegistry.registerBlock(NCBlocks.irradiatorIdle, "irradiatorIdle");
		NCBlocks.irradiatorActive = new BlockIrradiator(true).setBlockName("irradiatorActive").setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F).setLightLevel(0.750F);
		GameRegistry.registerBlock(NCBlocks.irradiatorActive, "irradiatorActive");
		NCBlocks.coolerIdle = new BlockCooler(false).setBlockName("coolerIdle").setCreativeTab(tabNC).setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F);
		GameRegistry.registerBlock(NCBlocks.coolerIdle, "coolerIdle");
		NCBlocks.coolerActive = new BlockCooler(true).setBlockName("coolerActive").setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F).setLightLevel(0.750F);
		GameRegistry.registerBlock(NCBlocks.coolerActive, "coolerActive");
		NCBlocks.factoryIdle = new BlockFactory(false).setBlockName("factoryIdle").setCreativeTab(tabNC).setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F);
		GameRegistry.registerBlock(NCBlocks.factoryIdle, "factoryIdle");
		NCBlocks.factoryActive = new BlockFactory(true).setBlockName("factoryActive").setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F).setLightLevel(0.750F);
		GameRegistry.registerBlock(NCBlocks.factoryActive, "factoryActive");
		NCBlocks.heliumExtractorIdle = new BlockHeliumExtractor(false).setBlockName("heliumExtractorIdle").setCreativeTab(tabNC).setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F);
		GameRegistry.registerBlock(NCBlocks.heliumExtractorIdle, "heliumExtractorIdle");
		NCBlocks.heliumExtractorActive = new BlockHeliumExtractor(true).setBlockName("heliumExtractorActive").setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F).setLightLevel(0.750F);
		GameRegistry.registerBlock(NCBlocks.heliumExtractorActive, "heliumExtractorActive");
		
		NCBlocks.nuke = new BlockNuke().setBlockName("nuke").setCreativeTab(tabNC).setStepSound(Block.soundTypeCloth)
				.setHardness(0.0F);
		GameRegistry.registerBlock(NCBlocks.nuke, "nuke");
		NCBlocks.nukeE = new BlockNukeExploding().setBlockName("nukeE").setStepSound(Block.soundTypeCloth)
				.setHardness(0.0F);
		GameRegistry.registerBlock(NCBlocks.nukeE, "nukeE");
			
			// Tile Entity
		GameRegistry.registerTileEntity(TileNuclearFurnace.class, "nuclearFurnace");
		GameRegistry.registerTileEntity(TileFurnace.class, "furnace");
		GameRegistry.registerTileEntity(TileCrusher.class, "crusher");
		GameRegistry.registerTileEntity(TileElectricCrusher.class, "electricCrusher");
		GameRegistry.registerTileEntity(TileElectricFurnace.class, "electricFurnace");
		GameRegistry.registerTileEntity(TileReactionGenerator.class, "reactionGenerator");
		GameRegistry.registerTileEntity(TileSeparator.class, "separator");
		GameRegistry.registerTileEntity(TileHastener.class, "hastener");
		GameRegistry.registerTileEntity(TileCollector.class, "collector");
		GameRegistry.registerTileEntity(TileElectrolyser.class, "electrolyser");
		GameRegistry.registerTileEntity(TileFissionReactor.class, "fissionReactorGraphite");
		GameRegistry.registerTileEntity(TileNuclearWorkspace.class, "nuclearWorkspace");
		GameRegistry.registerTileEntity(TileFusionReactor.class, "fusionReactor");
		GameRegistry.registerTileEntity(TileTubing1.class, "TEtubing1");
		GameRegistry.registerTileEntity(TileTubing2.class, "TEtubing2");
		GameRegistry.registerTileEntity(TileRTG.class, "RTG");
		GameRegistry.registerTileEntity(TileWRTG.class, "WRTG");
		GameRegistry.registerTileEntity(TileFusionReactorBlock.class, "fusionReactorBlock");
		GameRegistry.registerTileEntity(TileOxidiser.class, "oxidiser");
		GameRegistry.registerTileEntity(TileIoniser.class, "ioniser");
		GameRegistry.registerTileEntity(TileIrradiator.class, "irradiator");
		GameRegistry.registerTileEntity(TileCooler.class, "cooler");
		GameRegistry.registerTileEntity(TileFactory.class, "factory");
		GameRegistry.registerTileEntity(TileHeliumExtractor.class, "heliumExtractor");
		GameRegistry.registerTileEntity(TileSolarPanel.class, "solarPanel");
		
		GameRegistry.registerTileEntity(TileElectromagnet.class, "electromagnet");
		GameRegistry.registerTileEntity(TileSupercooler.class, "supercooler");
		GameRegistry.registerTileEntity(TileSynchrotron.class, "synchrotron");
		
		GameRegistry.registerTileEntity(TileSimpleQuantum.class, "simpleQuantum");
	
		// Item Registry	
		NCItems.dominoes = new ItemFood(10, 1.2F, false).setCreativeTab(tabNC).setUnlocalizedName("dominoes").setTextureName("nc:food/" + "dominoes");
		GameRegistry.registerItem(NCItems.dominoes, "dominoes");
		NCItems.boiledEgg = new ItemFood(6, 1.0F, false).setCreativeTab(tabNC).setUnlocalizedName("boiledEgg").setTextureName("nc:food/" + "boiledEgg");
		GameRegistry.registerItem(NCItems.boiledEgg, "boiledEgg");
		NCItems.upgrade = new Item().setCreativeTab(tabNC).setUnlocalizedName("upgrade").setTextureName("nc:upgrades/" + "upgrade").setMaxStackSize(8);
		GameRegistry.registerItem(NCItems.upgrade, "upgrade");
		NCItems.upgradeSpeed = new Item().setCreativeTab(tabNC).setUnlocalizedName("upgradeSpeed").setTextureName("nc:upgrades/" + "upgradeSpeed").setMaxStackSize(8);
		GameRegistry.registerItem(NCItems.upgradeSpeed, "upgradeSpeed");
		NCItems.upgradeEnergy = new Item().setCreativeTab(tabNC).setUnlocalizedName("upgradeEnergy").setTextureName("nc:upgrades/" + "upgradeEnergy").setMaxStackSize(8);
		GameRegistry.registerItem(NCItems.upgradeEnergy, "upgradeEnergy");
		
		NCItems.tabItem = new Item().setUnlocalizedName("tabItem").setTextureName("nc:fuel/" + "11");
		GameRegistry.registerItem(NCItems.tabItem, "tabItem");
		
		GameRegistry.registerItem(NCItems.fuel = new ItemFuel("fuel"), "fuel");
		GameRegistry.registerItem(NCItems.material = new ItemMaterial("material"), "material");
		GameRegistry.registerItem(NCItems.parts = new ItemPart("parts"), "parts");
		
		NCItems.nuclearGrenade = new ItemNuclearGrenade().setCreativeTab(tabNC).setUnlocalizedName("nuclearGrenade").setTextureName("nc:weapons/" + "nuclearGrenade");
		GameRegistry.registerItem(NCItems.nuclearGrenade, "nuclearGrenade");
		NCItems.nuclearGrenadeThrown = new Item().setUnlocalizedName("nuclearGrenadeThrown").setTextureName("nc:weapons/" + "nuclearGrenadeThrown");
		GameRegistry.registerItem(NCItems.nuclearGrenadeThrown, "nuclearGrenadeThrown");
		
		NCItems.portableEnderChest = new ItemEnderChest().setCreativeTab(tabNC).setUnlocalizedName("portableEnderChest").setTextureName("nc:" + "portableEnderChest").setMaxStackSize(1);
		GameRegistry.registerItem(NCItems.portableEnderChest, "portableEnderChest");
		
		// Tool Registry
		NCItems.bronzePickaxe = new NCPickaxe(Bronze).setCreativeTab(tabNC).setUnlocalizedName("bronzePickaxe").setTextureName("nc:tools/" + "bronzePickaxe");
		GameRegistry.registerItem(NCItems.bronzePickaxe, "bronzePickaxe");
		NCItems.bronzeShovel = new NCShovel(Bronze).setCreativeTab(tabNC).setUnlocalizedName("bronzeShovel").setTextureName("nc:tools/" + "bronzeShovel");
		GameRegistry.registerItem(NCItems.bronzeShovel, "bronzeShovel");
		NCItems.bronzeAxe = new NCAxe(Bronze).setCreativeTab(tabNC).setUnlocalizedName("bronzeAxe").setTextureName("nc:tools/" + "bronzeAxe");
		GameRegistry.registerItem(NCItems.bronzeAxe, "bronzeAxe");
		NCItems.bronzeHoe = new NCHoe(Bronze).setCreativeTab(tabNC).setUnlocalizedName("bronzeHoe").setTextureName("nc:tools/" + "bronzeHoe");
		GameRegistry.registerItem(NCItems.bronzeHoe, "bronzeHoe");
		NCItems.bronzeSword = new NCSword(Bronze).setCreativeTab(tabNC).setUnlocalizedName("bronzeSword").setTextureName("nc:tools/" + "bronzeSword");
		GameRegistry.registerItem(NCItems.bronzeSword, "bronzeSword");
		
		NCItems.boronPickaxe = new NCPickaxe(Boron).setCreativeTab(tabNC).setUnlocalizedName("boronPickaxe").setTextureName("nc:tools/" + "boronPickaxe");
		GameRegistry.registerItem(NCItems.boronPickaxe, "boronPickaxe");
		NCItems.boronShovel = new NCShovel(Boron).setCreativeTab(tabNC).setUnlocalizedName("boronShovel").setTextureName("nc:tools/" + "boronShovel");
		GameRegistry.registerItem(NCItems.boronShovel, "boronShovel");
		NCItems.boronAxe = new NCAxe(Boron).setCreativeTab(tabNC).setUnlocalizedName("boronAxe").setTextureName("nc:tools/" + "boronAxe");
		GameRegistry.registerItem(NCItems.boronAxe, "boronAxe");
		NCItems.boronHoe = new NCHoe(Boron).setCreativeTab(tabNC).setUnlocalizedName("boronHoe").setTextureName("nc:tools/" + "boronHoe");
		GameRegistry.registerItem(NCItems.boronHoe, "boronHoe");
		NCItems.boronSword = new NCSword(Boron).setCreativeTab(tabNC).setUnlocalizedName("boronSword").setTextureName("nc:tools/" + "boronSword");
		GameRegistry.registerItem(NCItems.boronSword, "boronSword");
		
		NCItems.toughAlloyPickaxe = new NCPickaxe(ToughAlloy).setCreativeTab(tabNC).setUnlocalizedName("toughAlloyPickaxe").setTextureName("nc:tools/" + "toughAlloyPickaxe");
		GameRegistry.registerItem(NCItems.toughAlloyPickaxe, "toughAlloyPickaxe");
		NCItems.toughAlloyShovel = new NCShovel(ToughAlloy).setCreativeTab(tabNC).setUnlocalizedName("toughAlloyShovel").setTextureName("nc:tools/" + "toughAlloyShovel");
		GameRegistry.registerItem(NCItems.toughAlloyShovel, "toughAlloyShovel");
		NCItems.toughAlloyAxe = new NCAxe(ToughAlloy).setCreativeTab(tabNC).setUnlocalizedName("toughAlloyAxe").setTextureName("nc:tools/" + "toughAlloyAxe");
		GameRegistry.registerItem(NCItems.toughAlloyAxe, "toughAlloyAxe");
		NCItems.toughAlloyHoe = new NCHoe(ToughAlloy).setCreativeTab(tabNC).setUnlocalizedName("toughAlloyHoe").setTextureName("nc:tools/" + "toughAlloyHoe");
		GameRegistry.registerItem(NCItems.toughAlloyHoe, "toughAlloyHoe");
		NCItems.toughAlloySword = new NCSword(ToughAlloy).setCreativeTab(tabNC).setUnlocalizedName("toughAlloySword").setTextureName("nc:tools/" + "toughAlloySword");
		GameRegistry.registerItem(NCItems.toughAlloySword, "toughAlloySword");
		NCItems.toughAlloyPaxel = new NCPaxel(ToughPaxel).setCreativeTab(tabNC).setUnlocalizedName("toughAlloyPaxel").setTextureName("nc:tools/" + "toughAlloyPaxel");
		GameRegistry.registerItem(NCItems.toughAlloyPaxel, "toughAlloyPaxel");
		
		NCItems.dUPickaxe = new NCPickaxe(dU).setCreativeTab(tabNC).setUnlocalizedName("dUPickaxe").setTextureName("nc:tools/" + "dUPickaxe");
		GameRegistry.registerItem(NCItems.dUPickaxe, "dUPickaxe");
		NCItems.dUShovel = new NCShovel(dU).setCreativeTab(tabNC).setUnlocalizedName("dUShovel").setTextureName("nc:tools/" + "dUShovel");
		GameRegistry.registerItem(NCItems.dUShovel, "dUShovel");
		NCItems.dUAxe = new NCAxe(dU).setCreativeTab(tabNC).setUnlocalizedName("dUAxe").setTextureName("nc:tools/" + "dUAxe");
		GameRegistry.registerItem(NCItems.dUAxe, "dUAxe");
		NCItems.dUHoe = new NCHoe(dU).setCreativeTab(tabNC).setUnlocalizedName("dUHoe").setTextureName("nc:tools/" + "dUHoe");
		GameRegistry.registerItem(NCItems.dUHoe, "dUHoe");
		NCItems.dUSword = new NCSword(dU).setCreativeTab(tabNC).setUnlocalizedName("dUSword").setTextureName("nc:tools/" + "dUSword");
		GameRegistry.registerItem(NCItems.dUSword, "dUSword");
		NCItems.dUPaxel = new NCPaxel(dUPaxel).setCreativeTab(tabNC).setUnlocalizedName("dUPaxel").setTextureName("nc:tools/" + "dUPaxel");
		GameRegistry.registerItem(NCItems.dUPaxel, "dUPaxel");
		
		NCItems.toughBow = new ItemToughBow().setCreativeTab(tabNC).setUnlocalizedName("toughBow").setMaxStackSize(1);
		GameRegistry.registerItem(NCItems.toughBow, "toughBow");
		NCItems.pistol = new ItemPistol().setCreativeTab(tabNC).setUnlocalizedName("pistol").setMaxStackSize(1).setTextureName("nc:tools/" + "pistol");
		GameRegistry.registerItem(NCItems.pistol, "pistol");
		NCItems.dUBullet = new Item().setCreativeTab(tabNC).setUnlocalizedName("dUBullet").setTextureName("nc:tools/" + "dUBullet");
		GameRegistry.registerItem(NCItems.dUBullet, "dUBullet");
		
		//Armor Registry
		NCItems.toughHelm = new ToughArmour(ToughArmorMaterial, toughHelmID, 0).setUnlocalizedName("toughHelm").setTextureName("nc:armour/" + "toughHelm");
		GameRegistry.registerItem(NCItems.toughHelm, "toughHelm");
		NCItems.toughChest = new ToughArmour(ToughArmorMaterial, toughChestID, 1).setUnlocalizedName("toughChest").setTextureName("nc:armour/" + "toughChest");
		GameRegistry.registerItem(NCItems.toughChest, "toughChest");
		NCItems.toughLegs = new ToughArmour(ToughArmorMaterial, toughLegsID, 2).setUnlocalizedName("toughLegs").setTextureName("nc:armour/" + "toughLegs");
		GameRegistry.registerItem(NCItems.toughLegs, "toughLegs");
		NCItems.toughBoots = new ToughArmour(ToughArmorMaterial, toughBootsID, 3).setUnlocalizedName("toughBoots").setTextureName("nc:armour/" + "toughBoots");
		GameRegistry.registerItem(NCItems.toughBoots, "toughBoots");
		
		NCItems.boronHelm = new BoronArmour(BoronArmorMaterial, boronHelmID, 0).setUnlocalizedName("boronHelm").setTextureName("nc:armour/" + "boronHelm");
		GameRegistry.registerItem(NCItems.boronHelm, "boronHelm");
		NCItems.boronChest = new BoronArmour(BoronArmorMaterial, boronChestID, 1).setUnlocalizedName("boronChest").setTextureName("nc:armour/" + "boronChest");
		GameRegistry.registerItem(NCItems.boronChest, "boronChest");
		NCItems.boronLegs = new BoronArmour(BoronArmorMaterial, boronLegsID, 2).setUnlocalizedName("boronLegs").setTextureName("nc:armour/" + "boronLegs");
		GameRegistry.registerItem(NCItems.boronLegs, "boronLegs");
		NCItems.boronBoots = new BoronArmour(BoronArmorMaterial, boronBootsID, 3).setUnlocalizedName("boronBoots").setTextureName("nc:armour/" + "boronBoots");
		GameRegistry.registerItem(NCItems.boronBoots, "boronBoots");
		
		NCItems.bronzeHelm = new BronzeArmour(BronzeArmorMaterial, bronzeHelmID, 0).setUnlocalizedName("bronzeHelm").setTextureName("nc:armour/" + "bronzeHelm");
		GameRegistry.registerItem(NCItems.bronzeHelm, "bronzeHelm");
		NCItems.bronzeChest = new BronzeArmour(BronzeArmorMaterial, bronzeChestID, 1).setUnlocalizedName("bronzeChest").setTextureName("nc:armour/" + "bronzeChest");
		GameRegistry.registerItem(NCItems.bronzeChest, "bronzeChest");
		NCItems.bronzeLegs = new BronzeArmour(BronzeArmorMaterial, bronzeLegsID, 2).setUnlocalizedName("bronzeLegs").setTextureName("nc:armour/" + "bronzeLegs");
		GameRegistry.registerItem(NCItems.bronzeLegs, "bronzeLegs");
		NCItems.bronzeBoots = new BronzeArmour(BronzeArmorMaterial, bronzeBootsID, 3).setUnlocalizedName("bronzeBoots").setTextureName("nc:armour/" + "bronzeBoots");
		GameRegistry.registerItem(NCItems.bronzeBoots, "bronzeBoots");
		
		NCItems.dUHelm = new DUArmour(dUArmorMaterial, dUHelmID, 0).setUnlocalizedName("dUHelm").setTextureName("nc:armour/" + "dUHelm");
		GameRegistry.registerItem(NCItems.dUHelm, "dUHelm");
		NCItems.dUChest = new DUArmour(dUArmorMaterial, dUChestID, 1).setUnlocalizedName("dUChest").setTextureName("nc:armour/" + "dUChest");
		GameRegistry.registerItem(NCItems.dUChest, "dUChest");
		NCItems.dULegs = new DUArmour(dUArmorMaterial, dULegsID, 2).setUnlocalizedName("dULegs").setTextureName("nc:armour/" + "dULegs");
		GameRegistry.registerItem(NCItems.dULegs, "dULegs");
		NCItems.dUBoots = new DUArmour(dUArmorMaterial, dUBootsID, 3).setUnlocalizedName("dUBoots").setTextureName("nc:armour/" + "dUBoots");
		GameRegistry.registerItem(NCItems.dUBoots, "dUBoots");
		
		// Block Crafting Recipes
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NCBlocks.blockBlock, 1, 4), true,
				new Object[] {"XXX", "XXX", "XXX", 'X', new ItemStack (NCItems.material, 1, 4)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NCBlocks.blockBlock, 1, 0), true,
				new Object[] {"XXX", "XXX", "XXX", 'X',  new ItemStack (NCItems.material, 1, 0)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NCBlocks.blockBlock, 1, 1), true,
				new Object[] {"XXX", "XXX", "XXX", 'X',  new ItemStack (NCItems.material, 1, 1)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NCBlocks.blockBlock, 1, 2), true,
				new Object[] {"XXX", "XXX", "XXX", 'X',  new ItemStack (NCItems.material, 1, 2)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NCBlocks.blockBlock, 1, 3), true,
				new Object[] {"XXX", "XXX", "XXX", 'X',  new ItemStack (NCItems.material, 1, 3)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NCBlocks.blockBlock, 1, 6), true,
				new Object[] {"XXX", "XXX", "XXX", 'X',  new ItemStack (NCItems.material, 1, 6)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NCBlocks.blockBlock, 1, 5), true,
				new Object[] {"XXX", "XXX", "XXX", 'X',  new ItemStack (NCItems.material, 1, 5)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NCBlocks.blockBlock, 1, 8), true,
				new Object[] {"XXX", "XXX", "XXX", 'X',  new ItemStack (NCItems.material, 1, 42)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NCBlocks.blockBlock, 1, 9), true,
				new Object[] {"XXX", "XXX", "XXX", 'X',  new ItemStack (NCItems.material, 1, 43)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NCBlocks.blockBlock, 1, 10), true,
				new Object[] {"XXX", "XXX", "XXX", 'X',  new ItemStack (NCItems.material, 1, 50)}));
		
		// Tiny Dust to Full Dust
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NCItems.material, 1, 17), true,
				new Object[] {"XXX", "XXX", "XXX", 'X', new ItemStack (NCItems.material, 1, 23)}));
		
		// Isotope Lump Recipes
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NCItems.material, 1, 28), true,
				new Object[] {"XXX", "XXX", "XXX", 'X', new ItemStack (NCItems.material, 1, 29)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NCItems.material, 1, 26), true,
				new Object[] {"XXX", "XXX", "XXX", 'X', new ItemStack (NCItems.material, 1, 27)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NCItems.material, 1, 24), true,
				new Object[] {"XXX", "XXX", "XXX", 'X', new ItemStack (NCItems.material, 1, 25)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NCItems.material, 1, 30), true,
				new Object[] {"XXX", "XXX", "XXX", 'X', new ItemStack (NCItems.material, 1, 31)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NCItems.material, 1, 32), true,
				new Object[] {"XXX", "XXX", "XXX", 'X', new ItemStack (NCItems.material, 1, 33)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NCItems.material, 1, 34), true,
				new Object[] {"XXX", "XXX", "XXX", 'X', new ItemStack (NCItems.material, 1, 35)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NCItems.material, 1, 36), true,
				new Object[] {"XXX", "XXX", "XXX", 'X', new ItemStack (NCItems.material, 1, 37)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NCItems.material, 1, 38), true,
				new Object[] {"XXX", "XXX", "XXX", 'X', new ItemStack (NCItems.material, 1, 39)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NCItems.material, 1, 40), true,
				new Object[] {"XXX", "XXX", "XXX", 'X', new ItemStack (NCItems.material, 1, 41)}));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NCItems.material, 1, 59), true,
				new Object[] {"XXX", "XXX", "XXX", 'X', new ItemStack (NCItems.material, 1, 60)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NCItems.material, 1, 57), true,
				new Object[] {"XXX", "XXX", "XXX", 'X', new ItemStack (NCItems.material, 1, 58)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NCItems.material, 1, 55), true,
				new Object[] {"XXX", "XXX", "XXX", 'X', new ItemStack (NCItems.material, 1, 56)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NCItems.material, 1, 61), true,
				new Object[] {"XXX", "XXX", "XXX", 'X', new ItemStack (NCItems.material, 1, 62)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NCItems.material, 1, 63), true,
				new Object[] {"XXX", "XXX", "XXX", 'X', new ItemStack (NCItems.material, 1, 64)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NCItems.material, 1, 65), true,
				new Object[] {"XXX", "XXX", "XXX", 'X', new ItemStack (NCItems.material, 1, 66)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NCItems.material, 1, 67), true,
				new Object[] {"XXX", "XXX", "XXX", 'X', new ItemStack (NCItems.material, 1, 68)}));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NCItems.material, 1, 46), true,
				new Object[] {"XXX", "XXX", "XXX", 'X', new ItemStack (NCItems.material, 1, 69)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NCItems.material, 1, 48), true,
				new Object[] {"XXX", "XXX", "XXX", 'X', new ItemStack (NCItems.material, 1, 70)}));
		
		// Shaped Crafting Recipes
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(NCItems.fuel, 16, 33), true,
				new Object[] {" I ", "I I", " I ", 'I', new ItemStack(NCItems.parts, 1, 1)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(NCItems.fuel, 16, 45), true,
				new Object[] {" I ", "I I", " I ", 'I', new ItemStack(NCItems.parts, 1, 6)}));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(NCItems.parts, 2, 0), true,
				new Object[] {"LLL", "CCC", 'L', "ingotLead", 'C', "dustCoal"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(NCItems.parts, 1, 2), true,
				new Object[] {"FFF", "CCC", "SSS", 'F', Items.flint, 'C', "cobblestone", 'S', Items.stick}));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(NCItems.parts, 16, 1), true,
				new Object[] {"III", "IBI", "III", 'I', "ingotIron", 'B', "blockIron"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(NCItems.parts, 16, 6), true,
				new Object[] {"III", "IBI", "III", 'I', "ingotTin", 'B', "blockTin"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(NCBlocks.nuclearFurnaceIdle, true,
				new Object[] {"XPX", "P P", "XPX", 'P', new ItemStack(NCItems.parts, 1, 0), 'X', "dustObsidian"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(NCBlocks.furnaceIdle, true,
				new Object[] {"PPP", "P P", "PPP", 'P', new ItemStack(NCItems.parts, 1, 1)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(NCBlocks.crusherIdle, true,
				new Object[] {"PPP", "PCP", "PPP", 'P', new ItemStack(NCItems.parts, 1, 1), 'C', new ItemStack(NCItems.parts, 1, 2)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(NCBlocks.electricCrusherIdle, true,
				new Object[] {"PRP", "RCR", "PRP", 'P', new ItemStack(NCItems.parts, 1, 1), 'R', Items.redstone, 'C', NCBlocks.crusherIdle}));
		GameRegistry.addRecipe(new ShapedOreRecipe(NCBlocks.electricFurnaceIdle, true,
				new Object[] {"PRP", "RCR", "PRP", 'P', new ItemStack(NCItems.parts, 1, 1), 'R', Items.redstone, 'C', NCBlocks.furnaceIdle}));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(NCBlocks.nuclearWorkspace, true,
				new Object[] {"NNN", " T ", "TTT", 'N', new ItemStack(NCItems.parts, 1, 0), 'T', "ingotTough"}));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(NCBlocks.graphiteBlock, true,
				new Object[] {"CDC", "DCD", "CDC", 'D', "dustCoal", 'C', Items.coal}));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(NCItems.dominoes, true,
				new Object[] {"H", "C", 'H', new ItemStack(Items.skull, 1, 4), 'C', Items.bread}));
	
		// Tool Crafting Recipes
		GameRegistry.addRecipe(new ShapedOreRecipe(NCItems.bronzePickaxe, true,
				new Object[] {"XXX", " S ", " S ", 'X', "ingotBronze", 'S', Items.stick}));
		GameRegistry.addRecipe(new ShapedOreRecipe(NCItems.bronzeShovel, true,
				new Object[] {"X", "S", "S", 'X', "ingotBronze", 'S', Items.stick}));
		GameRegistry.addRecipe(new ShapedOreRecipe(NCItems.bronzeAxe, true,
				new Object[] {"XX", "XS", " S", 'X', "ingotBronze", 'S', Items.stick}));
		GameRegistry.addRecipe(new ShapedOreRecipe(NCItems.bronzeAxe, true,
				new Object[] {"XX", "SX", "S ", 'X', "ingotBronze", 'S', Items.stick}));
		GameRegistry.addRecipe(new ShapedOreRecipe(NCItems.bronzeHoe, true,
				new Object[] {"XX", "S ", "S ", 'X', "ingotBronze", 'S', Items.stick}));
		GameRegistry.addRecipe(new ShapedOreRecipe(NCItems.bronzeHoe, true,
				new Object[] {"XX", " S", " S", 'X', "ingotBronze", 'S', Items.stick}));
		GameRegistry.addRecipe(new ShapedOreRecipe(NCItems.bronzeSword, true,
				new Object[] {"X", "X", "S", 'X', "ingotBronze", 'S', Items.stick}));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(NCItems.boronPickaxe, true,
				new Object[] {"XXX", " S ", " S ", 'X', "ingotBoron", 'S', Items.stick}));
		GameRegistry.addRecipe(new ShapedOreRecipe(NCItems.boronShovel, true,
				new Object[] {"X", "S", "S", 'X', "ingotBoron", 'S', Items.stick}));
		GameRegistry.addRecipe(new ShapedOreRecipe(NCItems.boronAxe, true,
				new Object[] {"XX", "XS", " S", 'X', "ingotBoron", 'S', Items.stick}));
		GameRegistry.addRecipe(new ShapedOreRecipe(NCItems.boronAxe, true,
				new Object[] {"XX", "SX", "S ", 'X', "ingotBoron", 'S', Items.stick}));
		GameRegistry.addRecipe(new ShapedOreRecipe(NCItems.boronHoe, true,
				new Object[] {"XX", "S ", "S ", 'X', "ingotBoron", 'S', Items.stick}));
		GameRegistry.addRecipe(new ShapedOreRecipe(NCItems.boronHoe, true,
				new Object[] {"XX", " S", " S", 'X', "ingotBoron", 'S', Items.stick}));
		GameRegistry.addRecipe(new ShapedOreRecipe(NCItems.boronSword, true,
				new Object[] {"X", "X", "S", 'X', "ingotBoron", 'S', Items.stick}));
		
		// Armour Crafting Recipes
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(NCItems.boronHelm, 1), true,
				new Object[] {"XXX", "X X", 'X', "ingotBoron"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(NCItems.boronChest, 1), true,
				new Object[] {"X X", "XXX", "XXX", 'X', "ingotBoron"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(NCItems.boronLegs, 1), true,
				new Object[] {"XXX", "X X", "X X", 'X', "ingotBoron"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(NCItems.boronBoots, 1), true,
				new Object[] {"X X", "X X", 'X', "ingotBoron"}));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(NCItems.bronzeHelm, 1), true,
				new Object[] {"XXX", "X X", 'X', "ingotBronze"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(NCItems.bronzeChest, 1), true,
				new Object[] {"X X", "XXX", "XXX", 'X', "ingotBronze"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(NCItems.bronzeLegs, 1), true,
				new Object[] {"XXX", "X X", "X X", 'X', "ingotBronze"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(NCItems.bronzeBoots, 1), true,
				new Object[] {"X X", "X X", 'X', "ingotBronze"}));
	
		// Simple Shapeless Crafting Recipes
		GameRegistry.addShapelessRecipe(new ItemStack(NCItems.material, 9, 4),
				new Object[] {new ItemStack(NCBlocks.blockBlock, 1, 4)});
		GameRegistry.addShapelessRecipe(new ItemStack(NCItems.material, 9, 0),
				new Object[] {new ItemStack(NCBlocks.blockBlock, 1, 0)});
		GameRegistry.addShapelessRecipe(new ItemStack(NCItems.material, 9, 1),
				new Object[] {new ItemStack(NCBlocks.blockBlock, 1, 1)});
		GameRegistry.addShapelessRecipe(new ItemStack(NCItems.material, 9, 2),
				new Object[] {new ItemStack(NCBlocks.blockBlock, 1, 2)});
		GameRegistry.addShapelessRecipe(new ItemStack(NCItems.material, 9, 3),
				new Object[] {new ItemStack(NCBlocks.blockBlock, 1, 3)});
		GameRegistry.addShapelessRecipe(new ItemStack(NCItems.material, 9, 6),
				new Object[] {new ItemStack(NCBlocks.blockBlock, 1, 6)});
		GameRegistry.addShapelessRecipe(new ItemStack(NCItems.material, 9, 5),
				new Object[] {new ItemStack(NCBlocks.blockBlock, 1, 5)});
		GameRegistry.addShapelessRecipe(new ItemStack(NCItems.material, 9, 42),
				new Object[] {new ItemStack(NCBlocks.blockBlock, 1, 8)});
		GameRegistry.addShapelessRecipe(new ItemStack(NCItems.material, 9, 43),
				new Object[] {new ItemStack(NCBlocks.blockBlock, 1, 9)});
		GameRegistry.addShapelessRecipe(new ItemStack(NCItems.material, 25, 7),
				new Object[] {new ItemStack(NCBlocks.blockBlock, 1, 7)});
		GameRegistry.addShapelessRecipe(new ItemStack(NCItems.material, 9, 50),
				new Object[] {new ItemStack(NCBlocks.blockBlock, 1, 10)});
		
		GameRegistry.addShapelessRecipe(new ItemStack(NCBlocks.tubing1, 1),
				new Object[] {new ItemStack(NCBlocks.tubing2)});
		GameRegistry.addShapelessRecipe(new ItemStack(NCBlocks.tubing2, 1),
				new Object[] {new ItemStack(NCBlocks.tubing1)});
		
		// Complex Shapeless Crafting Recipes
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(NCItems.material, 4, 21),
				new Object[] {"dustCopper", "dustCopper", "dustCopper", "dustTin"}));
		
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(NCItems.material, 3, 72),
				new Object[] {"dustMagnesium", "dustBoron", "dustBoron"}));
		
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(NCItems.fuel, 1, 0),
				new Object[] {"U238", "U238", "U238", "U238", "U238", "U238", "U238", "U238", new ItemStack(NCItems.material, 1, 26)}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(NCItems.fuel, 1, 6),
				new Object[] {"U238", "U238", "U238", "U238", "U238", "U238", "U238", "U238", new ItemStack(NCItems.material, 1, 28)}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(NCItems.fuel, 1, 1),
				new Object[] {"U238", "U238", "U238", "U238", "U238", new ItemStack(NCItems.material, 1, 26), new ItemStack(NCItems.material, 1, 26), new ItemStack(NCItems.material, 1, 26), new ItemStack(NCItems.material, 1, 26)}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(NCItems.fuel, 1, 7),
				new Object[] {"U238", "U238", "U238", "U238", "U238", new ItemStack(NCItems.material, 1, 28), new ItemStack(NCItems.material, 1, 28), new ItemStack(NCItems.material, 1, 28), new ItemStack(NCItems.material, 1, 28)}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(NCItems.fuel, 1, 2),
				new Object[] {"Pu242", "Pu242", "Pu242", "Pu242", "Pu242", "Pu242", "Pu242", "Pu242", new ItemStack(NCItems.material, 1, 32)}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(NCItems.fuel, 1, 8),
				new Object[] {"Pu242", "Pu242", "Pu242", "Pu242", "Pu242", "Pu242", "Pu242", "Pu242", new ItemStack(NCItems.material, 1, 36)}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(NCItems.fuel, 1, 3),
				new Object[] {"Pu242", "Pu242", "Pu242", "Pu242", "Pu242", new ItemStack(NCItems.material, 1, 32), new ItemStack(NCItems.material, 1, 32), new ItemStack(NCItems.material, 1, 32), new ItemStack(NCItems.material, 1, 32)}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(NCItems.fuel, 1, 9),
				new Object[] {"Pu242", "Pu242", "Pu242", "Pu242", "Pu242", new ItemStack(NCItems.material, 1, 36), new ItemStack(NCItems.material, 1, 36), new ItemStack(NCItems.material, 1, 36), new ItemStack(NCItems.material, 1, 36)}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(NCItems.fuel, 1, 5),
				new Object[] {new ItemStack(NCItems.material, 1, 38), new ItemStack(NCItems.material, 1, 38), new ItemStack(NCItems.material, 1, 38), new ItemStack(NCItems.material, 1, 38), new ItemStack(NCItems.material, 1, 38), new ItemStack(NCItems.material, 1, 38), new ItemStack(NCItems.material, 1, 38), new ItemStack(NCItems.material, 1, 38), new ItemStack(NCItems.material, 1, 38)}));
		
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(NCItems.fuel, 1, 51),
				new Object[] {"U238", "U238", "U238", "U238", "U238", "U238", "U238", "U238", new ItemStack(NCItems.material, 1, 57)}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(NCItems.fuel, 1, 55),
				new Object[] {"U238", "U238", "U238", "U238", "U238", "U238", "U238", "U238", new ItemStack(NCItems.material, 1, 59)}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(NCItems.fuel, 1, 52),
				new Object[] {"U238", "U238", "U238", "U238", "U238", new ItemStack(NCItems.material, 1, 57), new ItemStack(NCItems.material, 1, 57), new ItemStack(NCItems.material, 1, 57), new ItemStack(NCItems.material, 1, 57)}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(NCItems.fuel, 1, 56),
				new Object[] {"U238", "U238", "U238", "U238", "U238", new ItemStack(NCItems.material, 1, 59), new ItemStack(NCItems.material, 1, 59), new ItemStack(NCItems.material, 1, 59), new ItemStack(NCItems.material, 1, 59)}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(NCItems.fuel, 1, 53),
				new Object[] {"Pu242", "Pu242", "Pu242", "Pu242", "Pu242", "Pu242", "Pu242", "Pu242", new ItemStack(NCItems.material, 1, 63)}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(NCItems.fuel, 1, 57),
				new Object[] {"Pu242", "Pu242", "Pu242", "Pu242", "Pu242", "Pu242", "Pu242", "Pu242", new ItemStack(NCItems.material, 1, 67)}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(NCItems.fuel, 1, 54),
				new Object[] {"Pu242", "Pu242", "Pu242", "Pu242", "Pu242", new ItemStack(NCItems.material, 1, 63), new ItemStack(NCItems.material, 1, 63), new ItemStack(NCItems.material, 1, 63), new ItemStack(NCItems.material, 1, 63)}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(NCItems.fuel, 1, 58),
				new Object[] {"Pu242", "Pu242", "Pu242", "Pu242", "Pu242", new ItemStack(NCItems.material, 1, 67), new ItemStack(NCItems.material, 1, 67), new ItemStack(NCItems.material, 1, 67), new ItemStack(NCItems.material, 1, 67)}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(NCItems.fuel, 1, 4),
				new Object[] {"U238", "U238", "U238", "U238", "U238", "U238", "U238", "U238", new ItemStack(NCItems.material, 1, 63)}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(NCItems.fuel, 1, 10),
				new Object[] {"U238", "U238", "U238", "U238", "U238", "U238", "U238", "U238", new ItemStack(NCItems.material, 1, 67)}));
		
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(NCItems.parts, 3, 4),
				new Object[] {Items.sugar, "dustLapis", Items.redstone}));
		
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(NCItems.material, 4, 22),
				new Object[] {new ItemStack(NCItems.parts, 1, 4), "dustCoal", "dustCoal", "dustLead", "dustLead", "dustSilver", "dustSilver", "dustIron", "dustIron"}));
		
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(NCItems.fuel, 1, 45),
				new Object[] {"filledNCGasCell"}));
		
		GameRegistry.addShapelessRecipe(new ItemStack(NCItems.fuel, 1, 11),
				(new ItemStack (NCItems.fuel, 1, 0)), (new ItemStack (NCItems.fuel, 1, 33)));
		GameRegistry.addShapelessRecipe(new ItemStack(NCItems.fuel, 1, 17),
				(new ItemStack (NCItems.fuel, 1, 6)), (new ItemStack (NCItems.fuel, 1, 33)));
		GameRegistry.addShapelessRecipe(new ItemStack(NCItems.fuel, 1, 12),
				(new ItemStack (NCItems.fuel, 1, 1)), (new ItemStack (NCItems.fuel, 1, 33)));
		GameRegistry.addShapelessRecipe(new ItemStack(NCItems.fuel, 1, 18),
				(new ItemStack (NCItems.fuel, 1, 7)), (new ItemStack (NCItems.fuel, 1, 33)));
		GameRegistry.addShapelessRecipe(new ItemStack(NCItems.fuel, 1, 13),
				(new ItemStack (NCItems.fuel, 1, 2)), (new ItemStack (NCItems.fuel, 1, 33)));
		GameRegistry.addShapelessRecipe(new ItemStack(NCItems.fuel, 1, 19),
				(new ItemStack (NCItems.fuel, 1, 8)), (new ItemStack (NCItems.fuel, 1, 33)));
		GameRegistry.addShapelessRecipe(new ItemStack(NCItems.fuel, 1, 14),
				(new ItemStack (NCItems.fuel, 1, 3)), (new ItemStack (NCItems.fuel, 1, 33)));
		GameRegistry.addShapelessRecipe(new ItemStack(NCItems.fuel, 1, 20),
				(new ItemStack (NCItems.fuel, 1, 9)), (new ItemStack (NCItems.fuel, 1, 33)));
		GameRegistry.addShapelessRecipe(new ItemStack(NCItems.fuel, 1, 15),
				(new ItemStack (NCItems.fuel, 1, 4)), (new ItemStack (NCItems.fuel, 1, 33)));
		GameRegistry.addShapelessRecipe(new ItemStack(NCItems.fuel, 1, 21),
				(new ItemStack (NCItems.fuel, 1, 10)), (new ItemStack (NCItems.fuel, 1, 33)));
		GameRegistry.addShapelessRecipe(new ItemStack(NCItems.fuel, 1, 16),
				(new ItemStack (NCItems.fuel, 1, 5)), (new ItemStack (NCItems.fuel, 1, 33)));
		
		GameRegistry.addShapelessRecipe(new ItemStack(NCItems.fuel, 1, 59),
				(new ItemStack (NCItems.fuel, 1, 51)), (new ItemStack (NCItems.fuel, 1, 33)));
		GameRegistry.addShapelessRecipe(new ItemStack(NCItems.fuel, 1, 63),
				(new ItemStack (NCItems.fuel, 1, 55)), (new ItemStack (NCItems.fuel, 1, 33)));
		GameRegistry.addShapelessRecipe(new ItemStack(NCItems.fuel, 1, 60),
				(new ItemStack (NCItems.fuel, 1, 52)), (new ItemStack (NCItems.fuel, 1, 33)));
		GameRegistry.addShapelessRecipe(new ItemStack(NCItems.fuel, 1, 64),
				(new ItemStack (NCItems.fuel, 1, 56)), (new ItemStack (NCItems.fuel, 1, 33)));
		GameRegistry.addShapelessRecipe(new ItemStack(NCItems.fuel, 1, 61),
				(new ItemStack (NCItems.fuel, 1, 53)), (new ItemStack (NCItems.fuel, 1, 33)));
		GameRegistry.addShapelessRecipe(new ItemStack(NCItems.fuel, 1, 65),
				(new ItemStack (NCItems.fuel, 1, 57)), (new ItemStack (NCItems.fuel, 1, 33)));
		GameRegistry.addShapelessRecipe(new ItemStack(NCItems.fuel, 1, 62),
				(new ItemStack (NCItems.fuel, 1, 54)), (new ItemStack (NCItems.fuel, 1, 33)));
		GameRegistry.addShapelessRecipe(new ItemStack(NCItems.fuel, 1, 66),
				(new ItemStack (NCItems.fuel, 1, 58)), (new ItemStack (NCItems.fuel, 1, 33)));
		
		GameRegistry.addShapelessRecipe(new ItemStack(NCItems.fuel, 1, 41),
				(new ItemStack (NCItems.material, 1, 46)), (new ItemStack (NCItems.fuel, 1, 33)));
		GameRegistry.addShapelessRecipe(new ItemStack(NCItems.fuel, 1, 42),
				(new ItemStack (NCItems.material, 1, 47)), (new ItemStack (NCItems.fuel, 1, 33)));
		GameRegistry.addShapelessRecipe(new ItemStack(NCItems.fuel, 1, 43),
				(new ItemStack (NCItems.material, 1, 48)), (new ItemStack (NCItems.fuel, 1, 33)));
		GameRegistry.addShapelessRecipe(new ItemStack(NCItems.fuel, 1, 44),
				(new ItemStack (NCItems.material, 1, 49)), (new ItemStack (NCItems.fuel, 1, 33)));
		
		// Smelting Recipes
		GameRegistry.addSmelting(new ItemStack(NCBlocks.blockOre, 1, 4), new ItemStack (NCItems.material, 1, 4), 1.2F);
		GameRegistry.addSmelting(new ItemStack(NCBlocks.blockOre, 1, 0), new ItemStack(NCItems.material, 1, 0), 0.6F);
		GameRegistry.addSmelting(new ItemStack(NCBlocks.blockOre, 1, 1), new ItemStack(NCItems.material, 1, 1), 0.6F);
		GameRegistry.addSmelting(new ItemStack(NCBlocks.blockOre, 1, 2), new ItemStack(NCItems.material, 1, 2), 0.8F);
		GameRegistry.addSmelting(new ItemStack(NCBlocks.blockOre, 1, 3), new ItemStack(NCItems.material, 1, 3), 0.8F);
		GameRegistry.addSmelting(new ItemStack(NCBlocks.blockOre, 1, 5), new ItemStack(NCItems.material, 1, 5), 1.2F);
		GameRegistry.addSmelting(new ItemStack(NCBlocks.blockOre, 1, 6), new ItemStack(NCItems.material, 1, 33), 1.2F);
		GameRegistry.addSmelting(new ItemStack(NCBlocks.blockOre, 1, 7), new ItemStack(NCItems.material, 1, 42), 0.8F);
		GameRegistry.addSmelting(new ItemStack(NCBlocks.blockOre, 1, 8), new ItemStack(NCItems.material, 1, 43), 0.8F);
		GameRegistry.addSmelting(new ItemStack(NCBlocks.blockOre, 1, 9), new ItemStack(NCItems.material, 1, 50), 0.8F);
		
		GameRegistry.addSmelting(new ItemStack (NCItems.material, 1, 8), new ItemStack(Items.iron_ingot), 0.0F);
		GameRegistry.addSmelting(new ItemStack (NCItems.material, 1, 9), new ItemStack(Items.gold_ingot), 0.0F);
		GameRegistry.addSmelting(new ItemStack (NCItems.material, 1, 15), new ItemStack(NCItems.material, 1, 0), 0.0F);
		GameRegistry.addSmelting(new ItemStack (NCItems.material, 1, 17), new ItemStack(NCItems.material, 1, 2), 0.0F);
		GameRegistry.addSmelting(new ItemStack (NCItems.material, 1, 16), new ItemStack(NCItems.material, 1, 1), 0.0F);
		GameRegistry.addSmelting(new ItemStack (NCItems.material, 1, 18), new ItemStack(NCItems.material, 1, 3), 0.0F);
		GameRegistry.addSmelting(new ItemStack (NCItems.material, 1, 19), new ItemStack(NCItems.material, 1, 4), 0.0F);
		GameRegistry.addSmelting(new ItemStack (NCItems.material, 1, 20), new ItemStack(NCItems.material, 1, 5), 0.0F);
		GameRegistry.addSmelting(new ItemStack (NCItems.material, 1, 21), new ItemStack(NCItems.material, 1, 6), 0.0F);
		GameRegistry.addSmelting(new ItemStack (NCItems.material, 1, 22), new ItemStack(NCItems.material, 1, 7), 0.0F);
		GameRegistry.addSmelting(new ItemStack (NCItems.material, 1, 44), new ItemStack(NCItems.material, 1, 42), 0.0F);
		GameRegistry.addSmelting(new ItemStack (NCItems.material, 1, 45), new ItemStack(NCItems.material, 1, 43), 0.0F);
		GameRegistry.addSmelting(new ItemStack (NCItems.material, 1, 51), new ItemStack(NCItems.material, 1, 50), 0.0F);
		GameRegistry.addSmelting(new ItemStack (NCItems.material, 1, 54), new ItemStack(NCItems.material, 1, 53), 0.0F);
		GameRegistry.addSmelting(new ItemStack (NCItems.material, 1, 72), new ItemStack(NCItems.material, 1, 71), 0.0F);
		
		GameRegistry.addSmelting(new ItemStack (Items.egg, 1), new ItemStack(NCItems.boiledEgg, 1), 0.1F);
		
		// Gui Handler
		@SuppressWarnings("unused")
		GuiHandler guiHandler = new GuiHandler();
		
		// Proxy
		NCProxy.registerRenderThings();
		NCProxy.registerSounds();
		NCProxy.registerTileEntitySpecialRenderer();
		
		// Entities
		EntityHandler.registerMonsters(EntityNuclearMonster.class, "NuclearMonster");
		EntityHandler.registerPaul(EntityPaul.class, "Paul");
		EntityHandler.registerNuke(EntityNukePrimed.class, "NukePrimed");
		EntityHandler.registerNuclearGrenade(EntityNuclearGrenade.class, "NuclearGrenade");
		EntityHandler.registerEntityBullet(EntityBullet.class, "EntityBullet");
				
		// Fuel Handler	
		GameRegistry.registerFuelHandler(new FuelHandler());
			
		// Random Chest Loot
		if (enableLoot) {
			ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.dominoes, 1), 4, 5, 80/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.upgrade, 1), 2, 3, 60/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.fuel, 1, 4), 1, 2, 80/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NCBlocks.WRTG, 1), 1, 1, 20/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.material, 1, 32), 2, 5, 40/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.material, 1, 59), 3, 4, 60/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.fuel, 1, 47), 1, 2, 20/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NCBlocks.nuclearWorkspace, 1), 1, 1, 10/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NCBlocks.blastBlock, 1), 6, 12, 50/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.boronHelm, 1), 1, 1, 80/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.boronChest, 1), 1, 1, 60/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.boronLegs, 1), 1, 1, 60/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.boronBoots, 1), 1, 1, 80/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.pistol, 1), 1, 1, 50/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.dUBullet, 1), 6, 8, 50/NuclearCraft.lootModifier));
			
			ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.dominoes, 1), 4, 5, 80/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.upgrade, 1), 2, 3, 60/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.fuel, 1, 4), 1, 2, 80/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NCBlocks.WRTG, 1), 1, 1, 20/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.material, 1, 32), 2, 5, 40/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.material, 1, 59), 3, 4, 60/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.fuel, 1, 47), 1, 2, 20/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NCBlocks.nuclearWorkspace, 1), 1, 1, 10/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NCBlocks.blastBlock, 1), 6, 12, 50/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.boronHelm, 1), 1, 1, 80/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.boronChest, 1), 1, 1, 60/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.boronLegs, 1), 1, 1, 60/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.boronBoots, 1), 1, 1, 80/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.pistol, 1), 1, 1, 50/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.dUBullet, 1), 6, 8, 50/NuclearCraft.lootModifier));
			
			ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.dominoes, 1), 4, 5, 80/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.upgrade, 1), 2, 3, 60/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.fuel, 1, 4), 1, 2, 80/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NCBlocks.WRTG, 1), 1, 1, 20/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.material, 1, 32), 2, 5, 40/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.material, 1, 59), 3, 4, 60/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.fuel, 1, 47), 1, 2, 20/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NCBlocks.nuclearWorkspace, 1), 1, 1, 10/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NCBlocks.blastBlock, 1), 6, 12, 50/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.boronHelm, 1), 1, 1, 80/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.boronChest, 1), 1, 1, 60/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.boronLegs, 1), 1, 1, 60/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.boronBoots, 1), 1, 1, 80/NuclearCraft.lootModifier));
			
			ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_DISPENSER).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.toughBow, 1), 1, 1, 200/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_DISPENSER).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.pistol, 1), 1, 1, 100/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_DISPENSER).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.dUBullet, 1), 6, 8, 200/NuclearCraft.lootModifier));
			
			ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.dominoes, 1), 4, 5, 80/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.upgrade, 1), 2, 3, 60/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.fuel, 1, 4), 1, 2, 80/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NCBlocks.WRTG, 1), 1, 1, 20/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.material, 1, 32), 2, 5, 40/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.material, 1, 59), 3, 4, 60/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.fuel, 1, 47), 1, 2, 20/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NCBlocks.nuclearWorkspace, 1), 1, 1, 10/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NCBlocks.blastBlock, 1), 6, 12, 50/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.boronHelm, 1), 1, 1, 80/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.boronChest, 1), 1, 1, 60/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.boronLegs, 1), 1, 1, 60/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.boronBoots, 1), 1, 1, 80/NuclearCraft.lootModifier));
			
			ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.dominoes, 1), 4, 5, 80/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.upgrade, 1), 2, 3, 60/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.fuel, 1, 4), 1, 2, 80/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(new WeightedRandomChestContent(new ItemStack(NCBlocks.WRTG, 1), 1, 1, 20/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.material, 1, 32), 2, 5, 40/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.material, 1, 59), 3, 4, 60/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.fuel, 1, 47), 1, 2, 20/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(new WeightedRandomChestContent(new ItemStack(NCBlocks.nuclearWorkspace, 1), 1, 1, 10/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(new WeightedRandomChestContent(new ItemStack(NCBlocks.blastBlock, 1), 6, 12, 50/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.boronHelm, 1), 1, 1, 80/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.boronChest, 1), 1, 1, 60/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.boronLegs, 1), 1, 1, 60/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.boronBoots, 1), 1, 1, 80/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.pistol, 1), 1, 1, 50/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.dUBullet, 1), 6, 8, 50/NuclearCraft.lootModifier));
			
			ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.dominoes, 1), 4, 5, 80/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.upgrade, 1), 2, 3, 60/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.fuel, 1, 4), 1, 2, 80/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NCBlocks.WRTG, 1), 1, 1, 20/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.material, 1, 32), 2, 5, 40/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.material, 1, 59), 3, 4, 60/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.fuel, 1, 47), 1, 2, 20/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NCBlocks.nuclearWorkspace, 1), 1, 1, 10/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NCBlocks.blastBlock, 1), 6, 12, 50/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.toughHelm, 1), 1, 1, 80/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.toughChest, 1), 1, 1, 60/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.toughLegs, 1), 1, 1, 60/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.toughBoots, 1), 1, 1, 80/NuclearCraft.lootModifier));
			
			ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CROSSING).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.dominoes, 1), 4, 5, 80/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CROSSING).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.upgrade, 1), 2, 3, 60/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CROSSING).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.fuel, 1, 4), 1, 2, 80/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CROSSING).addItem(new WeightedRandomChestContent(new ItemStack(NCBlocks.WRTG, 1), 1, 1, 20/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CROSSING).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.material, 1, 32), 2, 5, 40/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CROSSING).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.material, 1, 59), 3, 4, 60/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CROSSING).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.fuel, 1, 47), 1, 2, 20/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CROSSING).addItem(new WeightedRandomChestContent(new ItemStack(NCBlocks.nuclearWorkspace, 1), 1, 1, 10/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CROSSING).addItem(new WeightedRandomChestContent(new ItemStack(NCBlocks.blastBlock, 1), 6, 12, 50/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CROSSING).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.toughHelm, 1), 1, 1, 80/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CROSSING).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.toughChest, 1), 1, 1, 60/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CROSSING).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.toughLegs, 1), 1, 1, 60/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CROSSING).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.toughBoots, 1), 1, 1, 80/NuclearCraft.lootModifier));
			
			ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_LIBRARY).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.dominoes, 1), 4, 5, 80/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_LIBRARY).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.upgrade, 1), 2, 3, 60/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_LIBRARY).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.fuel, 1, 4), 1, 2, 80/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_LIBRARY).addItem(new WeightedRandomChestContent(new ItemStack(NCBlocks.WRTG, 1), 1, 1, 20/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_LIBRARY).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.material, 1, 32), 2, 5, 40/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_LIBRARY).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.material, 1, 59), 3, 4, 60/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_LIBRARY).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.fuel, 1, 47), 1, 2, 20/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_LIBRARY).addItem(new WeightedRandomChestContent(new ItemStack(NCBlocks.nuclearWorkspace, 1), 1, 1, 10/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_LIBRARY).addItem(new WeightedRandomChestContent(new ItemStack(NCBlocks.blastBlock, 1), 6, 12, 50/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_LIBRARY).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.toughHelm, 1), 1, 1, 80/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_LIBRARY).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.toughChest, 1), 1, 1, 60/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_LIBRARY).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.toughLegs, 1), 1, 1, 60/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_LIBRARY).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.toughBoots, 1), 1, 1, 80/NuclearCraft.lootModifier));
			
			ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.dominoes, 1), 2, 4, 20/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.boiledEgg, 1), 3, 5, 20/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.boronHelm, 1), 1, 1, 20/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.boronChest, 1), 1, 1, 20/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.boronLegs, 1), 1, 1, 20/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.boronBoots, 1), 1, 1, 20/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.bronzeHelm, 1), 1, 1, 20/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.bronzeChest, 1), 1, 1, 20/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.bronzeLegs, 1), 1, 1, 20/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.bronzeBoots, 1), 1, 1, 20/NuclearCraft.lootModifier));
			ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NCItems.toughAlloyShovel, 1), 1, 1, 20/NuclearCraft.lootModifier));
		}
			
		// World Generation Registry
		GameRegistry.registerWorldGenerator(new OreGen(), 1);
		
		// Inter Mod Comms - Mekanism
		NBTTagCompound copperOreEnrichment = new NBTTagCompound();
		copperOreEnrichment.setTag("input", new ItemStack(NCBlocks.blockOre, 1, 0).writeToNBT(new NBTTagCompound()));
		copperOreEnrichment.setTag("output", new ItemStack(NCItems.material, 2, 15).writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "EnrichmentChamberRecipe", copperOreEnrichment);
		
		NBTTagCompound tinOreEnrichment = new NBTTagCompound();
		tinOreEnrichment.setTag("input", new ItemStack(NCBlocks.blockOre, 1, 1).writeToNBT(new NBTTagCompound()));
		tinOreEnrichment.setTag("output", new ItemStack(NCItems.material, 2, 16).writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "EnrichmentChamberRecipe", tinOreEnrichment);
		
		NBTTagCompound leadOreEnrichment = new NBTTagCompound();
		leadOreEnrichment.setTag("input", new ItemStack(NCBlocks.blockOre, 1, 2).writeToNBT(new NBTTagCompound()));
		leadOreEnrichment.setTag("output", new ItemStack(NCItems.material, 2, 17).writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "EnrichmentChamberRecipe", leadOreEnrichment);
		
		NBTTagCompound silverOreEnrichment = new NBTTagCompound();
		silverOreEnrichment.setTag("input", new ItemStack(NCBlocks.blockOre, 1, 3).writeToNBT(new NBTTagCompound()));
		silverOreEnrichment.setTag("output", new ItemStack(NCItems.material, 2, 18).writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "EnrichmentChamberRecipe", silverOreEnrichment);
		
		NBTTagCompound uraniumOreEnrichment = new NBTTagCompound();
		uraniumOreEnrichment.setTag("input", new ItemStack(NCBlocks.blockOre, 1, 4).writeToNBT(new NBTTagCompound()));
		uraniumOreEnrichment.setTag("output", new ItemStack(NCItems.material, 2, 19).writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "EnrichmentChamberRecipe", uraniumOreEnrichment);
		
		NBTTagCompound thoriumOreEnrichment = new NBTTagCompound();
		thoriumOreEnrichment.setTag("input", new ItemStack(NCBlocks.blockOre, 1, 5).writeToNBT(new NBTTagCompound()));
		thoriumOreEnrichment.setTag("output", new ItemStack(NCItems.material, 2, 20).writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "EnrichmentChamberRecipe", thoriumOreEnrichment);
		
		NBTTagCompound plutoniumOreEnrichment = new NBTTagCompound();
		plutoniumOreEnrichment.setTag("input", new ItemStack(NCBlocks.blockOre, 1, 6).writeToNBT(new NBTTagCompound()));
		plutoniumOreEnrichment.setTag("output", new ItemStack(NCItems.material, 2, 33).writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "EnrichmentChamberRecipe", plutoniumOreEnrichment);
		
		NBTTagCompound lithiumOreEnrichment = new NBTTagCompound();
		lithiumOreEnrichment.setTag("input", new ItemStack(NCBlocks.blockOre, 1, 7).writeToNBT(new NBTTagCompound()));
		lithiumOreEnrichment.setTag("output", new ItemStack(NCItems.material, 2, 44).writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "EnrichmentChamberRecipe", lithiumOreEnrichment);
		
		NBTTagCompound boronOreEnrichment = new NBTTagCompound();
		boronOreEnrichment.setTag("input", new ItemStack(NCBlocks.blockOre, 1, 8).writeToNBT(new NBTTagCompound()));
		boronOreEnrichment.setTag("output", new ItemStack(NCItems.material, 2, 45).writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "EnrichmentChamberRecipe", boronOreEnrichment);
		
		NBTTagCompound magnesiumOreEnrichment = new NBTTagCompound();
		magnesiumOreEnrichment.setTag("input", new ItemStack(NCBlocks.blockOre, 1, 9).writeToNBT(new NBTTagCompound()));
		magnesiumOreEnrichment.setTag("output", new ItemStack(NCItems.material, 2, 51).writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "EnrichmentChamberRecipe", magnesiumOreEnrichment);
		
		NBTTagCompound basicPlatingEnrichment = new NBTTagCompound();
		basicPlatingEnrichment.setTag("input", new ItemStack(NCItems.parts, 4, 0).writeToNBT(new NBTTagCompound()));
		basicPlatingEnrichment.setTag("output", new ItemStack(NCItems.parts, 1, 3).writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "EnrichmentChamberRecipe", basicPlatingEnrichment);
		
		NBTTagCompound ingotToPlatingEnrichment = new NBTTagCompound();
		ingotToPlatingEnrichment.setTag("input", new ItemStack(NCItems.material, 1, 7).writeToNBT(new NBTTagCompound()));
		ingotToPlatingEnrichment.setTag("output", new ItemStack(NCItems.parts, 3, 0).writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "EnrichmentChamberRecipe", ingotToPlatingEnrichment);
		
		NBTTagCompound uraniumIngotCrushing = new NBTTagCompound();
		uraniumIngotCrushing.setTag("input", new ItemStack(NCItems.material, 1, 4).writeToNBT(new NBTTagCompound()));
		uraniumIngotCrushing.setTag("output", new ItemStack(NCItems.material, 1, 19).writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "CrusherRecipe", uraniumIngotCrushing);
		
		NBTTagCompound thoriumIngotCrushing = new NBTTagCompound();
		thoriumIngotCrushing.setTag("input", new ItemStack(NCItems.material, 1, 5).writeToNBT(new NBTTagCompound()));
		thoriumIngotCrushing.setTag("output", new ItemStack(NCItems.material, 1, 20).writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "CrusherRecipe", thoriumIngotCrushing);
		
		NBTTagCompound bronzeIngotCrushing = new NBTTagCompound();
		bronzeIngotCrushing.setTag("input", new ItemStack(NCItems.material, 1, 6).writeToNBT(new NBTTagCompound()));
		bronzeIngotCrushing.setTag("output", new ItemStack(NCItems.material, 1, 21).writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "CrusherRecipe", bronzeIngotCrushing);
		
		NBTTagCompound toughIngotCrushing = new NBTTagCompound();
		toughIngotCrushing.setTag("input", new ItemStack(NCItems.material, 1, 7).writeToNBT(new NBTTagCompound()));
		toughIngotCrushing.setTag("output", new ItemStack(NCItems.material, 1, 22).writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "CrusherRecipe", toughIngotCrushing);
		
		NBTTagCompound lithiumIngotCrushing = new NBTTagCompound();
		lithiumIngotCrushing.setTag("input", new ItemStack(NCItems.material, 1, 42).writeToNBT(new NBTTagCompound()));
		lithiumIngotCrushing.setTag("output", new ItemStack(NCItems.material, 1, 44).writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "CrusherRecipe", lithiumIngotCrushing);
		
		NBTTagCompound boronIngotCrushing = new NBTTagCompound();
		boronIngotCrushing.setTag("input", new ItemStack(NCItems.material, 1, 43).writeToNBT(new NBTTagCompound()));
		boronIngotCrushing.setTag("output", new ItemStack(NCItems.material, 1, 45).writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "CrusherRecipe", boronIngotCrushing);
		
		NBTTagCompound magnesiumIngotCrushing = new NBTTagCompound();
		magnesiumIngotCrushing.setTag("input", new ItemStack(NCItems.material, 1, 50).writeToNBT(new NBTTagCompound()));
		magnesiumIngotCrushing.setTag("output", new ItemStack(NCItems.material, 1, 51).writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "CrusherRecipe", magnesiumIngotCrushing);
		
		NBTTagCompound mgbIngotCrushing = new NBTTagCompound();
		mgbIngotCrushing.setTag("input", new ItemStack(NCItems.material, 1, 71).writeToNBT(new NBTTagCompound()));
		mgbIngotCrushing.setTag("output", new ItemStack(NCItems.material, 1, 72).writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "CrusherRecipe", mgbIngotCrushing);
		
		NBTTagCompound leadCombining = new NBTTagCompound();
		leadCombining.setTag("input", new ItemStack(NCItems.material, 8, 17).writeToNBT(new NBTTagCompound()));
		leadCombining.setTag("output", new ItemStack(NCBlocks.blockOre, 1, 2).writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "CombinerRecipe", leadCombining);
		
		NBTTagCompound silverCombining = new NBTTagCompound();
		silverCombining.setTag("input", new ItemStack(NCItems.material, 8, 18).writeToNBT(new NBTTagCompound()));
		silverCombining.setTag("output", new ItemStack(NCBlocks.blockOre, 1, 3).writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "CombinerRecipe", silverCombining);
		
		NBTTagCompound uraniumCombining = new NBTTagCompound();
		uraniumCombining.setTag("input", new ItemStack(NCItems.material, 8, 19).writeToNBT(new NBTTagCompound()));
		uraniumCombining.setTag("output", new ItemStack(NCBlocks.blockOre, 1, 4).writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "CombinerRecipe", uraniumCombining);
		
		NBTTagCompound thoriumCombining = new NBTTagCompound();
		thoriumCombining.setTag("input", new ItemStack(NCItems.material, 8, 20).writeToNBT(new NBTTagCompound()));
		thoriumCombining.setTag("output", new ItemStack(NCBlocks.blockOre, 1, 5).writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "CombinerRecipe", thoriumCombining);
		
		NBTTagCompound lithiumCombining = new NBTTagCompound();
		lithiumCombining.setTag("input", new ItemStack(NCItems.material, 8, 44).writeToNBT(new NBTTagCompound()));
		lithiumCombining.setTag("output", new ItemStack(NCBlocks.blockOre, 1, 7).writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "CombinerRecipe", lithiumCombining);
		
		NBTTagCompound boronCombining = new NBTTagCompound();
		boronCombining.setTag("input", new ItemStack(NCItems.material, 8, 45).writeToNBT(new NBTTagCompound()));
		boronCombining.setTag("output", new ItemStack(NCBlocks.blockOre, 1, 8).writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "CombinerRecipe", boronCombining);
		
		NBTTagCompound aluminiumCombining = new NBTTagCompound();
		aluminiumCombining.setTag("input", new ItemStack(NCItems.material, 8, 51).writeToNBT(new NBTTagCompound()));
		aluminiumCombining.setTag("output", new ItemStack(NCBlocks.blockOre, 1, 9).writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "CombinerRecipe", aluminiumCombining);
		
		// Inter Mod Comms - AE2
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", TileNuclearFurnace.class.getCanonicalName());
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", TileFurnace.class.getCanonicalName());
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", TileCrusher.class.getCanonicalName());
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", TileElectricCrusher.class.getCanonicalName());
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", TileElectricFurnace.class.getCanonicalName());
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", TileReactionGenerator.class.getCanonicalName());
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", TileSeparator.class.getCanonicalName());
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", TileHastener.class.getCanonicalName());
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", TileCollector.class.getCanonicalName());
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", TileFissionReactor.class.getCanonicalName());
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", TileNuclearWorkspace.class.getCanonicalName());
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", TileFusionReactor.class.getCanonicalName());
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", TileTubing1.class.getCanonicalName());
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", TileTubing2.class.getCanonicalName());
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", TileRTG.class.getCanonicalName());
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", TileWRTG.class.getCanonicalName());
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", TileFusionReactorBlock.class.getCanonicalName());
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", TileElectrolyser.class.getCanonicalName());
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", TileOxidiser.class.getCanonicalName());
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", TileIoniser.class.getCanonicalName());
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", TileIrradiator.class.getCanonicalName());
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", TileCooler.class.getCanonicalName());
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", TileFactory.class.getCanonicalName());
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", TileHeliumExtractor.class.getCanonicalName());
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", TileSolarPanel.class.getCanonicalName());
		
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", TileElectromagnet.class.getCanonicalName());
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", TileSupercooler.class.getCanonicalName());
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", TileSynchrotron.class.getCanonicalName());
		
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", TileSimpleQuantum.class.getCanonicalName());
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		// Ores Ore Dictionary
		OreDictionary.registerOre("oreUranium", new ItemStack(NCBlocks.blockOre, 1, 4));
		OreDictionary.registerOre("oreCopper", new ItemStack(NCBlocks.blockOre, 1, 0));
		OreDictionary.registerOre("oreTin", new ItemStack(NCBlocks.blockOre, 1, 1));
		OreDictionary.registerOre("oreLead", new ItemStack(NCBlocks.blockOre, 1, 2));
		OreDictionary.registerOre("oreSilver", new ItemStack(NCBlocks.blockOre, 1, 3));
		OreDictionary.registerOre("oreThorium", new ItemStack(NCBlocks.blockOre, 1, 5));
		OreDictionary.registerOre("orePlutonium", new ItemStack(NCBlocks.blockOre, 1, 6));
		OreDictionary.registerOre("oreLithium", new ItemStack(NCBlocks.blockOre, 1, 7));
		OreDictionary.registerOre("oreBoron", new ItemStack(NCBlocks.blockOre, 1, 8));
		OreDictionary.registerOre("oreMagnesium", new ItemStack(NCBlocks.blockOre, 1, 9));
		
		// Items Ore Dictionary
		OreDictionary.registerOre("ingotUranium", new ItemStack(NCItems.material, 1, 4));
		OreDictionary.registerOre("ingotCopper", new ItemStack(NCItems.material, 1, 0));
		OreDictionary.registerOre("ingotTin", new ItemStack(NCItems.material, 1, 1));
		OreDictionary.registerOre("ingotLead", new ItemStack(NCItems.material, 1, 2));
		OreDictionary.registerOre("ingotSilver", new ItemStack(NCItems.material, 1, 3));
		OreDictionary.registerOre("ingotBronze", new ItemStack(NCItems.material, 1, 6));
		OreDictionary.registerOre("ingotThorium", new ItemStack(NCItems.material, 1, 5));
		OreDictionary.registerOre("ingotLithium", new ItemStack(NCItems.material, 1, 42));
		OreDictionary.registerOre("ingotBoron", new ItemStack(NCItems.material, 1, 43));
		OreDictionary.registerOre("ingotTough", new ItemStack(NCItems.material, 1, 7));
		OreDictionary.registerOre("ingotMagnesium", new ItemStack(NCItems.material, 1, 50));
		OreDictionary.registerOre("ingotUraniumOxide", new ItemStack(NCItems.material, 1, 53));
		OreDictionary.registerOre("ingotMagnesiumDiboride", new ItemStack(NCItems.material, 1, 71));
			
		// Dusts Ore Dictionary
		OreDictionary.registerOre("dustIron", new ItemStack(NCItems.material, 1, 8));
		OreDictionary.registerOre("dustGold", new ItemStack(NCItems.material, 1, 9));
		OreDictionary.registerOre("dustLapis", new ItemStack(NCItems.material, 1, 10));
		OreDictionary.registerOre("dustDiamond", new ItemStack(NCItems.material, 1, 11));
		OreDictionary.registerOre("dustEmerald", new ItemStack(NCItems.material, 1, 12));
		OreDictionary.registerOre("dustQuartz", new ItemStack(NCItems.material, 1, 13));
		OreDictionary.registerOre("dustCoal", new ItemStack(NCItems.material, 1, 14));
		OreDictionary.registerOre("dustCopper", new ItemStack(NCItems.material, 1, 15));
		OreDictionary.registerOre("dustLead", new ItemStack(NCItems.material, 1, 17));
		OreDictionary.registerOre("dustTin", new ItemStack(NCItems.material, 1, 16));
		OreDictionary.registerOre("dustSilver", new ItemStack(NCItems.material, 1, 18));
		OreDictionary.registerOre("dustUranium", new ItemStack(NCItems.material, 1, 19));
		OreDictionary.registerOre("dustThorium", new ItemStack(NCItems.material, 1, 20));
		OreDictionary.registerOre("dustBronze", new ItemStack(NCItems.material, 1, 21));
		OreDictionary.registerOre("dustLithium", new ItemStack(NCItems.material, 1, 44));
		OreDictionary.registerOre("dustBoron", new ItemStack(NCItems.material, 1, 45));
		OreDictionary.registerOre("dustTough", new ItemStack(NCItems.material, 1, 22));
		OreDictionary.registerOre("dustMagnesium", new ItemStack(NCItems.material, 1, 51));
		OreDictionary.registerOre("dustObsidian", new ItemStack(NCItems.material, 1, 52));
		OreDictionary.registerOre("dustUraniumOxide", new ItemStack(NCItems.material, 1, 54));
		OreDictionary.registerOre("dustMagnesiumDiboride", new ItemStack(NCItems.material, 1, 72));
		
		// Blocks Ore Dictionary
		OreDictionary.registerOre("blockUranium", new ItemStack(NCBlocks.blockBlock, 1, 4));
		OreDictionary.registerOre("blockCopper", new ItemStack(NCBlocks.blockBlock, 1, 0));
		OreDictionary.registerOre("blockTin", new ItemStack(NCBlocks.blockBlock, 1, 1));
		OreDictionary.registerOre("blockLead", new ItemStack(NCBlocks.blockBlock, 1, 2));
		OreDictionary.registerOre("blockSilver", new ItemStack(NCBlocks.blockBlock, 1, 3));
		OreDictionary.registerOre("blockBronze", new ItemStack(NCBlocks.blockBlock, 1, 6));
		OreDictionary.registerOre("blockThorium", new ItemStack(NCBlocks.blockBlock, 1, 5));
		OreDictionary.registerOre("blockTough", new ItemStack(NCBlocks.blockBlock, 1, 7));
		OreDictionary.registerOre("blockLithium", new ItemStack(NCBlocks.blockBlock, 1, 8));
		OreDictionary.registerOre("blockBoron", new ItemStack(NCBlocks.blockBlock, 1, 9));
		OreDictionary.registerOre("blockMagnesium", new ItemStack(NCBlocks.blockBlock, 1, 10));
		
		// Parts Ore Dictionary
		OreDictionary.registerOre("universalReactant", new ItemStack(NCItems.parts, 1, 4));
		OreDictionary.registerOre("plateLead", new ItemStack(NCItems.parts, 1, 0));
		OreDictionary.registerOre("plateIron", new ItemStack(NCItems.parts, 1, 1));
		OreDictionary.registerOre("plateTin", new ItemStack(NCItems.parts, 1, 6));
		
		// Non-Fissile Materials Ore Dictionary
		OreDictionary.registerOre("U238", new ItemStack(NCItems.material, 1, 24));
		OreDictionary.registerOre("U238", new ItemStack(NCItems.material, 1, 55));
		OreDictionary.registerOre("tinyU238", new ItemStack(NCItems.material, 1, 25));
		OreDictionary.registerOre("tinyU238", new ItemStack(NCItems.material, 1, 56));
		OreDictionary.registerOre("U235", new ItemStack(NCItems.material, 1, 26));
		OreDictionary.registerOre("U235", new ItemStack(NCItems.material, 1, 57));
		OreDictionary.registerOre("tinyU235", new ItemStack(NCItems.material, 1, 27));
		OreDictionary.registerOre("tinyU235", new ItemStack(NCItems.material, 1, 58));
		OreDictionary.registerOre("U233", new ItemStack(NCItems.material, 1, 28));
		OreDictionary.registerOre("U233", new ItemStack(NCItems.material, 1, 59));
		OreDictionary.registerOre("tinyU233", new ItemStack(NCItems.material, 1, 29));
		OreDictionary.registerOre("tinyU233", new ItemStack(NCItems.material, 1, 60));
		OreDictionary.registerOre("Pu238", new ItemStack(NCItems.material, 1, 30));
		OreDictionary.registerOre("Pu238", new ItemStack(NCItems.material, 1, 61));
		OreDictionary.registerOre("tinyPu238", new ItemStack(NCItems.material, 1, 31));
		OreDictionary.registerOre("tinyPu238", new ItemStack(NCItems.material, 1, 62));
		OreDictionary.registerOre("Pu239", new ItemStack(NCItems.material, 1, 32));
		OreDictionary.registerOre("Pu239", new ItemStack(NCItems.material, 1, 63));
		OreDictionary.registerOre("tinyPu239", new ItemStack(NCItems.material, 1, 33));
		OreDictionary.registerOre("tinyPu239", new ItemStack(NCItems.material, 1, 64));
		OreDictionary.registerOre("Pu242", new ItemStack(NCItems.material, 1, 34));
		OreDictionary.registerOre("Pu242", new ItemStack(NCItems.material, 1, 65));
		OreDictionary.registerOre("tinyPu242", new ItemStack(NCItems.material, 1, 35));
		OreDictionary.registerOre("tinyPu242", new ItemStack(NCItems.material, 1, 66));
		OreDictionary.registerOre("Pu241", new ItemStack(NCItems.material, 1, 36));
		OreDictionary.registerOre("Pu241", new ItemStack(NCItems.material, 1, 67));
		OreDictionary.registerOre("tinyPu241", new ItemStack(NCItems.material, 1, 37));
		OreDictionary.registerOre("tinyPu241", new ItemStack(NCItems.material, 1, 68));
		
		// Vanilla Ore Dictionary
		OreDictionary.registerOre("gemCoal", new ItemStack(Items.coal, 1));
		OreDictionary.registerOre("oreObsidian", new ItemStack(Blocks.obsidian, 1));
		
		// Filled Fluid Cell Dictionary
		OreDictionary.registerOre("filledNCGasCell", new ItemStack(NCItems.fuel, 1, 34));
		OreDictionary.registerOre("filledNCGasCell", new ItemStack(NCItems.fuel, 1, 35));
		OreDictionary.registerOre("filledNCGasCell", new ItemStack(NCItems.fuel, 1, 36));
		OreDictionary.registerOre("filledNCGasCell", new ItemStack(NCItems.fuel, 1, 37));
		OreDictionary.registerOre("filledNCGasCell", new ItemStack(NCItems.fuel, 1, 38));
		OreDictionary.registerOre("filledNCGasCell", new ItemStack(NCItems.fuel, 1, 39));
		OreDictionary.registerOre("filledNCGasCell", new ItemStack(NCItems.fuel, 1, 40));
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		// Mod Recipes
		IC2Hook = new ModRecipes();
		IC2Hook.hook();
	}
}