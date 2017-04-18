package nc.block.tile.energy.generator;

import nc.block.tile.BlockInventory;
import nc.proxy.CommonProxy;
import nc.tile.energy.generator.TileSolarPanels;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockSolarPanelBasic extends BlockInventory {

	public BlockSolarPanelBasic(String unlocalizedName, String registryName) {
		super(unlocalizedName, registryName, Material.IRON);
		setCreativeTab(CommonProxy.NC_TAB);
	}
	
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileSolarPanels.SolarPanelBasic();
	}
}
