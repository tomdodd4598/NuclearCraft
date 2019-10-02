package nc.enumm;

import nc.init.NCBlocks;
import nc.tab.NCTabs;
import nc.tile.TileBin;
import nc.tile.dummy.TileMachineInterface;
import nc.tile.energy.battery.TileBattery;
import nc.tile.generator.TileDecayGenerator;
import nc.tile.generator.TileRTG;
import nc.tile.generator.TileSolarPanel;
import nc.tile.passive.TilePassive;
import nc.tile.processor.TileProcessor;
import nc.tile.radiation.TileGeigerCounter;
import nc.tile.radiation.TileRadiationScrubber;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.SoundEvent;

public class BlockEnums {
	
	public enum ProcessorType implements IStringSerializable {
		MANUFACTORY("manufactory", 1, "reddust", "crit"),
		ISOTOPE_SEPARATOR("isotope_separator", 2, "reddust", "smoke"),
		DECAY_HASTENER("decay_hastener", 3, "reddust", "reddust"),
		FUEL_REPROCESSOR("fuel_reprocessor", 4, "reddust", "smoke"),
		ALLOY_FURNACE("alloy_furnace", 5, "smoke", "reddust"),
		INFUSER("infuser", 6, "portal", "reddust"),
		MELTER("melter", 7, "flame", "lava"),
		SUPERCOOLER("supercooler", 8, "snowshovel", "smoke"),
		ELECTROLYZER("electrolyzer", 9, "reddust", "splash"),
		IRRADIATOR("irradiator", 10, "portal", "enchantmenttable"),
		INGOT_FORMER("ingot_former", 11, "smoke", "smoke"),
		PRESSURIZER("pressurizer", 12, "smoke", "smoke"),
		CHEMICAL_REACTOR("chemical_reactor", 13, "reddust", "reddust"),
		SALT_MIXER("salt_mixer", 14, "reddust", "endRod"),
		CRYSTALLIZER("crystallizer", 15, "depthsuspend", "depthsuspend"),
		ENRICHER("enricher", 16, "splash", "depthsuspend"),
		EXTRACTOR("extractor", 17, "reddust", "depthsuspend"),
		CENTRIFUGE("centrifuge", 18, "endRod", "depthsuspend"),
		ROCK_CRUSHER("rock_crusher", 19, "smoke", "smoke");
		
		private String name;
		private int id;
		private String particle1;
		private String particle2;
		
		private ProcessorType(String name, int id, String particle1, String particle2) {
			this.name = name;
			this.id = id;
			this.particle1 = particle1;
			this.particle2 = particle2;
		}
		
		@Override
		public String getName() {
			return name;
		}
		
		public int getID() {
			return id;
		}
		
		public TileEntity getTile() {
			switch (this) {
			case MANUFACTORY:
				return new TileProcessor.Manufactory();
			case ISOTOPE_SEPARATOR:
				return new TileProcessor.IsotopeSeparator();
			case DECAY_HASTENER:
				return new TileProcessor.DecayHastener();
			case FUEL_REPROCESSOR:
				return new TileProcessor.FuelReprocessor();
			case ALLOY_FURNACE:
				return new TileProcessor.AlloyFurnace();
			case INFUSER:
				return new TileProcessor.Infuser();
			case MELTER:
				return new TileProcessor.Melter();
			case SUPERCOOLER:
				return new TileProcessor.Supercooler();
			case ELECTROLYZER:
				return new TileProcessor.Electrolyzer();
			case IRRADIATOR:
				return new TileProcessor.Irradiator();
			case INGOT_FORMER:
				return new TileProcessor.IngotFormer();
			case PRESSURIZER:
				return new TileProcessor.Pressurizer();
			case CHEMICAL_REACTOR:
				return new TileProcessor.ChemicalReactor();
			case SALT_MIXER:
				return new TileProcessor.SaltMixer();
			case CRYSTALLIZER:
				return new TileProcessor.Crystallizer();
			case ENRICHER:
				return new TileProcessor.Enricher();
			case EXTRACTOR:
				return new TileProcessor.Extractor();
			case CENTRIFUGE:
				return new TileProcessor.Centrifuge();
			case ROCK_CRUSHER:
				return new TileProcessor.RockCrusher();

			default:
				return null;
			}
		}
		
		public Block getBlock() {
			switch (this) {
			case MANUFACTORY:
				return NCBlocks.manufactory;
			case ISOTOPE_SEPARATOR:
				return NCBlocks.isotope_separator;
			case DECAY_HASTENER:
				return NCBlocks.decay_hastener;
			case FUEL_REPROCESSOR:
				return NCBlocks.fuel_reprocessor;
			case ALLOY_FURNACE:
				return NCBlocks.alloy_furnace;
			case INFUSER:
				return NCBlocks.infuser;
			case MELTER:
				return NCBlocks.melter;
			case SUPERCOOLER:
				return NCBlocks.supercooler;
			case ELECTROLYZER:
				return NCBlocks.electrolyzer;
			case IRRADIATOR:
				return NCBlocks.irradiator;
			case INGOT_FORMER:
				return NCBlocks.ingot_former;
			case PRESSURIZER:
				return NCBlocks.pressurizer;
			case CHEMICAL_REACTOR:
				return NCBlocks.chemical_reactor;
			case SALT_MIXER:
				return NCBlocks.salt_mixer;
			case CRYSTALLIZER:
				return NCBlocks.crystallizer;
			case ENRICHER:
				return NCBlocks.enricher;
			case EXTRACTOR:
				return NCBlocks.extractor;
			case CENTRIFUGE:
				return NCBlocks.centrifuge;
			case ROCK_CRUSHER:
				return NCBlocks.rock_crusher;
			default:
				return NCBlocks.manufactory;
			}
		}
		
		public CreativeTabs getCreativeTab() {
			switch (this) {
			default:
				return NCTabs.MACHINES;
			}
		}
		
		@SuppressWarnings("static-method")
		public SoundEvent getSound() {
			return null;
		}
		
		public String getParticle1() {
			return particle1;
		}
		
		public String getParticle2() {
			return particle2;
		}
	}
	
	public enum SimpleTileType implements IStringSerializable {
		MACHINE_INTERFACE("machine_interface", NCTabs.MACHINES),
		DECAY_GENERATOR("decay_generator", NCTabs.MACHINES),
		BIN("bin", NCTabs.MACHINES),
		
		RTG_URANIUM("rtg_uranium", NCTabs.MACHINES),
		RTG_PLUTONIUM("rtg_plutonium", NCTabs.MACHINES),
		RTG_AMERICIUM("rtg_americium", NCTabs.MACHINES),
		RTG_CALIFORNIUM("rtg_californium", NCTabs.MACHINES),
		
		SOLAR_PANEL_BASIC("solar_panel_basic", NCTabs.MACHINES),
		SOLAR_PANEL_ADVANCED("solar_panel_advanced", NCTabs.MACHINES),
		SOLAR_PANEL_DU("solar_panel_du", NCTabs.MACHINES),
		SOLAR_PANEL_ELITE("solar_panel_elite", NCTabs.MACHINES),
		
		VOLTAIC_PILE_BASIC("voltaic_pile_basic", NCTabs.MACHINES),
		VOLTAIC_PILE_ADVANCED("voltaic_pile_advanced", NCTabs.MACHINES),
		VOLTAIC_PILE_DU("voltaic_pile_du", NCTabs.MACHINES),
		VOLTAIC_PILE_ELITE("voltaic_pile_elite", NCTabs.MACHINES),
		
		LITHIUM_ION_BATTERY_BASIC("lithium_ion_battery_basic", NCTabs.MACHINES),
		LITHIUM_ION_BATTERY_ADVANCED("lithium_ion_battery_advanced", NCTabs.MACHINES),
		LITHIUM_ION_BATTERY_DU("lithium_ion_battery_du", NCTabs.MACHINES),
		LITHIUM_ION_BATTERY_ELITE("lithium_ion_battery_elite", NCTabs.MACHINES),
		
		COBBLESTONE_GENERATOR("cobblestone_generator", NCTabs.MACHINES),
		COBBLESTONE_GENERATOR_COMPACT("cobblestone_generator_compact", NCTabs.MACHINES),
		COBBLESTONE_GENERATOR_DENSE("cobblestone_generator_dense", NCTabs.MACHINES),
		
		WATER_SOURCE("water_source", NCTabs.MACHINES),
		WATER_SOURCE_COMPACT("water_source_compact", NCTabs.MACHINES),
		WATER_SOURCE_DENSE("water_source_dense", NCTabs.MACHINES),
		
		NITROGEN_COLLECTOR("nitrogen_collector", NCTabs.MACHINES),
		NITROGEN_COLLECTOR_COMPACT("nitrogen_collector_compact", NCTabs.MACHINES),
		NITROGEN_COLLECTOR_DENSE("nitrogen_collector_dense", NCTabs.MACHINES),
		
		RADIATION_SCRUBBER("radiation_scrubber", NCTabs.RADIATION),
		
		GEIGER_BLOCK("geiger_block", NCTabs.RADIATION);
		
		private String name;
		private CreativeTabs tab;
		
		private SimpleTileType(String name, CreativeTabs tab) {
			this.name = name;
			this.tab = tab;
		}
		
		@Override
		public String getName() {
			return name;
		}
		
		public TileEntity getTile() {
			switch (this) {
			case MACHINE_INTERFACE:
				return new TileMachineInterface();
			case DECAY_GENERATOR:
				return new TileDecayGenerator();
			case BIN:
				return new TileBin();
			
			case RTG_URANIUM:
				return new TileRTG.Uranium();
			case RTG_PLUTONIUM:
				return new TileRTG.Plutonium();
			case RTG_AMERICIUM:
				return new TileRTG.Americium();
			case RTG_CALIFORNIUM:
				return new TileRTG.Californium();
			
			case SOLAR_PANEL_BASIC:
				return new TileSolarPanel.Basic();
			case SOLAR_PANEL_ADVANCED:
				return new TileSolarPanel.Advanced();
			case SOLAR_PANEL_DU:
				return new TileSolarPanel.DU();
			case SOLAR_PANEL_ELITE:
				return new TileSolarPanel.Elite();
				
			case VOLTAIC_PILE_BASIC:
				return new TileBattery.VoltaicPileBasic();
			case VOLTAIC_PILE_ADVANCED:
				return new TileBattery.VoltaicPileAdvanced();
			case VOLTAIC_PILE_DU:
				return new TileBattery.VoltaicPileDU();
			case VOLTAIC_PILE_ELITE:
				return new TileBattery.VoltaicPileElite();
				
			case LITHIUM_ION_BATTERY_BASIC:
				return new TileBattery.LithiumIonBatteryBasic();
			case LITHIUM_ION_BATTERY_ADVANCED:
				return new TileBattery.LithiumIonBatteryAdvanced();
			case LITHIUM_ION_BATTERY_DU:
				return new TileBattery.LithiumIonBatteryDU();
			case LITHIUM_ION_BATTERY_ELITE:
				return new TileBattery.LithiumIonBatteryElite();
				
			case COBBLESTONE_GENERATOR:
				return new TilePassive.CobblestoneGenerator();
			case COBBLESTONE_GENERATOR_COMPACT:
				return new TilePassive.CobblestoneGeneratorCompact();
			case COBBLESTONE_GENERATOR_DENSE:
				return new TilePassive.CobblestoneGeneratorDense();
				
			case WATER_SOURCE:
				return new TilePassive.WaterSource();
			case WATER_SOURCE_COMPACT:
				return new TilePassive.WaterSourceCompact();
			case WATER_SOURCE_DENSE:
				return new TilePassive.WaterSourceDense();
				
			case NITROGEN_COLLECTOR:
				return new TilePassive.NitrogenCollector();
			case NITROGEN_COLLECTOR_COMPACT:
				return new TilePassive.NitrogenCollectorCompact();
			case NITROGEN_COLLECTOR_DENSE:
				return new TilePassive.NitrogenCollectorDense();
				
			case RADIATION_SCRUBBER:
				return new TileRadiationScrubber();
			
			case GEIGER_BLOCK:
				return new TileGeigerCounter();
			
			default:
				return null;
			}
		}
		
		public CreativeTabs getCreativeTab() {
			return tab;
		}
	}
	
	public enum ActivatableTileType implements IStringSerializable {
		/*FUSION_ELECTROMAGNET("fusion_electromagnet", NCTabs.FUSION),
		FUSION_ELECTROMAGNET_TRANSPARENT("fusion_electromagnet_transparent", NCTabs.FUSION),
		ACCELERATOR_ELECTROMAGNET("accelerator_electromagnet", NCTabs.ACCELERATOR),
		ELECTROMAGNET_SUPERCOOLER("electromagnet_supercooler", NCTabs.ACCELERATOR)*/;
		
		private String name;
		private CreativeTabs tab;
		
		private ActivatableTileType(String name, CreativeTabs tab) {
			this.name = name;
			this.tab = tab;
		}
		
		@Override
		public String getName() {
			return name;
		}
		
		public TileEntity getTile() {
			switch (this) {
			/*case FUSION_ELECTROMAGNET:
				return new TilePassive.FusionElectromagnet();
			case FUSION_ELECTROMAGNET_TRANSPARENT:
				return new TilePassive.FusionElectromagnet();
			case ACCELERATOR_ELECTROMAGNET:
				return new TilePassive.AcceleratorElectromagnet();
			case ELECTROMAGNET_SUPERCOOLER:
				return new TilePassive.ElectromagnetSupercooler();*/
			default:
				return null;
			}
		}
		
		public CreativeTabs getCreativeTab() {
			return tab;
		}
		
		public Block getBlock() {
			switch (this) {
			/*case FUSION_ELECTROMAGNET:
				return NCBlocks.fusion_electromagnet;
			case FUSION_ELECTROMAGNET_TRANSPARENT:
				return NCBlocks.fusion_electromagnet_transparent;
			case ACCELERATOR_ELECTROMAGNET:
				return NCBlocks.accelerator_electromagnet;
			case ELECTROMAGNET_SUPERCOOLER:
				return NCBlocks.electromagnet_supercooler;*/
			default:
				return /*NCBlocks.fusion_electromagnet*/ null;
			}
		}
	}
}
