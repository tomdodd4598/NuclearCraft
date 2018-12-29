package nc.block.tile.radiation;

import nc.block.tile.BlockSimpleTile;
import nc.config.NCConfig;
import nc.enumm.BlockEnums.SimpleTileType;
import nc.tile.radiation.TileGeigerCounter;
import nc.util.Lang;
import nc.util.RadiationHelper;
import nc.util.UnitHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
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
		
		if (player != null) {
			if (player.getHeldItemMainhand().isEmpty() && world.getTileEntity(pos) instanceof TileGeigerCounter) {
				if (!world.isRemote) {
					TileGeigerCounter geiger = (TileGeigerCounter) world.getTileEntity(pos);
					double radiation = geiger.getChunkRadiationLevel();
					player.sendMessage(new TextComponentString(RADIATION + " " + RadiationHelper.getRadiationTextColor(radiation) + (radiation < NCConfig.radiation_lowest_rate ? "0 Rads/t" : UnitHelper.prefix(radiation, 3, "Rads/t", 0, -8))));
				}
				return true;
			}
		}
		return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
	}
}
