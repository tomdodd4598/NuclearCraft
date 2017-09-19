package nc.init;

import nc.Global;
import nc.block.BlockIce;
import nc.block.BlockIngot;
import nc.block.BlockMushroom;
import nc.block.BlockOre;
import nc.block.BlockTransparent;
import nc.block.NCBlock;
import nc.block.fission.BlockCooler;
import nc.block.fission.BlockFission;
import nc.block.item.ItemBlockCooler;
import nc.block.item.ItemBlockFission;
import nc.block.item.ItemBlockIngot;
import nc.block.item.ItemBlockOre;
import nc.block.item.NCItemBlock;
import nc.block.tile.dummy.BlockFissionPort;
import nc.block.tile.dummy.BlockFusionDummySide;
import nc.block.tile.dummy.BlockFusionDummyTop;
import nc.block.tile.dummy.BlockMachineInterface;
import nc.block.tile.energyStorage.BlockLithiumIonBatteryBasic;
import nc.block.tile.energyStorage.BlockVoltaicPileBasic;
import nc.block.tile.generator.BlockAmericiumRTG;
import nc.block.tile.generator.BlockCaliforniumRTG;
import nc.block.tile.generator.BlockFissionController;
import nc.block.tile.generator.BlockFusionCore;
import nc.block.tile.generator.BlockPlutoniumRTG;
import nc.block.tile.generator.BlockSolarPanelBasic;
import nc.block.tile.generator.BlockUraniumRTG;
import nc.block.tile.passive.BlockAcceleratorElectromagnet;
import nc.block.tile.passive.BlockActiveCooler;
import nc.block.tile.passive.BlockBuffer;
import nc.block.tile.passive.BlockCobblestoneGenerator;
import nc.block.tile.passive.BlockElectromagnetSupercooler;
import nc.block.tile.passive.BlockFusionElectromagnet;
import nc.block.tile.passive.BlockFusionElectromagnetTransparent;
import nc.block.tile.passive.BlockHeliumCollector;
import nc.block.tile.passive.BlockNitrogenCollector;
import nc.block.tile.passive.BlockWaterSource;
import nc.block.tile.processor.BlockAlloyFurnace;
import nc.block.tile.processor.BlockChemicalReactor;
import nc.block.tile.processor.BlockCrystallizer;
import nc.block.tile.processor.BlockDecayHastener;
import nc.block.tile.processor.BlockElectrolyser;
import nc.block.tile.processor.BlockFuelReprocessor;
import nc.block.tile.processor.BlockInfuser;
import nc.block.tile.processor.BlockIngotFormer;
import nc.block.tile.processor.BlockIrradiator;
import nc.block.tile.processor.BlockIsotopeSeparator;
import nc.block.tile.processor.BlockManufactory;
import nc.block.tile.processor.BlockMelter;
import nc.block.tile.processor.BlockNuclearFurnace;
import nc.block.tile.processor.BlockPressurizer;
import nc.block.tile.processor.BlockSaltMixer;
import nc.block.tile.processor.BlockSupercooler;
import nc.config.NCConfig;
import nc.handler.EnumHandler.CoolerTypes;
import nc.handler.EnumHandler.FissionBlockTypes;
import nc.handler.EnumHandler.IngotTypes;
import nc.handler.EnumHandler.OreTypes;
import nc.proxy.CommonProxy;
import nc.util.NCMath;
import nc.util.NCUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class NCBlocks {
	
	public static Block ore;
	public static Block ingot_block;
	
	public static Block fission_block;
	public static Block reactor_casing_transparent;
	public static Block cell_block;
	public static Block cooler;
	
	public static Block block_depleted_thorium;
	public static Block block_depleted_uranium;
	public static Block block_ice;
	
	public static Block nuclear_furnace_idle;
	public static Block nuclear_furnace_active;
	public static Block manufactory_idle;
	public static Block manufactory_active;
	public static Block isotope_separator_idle;
	public static Block isotope_separator_active;
	public static Block decay_hastener_idle;
	public static Block decay_hastener_active;
	public static Block fuel_reprocessor_idle;
	public static Block fuel_reprocessor_active;
	public static Block alloy_furnace_idle;
	public static Block alloy_furnace_active;
	public static Block infuser_idle;
	public static Block infuser_active;
	public static Block melter_idle;
	public static Block melter_active;
	public static Block supercooler_idle;
	public static Block supercooler_active;
	public static Block electrolyser_idle;
	public static Block electrolyser_active;
	public static Block irradiator_idle;
	public static Block irradiator_active;
	public static Block ingot_former_idle;
	public static Block ingot_former_active;
	public static Block pressurizer_idle;
	public static Block pressurizer_active;
	public static Block chemical_reactor_idle;
	public static Block chemical_reactor_active;
	public static Block salt_mixer_idle;
	public static Block salt_mixer_active;
	public static Block crystallizer_idle;
	public static Block crystallizer_active;
	
	public static Block machine_interface;
	
	public static Block fission_controller_idle;
	public static Block fission_controller_active;
	public static Block fission_port;
	
	public static Block fusion_core;
	public static Block fusion_dummy_side;
	public static Block fusion_dummy_top;
	public static Block fusion_connector;
	
	public static Block rtg_uranium;
	public static Block rtg_plutonium;
	public static Block rtg_americium;
	public static Block rtg_californium;
	
	public static Block solar_panel_basic;
	
	public static Block voltaic_pile_basic;
	public static Block lithium_ion_battery_basic;
	
	public static Block buffer;
	public static Block active_cooler;
	
	public static Block fusion_electromagnet_idle;
	public static Block fusion_electromagnet_active;
	public static Block fusion_electromagnet_transparent_idle;
	public static Block fusion_electromagnet_transparent_active;
	public static Block accelerator_electromagnet_idle;
	public static Block accelerator_electromagnet_active;
	public static Block electromagnet_supercooler_idle;
	public static Block electromagnet_supercooler_active;
	
	public static Block helium_collector;
	public static Block cobblestone_generator;
	public static Block water_source;
	public static Block nitrogen_collector;
	
	public static Block glowing_mushroom;
	
	//public static Block spin;
	
	public static void init() {
		ore = new BlockOre("ore", "ore").setCreativeTab(CommonProxy.TAB_BASE_BLOCK_MATERIALS);
		ingot_block = new BlockIngot("ingot_block", "ingot_block").setCreativeTab(CommonProxy.TAB_BASE_BLOCK_MATERIALS);
		
		fission_block = new BlockFission("fission_block", "fission_block").setCreativeTab(CommonProxy.TAB_FISSION_BLOCKS);
		reactor_casing_transparent = new BlockTransparent("reactor_casing_transparent", "reactor_casing_transparent", Material.IRON, true).setCreativeTab(CommonProxy.TAB_FISSION_BLOCKS);
		cell_block = new BlockTransparent("cell_block", "cell_block", Material.IRON, false).setCreativeTab(CommonProxy.TAB_FISSION_BLOCKS);
		cooler = new BlockCooler("cooler", "cooler").setCreativeTab(CommonProxy.TAB_FISSION_BLOCKS);
		
		block_depleted_thorium = new NCBlock("block_depleted_thorium", "block_depleted_thorium", Material.IRON).setCreativeTab(CommonProxy.TAB_BASE_BLOCK_MATERIALS);
		block_depleted_uranium = new NCBlock("block_depleted_uranium", "block_depleted_uranium", Material.IRON).setCreativeTab(CommonProxy.TAB_BASE_BLOCK_MATERIALS);
		block_ice = new BlockIce("block_ice", "block_ice");
		
		nuclear_furnace_idle = new BlockNuclearFurnace("nuclear_furnace_idle", "nuclear_furnace_idle", false, 0);
		nuclear_furnace_active = new BlockNuclearFurnace("nuclear_furnace_active", "nuclear_furnace_active", true, 0);
		manufactory_idle = new BlockManufactory("manufactory_idle", "manufactory_idle", false, 1);
		manufactory_active = new BlockManufactory("manufactory_active", "manufactory_active", true, 1);
		isotope_separator_idle = new BlockIsotopeSeparator("isotope_separator_idle", "isotope_separator_idle", false, 2);
		isotope_separator_active = new BlockIsotopeSeparator("isotope_separator_active", "isotope_separator_active", true, 2);
		decay_hastener_idle = new BlockDecayHastener("decay_hastener_idle", "decay_hastener_idle", false, 3);
		decay_hastener_active = new BlockDecayHastener("decay_hastener_active", "decay_hastener_active", true, 3);
		fuel_reprocessor_idle = new BlockFuelReprocessor("fuel_reprocessor_idle", "fuel_reprocessor_idle", false, 4);
		fuel_reprocessor_active = new BlockFuelReprocessor("fuel_reprocessor_active", "fuel_reprocessor_active", true, 4);
		alloy_furnace_idle = new BlockAlloyFurnace("alloy_furnace_idle", "alloy_furnace_idle", false, 5);
		alloy_furnace_active = new BlockAlloyFurnace("alloy_furnace_active", "alloy_furnace_active", true, 5);
		infuser_idle = new BlockInfuser("infuser_idle", "infuser_idle", false, 6);
		infuser_active = new BlockInfuser("infuser_active", "infuser_active", true, 6);
		melter_idle = new BlockMelter("melter_idle", "melter_idle", false, 7);
		melter_active = new BlockMelter("melter_active", "melter_active", true, 7);
		supercooler_idle = new BlockSupercooler("supercooler_idle", "supercooler_idle", false, 8);
		supercooler_active = new BlockSupercooler("supercooler_active", "supercooler_active", true, 8);
		electrolyser_idle = new BlockElectrolyser("electrolyser_idle", "electrolyser_idle", false, 9);
		electrolyser_active = new BlockElectrolyser("electrolyser_active", "electrolyser_active", true, 9);
		irradiator_idle = new BlockIrradiator("irradiator_idle", "irradiator_idle", false, 10);
		irradiator_active = new BlockIrradiator("irradiator_active", "irradiator_active", true, 10);
		ingot_former_idle = new BlockIngotFormer("ingot_former_idle", "ingot_former_idle", false, 11);
		ingot_former_active = new BlockIngotFormer("ingot_former_active", "ingot_former_active", true, 11);
		pressurizer_idle = new BlockPressurizer("pressurizer_idle", "pressurizer_idle", false, 12);
		pressurizer_active = new BlockPressurizer("pressurizer_active", "pressurizer_active", true, 12);
		chemical_reactor_idle = new BlockChemicalReactor("chemical_reactor_idle", "chemical_reactor_idle", false, 13);
		chemical_reactor_active = new BlockChemicalReactor("chemical_reactor_active", "chemical_reactor_active", true, 13);
		salt_mixer_idle = new BlockSaltMixer("salt_mixer_idle", "salt_mixer_idle", false, 14);
		salt_mixer_active = new BlockSaltMixer("salt_mixer_active", "salt_mixer_active", true, 14);
		crystallizer_idle = new BlockCrystallizer("crystallizer_idle", "crystallizer_idle", false, 15);
		crystallizer_active = new BlockCrystallizer("crystallizer_active", "crystallizer_active", true, 15);
		
		machine_interface = new BlockMachineInterface("machine_interface", "machine_interface");
		
		fission_controller_idle = new BlockFissionController("fission_controller_idle", "fission_controller_idle", false, 100);
		fission_controller_active = new BlockFissionController("fission_controller_active", "fission_controller_active", true, 100);
		fission_port = new BlockFissionPort("fission_port", "fission_port");
		
		fusion_core = new BlockFusionCore("fusion_core", "fusion_core", 101);
		fusion_dummy_side = new BlockFusionDummySide("fusion_dummy_side", "fusion_dummy_side");
		fusion_dummy_top = new BlockFusionDummyTop("fusion_dummy_top", "fusion_dummy_top");
		fusion_connector = new NCBlock("fusion_connector", "fusion_connector", Material.IRON).setCreativeTab(CommonProxy.TAB_FUSION);
		
		rtg_uranium = new BlockUraniumRTG("rtg_uranium", "rtg_uranium");
		rtg_plutonium = new BlockPlutoniumRTG("rtg_plutonium", "rtg_plutonium");
		rtg_americium = new BlockAmericiumRTG("rtg_americium", "rtg_americium");
		rtg_californium = new BlockCaliforniumRTG("rtg_californium", "rtg_californium");
		
		solar_panel_basic = new BlockSolarPanelBasic("solar_panel_basic", "solar_panel_basic");
		
		voltaic_pile_basic = new BlockVoltaicPileBasic("voltaic_pile_basic", "voltaic_pile_basic");
		lithium_ion_battery_basic = new BlockLithiumIonBatteryBasic("lithium_ion_battery_basic", "lithium_ion_battery_basic");
		
		buffer = new BlockBuffer("buffer", "buffer");
		active_cooler = new BlockActiveCooler("active_cooler", "active_cooler");
		
		fusion_electromagnet_idle = new BlockFusionElectromagnet("fusion_electromagnet_idle", "fusion_electromagnet_idle", false);
		fusion_electromagnet_active = new BlockFusionElectromagnet("fusion_electromagnet_active", "fusion_electromagnet_active", true);
		fusion_electromagnet_transparent_idle = new BlockFusionElectromagnetTransparent("fusion_electromagnet_transparent_idle", "fusion_electromagnet_transparent_idle", false);
		fusion_electromagnet_transparent_active = new BlockFusionElectromagnetTransparent("fusion_electromagnet_transparent_active", "fusion_electromagnet_transparent_active", true);
		accelerator_electromagnet_idle = new BlockAcceleratorElectromagnet("accelerator_electromagnet_idle", "accelerator_electromagnet_idle", false);
		accelerator_electromagnet_active = new BlockAcceleratorElectromagnet("accelerator_electromagnet_active", "accelerator_electromagnet_active", true);
		electromagnet_supercooler_idle = new BlockElectromagnetSupercooler("electromagnet_supercooler_idle", "electromagnet_supercooler_idle", false);
		electromagnet_supercooler_active = new BlockElectromagnetSupercooler("electromagnet_supercooler_active", "electromagnet_supercooler_active", true);
		
		helium_collector = new BlockHeliumCollector("helium_collector", "helium_collector");
		cobblestone_generator = new BlockCobblestoneGenerator("cobblestone_generator", "cobblestone_generator");
		water_source = new BlockWaterSource("water_source", "water_source");
		nitrogen_collector = new BlockNitrogenCollector("nitrogen_collector", "nitrogen_collector");
		
		glowing_mushroom = new BlockMushroom("glowing_mushroom", "glowing_mushroom");
		
		//spin = new BlockSpin("spin", "spin");
	}
	
	public static void register() {
		registerBlock(ore, new ItemBlockOre(ore));
		registerBlock(ingot_block, new ItemBlockIngot(ingot_block));
		
		registerBlock(fission_block, new ItemBlockFission(fission_block));
		registerBlock(reactor_casing_transparent, 17);
		registerBlock(cell_block, 11);
		registerBlock(cooler, new ItemBlockCooler(cooler));
		
		registerBlock(block_depleted_thorium);
		registerBlock(block_depleted_uranium);
		registerBlock(block_ice);
		
		registerBlock(nuclear_furnace_idle, 2);
		registerBlock(nuclear_furnace_active);
		registerBlock(manufactory_idle, 2);
		registerBlock(manufactory_active);
		registerBlock(isotope_separator_idle, 2);
		registerBlock(isotope_separator_active);
		registerBlock(decay_hastener_idle, 2);
		registerBlock(decay_hastener_active);
		registerBlock(fuel_reprocessor_idle, 2);
		registerBlock(fuel_reprocessor_active);
		registerBlock(alloy_furnace_idle, 2);
		registerBlock(alloy_furnace_active);
		registerBlock(infuser_idle, 2);
		registerBlock(infuser_active);
		registerBlock(melter_idle, 2);
		registerBlock(melter_active);
		registerBlock(supercooler_idle, 2);
		registerBlock(supercooler_active);
		registerBlock(electrolyser_idle, 2);
		registerBlock(electrolyser_active);
		registerBlock(irradiator_idle, 2);
		registerBlock(irradiator_active);
		registerBlock(ingot_former_idle, 2);
		registerBlock(ingot_former_active);
		registerBlock(pressurizer_idle, 2);
		registerBlock(pressurizer_active);
		registerBlock(chemical_reactor_idle, 2);
		registerBlock(chemical_reactor_active);
		registerBlock(salt_mixer_idle, 2);
		registerBlock(salt_mixer_active);
		registerBlock(crystallizer_idle, 2);
		registerBlock(crystallizer_active);
		
		registerBlock(machine_interface, 4);
		
		registerBlock(fission_controller_idle, 9);
		registerBlock(fission_controller_active);
		registerBlock(fission_port, 15);
		
		registerBlock(fusion_core, 15);
		registerBlock(fusion_dummy_side);
		registerBlock(fusion_dummy_top);
		registerBlock(fusion_connector, 2);
		
		registerBlock(rtg_uranium, I18n.translateToLocalFormatted("tile.rtg.des0") + " " + NCConfig.rtg_power[0] + " " + I18n.translateToLocalFormatted("tile.rtg.des1"));
		registerBlock(rtg_plutonium, I18n.translateToLocalFormatted("tile.rtg.des0") + " " + NCConfig.rtg_power[1] + " " + I18n.translateToLocalFormatted("tile.rtg.des1"));
		registerBlock(rtg_americium, I18n.translateToLocalFormatted("tile.rtg.des0") + " " + NCConfig.rtg_power[2] + " " + I18n.translateToLocalFormatted("tile.rtg.des1"));
		registerBlock(rtg_californium, I18n.translateToLocalFormatted("tile.rtg.des0") + " " + NCConfig.rtg_power[3] + " " + I18n.translateToLocalFormatted("tile.rtg.des1"));
		
		registerBlock(solar_panel_basic, I18n.translateToLocalFormatted("tile.solar_panel.des0") + " " + NCConfig.solar_power[0] + " " + I18n.translateToLocalFormatted("tile.solar_panel.des1"));
		
		registerBlock(voltaic_pile_basic, I18n.translateToLocalFormatted("tile.energy_storage.des0") + " " + NCConfig.battery_capacity[0]/1000 + " " + I18n.translateToLocalFormatted("tile.energy_storage.des1"), I18n.translateToLocalFormatted("tile.energy_storage.des2"), I18n.translateToLocalFormatted("tile.energy_storage.des3"), I18n.translateToLocalFormatted("tile.energy_storage.des4"), I18n.translateToLocalFormatted("tile.energy_storage.des5"));
		registerBlock(lithium_ion_battery_basic, I18n.translateToLocalFormatted("tile.energy_storage.des0") + " " + NCConfig.battery_capacity[1]/1000 + " " + I18n.translateToLocalFormatted("tile.energy_storage.des1"), I18n.translateToLocalFormatted("tile.energy_storage.des2"), I18n.translateToLocalFormatted("tile.energy_storage.des3"), I18n.translateToLocalFormatted("tile.energy_storage.des4"), I18n.translateToLocalFormatted("tile.energy_storage.des5"));
		
		registerBlock(buffer, 4);
		registerBlock(active_cooler, 16);
		
		registerBlock(fusion_electromagnet_idle, I18n.translateToLocalFormatted("tile.fusion_electromagnet_idle.des0") + " " + NCMath.Round(0.05D*NCConfig.fusion_electromagnet_power, 1) + " " + I18n.translateToLocalFormatted("tile.fusion_electromagnet_idle.des1"));
		registerBlock(fusion_electromagnet_active, I18n.translateToLocalFormatted("tile.fusion_electromagnet_idle.des0") + " " + NCMath.Round(0.05D*NCConfig.fusion_electromagnet_power, 1) + " " + I18n.translateToLocalFormatted("tile.fusion_electromagnet_idle.des1"));
		registerBlock(fusion_electromagnet_transparent_idle, I18n.translateToLocalFormatted("tile.fusion_electromagnet_idle.des0") + " " + NCMath.Round(0.05D*NCConfig.fusion_electromagnet_power, 1) + " " + I18n.translateToLocalFormatted("tile.fusion_electromagnet_idle.des1"));
		registerBlock(fusion_electromagnet_transparent_active, I18n.translateToLocalFormatted("tile.fusion_electromagnet_idle.des0") + " " + NCMath.Round(0.05D*NCConfig.fusion_electromagnet_power, 1) + " " + I18n.translateToLocalFormatted("tile.fusion_electromagnet_idle.des1"));
		registerBlock(accelerator_electromagnet_idle, I18n.translateToLocalFormatted("tile.accelerator_electromagnet_idle.des0") + " " + NCMath.Round(0.05D*NCConfig.accelerator_electromagnet_power, 1) + " " + I18n.translateToLocalFormatted("tile.accelerator_electromagnet_idle.des1"));
		registerBlock(accelerator_electromagnet_active, I18n.translateToLocalFormatted("tile.accelerator_electromagnet_idle.des0") + " " + NCMath.Round(0.05D*NCConfig.accelerator_electromagnet_power, 1) + " " + I18n.translateToLocalFormatted("tile.accelerator_electromagnet_idle.des1"));
		registerBlock(electromagnet_supercooler_idle, I18n.translateToLocalFormatted("tile.electromagnet_supercooler_idle.des0") + " " + NCMath.Round(0.05D*NCConfig.accelerator_electromagnet_power, 1) + " " + I18n.translateToLocalFormatted("tile.electromagnet_supercooler_idle.des1") + " " + NCMath.Round(0.05D*NCConfig.accelerator_supercooler_coolant, 1) + " " + I18n.translateToLocalFormatted("tile.electromagnet_supercooler_idle.des2"), I18n.translateToLocalFormatted("tile.electromagnet_supercooler_idle.des3"));
		registerBlock(electromagnet_supercooler_active, I18n.translateToLocalFormatted("tile.electromagnet_supercooler_idle.des0") + " " + NCMath.Round(0.05D*NCConfig.accelerator_electromagnet_power, 1) + " " + I18n.translateToLocalFormatted("tile.electromagnet_supercooler_idle.des1") + " " + NCMath.Round(0.05D*NCConfig.accelerator_supercooler_coolant, 1) + " " + I18n.translateToLocalFormatted("tile.electromagnet_supercooler_idle.des2"), I18n.translateToLocalFormatted("tile.electromagnet_supercooler_idle.des3"));
		
		registerBlock(helium_collector, I18n.translateToLocalFormatted("tile.helium_collector.des0") + " " + NCMath.Round(0.05D*NCConfig.processor_passive_rate[0], 1) + " " + I18n.translateToLocalFormatted("tile.helium_collector.des1"));
		registerBlock(cobblestone_generator, I18n.translateToLocalFormatted("tile.cobblestone_generator.des0") + " " + NCMath.Round(0.025D*NCConfig.processor_passive_rate[1], 1) + " " + I18n.translateToLocalFormatted("tile.cobblestone_generator.des1"));
		registerBlock(water_source, I18n.translateToLocalFormatted("tile.water_source.des0") + " " + NCMath.Round(0.05D*NCConfig.processor_passive_rate[2], 1) + " " + I18n.translateToLocalFormatted("tile.water_source.des1"));
		registerBlock(nitrogen_collector, I18n.translateToLocalFormatted("tile.nitrogen_collector.des0") + " " + NCMath.Round(0.05D*NCConfig.processor_passive_rate[3], 1) + " " + I18n.translateToLocalFormatted("tile.nitrogen_collector.des1"));
		
		registerBlock(glowing_mushroom);
		
		//registerBlock(spin);
	}
	
	public static void registerRenders() {
		for (int i = 0; i < OreTypes.values().length; i++) {
			registerRender(ore, i, "ore_" + OreTypes.values()[i].getName());
		}
		
		for (int i = 0; i < IngotTypes.values().length; i++) {
			registerRender(ingot_block, i, "ingot_block_" + IngotTypes.values()[i].getName());
		}
		
		for (int i = 0; i < FissionBlockTypes.values().length; i++) {
			registerRender(fission_block, i, "fission_block_" + FissionBlockTypes.values()[i].getName());
		}
		
		registerRender(reactor_casing_transparent);
		registerRender(cell_block);
		
		for (int i = 0; i < CoolerTypes.values().length; i++) {
			registerRender(cooler, i, "cooler_" + CoolerTypes.values()[i].getName());
		}
		
		registerRender(block_depleted_thorium);
		registerRender(block_depleted_uranium);
		registerRender(block_ice);
		
		registerRender(nuclear_furnace_idle);
		registerRender(nuclear_furnace_active);
		registerRender(manufactory_idle);
		registerRender(manufactory_active);
		registerRender(isotope_separator_idle);
		registerRender(isotope_separator_active);
		registerRender(decay_hastener_idle);
		registerRender(decay_hastener_active);
		registerRender(fuel_reprocessor_idle);
		registerRender(fuel_reprocessor_active);
		registerRender(alloy_furnace_idle);
		registerRender(alloy_furnace_active);
		registerRender(infuser_idle);
		registerRender(infuser_active);
		registerRender(melter_idle);
		registerRender(melter_active);
		registerRender(supercooler_idle);
		registerRender(supercooler_active);
		registerRender(electrolyser_idle);
		registerRender(electrolyser_active);
		registerRender(irradiator_idle);
		registerRender(irradiator_active);
		registerRender(ingot_former_idle);
		registerRender(ingot_former_active);
		registerRender(pressurizer_idle);
		registerRender(pressurizer_active);
		registerRender(chemical_reactor_idle);
		registerRender(chemical_reactor_active);
		registerRender(salt_mixer_idle);
		registerRender(salt_mixer_active);
		registerRender(crystallizer_idle);
		registerRender(crystallizer_active);
		
		registerRender(machine_interface);
		
		registerRender(fission_controller_idle);
		registerRender(fission_controller_active);
		registerRender(fission_port);
		
		registerRender(fusion_core);
		registerRender(fusion_dummy_side);
		registerRender(fusion_dummy_top);
		registerRender(fusion_connector);
		
		registerRender(rtg_uranium);
		registerRender(rtg_plutonium);
		registerRender(rtg_americium);
		registerRender(rtg_californium);
		
		registerRender(solar_panel_basic);
		
		registerRender(voltaic_pile_basic);
		registerRender(lithium_ion_battery_basic);
		
		registerRender(buffer);
		registerRender(active_cooler);
		
		registerRender(fusion_electromagnet_idle);
		registerRender(fusion_electromagnet_active);
		registerRender(fusion_electromagnet_transparent_idle);
		registerRender(fusion_electromagnet_transparent_active);
		registerRender(accelerator_electromagnet_idle);
		registerRender(accelerator_electromagnet_active);
		registerRender(electromagnet_supercooler_idle);
		registerRender(electromagnet_supercooler_active);
		
		registerRender(helium_collector);
		registerRender(cobblestone_generator);
		registerRender(water_source);
		registerRender(nitrogen_collector);
		
		registerRender(glowing_mushroom);
		
		//registerRender(spin);
	}
	
	public static void registerBlock(Block block, Object... info) {
		GameRegistry.register(block);
		GameRegistry.register(new NCItemBlock(block, info).setRegistryName(block.getRegistryName()));
		NCUtil.getLogger().info("Registered block " + block.getUnlocalizedName().substring(5));
	}
	
	public static void registerBlock(Block block, ItemBlock itemBlock) {
		GameRegistry.register(block);
		GameRegistry.register(itemBlock.setRegistryName(block.getRegistryName()));
		NCUtil.getLogger().info("Registered block " + block.getUnlocalizedName().substring(5));
	}
	
	public static void registerRender(Block block) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(new ResourceLocation(Global.MOD_ID, block.getUnlocalizedName().substring(5)), "inventory"));
		NCUtil.getLogger().info("Registered render for block " + block.getUnlocalizedName().substring(5));
	}
	
	public static void registerRender(Block block, int meta, String fileName) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), meta, new ModelResourceLocation(new ResourceLocation(Global.MOD_ID, fileName), "inventory"));
		NCUtil.getLogger().info("Registered render for block " + fileName);
	}
}
