package com.nr.mod.crafting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.nr.mod.blocks.NRBlocks;
import com.nr.mod.items.NRItems;

public class XCrusherRecipes
{
    private static final XCrusherRecipes smeltingBase = new XCrusherRecipes();
    /** The list of smelting results. */
    private Map<ItemStack, ItemStack> smeltingList = new HashMap<ItemStack, ItemStack>();

    /**
     * Used to call methods addSmelting and getSmeltingResult.
     */
    public static XCrusherRecipes smelting()
    {
        return smeltingBase;
    }
    
    private void AddRecipe(String ore, ItemStack out) {
    	ArrayList<ItemStack> tList = OreDictionary.getOres(ore);
    	for (int i = 0; i < tList.size(); i++) {
    	    ItemStack tStack = tList.get(i);
    	    tStack = tStack.copy();
    	    tStack.stackSize = 1;
    	    this.recipeSmelt((ItemStack)OreDictionary.getOres(ore).get(i), out);
    	}
    }

    private XCrusherRecipes() {
    	//Ores to Dust
    	this.AddRecipe("oreIron", new ItemStack(NRItems.material, 2, 8));
    	this.AddRecipe("oreGold", new ItemStack(NRItems.material, 2, 9));
    	this.AddRecipe("oreLapis", new ItemStack(Items.dye, 8, 4));
    	this.AddRecipe("oreDiamond", new ItemStack(Items.diamond, 2));
    	this.AddRecipe("oreRedstone", new ItemStack(Items.redstone, 8));
    	this.AddRecipe("oreEmerald", new ItemStack(Items.emerald, 2));
    	this.AddRecipe("oreQuartz", new ItemStack(Items.quartz, 2));
    	this.AddRecipe("oreCoal", new ItemStack(Items.coal, 2));
    	this.AddRecipe("oreCopper", new ItemStack(NRItems.material, 2, 15));
    	this.AddRecipe("oreLead", new ItemStack(NRItems.material, 2, 17));
    	this.AddRecipe("oreTin", new ItemStack(NRItems.material, 2, 16));
    	this.AddRecipe("oreSilver", new ItemStack(NRItems.material, 2, 18));
    	this.AddRecipe("oreUranium", new ItemStack(NRItems.material, 2, 19));
    	this.AddRecipe("oreYellorite", new ItemStack(NRItems.material, 2, 19));
    	this.AddRecipe("oreYellorium", new ItemStack(NRItems.material, 2, 19));
    	this.AddRecipe("oreThorium", new ItemStack(NRItems.material, 2, 20));
    	this.AddRecipe("orePlutonium", new ItemStack(NRItems.material, 2, 33));
    	this.AddRecipe("oreLithium", new ItemStack(NRItems.material, 2, 44));
    	this.AddRecipe("oreBoron", new ItemStack(NRItems.material, 2, 45));
    	
    	//Gems to Dust
    	this.AddRecipe("gemLapis", new ItemStack(NRItems.material, 1, 10));
    	this.AddRecipe("gemDiamond", new ItemStack(NRItems.material, 1, 11));
    	this.AddRecipe("gemEmerald", new ItemStack(NRItems.material, 1, 12));
    	this.AddRecipe("gemQuartz", new ItemStack(NRItems.material, 1, 13));
    	this.recipeSmelt(new ItemStack(Items.coal), new ItemStack(NRItems.material, 1, 14));
    	
    	//Ingots to Dust
    	this.AddRecipe("ingotIron", new ItemStack(NRItems.material, 1, 8));
    	this.AddRecipe("ingotGold", new ItemStack(NRItems.material, 1, 9));
    	this.AddRecipe("ingotCopper", new ItemStack(NRItems.material, 1, 15));
    	this.AddRecipe("ingotLead", new ItemStack(NRItems.material, 1, 17));
    	this.AddRecipe("ingotTin", new ItemStack(NRItems.material, 1, 16));
    	this.AddRecipe("ingotSilver", new ItemStack(NRItems.material, 1, 18));
    	this.AddRecipe("ingotUranium", new ItemStack(NRItems.material, 1, 19));
    	this.AddRecipe("ingotYellorite", new ItemStack(NRItems.material, 1, 19));
    	this.AddRecipe("ingotYellorium", new ItemStack(NRItems.material, 1, 19));
    	this.AddRecipe("ingotThorium", new ItemStack(NRItems.material, 1, 20));
    	this.AddRecipe("ingotBronze", new ItemStack(NRItems.material, 1, 21));
    	this.AddRecipe("ingotLithium", new ItemStack(NRItems.material, 1, 44));
    	this.AddRecipe("ingotBoron", new ItemStack(NRItems.material, 1, 45));
    	
    	//Other Recipes
    	this.AddRecipe("cobblestone", new ItemStack(Blocks.gravel, 1));
    	this.recipeSmelt(new ItemStack(Blocks.gravel), new ItemStack(Blocks.sand, 1));
    	this.AddRecipe("stone", new ItemStack(Blocks.cobblestone, 1));
    	this.AddRecipe("blockGlass", new ItemStack(Blocks.sand, 1));
    	this.AddRecipe("sandstone", new ItemStack(Blocks.sand, 4));
    	this.AddRecipe("glowstone", new ItemStack(Items.glowstone_dust, 4));
    	this.recipeSmelt(new ItemStack(NRBlocks.graphiteBlock), new ItemStack(NRItems.material, 9, 14));
    	
    	//Lithium and Boron Cells
    	this.recipeSmelt(new ItemStack(NRItems.fuel, 1, 41), new ItemStack(NRItems.material, 1, 46));
    	this.recipeSmelt(new ItemStack(NRItems.fuel, 1, 42), new ItemStack(NRItems.material, 1, 47));
    	this.recipeSmelt(new ItemStack(NRItems.fuel, 1, 43), new ItemStack(NRItems.material, 1, 48));
    	this.recipeSmelt(new ItemStack(NRItems.fuel, 1, 44), new ItemStack(NRItems.material, 1, 49));
    }
    
    
    
    public void blockSmelt(Block p_151393_1_, ItemStack p_151393_2_)
    {
        this.itemSmelt(Item.getItemFromBlock(p_151393_1_), p_151393_2_);
    }

    public void itemSmelt(Item item, ItemStack itemstack)
    {
        this.recipeSmelt(new ItemStack(item, 1, 32767), itemstack);
    }

    public void recipeSmelt(ItemStack input, ItemStack output)
    {
        this.smeltingList.put(input, output);
    }

    /**
     * Returns the smelting result of an item.
     */
    public ItemStack getSmeltingResult(ItemStack itemstack3)
    {
        Iterator<?> iterator = this.smeltingList.entrySet().iterator();
        Entry<?, ?> entry;

        do
        {
            if (!iterator.hasNext())
            {
                return null;
            }

            entry = (Entry<?, ?>)iterator.next();
        }
        while (!this.func_151397_a(itemstack3, (ItemStack)entry.getKey()));

        return (ItemStack)entry.getValue();
    }

    private boolean func_151397_a(ItemStack p_151397_1_, ItemStack p_151397_2_)
    {
        return p_151397_2_.getItem() == p_151397_1_.getItem() && (p_151397_2_.getItemDamage() == 32767 || p_151397_2_.getItemDamage() == p_151397_1_.getItemDamage());
    }

    public Map<ItemStack, ItemStack> getSmeltingList()
    {
        return this.smeltingList;
    }
}