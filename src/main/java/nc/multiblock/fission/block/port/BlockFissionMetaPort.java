package nc.multiblock.fission.block.port;

import static nc.block.property.BlockProperties.*;

import nc.block.tile.IActivatable;
import nc.enumm.IBlockMetaEnum;
import nc.multiblock.fission.block.BlockFissionMetaPart;
import nc.multiblock.fission.tile.port.*;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.*;

public abstract class BlockFissionMetaPort<PORT extends TileFissionPort<PORT, TARGET>, TARGET extends IFissionPortTarget<PORT, TARGET>, T extends Enum<T> & IStringSerializable & IBlockMetaEnum> extends BlockFissionMetaPart<T> implements IActivatable {
	
	protected final Class<PORT> portClass;
	
	public BlockFissionMetaPort(Class<PORT> portClass, Class<T> enumm, PropertyEnum<T> property) {
		super(enumm, property);
		this.portClass = portClass;
		setDefaultState(getDefaultState().withProperty(AXIS_ALL, EnumFacing.Axis.Z).withProperty(ACTIVE, Boolean.valueOf(false)));
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		if (portClass.isInstance(tile)) {
			PORT port = portClass.cast(tile);
			EnumFacing facing = port.getPartPosition().getFacing();
			return state.withProperty(AXIS_ALL, facing != null ? facing.getAxis() : port.axis).withProperty(ACTIVE, getActualStateActive(port));
		}
		return state;
	}
	
	public abstract boolean getActualStateActive(PORT port);
	
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getStateFromMeta(meta).withProperty(AXIS_ALL, EnumFacing.getDirectionFromEntityLiving(pos, placer).getAxis());
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		TileEntity tile = world.getTileEntity(pos);
		if (portClass.isInstance(tile)) {
			PORT port = portClass.cast(tile);
			port.axis = state.getValue(AXIS_ALL);
			world.setBlockState(pos, state.withProperty(AXIS_ALL, port.axis), 2);
		}
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player == null) {
			return false;
		}
		if (hand != EnumHand.MAIN_HAND || player.isSneaking()) {
			return false;
		}
		return rightClickOnPart(world, pos, player, hand, facing);
	}
}
