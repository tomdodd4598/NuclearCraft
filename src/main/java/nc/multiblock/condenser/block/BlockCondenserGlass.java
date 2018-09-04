package nc.multiblock.condenser.block;

import nc.multiblock.condenser.tile.TileCondenserGlass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockCondenserGlass extends BlockCondenserPartBase.Transparent {

	public BlockCondenserGlass() {
		super("condenser_glass", true);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileCondenserGlass();
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player == null) return false;
		if (hand != EnumHand.MAIN_HAND || player.isSneaking()) return false;
		return rightClickOnPart(world, pos, player);
	}
}
