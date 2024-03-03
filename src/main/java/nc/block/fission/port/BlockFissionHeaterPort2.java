package nc.block.fission.port;

import static nc.block.property.BlockProperties.*;

import nc.enumm.MetaEnums;
import nc.tile.ITileGui;
import nc.tile.fission.TileSaltFissionHeater;
import nc.tile.fission.port.TileFissionHeaterPort;
import nc.tile.internal.fluid.TankSorption;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockFissionHeaterPort2 extends BlockFissionFluidMetaPort<TileFissionHeaterPort, TileSaltFissionHeater, MetaEnums.CoolantHeaterType2> {
	
	public final static PropertyEnum<MetaEnums.CoolantHeaterType2> TYPE = PropertyEnum.create("type", MetaEnums.CoolantHeaterType2.class);
	
	public BlockFissionHeaterPort2() {
		super(TileFissionHeaterPort.class, MetaEnums.CoolantHeaterType2.class, TYPE);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, TYPE, AXIS_ALL, ACTIVE);
	}
	
	@Override
	public boolean getActualStateActive(TileFissionHeaterPort port) {
		return port.getTankSorption(EnumFacing.DOWN, 0) != TankSorption.IN;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
        return switch (metadata) {
            case 0 -> new TileFissionHeaterPort.Tin();
            case 1 -> new TileFissionHeaterPort.Lead();
            case 2 -> new TileFissionHeaterPort.Boron();
            case 3 -> new TileFissionHeaterPort.Lithium();
            case 4 -> new TileFissionHeaterPort.Magnesium();
            case 5 -> new TileFissionHeaterPort.Manganese();
            case 6 -> new TileFissionHeaterPort.Aluminum();
            case 7 -> new TileFissionHeaterPort.Silver();
            case 8 -> new TileFissionHeaterPort.Fluorite();
            case 9 -> new TileFissionHeaterPort.Villiaumite();
            case 10 -> new TileFissionHeaterPort.Carobbiite();
            case 11 -> new TileFissionHeaterPort.Arsenic();
            case 12 -> new TileFissionHeaterPort.LiquidNitrogen();
            case 13 -> new TileFissionHeaterPort.LiquidHelium();
            case 14 -> new TileFissionHeaterPort.Enderium();
            case 15 -> new TileFissionHeaterPort.Cryotheum();
            default -> new TileFissionHeaterPort.Tin();
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
