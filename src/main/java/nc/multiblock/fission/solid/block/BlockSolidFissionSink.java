package nc.multiblock.fission.solid.block;

import nc.enumm.MetaEnums;
import nc.multiblock.fission.block.BlockFissionMetaPart;
import nc.multiblock.fission.solid.tile.TileSolidFissionSink;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockSolidFissionSink extends BlockFissionMetaPart<MetaEnums.HeatSinkType> {
	
	public final static PropertyEnum TYPE = PropertyEnum.create("type", MetaEnums.HeatSinkType.class);
	
	public BlockSolidFissionSink() {
		super(MetaEnums.HeatSinkType.class, TYPE);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {TYPE});
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		switch(metadata) {
		case 0:
			return new TileSolidFissionSink.Water();
		case 1:
			return new TileSolidFissionSink.Iron();
		case 2:
			return new TileSolidFissionSink.Redstone();
		case 3:
			return new TileSolidFissionSink.Quartz();
		case 4:
			return new TileSolidFissionSink.Obsidian();
		case 5:
			return new TileSolidFissionSink.NetherBrick();
		case 6:
			return new TileSolidFissionSink.Glowstone();
		case 7:
			return new TileSolidFissionSink.Lapis();
		case 8:
			return new TileSolidFissionSink.Gold();
		case 9:
			return new TileSolidFissionSink.Prismarine();
		case 10:
			return new TileSolidFissionSink.Slime();
		case 11:
			return new TileSolidFissionSink.EndStone();
		case 12:
			return new TileSolidFissionSink.Purpur();
		case 13:
			return new TileSolidFissionSink.Diamond();
		case 14:
			return new TileSolidFissionSink.Emerald();
		case 15:
			return new TileSolidFissionSink.Copper();
		}
		return new TileSolidFissionSink.Water();
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player == null) return false;
		if (hand != EnumHand.MAIN_HAND || player.isSneaking()) return false;
		return rightClickOnPart(world, pos, player, hand, facing);
	}
}
