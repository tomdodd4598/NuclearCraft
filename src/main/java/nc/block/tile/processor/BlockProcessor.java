package nc.block.tile.processor;

import static nc.block.property.BlockProperties.*;

import java.util.Random;

import nc.block.tile.*;
import nc.enumm.BlockEnums.ProcessorType;
import nc.util.BlockHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.*;

public class BlockProcessor extends BlockSidedTile implements IActivatable, ITileType {
	
	protected final ProcessorType type;
	
	public BlockProcessor(ProcessorType type) {
		super(Material.IRON);
		if (type.getCreativeTab() != null) {
			setCreativeTab(type.getCreativeTab());
		}
		this.type = type;
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
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing enumfacing = EnumFacing.byIndex(meta & 7);
		if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
			enumfacing = EnumFacing.NORTH;
		}
		return getDefaultState().withProperty(FACING_HORIZONTAL, enumfacing).withProperty(ACTIVE, Boolean.valueOf((meta & 8) > 0));
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		int i = state.getValue(FACING_HORIZONTAL).getIndex();
		if (state.getValue(ACTIVE).booleanValue()) {
			i |= 8;
		}
		return i;
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {FACING_HORIZONTAL, ACTIVE});
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return getDefaultState().withProperty(FACING_HORIZONTAL, placer.getHorizontalFacing().getOpposite()).withProperty(ACTIVE, Boolean.valueOf(false));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		if (!state.getValue(ACTIVE)) {
			return;
		}
		BlockHelper.spawnParticleOnProcessor(state, world, pos, rand, state.getValue(FACING_HORIZONTAL), type.getParticle1());
		BlockHelper.spawnParticleOnProcessor(state, world, pos, rand, state.getValue(FACING_HORIZONTAL), type.getParticle2());
	}
}
