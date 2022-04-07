package nc.block;

import nc.util.DamageSources;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.*;

public class NCBlockIce extends BlockIce {
	
	public NCBlockIce(float slipperiness) {
		super();
		setHardness(0.5F);
		setLightOpacity(3);
		this.slipperiness = slipperiness;
	}
	
	@Override
	public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, net.minecraft.entity.EntityLiving.SpawnPlacementType type) {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
		Block block = iblockstate.getBlock();
		
		return block == this ? false : super.shouldSideBeRendered(state, blockAccess, pos, side);
	}
	
	@Override
	public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		entityIn.attackEntityFrom(DamageSources.HYPOTHERMIA, 2F);
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return new AxisAlignedBB(0.002D, 0.002D, 0.002D, 0.998D, 1D, 0.998D);
	}
}
