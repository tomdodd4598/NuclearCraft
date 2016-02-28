package nc.item;

import java.util.List;

import nc.NuclearCraft;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ItemFuel extends Item {

	public ItemFuel(String unlocalizedName) {
        super();
        this.setHasSubtypes(true);
        this.setUnlocalizedName(unlocalizedName);
        this.setCreativeTab(NuclearCraft.tabNC);
	}

	public IIcon[] icons = new IIcon[6];
	public IIcon[] icons1 = new IIcon[6];
	public IIcon[] icons2 = new IIcon[6];
	public IIcon[] icons3 = new IIcon[6];
	public IIcon[] icons4 = new IIcon[6];
	public IIcon[] icons5 = new IIcon[6];
	public IIcon[] icons6 = new IIcon[6];
	public IIcon[] icons7 = new IIcon[6];
	public IIcon[] icons8 = new IIcon[6];
	public IIcon[] icons9 = new IIcon[6];
	public IIcon[] icons10 = new IIcon[6];
	public IIcon[] icons11 = new IIcon[6];
	public IIcon[] icons12 = new IIcon[4];
        
	public void registerIcons(IIconRegister reg) {
	    for (int i = 0; i < 6; i ++) {
	        this.icons[i] = reg.registerIcon("nc:fuel/" + (i));
	    }
	    for (int i = 0; i < 6; i ++) {
	        this.icons1[i] = reg.registerIcon("nc:fuel/" + (i+6));
	    }
	    for (int i = 0; i < 6; i ++) {
	        this.icons2[i] = reg.registerIcon("nc:fuel/" + (i+12));
	    }
	    for (int i = 0; i < 6; i ++) {
	        this.icons3[i] = reg.registerIcon("nc:fuel/" + (i+18));
	    }
	    for (int i = 0; i < 6; i ++) {
	        this.icons4[i] = reg.registerIcon("nc:fuel/" + (i+24));
	    }
	    for (int i = 0; i < 6; i ++) {
	        this.icons5[i] = reg.registerIcon("nc:fuel/" + (i+30));
	    }
	    for (int i = 0; i < 6; i ++) {
	        this.icons6[i] = reg.registerIcon("nc:fuel/" + (i+36));
	    }
	    for (int i = 0; i < 6; i ++) {
	        this.icons7[i] = reg.registerIcon("nc:fuel/" + (i+42));
	    }
	    for (int i = 0; i < 6; i ++) {
	        this.icons8[i] = reg.registerIcon("nc:fuel/" + (i+48));
	    }
	    for (int i = 0; i < 6; i ++) {
	        this.icons9[i] = reg.registerIcon("nc:fuel/" + (i+54));
	    }
	    for (int i = 0; i < 6; i ++) {
	        this.icons10[i] = reg.registerIcon("nc:fuel/" + (i+60));
	    }
	    for (int i = 0; i < 6; i ++) {
	        this.icons11[i] = reg.registerIcon("nc:fuel/" + (i+66));
	    }
	    for (int i = 0; i < 4; i ++) {
	        this.icons12[i] = reg.registerIcon("nc:fuel/" + (i+72));
	    }
	}

	public IIcon getIconFromDamage(int meta) {
	    if (meta > 75) meta = 0;
	    if 		(meta >= 0 && meta < 6) { return this.icons[meta]; }
	    else if (meta >= 6 && meta < 12) { return this.icons1[meta-6]; }
	    else if (meta >= 12 && meta < 18) { return this.icons2[meta-12]; }
	    else if (meta >= 18 && meta < 24) { return this.icons3[meta-18]; }
	    else if (meta >= 24 && meta < 30) { return this.icons4[meta-24]; }
	    else if (meta >= 30 && meta < 36) { return this.icons5[meta-30]; }
	    else if (meta >= 36 && meta < 42) { return this.icons6[meta-36]; }
	    else if (meta >= 42 && meta < 48) { return this.icons7[meta-42]; }
	    else if (meta >= 48 && meta < 54) { return this.icons8[meta-48]; }
	    else if (meta >= 54 && meta < 60) { return this.icons9[meta-54]; }
	    else if (meta >= 60 && meta < 66) { return this.icons10[meta-60];}
	    else if (meta >= 66 && meta < 72) { return this.icons11[meta-66];}
	    else if (meta >= 72 && meta < 76) { return this.icons12[meta-72];}
	    else { return this.icons[0]; }
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void getSubItems(Item item, CreativeTabs tab, List list) {
	    for (int i = 0; i < 76; i ++) {
	        list.add(new ItemStack(item, 1, i));
	    }
	}
	
	public String getUnlocalizedName(ItemStack stack) {
	    switch (stack.getItemDamage()) {
	    case 0: return "LEU235";
	    case 1: return "HEU235";
	    case 2: return "LEP239";
	    case 3: return "HEP239";
	    case 4: return "MOX239";
	    case 5: return "TBU";
	    case 6: return "LEU233";
	    case 7: return "HEU233";
	    case 8: return "LEP241";
	    case 9: return "HEP241";
	    case 10: return "MOX241";
	    case 11: return "LEUCell235";
	    case 12: return "HEUCell235";
	    case 13: return "LEPCell239";
	    case 14: return "HEPCell239";
	    case 15: return "MOXCell239";
	    case 16: return "TBUCell";
	    case 17: return "LEUCell233";
	    case 18: return "HEUCell233";
	    case 19: return "LEPCell241";
	    case 20: return "HEPCell241";
	    case 21: return "MOXCell241";
	    case 22: return "dLEUCell235";
	    case 23: return "dHEUCell235";
	    case 24: return "dLEPCell239";
	    case 25: return "dHEPCell239";
	    case 26: return "dMOXCell239";
	    case 27: return "dTBUCell";
	    case 28: return "dLEUCell233";
	    case 29: return "dHEUCell233";
	    case 30: return "dLEPCell241";
	    case 31: return "dHEPCell241";
	    case 32: return "dMOXCell241";
	    case 33: return "emptyCell";
	    case 34: return "H2OCell";
	    case 35: return "OCell";
	    case 36: return "HCell";
	    case 37: return "DCell";
	    case 38: return "TCell";
	    case 39: return "He3Cell";
	    case 40: return "He4Cell";
	    case 41: return "Li6Cell";
	    case 42: return "Li7Cell";
	    case 43: return "B10Cell";
	    case 44: return "B11Cell";
	    case 45: return "emptyFluidCell";
	    case 46: return "RTGCell";
	    case 47: return "nCapsule";
	    case 48: return "emptyCapsule";
	    case 49: return "pCapsule";
	    case 50: return "eCapsule";
	    case 51: return "LEU235Oxide";
	    case 52: return "HEU235Oxide";
	    case 53: return "LEP239Oxide";
	    case 54: return "HEP239Oxide";
	    case 55: return "LEU233Oxide";
	    case 56: return "HEU233Oxide";
	    case 57: return "LEP241Oxide";
	    case 58: return "HEP241Oxide";
	    case 59: return "LEUCell235Oxide";
	    case 60: return "HEUCell235Oxide";
	    case 61: return "LEPCell239Oxide";
	    case 62: return "HEPCell239Oxide";
	    case 63: return "LEUCell233Oxide";
	    case 64: return "HEUCell233Oxide";
	    case 65: return "LEPCell241Oxide";
	    case 66: return "HEPCell241Oxide";
	    case 67: return "dLEUCell235Oxide";
	    case 68: return "dHEUCell235Oxide";
	    case 69: return "dLEPCell239Oxide";
	    case 70: return "dHEPCell239Oxide";
	    case 71: return "dLEUCell233Oxide";
	    case 72: return "dHEUCell233Oxide";
	    case 73: return "dLEPCell241Oxide";
	    case 74: return "dHEPCell241Oxide";
	    case 75: return "SCHe4Cell";
	    default:
	        return this.getUnlocalizedName();
	    }
	}
	
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
	    if (stack.getItemDamage() == 45) {
		    MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, true);
		    if (movingobjectposition == null) {
	            return stack;
	        } else {
		    	int i = movingobjectposition.blockX;
		        int j = movingobjectposition.blockY;
		        int k = movingobjectposition.blockZ;
		    	Material material = world.getBlock(i, j, k).getMaterial();
		    	int l = world.getBlockMetadata(i, j, k);
		    	if (material == Material.water && l == 0) {
		    		world.setBlockToAir(i, j, k);
		            return this.func_150910_a(stack, player, new ItemStack(NCItems.fuel, 1, 34));
		        }
		    	if (material == NuclearCraft.liquidhelium && l == 0) {
		    		world.setBlockToAir(i, j, k);
		            return this.func_150910_a(stack, player, new ItemStack(NCItems.fuel, 1, 75));
		        }
	    	}
	    }
	    return stack;
	}
	
	private ItemStack func_150910_a(ItemStack stack, EntityPlayer player, ItemStack stack2)
	{
	    if (--stack.stackSize <= 0)
	    {
	        return stack2;
	    }
	    else
	    {
	        if (!player.inventory.addItemStackToInventory(stack2))
	        {
	        	player.dropPlayerItemWithRandomChoice(stack2, false);
	        }
	
	        return stack;
	    }
	}
}