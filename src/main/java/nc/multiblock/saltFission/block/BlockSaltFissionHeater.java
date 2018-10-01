package nc.multiblock.saltFission.block;

import nc.block.property.ISidedFluid;
import nc.multiblock.saltFission.tile.TileSaltFissionHeater;
import nc.tile.fluid.ITileFluid;
import nc.tile.internal.fluid.FluidConnection;
import nc.util.Lang;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSaltFissionHeater extends BlockSaltFissionPartBase implements ISidedFluid {

	public BlockSaltFissionHeater() {
		super("salt_fission_heater");
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileSaltFissionHeater();
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
			ITileFluid heater = (ITileFluid) world.getTileEntity(pos);
			EnumFacing side = player.isSneaking() ? facing.getOpposite() : facing;
			heater.toggleFluidConnection(side);
			FluidConnection connection = heater.getFluidConnection(side);
			String message = player.isSneaking() ? "nc.block.fluid_toggle_opposite" : "nc.block.fluid_toggle";
			TextFormatting color = connection == FluidConnection.IN ? TextFormatting.RED : (connection == FluidConnection.OUT ? TextFormatting.AQUA : (connection == FluidConnection.BOTH ? TextFormatting.WHITE : TextFormatting.GRAY));
			if (!world.isRemote) player.sendMessage(new TextComponentString(Lang.localise(message) + " " + color + Lang.localise("nc.block.salt_heater_fluid_side." + connection.getName())));
			return true;
		}
		return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
	}
}
