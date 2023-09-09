package nc.tile.processor;

import nc.tile.processor.info.ProcessorContainerInfoImpl.*;

public class TileProcessorImpl {
	
	public static abstract class TileBasicProcessor<TILE extends TileBasicProcessor<TILE>> extends TileProcessor<TILE, BasicProcessorContainerInfo<TILE>> {
		
		protected TileBasicProcessor(String name) {
			super(name);
		}
	}
	
	public static abstract class TileBasicUpgradableProcessor<TILE extends TileBasicUpgradableProcessor<TILE>> extends TileUpgradableProcessor<TILE, BasicUpgradableProcessorContainerInfo<TILE>> {
		
		protected TileBasicUpgradableProcessor(String name) {
			super(name);
		}
	}
	
	public static class Manufactory extends TileBasicUpgradableProcessor<Manufactory> {
		
		public Manufactory() {
			super("manufactory");
		}
	}
	
	public static class Separator extends TileBasicUpgradableProcessor<Separator> {
		
		public Separator() {
			super("separator");
		}
	}
	
	public static class DecayHastener extends TileBasicUpgradableProcessor<DecayHastener> {
		
		public DecayHastener() {
			super("decay_hastener");
		}
	}
	
	public static class FuelReprocessor extends TileBasicUpgradableProcessor<FuelReprocessor> {
		
		public FuelReprocessor() {
			super("fuel_reprocessor");
		}
	}
	
	public static class AlloyFurnace extends TileBasicUpgradableProcessor<AlloyFurnace> {
		
		public AlloyFurnace() {
			super("alloy_furnace");
		}
	}
	
	public static class Infuser extends TileBasicUpgradableProcessor<Infuser> {
		
		public Infuser() {
			super("infuser");
		}
	}
	
	public static class Melter extends TileBasicUpgradableProcessor<Melter> {
		
		public Melter() {
			super("melter");
		}
	}
	
	public static class Supercooler extends TileBasicUpgradableProcessor<Supercooler> {
		
		public Supercooler() {
			super("supercooler");
		}
	}
	
	public static class Electrolyzer extends TileBasicUpgradableProcessor<Electrolyzer> {
		
		public Electrolyzer() {
			super("electrolyzer");
		}
	}
	
	public static class Assembler extends TileBasicUpgradableProcessor<Assembler> {
		
		public Assembler() {
			super("assembler");
		}
	}
	
	public static class IngotFormer extends TileBasicUpgradableProcessor<IngotFormer> {
		
		public IngotFormer() {
			super("ingot_former");
		}
	}
	
	public static class Pressurizer extends TileBasicUpgradableProcessor<Pressurizer> {
		
		public Pressurizer() {
			super("pressurizer");
		}
	}
	
	public static class ChemicalReactor extends TileBasicUpgradableProcessor<ChemicalReactor> {
		
		public ChemicalReactor() {
			super("chemical_reactor");
		}
	}
	
	public static class SaltMixer extends TileBasicUpgradableProcessor<SaltMixer> {
		
		public SaltMixer() {
			super("salt_mixer");
		}
	}
	
	public static class Crystallizer extends TileBasicUpgradableProcessor<Crystallizer> {
		
		public Crystallizer() {
			super("crystallizer");
		}
	}
	
	public static class Enricher extends TileBasicUpgradableProcessor<Enricher> {
		
		public Enricher() {
			super("enricher");
		}
	}
	
	public static class Extractor extends TileBasicUpgradableProcessor<Extractor> {
		
		public Extractor() {
			super("extractor");
		}
	}
	
	public static class Centrifuge extends TileBasicUpgradableProcessor<Centrifuge> {
		
		public Centrifuge() {
			super("centrifuge");
		}
	}
	
	public static class RockCrusher extends TileBasicUpgradableProcessor<RockCrusher> {
		
		public RockCrusher() {
			super("rock_crusher");
		}
	}
}
