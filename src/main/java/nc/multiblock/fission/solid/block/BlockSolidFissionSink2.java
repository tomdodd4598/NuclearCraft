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

public class BlockSolidFissionSink2 extends BlockFissionMetaPart<MetaEnums.HeatSinkType2> {
	
	public final static PropertyEnum TYPE = PropertyEnum.create("type", MetaEnums.HeatSinkType2.class);
	
	public BlockSolidFissionSink2() {
		super(MetaEnums.HeatSinkType2.class, TYPE);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {TYPE});
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		switch(metadata) {
		case 0:
			return new TileSolidFissionSink.Tin();
		case 1:
			return new TileSolidFissionSink.Lead();
		case 2:
			return new TileSolidFissionSink.Boron();
		case 3:
			return new TileSolidFissionSink.Lithium();
		case 4:
			return new TileSolidFissionSink.Magnesium();
		case 5:
			return new TileSolidFissionSink.Manganese();
		case 6:
			return new TileSolidFissionSink.Aluminum();
		case 7:
			return new TileSolidFissionSink.Silver();
		case 8:
			return new TileSolidFissionSink.Fluorite();
		case 9:
			return new TileSolidFissionSink.Villiaumite();
		case 10:
			return new TileSolidFissionSink.Carobbiite();
		case 11:
			return new TileSolidFissionSink.Arsenic();
		case 12:
			return new TileSolidFissionSink.LiquidNitrogen();
		case 13:
			return new TileSolidFissionSink.LiquidHelium();
		case 14:
			return new TileSolidFissionSink.Enderium();
		case 15:
			return new TileSolidFissionSink.Cryotheum();
		}
		return new TileSolidFissionSink.Tin();
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player == null) return false;
		if (hand != EnumHand.MAIN_HAND || player.isSneaking()) return false;
		return rightClickOnPart(world, pos, player, hand, facing);
	}
}
