package nc.multiblock.fission.block;

import nc.multiblock.block.BlockMultiblockPart;
import nc.multiblock.fission.FissionReactor;
import nc.multiblock.fission.tile.IFissionComponent;
import nc.multiblock.fission.tile.TileFissionPart;
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

public abstract class BlockFissionPart extends BlockMultiblockPart {
	
	public BlockFissionPart() {
		super(Material.IRON, NCTabs.MULTIBLOCK);
	}
	
	public static abstract class Transparent extends BlockMultiblockPart.Transparent {
		
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
			if (tile instanceof TileFissionPart) {
				TileFissionPart part = (TileFissionPart) tile;
				FissionReactor reactor = part.getMultiblock();
				if (reactor != null) {
					float damage;
					if (part instanceof IFissionComponent && ((IFissionComponent)part).getCluster() != null) {
						damage = ((IFissionComponent)part).getCluster().getBurnDamage();
					}
					else {
						damage = reactor.getLogic().getBurnDamage();
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
