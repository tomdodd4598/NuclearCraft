package nc.block;

import java.util.Random;

import nc.util.BlockHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.common.util.ITeleporter;

public abstract class NCBlockPortal extends NCBlock {
	
	protected final int targetDimension, returnDimension;
	
	public NCBlockPortal(Material material, int targetDimension, int returnDimension) {
		super(material);
		setBlockUnbreakable();
		setResistance(6000000F);
		this.targetDimension = targetDimension;
		this.returnDimension = returnDimension;
	}
	
	@Override
	public int quantityDropped(Random random) {
		return 0;
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Items.AIR;
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return BlockHelper.REDUCED_BLOCK_AABB;
	}
	
	@Override
	public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		if (!worldIn.isRemote && !entityIn.isDead && !entityIn.isRiding() && !entityIn.isBeingRidden() && entityIn.isNonBoss()) {
			entityIn.changeDimension(entityIn.dimension == targetDimension ? returnDimension : targetDimension, getTeleporter(worldIn, pos, state, entityIn));
		}
	}
	
	protected abstract ITeleporter getTeleporter(World worldIn, BlockPos pos, IBlockState state, Entity entityIn);
}
