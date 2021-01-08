package nc.multiblock.fission.salt.block;

import nc.NuclearCraft;
import nc.multiblock.fission.FissionReactor;
import nc.multiblock.fission.block.BlockFissionPart;
import nc.multiblock.fission.salt.tile.TileSaltFissionVessel;
import nc.util.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

public class BlockSaltFissionVessel extends BlockFissionPart {
	
	public BlockSaltFissionVessel() {
		super();
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileSaltFissionVessel();
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player == null) {
			return false;
		}
		if (hand != EnumHand.MAIN_HAND || player.isSneaking()) {
			return false;
		}
		
		if (!world.isRemote) {
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof TileSaltFissionVessel) {
				TileSaltFissionVessel vessel = (TileSaltFissionVessel) tile;
				FissionReactor reactor = vessel.getMultiblock();
				if (reactor != null) {
					FluidStack fluidStack = FluidStackHelper.getFluid(player.getHeldItem(hand));
					if (vessel.canModifyFilter(0) && vessel.getTanks().get(0).isEmpty() && fluidStack != null && !FluidStackHelper.stacksEqual(vessel.getFilterTanks().get(0).getFluid(), fluidStack) && vessel.getTanks().get(0).canFillFluidType(fluidStack)) {
						player.sendMessage(new TextComponentString(Lang.localise("message.nuclearcraft.filter") + " " + TextFormatting.BOLD + Lang.localise(fluidStack.getUnlocalizedName())));
						FluidStack filter = fluidStack.copy();
						filter.amount = 1000;
						vessel.getFilterTanks().get(0).setFluid(filter);
						vessel.onFilterChanged(0);
					}
					else {
						player.openGui(NuclearCraft.instance, 202, world, pos.getX(), pos.getY(), pos.getZ());
					}
					return true;
				}
			}
		}
		return rightClickOnPart(world, pos, player, hand, facing, true);
	}
}
