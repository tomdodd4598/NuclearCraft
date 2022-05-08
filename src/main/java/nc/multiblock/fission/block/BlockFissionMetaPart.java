package nc.multiblock.fission.block;

import static nc.config.NCConfig.fission_heat_damage;

import nc.enumm.IBlockMetaEnum;
import nc.multiblock.block.BlockMultiblockMetaPart;
import nc.tab.NCTabs;
import nc.util.BlockHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public abstract class BlockFissionMetaPart<T extends Enum<T> & IStringSerializable & IBlockMetaEnum> extends BlockMultiblockMetaPart<T> {
	
	public BlockFissionMetaPart(Class<T> enumm, PropertyEnum<T> property) {
		super(enumm, property, Material.IRON, NCTabs.multiblock());
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return fission_heat_damage ? BlockHelper.REDUCED_BLOCK_AABB : super.getCollisionBoundingBox(state, worldIn, pos);
	}
	
	@Override
	public void onEntityCollision(World world, BlockPos pos, IBlockState state, Entity entity) {
		BlockFissionPart.onEntityCollisionWithFissionReactor(world, pos, entity);
	}
}
