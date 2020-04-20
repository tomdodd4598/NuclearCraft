package nc.multiblock.fission.block.port;

import nc.NuclearCraft;
import nc.enumm.IBlockMetaEnum;
import nc.multiblock.fission.FissionReactor;
import nc.multiblock.fission.tile.port.IFissionPortTarget;
import nc.multiblock.fission.tile.port.TileFissionFluidPort;
import nc.tile.fluid.ITileFilteredFluid;
import nc.util.FluidStackHelper;
import nc.util.Lang;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

public abstract class BlockFissionFluidMetaPort<PORT extends TileFissionFluidPort<PORT, TARGET>, TARGET extends IFissionPortTarget<PORT, TARGET> & ITileFilteredFluid, T extends Enum<T> & IStringSerializable & IBlockMetaEnum> extends BlockFissionMetaPort<PORT, TARGET, T> {
	
	protected final int guiID;
	
	public BlockFissionFluidMetaPort(Class<PORT> portClass, Class<T> enumm, PropertyEnum<T> property, int guiID) {
		super(portClass, enumm, property);
		this.guiID = guiID;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player == null) return false;
		if (hand != EnumHand.MAIN_HAND || player.isSneaking()) return false;
		
		if (!world.isRemote) {
			TileEntity tile = world.getTileEntity(pos);
			if (portClass.isInstance(tile)) {
				PORT port = portClass.cast(tile);
				FissionReactor reactor = port.getMultiblock();
				if (reactor != null) {
					FluidStack fluidStack = FluidStackHelper.getFluid(player.getHeldItem(hand));
					if (port.canModifyFilter(0) && port.getTanks().get(0).isEmpty() && fluidStack != null && !FluidStackHelper.stacksEqual(port.getFilterTanks().get(0).getFluid(), fluidStack) && port.getTanks().get(0).canFillFluidType(fluidStack)) {
						player.sendMessage(new TextComponentString(Lang.localise("message.nuclearcraft.filter") + " " + TextFormatting.BOLD + Lang.localise(fluidStack.getUnlocalizedName())));
						FluidStack filter = fluidStack.copy();
						filter.amount = 1000;
						port.getFilterTanks().get(0).setFluid(filter);
						port.onFilterChanged(0);
					}
					else {
						player.openGui(NuclearCraft.instance, guiID, world, pos.getX(), pos.getY(), pos.getZ());
					}
					return true;
				}
			}
		}
		return rightClickOnPart(world, pos, player, hand, facing, true);
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		//super.breakBlock(world, pos, state);
		world.removeTileEntity(pos);
	}
}
