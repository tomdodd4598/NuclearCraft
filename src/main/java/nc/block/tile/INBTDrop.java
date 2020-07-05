package nc.block.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;

public interface INBTDrop {
	
	public ItemStack getNBTDrop(IBlockAccess world, BlockPos pos, IBlockState state);
	
	public void readStackData(World world, BlockPos pos, EntityLivingBase player, ItemStack stack);
}
