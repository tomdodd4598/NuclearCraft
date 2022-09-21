package nc.tile.processor;

import static nc.config.NCConfig.*;

import nc.recipe.NCRecipes;

public class TileBasicProcessor {
	
	public static class Manufactory extends TileBasicUpgradeProcessor<Manufactory> {
		
		public Manufactory() {
			super("manufactory", 1, 1, defaultItemSorptions(1, 1, true), processor_time_multiplier * processor_time[0], processor_power_multiplier * processor_power[0], false, NCRecipes.manufactory, 1, 0, 0);
		}
	}
	
	public static class Separator extends TileBasicUpgradeProcessor<Separator> {
		
		public Separator() {
			super("separator", 1, 2, defaultItemSorptions(1, 2, true), processor_time_multiplier * processor_time[1], processor_power_multiplier * processor_power[1], false, NCRecipes.separator, 2, 0, 0);
		}
	}
	
	public static class DecayHastener extends TileBasicUpgradeProcessor<DecayHastener> {
		
		public DecayHastener() {
			super("decay_hastener", 1, 1, defaultItemSorptions(1, 1, true), processor_time_multiplier * processor_time[2], processor_power_multiplier * processor_power[2], false, NCRecipes.decay_hastener, 3, 0, 0);
		}
	}
	
	public static class FuelReprocessor extends TileBasicUpgradeProcessor<FuelReprocessor> {
		
		public FuelReprocessor() {
			super("fuel_reprocessor", 1, 8, defaultItemSorptions(1, 8, true), processor_time_multiplier * processor_time[3], processor_power_multiplier * processor_power[3], false, NCRecipes.fuel_reprocessor, 4, 0, 12);
		}
	}
	
	public static class AlloyFurnace extends TileBasicUpgradeProcessor<AlloyFurnace> {
		
		public AlloyFurnace() {
			super("alloy_furnace", 2, 1, defaultItemSorptions(2, 1, true), processor_time_multiplier * processor_time[4], processor_power_multiplier * processor_power[4], true, NCRecipes.alloy_furnace, 5, 0, 0);
		}
	}
	
	public static class Infuser extends TileBasicUpgradeProcessor<Infuser> {
		
		public Infuser() {
			super("infuser", 1, 1, 1, 0, defaultItemSorptions(1, 1, true), defaultTankCapacities(16000, 1, 0), defaultTankSorptions(1, 0), NCRecipes.infuser_valid_fluids, processor_time_multiplier * processor_time[5], processor_power_multiplier * processor_power[5], true, NCRecipes.infuser, 6, 0, 0);
		}
	}
	
	public static class Melter extends TileBasicUpgradeProcessor<Melter> {
		
		public Melter() {
			super("melter", 1, 0, 0, 1, defaultItemSorptions(1, 0, true), defaultTankCapacities(16000, 0, 1), defaultTankSorptions(0, 1), NCRecipes.melter_valid_fluids, processor_time_multiplier * processor_time[6], processor_power_multiplier * processor_power[6], true, NCRecipes.melter, 7, 0, 0);
		}
	}
	
	public static class Supercooler extends TileBasicUpgradeProcessor<Supercooler> {
		
		public Supercooler() {
			super("supercooler", 1, 1, defaultItemSorptions(true), defaultTankCapacities(16000, 1, 1), defaultTankSorptions(1, 1), NCRecipes.supercooler_valid_fluids, processor_time_multiplier * processor_time[7], processor_power_multiplier * processor_power[7], true, NCRecipes.supercooler, 8, 0, 0);
		}
	}
	
	public static class Electrolyzer extends TileBasicUpgradeProcessor<Electrolyzer> {
		
		public Electrolyzer() {
			super("electrolyzer", 1, 4, defaultItemSorptions(true), defaultTankCapacities(16000, 1, 4), defaultTankSorptions(1, 4), NCRecipes.electrolyzer_valid_fluids, processor_time_multiplier * processor_time[8], processor_power_multiplier * processor_power[8], true, NCRecipes.electrolyzer, 9, 0, 12);
		}
	}
	
	public static class Assembler extends TileBasicUpgradeProcessor<Assembler> {
		
		public Assembler() {
			super("assembler", 4, 1, defaultItemSorptions(4, 1, true), processor_time_multiplier * processor_time[9], processor_power_multiplier * processor_power[9], false, NCRecipes.assembler, 10, 0, 12);
		}
	}
	
	public static class IngotFormer extends TileBasicUpgradeProcessor<IngotFormer> {
		
		public IngotFormer() {
			super("ingot_former", 0, 1, 1, 0, defaultItemSorptions(0, 1, true), defaultTankCapacities(16000, 1, 0), defaultTankSorptions(1, 0), NCRecipes.ingot_former_valid_fluids, processor_time_multiplier * processor_time[10], processor_power_multiplier * processor_power[10], false, NCRecipes.ingot_former, 11, 0, 0);
		}
	}
	
	public static class Pressurizer extends TileBasicUpgradeProcessor<Pressurizer> {
		
		public Pressurizer() {
			super("pressurizer", 1, 1, defaultItemSorptions(1, 1, true), processor_time_multiplier * processor_time[11], processor_power_multiplier * processor_power[11], true, NCRecipes.pressurizer, 12, 0, 0);
		}
	}
	
	public static class ChemicalReactor extends TileBasicUpgradeProcessor<ChemicalReactor> {
		
		public ChemicalReactor() {
			super("chemical_reactor", 2, 2, defaultItemSorptions(true), defaultTankCapacities(16000, 2, 2), defaultTankSorptions(2, 2), NCRecipes.chemical_reactor_valid_fluids, processor_time_multiplier * processor_time[12], processor_power_multiplier * processor_power[12], true, NCRecipes.chemical_reactor, 13, 0, 0);
		}
	}
	
	public static class SaltMixer extends TileBasicUpgradeProcessor<SaltMixer> {
		
		public SaltMixer() {
			super("salt_mixer", 2, 1, defaultItemSorptions(true), defaultTankCapacities(16000, 2, 1), defaultTankSorptions(2, 1), NCRecipes.salt_mixer_valid_fluids, processor_time_multiplier * processor_time[13], processor_power_multiplier * processor_power[13], false, NCRecipes.salt_mixer, 14, 0, 0);
		}
	}
	
	public static class Crystallizer extends TileBasicUpgradeProcessor<Crystallizer> {
		
		public Crystallizer() {
			super("crystallizer", 0, 1, 1, 0, defaultItemSorptions(0, 1, true), defaultTankCapacities(16000, 1, 0), defaultTankSorptions(1, 0), NCRecipes.crystallizer_valid_fluids, processor_time_multiplier * processor_time[14], processor_power_multiplier * processor_power[14], true, NCRecipes.crystallizer, 15, 0, 0);
		}
	}
	
	public static class Enricher extends TileBasicUpgradeProcessor<Enricher> {
		
		public Enricher() {
			super("enricher", 1, 1, 0, 1, defaultItemSorptions(1, 0, true), defaultTankCapacities(16000, 1, 1), defaultTankSorptions(1, 1), NCRecipes.enricher_valid_fluids, processor_time_multiplier * processor_time[15], processor_power_multiplier * processor_power[15], true, NCRecipes.enricher, 16, 0, 0);
		}
	}
	
	public static class Extractor extends TileBasicUpgradeProcessor<Extractor> {
		
		public Extractor() {
			super("extractor", 1, 0, 1, 1, defaultItemSorptions(1, 1, true), defaultTankCapacities(16000, 0, 1), defaultTankSorptions(0, 1), NCRecipes.extractor_valid_fluids, processor_time_multiplier * processor_time[16], processor_power_multiplier * processor_power[16], false, NCRecipes.extractor, 17, 0, 0);
		}
	}
	
	public static class Centrifuge extends TileBasicUpgradeProcessor<Centrifuge> {
		
		public Centrifuge() {
			super("centrifuge", 1, 6, defaultItemSorptions(true), defaultTankCapacities(16000, 1, 6), defaultTankSorptions(1, 6), NCRecipes.centrifuge_valid_fluids, processor_time_multiplier * processor_time[17], processor_power_multiplier * processor_power[17], false, NCRecipes.centrifuge, 18, 0, 12);
		}
	}
	
	public static class RockCrusher extends TileBasicUpgradeProcessor<RockCrusher> {
		
		public RockCrusher() {
			super("rock_crusher", 1, 3, defaultItemSorptions(1, 3, true), processor_time_multiplier * processor_time[18], processor_power_multiplier * processor_power[18], false, NCRecipes.rock_crusher, 19, 0, 0);
		}
	}
}
