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

public class BlockFissionHeaterPort extends BlockFissionFluidMetaPort<TileFissionHeaterPort, TileSaltFissionHeater, MetaEnums.CoolantHeaterType> {
	
	public final static PropertyEnum<MetaEnums.CoolantHeaterType> TYPE = PropertyEnum.create("type", MetaEnums.CoolantHeaterType.class);
	
	public BlockFissionHeaterPort() {
		super(TileFissionHeaterPort.class, MetaEnums.CoolantHeaterType.class, TYPE);
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
            case 0 -> new TileFissionHeaterPort.Standard();
            case 1 -> new TileFissionHeaterPort.Iron();
            case 2 -> new TileFissionHeaterPort.Redstone();
            case 3 -> new TileFissionHeaterPort.Quartz();
            case 4 -> new TileFissionHeaterPort.Obsidian();
            case 5 -> new TileFissionHeaterPort.NetherBrick();
            case 6 -> new TileFissionHeaterPort.Glowstone();
            case 7 -> new TileFissionHeaterPort.Lapis();
            case 8 -> new TileFissionHeaterPort.Gold();
            case 9 -> new TileFissionHeaterPort.Prismarine();
            case 10 -> new TileFissionHeaterPort.Slime();
            case 11 -> new TileFissionHeaterPort.EndStone();
            case 12 -> new TileFissionHeaterPort.Purpur();
            case 13 -> new TileFissionHeaterPort.Diamond();
            case 14 -> new TileFissionHeaterPort.Emerald();
            case 15 -> new TileFissionHeaterPort.Copper();
            default -> new TileFissionHeaterPort.Standard();
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
