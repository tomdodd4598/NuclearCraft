package nc.item;

import mcp.MethodsReturnNonnullByDefault;
import nc.init.NCItems;
import nc.tile.fluid.ITileFluid;
import nc.tile.inventory.ITileInventory;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ItemConfigurationBlueprint extends NCItem {

    boolean isEmpty;

    public ItemConfigurationBlueprint(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isEmpty) {
            super.getSubItems(tab, items);
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (!worldIn.isRemote && playerIn.isSneaking()) {
            playSoundAt(playerIn, SoundEvents.BLOCK_LEVER_CLICK, 0.35F, 1.0F);
            return new ActionResult<>(EnumActionResult.SUCCESS, new ItemStack(NCItems.configuration_blueprint_empty));
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        if (world.isRemote && !player.isSneaking()) {
            return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
        }
        TileEntity te = world.getTileEntity(pos);
        ITileInventory items = te instanceof ITileInventory ? (ITileInventory) te : null;
        ITileFluid fluids = te instanceof ITileFluid ? (ITileFluid) te : null;
        if (items == null || !items.hasConfigurableInventoryConnections()) {
            return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
        }
        if (player.isSneaking()) {
            playSoundAt(player, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 0.35F, 1.0F);
            ItemStack stack = new ItemStack(NCItems.configuration_blueprint);
            NBTTagCompound stackNbt = new NBTTagCompound();
            stackNbt.setTag("inventory_connections", items.writeInventoryConnections(new NBTTagCompound()));
            stackNbt.setTag("slot_settings", items.writeSlotSettings(new NBTTagCompound()));
            stackNbt.setBoolean("redstone_control", items.getRedstoneControl());
            if (fluids != null && fluids.hasConfigurableFluidConnections()) {
                stackNbt.setTag("fluid_connections", fluids.writeFluidConnections(new NBTTagCompound()));
                stackNbt.setTag("tank_settings", fluids.writeTankSettings(new NBTTagCompound()));
            }
            stackNbt.setString("name", ((items.getName())));
            stackNbt.setString("class", items.getClass().getName());
            stack.setTagCompound(stackNbt);
            player.setHeldItem(hand, stack);
            return EnumActionResult.SUCCESS;
        }
        else if (!this.isEmpty) {
            NBTTagCompound nbt = player.getHeldItem(hand).getTagCompound();
            if (nbt != null && nbt.getString("class").equals(items.getClass().getName())) {
                playSoundAt(player, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 0.35F, 0.9F);
                items.readInventoryConnections(nbt.getCompoundTag("inventory_connections"));
                items.readSlotSettings(nbt.getCompoundTag("slot_settings"));
                items.setRedstoneControl(nbt.getBoolean("redstone_control"));
                if (fluids != null && nbt.hasKey("fluid_connections")) {
                    fluids.readFluidConnections(nbt.getCompoundTag("fluid_connections"));
                    fluids.readTankSettings(nbt.getCompoundTag("tank_settings"));
                }
                items.markDirtyAndNotify();
            }
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.FAIL;
    }

    @Override
    public void addInformation(ItemStack itemStack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        super.addInformation(itemStack, world, tooltip, flag);
        NBTTagCompound nbt = itemStack.getTagCompound();
        if (nbt != null && nbt.hasKey("name")) {
            tooltip.add(I18n.format(nbt.getString("name")));
        }
    }

    private static void playSoundAt(EntityPlayer player, SoundEvent sound, float volume, float pitch) {
        player.world.playSound(null, player.posX, player.posY, player.posZ, sound, SoundCategory.PLAYERS, volume, pitch);
    }
}
