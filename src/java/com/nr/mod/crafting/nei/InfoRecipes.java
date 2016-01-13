package com.nr.mod.crafting.nei;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.item.ItemStack;

import com.nr.mod.NuclearRelativistics;
import com.nr.mod.blocks.NRBlocks;
import com.nr.mod.blocks.tileentities.TileEntityFissionReactorGraphite;
import com.nr.mod.items.NRItems;

public class InfoRecipes {
	private static final InfoRecipes infoBase = new InfoRecipes();
	@SuppressWarnings("rawtypes")
	private Map info = new HashMap();
	@SuppressWarnings("rawtypes")
	private Map infoTypes = new HashMap();

	public static InfoRecipes info() {
		return infoBase;
	}
	
	private InfoRecipes() {
		
		addRecipe(new ItemStack(NRBlocks.fissionReactorGraphiteIdle), "Compatible Fuel Cells-Check uses in NEI-for more info:-LEU      HEU-LEP      HEP-MOX      TBU");
		addRecipe(new ItemStack(NRBlocks.fissionReactorGraphiteActive), "Compatible Fuel Cells-Check uses in NEI-for more info:-LEU      HEU-LEP      HEP-MOX      TBU");
		addRecipe(new ItemStack(NRBlocks.reactionGeneratorIdle), "Uses Nuclear Fuel-and Universal Reactant,-Redstone, Gunpowder,-Ghast Tears, Nether-Wart or Blaze Powder-as the catalyst-to generate " + (NuclearRelativistics.reactionGeneratorRF*5) + " RF/t");
		addRecipe(new ItemStack(NRBlocks.reactionGeneratorActive), "Compatible Fuel Cells-Check uses in NEI-for more info:-LEU      HEU-LEP      HEP-MOX      TBU");
		
		fuelInfo(11, 15, 15000, 14);
		fuelInfo(17, 15, 15000, 14);
		fuelInfo(12, 68, 15000, 140);
		fuelInfo(18, 68, 15000, 140);
		fuelInfo(13, 30, 28800, 42);
		fuelInfo(19, 30, 28800, 42);
		fuelInfo(14, 135, 28800, 420);
		fuelInfo(20, 135, 28800, 420);
		fuelInfo(15, 33, 20000, 36);
		fuelInfo(21, 33, 20000, 36);
		fuelInfo(16, 4, 3750, 2);
		
		fuelInfo(59, 23, 15000, 17);
		fuelInfo(63, 23, 15000, 17);
		fuelInfo(60, 101, 15000, 168);
		fuelInfo(64, 101, 15000, 168);
		fuelInfo(61, 45, 28800, 50);
		fuelInfo(65, 45, 28800, 50);
		fuelInfo(62, 203, 28800, 504);
		fuelInfo(66, 203, 28800, 504);
		
		addRecipe(new ItemStack(NRItems.dominoes), "Paul's Favourite:-He'll follow anyone-he sees carrying-this in their hand...");
		addRecipe(new ItemStack(NRItems.fuel, 1, 34), "Right click on a water-source block with an-empty fluid cell to obtain");
		addRecipe(new ItemStack(NRItems.fuel, 1, 45), "Right click on a water-source block to-obtain a water cell");
	}

	public void fuelInfo(int meta, int power, int time, int heat) {
		addRecipe(new ItemStack(NRItems.fuel, 1, meta), "Base Power = " + power*TileEntityFissionReactorGraphite.power + " RF/t-Lifetime = " + (7200000*NuclearRelativistics.fissionEfficiency)/(time*20) + " s-Base Heat = "+ heat*2 + " H/t-Base Total Energy = " + (720*power*NuclearRelativistics.fissionRF*NuclearRelativistics.fissionEfficiency)/time + " kRF" + "-* Values for a 1*1*1 Reactor-Multiply Base Power by-4 for a 3*3*3 Reactor,-9 for a 5*5*5 Reactor, etc.-Multiply Base Heat-and Base Energy by-2 for a 3*3*3 Reactor,-3 for a 5*5*5 Reactor, etc.");
	}
		
	@SuppressWarnings("unchecked")
	public void addRecipe(ItemStack input, String info) {
		this.info.put(input, info);
	}

	@SuppressWarnings("unchecked")
	public void addRecipe(String type, ItemStack input, String info) {
		this.info.put(input, info);
		this.infoTypes.put(input, type);
	}

	@SuppressWarnings("rawtypes")
	public String getInfo(ItemStack stack) {
		Iterator iterator = this.info.entrySet().iterator();
		Map.Entry entry;
		do {
			if (!iterator.hasNext()) {
				return null;
			}
			entry = (Map.Entry)iterator.next();
		}
		while (!func_151397_a(stack, (ItemStack)entry.getKey()));
		return (String)entry.getValue();
	}

	private boolean func_151397_a(ItemStack p_151397_1_, ItemStack p_151397_2_) {
		return (p_151397_2_.getItem() == p_151397_1_.getItem()) && ((p_151397_2_.getItemDamage() == 32767) || (p_151397_2_.getItemDamage() == p_151397_1_.getItemDamage()));
	}
	
	@SuppressWarnings("rawtypes")
	public Map getInfoList() {
		return this.info;
	}

	@SuppressWarnings("rawtypes")
	public Map getInfoType() {
		return this.infoTypes;
	}
}