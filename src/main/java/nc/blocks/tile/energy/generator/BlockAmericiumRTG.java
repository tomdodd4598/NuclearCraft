package nc.blocks.tile.energy.generator;

import nc.blocks.tile.BlockInventory;
import nc.proxy.CommonProxy;
import nc.tile.energy.generator.TileRTGs;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockAmericiumRTG extends BlockInventory {

	public BlockAmericiumRTG(String unlocalizedName, String registryName) {
		super(unlocalizedName, registryName, Material.IRON);
		setCreativeTab(CommonProxy.NC_TAB);
	}
	
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileRTGs.AmericiumRTG();
	}
}
