package nc.item.tool;

import java.util.List;

import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import nc.item.IInfoItem;
import nc.util.*;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.relauncher.*;

public class NCSpaxelhoe extends ItemTool implements IInfoItem {
	
	private final TextFormatting infoColor;
	private final String[] tooltip;
	public String[] info;
	
	public NCSpaxelhoe(ToolMaterial material, TextFormatting infoColor, String... tooltip) {
		super(3F, -2.4F, material, new ObjectOpenHashSet<>());
		this.infoColor = infoColor;
		this.tooltip = tooltip;
	}
	
	@Override
	public void setInfo() {
		info = InfoHelper.buildInfo(getTranslationKey(), tooltip);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, @Nullable World world, List<String> currentTooltip, ITooltipFlag flag) {
		super.addInformation(itemStack, world, currentTooltip, flag);
		if (info.length > 0) {
			InfoHelper.infoFull(currentTooltip, TextFormatting.RED, InfoHelper.EMPTY_ARRAY, infoColor, info);
		}
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
		ItemStack stack = player.getHeldItem(hand);
		
		if (!player.canPlayerEdit(pos.offset(facing), facing, stack)) {
			return EnumActionResult.FAIL;
		}
		else {
			int hook = ForgeEventFactory.onHoeUse(stack, player, worldIn, pos);
			if (hook != 0) {
				return hook > 0 ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
			}
			
			IBlockState state = worldIn.getBlockState(pos);
			Block block = state.getBlock();
			
			if (facing != EnumFacing.DOWN && worldIn.isAirBlock(pos.up())) {
				if (block == Blocks.GRASS || block == Blocks.GRASS_PATH) {
					tillBlock(stack, player, worldIn, pos, Blocks.FARMLAND.getDefaultState());
					return EnumActionResult.SUCCESS;
				}
				else if (block == Blocks.DIRT) {
					switch (state.getValue(BlockDirt.VARIANT)) {
						case DIRT:
							tillBlock(stack, player, worldIn, pos, Blocks.FARMLAND.getDefaultState());
							return EnumActionResult.SUCCESS;
						case COARSE_DIRT:
							tillBlock(stack, player, worldIn, pos, Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));
							return EnumActionResult.SUCCESS;
						default:
							break;
					}
				}
			}
			
			return EnumActionResult.PASS;
		}
	}
	
	protected void tillBlock(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos, IBlockState state) {
		worldIn.playSound(player, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1F, 1F);
		
		if (!worldIn.isRemote) {
			worldIn.setBlockState(pos, state, 11);
			stack.damageItem(1, player);
		}
	}
}
