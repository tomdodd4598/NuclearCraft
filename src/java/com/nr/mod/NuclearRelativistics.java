package com.nr.mod;

import java.io.File;

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

import com.nr.mod.armor.BoronArmor;
import com.nr.mod.armor.BronzeArmor;
import com.nr.mod.armor.DUArmor;
import com.nr.mod.armor.ToughArmor;
import com.nr.mod.blocks.BlockBlastBlock;
import com.nr.mod.blocks.BlockBlock;
import com.nr.mod.blocks.BlockCellBlock;
import com.nr.mod.blocks.BlockCoolerBlock;
import com.nr.mod.blocks.BlockGraphiteBlock;
import com.nr.mod.blocks.BlockMachineBlock;
import com.nr.mod.blocks.BlockNuke;
import com.nr.mod.blocks.BlockNukeExploding;
import com.nr.mod.blocks.BlockOre;
import com.nr.mod.blocks.BlockReactorBlock;
import com.nr.mod.blocks.BlockSpeedBlock;
import com.nr.mod.blocks.NRBlocks;
import com.nr.mod.blocks.fluids.BlockHelium;
import com.nr.mod.blocks.fluids.FluidHelium;
import com.nr.mod.blocks.itemblock.ItemBlockBlock;
import com.nr.mod.blocks.itemblock.ItemBlockOre;
import com.nr.mod.blocks.tileentities.BlockAccStraight1;
import com.nr.mod.blocks.tileentities.BlockAccStraight2;
import com.nr.mod.blocks.tileentities.BlockCollector;
import com.nr.mod.blocks.tileentities.BlockCooler;
import com.nr.mod.blocks.tileentities.BlockCrusher;
import com.nr.mod.blocks.tileentities.BlockElectricCrusher;
import com.nr.mod.blocks.tileentities.BlockElectricFurnace;
import com.nr.mod.blocks.tileentities.BlockElectrolyser;
import com.nr.mod.blocks.tileentities.BlockFactory;
import com.nr.mod.blocks.tileentities.BlockFissionReactorGraphite;
import com.nr.mod.blocks.tileentities.BlockFurnace;
import com.nr.mod.blocks.tileentities.BlockFusionReactor;
import com.nr.mod.blocks.tileentities.BlockFusionReactorBlock;
import com.nr.mod.blocks.tileentities.BlockHastener;
import com.nr.mod.blocks.tileentities.BlockIoniser;
import com.nr.mod.blocks.tileentities.BlockIrradiator;
import com.nr.mod.blocks.tileentities.BlockNuclearFurnace;
import com.nr.mod.blocks.tileentities.BlockNuclearWorkspace;
import com.nr.mod.blocks.tileentities.BlockOxidiser;
import com.nr.mod.blocks.tileentities.BlockRTG;
import com.nr.mod.blocks.tileentities.BlockReactionGenerator;
import com.nr.mod.blocks.tileentities.BlockSeparator;
import com.nr.mod.blocks.tileentities.BlockSimpleQuantum;
import com.nr.mod.blocks.tileentities.BlockWRTG;
import com.nr.mod.blocks.tileentities.TileEntityAccStraight1;
import com.nr.mod.blocks.tileentities.TileEntityAccStraight2;
import com.nr.mod.blocks.tileentities.TileEntityCollector;
import com.nr.mod.blocks.tileentities.TileEntityCooler;
import com.nr.mod.blocks.tileentities.TileEntityCrusher;
import com.nr.mod.blocks.tileentities.TileEntityElectricCrusher;
import com.nr.mod.blocks.tileentities.TileEntityElectricFurnace;
import com.nr.mod.blocks.tileentities.TileEntityElectrolyser;
import com.nr.mod.blocks.tileentities.TileEntityFactory;
import com.nr.mod.blocks.tileentities.TileEntityFissionReactorGraphite;
import com.nr.mod.blocks.tileentities.TileEntityFurnace;
import com.nr.mod.blocks.tileentities.TileEntityFusionReactor;
import com.nr.mod.blocks.tileentities.TileEntityFusionReactorBlock;
import com.nr.mod.blocks.tileentities.TileEntityHastener;
import com.nr.mod.blocks.tileentities.TileEntityIoniser;
import com.nr.mod.blocks.tileentities.TileEntityIrradiator;
import com.nr.mod.blocks.tileentities.TileEntityNuclearFurnace;
import com.nr.mod.blocks.tileentities.TileEntityNuclearWorkspace;
import com.nr.mod.blocks.tileentities.TileEntityOxidiser;
import com.nr.mod.blocks.tileentities.TileEntityRTG;
import com.nr.mod.blocks.tileentities.TileEntityReactionGenerator;
import com.nr.mod.blocks.tileentities.TileEntitySeparator;
import com.nr.mod.blocks.tileentities.TileEntitySimpleQuantum;
import com.nr.mod.blocks.tileentities.TileEntityWRTG;
import com.nr.mod.entity.EntityBullet;
import com.nr.mod.entity.EntityNuclearGrenade;
import com.nr.mod.entity.EntityNuclearMonster;
import com.nr.mod.entity.EntityNukePrimed;
import com.nr.mod.entity.EntityPaul;
import com.nr.mod.gui.GuiHandler;
import com.nr.mod.handlers.EntityHandler;
import com.nr.mod.handlers.FuelHandler;
import com.nr.mod.items.ItemEnderChest;
import com.nr.mod.items.ItemFuel;
import com.nr.mod.items.ItemMaterial;
import com.nr.mod.items.ItemNuclearGrenade;
import com.nr.mod.items.ItemPart;
import com.nr.mod.items.ItemPistol;
import com.nr.mod.items.ItemToughBow;
import com.nr.mod.items.NRAxe;
import com.nr.mod.items.NRHoe;
import com.nr.mod.items.NRItems;
import com.nr.mod.items.NRPaxel;
import com.nr.mod.items.NRPickaxe;
import com.nr.mod.items.NRShovel;
import com.nr.mod.items.NRSword;
import com.nr.mod.proxy.CommonProxy;
import com.nr.mod.worldgen.OreGen;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = NuclearRelativistics.modid, version = NuclearRelativistics.version)

public class NuclearRelativistics {
	public static final String modid = "NuclearCraft";
	public static final String version = "1.6a";
	
	public static final CreativeTabs tabNR = new CreativeTabs("tabNR") {
		// Creative Tab Shown Item
		public Item getTabIconItem() {
			return NRItems.tabItem;
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
	public static NuclearRelativistics instance;
	
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
	public static boolean oreGenPlutonium;
	public static int oreSizePlutonium;
	public static int oreRarityPlutonium;
	
	public static int reactionGeneratorRF;
	public static int reactionGeneratorEfficiency;
	public static int fissionRF;
	public static int fissionEfficiency;
	public static boolean nuclearMeltdowns;
	public static int fusionRF;
	public static int RTGRF;
	public static int WRTGRF;
	
	public static boolean enablePaul;
	public static boolean enableNuclearMonster;
	
	public static boolean enableNukes;
	
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
	
	@SidedProxy(clientSide = "com.nr.mod.proxy.ClientProxy", serverSide = "com.nr.mod.proxy.CommonProxy")
	public static CommonProxy NRProxy;
	
	public static final Material liquidhelium = (new MaterialLiquid(MapColor.tntColor));
	public static DamageSource heliumfreeze = (new DamageSource("heliumfreeze")).setDamageBypassesArmor();
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {	
		//config
		Configuration ores = new Configuration(new File("config/NuclearCraft/NROres.cfg"));
		ores.load();
		Configuration machines = new Configuration(new File("config/NuclearCraft/NRMachines.cfg"));
		machines.load();
		Configuration entities = new Configuration(new File("config/NuclearCraft/NREntities.cfg"));
		entities.load();
		Configuration other = new Configuration(new File("config/NuclearCraft/NROther.cfg"));
		entities.load();
		
		oreGenPitchblende = ores.getBoolean("Uranium Ore Generation", "1aa)", true, "");
		oreSizePitchblende = ores.getInt("Uranium Ore Chunk Size", "1ab)", 6, 1, 100, "");
		oreRarityPitchblende = ores.getInt("Uranium Ore Gen Rate", "1ac)", 8, 1, 100, "");
		oreGenCopper = ores.getBoolean("Copper Ore Generation", "1ba)", true, "");
		oreSizeCopper = ores.getInt("Copper Ore Chunk Size", "1bb)", 8, 1, 100, "");
		oreRarityCopper = ores.getInt("Copper Ore Gen Rate", "1bc)", 15, 1, 100, "");
		oreGenTin = ores.getBoolean("Tin Ore Generation", "1ca)", true, "");
		oreSizeTin = ores.getInt("Tin Ore Chunk Size", "1cb)", 8, 1, 100, "");
		oreRarityTin = ores.getInt("Tin Ore Gen Rate", "1cc)", 14, 1, 100, "");
		oreGenLead = ores.getBoolean("Lead Ore Generation", "1da)", true, "");
		oreSizeLead = ores.getInt("Lead Ore Chunk Size", "1db)", 7, 1, 100, "");
		oreRarityLead = ores.getInt("Lead Ore Gen Rate", "1dc)", 14, 1, 100, "");
		oreGenSilver = ores.getBoolean("Silver Ore Generation", "1ea)", true, "");
		oreSizeSilver = ores.getInt("Silver Ore Chunk Size", "1eb)", 7, 1, 100, "");
		oreRaritySilver = ores.getInt("Silver Ore Gen Rate", "1ec)", 10, 1, 100, "");
		oreGenThorium = ores.getBoolean("Thorium Ore Generation", "1fa)", true, "");
		oreSizeThorium = ores.getInt("Thorium Ore Chunk Size", "1fb)", 6, 1, 100, "");
		oreRarityThorium = ores.getInt("Thorium Ore Gen Rate", "1fc)", 8, 1, 100, "");
		oreGenPlutonium = ores.getBoolean("Plutonium Ore Generation", "1ga)", true, "");
		oreSizePlutonium = ores.getInt("Plutonium Ore Chunk Size", "1gb)", 4, 1, 100, "");
		oreRarityPlutonium = ores.getInt("Plutonium Ore Gen Rate", "1gc)", 8, 1, 100, "");
		oreGenLithium = ores.getBoolean("Lithium Ore Generation", "1ha)", true, "");
		oreSizeLithium = ores.getInt("Lithium Ore Chunk Size", "1hb)", 7, 1, 100, "");
		oreRarityLithium = ores.getInt("Lithium Ore Gen Rate", "1hc)", 8, 1, 100, "");
		oreGenBoron = ores.getBoolean("Boron Ore Generation", "1ia)", true, "");
		oreSizeBoron = ores.getInt("Boron Ore Chunk Size", "1ib)", 7, 1, 100, "");
		oreRarityBoron = ores.getInt("Boron Ore Gen Rate", "1ic)", 8, 1, 100, "");
		
		nuclearFurnaceCookSpeed = machines.getInt("Nuclear Furnace Speed Multiplier", "1a)", 100, 10, 1000, "");
		nuclearFurnaceCookEfficiency = machines.getInt("Nuclear Furnace Fuel Usage Multiplier", "1b)", 100, 10, 1000, "");
		metalFurnaceCookSpeed = machines.getInt("Metal Furnace Speed Multiplier", "1c)", 100, 10, 1000, "");
		metalFurnaceCookEfficiency = machines.getInt("Metal Furnace Fuel Usage Multiplier", "1d)", 100, 10, 1000, "");
		crusherCrushSpeed = machines.getInt("Crusher Speed Multiplier", "1e)", 100, 10, 1000, "");
		crusherCrushEfficiency = machines.getInt("Crusher Fuel Usage Multiplier", "1f)", 100, 10, 1000, "");
		electricCrusherCrushSpeed = machines.getInt("Electic Crusher Speed Multiplier", "1g)", 100, 10, 1000, "");
		electricFurnaceSmeltSpeed = machines.getInt("Electic Furnace Speed Multiplier", "1h)", 100, 10, 1000, "");
		separatorSpeed = machines.getInt("Isotope Separator Speed Multiplier", "1i)", 100, 10, 1000, "");
		hastenerSpeed = machines.getInt("Decay Hastener Speed Multiplier", "1j)", 100, 10, 1000, "");
		collectorSpeed = machines.getInt("Helium Collector Speed Multiplier", "1k)", 100, 10, 1000, "");
		electrolyserSpeed = machines.getInt("Electrolyser Speed Multiplier", "1l)", 100, 10, 1000, "");
		oxidiserSpeed = machines.getInt("Oxidiser Speed Multiplier", "1m)", 100, 10, 1000, "");
		ioniserSpeed = machines.getInt("Ioniser Speed Multiplier", "1n)", 100, 10, 1000, "");
		irradiatorSpeed = machines.getInt("Neutron Irradiator Speed Multiplier", "1o)", 100, 10, 1000, "");
		coolerSpeed = machines.getInt("Supercooler Speed Multiplier", "1p)", 100, 10, 1000, "");
		factorySpeed = machines.getInt("Component Factory Speed Multiplier", "1q)", 100, 10, 1000, "");
		
		reactionGeneratorRF = machines.getInt("Reaction Generator RF Production Multiplier", "2a)", 100, 10, 1000, "");
		reactionGeneratorEfficiency = machines.getInt("Reaction Generator Efficiency Multiplier", "2b)", 100, 10, 1000, "");
		fissionRF = machines.getInt("Fission Reactor RF Production Multiplier", "2c)", 100, 10, 1000, "");
		fissionEfficiency = machines.getInt("Fission Reactor Efficiency Multiplier", "2d)", 100, 10, 1000, "");
		RTGRF = machines.getInt("RTG RF/t", "2e)", 100, 10, 1000, "");
		WRTGRF = machines.getInt("WRTG RF/t", "2f)", 1, 0, 10, "");
		fusionRF = machines.getInt("Fusion Reactor RF Production Multiplier", "2g)", 100, 10, 1000, "");
		nuclearMeltdowns = machines.getBoolean("Enable Fission Reactor Meltdowns", "2h)", true, "");
		
		enableNuclearMonster = entities.getBoolean("Enable Nuclear Monsters Spawning", "1a)", true, "");
		enablePaul = entities.getBoolean("Enable Paul", "1b)", true, "");
		
		enableNukes = other.getBoolean("Enable Nuclear Weapons", "1a)", true, "");
		
		ores.save();
		machines.save();
		entities.save();
		other.save();
		
		// Fusion
		//TileEntityFusionReactor.registerReactions();
		
		// Fluid Registry
		Fluid liqHelium = new FluidHelium().setDensity(125).setTemperature(4).setViscosity(0).setRarity(net.minecraft.item.EnumRarity.rare);
		FluidRegistry.registerFluid(liqHelium);
		NRBlocks.blockHelium = new BlockHelium(liqHelium, liquidhelium.setReplaceable()).setBlockName("liquidhelium");
		GameRegistry.registerBlock(NRBlocks.blockHelium, "blockHelium");
		liqHelium.setUnlocalizedName(NRBlocks.blockHelium.getUnlocalizedName());
		
		// Ore Registry
		GameRegistry.registerBlock(NRBlocks.blockOre = new BlockOre("blockOre", Material.rock), ItemBlockOre.class, "blockOre");
		
		// Block Registry
		GameRegistry.registerBlock(NRBlocks.blockBlock = new BlockBlock("blockBlock", Material.iron), ItemBlockBlock.class, "blockBlock");
		
		NRBlocks.simpleQuantumUp = new BlockSimpleQuantum(true).setBlockName("simpleQuantumUp").setCreativeTab(tabNR).setStepSound(Block.soundTypeMetal)
				.setResistance(5.0F).setHardness(2.0F);
		GameRegistry.registerBlock(NRBlocks.simpleQuantumUp, "simpleQuantumUp");
		
		NRBlocks.simpleQuantumDown = new BlockSimpleQuantum(false).setBlockName("simpleQuantumDown").setCreativeTab(tabNR).setStepSound(Block.soundTypeMetal)
				.setResistance(5.0F).setHardness(2.0F);
		GameRegistry.registerBlock(NRBlocks.simpleQuantumDown, "simpleQuantumDown");
		
		NRBlocks.graphiteBlock = new BlockGraphiteBlock().setBlockName("graphiteBlock").setCreativeTab(tabNR).setStepSound(Block.soundTypeStone)
				.setResistance(5.0F).setHardness(2.0F);
		GameRegistry.registerBlock(NRBlocks.graphiteBlock, "graphiteBlock");
		NRBlocks.cellBlock = new BlockCellBlock().setBlockName("cellBlock").setCreativeTab(tabNR).setStepSound(Block.soundTypeMetal)
				.setResistance(5.0F).setHardness(2.0F);
		GameRegistry.registerBlock(NRBlocks.cellBlock, "cellBlock");
		NRBlocks.reactorBlock = new BlockReactorBlock().setBlockName("reactorBlock").setCreativeTab(tabNR).setStepSound(Block.soundTypeMetal)
				.setResistance(5.0F).setHardness(3.0F);
		GameRegistry.registerBlock(NRBlocks.reactorBlock, "reactorBlock");
		NRBlocks.coolerBlock = new BlockCoolerBlock().setBlockName("coolerBlock").setCreativeTab(tabNR).setStepSound(Block.soundTypeMetal)
				.setResistance(5.0F).setHardness(2.0F);
		GameRegistry.registerBlock(NRBlocks.coolerBlock, "coolerBlock");
		NRBlocks.speedBlock = new BlockSpeedBlock().setBlockName("speedBlock").setCreativeTab(tabNR).setStepSound(Block.soundTypeMetal)
				.setResistance(5.0F).setHardness(2.0F);
		GameRegistry.registerBlock(NRBlocks.speedBlock, "speedBlock");
		NRBlocks.blastBlock = new BlockBlastBlock().setBlockName("blastBlock").setCreativeTab(tabNR).setStepSound(Block.soundTypeMetal)
				.setResistance(4000.0F).setHardness(30.0F);
		GameRegistry.registerBlock(NRBlocks.blastBlock, "blastBlock");
		NRBlocks.machineBlock = new BlockMachineBlock().setBlockName("machineBlock").setCreativeTab(tabNR).setStepSound(Block.soundTypeMetal)
				.setResistance(4000.0F).setHardness(30.0F);
		GameRegistry.registerBlock(NRBlocks.machineBlock, "machineBlock");
		
		NRBlocks.accStraight1 = new BlockAccStraight1().setBlockName("accStraight1").setCreativeTab(tabNR).setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F);
		GameRegistry.registerBlock(NRBlocks.accStraight1, "accStraight1");
		NRBlocks.accStraight2 = new BlockAccStraight2().setBlockName("accStraight2").setCreativeTab(tabNR).setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F);
		GameRegistry.registerBlock(NRBlocks.accStraight2, "accStraight2");
	
		// Machine Registry
			// Block
		NRBlocks.nuclearWorkspace = new BlockNuclearWorkspace(Material.iron).setBlockName("nuclearWorkspace").setCreativeTab(tabNR).setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F);
		GameRegistry.registerBlock(NRBlocks.nuclearWorkspace, "nuclearWorkspace");
		
		NRBlocks.fusionReactor = new BlockFusionReactor(Material.iron).setBlockName("fusionReactor").setCreativeTab(tabNR).setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F);
		GameRegistry.registerBlock(NRBlocks.fusionReactor, "fusionReactor");
		NRBlocks.fusionReactorBlock = new BlockFusionReactorBlock().setBlockName("fusionReactorBlock").setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F);
		GameRegistry.registerBlock(NRBlocks.fusionReactorBlock, "fusionReactorBlock");
		
		NRBlocks.nuclearFurnaceIdle = new BlockNuclearFurnace(false).setBlockName("nuclearFurnaceIdle").setCreativeTab(tabNR).setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F);
		GameRegistry.registerBlock(NRBlocks.nuclearFurnaceIdle, "nuclearFurnaceIdle");
		NRBlocks.nuclearFurnaceActive = new BlockNuclearFurnace(true).setBlockName("nuclearFurnaceActive").setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F).setLightLevel(0.750F);
		GameRegistry.registerBlock(NRBlocks.nuclearFurnaceActive, "nuclearFurnaceActive");
		NRBlocks.furnaceIdle = new BlockFurnace(false).setBlockName("furnaceIdle").setCreativeTab(tabNR).setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F);
		GameRegistry.registerBlock(NRBlocks.furnaceIdle, "furnaceIdle");
		NRBlocks.furnaceActive = new BlockFurnace(true).setBlockName("furnaceActive").setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F).setLightLevel(0.750F);
		GameRegistry.registerBlock(NRBlocks.furnaceActive, "furnaceActive");
		NRBlocks.crusherIdle = new BlockCrusher(false).setBlockName("crusherIdle").setCreativeTab(tabNR).setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F);
		GameRegistry.registerBlock(NRBlocks.crusherIdle, "crusherIdle");
		NRBlocks.crusherActive = new BlockCrusher(true).setBlockName("crusherActive").setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F).setLightLevel(0.750F);
		GameRegistry.registerBlock(NRBlocks.crusherActive, "crusherActive");
		NRBlocks.electricCrusherIdle = new BlockElectricCrusher(false).setBlockName("electricCrusherIdle").setCreativeTab(tabNR).setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F);
		GameRegistry.registerBlock(NRBlocks.electricCrusherIdle, "electricCrusherIdle");
		NRBlocks.electricCrusherActive = new BlockElectricCrusher(true).setBlockName("electricCrusherActive").setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F).setLightLevel(0.750F);
		GameRegistry.registerBlock(NRBlocks.electricCrusherActive, "electricCrusherActive");
		NRBlocks.electricFurnaceIdle = new BlockElectricFurnace(false).setBlockName("electricFurnaceIdle").setCreativeTab(tabNR).setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F);
		GameRegistry.registerBlock(NRBlocks.electricFurnaceIdle, "electricFurnaceIdle");
		NRBlocks.electricFurnaceActive = new BlockElectricFurnace(true).setBlockName("electricFurnaceActive").setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F).setLightLevel(0.750F);
		GameRegistry.registerBlock(NRBlocks.electricFurnaceActive, "electricFurnaceActive");
		NRBlocks.reactionGeneratorIdle = new BlockReactionGenerator(false).setBlockName("reactionGeneratorIdle").setCreativeTab(tabNR).setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F);
		GameRegistry.registerBlock(NRBlocks.reactionGeneratorIdle, "reactionGeneratorIdle");
		NRBlocks.reactionGeneratorActive = new BlockReactionGenerator(true).setBlockName("reactionGeneratorActive").setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F).setLightLevel(0.750F);
		GameRegistry.registerBlock(NRBlocks.reactionGeneratorActive, "reactionGeneratorActive");
		NRBlocks.separatorIdle = new BlockSeparator(false).setBlockName("separatorIdle").setCreativeTab(tabNR).setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F);
		GameRegistry.registerBlock(NRBlocks.separatorIdle, "separatorIdle");
		NRBlocks.separatorActive = new BlockSeparator(true).setBlockName("separatorActive").setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F).setLightLevel(0.750F);
		GameRegistry.registerBlock(NRBlocks.separatorActive, "separatorActive");
		NRBlocks.hastenerIdle = new BlockHastener(false).setBlockName("hastenerIdle").setCreativeTab(tabNR).setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F);
		GameRegistry.registerBlock(NRBlocks.hastenerIdle, "hastenerIdle");
		NRBlocks.hastenerActive = new BlockHastener(true).setBlockName("hastenerActive").setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F).setLightLevel(0.750F);
		GameRegistry.registerBlock(NRBlocks.hastenerActive, "hastenerActive");
		NRBlocks.electrolyserIdle = new BlockElectrolyser(false).setBlockName("electrolyserIdle").setCreativeTab(tabNR).setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F);
		GameRegistry.registerBlock(NRBlocks.electrolyserIdle, "electrolyserIdle");
		NRBlocks.electrolyserActive = new BlockElectrolyser(true).setBlockName("electrolyserActive").setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F).setLightLevel(0.750F);
		GameRegistry.registerBlock(NRBlocks.electrolyserActive, "electrolyserActive");
		NRBlocks.fissionReactorGraphiteIdle = new BlockFissionReactorGraphite(false).setBlockName("fissionReactorGraphiteIdle").setCreativeTab(tabNR).setStepSound(Block.soundTypeMetal)
				.setResistance(5.0F).setHardness(5.0F);
		GameRegistry.registerBlock(NRBlocks.fissionReactorGraphiteIdle, "fissionReactorGraphiteIdle");
		NRBlocks.fissionReactorGraphiteActive = new BlockFissionReactorGraphite(true).setBlockName("fissionReactorGraphiteActive").setStepSound(Block.soundTypeMetal)
				.setResistance(5.0F).setHardness(5.0F).setLightLevel(0.750F);
		GameRegistry.registerBlock(NRBlocks.fissionReactorGraphiteActive, "fissionReactorGraphiteActive");
		NRBlocks.RTG = new BlockRTG().setBlockName("RTG").setCreativeTab(tabNR).setStepSound(Block.soundTypeMetal)
				.setResistance(5.0F).setHardness(5.0F).setLightLevel(0.250F);
		GameRegistry.registerBlock(NRBlocks.RTG, "RTG");
		NRBlocks.WRTG = new BlockWRTG().setBlockName("WRTG").setCreativeTab(tabNR).setStepSound(Block.soundTypeMetal)
				.setResistance(5.0F).setHardness(5.0F).setLightLevel(0.250F);
		GameRegistry.registerBlock(NRBlocks.WRTG, "WRTG");
		NRBlocks.collectorIdle = new BlockCollector(false).setBlockName("collectorIdle").setCreativeTab(tabNR).setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F);
		GameRegistry.registerBlock(NRBlocks.collectorIdle, "collectorIdle");
		NRBlocks.collectorActive = new BlockCollector(true).setBlockName("collectorActive").setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F).setLightLevel(0.750F);
		GameRegistry.registerBlock(NRBlocks.collectorActive, "collectorActive");
		NRBlocks.oxidiserIdle = new BlockOxidiser(false).setBlockName("oxidiserIdle").setCreativeTab(tabNR).setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F);
		GameRegistry.registerBlock(NRBlocks.oxidiserIdle, "oxidiserIdle");
		NRBlocks.oxidiserActive = new BlockOxidiser(true).setBlockName("oxidiserActive").setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F).setLightLevel(0.750F);
		GameRegistry.registerBlock(NRBlocks.oxidiserActive, "oxidiserActive");
		NRBlocks.ioniserIdle = new BlockIoniser(false).setBlockName("ioniserIdle").setCreativeTab(tabNR).setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F);
		GameRegistry.registerBlock(NRBlocks.ioniserIdle, "ioniserIdle");
		NRBlocks.ioniserActive = new BlockIoniser(true).setBlockName("ioniserActive").setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F).setLightLevel(0.750F);
		GameRegistry.registerBlock(NRBlocks.ioniserActive, "ioniserActive");
		NRBlocks.irradiatorIdle = new BlockIrradiator(false).setBlockName("irradiatorIdle").setCreativeTab(tabNR).setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F);
		GameRegistry.registerBlock(NRBlocks.irradiatorIdle, "irradiatorIdle");
		NRBlocks.irradiatorActive = new BlockIrradiator(true).setBlockName("irradiatorActive").setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F).setLightLevel(0.750F);
		GameRegistry.registerBlock(NRBlocks.irradiatorActive, "irradiatorActive");
		NRBlocks.coolerIdle = new BlockCooler(false).setBlockName("coolerIdle").setCreativeTab(tabNR).setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F);
		GameRegistry.registerBlock(NRBlocks.coolerIdle, "coolerIdle");
		NRBlocks.coolerActive = new BlockCooler(true).setBlockName("coolerActive").setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F).setLightLevel(0.750F);
		GameRegistry.registerBlock(NRBlocks.coolerActive, "coolerActive");
		NRBlocks.factoryIdle = new BlockFactory(false).setBlockName("factoryIdle").setCreativeTab(tabNR).setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F);
		GameRegistry.registerBlock(NRBlocks.factoryIdle, "factoryIdle");
		NRBlocks.factoryActive = new BlockFactory(true).setBlockName("factoryActive").setStepSound(Block.soundTypeMetal)
				.setResistance(20.0F).setHardness(5.0F).setLightLevel(0.750F);
		GameRegistry.registerBlock(NRBlocks.factoryActive, "factoryActive");
		
		NRBlocks.nuke = new BlockNuke().setBlockName("nuke").setCreativeTab(tabNR).setStepSound(Block.soundTypeCloth)
				.setHardness(0.0F);
		GameRegistry.registerBlock(NRBlocks.nuke, "nuke");
		NRBlocks.nukeE = new BlockNukeExploding().setBlockName("nukeE").setStepSound(Block.soundTypeCloth)
				.setHardness(0.0F);
		GameRegistry.registerBlock(NRBlocks.nukeE, "nukeE");
			
			// Tile Entity
		GameRegistry.registerTileEntity(TileEntityNuclearFurnace.class, "nuclearFurnace");
		GameRegistry.registerTileEntity(TileEntityFurnace.class, "furnace");
		GameRegistry.registerTileEntity(TileEntityCrusher.class, "crusher");
		GameRegistry.registerTileEntity(TileEntityElectricCrusher.class, "electricCrusher");
		GameRegistry.registerTileEntity(TileEntityElectricFurnace.class, "electricFurnace");
		GameRegistry.registerTileEntity(TileEntityReactionGenerator.class, "reactionGenerator");
		GameRegistry.registerTileEntity(TileEntitySeparator.class, "separator");
		GameRegistry.registerTileEntity(TileEntityHastener.class, "hastener");
		GameRegistry.registerTileEntity(TileEntityCollector.class, "collector");
		GameRegistry.registerTileEntity(TileEntityElectrolyser.class, "electrolyser");
		GameRegistry.registerTileEntity(TileEntityFissionReactorGraphite.class, "fissionReactorGraphite");
		GameRegistry.registerTileEntity(TileEntityNuclearWorkspace.class, "nuclearWorkspace");
		GameRegistry.registerTileEntity(TileEntityFusionReactor.class, "fusionReactor");
		GameRegistry.registerTileEntity(TileEntityAccStraight1.class, "TEaccStraight1");
		GameRegistry.registerTileEntity(TileEntityAccStraight2.class, "TEaccStraight2");
		GameRegistry.registerTileEntity(TileEntityRTG.class, "RTG");
		GameRegistry.registerTileEntity(TileEntityWRTG.class, "WRTG");
		GameRegistry.registerTileEntity(TileEntityFusionReactorBlock.class, "fusionReactorBlock");
		GameRegistry.registerTileEntity(TileEntityOxidiser.class, "oxidiser");
		GameRegistry.registerTileEntity(TileEntityIoniser.class, "ioniser");
		GameRegistry.registerTileEntity(TileEntityIrradiator.class, "irradiator");
		GameRegistry.registerTileEntity(TileEntityCooler.class, "cooler");
		GameRegistry.registerTileEntity(TileEntityFactory.class, "factory");
		
		GameRegistry.registerTileEntity(TileEntitySimpleQuantum.class, "simpleQuantum");
	
		// Item Registry	
		NRItems.dominoes = new ItemFood(10, 1.2F, false).setCreativeTab(tabNR).setUnlocalizedName("dominoes").setTextureName("nr:food/" + "dominoes");
		GameRegistry.registerItem(NRItems.dominoes, "dominoes");
		NRItems.boiledEgg = new ItemFood(6, 1.0F, false).setCreativeTab(tabNR).setUnlocalizedName("boiledEgg").setTextureName("nr:food/" + "boiledEgg");
		GameRegistry.registerItem(NRItems.boiledEgg, "boiledEgg");
		NRItems.upgrade = new Item().setCreativeTab(tabNR).setUnlocalizedName("upgrade").setTextureName("nr:upgrades/" + "upgrade").setMaxStackSize(8);
		GameRegistry.registerItem(NRItems.upgrade, "upgrade");
		NRItems.upgradeSpeed = new Item().setCreativeTab(tabNR).setUnlocalizedName("upgradeSpeed").setTextureName("nr:upgrades/" + "upgradeSpeed").setMaxStackSize(8);
		GameRegistry.registerItem(NRItems.upgradeSpeed, "upgradeSpeed");
		NRItems.upgradeEnergy = new Item().setCreativeTab(tabNR).setUnlocalizedName("upgradeEnergy").setTextureName("nr:upgrades/" + "upgradeEnergy").setMaxStackSize(8);
		GameRegistry.registerItem(NRItems.upgradeEnergy, "upgradeEnergy");
		
		NRItems.tabItem = new Item().setUnlocalizedName("tabItem").setTextureName("nr:fuel/" + "11");
		GameRegistry.registerItem(NRItems.tabItem, "tabItem");
		
		GameRegistry.registerItem(NRItems.fuel = new ItemFuel("fuel"), "fuel");
		GameRegistry.registerItem(NRItems.material = new ItemMaterial("material"), "material");
		GameRegistry.registerItem(NRItems.parts = new ItemPart("parts"), "parts");
		
		NRItems.nuclearGrenade = new ItemNuclearGrenade().setCreativeTab(tabNR).setUnlocalizedName("nuclearGrenade").setTextureName("nr:weapons/" + "nuclearGrenade");
		GameRegistry.registerItem(NRItems.nuclearGrenade, "nuclearGrenade");
		NRItems.nuclearGrenadeThrown = new Item().setUnlocalizedName("nuclearGrenadeThrown").setTextureName("nr:weapons/" + "nuclearGrenadeThrown");
		GameRegistry.registerItem(NRItems.nuclearGrenadeThrown, "nuclearGrenadeThrown");
		
		NRItems.portableEnderChest = new ItemEnderChest().setCreativeTab(tabNR).setUnlocalizedName("portableEnderChest").setTextureName("nr:" + "portableEnderChest").setMaxStackSize(1);
		GameRegistry.registerItem(NRItems.portableEnderChest, "portableEnderChest");
		
		// Tool Registry
		NRItems.bronzePickaxe = new NRPickaxe(Bronze).setCreativeTab(tabNR).setUnlocalizedName("bronzePickaxe").setTextureName("nr:tools/" + "bronzePickaxe");
		GameRegistry.registerItem(NRItems.bronzePickaxe, "bronzePickaxe");
		NRItems.bronzeShovel = new NRShovel(Bronze).setCreativeTab(tabNR).setUnlocalizedName("bronzeShovel").setTextureName("nr:tools/" + "bronzeShovel");
		GameRegistry.registerItem(NRItems.bronzeShovel, "bronzeShovel");
		NRItems.bronzeAxe = new NRAxe(Bronze).setCreativeTab(tabNR).setUnlocalizedName("bronzeAxe").setTextureName("nr:tools/" + "bronzeAxe");
		GameRegistry.registerItem(NRItems.bronzeAxe, "bronzeAxe");
		NRItems.bronzeHoe = new NRHoe(Bronze).setCreativeTab(tabNR).setUnlocalizedName("bronzeHoe").setTextureName("nr:tools/" + "bronzeHoe");
		GameRegistry.registerItem(NRItems.bronzeHoe, "bronzeHoe");
		NRItems.bronzeSword = new NRSword(Bronze).setCreativeTab(tabNR).setUnlocalizedName("bronzeSword").setTextureName("nr:tools/" + "bronzeSword");
		GameRegistry.registerItem(NRItems.bronzeSword, "bronzeSword");
		
		NRItems.boronPickaxe = new NRPickaxe(Boron).setCreativeTab(tabNR).setUnlocalizedName("boronPickaxe").setTextureName("nr:tools/" + "boronPickaxe");
		GameRegistry.registerItem(NRItems.boronPickaxe, "boronPickaxe");
		NRItems.boronShovel = new NRShovel(Boron).setCreativeTab(tabNR).setUnlocalizedName("boronShovel").setTextureName("nr:tools/" + "boronShovel");
		GameRegistry.registerItem(NRItems.boronShovel, "boronShovel");
		NRItems.boronAxe = new NRAxe(Boron).setCreativeTab(tabNR).setUnlocalizedName("boronAxe").setTextureName("nr:tools/" + "boronAxe");
		GameRegistry.registerItem(NRItems.boronAxe, "boronAxe");
		NRItems.boronHoe = new NRHoe(Boron).setCreativeTab(tabNR).setUnlocalizedName("boronHoe").setTextureName("nr:tools/" + "boronHoe");
		GameRegistry.registerItem(NRItems.boronHoe, "boronHoe");
		NRItems.boronSword = new NRSword(Boron).setCreativeTab(tabNR).setUnlocalizedName("boronSword").setTextureName("nr:tools/" + "boronSword");
		GameRegistry.registerItem(NRItems.boronSword, "boronSword");
		
		NRItems.toughAlloyPickaxe = new NRPickaxe(ToughAlloy).setCreativeTab(tabNR).setUnlocalizedName("toughAlloyPickaxe").setTextureName("nr:tools/" + "toughAlloyPickaxe");
		GameRegistry.registerItem(NRItems.toughAlloyPickaxe, "toughAlloyPickaxe");
		NRItems.toughAlloyShovel = new NRShovel(ToughAlloy).setCreativeTab(tabNR).setUnlocalizedName("toughAlloyShovel").setTextureName("nr:tools/" + "toughAlloyShovel");
		GameRegistry.registerItem(NRItems.toughAlloyShovel, "toughAlloyShovel");
		NRItems.toughAlloyAxe = new NRAxe(ToughAlloy).setCreativeTab(tabNR).setUnlocalizedName("toughAlloyAxe").setTextureName("nr:tools/" + "toughAlloyAxe");
		GameRegistry.registerItem(NRItems.toughAlloyAxe, "toughAlloyAxe");
		NRItems.toughAlloyHoe = new NRHoe(ToughAlloy).setCreativeTab(tabNR).setUnlocalizedName("toughAlloyHoe").setTextureName("nr:tools/" + "toughAlloyHoe");
		GameRegistry.registerItem(NRItems.toughAlloyHoe, "toughAlloyHoe");
		NRItems.toughAlloySword = new NRSword(ToughAlloy).setCreativeTab(tabNR).setUnlocalizedName("toughAlloySword").setTextureName("nr:tools/" + "toughAlloySword");
		GameRegistry.registerItem(NRItems.toughAlloySword, "toughAlloySword");
		NRItems.toughAlloyPaxel = new NRPaxel(ToughPaxel).setCreativeTab(tabNR).setUnlocalizedName("toughAlloyPaxel").setTextureName("nr:tools/" + "toughAlloyPaxel");
		GameRegistry.registerItem(NRItems.toughAlloyPaxel, "toughAlloyPaxel");
		
		NRItems.dUPickaxe = new NRPickaxe(dU).setCreativeTab(tabNR).setUnlocalizedName("dUPickaxe").setTextureName("nr:tools/" + "dUPickaxe");
		GameRegistry.registerItem(NRItems.dUPickaxe, "dUPickaxe");
		NRItems.dUShovel = new NRShovel(dU).setCreativeTab(tabNR).setUnlocalizedName("dUShovel").setTextureName("nr:tools/" + "dUShovel");
		GameRegistry.registerItem(NRItems.dUShovel, "dUShovel");
		NRItems.dUAxe = new NRAxe(dU).setCreativeTab(tabNR).setUnlocalizedName("dUAxe").setTextureName("nr:tools/" + "dUAxe");
		GameRegistry.registerItem(NRItems.dUAxe, "dUAxe");
		NRItems.dUHoe = new NRHoe(dU).setCreativeTab(tabNR).setUnlocalizedName("dUHoe").setTextureName("nr:tools/" + "dUHoe");
		GameRegistry.registerItem(NRItems.dUHoe, "dUHoe");
		NRItems.dUSword = new NRSword(dU).setCreativeTab(tabNR).setUnlocalizedName("dUSword").setTextureName("nr:tools/" + "dUSword");
		GameRegistry.registerItem(NRItems.dUSword, "dUSword");
		NRItems.dUPaxel = new NRPaxel(dUPaxel).setCreativeTab(tabNR).setUnlocalizedName("dUPaxel").setTextureName("nr:tools/" + "dUPaxel");
		GameRegistry.registerItem(NRItems.dUPaxel, "dUPaxel");
		
		NRItems.toughBow = new ItemToughBow().setCreativeTab(tabNR).setUnlocalizedName("toughBow").setMaxStackSize(1);
		GameRegistry.registerItem(NRItems.toughBow, "toughBow");
		NRItems.pistol = new ItemPistol().setCreativeTab(tabNR).setUnlocalizedName("pistol").setMaxStackSize(1).setTextureName("nr:tools/" + "pistol");
		GameRegistry.registerItem(NRItems.pistol, "pistol");
		NRItems.dUBullet = new Item().setCreativeTab(tabNR).setUnlocalizedName("dUBullet").setTextureName("nr:tools/" + "dUBullet");
		GameRegistry.registerItem(NRItems.dUBullet, "dUBullet");
		
		//Armor Registry
		NRItems.toughHelm = new ToughArmor(ToughArmorMaterial, toughHelmID, 0).setUnlocalizedName("toughHelm").setTextureName("nr:armour/" + "toughHelm");
		GameRegistry.registerItem(NRItems.toughHelm, "toughHelm");
		NRItems.toughChest = new ToughArmor(ToughArmorMaterial, toughChestID, 1).setUnlocalizedName("toughChest").setTextureName("nr:armour/" + "toughChest");
		GameRegistry.registerItem(NRItems.toughChest, "toughChest");
		NRItems.toughLegs = new ToughArmor(ToughArmorMaterial, toughLegsID, 2).setUnlocalizedName("toughLegs").setTextureName("nr:armour/" + "toughLegs");
		GameRegistry.registerItem(NRItems.toughLegs, "toughLegs");
		NRItems.toughBoots = new ToughArmor(ToughArmorMaterial, toughBootsID, 3).setUnlocalizedName("toughBoots").setTextureName("nr:armour/" + "toughBoots");
		GameRegistry.registerItem(NRItems.toughBoots, "toughBoots");
		
		NRItems.boronHelm = new BoronArmor(BoronArmorMaterial, boronHelmID, 0).setUnlocalizedName("boronHelm").setTextureName("nr:armour/" + "boronHelm");
		GameRegistry.registerItem(NRItems.boronHelm, "boronHelm");
		NRItems.boronChest = new BoronArmor(BoronArmorMaterial, boronChestID, 1).setUnlocalizedName("boronChest").setTextureName("nr:armour/" + "boronChest");
		GameRegistry.registerItem(NRItems.boronChest, "boronChest");
		NRItems.boronLegs = new BoronArmor(BoronArmorMaterial, boronLegsID, 2).setUnlocalizedName("boronLegs").setTextureName("nr:armour/" + "boronLegs");
		GameRegistry.registerItem(NRItems.boronLegs, "boronLegs");
		NRItems.boronBoots = new BoronArmor(BoronArmorMaterial, boronBootsID, 3).setUnlocalizedName("boronBoots").setTextureName("nr:armour/" + "boronBoots");
		GameRegistry.registerItem(NRItems.boronBoots, "boronBoots");
		
		NRItems.bronzeHelm = new BronzeArmor(BronzeArmorMaterial, bronzeHelmID, 0).setUnlocalizedName("bronzeHelm").setTextureName("nr:armour/" + "bronzeHelm");
		GameRegistry.registerItem(NRItems.bronzeHelm, "bronzeHelm");
		NRItems.bronzeChest = new BronzeArmor(BronzeArmorMaterial, bronzeChestID, 1).setUnlocalizedName("bronzeChest").setTextureName("nr:armour/" + "bronzeChest");
		GameRegistry.registerItem(NRItems.bronzeChest, "bronzeChest");
		NRItems.bronzeLegs = new BronzeArmor(BronzeArmorMaterial, bronzeLegsID, 2).setUnlocalizedName("bronzeLegs").setTextureName("nr:armour/" + "bronzeLegs");
		GameRegistry.registerItem(NRItems.bronzeLegs, "bronzeLegs");
		NRItems.bronzeBoots = new BronzeArmor(BronzeArmorMaterial, bronzeBootsID, 3).setUnlocalizedName("bronzeBoots").setTextureName("nr:armour/" + "bronzeBoots");
		GameRegistry.registerItem(NRItems.bronzeBoots, "bronzeBoots");
		
		NRItems.dUHelm = new DUArmor(dUArmorMaterial, dUHelmID, 0).setUnlocalizedName("dUHelm").setTextureName("nr:armour/" + "dUHelm");
		GameRegistry.registerItem(NRItems.dUHelm, "dUHelm");
		NRItems.dUChest = new DUArmor(dUArmorMaterial, dUChestID, 1).setUnlocalizedName("dUChest").setTextureName("nr:armour/" + "dUChest");
		GameRegistry.registerItem(NRItems.dUChest, "dUChest");
		NRItems.dULegs = new DUArmor(dUArmorMaterial, dULegsID, 2).setUnlocalizedName("dULegs").setTextureName("nr:armour/" + "dULegs");
		GameRegistry.registerItem(NRItems.dULegs, "dULegs");
		NRItems.dUBoots = new DUArmor(dUArmorMaterial, dUBootsID, 3).setUnlocalizedName("dUBoots").setTextureName("nr:armour/" + "dUBoots");
		GameRegistry.registerItem(NRItems.dUBoots, "dUBoots");
		
		// Block Crafting Recipes
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NRBlocks.blockBlock, 1, 4), true,
				new Object[] {"XXX", "XXX", "XXX", 'X', new ItemStack (NRItems.material, 1, 4)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NRBlocks.blockBlock, 1, 0), true,
				new Object[] {"XXX", "XXX", "XXX", 'X',  new ItemStack (NRItems.material, 1, 0)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NRBlocks.blockBlock, 1, 1), true,
				new Object[] {"XXX", "XXX", "XXX", 'X',  new ItemStack (NRItems.material, 1, 1)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NRBlocks.blockBlock, 1, 2), true,
				new Object[] {"XXX", "XXX", "XXX", 'X',  new ItemStack (NRItems.material, 1, 2)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NRBlocks.blockBlock, 1, 3), true,
				new Object[] {"XXX", "XXX", "XXX", 'X',  new ItemStack (NRItems.material, 1, 3)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NRBlocks.blockBlock, 1, 6), true,
				new Object[] {"XXX", "XXX", "XXX", 'X',  new ItemStack (NRItems.material, 1, 6)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NRBlocks.blockBlock, 1, 5), true,
				new Object[] {"XXX", "XXX", "XXX", 'X',  new ItemStack (NRItems.material, 1, 5)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NRBlocks.blockBlock, 1, 8), true,
				new Object[] {"XXX", "XXX", "XXX", 'X',  new ItemStack (NRItems.material, 1, 42)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NRBlocks.blockBlock, 1, 9), true,
				new Object[] {"XXX", "XXX", "XXX", 'X',  new ItemStack (NRItems.material, 1, 43)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NRBlocks.blockBlock, 1, 10), true,
				new Object[] {"XXX", "XXX", "XXX", 'X',  new ItemStack (NRItems.material, 1, 50)}));
		
		// Tiny Dust to Full Dust
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NRItems.material, 1, 17), true,
				new Object[] {"XXX", "XXX", "XXX", 'X', new ItemStack (NRItems.material, 1, 23)}));
		
		// Isotope Lump Recipes
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NRItems.material, 1, 28), true,
				new Object[] {"XXX", "XXX", "XXX", 'X', new ItemStack (NRItems.material, 1, 29)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NRItems.material, 1, 26), true,
				new Object[] {"XXX", "XXX", "XXX", 'X', new ItemStack (NRItems.material, 1, 27)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NRItems.material, 1, 24), true,
				new Object[] {"XXX", "XXX", "XXX", 'X', new ItemStack (NRItems.material, 1, 25)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NRItems.material, 1, 30), true,
				new Object[] {"XXX", "XXX", "XXX", 'X', new ItemStack (NRItems.material, 1, 31)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NRItems.material, 1, 32), true,
				new Object[] {"XXX", "XXX", "XXX", 'X', new ItemStack (NRItems.material, 1, 33)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NRItems.material, 1, 34), true,
				new Object[] {"XXX", "XXX", "XXX", 'X', new ItemStack (NRItems.material, 1, 35)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NRItems.material, 1, 36), true,
				new Object[] {"XXX", "XXX", "XXX", 'X', new ItemStack (NRItems.material, 1, 37)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NRItems.material, 1, 38), true,
				new Object[] {"XXX", "XXX", "XXX", 'X', new ItemStack (NRItems.material, 1, 39)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NRItems.material, 1, 40), true,
				new Object[] {"XXX", "XXX", "XXX", 'X', new ItemStack (NRItems.material, 1, 41)}));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NRItems.material, 1, 59), true,
				new Object[] {"XXX", "XXX", "XXX", 'X', new ItemStack (NRItems.material, 1, 60)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NRItems.material, 1, 57), true,
				new Object[] {"XXX", "XXX", "XXX", 'X', new ItemStack (NRItems.material, 1, 58)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NRItems.material, 1, 55), true,
				new Object[] {"XXX", "XXX", "XXX", 'X', new ItemStack (NRItems.material, 1, 56)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NRItems.material, 1, 61), true,
				new Object[] {"XXX", "XXX", "XXX", 'X', new ItemStack (NRItems.material, 1, 62)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NRItems.material, 1, 63), true,
				new Object[] {"XXX", "XXX", "XXX", 'X', new ItemStack (NRItems.material, 1, 64)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NRItems.material, 1, 65), true,
				new Object[] {"XXX", "XXX", "XXX", 'X', new ItemStack (NRItems.material, 1, 66)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NRItems.material, 1, 67), true,
				new Object[] {"XXX", "XXX", "XXX", 'X', new ItemStack (NRItems.material, 1, 68)}));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NRItems.material, 1, 46), true,
				new Object[] {"XXX", "XXX", "XXX", 'X', new ItemStack (NRItems.material, 1, 69)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (NRItems.material, 1, 48), true,
				new Object[] {"XXX", "XXX", "XXX", 'X', new ItemStack (NRItems.material, 1, 70)}));
		
		// Shaped Crafting Recipes
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(NRItems.fuel, 16, 33), true,
				new Object[] {" I ", "I I", " I ", 'I', new ItemStack(NRItems.parts, 1, 1)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(NRItems.fuel, 16, 45), true,
				new Object[] {" I ", "I I", " I ", 'I', new ItemStack(NRItems.parts, 1, 6)}));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(NRItems.parts, 2, 0), true,
				new Object[] {"LLL", "CCC", 'L', "ingotLead", 'C', "dustCoal"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(NRItems.parts, 1, 2), true,
				new Object[] {"FFF", "CCC", "SSS", 'F', Items.flint, 'C', "cobblestone", 'S', Items.stick}));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(NRItems.parts, 16, 1), true,
				new Object[] {"III", "IBI", "III", 'I', "ingotIron", 'B', "blockIron"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(NRItems.parts, 16, 6), true,
				new Object[] {"III", "IBI", "III", 'I', "ingotTin", 'B', "blockTin"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(NRBlocks.nuclearFurnaceIdle, true,
				new Object[] {"XPX", "P P", "XPX", 'P', new ItemStack(NRItems.parts, 1, 0), 'X', "dustObsidian"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(NRBlocks.furnaceIdle, true,
				new Object[] {"PPP", "P P", "PPP", 'P', new ItemStack(NRItems.parts, 1, 1)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(NRBlocks.crusherIdle, true,
				new Object[] {"PPP", "PCP", "PPP", 'P', new ItemStack(NRItems.parts, 1, 1), 'C', new ItemStack(NRItems.parts, 1, 2)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(NRBlocks.electricCrusherIdle, true,
				new Object[] {"PRP", "RCR", "PRP", 'P', new ItemStack(NRItems.parts, 1, 1), 'R', Items.redstone, 'C', NRBlocks.crusherIdle}));
		GameRegistry.addRecipe(new ShapedOreRecipe(NRBlocks.electricFurnaceIdle, true,
				new Object[] {"PRP", "RCR", "PRP", 'P', new ItemStack(NRItems.parts, 1, 1), 'R', Items.redstone, 'C', NRBlocks.furnaceIdle}));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(NRBlocks.nuclearWorkspace, true,
				new Object[] {"NNN", " T ", "TTT", 'N', new ItemStack(NRItems.parts, 1, 0), 'T', "ingotTough"}));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(NRBlocks.graphiteBlock, true,
				new Object[] {"CDC", "DCD", "CDC", 'D', "dustCoal", 'C', Items.coal}));
	
		// Tool Crafting Recipes
		GameRegistry.addRecipe(new ShapedOreRecipe(NRItems.bronzePickaxe, true,
				new Object[] {"XXX", " S ", " S ", 'X', "ingotBronze", 'S', Items.stick}));
		GameRegistry.addRecipe(new ShapedOreRecipe(NRItems.bronzeShovel, true,
				new Object[] {"X", "S", "S", 'X', "ingotBronze", 'S', Items.stick}));
		GameRegistry.addRecipe(new ShapedOreRecipe(NRItems.bronzeAxe, true,
				new Object[] {"XX", "XS", " S", 'X', "ingotBronze", 'S', Items.stick}));
		GameRegistry.addRecipe(new ShapedOreRecipe(NRItems.bronzeAxe, true,
				new Object[] {"XX", "SX", "S ", 'X', "ingotBronze", 'S', Items.stick}));
		GameRegistry.addRecipe(new ShapedOreRecipe(NRItems.bronzeHoe, true,
				new Object[] {"XX", "S ", "S ", 'X', "ingotBronze", 'S', Items.stick}));
		GameRegistry.addRecipe(new ShapedOreRecipe(NRItems.bronzeHoe, true,
				new Object[] {"XX", " S", " S", 'X', "ingotBronze", 'S', Items.stick}));
		GameRegistry.addRecipe(new ShapedOreRecipe(NRItems.bronzeSword, true,
				new Object[] {"X", "X", "S", 'X', "ingotBronze", 'S', Items.stick}));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(NRItems.boronPickaxe, true,
				new Object[] {"XXX", " S ", " S ", 'X', "ingotBoron", 'S', Items.stick}));
		GameRegistry.addRecipe(new ShapedOreRecipe(NRItems.boronShovel, true,
				new Object[] {"X", "S", "S", 'X', "ingotBoron", 'S', Items.stick}));
		GameRegistry.addRecipe(new ShapedOreRecipe(NRItems.boronAxe, true,
				new Object[] {"XX", "XS", " S", 'X', "ingotBoron", 'S', Items.stick}));
		GameRegistry.addRecipe(new ShapedOreRecipe(NRItems.boronAxe, true,
				new Object[] {"XX", "SX", "S ", 'X', "ingotBoron", 'S', Items.stick}));
		GameRegistry.addRecipe(new ShapedOreRecipe(NRItems.boronHoe, true,
				new Object[] {"XX", "S ", "S ", 'X', "ingotBoron", 'S', Items.stick}));
		GameRegistry.addRecipe(new ShapedOreRecipe(NRItems.boronHoe, true,
				new Object[] {"XX", " S", " S", 'X', "ingotBoron", 'S', Items.stick}));
		GameRegistry.addRecipe(new ShapedOreRecipe(NRItems.boronSword, true,
				new Object[] {"X", "X", "S", 'X', "ingotBoron", 'S', Items.stick}));
		
		// Armour Crafting Recipes
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(NRItems.boronHelm, 1), true,
				new Object[] {"XXX", "X X", 'X', "ingotBoron"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(NRItems.boronChest, 1), true,
				new Object[] {"X X", "XXX", "XXX", 'X', "ingotBoron"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(NRItems.boronLegs, 1), true,
				new Object[] {"XXX", "X X", "X X", 'X', "ingotBoron"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(NRItems.boronBoots, 1), true,
				new Object[] {"X X", "X X", 'X', "ingotBoron"}));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(NRItems.bronzeHelm, 1), true,
				new Object[] {"XXX", "X X", 'X', "ingotBronze"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(NRItems.bronzeChest, 1), true,
				new Object[] {"X X", "XXX", "XXX", 'X', "ingotBronze"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(NRItems.bronzeLegs, 1), true,
				new Object[] {"XXX", "X X", "X X", 'X', "ingotBronze"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(NRItems.bronzeBoots, 1), true,
				new Object[] {"X X", "X X", 'X', "ingotBronze"}));
	
		// Simple Shapeless Crafting Recipes
		GameRegistry.addShapelessRecipe(new ItemStack(NRItems.material, 9, 4),
				new Object[] {new ItemStack(NRBlocks.blockBlock, 1, 4)});
		GameRegistry.addShapelessRecipe(new ItemStack(NRItems.material, 9, 0),
				new Object[] {new ItemStack(NRBlocks.blockBlock, 1, 0)});
		GameRegistry.addShapelessRecipe(new ItemStack(NRItems.material, 9, 1),
				new Object[] {new ItemStack(NRBlocks.blockBlock, 1, 1)});
		GameRegistry.addShapelessRecipe(new ItemStack(NRItems.material, 9, 2),
				new Object[] {new ItemStack(NRBlocks.blockBlock, 1, 2)});
		GameRegistry.addShapelessRecipe(new ItemStack(NRItems.material, 9, 3),
				new Object[] {new ItemStack(NRBlocks.blockBlock, 1, 3)});
		GameRegistry.addShapelessRecipe(new ItemStack(NRItems.material, 9, 6),
				new Object[] {new ItemStack(NRBlocks.blockBlock, 1, 6)});
		GameRegistry.addShapelessRecipe(new ItemStack(NRItems.material, 9, 5),
				new Object[] {new ItemStack(NRBlocks.blockBlock, 1, 5)});
		GameRegistry.addShapelessRecipe(new ItemStack(NRItems.material, 9, 42),
				new Object[] {new ItemStack(NRBlocks.blockBlock, 1, 8)});
		GameRegistry.addShapelessRecipe(new ItemStack(NRItems.material, 9, 43),
				new Object[] {new ItemStack(NRBlocks.blockBlock, 1, 9)});
		GameRegistry.addShapelessRecipe(new ItemStack(NRItems.material, 25, 7),
				new Object[] {new ItemStack(NRBlocks.blockBlock, 1, 7)});
		GameRegistry.addShapelessRecipe(new ItemStack(NRItems.material, 9, 50),
				new Object[] {new ItemStack(NRBlocks.blockBlock, 1, 10)});
		
		GameRegistry.addShapelessRecipe(new ItemStack(NRBlocks.accStraight1, 1),
				new Object[] {new ItemStack(NRBlocks.accStraight2)});
		GameRegistry.addShapelessRecipe(new ItemStack(NRBlocks.accStraight2, 1),
				new Object[] {new ItemStack(NRBlocks.accStraight1)});
		
		// Complex Shapeless Crafting Recipes
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(NRItems.material, 4, 21),
				new Object[] {"dustCopper", "dustCopper", "dustCopper", "dustTin"}));
		
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(NRItems.fuel, 1, 0),
				new Object[] {"U238", "U238", "U238", "U238", "U238", "U238", "U238", "U238", new ItemStack(NRItems.material, 1, 26)}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(NRItems.fuel, 1, 6),
				new Object[] {"U238", "U238", "U238", "U238", "U238", "U238", "U238", "U238", new ItemStack(NRItems.material, 1, 28)}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(NRItems.fuel, 1, 1),
				new Object[] {"U238", "U238", "U238", "U238", "U238", new ItemStack(NRItems.material, 1, 26), new ItemStack(NRItems.material, 1, 26), new ItemStack(NRItems.material, 1, 26), new ItemStack(NRItems.material, 1, 26)}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(NRItems.fuel, 1, 7),
				new Object[] {"U238", "U238", "U238", "U238", "U238", new ItemStack(NRItems.material, 1, 28), new ItemStack(NRItems.material, 1, 28), new ItemStack(NRItems.material, 1, 28), new ItemStack(NRItems.material, 1, 28)}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(NRItems.fuel, 1, 2),
				new Object[] {"Pu242", "Pu242", "Pu242", "Pu242", "Pu242", "Pu242", "Pu242", "Pu242", new ItemStack(NRItems.material, 1, 32)}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(NRItems.fuel, 1, 8),
				new Object[] {"Pu242", "Pu242", "Pu242", "Pu242", "Pu242", "Pu242", "Pu242", "Pu242", new ItemStack(NRItems.material, 1, 36)}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(NRItems.fuel, 1, 3),
				new Object[] {"Pu242", "Pu242", "Pu242", "Pu242", "Pu242", new ItemStack(NRItems.material, 1, 32), new ItemStack(NRItems.material, 1, 32), new ItemStack(NRItems.material, 1, 32), new ItemStack(NRItems.material, 1, 32)}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(NRItems.fuel, 1, 9),
				new Object[] {"Pu242", "Pu242", "Pu242", "Pu242", "Pu242", new ItemStack(NRItems.material, 1, 36), new ItemStack(NRItems.material, 1, 36), new ItemStack(NRItems.material, 1, 36), new ItemStack(NRItems.material, 1, 36)}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(NRItems.fuel, 1, 5),
				new Object[] {new ItemStack(NRItems.material, 1, 38), new ItemStack(NRItems.material, 1, 38), new ItemStack(NRItems.material, 1, 38), new ItemStack(NRItems.material, 1, 38), new ItemStack(NRItems.material, 1, 38), new ItemStack(NRItems.material, 1, 38), new ItemStack(NRItems.material, 1, 38), new ItemStack(NRItems.material, 1, 38), new ItemStack(NRItems.material, 1, 38)}));
		
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(NRItems.fuel, 1, 51),
				new Object[] {"U238", "U238", "U238", "U238", "U238", "U238", "U238", "U238", new ItemStack(NRItems.material, 1, 57)}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(NRItems.fuel, 1, 55),
				new Object[] {"U238", "U238", "U238", "U238", "U238", "U238", "U238", "U238", new ItemStack(NRItems.material, 1, 59)}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(NRItems.fuel, 1, 52),
				new Object[] {"U238", "U238", "U238", "U238", "U238", new ItemStack(NRItems.material, 1, 57), new ItemStack(NRItems.material, 1, 57), new ItemStack(NRItems.material, 1, 57), new ItemStack(NRItems.material, 1, 57)}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(NRItems.fuel, 1, 56),
				new Object[] {"U238", "U238", "U238", "U238", "U238", new ItemStack(NRItems.material, 1, 59), new ItemStack(NRItems.material, 1, 59), new ItemStack(NRItems.material, 1, 59), new ItemStack(NRItems.material, 1, 59)}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(NRItems.fuel, 1, 53),
				new Object[] {"Pu242", "Pu242", "Pu242", "Pu242", "Pu242", "Pu242", "Pu242", "Pu242", new ItemStack(NRItems.material, 1, 63)}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(NRItems.fuel, 1, 57),
				new Object[] {"Pu242", "Pu242", "Pu242", "Pu242", "Pu242", "Pu242", "Pu242", "Pu242", new ItemStack(NRItems.material, 1, 67)}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(NRItems.fuel, 1, 54),
				new Object[] {"Pu242", "Pu242", "Pu242", "Pu242", "Pu242", new ItemStack(NRItems.material, 1, 63), new ItemStack(NRItems.material, 1, 63), new ItemStack(NRItems.material, 1, 63), new ItemStack(NRItems.material, 1, 63)}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(NRItems.fuel, 1, 58),
				new Object[] {"Pu242", "Pu242", "Pu242", "Pu242", "Pu242", new ItemStack(NRItems.material, 1, 67), new ItemStack(NRItems.material, 1, 67), new ItemStack(NRItems.material, 1, 67), new ItemStack(NRItems.material, 1, 67)}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(NRItems.fuel, 1, 4),
				new Object[] {"U238", "U238", "U238", "U238", "U238", "U238", "U238", "U238", new ItemStack(NRItems.material, 1, 63)}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(NRItems.fuel, 1, 10),
				new Object[] {"U238", "U238", "U238", "U238", "U238", "U238", "U238", "U238", new ItemStack(NRItems.material, 1, 67)}));
		
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(NRItems.parts, 3, 4),
				new Object[] {Items.sugar, "dustLapis", Items.redstone}));
		
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(NRItems.material, 4, 22),
				new Object[] {new ItemStack(NRItems.parts, 1, 4), "dustCoal", "dustCoal", "dustLead", "dustLead", "dustSilver", "dustSilver", "dustIron", "dustIron"}));
		
		GameRegistry.addShapelessRecipe(new ItemStack(NRItems.fuel, 1, 11),
				(new ItemStack (NRItems.fuel, 1, 0)), (new ItemStack (NRItems.fuel, 1, 33)));
		GameRegistry.addShapelessRecipe(new ItemStack(NRItems.fuel, 1, 17),
				(new ItemStack (NRItems.fuel, 1, 6)), (new ItemStack (NRItems.fuel, 1, 33)));
		GameRegistry.addShapelessRecipe(new ItemStack(NRItems.fuel, 1, 12),
				(new ItemStack (NRItems.fuel, 1, 1)), (new ItemStack (NRItems.fuel, 1, 33)));
		GameRegistry.addShapelessRecipe(new ItemStack(NRItems.fuel, 1, 18),
				(new ItemStack (NRItems.fuel, 1, 7)), (new ItemStack (NRItems.fuel, 1, 33)));
		GameRegistry.addShapelessRecipe(new ItemStack(NRItems.fuel, 1, 13),
				(new ItemStack (NRItems.fuel, 1, 2)), (new ItemStack (NRItems.fuel, 1, 33)));
		GameRegistry.addShapelessRecipe(new ItemStack(NRItems.fuel, 1, 19),
				(new ItemStack (NRItems.fuel, 1, 8)), (new ItemStack (NRItems.fuel, 1, 33)));
		GameRegistry.addShapelessRecipe(new ItemStack(NRItems.fuel, 1, 14),
				(new ItemStack (NRItems.fuel, 1, 3)), (new ItemStack (NRItems.fuel, 1, 33)));
		GameRegistry.addShapelessRecipe(new ItemStack(NRItems.fuel, 1, 20),
				(new ItemStack (NRItems.fuel, 1, 9)), (new ItemStack (NRItems.fuel, 1, 33)));
		GameRegistry.addShapelessRecipe(new ItemStack(NRItems.fuel, 1, 15),
				(new ItemStack (NRItems.fuel, 1, 4)), (new ItemStack (NRItems.fuel, 1, 33)));
		GameRegistry.addShapelessRecipe(new ItemStack(NRItems.fuel, 1, 21),
				(new ItemStack (NRItems.fuel, 1, 10)), (new ItemStack (NRItems.fuel, 1, 33)));
		GameRegistry.addShapelessRecipe(new ItemStack(NRItems.fuel, 1, 16),
				(new ItemStack (NRItems.fuel, 1, 5)), (new ItemStack (NRItems.fuel, 1, 33)));
		
		GameRegistry.addShapelessRecipe(new ItemStack(NRItems.fuel, 1, 59),
				(new ItemStack (NRItems.fuel, 1, 51)), (new ItemStack (NRItems.fuel, 1, 33)));
		GameRegistry.addShapelessRecipe(new ItemStack(NRItems.fuel, 1, 63),
				(new ItemStack (NRItems.fuel, 1, 55)), (new ItemStack (NRItems.fuel, 1, 33)));
		GameRegistry.addShapelessRecipe(new ItemStack(NRItems.fuel, 1, 60),
				(new ItemStack (NRItems.fuel, 1, 52)), (new ItemStack (NRItems.fuel, 1, 33)));
		GameRegistry.addShapelessRecipe(new ItemStack(NRItems.fuel, 1, 64),
				(new ItemStack (NRItems.fuel, 1, 56)), (new ItemStack (NRItems.fuel, 1, 33)));
		GameRegistry.addShapelessRecipe(new ItemStack(NRItems.fuel, 1, 61),
				(new ItemStack (NRItems.fuel, 1, 53)), (new ItemStack (NRItems.fuel, 1, 33)));
		GameRegistry.addShapelessRecipe(new ItemStack(NRItems.fuel, 1, 65),
				(new ItemStack (NRItems.fuel, 1, 57)), (new ItemStack (NRItems.fuel, 1, 33)));
		GameRegistry.addShapelessRecipe(new ItemStack(NRItems.fuel, 1, 62),
				(new ItemStack (NRItems.fuel, 1, 54)), (new ItemStack (NRItems.fuel, 1, 33)));
		GameRegistry.addShapelessRecipe(new ItemStack(NRItems.fuel, 1, 66),
				(new ItemStack (NRItems.fuel, 1, 58)), (new ItemStack (NRItems.fuel, 1, 33)));
		
		GameRegistry.addShapelessRecipe(new ItemStack(NRItems.fuel, 1, 41),
				(new ItemStack (NRItems.material, 1, 46)), (new ItemStack (NRItems.fuel, 1, 33)));
		GameRegistry.addShapelessRecipe(new ItemStack(NRItems.fuel, 1, 42),
				(new ItemStack (NRItems.material, 1, 47)), (new ItemStack (NRItems.fuel, 1, 33)));
		GameRegistry.addShapelessRecipe(new ItemStack(NRItems.fuel, 1, 43),
				(new ItemStack (NRItems.material, 1, 48)), (new ItemStack (NRItems.fuel, 1, 33)));
		GameRegistry.addShapelessRecipe(new ItemStack(NRItems.fuel, 1, 44),
				(new ItemStack (NRItems.material, 1, 49)), (new ItemStack (NRItems.fuel, 1, 33)));
		
		// Smelting Recipes
		GameRegistry.addSmelting(new ItemStack(NRBlocks.blockOre, 1, 4), new ItemStack (NRItems.material, 1, 4), 1.2F);
		GameRegistry.addSmelting(new ItemStack(NRBlocks.blockOre, 1, 0), new ItemStack(NRItems.material, 1, 0), 0.6F);
		GameRegistry.addSmelting(new ItemStack(NRBlocks.blockOre, 1, 1), new ItemStack(NRItems.material, 1, 1), 0.6F);
		GameRegistry.addSmelting(new ItemStack(NRBlocks.blockOre, 1, 2), new ItemStack(NRItems.material, 1, 2), 0.8F);
		GameRegistry.addSmelting(new ItemStack(NRBlocks.blockOre, 1, 3), new ItemStack(NRItems.material, 1, 3), 0.8F);
		GameRegistry.addSmelting(new ItemStack(NRBlocks.blockOre, 1, 5), new ItemStack(NRItems.material, 1, 5), 1.2F);
		GameRegistry.addSmelting(new ItemStack(NRBlocks.blockOre, 1, 6), new ItemStack(NRItems.material, 1, 33), 1.2F);
		GameRegistry.addSmelting(new ItemStack(NRBlocks.blockOre, 1, 7), new ItemStack(NRItems.material, 1, 42), 0.8F);
		GameRegistry.addSmelting(new ItemStack(NRBlocks.blockOre, 1, 8), new ItemStack(NRItems.material, 1, 43), 0.8F);
		
		GameRegistry.addSmelting(new ItemStack (NRItems.material, 1, 8), new ItemStack(Items.iron_ingot), 0.0F);
		GameRegistry.addSmelting(new ItemStack (NRItems.material, 1, 9), new ItemStack(Items.gold_ingot), 0.0F);
		GameRegistry.addSmelting(new ItemStack (NRItems.material, 1, 15), new ItemStack(NRItems.material, 1, 0), 0.0F);
		GameRegistry.addSmelting(new ItemStack (NRItems.material, 1, 17), new ItemStack(NRItems.material, 1, 2), 0.0F);
		GameRegistry.addSmelting(new ItemStack (NRItems.material, 1, 16), new ItemStack(NRItems.material, 1, 1), 0.0F);
		GameRegistry.addSmelting(new ItemStack (NRItems.material, 1, 18), new ItemStack(NRItems.material, 1, 3), 0.0F);
		GameRegistry.addSmelting(new ItemStack (NRItems.material, 1, 19), new ItemStack(NRItems.material, 1, 4), 0.0F);
		GameRegistry.addSmelting(new ItemStack (NRItems.material, 1, 20), new ItemStack(NRItems.material, 1, 5), 0.0F);
		GameRegistry.addSmelting(new ItemStack (NRItems.material, 1, 21), new ItemStack(NRItems.material, 1, 6), 0.0F);
		GameRegistry.addSmelting(new ItemStack (NRItems.material, 1, 22), new ItemStack(NRItems.material, 1, 7), 0.0F);
		GameRegistry.addSmelting(new ItemStack (NRItems.material, 1, 44), new ItemStack(NRItems.material, 1, 42), 0.0F);
		GameRegistry.addSmelting(new ItemStack (NRItems.material, 1, 45), new ItemStack(NRItems.material, 1, 43), 0.0F);
		GameRegistry.addSmelting(new ItemStack (NRItems.material, 1, 51), new ItemStack(NRItems.material, 1, 50), 0.0F);
		GameRegistry.addSmelting(new ItemStack (NRItems.material, 1, 54), new ItemStack(NRItems.material, 1, 53), 0.0F);
		
		GameRegistry.addSmelting(new ItemStack (Items.egg, 1), new ItemStack(NRItems.boiledEgg, 1), 0.1F);
		
		// Gui Handler
		@SuppressWarnings("unused")
		GuiHandler guiHandler = new GuiHandler();
		
		// Proxy
		NRProxy.registerRenderThings();
		NRProxy.registerSounds();
		NRProxy.registerTileEntitySpecialRenderer();
		
		// Entities
		EntityHandler.registerMonsters(EntityNuclearMonster.class, "NuclearMonster");
		EntityHandler.registerPaul(EntityPaul.class, "Paul");
		EntityHandler.registerNuke(EntityNukePrimed.class, "NukePrimed");
		EntityHandler.registerNuclearGrenade(EntityNuclearGrenade.class, "NuclearGrenade");
		EntityHandler.registerEntityBullet(EntityBullet.class, "EntityBullet");
				
		// Fuel Handler	
		GameRegistry.registerFuelHandler(new FuelHandler());
			
		// Random Chest Loot
		ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.dominoes, 1), 4, 5, 16));
		ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.upgrade, 1), 2, 3, 12));
		ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.fuel, 1, 4), 1, 2, 16));
		ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NRBlocks.WRTG, 1), 1, 1, 4));
		ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.material, 1, 32), 2, 5, 8));
		ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.material, 1, 59), 3, 4, 12));
		ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.fuel, 1, 47), 1, 2, 4));
		ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NRBlocks.nuclearWorkspace, 1), 1, 1, 2));
		ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NRBlocks.blastBlock, 1), 6, 12, 10));
		ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.boronHelm, 1), 1, 1, 16));
		ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.boronChest, 1), 1, 1, 12));
		ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.boronLegs, 1), 1, 1, 12));
		ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.boronBoots, 1), 1, 1, 16));
		ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.pistol, 1), 1, 1, 10));
		ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.dUBullet, 1), 6, 8, 10));
		
		ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.dominoes, 1), 4, 5, 16));
		ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.upgrade, 1), 2, 3, 12));
		ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.fuel, 1, 4), 1, 2, 16));
		ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NRBlocks.WRTG, 1), 1, 1, 4));
		ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.material, 1, 32), 2, 5, 8));
		ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.material, 1, 59), 3, 4, 12));
		ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.fuel, 1, 47), 1, 2, 4));
		ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NRBlocks.nuclearWorkspace, 1), 1, 1, 2));
		ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NRBlocks.blastBlock, 1), 6, 12, 10));
		ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.boronHelm, 1), 1, 1, 16));
		ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.boronChest, 1), 1, 1, 12));
		ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.boronLegs, 1), 1, 1, 12));
		ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.boronBoots, 1), 1, 1, 16));
		ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.pistol, 1), 1, 1, 10));
		ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.dUBullet, 1), 6, 8, 10));
		
		ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.dominoes, 1), 4, 5, 16));
		ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.upgrade, 1), 2, 3, 12));
		ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.fuel, 1, 4), 1, 2, 16));
		ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NRBlocks.WRTG, 1), 1, 1, 4));
		ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.material, 1, 32), 2, 5, 8));
		ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.material, 1, 59), 3, 4, 12));
		ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.fuel, 1, 47), 1, 2, 4));
		ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NRBlocks.nuclearWorkspace, 1), 1, 1, 2));
		ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NRBlocks.blastBlock, 1), 6, 12, 10));
		ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.boronHelm, 1), 1, 1, 16));
		ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.boronChest, 1), 1, 1, 12));
		ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.boronLegs, 1), 1, 1, 12));
		ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.boronBoots, 1), 1, 1, 16));
		
		ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_DISPENSER).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.toughBow, 1), 1, 1, 40));
		ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_DISPENSER).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.pistol, 1), 1, 1, 20));
		ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_DISPENSER).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.dUBullet, 1), 6, 8, 40));
		
		ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.dominoes, 1), 4, 5, 16));
		ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.upgrade, 1), 2, 3, 12));
		ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.fuel, 1, 4), 1, 2, 16));
		ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NRBlocks.WRTG, 1), 1, 1, 4));
		ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.material, 1, 32), 2, 5, 8));
		ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.material, 1, 59), 3, 4, 12));
		ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.fuel, 1, 47), 1, 2, 4));
		ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NRBlocks.nuclearWorkspace, 1), 1, 1, 2));
		ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NRBlocks.blastBlock, 1), 6, 12, 10));
		ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.boronHelm, 1), 1, 1, 16));
		ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.boronChest, 1), 1, 1, 12));
		ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.boronLegs, 1), 1, 1, 12));
		ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.boronBoots, 1), 1, 1, 16));
		
		ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.dominoes, 1), 4, 5, 16));
		ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.upgrade, 1), 2, 3, 12));
		ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.fuel, 1, 4), 1, 2, 16));
		ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(new WeightedRandomChestContent(new ItemStack(NRBlocks.WRTG, 1), 1, 1, 4));
		ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.material, 1, 32), 2, 5, 8));
		ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.material, 1, 59), 3, 4, 12));
		ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.fuel, 1, 47), 1, 2, 4));
		ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(new WeightedRandomChestContent(new ItemStack(NRBlocks.nuclearWorkspace, 1), 1, 1, 2));
		ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(new WeightedRandomChestContent(new ItemStack(NRBlocks.blastBlock, 1), 6, 12, 10));
		ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.boronHelm, 1), 1, 1, 16));
		ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.boronChest, 1), 1, 1, 12));
		ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.boronLegs, 1), 1, 1, 12));
		ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.boronBoots, 1), 1, 1, 16));
		ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.pistol, 1), 1, 1, 10));
		ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.dUBullet, 1), 6, 8, 10));
		
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.dominoes, 1), 4, 5, 16));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.upgrade, 1), 2, 3, 12));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.fuel, 1, 4), 1, 2, 16));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NRBlocks.WRTG, 1), 1, 1, 4));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.material, 1, 32), 2, 5, 8));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.material, 1, 59), 3, 4, 12));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.fuel, 1, 47), 1, 2, 4));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NRBlocks.nuclearWorkspace, 1), 1, 1, 2));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NRBlocks.blastBlock, 1), 6, 12, 10));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.toughHelm, 1), 1, 1, 16));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.toughChest, 1), 1, 1, 12));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.toughLegs, 1), 1, 1, 12));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.toughBoots, 1), 1, 1, 16));
		
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CROSSING).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.dominoes, 1), 4, 5, 16));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CROSSING).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.upgrade, 1), 2, 3, 12));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CROSSING).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.fuel, 1, 4), 1, 2, 16));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CROSSING).addItem(new WeightedRandomChestContent(new ItemStack(NRBlocks.WRTG, 1), 1, 1, 4));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CROSSING).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.material, 1, 32), 2, 5, 8));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CROSSING).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.material, 1, 59), 3, 4, 12));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CROSSING).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.fuel, 1, 47), 1, 2, 4));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CROSSING).addItem(new WeightedRandomChestContent(new ItemStack(NRBlocks.nuclearWorkspace, 1), 1, 1, 2));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CROSSING).addItem(new WeightedRandomChestContent(new ItemStack(NRBlocks.blastBlock, 1), 6, 12, 10));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CROSSING).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.toughHelm, 1), 1, 1, 16));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CROSSING).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.toughChest, 1), 1, 1, 12));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CROSSING).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.toughLegs, 1), 1, 1, 12));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CROSSING).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.toughBoots, 1), 1, 1, 16));
		
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_LIBRARY).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.dominoes, 1), 4, 5, 16));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_LIBRARY).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.upgrade, 1), 2, 3, 12));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_LIBRARY).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.fuel, 1, 4), 1, 2, 16));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_LIBRARY).addItem(new WeightedRandomChestContent(new ItemStack(NRBlocks.WRTG, 1), 1, 1, 4));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_LIBRARY).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.material, 1, 32), 2, 5, 8));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_LIBRARY).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.material, 1, 59), 3, 4, 12));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_LIBRARY).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.fuel, 1, 47), 1, 2, 4));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_LIBRARY).addItem(new WeightedRandomChestContent(new ItemStack(NRBlocks.nuclearWorkspace, 1), 1, 1, 2));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_LIBRARY).addItem(new WeightedRandomChestContent(new ItemStack(NRBlocks.blastBlock, 1), 6, 12, 10));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_LIBRARY).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.toughHelm, 1), 1, 1, 16));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_LIBRARY).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.toughChest, 1), 1, 1, 12));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_LIBRARY).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.toughLegs, 1), 1, 1, 12));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_LIBRARY).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.toughBoots, 1), 1, 1, 16));
		
		ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.dominoes, 1), 2, 4, 8));
		ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.boiledEgg, 1), 3, 5, 8));
		ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.boronHelm, 1), 1, 1, 6));
		ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.boronChest, 1), 1, 1, 2));
		ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.boronLegs, 1), 1, 1, 2));
		ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.boronBoots, 1), 1, 1, 6));
		ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.bronzeHelm, 1), 1, 1, 6));
		ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.bronzeChest, 1), 1, 1, 4));
		ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.bronzeLegs, 1), 1, 1, 4));
		ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.bronzeBoots, 1), 1, 1, 6));
		ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(NRItems.toughAlloyShovel, 1), 1, 1, 2));
				
		// World Generation Registry
		GameRegistry.registerWorldGenerator(new OreGen(), 1);
		
		// Inter Mod Comms - Mekanism
		NBTTagCompound copperOreEnrichment = new NBTTagCompound();
		copperOreEnrichment.setTag("input", new ItemStack(NRBlocks.blockOre, 1, 0).writeToNBT(new NBTTagCompound()));
		copperOreEnrichment.setTag("output", new ItemStack(NRItems.material, 2, 15).writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "EnrichmentChamberRecipe", copperOreEnrichment);
		
		NBTTagCompound tinOreEnrichment = new NBTTagCompound();
		tinOreEnrichment.setTag("input", new ItemStack(NRBlocks.blockOre, 1, 1).writeToNBT(new NBTTagCompound()));
		tinOreEnrichment.setTag("output", new ItemStack(NRItems.material, 2, 16).writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "EnrichmentChamberRecipe", tinOreEnrichment);
		
		NBTTagCompound leadOreEnrichment = new NBTTagCompound();
		leadOreEnrichment.setTag("input", new ItemStack(NRBlocks.blockOre, 1, 2).writeToNBT(new NBTTagCompound()));
		leadOreEnrichment.setTag("output", new ItemStack(NRItems.material, 2, 17).writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "EnrichmentChamberRecipe", leadOreEnrichment);
		
		NBTTagCompound silverOreEnrichment = new NBTTagCompound();
		silverOreEnrichment.setTag("input", new ItemStack(NRBlocks.blockOre, 1, 3).writeToNBT(new NBTTagCompound()));
		silverOreEnrichment.setTag("output", new ItemStack(NRItems.material, 2, 18).writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "EnrichmentChamberRecipe", silverOreEnrichment);
		
		NBTTagCompound uraniumOreEnrichment = new NBTTagCompound();
		uraniumOreEnrichment.setTag("input", new ItemStack(NRBlocks.blockOre, 1, 4).writeToNBT(new NBTTagCompound()));
		uraniumOreEnrichment.setTag("output", new ItemStack(NRItems.material, 2, 19).writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "EnrichmentChamberRecipe", uraniumOreEnrichment);
		
		NBTTagCompound thoriumOreEnrichment = new NBTTagCompound();
		thoriumOreEnrichment.setTag("input", new ItemStack(NRBlocks.blockOre, 1, 5).writeToNBT(new NBTTagCompound()));
		thoriumOreEnrichment.setTag("output", new ItemStack(NRItems.material, 2, 20).writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "EnrichmentChamberRecipe", thoriumOreEnrichment);
		
		NBTTagCompound plutoniumOreEnrichment = new NBTTagCompound();
		plutoniumOreEnrichment.setTag("input", new ItemStack(NRBlocks.blockOre, 1, 6).writeToNBT(new NBTTagCompound()));
		plutoniumOreEnrichment.setTag("output", new ItemStack(NRItems.material, 2, 33).writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "EnrichmentChamberRecipe", plutoniumOreEnrichment);
		
		NBTTagCompound lithiumOreEnrichment = new NBTTagCompound();
		lithiumOreEnrichment.setTag("input", new ItemStack(NRBlocks.blockOre, 1, 7).writeToNBT(new NBTTagCompound()));
		lithiumOreEnrichment.setTag("output", new ItemStack(NRItems.material, 2, 44).writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "EnrichmentChamberRecipe", lithiumOreEnrichment);
		
		NBTTagCompound boronOreEnrichment = new NBTTagCompound();
		boronOreEnrichment.setTag("input", new ItemStack(NRBlocks.blockOre, 1, 8).writeToNBT(new NBTTagCompound()));
		boronOreEnrichment.setTag("output", new ItemStack(NRItems.material, 2, 45).writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "EnrichmentChamberRecipe", boronOreEnrichment);
		
		NBTTagCompound basicPlatingEnrichment = new NBTTagCompound();
		basicPlatingEnrichment.setTag("input", new ItemStack(NRItems.parts, 4, 0).writeToNBT(new NBTTagCompound()));
		basicPlatingEnrichment.setTag("output", new ItemStack(NRItems.parts, 1, 3).writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "EnrichmentChamberRecipe", basicPlatingEnrichment);
		
		NBTTagCompound ingotToPlatingEnrichment = new NBTTagCompound();
		ingotToPlatingEnrichment.setTag("input", new ItemStack(NRItems.material, 1, 7).writeToNBT(new NBTTagCompound()));
		ingotToPlatingEnrichment.setTag("output", new ItemStack(NRItems.parts, 3, 0).writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "EnrichmentChamberRecipe", ingotToPlatingEnrichment);
		
		NBTTagCompound uraniumIngotCrushing = new NBTTagCompound();
		uraniumIngotCrushing.setTag("input", new ItemStack(NRItems.material, 1, 4).writeToNBT(new NBTTagCompound()));
		uraniumIngotCrushing.setTag("output", new ItemStack(NRItems.material, 1, 19).writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "CrusherRecipe", uraniumIngotCrushing);
		
		NBTTagCompound thoriumIngotCrushing = new NBTTagCompound();
		thoriumIngotCrushing.setTag("input", new ItemStack(NRItems.material, 1, 5).writeToNBT(new NBTTagCompound()));
		thoriumIngotCrushing.setTag("output", new ItemStack(NRItems.material, 1, 20).writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "CrusherRecipe", thoriumIngotCrushing);
		
		NBTTagCompound bronzeIngotCrushing = new NBTTagCompound();
		bronzeIngotCrushing.setTag("input", new ItemStack(NRItems.material, 1, 6).writeToNBT(new NBTTagCompound()));
		bronzeIngotCrushing.setTag("output", new ItemStack(NRItems.material, 1, 21).writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "CrusherRecipe", bronzeIngotCrushing);
		
		NBTTagCompound toughIngotCrushing = new NBTTagCompound();
		toughIngotCrushing.setTag("input", new ItemStack(NRItems.material, 1, 7).writeToNBT(new NBTTagCompound()));
		toughIngotCrushing.setTag("output", new ItemStack(NRItems.material, 1, 22).writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "CrusherRecipe", toughIngotCrushing);
		
		NBTTagCompound lithiumIngotCrushing = new NBTTagCompound();
		lithiumIngotCrushing.setTag("input", new ItemStack(NRItems.material, 1, 42).writeToNBT(new NBTTagCompound()));
		lithiumIngotCrushing.setTag("output", new ItemStack(NRItems.material, 1, 44).writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "CrusherRecipe", lithiumIngotCrushing);
		
		NBTTagCompound boronIngotCrushing = new NBTTagCompound();
		boronIngotCrushing.setTag("input", new ItemStack(NRItems.material, 1, 43).writeToNBT(new NBTTagCompound()));
		boronIngotCrushing.setTag("output", new ItemStack(NRItems.material, 1, 45).writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "CrusherRecipe", boronIngotCrushing);
		
		NBTTagCompound aluminiumIngotCrushing = new NBTTagCompound();
		aluminiumIngotCrushing.setTag("input", new ItemStack(NRItems.material, 1, 50).writeToNBT(new NBTTagCompound()));
		aluminiumIngotCrushing.setTag("output", new ItemStack(NRItems.material, 1, 51).writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "CrusherRecipe", aluminiumIngotCrushing);
		
		NBTTagCompound leadCombining = new NBTTagCompound();
		leadCombining.setTag("input", new ItemStack(NRItems.material, 8, 17).writeToNBT(new NBTTagCompound()));
		leadCombining.setTag("output", new ItemStack(NRBlocks.blockOre, 1, 2).writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "CombinerRecipe", leadCombining);
		
		NBTTagCompound silverCombining = new NBTTagCompound();
		silverCombining.setTag("input", new ItemStack(NRItems.material, 8, 18).writeToNBT(new NBTTagCompound()));
		silverCombining.setTag("output", new ItemStack(NRBlocks.blockOre, 1, 3).writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "CombinerRecipe", silverCombining);
		
		NBTTagCompound uraniumCombining = new NBTTagCompound();
		uraniumCombining.setTag("input", new ItemStack(NRItems.material, 8, 19).writeToNBT(new NBTTagCompound()));
		uraniumCombining.setTag("output", new ItemStack(NRBlocks.blockOre, 1, 4).writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "CombinerRecipe", uraniumCombining);
		
		NBTTagCompound thoriumCombining = new NBTTagCompound();
		thoriumCombining.setTag("input", new ItemStack(NRItems.material, 8, 20).writeToNBT(new NBTTagCompound()));
		thoriumCombining.setTag("output", new ItemStack(NRBlocks.blockOre, 1, 5).writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "CombinerRecipe", thoriumCombining);
		
		NBTTagCompound lithiumCombining = new NBTTagCompound();
		lithiumCombining.setTag("input", new ItemStack(NRItems.material, 8, 44).writeToNBT(new NBTTagCompound()));
		lithiumCombining.setTag("output", new ItemStack(NRBlocks.blockOre, 1, 7).writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "CombinerRecipe", lithiumCombining);
		
		NBTTagCompound boronCombining = new NBTTagCompound();
		boronCombining.setTag("input", new ItemStack(NRItems.material, 8, 45).writeToNBT(new NBTTagCompound()));
		boronCombining.setTag("output", new ItemStack(NRBlocks.blockOre, 1, 8).writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", "CombinerRecipe", boronCombining);
		
		// Inter Mod Comms - AE2
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", TileEntityNuclearFurnace.class.getCanonicalName());
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", TileEntityFurnace.class.getCanonicalName());
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", TileEntityCrusher.class.getCanonicalName());
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", TileEntityElectricCrusher.class.getCanonicalName());
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", TileEntityElectricFurnace.class.getCanonicalName());
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", TileEntityReactionGenerator.class.getCanonicalName());
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", TileEntitySeparator.class.getCanonicalName());
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", TileEntityHastener.class.getCanonicalName());
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", TileEntityCollector.class.getCanonicalName());
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", TileEntityFissionReactorGraphite.class.getCanonicalName());
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", TileEntityNuclearWorkspace.class.getCanonicalName());
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", TileEntityFusionReactor.class.getCanonicalName());
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", TileEntityAccStraight1.class.getCanonicalName());
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", TileEntityAccStraight2.class.getCanonicalName());
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", TileEntityRTG.class.getCanonicalName());
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", TileEntityWRTG.class.getCanonicalName());
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", TileEntityFusionReactorBlock.class.getCanonicalName());
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", TileEntityElectrolyser.class.getCanonicalName());
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", TileEntityOxidiser.class.getCanonicalName());
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", TileEntityIoniser.class.getCanonicalName());
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", TileEntityIrradiator.class.getCanonicalName());
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", TileEntityCooler.class.getCanonicalName());
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", TileEntityFactory.class.getCanonicalName());
		
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", TileEntitySimpleQuantum.class.getCanonicalName());
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		// Ores Ore Dictionary
		OreDictionary.registerOre("oreUranium", new ItemStack(NRBlocks.blockOre, 1, 4));
		OreDictionary.registerOre("oreCopper", new ItemStack(NRBlocks.blockOre, 1, 0));
		OreDictionary.registerOre("oreTin", new ItemStack(NRBlocks.blockOre, 1, 1));
		OreDictionary.registerOre("oreLead", new ItemStack(NRBlocks.blockOre, 1, 2));
		OreDictionary.registerOre("oreSilver", new ItemStack(NRBlocks.blockOre, 1, 3));
		OreDictionary.registerOre("oreThorium", new ItemStack(NRBlocks.blockOre, 1, 5));
		OreDictionary.registerOre("orePlutonium", new ItemStack(NRBlocks.blockOre, 1, 6));
		OreDictionary.registerOre("oreLithium", new ItemStack(NRBlocks.blockOre, 1, 7));
		OreDictionary.registerOre("oreBoron", new ItemStack(NRBlocks.blockOre, 1, 8));
		
		// Items Ore Dictionary
		OreDictionary.registerOre("ingotUranium", new ItemStack(NRItems.material, 1, 4));
		OreDictionary.registerOre("ingotCopper", new ItemStack(NRItems.material, 1, 0));
		OreDictionary.registerOre("ingotTin", new ItemStack(NRItems.material, 1, 1));
		OreDictionary.registerOre("ingotLead", new ItemStack(NRItems.material, 1, 2));
		OreDictionary.registerOre("ingotSilver", new ItemStack(NRItems.material, 1, 3));
		OreDictionary.registerOre("ingotBronze", new ItemStack(NRItems.material, 1, 6));
		OreDictionary.registerOre("ingotThorium", new ItemStack(NRItems.material, 1, 5));
		OreDictionary.registerOre("ingotLithium", new ItemStack(NRItems.material, 1, 42));
		OreDictionary.registerOre("ingotBoron", new ItemStack(NRItems.material, 1, 43));
		OreDictionary.registerOre("ingotTough", new ItemStack(NRItems.material, 1, 7));
		OreDictionary.registerOre("ingotAluminium", new ItemStack(NRItems.material, 1, 50));
		OreDictionary.registerOre("ingotAluminum", new ItemStack(NRItems.material, 1, 50));
		OreDictionary.registerOre("ingotUraniumOxide", new ItemStack(NRItems.material, 1, 53));
			
		// Dusts Ore Dictionary
		OreDictionary.registerOre("dustIron", new ItemStack(NRItems.material, 1, 8));
		OreDictionary.registerOre("dustGold", new ItemStack(NRItems.material, 1, 9));
		OreDictionary.registerOre("dustLapis", new ItemStack(NRItems.material, 1, 10));
		OreDictionary.registerOre("dustDiamond", new ItemStack(NRItems.material, 1, 11));
		OreDictionary.registerOre("dustEmerald", new ItemStack(NRItems.material, 1, 12));
		OreDictionary.registerOre("dustQuartz", new ItemStack(NRItems.material, 1, 13));
		OreDictionary.registerOre("dustCoal", new ItemStack(NRItems.material, 1, 14));
		OreDictionary.registerOre("dustCopper", new ItemStack(NRItems.material, 1, 15));
		OreDictionary.registerOre("dustLead", new ItemStack(NRItems.material, 1, 17));
		OreDictionary.registerOre("dustTin", new ItemStack(NRItems.material, 1, 16));
		OreDictionary.registerOre("dustSilver", new ItemStack(NRItems.material, 1, 18));
		OreDictionary.registerOre("dustUranium", new ItemStack(NRItems.material, 1, 19));
		OreDictionary.registerOre("dustThorium", new ItemStack(NRItems.material, 1, 20));
		OreDictionary.registerOre("dustBronze", new ItemStack(NRItems.material, 1, 21));
		OreDictionary.registerOre("dustLithium", new ItemStack(NRItems.material, 1, 44));
		OreDictionary.registerOre("dustBoron", new ItemStack(NRItems.material, 1, 45));
		OreDictionary.registerOre("dustTough", new ItemStack(NRItems.material, 1, 22));
		OreDictionary.registerOre("dustAluminium", new ItemStack(NRItems.material, 1, 51));
		OreDictionary.registerOre("dustAluminum", new ItemStack(NRItems.material, 1, 51));
		OreDictionary.registerOre("dustObsidian", new ItemStack(NRItems.material, 1, 52));
		OreDictionary.registerOre("dustUraniumOxide", new ItemStack(NRItems.material, 1, 54));
		
		// Blocks Ore Dictionary
		OreDictionary.registerOre("blockUranium", new ItemStack(NRBlocks.blockBlock, 1, 4));
		OreDictionary.registerOre("blockCopper", new ItemStack(NRBlocks.blockBlock, 1, 0));
		OreDictionary.registerOre("blockTin", new ItemStack(NRBlocks.blockBlock, 1, 1));
		OreDictionary.registerOre("blockLead", new ItemStack(NRBlocks.blockBlock, 1, 2));
		OreDictionary.registerOre("blockSilver", new ItemStack(NRBlocks.blockBlock, 1, 3));
		OreDictionary.registerOre("blockBronze", new ItemStack(NRBlocks.blockBlock, 1, 6));
		OreDictionary.registerOre("blockThorium", new ItemStack(NRBlocks.blockBlock, 1, 5));
		OreDictionary.registerOre("blockTough", new ItemStack(NRBlocks.blockBlock, 1, 7));
		OreDictionary.registerOre("blockLithium", new ItemStack(NRBlocks.blockBlock, 1, 8));
		OreDictionary.registerOre("blockBoron", new ItemStack(NRBlocks.blockBlock, 1, 9));
		OreDictionary.registerOre("blockAluminium", new ItemStack(NRBlocks.blockBlock, 1, 10));
		OreDictionary.registerOre("blockAluminum", new ItemStack(NRBlocks.blockBlock, 1, 10));
		
		// Parts Ore Dictionary
		OreDictionary.registerOre("universalReactant", new ItemStack(NRItems.parts, 1, 4));
		
		// Non-Fissile Materials Ore Dictionary
		OreDictionary.registerOre("U238", new ItemStack(NRItems.material, 1, 24));
		OreDictionary.registerOre("U238", new ItemStack(NRItems.material, 1, 55));
		OreDictionary.registerOre("tinyU238", new ItemStack(NRItems.material, 1, 25));
		OreDictionary.registerOre("tinyU238", new ItemStack(NRItems.material, 1, 56));
		OreDictionary.registerOre("U235", new ItemStack(NRItems.material, 1, 26));
		OreDictionary.registerOre("U235", new ItemStack(NRItems.material, 1, 57));
		OreDictionary.registerOre("tinyU235", new ItemStack(NRItems.material, 1, 27));
		OreDictionary.registerOre("tinyU235", new ItemStack(NRItems.material, 1, 58));
		OreDictionary.registerOre("U233", new ItemStack(NRItems.material, 1, 28));
		OreDictionary.registerOre("U233", new ItemStack(NRItems.material, 1, 59));
		OreDictionary.registerOre("tinyU233", new ItemStack(NRItems.material, 1, 29));
		OreDictionary.registerOre("tinyU233", new ItemStack(NRItems.material, 1, 60));
		OreDictionary.registerOre("Pu238", new ItemStack(NRItems.material, 1, 30));
		OreDictionary.registerOre("Pu238", new ItemStack(NRItems.material, 1, 61));
		OreDictionary.registerOre("tinyPu238", new ItemStack(NRItems.material, 1, 31));
		OreDictionary.registerOre("tinyPu238", new ItemStack(NRItems.material, 1, 62));
		OreDictionary.registerOre("Pu239", new ItemStack(NRItems.material, 1, 32));
		OreDictionary.registerOre("Pu239", new ItemStack(NRItems.material, 1, 63));
		OreDictionary.registerOre("tinyPu239", new ItemStack(NRItems.material, 1, 33));
		OreDictionary.registerOre("tinyPu239", new ItemStack(NRItems.material, 1, 64));
		OreDictionary.registerOre("Pu242", new ItemStack(NRItems.material, 1, 34));
		OreDictionary.registerOre("Pu242", new ItemStack(NRItems.material, 1, 65));
		OreDictionary.registerOre("tinyPu242", new ItemStack(NRItems.material, 1, 35));
		OreDictionary.registerOre("tinyPu242", new ItemStack(NRItems.material, 1, 66));
		OreDictionary.registerOre("Pu241", new ItemStack(NRItems.material, 1, 36));
		OreDictionary.registerOre("Pu241", new ItemStack(NRItems.material, 1, 67));
		OreDictionary.registerOre("tinyPu241", new ItemStack(NRItems.material, 1, 37));
		OreDictionary.registerOre("tinyPu241", new ItemStack(NRItems.material, 1, 68));
		
		// Vanilla Ore Dictionary
		OreDictionary.registerOre("gemCoal", new ItemStack(Items.coal, 1));
		OreDictionary.registerOre("oreObsidian", new ItemStack(Blocks.obsidian, 1));
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		// Mod Recipes
		IC2Hook = new ModRecipes();
		IC2Hook.hook();
	}
}