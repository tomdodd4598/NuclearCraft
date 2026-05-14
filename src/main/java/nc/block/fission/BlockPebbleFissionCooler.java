package nc.block.fission;

import nc.multiblock.fission.FissionReactor;
import nc.tile.fission.TilePebbleFissionCooler;
import nc.util.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

public abstract class BlockPebbleFissionCooler extends BlockFissionPart {
	
	public BlockPebbleFissionCooler() {
		super();
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (hand != EnumHand.MAIN_HAND || player.isSneaking()) {
			return false;
		}
		
		if (!world.isRemote) {
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof TilePebbleFissionCooler cooler) {
				FissionReactor reactor = cooler.getMultiblock();
				if (reactor != null) {
					FluidStack fluidStack = FluidStackHelper.getFluid(player.getHeldItem(hand));
					if (cooler.canModifyFilter(0) && cooler.getTanks().get(0).isEmpty() && fluidStack != null && !FluidStackHelper.stacksEqual(cooler.getFilterTanks().get(0).getFluid(), fluidStack) && cooler.isFluidValidForTank(0, fluidStack)) {
						player.sendMessage(new TextComponentString(Lang.localize("message.nuclearcraft.filter") + " " + TextFormatting.BOLD + Lang.localize(fluidStack.getUnlocalizedName())));
						FluidStack filter = fluidStack.copy();
						filter.amount = 1000;
						cooler.getFilterTanks().get(0).setFluid(filter);
						cooler.onFilterChanged(0);
					}
					else {
						cooler.openGui(world, pos, player);
					}
					return true;
				}
			}
		}
		return rightClickOnPart(world, pos, player, hand, facing, true);
	}
}
