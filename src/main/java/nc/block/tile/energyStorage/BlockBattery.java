package nc.block.tile.energyStorage;

import nc.block.tile.BlockInventory;
import nc.proxy.CommonProxy;
import nc.tile.energyStorage.IBattery;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class BlockBattery extends BlockInventory {

	public BlockBattery(String unlocalizedName, String registryName) {
		super(unlocalizedName, registryName, Material.IRON);
		setHarvestLevel("pickaxe", 0);
		setHardness(2);
		setResistance(15);
		setCreativeTab(CommonProxy.TAB_MACHINES);
	}
	
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player != null) {
			TileEntity tileentity = world.getTileEntity(pos);
			if (tileentity instanceof IBattery) {
				if ((player.getHeldItemMainhand().isEmpty() || !player.isSneaking()) && world.isRemote) player.sendMessage(new TextComponentString("Energy Stored: " + ((IBattery) tileentity).getBatteryStorage().getEnergyStored() + " / " + ((IBattery) tileentity).getBatteryStorage().getMaxEnergyStored() + " RF"));
			}
		}
		return true;
	}
	
	public static void update(World world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile != null) {
			tile.validate();
			world.setTileEntity(pos, tile);
		}
	}
}
