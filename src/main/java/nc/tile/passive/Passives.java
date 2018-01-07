package nc.tile.passive;

import nc.config.NCConfig;
import nc.tile.energy.IEnergySpread;
import nc.tile.fluid.IFluidSpread;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;

public class Passives {
	
	public static abstract class TileElectromagnet extends TilePassive implements IEnergySpread {
		
		public TileElectromagnet(String name, int energyChange, int updateRate) {
			super(name + "_electromagnet", -energyChange, updateRate);
		}
		
		@Override
		public void update() {
			super.update();
			spreadEnergy();
		}
	}
	
	public static class TileFusionElectromagnet extends TileElectromagnet {
		
		public TileFusionElectromagnet() {
			super("fusion", NCConfig.fusion_electromagnet_power, NCConfig.fusion_update_rate / 4);
		}
	}
	
	public static class TileAcceleratorElectromagnet extends TileElectromagnet {
		
		public TileAcceleratorElectromagnet() {
			super("accelerator", NCConfig.accelerator_electromagnet_power, NCConfig.accelerator_update_rate / 4);
		}
	}
	
	public static class TileElectromagnetSupercooler extends TilePassive implements IEnergySpread, IFluidSpread {
		
		public TileElectromagnetSupercooler() {
			super("electromagnet_supercooler", -NCConfig.accelerator_electromagnet_power, FluidRegistry.getFluid("liquidhelium"), -NCConfig.accelerator_supercooler_coolant, NCConfig.accelerator_update_rate / 4);
		}
		
		@Override
		public void update() {
			super.update();
			spreadEnergy();
			spreadFluid();
		}
	}
	
	public static abstract class TileHeliumCollectorBase extends TilePassive {
		
		public TileHeliumCollectorBase(String type, int rateMult) {
			super("helium_collector" + type, FluidRegistry.getFluid("helium"), NCConfig.processor_passive_rate[0]*rateMult, NCConfig.processor_update_rate / 4);
		}
	}
	
	public static class TileHeliumCollector extends TileHeliumCollectorBase {
		
		public TileHeliumCollector() {
			super("", 1);
		}
	}
	
	public static class TileHeliumCollectorCompact extends TileHeliumCollectorBase {
		
		public TileHeliumCollectorCompact() {
			super("_compact", 8);
		}
	}
	
	public static class TileHeliumCollectorDense extends TileHeliumCollectorBase {
		
		public TileHeliumCollectorDense() {
			super("_dense", 64);
		}
	}
	
	public static abstract class TileCobblestoneGeneratorBase extends TilePassive {
		
		final int rateMult;
		
		public TileCobblestoneGeneratorBase(String type, int rateMult) {
			super("cobblestone_generator" + type, new ItemStack(Blocks.COBBLESTONE), NCConfig.processor_passive_rate[1]*rateMult, -NCConfig.cobble_gen_power*rateMult, NCConfig.processor_update_rate / 4);
			this.rateMult = rateMult;
		}
		
		@Override
		public void newStack() {
			inventoryStacks.set(0, new ItemStack(Blocks.COBBLESTONE, NCConfig.processor_passive_rate[1]*5*rateMult));
		}
	}
	
	public static class TileCobblestoneGenerator extends TileCobblestoneGeneratorBase {
		
		public TileCobblestoneGenerator() {
			super("", 1);
		}
	}
	
	public static class TileCobblestoneGeneratorCompact extends TileCobblestoneGeneratorBase {
		
		public TileCobblestoneGeneratorCompact() {
			super("_compact", 8);
		}
	}
	
	public static class TileCobblestoneGeneratorDense extends TileCobblestoneGeneratorBase {
		
		public TileCobblestoneGeneratorDense() {
			super("_dense", 64);
		}
	}
	
	public static abstract class TileWaterSourceBase extends TilePassive {
		
		public TileWaterSourceBase(String type, int rateMult) {
			super("water_source" + type, FluidRegistry.WATER, NCConfig.processor_passive_rate[2]*rateMult, NCConfig.processor_update_rate / 4);
		}
	}
	
	public static class TileWaterSource extends TileWaterSourceBase {
		
		public TileWaterSource() {
			super("", 1);
		}
	}
	
	public static class TileWaterSourceCompact extends TileWaterSourceBase {
		
		public TileWaterSourceCompact() {
			super("_compact", 8);
		}
	}
	
	public static class TileWaterSourceDense extends TileWaterSourceBase {
		
		public TileWaterSourceDense() {
			super("_dense", 64);
		}
	}
	
	public static abstract class TileNitrogenCollectorBase extends TilePassive {
		
		public TileNitrogenCollectorBase(String type, int rateMult) {
			super("nitrogen_collector" + type, FluidRegistry.getFluid("nitrogen"), NCConfig.processor_passive_rate[3]*rateMult, NCConfig.processor_update_rate / 4);
		}
	}
	
	public static class TileNitrogenCollector extends TileNitrogenCollectorBase {
		
		public TileNitrogenCollector() {
			super("", 1);
		}
	}
	
	public static class TileNitrogenCollectorCompact extends TileNitrogenCollectorBase {
		
		public TileNitrogenCollectorCompact() {
			super("_compact", 8);
		}
	}
	
	public static class TileNitrogenCollectorDense extends TileNitrogenCollectorBase {
		
		public TileNitrogenCollectorDense() {
			super("_dense", 64);
		}
	}
}
