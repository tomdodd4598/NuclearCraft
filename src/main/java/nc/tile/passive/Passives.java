package nc.tile.passive;

import nc.block.tile.passive.BlockAcceleratorElectromagnet;
import nc.block.tile.passive.BlockCobblestoneGenerator;
import nc.block.tile.passive.BlockElectromagnetSupercooler;
import nc.block.tile.passive.BlockFusionElectromagnet;
import nc.block.tile.passive.BlockFusionElectromagnetTransparent;
import nc.block.tile.passive.BlockHeliumCollector;
import nc.block.tile.passive.BlockNitrogenCollector;
import nc.block.tile.passive.BlockWaterSource;
import nc.config.NCConfig;
import nc.init.NCFluids;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;

public class Passives {
	
	public static abstract class TileElectromagnet extends TilePassive {
		
		public TileElectromagnet(String name, int energyChange) {
			super(name + " Electromagnet", -energyChange, 5);
		}
	}
	
	public static class TileFusionElectromagnet extends TileElectromagnet {
		
		public TileFusionElectromagnet() {
			super("Fusion", NCConfig.fusion_electromagnet_power);
		}
		
		public void setBlockState() {
			if (worldObj.getBlockState(pos).getBlock() instanceof BlockFusionElectromagnetTransparent) BlockFusionElectromagnetTransparent.setState(isRunning, worldObj, pos); else BlockFusionElectromagnet.setState(isRunning, worldObj, pos);
		}
	}
	
	public static class TileAcceleratorElectromagnet extends TileElectromagnet {
		
		public TileAcceleratorElectromagnet() {
			super("Accelerator", NCConfig.accelerator_electromagnet_power);
		}
		
		public void setBlockState() {
			BlockAcceleratorElectromagnet.setState(isRunning, worldObj, pos);
		}
	}
	
	public static class TileElectromagnetSupercooler extends TilePassive {
		
		public TileElectromagnetSupercooler() {
			super("Electromagnet Supercooler", -NCConfig.accelerator_electromagnet_power, NCFluids.liquidhelium, -NCConfig.accelerator_supercooler_coolant, 5);
		}
		
		public void setBlockState() {
			BlockElectromagnetSupercooler.setState(isRunning, worldObj, pos);
		}
	}
	
	public static class TileHeliumCollector extends TilePassive {
		
		public TileHeliumCollector() {
			super("Helium Collector", NCFluids.helium, NCConfig.processor_passive_rate[0], 5);
		}
		
		public void setBlockState() {
			BlockHeliumCollector.setState(worldObj, pos);
		}
	}
	
	public static class TileCobblestoneGenerator extends TilePassive {
		
		public TileCobblestoneGenerator() {
			super("Cobblestone Generator", new ItemStack(Blocks.COBBLESTONE), NCConfig.processor_passive_rate[1], -NCConfig.cobble_gen_power, 5);
		}
		
		public void setBlockState() {
			BlockCobblestoneGenerator.setState(worldObj, pos);
		}
	}
	
	public static class TileWaterSource extends TilePassive {
		
		public TileWaterSource() {
			super("Water Source", FluidRegistry.WATER, NCConfig.processor_passive_rate[2], 5);
		}
		
		public void setBlockState() {
			BlockWaterSource.setState(worldObj, pos);
		}
	}
	
public static class TileNitrogenCollector extends TilePassive {
		
		public TileNitrogenCollector() {
			super("Nitrogen Collector", NCFluids.nitrogen, NCConfig.processor_passive_rate[3], 5);
		}
		
		public void setBlockState() {
			BlockNitrogenCollector.setState(worldObj, pos);
		}
	}
}
