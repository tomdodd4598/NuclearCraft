package nc.block.fission.port;

import nc.enumm.MetaEnums;
import nc.tile.ITileGui;
import nc.tile.fission.TilePebbleFissionCooler;
import nc.tile.fission.port.TileFissionCoolerPort;
import nc.tile.internal.fluid.TankSorption;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static nc.block.property.BlockProperties.*;

public class BlockFissionCoolerPort extends BlockFissionFluidMetaPort<TileFissionCoolerPort, TilePebbleFissionCooler, MetaEnums.GasCoolerType> {
	
	public final static PropertyEnum<MetaEnums.GasCoolerType> TYPE = PropertyEnum.create("type", MetaEnums.GasCoolerType.class);
	
	public BlockFissionCoolerPort() {
		super(TileFissionCoolerPort.class, MetaEnums.GasCoolerType.class, TYPE);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, TYPE, AXIS_ALL, ACTIVE);
	}
	
	@Override
	public boolean getActualStateActive(TileFissionCoolerPort port) {
		return port.getTankSorption(EnumFacing.DOWN, 0) != TankSorption.IN;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return switch (metadata) {
			case 0 -> new TileFissionCoolerPort.Oxygen();
			case 1 -> new TileFissionCoolerPort.Hydrogen();
			case 2 -> new TileFissionCoolerPort.Helium();
			case 3 -> new TileFissionCoolerPort.Nitrogen();
			case 4 -> new TileFissionCoolerPort.Fluorine();
			case 5 -> new TileFissionCoolerPort.Methane();
			case 6 -> new TileFissionCoolerPort.CarbonDioxide();
			case 7 -> new TileFissionCoolerPort.CarbonMonoxide();
			case 8 -> new TileFissionCoolerPort.Ethene();
			case 9 -> new TileFissionCoolerPort.Ethyne();
			case 10 -> new TileFissionCoolerPort.Fluoromethane();
			case 11 -> new TileFissionCoolerPort.Ammonia();
			case 12 -> new TileFissionCoolerPort.Diborane();
			case 13 -> new TileFissionCoolerPort.SulfurDioxide();
			case 14 -> new TileFissionCoolerPort.SulfurTrioxide();
			case 15 -> new TileFissionCoolerPort.SulfurHexafluoride();
			default -> new TileFissionCoolerPort.Oxygen();
		};
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (hand != EnumHand.MAIN_HAND || player.isSneaking()) {
			return false;
		}
		
		if (!world.isRemote) {
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof ITileGui<?, ?, ?> tileGui) {
				tileGui.openGui(world, pos, player);
			}
			return true;
		}
		return rightClickOnPart(world, pos, player, hand, facing, true);
	}
}
