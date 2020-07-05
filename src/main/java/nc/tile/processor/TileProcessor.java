package nc.tile.processor;

import static nc.config.NCConfig.*;
import static nc.recipe.NCRecipes.*;

public class TileProcessor {
	
	public static class Manufactory extends TileItemProcessor {
		
		public Manufactory() {
			super("manufactory", 1, 1, defaultItemSorptions(1, 1, true), processor_time[0], processor_power[0], false, manufactory, 1, 0);
		}
	}
	
	public static class Separator extends TileItemProcessor {
		
		public Separator() {
			super("separator", 1, 2, defaultItemSorptions(1, 2, true), processor_time[1], processor_power[1], false, separator, 2, 0);
		}
	}
	
	public static class DecayHastener extends TileItemProcessor {
		
		public DecayHastener() {
			super("decay_hastener", 1, 1, defaultItemSorptions(1, 1, true), processor_time[2], processor_power[2], false, decay_hastener, 3, 0);
		}
	}
	
	public static class FuelReprocessor extends TileItemProcessor {
		
		public FuelReprocessor() {
			super("fuel_reprocessor", 1, 6, defaultItemSorptions(1, 6, true), processor_time[3], processor_power[3], false, fuel_reprocessor, 4, 12);
		}
	}
	
	public static class AlloyFurnace extends TileItemProcessor {
		
		public AlloyFurnace() {
			super("alloy_furnace", 2, 1, defaultItemSorptions(2, 1, true), processor_time[4], processor_power[4], true, alloy_furnace, 5, 0);
		}
	}
	
	public static class Infuser extends TileItemFluidProcessor {
		
		public Infuser() {
			super("infuser", 1, 1, 1, 0, defaultItemSorptions(1, 1, true), defaultTankCapacities(16000, 1, 0), defaultTankSorptions(1, 0), infuser_valid_fluids, processor_time[5], processor_power[5], true, infuser, 6, 0);
		}
	}
	
	public static class Melter extends TileItemFluidProcessor {
		
		public Melter() {
			super("melter", 1, 0, 0, 1, defaultItemSorptions(1, 0, true), defaultTankCapacities(16000, 0, 1), defaultTankSorptions(0, 1), melter_valid_fluids, processor_time[6], processor_power[6], true, melter, 7, 0);
		}
	}
	
	public static class Supercooler extends TileFluidProcessor {
		
		public Supercooler() {
			super("supercooler", 1, 1, defaultItemSorptions(true), defaultTankCapacities(16000, 1, 1), defaultTankSorptions(1, 1), supercooler_valid_fluids, processor_time[7], processor_power[7], true, supercooler, 8, 0);
		}
	}
	
	public static class Electrolyzer extends TileFluidProcessor {
		
		public Electrolyzer() {
			super("electrolyzer", 1, 4, defaultItemSorptions(true), defaultTankCapacities(16000, 1, 4), defaultTankSorptions(1, 4), electrolyzer_valid_fluids, processor_time[8], processor_power[8], true, electrolyzer, 9, 12);
		}
	}
	
	public static class Assembler extends TileItemProcessor {
		
		public Assembler() {
			super("assembler", 4, 1, defaultItemSorptions(4, 1, true), processor_time[9], processor_power[9], false, assembler, 10, 12);
		}
	}
	
	public static class IngotFormer extends TileItemFluidProcessor {
		
		public IngotFormer() {
			super("ingot_former", 0, 1, 1, 0, defaultItemSorptions(0, 1, true), defaultTankCapacities(16000, 1, 0), defaultTankSorptions(1, 0), ingot_former_valid_fluids, processor_time[10], processor_power[10], false, ingot_former, 11, 0);
		}
	}
	
	public static class Pressurizer extends TileItemProcessor {
		
		public Pressurizer() {
			super("pressurizer", 1, 1, defaultItemSorptions(1, 1, true), processor_time[11], processor_power[11], true, pressurizer, 12, 0);
		}
	}
	
	public static class ChemicalReactor extends TileFluidProcessor {
		
		public ChemicalReactor() {
			super("chemical_reactor", 2, 2, defaultItemSorptions(true), defaultTankCapacities(16000, 2, 2), defaultTankSorptions(2, 2), chemical_reactor_valid_fluids, processor_time[12], processor_power[12], true, chemical_reactor, 13, 0);
		}
	}
	
	public static class SaltMixer extends TileFluidProcessor {
		
		public SaltMixer() {
			super("salt_mixer", 2, 1, defaultItemSorptions(true), defaultTankCapacities(16000, 2, 1), defaultTankSorptions(2, 1), salt_mixer_valid_fluids, processor_time[13], processor_power[13], false, salt_mixer, 14, 0);
		}
	}
	
	public static class Crystallizer extends TileItemFluidProcessor {
		
		public Crystallizer() {
			super("crystallizer", 0, 1, 1, 0, defaultItemSorptions(0, 1, true), defaultTankCapacities(16000, 1, 0), defaultTankSorptions(1, 0), crystallizer_valid_fluids, processor_time[14], processor_power[14], true, crystallizer, 15, 0);
		}
	}
	
	public static class Enricher extends TileItemFluidProcessor {
		
		public Enricher() {
			super("enricher", 1, 1, 0, 1, defaultItemSorptions(1, 0, true), defaultTankCapacities(16000, 1, 1), defaultTankSorptions(1, 1), enricher_valid_fluids, processor_time[15], processor_power[15], true, enricher, 16, 0);
		}
	}
	
	public static class Extractor extends TileItemFluidProcessor {
		
		public Extractor() {
			super("extractor", 1, 0, 1, 1, defaultItemSorptions(1, 1, true), defaultTankCapacities(16000, 0, 1), defaultTankSorptions(0, 1), extractor_valid_fluids, processor_time[16], processor_power[16], false, extractor, 17, 0);
		}
	}
	
	public static class Centrifuge extends TileFluidProcessor {
		
		public Centrifuge() {
			super("centrifuge", 1, 4, defaultItemSorptions(true), defaultTankCapacities(16000, 1, 4), defaultTankSorptions(1, 4), centrifuge_valid_fluids, processor_time[17], processor_power[17], false, centrifuge, 18, 12);
		}
	}
	
	public static class RockCrusher extends TileItemProcessor {
		
		public RockCrusher() {
			super("rock_crusher", 1, 3, defaultItemSorptions(1, 3, true), processor_time[18], processor_power[18], false, rock_crusher, 19, 0);
		}
	}
}
