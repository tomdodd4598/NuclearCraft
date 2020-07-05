package nc.block;

import java.util.Random;

import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;

public abstract class NCBlockDoor extends BlockDoor {
	
	public NCBlockDoor(Material material) {
		super(material);
		setHarvestLevel("pickaxe", 0);
		setHardness(2F);
		setResistance(15F);
	}
	
	@Override
	public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, net.minecraft.entity.EntityLiving.SpawnPlacementType type) {
		return false;
	}
	
	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(getDoorItem());
	}
	
	protected abstract Item getDoorItem();
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return state.getValue(HALF) == BlockDoor.EnumDoorHalf.UPPER ? Items.AIR : getDoorItem();
	}
}
