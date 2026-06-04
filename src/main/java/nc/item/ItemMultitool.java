package nc.item;

import buildcraft.api.tools.IToolWrench;
import cofh.api.item.IToolHammer;
import nc.config.NCConfig;
import nc.tile.IMultitoolLogic;
import nc.util.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.*;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.Optional.*;
import net.minecraftforge.fml.relauncher.*;

import javax.annotation.*;
import java.util.*;

@InterfaceList({@Interface(iface = "buildcraft.api.tools.IToolWrench", modid = "buildcraftcore"), @Interface(iface = "cofh.api.item.IToolHammer", modid = "cofhapi")})
public class ItemMultitool extends NCItem implements IToolWrench, IToolHammer {
	
	public ItemMultitool(String... tooltip) {
		super(tooltip);
		maxStackSize = 1;
	}
	
	public static ItemStack newMultitool(Item item) {
		ItemStack stack = new ItemStack(item);
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setTag("ncMultitool", new NBTTagCompound());
		stack.setTagCompound(nbt);
		return stack;
	}
	
	public static boolean isMultitool(ItemStack stack) {
		return !stack.isEmpty() && stack.getItem() instanceof ItemMultitool;
	}
	
	public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
		if (isInCreativeTab(tab)) {
			items.add(newMultitool(this));
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean isFull3D() {
		return true;
	}
	
	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if (isMultitool(stack)) {
			if (!world.isRemote) {
				TileEntity tile = world.getTileEntity(pos);
				if (tile instanceof IMultitoolLogic multitoolTile) {
					NBTTagCompound nbt = NBTHelper.getStackNBT(stack, "ncMultitool");
					if (nbt != null) {
						boolean multitoolUsed = multitoolTile.onUseMultitool(stack, (EntityPlayerMP) player, world, facing, hitX, hitY, hitZ);
						nbt.setBoolean("multitoolUsed", multitoolUsed);
						if (multitoolUsed) {
							multitoolTile.markDirtyAndNotify(true);
							playUseSound(world, player);
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
				ActionResult<ItemStack> result = null;
				NBTTagCompound nbt = NBTHelper.getStackNBT(stack, "ncMultitool");
				if (nbt != null) {
					for (MultitoolRightClickLogic logic : MULTITOOL_RIGHT_CLICK_LOGIC) {
						result = logic.onRightClick(this, world, player, hand, stack);
						if (result != null) {
							playUseSound(world, player);
							break;
						}
					}
					nbt.removeTag("multitoolUsed");
				}
				
				if (result != null) {
					return result;
				}
			}
		}
		return super.onItemRightClick(world, player, hand);
	}
	
	public static void playUseSound(World world, EntityPlayer player) {
		world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.5F, player.isSneaking() ? 1F : SoundHelper.getPitch(-1D));
	}
	
	@Override
	public boolean doesSneakBypassUse(ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player) {
		// return world.getTileEntity(pos) instanceof IMultitoolLogic;
		return false;
	}
	
	// IToolWrench
	
	@Override
	@Optional.Method(modid = "buildcraftcore")
	public boolean canWrench(EntityPlayer player, EnumHand hand, ItemStack wrench, RayTraceResult ray) {
		return isMultitool(wrench);
	}
	
	@Override
	@Optional.Method(modid = "buildcraftcore")
	public void wrenchUsed(EntityPlayer player, EnumHand hand, ItemStack wrench, RayTraceResult ray) {}
	
	// IToolHammer
	
	@Override
	@Optional.Method(modid = "cofhapi")
	public boolean isUsable(ItemStack item, EntityLivingBase user, BlockPos pos) {
		return isMultitool(item);
	}
	
	@Override
	@Optional.Method(modid = "cofhapi")
	public boolean isUsable(ItemStack item, EntityLivingBase user, Entity entity) {
		return isMultitool(item);
	}
	
	@Override
	@Optional.Method(modid = "cofhapi")
	public void toolUsed(ItemStack item, EntityLivingBase user, BlockPos pos) {}
	
	@Override
	@Optional.Method(modid = "cofhapi")
	public void toolUsed(ItemStack item, EntityLivingBase user, Entity entity) {}
	
	// Right click logic
	
	public interface MultitoolRightClickLogic {
		
		@Nullable
		ActionResult<ItemStack> onRightClick(ItemMultitool itemMultitool, World world, EntityPlayer player, EnumHand hand, ItemStack heldItem);
	}
	
	/**
	 * List of all multitool right-click logic. Earlier entries are prioritised!
	 */
	public static final List<MultitoolRightClickLogic> MULTITOOL_RIGHT_CLICK_LOGIC = new LinkedList<>();
	
	public static void registerRightClickLogic() {
		MULTITOOL_RIGHT_CLICK_LOGIC.add((itemMultitool, world, player, hand, heldItem) -> {
			NBTTagCompound nbt = NBTHelper.getStackNBT(heldItem, "ncMultitool");
			if (nbt != null && !player.isSneaking() && nbt.getString("qComputerGateMode").equals("angle")) {
				double angle = NCMath.roundTo(player.rotationYaw + 360D, 360D / NCConfig.quantum_angle_precision) % 360D;
				nbt.setDouble("qGateAngle", angle);
				player.sendMessage(new TextComponentString(Lang.localize("info.nuclearcraft.multitool.quantum_computer.tool_set_angle", NCMath.decimalPlaces(angle, 5))));
				return itemMultitool.actionResult(true, heldItem);
			}
			return null;
		});
		
		MULTITOOL_RIGHT_CLICK_LOGIC.add((itemMultitool, world, player, hand, heldItem) -> {
			NBTTagCompound nbt = NBTHelper.getStackNBT(heldItem, "ncMultitool");
			if (nbt != null && player.isSneaking() && !nbt.isEmpty() && !nbt.getBoolean("multitoolUsed")) {
				RayTraceResult ray = itemMultitool.rayTrace(world, player, false);
				if (ray == null || ray.typeOfHit != RayTraceResult.Type.BLOCK || !(world.getTileEntity(ray.getBlockPos()) instanceof IMultitoolLogic)) {
					NBTHelper.clearStackNBT(heldItem, "ncMultitool");
					player.sendMessage(new TextComponentString(Lang.localize("info.nuclearcraft.multitool.clear_info")));
					return itemMultitool.actionResult(true, heldItem);
				}
			}
			return null;
		});
	}
}
