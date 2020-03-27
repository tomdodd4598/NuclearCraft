package nc.tile.passive;

import nc.config.NCConfig;
import nc.recipe.ingredient.FluidIngredient;
import nc.recipe.ingredient.ItemIngredient;
import nc.tile.energy.IEnergySpread;
import nc.tile.fluid.IFluidSpread;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class TilePassive {
	
	public static abstract class ElectromagnetAbstract extends TilePassiveAbstract implements IEnergySpread {
		
		public ElectromagnetAbstract(String name, double energyChange) {
			super(name + "_electromagnet", -energyChange);
		}
	}
	
	public static class FusionElectromagnet extends ElectromagnetAbstract {
		
		public FusionElectromagnet() {
			super("fusion", NCConfig.fusion_electromagnet_power);
		}
	}
	
	public static class AcceleratorElectromagnet extends ElectromagnetAbstract {
		
		public AcceleratorElectromagnet() {
			super("accelerator", NCConfig.accelerator_electromagnet_power);
		}
	}
	
	public static class ElectromagnetSupercooler extends TilePassiveAbstract implements IEnergySpread, IFluidSpread {
		
		public ElectromagnetSupercooler() {
			super("electromagnet_supercooler", -NCConfig.accelerator_electromagnet_power, new FluidIngredient("liquid_helium", 1), -NCConfig.accelerator_supercooler_coolant);
		}
	}
	
	public static abstract class CobblestoneGeneratorAbstract extends TilePassiveAbstract {
		
		public CobblestoneGeneratorAbstract(String type, int rateMult) {
			super("cobblestone_generator" + type, new ItemIngredient(new ItemStack(Blocks.COBBLESTONE)), NCConfig.processor_passive_rate[0]*rateMult, -NCConfig.cobble_gen_power*rateMult);
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
			super("water_source" + type, new FluidIngredient("water", 1), NCConfig.processor_passive_rate[1]*rateMult);
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
			super("nitrogen_collector" + type, new FluidIngredient("nitrogen", 1), NCConfig.processor_passive_rate[2]*rateMult);
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
