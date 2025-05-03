package nc.block.fission;

import nc.block.multiblock.BlockMultiblockPart;
import nc.multiblock.fission.FissionReactor;
import nc.tab.NCTabs;
import nc.tile.fission.*;
import nc.util.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

import static nc.config.NCConfig.fission_heat_damage;

public abstract class BlockFissionPart extends BlockMultiblockPart {
	
	public BlockFissionPart() {
		super(Material.IRON, NCTabs.multiblock);
	}
	
	public static abstract class Transparent extends BlockMultiblockPart.Transparent {
		
		public Transparent(boolean smartRender) {
			super(Material.IRON, NCTabs.multiblock, smartRender);
		}
		
		@Override
		public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
			return fission_heat_damage ? BlockHelper.REDUCED_BLOCK_AABB : super.getCollisionBoundingBox(state, worldIn, pos);
		}
		
		@Override
		public void onEntityCollision(World world, BlockPos pos, IBlockState state, Entity entity) {
			onEntityCollisionWithFissionReactor(world, pos, entity);
		}
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return fission_heat_damage ? BlockHelper.REDUCED_BLOCK_AABB : super.getCollisionBoundingBox(state, worldIn, pos);
	}
	
	@Override
	public void onEntityCollision(World world, BlockPos pos, IBlockState state, Entity entity) {
		onEntityCollisionWithFissionReactor(world, pos, entity);
	}
	
	public static void onEntityCollisionWithFissionReactor(World world, BlockPos pos, Entity entity) {
		if (fission_heat_damage && entity instanceof EntityLivingBase) {
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof TileFissionPart part) {
				FissionReactor reactor = part.getMultiblock();
				if (reactor != null) {
					float damage;
					if (part instanceof IFissionComponent && ((IFissionComponent) part).getCluster() != null) {
						damage = ((IFissionComponent) part).getCluster().getBurnDamage();
					}
					else {
						damage = reactor.getLogic().getBurnDamage();
					}
					if (damage > 0F) {
						entity.attackEntityFrom(DamageSources.FISSION_BURN, damage);
					}
					if (damage > 5F) {
						entity.setFire(NCMath.toInt(damage - 4F));
					}
				}
			}
		}
	}
}
