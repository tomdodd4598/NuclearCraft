package nc.block.tile.energy.generator;

import nc.block.tile.BlockSidedInventory;
import nc.proxy.CommonProxy;
import net.minecraft.block.material.Material;

public class BlockFissionPort extends BlockSidedInventory {
	
	public BlockFissionPort(String unlocalizedName, String registryName) {
		super(unlocalizedName, registryName, Material.IRON);
		setHarvestLevel("pickaxe", 0);
		setHardness(2);
		setResistance(15);
		setCreativeTab(CommonProxy.TAB_FISSION_BLOCKS);
	}
}
