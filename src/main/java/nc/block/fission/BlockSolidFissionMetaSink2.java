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

public class BlockSolidFissionMetaSink2 extends BlockFissionMetaPart<MetaEnums.HeatSinkType2> {
	
	public final static PropertyEnum<MetaEnums.HeatSinkType2> TYPE = PropertyEnum.create("type", MetaEnums.HeatSinkType2.class);
	
	public BlockSolidFissionMetaSink2() {
		super(MetaEnums.HeatSinkType2.class, TYPE);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, TYPE);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
        return switch (metadata) {
            case 0 -> new TileSolidFissionSink.Tin();
            case 1 -> new TileSolidFissionSink.Lead();
            case 2 -> new TileSolidFissionSink.Boron();
            case 3 -> new TileSolidFissionSink.Lithium();
            case 4 -> new TileSolidFissionSink.Magnesium();
            case 5 -> new TileSolidFissionSink.Manganese();
            case 6 -> new TileSolidFissionSink.Aluminum();
            case 7 -> new TileSolidFissionSink.Silver();
            case 8 -> new TileSolidFissionSink.Fluorite();
            case 9 -> new TileSolidFissionSink.Villiaumite();
            case 10 -> new TileSolidFissionSink.Carobbiite();
            case 11 -> new TileSolidFissionSink.Arsenic();
            case 12 -> new TileSolidFissionSink.LiquidNitrogen();
            case 13 -> new TileSolidFissionSink.LiquidHelium();
            case 14 -> new TileSolidFissionSink.Enderium();
            case 15 -> new TileSolidFissionSink.Cryotheum();
            default -> new TileSolidFissionSink.Tin();
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
