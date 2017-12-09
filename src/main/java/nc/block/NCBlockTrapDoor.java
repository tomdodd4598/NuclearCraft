package nc.block;

import nc.Global;
import nc.proxy.CommonProxy;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class NCBlockTrapDoor extends BlockTrapDoor {
	
	public NCBlockTrapDoor(String unlocalizedName, String registryName, Material material) {
		super(material);
		setUnlocalizedName(unlocalizedName);
		setRegistryName(new ResourceLocation(Global.MOD_ID, registryName));
		setCreativeTab(CommonProxy.TAB_FISSION_BLOCKS);
		setHarvestLevel("pickaxe", 0);
		setHardness(2);
		setResistance(15);
	}
	
	public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, net.minecraft.entity.EntityLiving.SpawnPlacementType type) {
		return false;
	}
}
