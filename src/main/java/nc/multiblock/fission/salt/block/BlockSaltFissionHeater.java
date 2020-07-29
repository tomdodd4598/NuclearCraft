package nc.multiblock.fission.salt.block;

import nc.NuclearCraft;
import nc.enumm.MetaEnums;
import nc.multiblock.fission.FissionReactor;
import nc.multiblock.fission.block.BlockFissionMetaPart;
import nc.multiblock.fission.salt.tile.TileSaltFissionHeater;
import nc.util.*;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.world.*;
import net.minecraftforge.fluids.FluidStack;

public class BlockSaltFissionHeater extends BlockFissionMetaPart<MetaEnums.CoolantHeaterType> /* implements ISidedProperty< SaltFissionHeaterSetting> */ {
	
	public final static PropertyEnum TYPE = PropertyEnum.create("type", MetaEnums.CoolantHeaterType.class);
	
	public BlockSaltFissionHeater() {
		super(MetaEnums.CoolantHeaterType.class, TYPE);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		// return new BlockStateContainer(this, TYPE, DOWN, UP, NORTH, SOUTH, WEST, EAST);
		return new BlockStateContainer(this, TYPE);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		switch (metadata) {
			case 0:
				return new TileSaltFissionHeater.Standard();
			case 1:
				return new TileSaltFissionHeater.Iron();
			case 2:
				return new TileSaltFissionHeater.Redstone();
			case 3:
				return new TileSaltFissionHeater.Quartz();
			case 4:
				return new TileSaltFissionHeater.Obsidian();
			case 5:
				return new TileSaltFissionHeater.NetherBrick();
			case 6:
				return new TileSaltFissionHeater.Glowstone();
			case 7:
				return new TileSaltFissionHeater.Lapis();
			case 8:
				return new TileSaltFissionHeater.Gold();
			case 9:
				return new TileSaltFissionHeater.Prismarine();
			case 10:
				return new TileSaltFissionHeater.Slime();
			case 11:
				return new TileSaltFissionHeater.EndStone();
			case 12:
				return new TileSaltFissionHeater.Purpur();
			case 13:
				return new TileSaltFissionHeater.Diamond();
			case 14:
				return new TileSaltFissionHeater.Emerald();
			case 15:
				return new TileSaltFissionHeater.Copper();
		}
		return new TileSaltFissionHeater.Standard();
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		// return state.withProperty(DOWN, getProperty(world, pos, EnumFacing.DOWN)).withProperty(UP, getProperty(world, pos, EnumFacing.UP)).withProperty(NORTH, getProperty(world, pos, EnumFacing.NORTH)).withProperty(SOUTH, getProperty(world, pos, EnumFacing.SOUTH)).withProperty(WEST, getProperty(world, pos, EnumFacing.WEST)).withProperty(EAST, getProperty(world, pos, EnumFacing.EAST));
		return super.getActualState(state, world, pos);
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
			if (tile instanceof TileSaltFissionHeater) {
				TileSaltFissionHeater heater = (TileSaltFissionHeater) tile;
				FissionReactor reactor = heater.getMultiblock();
				if (reactor != null) {
					FluidStack fluidStack = FluidStackHelper.getFluid(player.getHeldItem(hand));
					if (heater.canModifyFilter(0) && heater.getTanks().get(0).isEmpty() && fluidStack != null && !FluidStackHelper.stacksEqual(heater.getFilterTanks().get(0).getFluid(), fluidStack) && heater.getTanks().get(0).canFillFluidType(fluidStack)) {
						player.sendMessage(new TextComponentString(Lang.localise("message.nuclearcraft.filter") + " " + TextFormatting.BOLD + Lang.localise(fluidStack.getUnlocalizedName())));
						FluidStack filter = fluidStack.copy();
						filter.amount = 1000;
						heater.getFilterTanks().get(0).setFluid(filter);
						heater.onFilterChanged(0);
					}
					else {
						player.openGui(NuclearCraft.instance, 203, world, pos.getX(), pos.getY(), pos.getZ());
					}
					return true;
				}
			}
		}
		return rightClickOnPart(world, pos, player, hand, facing, true);
	}	
}
