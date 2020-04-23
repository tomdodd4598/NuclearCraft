package nc.multiblock.qComputer.block;

import javax.annotation.Nullable;

import nc.item.ItemMultitool;
import nc.multiblock.qComputer.tile.TileQuantumComputerQubit;
import nc.util.Lang;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockQuantumComputerQubit extends BlockQuantumComputerPart {
	
	public BlockQuantumComputerQubit() {
		super();
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileQuantumComputerQubit();
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player == null) return false;
		if (hand != EnumHand.MAIN_HAND) return false;
		
		if (!ItemMultitool.isMultitool(player.getHeldItem(hand))) {
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof TileQuantumComputerQubit) {
				if (!world.isRemote) {
					TileQuantumComputerQubit qubit = (TileQuantumComputerQubit) tile;
					player.sendMessage(new TextComponentString(Lang.localise("info.nuclearcraft.multitool.quantum_computer.qubit_id", qubit.id)));
				}
				return true;
			}
		}
		
		return rightClickOnPart(world, pos, player, hand, facing, false);
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isNormalCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EnumFacing side) {
		return side != null;
	}
	
	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}
	
	@Override
	public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileQuantumComputerQubit) {
			return ((TileQuantumComputerQubit)tile).redstone ? 15 : 0;
		}
		return 0;
	}
}
