package nc.item;

import nc.entity.EntityBullet;
import nc.handler.BulletEvent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemPistol extends ItemNC {

    public ItemPistol(String nam, String... lines) {
		super("tools", nam, lines);
		this.setMaxDamage(-1);
	}

	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {

        BulletEvent event = new BulletEvent(player, stack);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            return stack;
        }

        boolean flag = player.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, stack) > 0;

        if (flag || player.inventory.hasItem(NCItems.dUBullet)) {
        	EntityBullet entitybullet = new EntityBullet(world, player, 3.2F);

            entitybullet.setDamage(entitybullet.getDamage() + 2.0D);

            world.playSoundAtEntity(player, "fireworks.blast", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + 0.5F);

            if (flag) {
            	entitybullet.canBePickedUp = 2;
            } else {
            	player.inventory.consumeInventoryItem(NCItems.dUBullet);
            }

            if (!world.isRemote) {
            	world.spawnEntityInWorld(entitybullet);
            }
        }
        return stack;
    }

    public int getMaxItemUseDuration(ItemStack p_77626_1_) {
        return -1;
    }

    @SideOnly(Side.CLIENT)
    public boolean isFull3D() {
        return true;
    }
}