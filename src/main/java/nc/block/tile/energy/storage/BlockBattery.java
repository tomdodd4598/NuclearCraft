package nc.block.tile.energy.storage;

import nc.block.tile.BlockInventory;
import nc.proxy.CommonProxy;
import nc.tile.energy.storage.TileBattery;
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
		setCreativeTab(CommonProxy.NC_TAB);
	}
	
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player != null) {
			TileEntity tileentity = world.getTileEntity(pos);
			if (tileentity instanceof TileBattery) {
				if ((player.getHeldItemMainhand().isEmpty() || !player.isSneaking()) && world.isRemote) player.sendMessage(new TextComponentString("Energy Stored: " + ((TileBattery) tileentity).storage.getEnergyStored() + " / " + ((TileBattery) tileentity).storage.getMaxEnergyStored() + " RF"));
			}
		}
		return true;
	}
}
