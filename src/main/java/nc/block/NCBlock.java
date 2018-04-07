package nc.block;

import nc.Global;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class NCBlock extends Block {
	
	protected final boolean transparent;
	protected final boolean smartRender;
	
	public NCBlock(String name, Material material) {
		this(name, material, false, false);
	}
	
	public NCBlock(String name, Material material, boolean smartRender) {
		this(name, material, true, smartRender);
	}
	
	public NCBlock(String name, Material material, boolean transparent, boolean smartRender) {
		super(material);
		setUnlocalizedName(Global.MOD_ID + "." + name);
		setRegistryName(new ResourceLocation(Global.MOD_ID, name));
		setHarvestLevel("pickaxe", 0);
		setHardness(transparent ? 1.5F : 2F);
		setResistance(transparent ? 10F : 15F);
		this.transparent = transparent;
		this.smartRender = smartRender;
	}
	
	@Override
	public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, net.minecraft.entity.EntityLiving.SpawnPlacementType type) {
		return false;
	}
	
	@Override
	public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return transparent ? BlockRenderLayer.CUTOUT : BlockRenderLayer.SOLID;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return !transparent;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return !transparent;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess world, BlockPos pos, EnumFacing side) {
		if (!transparent) return super.shouldSideBeRendered(blockState, world, pos, side);
		if (!smartRender) return true;
		
		IBlockState otherState = world.getBlockState(pos.offset(side));
		Block block = otherState.getBlock();
		
		if (blockState != otherState) return true;
		
		return block == this ? false : super.shouldSideBeRendered(blockState, world, pos, side);
    }
}
