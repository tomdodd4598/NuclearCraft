package nc.block.tile.radiation;

import nc.block.tile.BlockSimpleTile;
import nc.config.NCConfig;
import nc.enumm.BlockEnums.SimpleTileType;
import nc.radiation.RadiationHelper;
import nc.tile.radiation.TileGeigerCounter;
import nc.util.Lang;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class BlockGeigerCounter extends BlockSimpleTile {
	
	private static final String RADIATION = Lang.localise("item.nuclearcraft.geiger_counter.rads");

	public BlockGeigerCounter() {
		super(SimpleTileType.GEIGER_BLOCK);
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (hand != EnumHand.MAIN_HAND) return false;
		
		if (player != null && player.getHeldItem(hand).isEmpty()) {
			TileEntity tile = world.getTileEntity(pos);
			if (!world.isRemote && tile instanceof TileGeigerCounter) {
				TileGeigerCounter geiger = (TileGeigerCounter) tile;
				double radiation = geiger.getChunkRadiationLevel();
				player.sendMessage(new TextComponentString(RADIATION + " " + RadiationHelper.getRadiationTextColor(radiation) + (radiation < NCConfig.radiation_lowest_rate ? "0 Rad/t" : RadiationHelper.radsPrefix(radiation, true))));
			}
			return true;
		}
		return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
	}
	
	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}
	
	@Override
	public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileGeigerCounter) {
			return ((TileGeigerCounter)tile).comparatorStrength;
		}
		return 0;
	}
}
