package nc.block;

import java.util.Random;

import javax.annotation.Nullable;

import nc.Global;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class NCBlockDoor extends BlockDoor {
	
	public final Item doorDrop;
	
	public NCBlockDoor(String unlocalizedName, String registryName, Material material, Item doorItem) {
		super(material);
		setUnlocalizedName(unlocalizedName);
		setRegistryName(new ResourceLocation(Global.MOD_ID, registryName));
		doorDrop = doorItem;
	}
	
	public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, net.minecraft.entity.EntityLiving.SpawnPlacementType type) {
		return false;
	}
	
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(doorDrop);
	}

	@Nullable
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return state.getValue(HALF) == BlockDoor.EnumDoorHalf.UPPER ? null : doorDrop;
	}
}
