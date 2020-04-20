package nc.multiblock.qComputer;

import nc.enumm.IBlockMetaEnum;
import nc.multiblock.qComputer.tile.TileQuantumComputerGate;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IStringSerializable;

public class QuantumComputerGateEnums {
	
	public static enum SingleType implements IStringSerializable, IBlockMetaEnum {
		X("x", 0),
		Y("y", 1),
		Z("z", 2),
		H("h", 3),
		S("s", 4),
		Sdg("sdg", 5),
		T("t", 6),
		Tdg("tdg", 7),
		P("p", 8),
		RX("rx", 9),
		RY("ry", 10),
		RZ("rz", 11);
		
		private String name;
		private int id;
		
		private SingleType(String name, int id) {
			this.name = name;
			this.id = id;
		}
		
		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
		@Override
		public int getID() {
			return id;
		}
		
		@Override
		public int getHarvestLevel() {
			return 0;
		}
		
		@Override
		public String getHarvestTool() {
			return "pickaxe";
		}
		
		@Override
		public float getHardness() {
			return 2F;
		}
		
		@Override
		public float getResistance() {
			return 15F;
		}
		
		@Override
		public int getLightValue() {
			return 0;
		}
		
		public TileEntity getTile() {
			switch (this) {
			case X:
				return new TileQuantumComputerGate.X();
			case Y:
				return new TileQuantumComputerGate.Y();
			case Z:
				return new TileQuantumComputerGate.Z();
			case H:
				return new TileQuantumComputerGate.H();
			case S:
				return new TileQuantumComputerGate.S();
			case Sdg:
				return new TileQuantumComputerGate.Sdg();
			case T:
				return new TileQuantumComputerGate.T();
			case Tdg:
				return new TileQuantumComputerGate.Tdg();
			case P:
				return new TileQuantumComputerGate.P();
			case RX:
				return new TileQuantumComputerGate.RX();
			case RY:
				return new TileQuantumComputerGate.RY();
			case RZ:
				return new TileQuantumComputerGate.RZ();
				
			default:
				return null;
			}
		}
	}
	
	public static enum ControlType implements IStringSerializable, IBlockMetaEnum {
		CX("cx", 0),
		CY("cy", 1),
		CZ("cz", 2),
		CH("ch", 3),
		CS("cs", 4),
		CSdg("csdg", 5),
		CT("ct", 6),
		CTdg("ctdg", 7),
		CP("cp", 8),
		CRX("crx", 9),
		CRY("cry", 10),
		CRZ("crz", 11);
		
		private String name;
		private int id;
		
		private ControlType(String name, int id) {
			this.name = name;
			this.id = id;
		}
		
		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
		@Override
		public int getID() {
			return id;
		}
		
		@Override
		public int getHarvestLevel() {
			return 0;
		}
		
		@Override
		public String getHarvestTool() {
			return "pickaxe";
		}
		
		@Override
		public float getHardness() {
			return 2F;
		}
		
		@Override
		public float getResistance() {
			return 15F;
		}
		
		@Override
		public int getLightValue() {
			return 0;
		}
		
		public TileEntity getTile() {
			switch (this) {
			case CX:
				return new TileQuantumComputerGate.CX();
			case CY:
				return new TileQuantumComputerGate.CY();
			case CZ:
				return new TileQuantumComputerGate.CZ();
			case CH:
				return new TileQuantumComputerGate.CH();
			case CS:
				return new TileQuantumComputerGate.CS();
			case CSdg:
				return new TileQuantumComputerGate.CSdg();
			case CT:
				return new TileQuantumComputerGate.CT();
			case CTdg:
				return new TileQuantumComputerGate.CTdg();
			case CP:
				return new TileQuantumComputerGate.CP();
			case CRX:
				return new TileQuantumComputerGate.CRX();
			case CRY:
				return new TileQuantumComputerGate.CRY();
			case CRZ:
				return new TileQuantumComputerGate.CRZ();
				
			default:
				return null;
			}
		}
	}
	
	public static enum SwapType implements IStringSerializable, IBlockMetaEnum {
		SWAP("swap", 0),
		CSWAP("cswap", 1);
		
		private String name;
		private int id;
		
		private SwapType(String name, int id) {
			this.name = name;
			this.id = id;
		}
		
		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
		@Override
		public int getID() {
			return id;
		}
		
		@Override
		public int getHarvestLevel() {
			return 0;
		}
		
		@Override
		public String getHarvestTool() {
			return "pickaxe";
		}
		
		@Override
		public float getHardness() {
			return 2F;
		}
		
		@Override
		public float getResistance() {
			return 15F;
		}
		
		@Override
		public int getLightValue() {
			return 0;
		}
		
		public TileEntity getTile() {
			switch (this) {
			case SWAP:
				return new TileQuantumComputerGate.Swap();
			case CSWAP:
				return new TileQuantumComputerGate.ControlSwap();
				
			default:
				return null;
			}
		}
	}
}
