package nc.block.tile.passive;

import nc.block.tile.BlockInventory;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockPassive extends BlockInventory {
	
	protected final boolean isActive;
	
	public BlockPassive(String unlocalizedName, String registryName, boolean isActive, CreativeTabs tab) {
		super(unlocalizedName, registryName, Material.IRON);
		this.isActive = isActive;
		setHarvestLevel("pickaxe", 0);
		setHardness(2);
		setResistance(15);
		if (!isActive) setCreativeTab(tab);
	}
}
