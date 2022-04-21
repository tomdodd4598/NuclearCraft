package nc.multiblock.qComputer.block;

import nc.enumm.IBlockMetaEnum;
import nc.multiblock.qComputer.tile.TileQuantumComputerCodeGenerator;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockQuantumComputerCodeGenerator extends BlockQuantumComputerMetaPart<BlockQuantumComputerCodeGenerator.Type> {
	
	public final static PropertyEnum<Type> TYPE = PropertyEnum.create("type", Type.class);
	
	public BlockQuantumComputerCodeGenerator() {
		super(Type.class, TYPE);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, TYPE);
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return values[meta].getTile();
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player == null) {
			return false;
		}
		if (hand != EnumHand.MAIN_HAND || player.isSneaking()) {
			return false;
		}
		return rightClickOnPart(world, pos, player, hand, facing);
	}
	
	public static enum Type implements IStringSerializable, IBlockMetaEnum {
		
		QASM("qasm", 0),
		QISKIT("qiskit", 1);
		
		private final String name;
		private final int id;
		
		private Type(String name, int id) {
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
				case QASM:
					return new TileQuantumComputerCodeGenerator.Qasm();
				case QISKIT:
					return new TileQuantumComputerCodeGenerator.Qiskit();
				
				default:
					return null;
			}
		}
	}
}
