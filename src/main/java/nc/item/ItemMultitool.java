package nc.item;

import static nc.config.NCConfig.quantum_angle_precision;

import nc.tile.IMultitoolLogic;
import nc.util.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.*;

public class ItemMultitool extends NCItem {
	
	public ItemMultitool(String... tooltip) {
		super(tooltip);
	}
	
	public static boolean isMultitool(ItemStack stack) {
		return stack.isEmpty() ? false : stack.getItem() instanceof ItemMultitool;
	}
	
	protected static void clearNBT(ItemStack stack) {
		stack.setTagCompound(new NBTTagCompound());
	}
	
	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if (isMultitool(stack)) {
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof IMultitoolLogic) {
				if (!world.isRemote) {
					if (stack.getTagCompound() == null) {
						clearNBT(stack);
					}
					NBTTagCompound nbt = stack.getTagCompound();
					
					boolean multitoolUsed = ((IMultitoolLogic) tile).onUseMultitool(stack, player, world, facing, hitX, hitY, hitZ);
					nbt.setBoolean("multitoolUsed", multitoolUsed);
					
					tile.markDirty();
					
					if (multitoolUsed) {
						return EnumActionResult.SUCCESS;
					}
				}
			}
		}
		return super.onItemUseFirst(player, world, pos, facing, hitX, hitY, hitZ, hand);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if (stack.getTagCompound() == null) {
			stack.setTagCompound(new NBTTagCompound());
		}
		if (!world.isRemote && isMultitool(stack)) {
			NBTTagCompound nbt = stack.getTagCompound();
			if (!player.isSneaking() && nbt.getString("gateMode").equals("angle")) {
				double angle = NCMath.roundTo(player.rotationYaw + 360D, 360D / quantum_angle_precision) % 360D;
				nbt.setDouble("gateAngle", angle);
				player.sendMessage(new TextComponentString(Lang.localise("info.nuclearcraft.multitool.quantum_computer.tool_set_angle", NCMath.decimalPlaces(angle, 5))));
				return actionResult(true, stack);
			}
			else if (player.isSneaking() && !nbt.isEmpty() && !nbt.getBoolean("multitoolUsed")) {
				RayTraceResult raytraceresult = rayTrace(world, player, false);
				if (raytraceresult == null || raytraceresult.typeOfHit != RayTraceResult.Type.BLOCK) {
					return actionResult(false, stack);
				}
				
				BlockPos pos = raytraceresult.getBlockPos();
				TileEntity tile = world.getTileEntity(pos);
				if (!(tile instanceof IMultitoolLogic)) {
					clearNBT(stack);
					player.sendMessage(new TextComponentString(Lang.localise("info.nuclearcraft.multitool.clear_info")));
					return actionResult(true, stack);
				}
			}
			stack.getTagCompound().removeTag("multitoolUsed");
		}
		return super.onItemRightClick(world, player, hand);
	}
	
	@Override
	public boolean doesSneakBypassUse(ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player) {
		// return world.getTileEntity(pos) instanceof IMultitoolLogic;
		return false;
	}
}
