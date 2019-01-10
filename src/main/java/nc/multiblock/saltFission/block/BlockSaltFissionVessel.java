package nc.multiblock.saltFission.block;

import nc.block.property.ISidedFluid;
import nc.multiblock.saltFission.tile.TileSaltFissionVessel;
import nc.tile.fluid.ITileFluid;
import nc.tile.internal.fluid.FluidConnection;
import nc.util.Lang;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSaltFissionVessel extends BlockSaltFissionPartBase implements ISidedFluid {

	private static EnumFacing placementSide = null;
	
	public BlockSaltFissionVessel() {
		super("salt_fission_vessel");
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileSaltFissionVessel();
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return createFluidBlockState(this);
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		return getActualFluidState(state, world, pos);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (hand != EnumHand.MAIN_HAND || player == null) return false;
		
		if (player.getHeldItemMainhand().isEmpty() && world.getTileEntity(pos) instanceof ITileFluid) {
			ITileFluid vessel = (ITileFluid) world.getTileEntity(pos);
			EnumFacing side = player.isSneaking() ? facing.getOpposite() : facing;
			vessel.toggleFluidConnection(side);
			FluidConnection connection = vessel.getFluidConnection(side);
			String message = player.isSneaking() ? "nc.block.fluid_toggle_opposite" : "nc.block.fluid_toggle";
			TextFormatting color = connection == FluidConnection.IN ? TextFormatting.RED : (connection == FluidConnection.OUT ? TextFormatting.AQUA : (connection == FluidConnection.BOTH ? TextFormatting.WHITE : TextFormatting.GRAY));
			if (!world.isRemote) player.sendMessage(new TextComponentString(Lang.localise(message) + " " + color + Lang.localise("nc.block.salt_vessel_fluid_side." + connection.getName())));
			return true;
		}
		return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		placementSide = null;
		if (placer != null && placer.isSneaking()) placementSide = facing.getOpposite();
		return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand);
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		if (placementSide ==  null) return;
		BlockPos from = pos.offset(placementSide);
		if (world.getTileEntity(pos) instanceof TileSaltFissionVessel && world.getTileEntity(from) instanceof TileSaltFissionVessel) {
			TileSaltFissionVessel tube = (TileSaltFissionVessel) world.getTileEntity(pos);
			TileSaltFissionVessel other = (TileSaltFissionVessel) world.getTileEntity(from);
			tube.setFluidConnections(other.getFluidConnections().clone());
			tube.markAndRefresh();
		}
	}
}
