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
	
	public static class TileBasicProcessorDyn extends TileBasicProcessor<TileBasicProcessorDyn> {
		
		public TileBasicProcessorDyn() {
			super("basic_processor_dyn");
		}
	}
	
	public static class TileBasicUpgradableProcessorDyn extends TileBasicUpgradableProcessor<TileBasicUpgradableProcessorDyn> {
		
		public TileBasicUpgradableProcessorDyn() {
			super("basic_upgradable_processor_dyn");
		}
	}
	
	public static class TileManufactory extends TileBasicUpgradableProcessor<TileManufactory> {
		
		public TileManufactory() {
			super("manufactory");
		}
	}
	
	public static class TileSeparator extends TileBasicUpgradableProcessor<TileSeparator> {
		
		public TileSeparator() {
			super("separator");
		}
	}
	
	public static class TileDecayHastener extends TileBasicUpgradableProcessor<TileDecayHastener> {
		
		public TileDecayHastener() {
			super("decay_hastener");
		}
	}
	
	public static class TileFuelReprocessor extends TileBasicUpgradableProcessor<TileFuelReprocessor> {
		
		public TileFuelReprocessor() {
			super("fuel_reprocessor");
		}
	}
	
	public static class TileAlloyFurnace extends TileBasicUpgradableProcessor<TileAlloyFurnace> {
		
		public TileAlloyFurnace() {
			super("alloy_furnace");
		}
	}
	
	public static class TileInfuser extends TileBasicUpgradableProcessor<TileInfuser> {
		
		public TileInfuser() {
			super("infuser");
		}
	}
	
	public static class TileMelter extends TileBasicUpgradableProcessor<TileMelter> {
		
		public TileMelter() {
			super("melter");
		}
	}
	
	public static class TileSupercooler extends TileBasicUpgradableProcessor<TileSupercooler> {
		
		public TileSupercooler() {
			super("supercooler");
		}
	}
	
	public static class TileElectrolyzer extends TileBasicUpgradableProcessor<TileElectrolyzer> {
		
		public TileElectrolyzer() {
			super("electrolyzer");
		}
	}
	
	public static class TileAssembler extends TileBasicUpgradableProcessor<TileAssembler> {
		
		public TileAssembler() {
			super("assembler");
		}
	}
	
	public static class TileIngotFormer extends TileBasicUpgradableProcessor<TileIngotFormer> {
		
		public TileIngotFormer() {
			super("ingot_former");
		}
	}
	
	public static class TilePressurizer extends TileBasicUpgradableProcessor<TilePressurizer> {
		
		public TilePressurizer() {
			super("pressurizer");
		}
	}
	
	public static class TileChemicalReactor extends TileBasicUpgradableProcessor<TileChemicalReactor> {
		
		public TileChemicalReactor() {
			super("chemical_reactor");
		}
	}
	
	public static class TileSaltMixer extends TileBasicUpgradableProcessor<TileSaltMixer> {
		
		public TileSaltMixer() {
			super("salt_mixer");
		}
	}
	
	public static class TileCrystallizer extends TileBasicUpgradableProcessor<TileCrystallizer> {
		
		public TileCrystallizer() {
			super("crystallizer");
		}
	}
	
	public static class TileEnricher extends TileBasicUpgradableProcessor<TileEnricher> {
		
		public TileEnricher() {
			super("enricher");
		}
	}
	
	public static class TileExtractor extends TileBasicUpgradableProcessor<TileExtractor> {
		
		public TileExtractor() {
			super("extractor");
		}
	}
	
	public static class TileCentrifuge extends TileBasicUpgradableProcessor<TileCentrifuge> {
		
		public TileCentrifuge() {
			super("centrifuge");
		}
	}
	
	public static class TileRockCrusher extends TileBasicUpgradableProcessor<TileRockCrusher> {
		
		public TileRockCrusher() {
			super("rock_crusher");
		}
	}
}
