package nc.block.tile.radiation;

import static nc.config.NCConfig.radiation_lowest_rate;

import nc.block.tile.BlockSimpleTile;
import nc.radiation.RadiationHelper;
import nc.tile.radiation.TileRadiationScrubber;
import nc.util.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class BlockScrubber extends BlockSimpleTile {
	
	public BlockScrubber(String name) {
		super(name);
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (hand != EnumHand.MAIN_HAND) {
			return false;
		}
		
		if (player != null && player.getHeldItem(hand).isEmpty()) {
			TileEntity tile = world.getTileEntity(pos);
			if (!world.isRemote && tile instanceof TileRadiationScrubber) {
				TileRadiationScrubber scrubber = (TileRadiationScrubber) tile;
				scrubber.checkRadiationEnvironmentInfo();
				double radRemoval = scrubber.getRawScrubberRate();
				player.sendMessage(new TextComponentString(Lang.localise("message.nuclearcraft.scrubber_removal_rate") + " " + (Math.abs(radRemoval) < radiation_lowest_rate ? "0 Rad/t" : RadiationHelper.radsPrefix(radRemoval, true)) + " [" + NCMath.pcDecimalPlaces(Math.abs(scrubber.getRadiationContributionFraction() / TileRadiationScrubber.getMaxScrubberFraction()), 1) + "]"));
			}
			return true;
		}
		return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
	}
}
