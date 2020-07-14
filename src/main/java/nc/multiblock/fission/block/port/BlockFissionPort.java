package nc.multiblock.fission.block.port;

import static nc.block.property.BlockProperties.AXIS_ALL;

import nc.multiblock.fission.block.BlockFissionPart;
import nc.multiblock.fission.tile.port.*;
import nc.util.PosHelper;
import net.minecraft.block.state.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockFissionPort<PORT extends TileFissionPort<PORT, TARGET>, TARGET extends IFissionPortTarget<PORT, TARGET>> extends BlockFissionPart {
	
	protected final Class<PORT> portClass;
	
	public BlockFissionPort(Class<PORT> portClass) {
		super();
		this.portClass = portClass;
		setDefaultState(blockState.getBaseState().withProperty(AXIS_ALL, EnumFacing.Axis.Z));
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, AXIS_ALL);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(AXIS_ALL, PosHelper.AXES[meta]);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(AXIS_ALL).ordinal();
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return getDefaultState().withProperty(AXIS_ALL, EnumFacing.getDirectionFromEntityLiving(pos, placer).getAxis());
	}
}
