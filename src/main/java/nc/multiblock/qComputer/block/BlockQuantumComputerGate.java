package nc.multiblock.qComputer.block;

import javax.annotation.Nullable;

import nc.enumm.IBlockMetaEnum;
import nc.item.ItemMultitool;
import nc.multiblock.qComputer.QuantumComputerGateEnums;
import nc.multiblock.qComputer.tile.TileQuantumComputerGate;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockQuantumComputerGate<T extends Enum<T> & IStringSerializable & IBlockMetaEnum> extends BlockQuantumComputerMetaPart<T> {
	
	public BlockQuantumComputerGate(Class<T> enumm, PropertyEnum<T> property) {
		super(enumm, property);
	}
	
	public static class Single extends BlockQuantumComputerGate<QuantumComputerGateEnums.SingleType> {
		
		public final static PropertyEnum<QuantumComputerGateEnums.SingleType> TYPE = PropertyEnum.create("type", QuantumComputerGateEnums.SingleType.class);
		
		public Single() {
			super(QuantumComputerGateEnums.SingleType.class, TYPE);
		}
		
		@Override
		protected BlockStateContainer createBlockState() {
			return new BlockStateContainer(this, new IProperty[] {TYPE});
		}
		
		@Override
		public TileEntity createNewTileEntity(World world, int metadata) {
			return QuantumComputerGateEnums.SingleType.values()[metadata].getTile();
		}
	}
	
	public static class Control extends BlockQuantumComputerGate<QuantumComputerGateEnums.ControlType> {
		
		public final static PropertyEnum<QuantumComputerGateEnums.ControlType> TYPE = PropertyEnum.create("type", QuantumComputerGateEnums.ControlType.class);
		
		public Control() {
			super(QuantumComputerGateEnums.ControlType.class, TYPE);
		}
		
		@Override
		protected BlockStateContainer createBlockState() {
			return new BlockStateContainer(this, new IProperty[] {TYPE});
		}
		
		@Override
		public TileEntity createNewTileEntity(World world, int metadata) {
			return QuantumComputerGateEnums.ControlType.values()[metadata].getTile();
		}
	}
	
	public static class Swap extends BlockQuantumComputerGate<QuantumComputerGateEnums.SwapType> {
		
		public final static PropertyEnum<QuantumComputerGateEnums.SwapType> TYPE = PropertyEnum.create("type", QuantumComputerGateEnums.SwapType.class);
		
		public Swap() {
			super(QuantumComputerGateEnums.SwapType.class, TYPE);
		}
		
		@Override
		protected BlockStateContainer createBlockState() {
			return new BlockStateContainer(this, new IProperty[] {TYPE});
		}
		
		@Override
		public TileEntity createNewTileEntity(World world, int metadata) {
			return QuantumComputerGateEnums.SwapType.values()[metadata].getTile();
		}
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player == null) return false;
		if (hand != EnumHand.MAIN_HAND) return false;
		
		if (!ItemMultitool.isMultitool(player.getHeldItem(hand))) {
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof TileQuantumComputerGate) {
				if (!world.isRemote) {
					TileQuantumComputerGate gate = (TileQuantumComputerGate) tile;
					player.sendMessage(gate.gateInfo());
				}
				return true;
			}
		}
		
		return rightClickOnPart(world, pos, player, hand, facing, false);
	}
	
	@Override
	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EnumFacing side) {
		return side != null;
	}
}
