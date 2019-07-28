package nc.item.tool;

import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import nc.util.InfoHelper;
import nc.util.OreDictHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class NCSpaxelhoe extends ItemTool {
	
	String[] info;
	
	public NCSpaxelhoe(ToolMaterial material, String... tooltip) {
		super(3F, -2.4F, material, new ObjectOpenHashSet<Block>());
		info = InfoHelper.buildInfo(getTranslationKey(), tooltip);
	}
	
	@Override
	public boolean canHarvestBlock(IBlockState state) {
		return true;
	}
	
	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state) {
		return efficiency;
	}
	
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		stack.damageItem(1, attacker);
		return true;
	}
	
	@Override
	public int getHarvestLevel(ItemStack stack, String toolClass, @Nullable EntityPlayer player, @Nullable IBlockState blockState) {
		return toolMaterial.getHarvestLevel();
	}
	
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		ItemStack mat = toolMaterial.getRepairItemStack();
		return mat != null && !mat.isEmpty() && OreDictHelper.isOreMatching(mat, repair);
	}
	
	// Hoe
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack itemstack = player.getHeldItem(hand);
		
		if (!player.canPlayerEdit(pos.offset(facing), facing, itemstack)) {
			return EnumActionResult.FAIL;
		} else {
			int hook = net.minecraftforge.event.ForgeEventFactory.onHoeUse(itemstack, player, worldIn, pos);
			if (hook != 0) return hook > 0 ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
			
			IBlockState iblockstate = worldIn.getBlockState(pos);
			Block block = iblockstate.getBlock();
			
			if (facing != EnumFacing.DOWN && worldIn.isAirBlock(pos.up())) {
				if (block == Blocks.GRASS || block == Blocks.GRASS_PATH) {
					this.setBlock(itemstack, player, worldIn, pos, Blocks.FARMLAND.getDefaultState());
					return EnumActionResult.SUCCESS;
				}
				
				if (block == Blocks.DIRT) {
					switch (iblockstate.getValue(BlockDirt.VARIANT)) {
					case DIRT:
						this.setBlock(itemstack, player, worldIn, pos, Blocks.FARMLAND.getDefaultState());
						return EnumActionResult.SUCCESS;
					case COARSE_DIRT:
						this.setBlock(itemstack, player, worldIn, pos, Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));
						return EnumActionResult.SUCCESS;
					default:
						break;
					}
				}
			}
			
			return EnumActionResult.PASS;
		}
	}
	 
	 protected void setBlock(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos, IBlockState state) {
		worldIn.playSound(player, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1F, 1F);
		
		if (!worldIn.isRemote) {
			worldIn.setBlockState(pos, state, 11);
			stack.damageItem(1, player);
		}
	}
}
