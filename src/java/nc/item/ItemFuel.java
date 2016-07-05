package nc.item;

import java.util.List;

import nc.NuclearCraft;
import nc.util.InfoNC;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemFuel extends ItemMeta {

	public ItemFuel() {
		super("fuel", "fuel", 76);
	}
	
	@SuppressWarnings({ "rawtypes" })
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean whatIsThis) {
        super.addInformation(itemStack, player, list, whatIsThis);
        if (info(itemStack.getItemDamage()) == InfoNC.nul); else if (info(itemStack.getItemDamage()).length > 0) InfoNC.infoFull(list, info(itemStack.getItemDamage()));
    }
	
	public String[] info(int m) {
		if (m == 0 || m == 6 || m == 11 || m == 17) return fuelInfo(NuclearCraft.baseRFLEU, NuclearCraft.baseFuelLEU, NuclearCraft.baseHeatLEU);
		else if (m == 1 || m == 7 || m == 12 || m == 18) return fuelInfo(NuclearCraft.baseRFHEU, NuclearCraft.baseFuelHEU, NuclearCraft.baseHeatHEU);
		else if (m == 2 || m == 8 || m == 13 || m == 19) return fuelInfo(NuclearCraft.baseRFLEP, NuclearCraft.baseFuelLEP, NuclearCraft.baseHeatLEP);
		else if (m == 3 || m == 9 || m == 14 || m == 20) return fuelInfo(NuclearCraft.baseRFHEP, NuclearCraft.baseFuelHEP, NuclearCraft.baseHeatHEP);
		else if (m == 4 || m == 10 || m == 15 || m == 21) return fuelInfo(NuclearCraft.baseRFMOX, NuclearCraft.baseFuelMOX, NuclearCraft.baseHeatMOX);
		else if (m == 5 || m == 16) return fuelInfo(NuclearCraft.baseRFTBU, NuclearCraft.baseFuelTBU, NuclearCraft.baseHeatTBU);
		else if (m == 51 || m == 55 || m == 59 || m == 63) return fuelInfo(NuclearCraft.baseRFLEUOx, NuclearCraft.baseFuelLEUOx, NuclearCraft.baseHeatLEUOx);
		else if (m == 52 || m == 56 || m == 60 || m == 64) return fuelInfo(NuclearCraft.baseRFHEUOx, NuclearCraft.baseFuelHEUOx, NuclearCraft.baseHeatHEUOx);
		else if (m == 53 || m == 57 || m == 61 || m == 65) return fuelInfo(NuclearCraft.baseRFLEPOx, NuclearCraft.baseFuelLEPOx, NuclearCraft.baseHeatLEPOx);
		else if (m == 54 || m == 58 || m == 62 || m == 66) return fuelInfo(NuclearCraft.baseRFHEPOx, NuclearCraft.baseFuelHEPOx, NuclearCraft.baseHeatHEPOx);
		else return InfoNC.nul;
	}
	
	public String[] fuelInfo(int power, int time, int heat) {
		String[] inf = {
			"Base Power: " + power*NuclearCraft.fissionRF/100 + " RF/t",
			"Base Lifetime: " + (10000000/(time*20))*NuclearCraft.fissionEfficiency + " s",
			"Base Heat: " + heat + " H/t",
			"For a Fission Reactor with c Cell",
			"Compartments and an Efficiency of e:",
			"Multiply the Base Power by: c*(e/100)",
			"Multiply the Base Lifetime by: 1/c",
			"",
			"The heat produced is determined by",
			"the positions of the Cell Compartments.",
			"Each Cell Compartments adjacent to",
			"n other Cell Compartments produces",
			"(n+1)(n+2)/2 times the Base Heat of",
			"the fuel.",
			"",
			"In addition, the higher the heat level",
			"of the reactor, the more efficient the",
			"fuel will be."
		};
		return inf;
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
		    default: return this.getUnlocalizedName();
	    }
	}
	
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
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
	
	private ItemStack func_150910_a(ItemStack stack, EntityPlayer player, ItemStack stack2) {
	    if (--stack.stackSize <= 0) {
	        return stack2;
	    } else {
	        if (!player.inventory.addItemStackToInventory(stack2)) {
	        	player.dropPlayerItemWithRandomChoice(stack2, false);
	        }
	        return stack;
	    }
	}
}