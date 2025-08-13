package nc.item;

import nc.init.NCItems;
import nc.tile.fluid.ITileFluid;
import nc.tile.inventory.ITileInventory;
import nc.tile.processor.IUpgradable;
import nc.util.Lang;
import nc.util.SoundHelper;
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
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemConfigurationBlueprint extends NCItem {

    boolean isEmpty;

    public ItemConfigurationBlueprint(boolean isEmpty) {
        this.isEmpty = isEmpty;
        this.setMaxStackSize(1);
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
            playSoundAt(playerIn, SoundEvents.BLOCK_LEVER_CLICK, 0.5F, 1F);
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
        EnumFacing facing = items.getFacingHorizontal();
        if (player.isSneaking()) {
            playSoundAt(player, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5F, 1.0F);
            ItemStack stack = new ItemStack(NCItems.configuration_blueprint);
            NBTTagCompound stackNbt = new NBTTagCompound();
            stackNbt.setTag("inventory_connections", items.writeInventoryConnectionsNormalized(new NBTTagCompound(), facing));
            stackNbt.setTag("slot_settings", items.writeSlotSettings(new NBTTagCompound()));
            stackNbt.setBoolean("redstone_control", items.getRedstoneControl());
            if (fluids != null && fluids.hasConfigurableFluidConnections()) {
                stackNbt.setTag("fluid_connections", fluids.writeFluidConnectionsNormalized(new NBTTagCompound(), facing));
                stackNbt.setTag("tank_settings", fluids.writeTankSettings(new NBTTagCompound()));
            }
            
            // Save upgrade information when copying configuration
            if (items instanceof IUpgradable) {
                IUpgradable upgradable = (IUpgradable) items;
                if (upgradable.hasUpgrades()) {
                    // Store the count of speed and energy upgrades
                    // Subtract 1 from each count as the base value is 1 (no upgrades = 1 count)
                    int speedCount = upgradable.getSpeedCount() - 1;
                    int energyCount = upgradable.getEnergyCount() - 1;
                    stackNbt.setInteger("speed_upgrades", speedCount);
                    stackNbt.setInteger("energy_upgrades", energyCount);
                }
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
                playSoundAt(player, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5F, SoundHelper.getPitch(-1D));
                items.readInventoryConnectionsNormalized(nbt.getCompoundTag("inventory_connections"), facing);
                items.readSlotSettings(nbt.getCompoundTag("slot_settings"));
                items.setRedstoneControl(nbt.getBoolean("redstone_control"));
                if (fluids != null && nbt.hasKey("fluid_connections")) {
                    fluids.readFluidConnectionsNormalized(nbt.getCompoundTag("fluid_connections"), facing);
                    fluids.readTankSettings(nbt.getCompoundTag("tank_settings"));
                }
                
                // Apply Upgrades from Blueprint
                if (items instanceof IUpgradable && !world.isRemote) {
                    applyUpgradesFromBlueprint(player, items, nbt);
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
        
        // Add Upgrades information to tooltip
        if (nbt != null) {
            if (nbt.hasKey("speed_upgrades")) {
                tooltip.add(TextFormatting.AQUA + Lang.localise("item.nuclearcraft.configuration_blueprint.speed_upgrades") + nbt.getInteger("speed_upgrades"));
            }
            if (nbt.hasKey("energy_upgrades")) {
                tooltip.add(TextFormatting.AQUA + Lang.localise("item.nuclearcraft.configuration_blueprint.energy_upgrades") + nbt.getInteger("energy_upgrades"));
            }
        }
    }

    private static void playSoundAt(EntityPlayer player, SoundEvent sound, float volume, float pitch) {
        player.world.playSound(null, player.posX, player.posY, player.posZ, sound, SoundCategory.PLAYERS, volume, pitch);
    }
    
    /**
     *  Apply Upgrades in player inventory from Blueprint to the machine
     */
    private void applyUpgradesFromBlueprint(EntityPlayer player, ITileInventory items, NBTTagCompound nbt) {
        if (!(items instanceof IUpgradable)) {
            return;
        }
        
        IUpgradable upgradable = (IUpgradable) items;
        if (!upgradable.hasUpgrades()) {
            return;
        }
        
        int requiredSpeedUpgrades = nbt.hasKey("speed_upgrades") ? nbt.getInteger("speed_upgrades") : 0;
        int requiredEnergyUpgrades = nbt.hasKey("energy_upgrades") ? nbt.getInteger("energy_upgrades") : 0;
        
        int speedUpgradeSlot = upgradable.getSpeedUpgradeSlot();
        int energyUpgradeSlot = upgradable.getEnergyUpgradeSlot();
        
        // Get existing upgrades in the machine
        ItemStack existingSpeedUpgrades = items.getInventoryStacks().get(speedUpgradeSlot).copy();
        ItemStack existingEnergyUpgrades = items.getInventoryStacks().get(energyUpgradeSlot).copy();
        
        int existingSpeedCount = existingSpeedUpgrades.getItem() == NCItems.upgrade && existingSpeedUpgrades.getMetadata() == 0 ? existingSpeedUpgrades.getCount() : 0;
        int existingEnergyCount = existingEnergyUpgrades.getItem() == NCItems.upgrade && existingEnergyUpgrades.getMetadata() == 1 ? existingEnergyUpgrades.getCount() : 0;
                
        int availableSpeedUpgrades = 0;
        int availableEnergyUpgrades = 0;
        
        // Check available upgrades in player inventory
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if (stack.isEmpty()) continue;
            
            if (stack.getItem() == NCItems.upgrade && stack.getMetadata() == 0) { // Speed upgrade
                availableSpeedUpgrades += stack.getCount();
            } else if (stack.getItem() == NCItems.upgrade && stack.getMetadata() == 1) { // Energy upgrade
                availableEnergyUpgrades += stack.getCount();
            }
        }
        
        // Calculate how many upgrades actually need to apply
        int neededSpeedUpgrades = Math.max(0, requiredSpeedUpgrades - existingSpeedCount);
        int neededEnergyUpgrades = Math.max(0, requiredEnergyUpgrades - existingEnergyCount);
        
        int appliedSpeedUpgrades = Math.min(neededSpeedUpgrades, availableSpeedUpgrades);
        int appliedEnergyUpgrades = Math.min(neededEnergyUpgrades, availableEnergyUpgrades);
        
        // Return excess upgrades to player inventory if machine had more than required
        if (existingSpeedCount > requiredSpeedUpgrades) {
            int excessSpeedUpgrades = existingSpeedCount - requiredSpeedUpgrades;
            returnUpgradesToPlayer(player, new ItemStack(NCItems.upgrade, excessSpeedUpgrades, 0));
        }
        
        if (existingEnergyCount > requiredEnergyUpgrades) {
            int excessEnergyUpgrades = existingEnergyCount - requiredEnergyUpgrades;
            returnUpgradesToPlayer(player, new ItemStack(NCItems.upgrade, excessEnergyUpgrades, 1));
        }
        
        // Keep existing upgrades that are within the required limit
        int keptSpeedUpgrades = Math.min(existingSpeedCount, requiredSpeedUpgrades);
        int keptEnergyUpgrades = Math.min(existingEnergyCount, requiredEnergyUpgrades);
        
        // Apply new upgrades from player inventory to machine
        if (appliedSpeedUpgrades > 0) {
            int remaining = appliedSpeedUpgrades;
            for (int i = 0; i < player.inventory.getSizeInventory() && remaining > 0; i++) {
                ItemStack stack = player.inventory.getStackInSlot(i);
                if (stack.isEmpty()) continue;
                
                if (stack.getItem() == NCItems.upgrade && stack.getMetadata() == 0) {
                    int toTake = Math.min(remaining, stack.getCount());
                    stack.shrink(toTake);
                    if (stack.getCount() <= 0) {
                        player.inventory.setInventorySlotContents(i, ItemStack.EMPTY);
                    }
                    remaining -= toTake;
                }
            }
        }
        
        if (appliedEnergyUpgrades > 0) {
            int remaining = appliedEnergyUpgrades;
            for (int i = 0; i < player.inventory.getSizeInventory() && remaining > 0; i++) {
                ItemStack stack = player.inventory.getStackInSlot(i);
                if (stack.isEmpty()) continue;
                
                if (stack.getItem() == NCItems.upgrade && stack.getMetadata() == 1) {
                    int toTake = Math.min(remaining, stack.getCount());
                    stack.shrink(toTake);
                    if (stack.getCount() <= 0) {
                        player.inventory.setInventorySlotContents(i, ItemStack.EMPTY);
                    }
                    remaining -= toTake;
                }
            }
        }
        
        // Clear upgrade slots
        items.getInventoryStacks().get(speedUpgradeSlot).setCount(0);
        items.getInventoryStacks().get(energyUpgradeSlot).setCount(0);
        
        // Insert upgrade stacks into machine (existing + newly applied)
        ItemStack finalSpeedUpgradeStack = new ItemStack(NCItems.upgrade, keptSpeedUpgrades + appliedSpeedUpgrades, 0);
        items.getInventoryStacks().set(speedUpgradeSlot, finalSpeedUpgradeStack);
        
        ItemStack finalEnergyUpgradeStack = new ItemStack(NCItems.upgrade, keptEnergyUpgrades + appliedEnergyUpgrades, 1);
        items.getInventoryStacks().set(energyUpgradeSlot, finalEnergyUpgradeStack);
        
        // Send message to player
        int totalNeededSpeedUpgrades = neededSpeedUpgrades;
        int totalNeededEnergyUpgrades = neededEnergyUpgrades;
        
        if (appliedSpeedUpgrades < totalNeededSpeedUpgrades || appliedEnergyUpgrades < totalNeededEnergyUpgrades) {
            StringBuilder message = new StringBuilder();
            message.append(TextFormatting.YELLOW);
            message.append(Lang.localise("item.nuclearcraft.configuration_blueprint.missing_upgrades")).append("\n");
            
            if (appliedSpeedUpgrades < totalNeededSpeedUpgrades) {
                int missing = totalNeededSpeedUpgrades - appliedSpeedUpgrades;
                message.append(Lang.localise("item.nuclearcraft.configuration_blueprint.speed_upgrades")).append(missing).append("\n");
            }
            
            if (appliedEnergyUpgrades < totalNeededEnergyUpgrades) {
                int missing = totalNeededEnergyUpgrades - appliedEnergyUpgrades;
                message.append(Lang.localise("item.nuclearcraft.configuration_blueprint.energy_upgrades")).append(missing);
            }
            
            player.sendMessage(new TextComponentString(message.toString()));
        }
        
        // Update upgrade slots
        upgradable.refreshUpgrades();
    }

    /**
     * Return upgrades to player inventory, dropping excess items if inventory is full
     */
    private void returnUpgradesToPlayer(EntityPlayer player, ItemStack upgrades) {
        if (upgrades.isEmpty()) return;
        
        // Try to add to player inventory
        ItemStack remainder = player.addItemStackToInventory(upgrades) ? ItemStack.EMPTY : upgrades;
        
        // If inventory is full, drop items
        if (!remainder.isEmpty()) {
            player.dropItem(remainder, false);
        }
    }
}