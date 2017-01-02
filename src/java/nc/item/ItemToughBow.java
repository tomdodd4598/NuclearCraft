package nc.item;

import java.util.List;

import nc.NuclearCraft;
import nc.util.InfoNC;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemToughBow extends ItemBow {

	String[] info;
	String name;
	
	public ItemToughBow(String nam, String... lines) {
		super();
		String[] strings = new String[lines.length];
		for (int i = 0; i < lines.length; i++) {
			strings[i] = lines[i];
		}
		info = strings;
		name = nam;
		setUnlocalizedName(nam);
		this.setMaxDamage(4000);
	}
	
	public String getUnlocalizedName() {
		return "item." + name;
	}
	
	@SideOnly(Side.CLIENT)
	public CreativeTabs getCreativeTab() {
		return NuclearCraft.tabNC;
	}
	
    public static final String[] toughBowPullIconNameArray = new String[] {"Pulled0", "Pulled1", "Pulled2"};
    @SideOnly(Side.CLIENT)
    private IIcon[] iconArray;

    public void onPlayerStoppedUsing(ItemStack p_77615_1_, World p_77615_2_, EntityPlayer p_77615_3_, int p_77615_4_) {
        int j = this.getMaxItemUseDuration(p_77615_1_) - p_77615_4_;

        ArrowLooseEvent event = new ArrowLooseEvent(p_77615_3_, p_77615_1_, j);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            return;
        }
        j = event.charge;

        boolean flag = p_77615_3_.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, p_77615_1_) > 0;

        if (flag || p_77615_3_.inventory.hasItem(Items.arrow)) {
            float f = (float)j / 20.0F;
            f = (f * f + f * 2.0F) / 3.0F;

            if ((double)f < 0.1D) {
                return;
            }

            if (f > 1.0F) {
                f = 1.0F;
            }

            EntityArrow entityarrow = new EntityArrow(p_77615_2_, p_77615_3_, f * 2.0F);

            if (f == 1.0F) {
                entityarrow.setIsCritical(true);
            }

            int k = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, p_77615_1_);

            if (k > 0) {
                entityarrow.setDamage(entityarrow.getDamage() + (double)k * 1.0D + 1.0D);
            }
            else {entityarrow.setDamage(entityarrow.getDamage() + 3.0D);}

            int l = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, p_77615_1_);

            if (l > 0) {
                entityarrow.setKnockbackStrength(l);
            }

            if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, p_77615_1_) > 0) {
                entityarrow.setFire(200);
            }

            p_77615_1_.damageItem(1, p_77615_3_);
            p_77615_2_.playSoundAtEntity(p_77615_3_, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

            if (flag) {
                entityarrow.canBePickedUp = 2;
            }
            else {
                p_77615_3_.inventory.consumeInventoryItem(Items.arrow);
            }

            if (!p_77615_2_.isRemote) {
                p_77615_2_.spawnEntityInWorld(entityarrow);
            }
        }
    }

    public int getMaxItemUseDuration(ItemStack p_77626_1_) {
        return 24000;
    }

    public int getItemEnchantability() {
        return 1;
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister p_94581_1_) {
        this.itemIcon = p_94581_1_.registerIcon("nc:tools/bow");
        this.iconArray = new IIcon[toughBowPullIconNameArray.length];

        for (int i = 0; i < this.iconArray.length; ++i) {
            this.iconArray[i] = p_94581_1_.registerIcon("nc:tools/bow" + "" + toughBowPullIconNameArray[i]);
        }
    }
    
    @SideOnly(Side.CLIENT)
    public IIcon getIcon (ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
        if (usingItem != null) {
            int time = 24000 - useRemaining;
            if (time < 11) {return iconArray[0];}
            if (time < 15) {return iconArray[1];}
            return iconArray[2];
        }
        return getIcon(stack, renderPass);
    }
    
    @SideOnly(Side.CLIENT)
    public boolean isFull3D() {
        return true;
    }
    
    @SuppressWarnings({ "rawtypes" })
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean whatIsThis) {
        super.addInformation(itemStack, player, list, whatIsThis);
        InfoNC.infoFull(list, info);
    }
}