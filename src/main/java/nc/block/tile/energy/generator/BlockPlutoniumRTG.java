package nc.block.tile.energy.generator;

import nc.block.tile.BlockInventory;
import nc.proxy.CommonProxy;
import nc.tile.generator.RTGs;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockPlutoniumRTG extends BlockInventory {

	public BlockPlutoniumRTG(String unlocalizedName, String registryName) {
		super(unlocalizedName, registryName, Material.IRON);
		setCreativeTab(CommonProxy.TAB_MACHINES);
	}
	
	public TileEntity createNewTileEntity(World world, int meta) {
		return new RTGs.PlutoniumRTG();
	}
}
