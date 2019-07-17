package nc.block;

import nc.Global;
import nc.NuclearCraft;
import nc.tab.NCTabs;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class NCBlockTrapDoor extends BlockTrapDoor {
	
	public NCBlockTrapDoor(String name, Material material) {
		super(material);
		setTranslationKey(Global.MOD_ID + "." + name);
		if (NuclearCraft.regName) setRegistryName(new ResourceLocation(Global.MOD_ID, name));
		setCreativeTab(NCTabs.FISSION_BLOCKS);
		setHarvestLevel("pickaxe", 0);
		setHardness(2F);
		setResistance(15F);
	}
	
	@Override
	public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, net.minecraft.entity.EntityLiving.SpawnPlacementType type) {
		return false;
	}
}
