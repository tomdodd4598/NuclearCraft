package nc.block.tile.generator;

import nc.block.tile.BlockInventory;
import nc.proxy.CommonProxy;
import nc.tile.generator.RTGs;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockUraniumRTG extends BlockInventory {

	public BlockUraniumRTG(String unlocalizedName, String registryName) {
		super(unlocalizedName, registryName, Material.IRON);
		setCreativeTab(CommonProxy.TAB_MACHINES);
	}
	
	public TileEntity createNewTileEntity(World world, int meta) {
		return new RTGs.UraniumRTG();
	}
}
