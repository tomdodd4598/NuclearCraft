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
		super("fuel", "fuel", 141);
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
		else if (m == 76 || m == 77) return fuelInfo(NuclearCraft.baseRFTBUOx, NuclearCraft.baseFuelTBUOx, NuclearCraft.baseHeatTBUOx);
		
		else if (m == 79 || m == 99) return fuelInfo(NuclearCraft.baseRFLEN, NuclearCraft.baseFuelLEN, NuclearCraft.baseHeatLEN);
		else if (m == 80 || m == 100) return fuelInfo(NuclearCraft.baseRFHEN, NuclearCraft.baseFuelHEN, NuclearCraft.baseHeatHEN);
		else if (m == 81 || m == 101) return fuelInfo(NuclearCraft.baseRFLEA, NuclearCraft.baseFuelLEA, NuclearCraft.baseHeatLEA);
		else if (m == 82 || m == 102) return fuelInfo(NuclearCraft.baseRFHEA, NuclearCraft.baseFuelHEA, NuclearCraft.baseHeatHEA);
		else if (m == 83 || m == 103 || m == 85 || m == 105 || m == 87 || m == 107) return fuelInfo(NuclearCraft.baseRFLEC, NuclearCraft.baseFuelLEC, NuclearCraft.baseHeatLEC);
		else if (m == 84 || m == 104 || m == 86 || m == 106 || m == 88 || m == 108) return fuelInfo(NuclearCraft.baseRFHEC, NuclearCraft.baseFuelHEC, NuclearCraft.baseHeatHEC);
		
		else if (m == 89 || m == 109) return fuelInfo(NuclearCraft.baseRFLENOx, NuclearCraft.baseFuelLENOx, NuclearCraft.baseHeatLENOx);
		else if (m == 90 || m == 110) return fuelInfo(NuclearCraft.baseRFHENOx, NuclearCraft.baseFuelHENOx, NuclearCraft.baseHeatHENOx);
		else if (m == 91 || m == 111) return fuelInfo(NuclearCraft.baseRFLEAOx, NuclearCraft.baseFuelLEAOx, NuclearCraft.baseHeatLEAOx);
		else if (m == 92 || m == 112) return fuelInfo(NuclearCraft.baseRFHEAOx, NuclearCraft.baseFuelHEAOx, NuclearCraft.baseHeatHEAOx);
		else if (m == 93 || m == 113 || m == 95 || m == 115 || m == 97 || m == 117) return fuelInfo(NuclearCraft.baseRFLECOx, NuclearCraft.baseFuelLECOx, NuclearCraft.baseHeatLECOx);
		else if (m == 94 || m == 114 || m == 96 || m == 116 || m == 98 || m == 118) return fuelInfo(NuclearCraft.baseRFHECOx, NuclearCraft.baseFuelHECOx, NuclearCraft.baseHeatHECOx);
		
		else if (m == 36) return fuelInfo("Boron-11", "Lithium-7");
		else if (m == 37) return fuelInfo("Deuterium", "Tritium", "Helium-3", "Lithium-6");
		else if (m == 38) return fuelInfo("Deuterium", "Tritium");
		else if (m == 39) return fuelInfo("Helium-3", "Lithium-6");
		else if (m == 41) return fuelInfo("Deuterium", "Helium-3");
		else if (m == 42) return fuelInfo("Hydrogen");
		else if (m == 44) return fuelInfo("Hydrogen");
		
		else return InfoNC.nul;
	}
	
	public String[] fuelInfo(int power, int time, int heat) {
		String[] inf = {
			"Base Power: " + power*NuclearCraft.fissionRF/100 + " RF/t",
			"Base Lifetime: " + (10000000/(time*20))*NuclearCraft.fissionEfficiency + " s",
			"Base Heat: " + heat + " H/t",
			"For a Fission Reactor with c Cell Compartments and an Efficiency of e,",
			"multiply the Base Power by c*(e/100) and multiply the Base Lifetime by 1/c",
			"",
			"The heat produced is determined by the positions of the Cell Compartments.",
			"Each Cell Compartment adjacent to n other Cell Compartments, or Graphite",
			"blocks followed", "by Cell Compartments in the same direction produces",
			"(n+1)(n+2)/2 times the Base Heat of the fuel.",
			"",
			"In addition, the higher the heat level of the reactor, the more efficient",
			"the fuel will be."
		};
		return inf;
	}
	
	public String[] fuelInfo(String... fuels) {
		String[] inf = new String[1 + fuels.length];
		inf[0] = "Fusion fuel - best combined with:";
		for (int i = 0; i < fuels.length; i ++) inf[i + 1] = fuels[i];
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
			case 11: return "LEU235Cell";
			case 12: return "HEU235Cell";
			case 13: return "LEP239Cell";
			case 14: return "HEP239Cell";
			case 15: return "MOX239Cell";
			case 16: return "TBUCell";
			case 17: return "LEU233Cell";
			case 18: return "HEU233Cell";
			case 19: return "LEP241Cell";
			case 20: return "HEP241Cell";
			case 21: return "MOX241Cell";
			case 22: return "dLEU235Cell";
			case 23: return "dHEU235Cell";
			case 24: return "dLEP239Cell";
			case 25: return "dHEP239Cell";
			case 26: return "dMOX239Cell";
			case 27: return "dTBUCell";
			case 28: return "dLEU233Cell";
			case 29: return "dHEU233Cell";
			case 30: return "dLEP241Cell";
			case 31: return "dHEP241Cell";
			case 32: return "dMOX241Cell";
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
			case 59: return "LEU235CellOxide";
			case 60: return "HEU235CellOxide";
			case 61: return "LEP239CellOxide";
			case 62: return "HEP239CellOxide";
			case 63: return "LEU233CellOxide";
			case 64: return "HEU233CellOxide";
			case 65: return "LEP241CellOxide";
			case 66: return "HEP241CellOxide";
			case 67: return "dLEU235CellOxide";
			case 68: return "dHEU235CellOxide";
			case 69: return "dLEP239CellOxide";
			case 70: return "dHEP239CellOxide";
			case 71: return "dLEU233CellOxide";
			case 72: return "dHEU233CellOxide";
			case 73: return "dLEP241CellOxide";
			case 74: return "dHEP241CellOxide";
			case 75: return "SCHe4Cell";
			case 76: return "TBUOxide";
			case 77: return "TBUCellOxide";
			case 78: return "dTBUCellOxide";
			case 79: return "LEN236";
			case 80: return "HEN236";
			case 81: return "LEA242";
			case 82: return "HEA242";
			case 83: return "LEC243";
			case 84: return "HEC243";
			case 85: return "LEC245";
			case 86: return "HEC245";
			case 87: return "LEC247";
			case 88: return "HEC247";
			case 89: return "LEN236Oxide";
			case 90: return "HEN236Oxide";
			case 91: return "LEA242Oxide";
			case 92: return "HEA242Oxide";
			case 93: return "LEC243Oxide";
			case 94: return "HEC243Oxide";
			case 95: return "LEC245Oxide";
			case 96: return "HEC245Oxide";
			case 97: return "LEC247Oxide";
			case 98: return "HEC247Oxide";
			case 99: return "LEN236Cell";
			case 100: return "HEN236Cell";
			case 101: return "LEA242Cell";
			case 102: return "HEA242Cell";
			case 103: return "LEC243Cell";
			case 104: return "HEC243Cell";
			case 105: return "LEC245Cell";
			case 106: return "HEC245Cell";
			case 107: return "LEC247Cell";
			case 108: return "HEC247Cell";
			case 109: return "LEN236CellOxide";
			case 110: return "HEN236CellOxide";
			case 111: return "LEA242CellOxide";
			case 112: return "HEA242CellOxide";
			case 113: return "LEC243CellOxide";
			case 114: return "HEC243CellOxide";
			case 115: return "LEC245CellOxide";
			case 116: return "HEC245CellOxide";
			case 117: return "LEC247CellOxide";
			case 118: return "HEC247CellOxide";
			case 119: return "dLEN236Cell";
			case 120: return "dHEN236Cell";
			case 121: return "dLEA242Cell";
			case 122: return "dHEA242Cell";
			case 123: return "dLEC243Cell";
			case 124: return "dHEC243Cell";
			case 125: return "dLEC245Cell";
			case 126: return "dHEC245Cell";
			case 127: return "dLEC247Cell";
			case 128: return "dHEC247Cell";
			case 129: return "dLEN236CellOxide";
			case 130: return "dHEN236CellOxide";
			case 131: return "dLEA242CellOxide";
			case 132: return "dHEA242CellOxide";
			case 133: return "dLEC243CellOxide";
			case 134: return "dHEC243CellOxide";
			case 135: return "dLEC245CellOxide";
			case 136: return "dHEC245CellOxide";
			case 137: return "dLEC247CellOxide";
			case 138: return "dHEC247CellOxide";
			case 139: return "AmRTGCell";
			case 140: return "CfRTGCell";
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
					return addStackToInv(stack, player, new ItemStack(NCItems.fuel, 1, 34));
				}
				if (material == NuclearCraft.liquidhelium && l == 0) {
					world.setBlockToAir(i, j, k);
					return addStackToInv(stack, player, new ItemStack(NCItems.fuel, 1, 75));
				}
			}
		}
		return stack;
	}
	
	private ItemStack addStackToInv(ItemStack stack, EntityPlayer player, ItemStack stack2) {
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