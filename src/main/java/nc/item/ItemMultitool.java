package nc.item;

import static nc.config.NCConfig.quantum_angle_precision;

import java.util.*;

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
import net.minecraftforge.fml.relauncher.*;

public class ItemMultitool extends NCItem {
	
	/** List of all multitool right-click logic. Earlier entries are prioritised! */
	public static final List<MultitoolRightClickLogic> MULTITOOL_RIGHT_CLICK_LOGIC = new LinkedList<>();
	
	public ItemMultitool(String... tooltip) {
		super(tooltip);
		maxStackSize = 1;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean isFull3D() {
		return true;
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
					NBTTagCompound nbt = NBTHelper.getStackNBT(stack);
					
					if (nbt != null) {
						boolean multitoolUsed = ((IMultitoolLogic) tile).onUseMultitool(stack, player, world, facing, hitX, hitY, hitZ);
						nbt.setBoolean("multitoolUsed", multitoolUsed);
					
						tile.markDirty();
						
						if (multitoolUsed) {
							return EnumActionResult.SUCCESS;
						}
					}
				}
			}
		}
		return super.onItemUseFirst(player, world, pos, facing, hitX, hitY, hitZ, hand);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if (isMultitool(stack)) {
			if (!world.isRemote) {
				for (MultitoolRightClickLogic logic : MULTITOOL_RIGHT_CLICK_LOGIC) {
					ActionResult<ItemStack> result = logic.onRightClick(this, world, player, hand, stack);
					if (result != null) {
						return result;
					}
				}
			}
		}
		return super.onItemRightClick(world, player, hand);
	}
	
	@Override
	public boolean doesSneakBypassUse(ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player) {
		// return world.getTileEntity(pos) instanceof IMultitoolLogic;
		return false;
	}
	
	public abstract static class MultitoolRightClickLogic {
		
		public abstract ActionResult<ItemStack> onRightClick(ItemMultitool itemMultitool, World world, EntityPlayer player, EnumHand hand, ItemStack heldItem);
	}
	
	public static void registerRightClickLogic() {
		MULTITOOL_RIGHT_CLICK_LOGIC.add(new MultitoolRightClickLogic() {
			
			@Override
			public ActionResult<ItemStack> onRightClick(ItemMultitool itemMultitool, World world, EntityPlayer player, EnumHand hand, ItemStack heldItem) {
				NBTTagCompound nbt = NBTHelper.getStackNBT(heldItem);
				if (nbt != null && !player.isSneaking() && nbt.getString("gateMode").equals("angle")) {
					double angle = NCMath.roundTo(player.rotationYaw + 360D, 360D / quantum_angle_precision) % 360D;
					nbt.setDouble("gateAngle", angle);
					player.sendMessage(new TextComponentString(Lang.localise("info.nuclearcraft.multitool.quantum_computer.tool_set_angle", NCMath.decimalPlaces(angle, 5))));
					return itemMultitool.actionResult(true, heldItem);
				}
				return null;
			}
		});
		
		MULTITOOL_RIGHT_CLICK_LOGIC.add(new MultitoolRightClickLogic() {
			
			@Override
			public ActionResult<ItemStack> onRightClick(ItemMultitool itemMultitool, World world, EntityPlayer player, EnumHand hand, ItemStack heldItem) {
				NBTTagCompound nbt = NBTHelper.getStackNBT(heldItem);
				if (nbt != null && player.isSneaking() && !nbt.isEmpty() && !nbt.getBoolean("multitoolUsed")) {
					@SuppressWarnings("synthetic-access")
					RayTraceResult raytraceresult = itemMultitool.rayTrace(world, player, false);
					if (raytraceresult == null || raytraceresult.typeOfHit != RayTraceResult.Type.BLOCK) {
						return itemMultitool.actionResult(false, heldItem);
					}
					
					BlockPos pos = raytraceresult.getBlockPos();
					TileEntity tile = world.getTileEntity(pos);
					if (!(tile instanceof IMultitoolLogic)) {
						clearNBT(heldItem);
						player.sendMessage(new TextComponentString(Lang.localise("info.nuclearcraft.multitool.clear_info")));
						return itemMultitool.actionResult(true, heldItem);
					}
				}
				return null;
			}
		});
		
		MULTITOOL_RIGHT_CLICK_LOGIC.add(new MultitoolRightClickLogic() {
			
			@Override
			public ActionResult<ItemStack> onRightClick(ItemMultitool itemMultitool, World world, EntityPlayer player, EnumHand hand, ItemStack heldItem) {
				NBTTagCompound nbt = NBTHelper.getStackNBT(heldItem);
				if (nbt != null) {
					nbt.removeTag("multitoolUsed");
				}
				return null;
			}
		});
	}
}
