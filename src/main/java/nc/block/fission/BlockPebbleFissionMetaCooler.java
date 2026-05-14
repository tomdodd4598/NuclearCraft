package nc.block.fission;

import nc.enumm.MetaEnums;
import nc.multiblock.fission.FissionReactor;
import nc.tile.fission.*;
import nc.util.*;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

public class BlockPebbleFissionMetaCooler extends BlockFissionMetaPart<MetaEnums.GasCoolerType> {
	
	public final static PropertyEnum<MetaEnums.GasCoolerType> TYPE = PropertyEnum.create("type", MetaEnums.GasCoolerType.class);
	
	public BlockPebbleFissionMetaCooler() {
		super(MetaEnums.GasCoolerType.class, TYPE);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, TYPE);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return switch (metadata) {
			case 0 -> new TilePebbleFissionCooler.Oxygen();
			case 1 -> new TilePebbleFissionCooler.Hydrogen();
			case 2 -> new TilePebbleFissionCooler.Helium();
			case 3 -> new TilePebbleFissionCooler.Nitrogen();
			case 4 -> new TilePebbleFissionCooler.Fluorine();
			case 5 -> new TilePebbleFissionCooler.Methane();
			case 6 -> new TilePebbleFissionCooler.CarbonDioxide();
			case 7 -> new TilePebbleFissionCooler.CarbonMonoxide();
			case 8 -> new TilePebbleFissionCooler.Ethene();
			case 9 -> new TilePebbleFissionCooler.Ethyne();
			case 10 -> new TilePebbleFissionCooler.Fluoromethane();
			case 11 -> new TilePebbleFissionCooler.Ammonia();
			case 12 -> new TilePebbleFissionCooler.Diborane();
			case 13 -> new TilePebbleFissionCooler.SulfurDioxide();
			case 14 -> new TilePebbleFissionCooler.SulfurTrioxide();
			case 15 -> new TilePebbleFissionCooler.SulfurHexafluoride();
			default -> new TilePebbleFissionCooler.Oxygen();
		};
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
