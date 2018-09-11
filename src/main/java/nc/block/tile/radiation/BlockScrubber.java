package nc.block.tile.radiation;

import nc.block.tile.BlockSimpleTile;
import nc.config.NCConfig;
import nc.enumm.BlockEnums.SimpleTileType;
import nc.tile.radiation.TileScrubber;
import nc.util.UnitHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class BlockScrubber extends BlockSimpleTile {

	public BlockScrubber() {
		super(SimpleTileType.RADIATION_SCRUBBER);
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (hand != EnumHand.MAIN_HAND) return false;
		
		if (player != null) {
			if (player.getHeldItemMainhand().isEmpty() && world.getTileEntity(pos) instanceof TileScrubber) {
				if (!world.isRemote) {
					double radRemoval = ((TileScrubber) world.getTileEntity(pos)).getChunkBufferContribution();
					player.sendMessage(new TextComponentString((Math.abs(radRemoval) < NCConfig.radiation_lowest_rate ? "0 Rads/t" : UnitHelper.prefix(radRemoval, 3, "Rads/t", 0, -8)) + " [" + Math.round(-100D*radRemoval/TileScrubber.maxScrubberRate) + "%]"));
				}
				return true;
			}
		}
		return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
	}
}
