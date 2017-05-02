package nc.block.tile.generator;

import nc.block.tile.BlockInventory;
import nc.proxy.CommonProxy;
import nc.tile.generator.SolarPanels;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockSolarPanelBasic extends BlockInventory {

	public BlockSolarPanelBasic(String unlocalizedName, String registryName) {
		super(unlocalizedName, registryName, Material.IRON);
		setCreativeTab(CommonProxy.TAB_MACHINES);
	}
	
	public TileEntity createNewTileEntity(World world, int meta) {
		return new SolarPanels.SolarPanelBasic();
	}
}
