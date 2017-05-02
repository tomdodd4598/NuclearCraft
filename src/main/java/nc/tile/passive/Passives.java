package nc.tile.passive;

import nc.block.tile.passive.BlockAcceleratorElectromagnet;
import nc.block.tile.passive.BlockElectromagnetSupercooler;
import nc.block.tile.passive.BlockFusionElectromagnet;
import nc.init.NCFluids;

public class Passives {
	
	public static abstract class TileElectromagnet extends TilePassive {
		
		public TileElectromagnet(String name, int energyChange) {
			super(name + "Electromagnet", -energyChange, 5);
		}
	}
	
	public static class TileFusionElectromagnet extends TileElectromagnet {
		
		public TileFusionElectromagnet() {
			super("fusion", 1000);
		}
		
		public void setBlockState() {
			BlockFusionElectromagnet.setState(isRunning, world, pos);
		}
	}
	
	public static class TileAcceleratorElectromagnet extends TileElectromagnet {
		
		public TileAcceleratorElectromagnet() {
			super("accelerator", 5000);
		}
		
		public void setBlockState() {
			BlockAcceleratorElectromagnet.setState(isRunning, world, pos);
		}
	}
	
	public static class TileElectromagnetSupercooler extends TilePassive {
		
		public TileElectromagnetSupercooler() {
			super("electromagnetSupercooler", -5000, NCFluids.liquidhelium, -1, 5);
		}
		
		public void setBlockState() {
			BlockElectromagnetSupercooler.setState(isRunning, world, pos);
		}
	}
}
