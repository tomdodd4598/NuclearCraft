package nc.tile.passive;

import nc.config.NCConfig;
import nc.tile.energy.IEnergySpread;
import nc.tile.fluid.IFluidSpread;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;

public class TilePassive {
	
	public static abstract class ElectromagnetAbstract extends TilePassiveAbstract implements IEnergySpread {
		
		public ElectromagnetAbstract(String name, int energyChange, int updateRate) {
			super(name + "_electromagnet", -energyChange, updateRate);
		}
		
		@Override
		public void update() {
			super.update();
			if(!world.isRemote) {
				if(shouldTileCheck()) spreadEnergy();
				tickTile();
			}
		}
	}
	
	public static class FusionElectromagnet extends ElectromagnetAbstract {
		
		public FusionElectromagnet() {
			super("fusion", NCConfig.fusion_electromagnet_power, NCConfig.machine_update_rate / 5);
		}
	}
	
	public static class AcceleratorElectromagnet extends ElectromagnetAbstract {
		
		public AcceleratorElectromagnet() {
			super("accelerator", NCConfig.accelerator_electromagnet_power, NCConfig.machine_update_rate / 5);
		}
	}
	
	public static class ElectromagnetSupercooler extends TilePassiveAbstract implements IEnergySpread, IFluidSpread {
		
		public ElectromagnetSupercooler() {
			super("electromagnet_supercooler", -NCConfig.accelerator_electromagnet_power, FluidRegistry.getFluid("liquidhelium"), -NCConfig.accelerator_supercooler_coolant, NCConfig.machine_update_rate / 5);
		}
		
		@Override
		public void update() {
			super.update();
			if(!world.isRemote) {
				if(shouldTileCheck()) {
					spreadEnergy();
					spreadFluid();
				}
				tickTile();
			}
		}
	}
	
	public static abstract class HeliumCollectorAbstract extends TilePassiveAbstract {
		
		public HeliumCollectorAbstract(String type, int rateMult) {
			super("helium_collector" + type, FluidRegistry.getFluid("helium"), NCConfig.processor_passive_rate[0]*rateMult, NCConfig.machine_update_rate / 5);
		}
		
		@Override
		public void update() {
			super.update();
			if(!world.isRemote) tickTile();
		}
	}
	
	public static class HeliumCollector extends HeliumCollectorAbstract {
		
		public HeliumCollector() {
			super("", 1);
		}
	}
	
	public static class HeliumCollectorCompact extends HeliumCollectorAbstract {
		
		public HeliumCollectorCompact() {
			super("_compact", 8);
		}
	}
	
	public static class HeliumCollectorDense extends HeliumCollectorAbstract {
		
		public HeliumCollectorDense() {
			super("_dense", 64);
		}
	}
	
	public static abstract class CobblestoneGeneratorAbstract extends TilePassiveAbstract {
		
		final int rateMult;
		
		public CobblestoneGeneratorAbstract(String type, int rateMult) {
			super("cobblestone_generator" + type, new ItemStack(Blocks.COBBLESTONE), NCConfig.processor_passive_rate[1]*rateMult, -NCConfig.cobble_gen_power*rateMult, NCConfig.machine_update_rate / 5);
			this.rateMult = rateMult;
		}
		
		@Override
		public void update() {
			super.update();
			if(!world.isRemote) tickTile();
		}
		
		@Override
		public void setNewStack() {
			inventoryStacks.set(0, new ItemStack(Blocks.COBBLESTONE, NCConfig.processor_passive_rate[1]*NCConfig.machine_update_rate*rateMult/5));
		}
	}
	
	public static class CobblestoneGenerator extends CobblestoneGeneratorAbstract {
		
		public CobblestoneGenerator() {
			super("", 1);
		}
	}
	
	public static class CobblestoneGeneratorCompact extends CobblestoneGeneratorAbstract {
		
		public CobblestoneGeneratorCompact() {
			super("_compact", 8);
		}
	}
	
	public static class CobblestoneGeneratorDense extends CobblestoneGeneratorAbstract {
		
		public CobblestoneGeneratorDense() {
			super("_dense", 64);
		}
	}
	
	public static abstract class WaterSourceAbstract extends TilePassiveAbstract {
		
		public WaterSourceAbstract(String type, int rateMult) {
			super("water_source" + type, FluidRegistry.WATER, NCConfig.processor_passive_rate[2]*rateMult, NCConfig.machine_update_rate / 5);
		}
		
		@Override
		public void update() {
			super.update();
			if(!world.isRemote) tickTile();
		}
	}
	
	public static class WaterSource extends WaterSourceAbstract {
		
		public WaterSource() {
			super("", 1);
		}
	}
	
	public static class WaterSourceCompact extends WaterSourceAbstract {
		
		public WaterSourceCompact() {
			super("_compact", 8);
		}
	}
	
	public static class WaterSourceDense extends WaterSourceAbstract {
		
		public WaterSourceDense() {
			super("_dense", 64);
		}
	}
	
	public static abstract class NitrogenCollectorAbstract extends TilePassiveAbstract {
		
		public NitrogenCollectorAbstract(String type, int rateMult) {
			super("nitrogen_collector" + type, FluidRegistry.getFluid("nitrogen"), NCConfig.processor_passive_rate[3]*rateMult, NCConfig.machine_update_rate / 5);
		}
		
		@Override
		public void update() {
			super.update();
			if(!world.isRemote) tickTile();
		}
	}
	
	public static class NitrogenCollector extends NitrogenCollectorAbstract {
		
		public NitrogenCollector() {
			super("", 1);
		}
	}
	
	public static class NitrogenCollectorCompact extends NitrogenCollectorAbstract {
		
		public NitrogenCollectorCompact() {
			super("_compact", 8);
		}
	}
	
	public static class NitrogenCollectorDense extends NitrogenCollectorAbstract {
		
		public NitrogenCollectorDense() {
			super("_dense", 64);
		}
	}
}
