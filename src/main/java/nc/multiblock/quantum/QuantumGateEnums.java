package nc.multiblock.quantum;

import nc.enumm.IBlockMetaEnum;
import nc.tile.quantum.TileQuantumComputerGate;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IStringSerializable;

public class QuantumGateEnums {
	
	public enum SingleType implements IStringSerializable, IBlockMetaEnum {
		
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
		
		private final String name;
		private final int id;
		
		SingleType(String name, int id) {
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
            return switch (this) {
                case X -> new TileQuantumComputerGate.X();
                case Y -> new TileQuantumComputerGate.Y();
                case Z -> new TileQuantumComputerGate.Z();
                case H -> new TileQuantumComputerGate.H();
                case S -> new TileQuantumComputerGate.S();
                case Sdg -> new TileQuantumComputerGate.Sdg();
                case T -> new TileQuantumComputerGate.T();
                case Tdg -> new TileQuantumComputerGate.Tdg();
                case P -> new TileQuantumComputerGate.P();
                case RX -> new TileQuantumComputerGate.RX();
                case RY -> new TileQuantumComputerGate.RY();
                case RZ -> new TileQuantumComputerGate.RZ();
            };
		}
	}
	
	public enum ControlType implements IStringSerializable, IBlockMetaEnum {
		
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
		
		private final String name;
		private final int id;
		
		ControlType(String name, int id) {
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
            return switch (this) {
                case CX -> new TileQuantumComputerGate.CX();
                case CY -> new TileQuantumComputerGate.CY();
                case CZ -> new TileQuantumComputerGate.CZ();
                case CH -> new TileQuantumComputerGate.CH();
                case CS -> new TileQuantumComputerGate.CS();
                case CSdg -> new TileQuantumComputerGate.CSdg();
                case CT -> new TileQuantumComputerGate.CT();
                case CTdg -> new TileQuantumComputerGate.CTdg();
                case CP -> new TileQuantumComputerGate.CP();
                case CRX -> new TileQuantumComputerGate.CRX();
                case CRY -> new TileQuantumComputerGate.CRY();
                case CRZ -> new TileQuantumComputerGate.CRZ();
            };
		}
	}
	
	public enum SwapType implements IStringSerializable, IBlockMetaEnum {
		
		SWAP("swap", 0),
		CSWAP("cswap", 1);
		
		private final String name;
		private final int id;
		
		SwapType(String name, int id) {
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
            return switch (this) {
                case SWAP -> new TileQuantumComputerGate.Swap();
                case CSWAP -> new TileQuantumComputerGate.ControlSwap();
            };
		}
	}
}
