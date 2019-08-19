package nc.block.tile.dummy;

import javax.annotation.Nullable;

import nc.NuclearCraft;
import nc.block.tile.BlockTile;
import nc.block.tile.ITileType;
import nc.block.tile.generator.BlockFusionCore;
import nc.enumm.BlockEnums.FusionDummyTileType;
import nc.init.NCBlocks;
import nc.tile.generator.TileFusionCore;
import nc.util.BlockPosHelper;
import nc.util.FluidHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFusionDummy extends BlockTile implements ITileType {
	
	private final FusionDummyTileType type;
	
	public BlockFusionDummy(FusionDummyTileType type) {
		super(Material.ANVIL);
		this.type = type;
		canCreatureSpawn = false;
	}
	
	@Override
	public String getTileName() {
		return type.getName();
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return type.getTile();
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		if (findCore(world, pos) != null) world.destroyBlock(findCore(world, pos), true);
		if (world.getTileEntity(pos) != null) world.removeTileEntity(pos);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.INVISIBLE;
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player == null || hand != EnumHand.MAIN_HAND || player.isSneaking()) return false;
		if (world.isRemote) return true;
		
		BlockPos corePos = findCore(world, pos);
		if (corePos != null) {
			if (world.getTileEntity(corePos) instanceof TileFusionCore) {
				TileFusionCore core = (TileFusionCore) world.getTileEntity(corePos);
				boolean accessedTanks = FluidHelper.accessTanks(player, hand, facing, core);
				if (accessedTanks) {
					core.refreshRecipe();
					core.refreshActivity();
					return true;
				}
				core.refreshMultiblock();
				FMLNetworkHandler.openGui(player, NuclearCraft.instance, 101, world, corePos.getX(), corePos.getY(), corePos.getZ());
			}
		}
		return true;
	}
	
	public BlockPos findCore(World world, BlockPos pos) {
		BlockPosHelper helper = new BlockPosHelper(pos);
		for (BlockPos blockPos : helper.cuboid(type.getCoords()[0], type.getCoords()[1], type.getCoords()[2], type.getCoords()[3], type.getCoords()[4], type.getCoords()[5])) if (isCore(world, blockPos)) return blockPos;
		return null;
	}
	
	private static boolean isCore(World world, BlockPos pos) {
		return world.getBlockState(pos).getBlock() instanceof BlockFusionCore;
	}
	
	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}
	
	@Override
	public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
		BlockPos corePos = findCore(world, pos);
		if (corePos == null) return 0;
		TileEntity tile = world.getTileEntity(corePos);
		if (tile instanceof TileFusionCore) {
			return ((TileFusionCore)tile).getComparatorStrength();
		}
		return Container.calcRedstone(tile);
	}
	
	@Override
	public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
		return new ItemStack(NCBlocks.fusion_core);
	}
	
	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
		player.addStat(StatList.getBlockStats(NCBlocks.fusion_core));
		player.addExhaustion(0.005F);
	}
}
