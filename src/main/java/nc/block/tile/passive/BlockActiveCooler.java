package nc.block.tile.passive;

import nc.block.tile.BlockInventory;
import nc.proxy.CommonProxy;
import nc.tile.fluid.TileActiveCooler;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockActiveCooler extends BlockInventory {
	
	public BlockActiveCooler(String unlocalizedName, String registryName) {
		super(unlocalizedName, registryName, Material.IRON);
		setHarvestLevel("pickaxe", 0);
		setHardness(2);
		setResistance(15);
		setCreativeTab(CommonProxy.TAB_MACHINES);
	}
	
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileActiveCooler();
	}
}
