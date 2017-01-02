package nc.itemblock.storage;

import nc.NuclearCraft;
import net.minecraft.block.Block;

public class ItemBlockLithiumIonBattery extends ItemBlockEnergyStorage {

	public ItemBlockLithiumIonBattery(Block block) {
		super(block, NuclearCraft.lithiumIonRF*10);
	}
}
