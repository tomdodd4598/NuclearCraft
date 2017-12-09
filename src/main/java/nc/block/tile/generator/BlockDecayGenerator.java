package nc.block.tile.generator;

import nc.block.tile.BlockInventory;
import nc.proxy.CommonProxy;
import nc.tile.generator.TileDecayGenerator;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockDecayGenerator extends BlockInventory {

	public BlockDecayGenerator(String unlocalizedName, String registryName) {
		super(unlocalizedName, registryName, Material.IRON);
		setHarvestLevel("pickaxe", 0);
		setHardness(2);
		setResistance(15);
		setCreativeTab(CommonProxy.TAB_MACHINES);
	}
	
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileDecayGenerator();
	}
}
