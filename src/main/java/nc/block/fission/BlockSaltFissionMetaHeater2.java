package nc.block.fission;

import nc.enumm.MetaEnums;
import nc.multiblock.fission.FissionReactor;
import nc.tile.fission.TileSaltFissionHeater;
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

public class BlockSaltFissionMetaHeater2 extends BlockFissionMetaPart<MetaEnums.CoolantHeaterType2> {
	
	public final static PropertyEnum<MetaEnums.CoolantHeaterType2> TYPE = PropertyEnum.create("type", MetaEnums.CoolantHeaterType2.class);
	
	public BlockSaltFissionMetaHeater2() {
		super(MetaEnums.CoolantHeaterType2.class, TYPE);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, TYPE);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
        return switch (metadata) {
            case 0 -> new TileSaltFissionHeater.Tin();
            case 1 -> new TileSaltFissionHeater.Lead();
            case 2 -> new TileSaltFissionHeater.Boron();
            case 3 -> new TileSaltFissionHeater.Lithium();
            case 4 -> new TileSaltFissionHeater.Magnesium();
            case 5 -> new TileSaltFissionHeater.Manganese();
            case 6 -> new TileSaltFissionHeater.Aluminum();
            case 7 -> new TileSaltFissionHeater.Silver();
            case 8 -> new TileSaltFissionHeater.Fluorite();
            case 9 -> new TileSaltFissionHeater.Villiaumite();
            case 10 -> new TileSaltFissionHeater.Carobbiite();
            case 11 -> new TileSaltFissionHeater.Arsenic();
            case 12 -> new TileSaltFissionHeater.LiquidNitrogen();
            case 13 -> new TileSaltFissionHeater.LiquidHelium();
            case 14 -> new TileSaltFissionHeater.Enderium();
            case 15 -> new TileSaltFissionHeater.Cryotheum();
            default -> new TileSaltFissionHeater.Tin();
        };
    }
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (hand != EnumHand.MAIN_HAND || player.isSneaking()) {
			return false;
		}
		
		if (!world.isRemote) {
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof TileSaltFissionHeater heater) {
                FissionReactor reactor = heater.getMultiblock();
				if (reactor != null) {
					FluidStack fluidStack = FluidStackHelper.getFluid(player.getHeldItem(hand));
					if (heater.canModifyFilter(0) && heater.getTanks().get(0).isEmpty() && fluidStack != null && !FluidStackHelper.stacksEqual(heater.getFilterTanks().get(0).getFluid(), fluidStack) && heater.getTanks().get(0).canFillFluidType(fluidStack)) {
						player.sendMessage(new TextComponentString(Lang.localize("message.nuclearcraft.filter") + " " + TextFormatting.BOLD + Lang.localize(fluidStack.getUnlocalizedName())));
						FluidStack filter = fluidStack.copy();
						filter.amount = 1000;
						heater.getFilterTanks().get(0).setFluid(filter);
						heater.onFilterChanged(0);
					}
					else {
						heater.openGui(world, pos, player);
					}
					return true;
				}
			}
		}
		return rightClickOnPart(world, pos, player, hand, facing, true);
	}
}
