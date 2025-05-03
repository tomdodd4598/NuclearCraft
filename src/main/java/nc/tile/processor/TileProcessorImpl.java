package nc.tile.processor;

import nc.network.tile.processor.EnergyProcessorUpdatePacket;
import nc.tile.processor.info.ProcessorContainerInfoImpl;

public class TileProcessorImpl {
	
	public static abstract class TileBasicEnergyProcessor<TILE extends TileBasicEnergyProcessor<TILE>> extends TileEnergyProcessor<TILE, ProcessorContainerInfoImpl.BasicProcessorContainerInfo<TILE, EnergyProcessorUpdatePacket>> implements IBasicProcessor<TILE, EnergyProcessorUpdatePacket> {
		
		/**
		 * Don't use this constructor!
		 */
		protected TileBasicEnergyProcessor() {
			super();
		}
		
		protected TileBasicEnergyProcessor(String name) {
			super(name);
		}
	}
	
	public static abstract class TileBasicUpgradableEnergyProcessor<TILE extends TileBasicUpgradableEnergyProcessor<TILE>> extends TileUpgradableEnergyProcessor<TILE, ProcessorContainerInfoImpl.BasicUpgradableProcessorContainerInfo<TILE, EnergyProcessorUpdatePacket>> implements IBasicUpgradableProcessor<TILE, EnergyProcessorUpdatePacket> {
		
		/**
		 * Don't use this constructor!
		 */
		protected TileBasicUpgradableEnergyProcessor() {
			super();
		}
		
		protected TileBasicUpgradableEnergyProcessor(String name) {
			super(name);
		}
	}
	
	public static class TileBasicEnergyProcessorDyn extends TileBasicEnergyProcessor<TileBasicEnergyProcessorDyn> {
		
		/**
		 * Don't use this constructor!
		 */
		public TileBasicEnergyProcessorDyn() {
			super();
		}
		
		public TileBasicEnergyProcessorDyn(String name) {
			super(name);
		}
	}
	
	public static class TileBasicUpgradableEnergyProcessorDyn extends TileBasicUpgradableEnergyProcessor<TileBasicUpgradableEnergyProcessorDyn> {
		
		/**
		 * Don't use this constructor!
		 */
		public TileBasicUpgradableEnergyProcessorDyn() {
			super();
		}
		
		public TileBasicUpgradableEnergyProcessorDyn(String name) {
			super(name);
		}
	}
	
	public static class TileManufactory extends TileBasicUpgradableEnergyProcessor<TileManufactory> {
		
		public TileManufactory() {
			super("manufactory");
		}
	}
	
	public static class TileSeparator extends TileBasicUpgradableEnergyProcessor<TileSeparator> {
		
		public TileSeparator() {
			super("separator");
		}
	}
	
	public static class TileDecayHastener extends TileBasicUpgradableEnergyProcessor<TileDecayHastener> {
		
		public TileDecayHastener() {
			super("decay_hastener");
		}
	}
	
	public static class TileFuelReprocessor extends TileBasicUpgradableEnergyProcessor<TileFuelReprocessor> {
		
		public TileFuelReprocessor() {
			super("fuel_reprocessor");
		}
	}
	
	public static class TileAlloyFurnace extends TileBasicUpgradableEnergyProcessor<TileAlloyFurnace> {
		
		public TileAlloyFurnace() {
			super("alloy_furnace");
		}
	}
	
	public static class TileInfuser extends TileBasicUpgradableEnergyProcessor<TileInfuser> {
		
		public TileInfuser() {
			super("infuser");
		}
	}
	
	public static class TileMelter extends TileBasicUpgradableEnergyProcessor<TileMelter> {
		
		public TileMelter() {
			super("melter");
		}
	}
	
	public static class TileSupercooler extends TileBasicUpgradableEnergyProcessor<TileSupercooler> {
		
		public TileSupercooler() {
			super("supercooler");
		}
	}
	
	public static class TileElectrolyzer extends TileBasicUpgradableEnergyProcessor<TileElectrolyzer> {
		
		public TileElectrolyzer() {
			super("electrolyzer");
		}
	}
	
	public static class TileAssembler extends TileBasicUpgradableEnergyProcessor<TileAssembler> {
		
		public TileAssembler() {
			super("assembler");
		}
	}
	
	public static class TileIngotFormer extends TileBasicUpgradableEnergyProcessor<TileIngotFormer> {
		
		public TileIngotFormer() {
			super("ingot_former");
		}
	}
	
	public static class TilePressurizer extends TileBasicUpgradableEnergyProcessor<TilePressurizer> {
		
		public TilePressurizer() {
			super("pressurizer");
		}
	}
	
	public static class TileChemicalReactor extends TileBasicUpgradableEnergyProcessor<TileChemicalReactor> {
		
		public TileChemicalReactor() {
			super("chemical_reactor");
		}
	}
	
	public static class TileSaltMixer extends TileBasicUpgradableEnergyProcessor<TileSaltMixer> {
		
		public TileSaltMixer() {
			super("salt_mixer");
		}
	}
	
	public static class TileCrystallizer extends TileBasicUpgradableEnergyProcessor<TileCrystallizer> {
		
		public TileCrystallizer() {
			super("crystallizer");
		}
	}
	
	public static class TileEnricher extends TileBasicUpgradableEnergyProcessor<TileEnricher> {
		
		public TileEnricher() {
			super("enricher");
		}
	}
	
	public static class TileExtractor extends TileBasicUpgradableEnergyProcessor<TileExtractor> {
		
		public TileExtractor() {
			super("extractor");
		}
	}
	
	public static class TileCentrifuge extends TileBasicUpgradableEnergyProcessor<TileCentrifuge> {
		
		public TileCentrifuge() {
			super("centrifuge");
		}
	}
	
	public static class TileRockCrusher extends TileBasicUpgradableEnergyProcessor<TileRockCrusher> {
		
		public TileRockCrusher() {
			super("rock_crusher");
		}
	}
	
	public static class TileElectricFurnace extends TileBasicUpgradableEnergyProcessor<TileElectricFurnace> {
		
		public TileElectricFurnace() {
			super("electric_furnace");
		}
	}
}
