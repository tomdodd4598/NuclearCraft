package nc.enumm;

import nc.init.NCBlocks;
import nc.tab.NCTabs;
import nc.tile.TileBin;
import nc.tile.dummy.TileMachineInterface;
import nc.tile.generator.TileDecayGenerator;
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
		SEPARATOR("separator", 2, "reddust", "smoke"),
		DECAY_HASTENER("decay_hastener", 3, "reddust", "reddust"),
		FUEL_REPROCESSOR("fuel_reprocessor", 4, "reddust", "smoke"),
		ALLOY_FURNACE("alloy_furnace", 5, "smoke", "reddust"),
		INFUSER("infuser", 6, "portal", "reddust"),
		MELTER("melter", 7, "flame", "lava"),
		SUPERCOOLER("supercooler", 8, "snowshovel", "smoke"),
		ELECTROLYZER("electrolyzer", 9, "reddust", "splash"),
		ASSEMBLER("assembler", 10, "smoke", "crit"),
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
			case SEPARATOR:
				return new TileProcessor.Separator();
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
			case ASSEMBLER:
				return new TileProcessor.Assembler();
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
			case SEPARATOR:
				return NCBlocks.separator;
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
			case ASSEMBLER:
				return NCBlocks.assembler;
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
				return NCTabs.MACHINE;
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
		MACHINE_INTERFACE("machine_interface", NCTabs.MACHINE),
		DECAY_GENERATOR("decay_generator", NCTabs.MACHINE),
		BIN("bin", NCTabs.MACHINE),
		
		SOLAR_PANEL_BASIC("solar_panel_basic", NCTabs.MACHINE),
		SOLAR_PANEL_ADVANCED("solar_panel_advanced", NCTabs.MACHINE),
		SOLAR_PANEL_DU("solar_panel_du", NCTabs.MACHINE),
		SOLAR_PANEL_ELITE("solar_panel_elite", NCTabs.MACHINE),
		
		COBBLESTONE_GENERATOR("cobblestone_generator", NCTabs.MACHINE),
		COBBLESTONE_GENERATOR_COMPACT("cobblestone_generator_compact", NCTabs.MACHINE),
		COBBLESTONE_GENERATOR_DENSE("cobblestone_generator_dense", NCTabs.MACHINE),
		
		WATER_SOURCE("water_source", NCTabs.MACHINE),
		WATER_SOURCE_COMPACT("water_source_compact", NCTabs.MACHINE),
		WATER_SOURCE_DENSE("water_source_dense", NCTabs.MACHINE),
		
		NITROGEN_COLLECTOR("nitrogen_collector", NCTabs.MACHINE),
		NITROGEN_COLLECTOR_COMPACT("nitrogen_collector_compact", NCTabs.MACHINE),
		NITROGEN_COLLECTOR_DENSE("nitrogen_collector_dense", NCTabs.MACHINE),
		
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
			
			case SOLAR_PANEL_BASIC:
				return new TileSolarPanel.Basic();
			case SOLAR_PANEL_ADVANCED:
				return new TileSolarPanel.Advanced();
			case SOLAR_PANEL_DU:
				return new TileSolarPanel.DU();
			case SOLAR_PANEL_ELITE:
				return new TileSolarPanel.Elite();
				
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
