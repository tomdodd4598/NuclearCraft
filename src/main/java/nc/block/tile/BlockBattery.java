package nc.block.tile;

import nc.enumm.BlockEnums.SimpleTileType;
import nc.tile.energyStorage.IBattery;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
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
			TileEntity tileentity = world.getTileEntity(pos);
			if (tileentity instanceof IBattery) {
				if ((player.getHeldItemMainhand().isEmpty() || !player.isSneaking()) && world.isRemote) player.sendMessage(new TextComponentString("Energy Stored: " + ((IBattery) tileentity).getBatteryStorage().getEnergyStored() + " / " + ((IBattery) tileentity).getBatteryStorage().getMaxEnergyStored() + " RF"));
			}
		}
		return true;
	}
}
