package nc.block.tile;

import nc.enumm.BlockEnums.SimpleTileType;
import nc.tile.energy.IBattery;
import nc.tile.internal.EnergyStorage;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class BlockBattery extends BlockSimpleTile {
	
	public BlockBattery(SimpleTileType type) {
		super(type);
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player != null) {
			if (world.getTileEntity(pos) instanceof IBattery) {
				EnergyStorage storage = ((IBattery) world.getTileEntity(pos)).getBatteryStorage();
				if (!player.isSneaking() && !world.isRemote) player.sendMessage(new TextComponentString("Energy Stored: " + storage.getEnergyStored() + " / " + storage.getMaxEnergyStored() + " RF"));
			}
		}
		return true;
	}
}
