package nc.multiblock.fission.block;

import nc.multiblock.MultiblockBlockPartBase;
import nc.multiblock.fission.FissionReactor;
import nc.multiblock.fission.tile.IFissionComponent;
import nc.multiblock.fission.tile.TileFissionPartBase;
import nc.tab.NCTabs;
import nc.util.BlockHelper;
import nc.util.DamageSources;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockFissionPartBase extends MultiblockBlockPartBase {
	
	public BlockFissionPartBase() {
		super(Material.IRON, NCTabs.MULTIBLOCK);
	}
	
	public static abstract class Transparent extends MultiblockBlockPartBase.Transparent {
		
		public Transparent(boolean smartRender) {
			super(Material.IRON, NCTabs.MULTIBLOCK, smartRender);
		}
		
		@Override
		public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
			return BlockHelper.REDUCED_BLOCK_AABB;
		}
		
		@Override
		public void onEntityCollision(World world, BlockPos pos, IBlockState state, Entity entity) {
			onEntityCollisionWithFissionReactor(world, pos, entity);
		}
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return BlockHelper.REDUCED_BLOCK_AABB;
	}
	
	@Override
	public void onEntityCollision(World world, BlockPos pos, IBlockState state, Entity entity) {
		onEntityCollisionWithFissionReactor(world, pos, entity);
	}
	
	public static void onEntityCollisionWithFissionReactor(World world, BlockPos pos, Entity entity) {
		if (entity instanceof EntityLivingBase) {
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof TileFissionPartBase) {
				TileFissionPartBase part = (TileFissionPartBase) tile;
				FissionReactor reactor = part.getMultiblock();
				if (reactor != null) {
					float damage;
					if (part instanceof IFissionComponent && ((IFissionComponent)part).getCluster() != null) {
						damage = ((IFissionComponent)part).getCluster().getBurnDamage();
					}
					else {
						damage = reactor.getBurnDamage();
					}
					if (damage > 0F) {
						entity.attackEntityFrom(DamageSources.FISSION_BURN, damage);
					}
					if (damage > 5F) {
						entity.setFire((int) (damage - 4F));
					}
				}
			}
		}
	}
}
