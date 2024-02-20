package nc.block.fission;

import nc.enumm.MetaEnums;
import nc.tile.fission.TileSolidFissionSink;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockSolidFissionMetaSink extends BlockFissionMetaPart<MetaEnums.HeatSinkType> {
	
	public final static PropertyEnum<MetaEnums.HeatSinkType> TYPE = PropertyEnum.create("type", MetaEnums.HeatSinkType.class);
	
	public BlockSolidFissionMetaSink() {
		super(MetaEnums.HeatSinkType.class, TYPE);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, TYPE);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
        return switch (metadata) {
            case 0 -> new TileSolidFissionSink.Water();
            case 1 -> new TileSolidFissionSink.Iron();
            case 2 -> new TileSolidFissionSink.Redstone();
            case 3 -> new TileSolidFissionSink.Quartz();
            case 4 -> new TileSolidFissionSink.Obsidian();
            case 5 -> new TileSolidFissionSink.NetherBrick();
            case 6 -> new TileSolidFissionSink.Glowstone();
            case 7 -> new TileSolidFissionSink.Lapis();
            case 8 -> new TileSolidFissionSink.Gold();
            case 9 -> new TileSolidFissionSink.Prismarine();
            case 10 -> new TileSolidFissionSink.Slime();
            case 11 -> new TileSolidFissionSink.EndStone();
            case 12 -> new TileSolidFissionSink.Purpur();
            case 13 -> new TileSolidFissionSink.Diamond();
            case 14 -> new TileSolidFissionSink.Emerald();
            case 15 -> new TileSolidFissionSink.Copper();
            default -> new TileSolidFissionSink.Water();
        };
    }
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (hand != EnumHand.MAIN_HAND || player.isSneaking()) {
			return false;
		}
		return rightClickOnPart(world, pos, player, hand, facing);
	}
}
