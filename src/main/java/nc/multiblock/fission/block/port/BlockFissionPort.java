package nc.multiblock.fission.block.port;

import static nc.block.property.BlockProperties.ACTIVE;
import static nc.block.property.BlockProperties.AXIS_ALL;

import nc.block.tile.IActivatable;
import nc.block.tile.IDynamicState;
import nc.multiblock.fission.block.BlockFissionPart;
import nc.multiblock.fission.tile.port.IFissionPortTarget;
import nc.multiblock.fission.tile.port.TileFissionPort;
import nc.util.PosHelper;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockFissionPort<PORT extends TileFissionPort<PORT, TARGET>, TARGET extends IFissionPortTarget<PORT, TARGET>> extends BlockFissionPart implements IActivatable {
	
	protected final Class<PORT> portClass;
	
	public BlockFissionPort(Class<PORT> portClass) {
		super();
		this.portClass = portClass;
		setDefaultState(blockState.getBaseState().withProperty(AXIS_ALL, EnumFacing.Axis.Z).withProperty(ACTIVE, Boolean.valueOf(false)));
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, AXIS_ALL, ACTIVE);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing.Axis axis = PosHelper.AXES[meta & 3];
		return getDefaultState().withProperty(AXIS_ALL, axis).withProperty(ACTIVE, Boolean.valueOf((meta & 4) > 0));
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		int i = state.getValue(AXIS_ALL).ordinal();
		if (state.getValue(ACTIVE).booleanValue()) {
			i |= 4;
		}
		return i;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return getDefaultState().withProperty(AXIS_ALL, EnumFacing.getDirectionFromEntityLiving(pos, placer).getAxis());
	}
}
