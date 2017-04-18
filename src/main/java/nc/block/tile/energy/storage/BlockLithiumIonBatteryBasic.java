package nc.block.tile.energy.storage;

import nc.tile.energy.storage.TileBatteries;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockLithiumIonBatteryBasic extends BlockBattery {

	public BlockLithiumIonBatteryBasic(String unlocalizedName, String registryName) {
		super(unlocalizedName, registryName);
	}
	
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileBatteries.LithiumIonBatteryBasic();
	}
}
