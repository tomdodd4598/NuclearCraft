package com.nr.mod.crafting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

import com.nr.mod.blocks.NRBlocks;
import com.nr.mod.items.NRItems;

public class NuclearWorkspaceCraftingManager
{
    /** The static instance of this class */
    private static final NuclearWorkspaceCraftingManager instance = new NuclearWorkspaceCraftingManager();
    /** A list of all the recipes added */
    @SuppressWarnings("rawtypes")
	private List recipes = new ArrayList();
    @SuppressWarnings("unused")
	private static final String __OBFID = "CL_00000090";

    /**
     * Returns the static instance of this class
     */
    public static final NuclearWorkspaceCraftingManager getInstance()
    {
        /** The static instance of this class */
        return instance;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	private NuclearWorkspaceCraftingManager()
    {
    	recipes = new ArrayList();
    	
    	this.addRecipe(new ItemStack(NRBlocks.machineBlock, 1), new Object[] {"PLCLP", "LBRBL", "GTITG", "LBRBL", "PLCLP", 'P', new ItemStack(NRItems.parts, 1, 0), 'L', new ItemStack(NRItems.parts, 1, 14), 'C', new ItemStack(NRItems.parts, 1, 12), 'B', new ItemStack (NRItems.parts, 1, 16), 'R', Items.redstone, 'G', new ItemStack (NRItems.parts, 1, 11), 'T', new ItemStack (NRItems.parts, 1, 13), 'I', new ItemStack (NRItems.parts, 1, 10)});
    	this.addRecipe(new ItemStack(NRBlocks.reactorBlock, 16), new Object[] {"NNNNN", "NI IN", "N   N", "NI IN", "NNNNN", 'N', new ItemStack(NRItems.parts, 1, 0), 'I', new ItemStack (NRItems.material, 1, 7)});
    	this.addRecipe(new ItemStack(NRBlocks.cellBlock, 4), new Object[] {"NNTNN", "NPLPN", "TL LT", "NPLPN", "NNTNN", 'N', new ItemStack(NRItems.parts, 1, 0), 'P', new ItemStack(NRItems.parts, 1, 14), 'L', Blocks.glass, 'T', new ItemStack (NRItems.material, 1, 7)});
    	this.addRecipe(new ItemStack(NRBlocks.coolerBlock, 12), new Object[] {"UNNNU", "NUNUN", "RNUNR", "NUNUN", "UNNNU", 'N', new ItemStack(NRItems.parts, 1, 0), 'R', Items.redstone, 'U', new ItemStack(NRItems.parts, 1, 4)});
    	this.addRecipe(new ItemStack(NRBlocks.speedBlock, 4), new Object[] {"UNNNU", "NUNUN", "RNUNR", "NUNUN", "UNNNU", 'N', new ItemStack(NRItems.parts, 1, 0), 'R', Items.blaze_powder, 'U', Items.redstone});
    	this.addRecipe(new ItemStack(NRBlocks.fissionReactorGraphiteIdle, 1), new Object[] {"TNNNT", "NUUUN", "NUMUN", "NUUUN", "TNNNT", 'N', new ItemStack(NRItems.parts, 1, 3), 'U', new ItemStack(NRItems.parts, 1, 0), 'T', new ItemStack(NRItems.material, 1, 7), 'M', NRBlocks.machineBlock});
    	this.addRecipe(new ItemStack(NRBlocks.blastBlock, 16), new Object[] {"UNNU", "NUUN", "NUUN", "UNNU", 'N', NRBlocks.reactorBlock, 'U', Blocks.obsidian});
    	
    	this.addRecipe(new ItemStack(NRItems.parts, 4, 3), new Object[] {"RTTR", "TNNT", "TNNT", "RTTR", 'N', new ItemStack(NRItems.parts, 1, 0), 'T', new ItemStack (NRItems.material, 1, 7), 'R', Items.redstone});
    	this.addRecipe(new ItemStack(NRItems.parts, 4, 9), new Object[] {"RTTTR", "TNNNT", "TNNNT", "TNNNT", "RTTTR", 'N', new ItemStack(NRItems.parts, 1, 8), 'T', new ItemStack (NRItems.material, 1, 48), 'R', new ItemStack(NRItems.parts, 1, 4)});
    	this.addRecipe(new ItemStack(NRItems.parts, 3, 5), new Object[] {"RTTTR", "T   T", "T   T", "RTTTR", 'T', new ItemStack(NRItems.parts, 1, 0), 'R', new ItemStack(NRItems.parts, 1, 4)});
    	this.addRecipe(new ItemStack(NRItems.parts, 12, 0), new Object[] {"RRRRR", "TTTTT", 'T', new ItemStack(NRItems.material, 1, 22), 'R', new ItemStack(NRItems.material, 1, 7)});
    	this.addRecipe(new ItemStack(NRItems.parts, 1, 7), new Object[] {" NN ", "NWWN", "NWWN", " NN ", 'N', new ItemStack(NRItems.parts, 1, 6), 'W', new ItemStack(NRItems.fuel, 1, 34)});
    	this.addRecipe(new ItemStack(NRItems.parts, 2, 8), new Object[] {"  W  ", " WNW ", "WNWNW", " WNW ", "  W  ", 'N', new ItemStack(NRItems.parts, 1, 3), 'W', new ItemStack(NRItems.material, 1, 24)});
    	this.addRecipe(new ItemStack(NRItems.parts, 2, 8), new Object[] {"  W  ", " WNW ", "WNWNW", " WNW ", "  W  ", 'N', new ItemStack(NRItems.parts, 1, 3), 'W', new ItemStack(NRItems.material, 1, 55)});
    	
    	this.addRecipe(new ItemStack(NRItems.upgrade, 1), new Object[] {"  T  ", " TTT ", "TTNTT", " TTT ", "  T  ", 'N', new ItemStack(NRItems.parts, 1, 3), 'T', Items.redstone});
    	this.addRecipe(new ItemStack(NRItems.upgradeSpeed, 1), new Object[] {"  T  ", " TTT ", "TTNTT", " TTT ", "  T  ", 'N', new ItemStack(NRItems.parts, 1, 1), 'T', new ItemStack (Items.dye, 1, 4)});
    	this.addRecipe(new ItemStack(NRItems.upgradeEnergy, 1), new Object[] {"  T  ", " TTT ", "TTNTT", " TTT ", "  T  ", 'N', new ItemStack(NRItems.parts, 1, 1), 'T', new ItemStack (NRItems.parts, 1, 4)});
    	
    	this.addRecipe(new ItemStack(NRBlocks.accStraight1, 4), new Object[] {" NN ", "NIIN", " NN ", 'N', new ItemStack(NRItems.parts, 1, 14), 'I', new ItemStack(NRItems.parts, 1, 1)});
    	this.addRecipe(new ItemStack(NRBlocks.accStraight2, 4), new Object[] {" N ", "NIN", "NIN", " N ", 'N', new ItemStack(NRItems.parts, 1, 14), 'I', new ItemStack(NRItems.parts, 1, 1)});

    	this.addRecipe(new ItemStack(NRBlocks.separatorIdle, 1), new Object[] {"TNNNT", "NUUUN", "NUMUN", "NUUUN", "TNNNT", 'N', new ItemStack(NRItems.parts, 1, 14), 'T', Items.redstone, 'U', new ItemStack(NRItems.material, 1, 7), 'M', NRBlocks.machineBlock});
    	this.addRecipe(new ItemStack(NRBlocks.hastenerIdle, 1), new Object[] {"TNNNT", "NUUUN", "NUMUN", "NUUUN", "TNNNT", 'N', new ItemStack(NRItems.parts, 1, 14), 'T', new ItemStack (NRItems.material, 1, 7), 'U', new ItemStack(NRItems.parts, 1, 4), 'M', NRBlocks.machineBlock});
    	this.addRecipe(new ItemStack(NRBlocks.collectorIdle, 1), new Object[] {" NN ", "NUUN", "NUUN", " NN ", 'N', new ItemStack(NRItems.parts, 1, 0), 'U', new ItemStack(NRItems.material, 1, 40)});
    	this.addRecipe(new ItemStack(NRBlocks.reactionGeneratorIdle, 1), new Object[] {"TNNNT", "NLULN", "NUMUN", "NLULN", "TNNNT", 'N', Items.redstone, 'T', new ItemStack (NRItems.parts, 1, 0), 'L', new ItemStack (NRItems.parts, 1, 14), 'U', new ItemStack(NRItems.parts, 1, 5), 'M', NRBlocks.machineBlock});
    	this.addRecipe(new ItemStack(NRBlocks.electrolyserIdle, 1), new Object[] {"TNNNT", "NLLLN", "NUMUN", "NLLLN", "TNNNT", 'N', new ItemStack (NRItems.parts, 1, 3), 'T', new ItemStack (NRItems.parts, 1, 4), 'L', new ItemStack (NRItems.parts, 1, 14), 'U', new ItemStack(NRItems.parts, 1, 7), 'M', NRBlocks.machineBlock});
    	this.addRecipe(new ItemStack(NRBlocks.oxidiserIdle, 1), new Object[] {"TNNNT", "NUUUN", "NUMUN", "NUUUN", "TNNNT", 'N', new ItemStack(NRItems.parts, 1, 14), 'T', new ItemStack(NRItems.parts, 1, 8), 'U', new ItemStack(NRItems.parts, 1, 4), 'M', NRBlocks.machineBlock});
    	this.addRecipe(new ItemStack(NRBlocks.ioniserIdle, 1), new Object[] {"TNNNT", "NUUUN", "NUMUN", "NUUUN", "TNNNT", 'N', new ItemStack(NRItems.parts, 1, 14), 'T', new ItemStack(NRItems.parts, 1, 8), 'U', Items.redstone, 'M', NRBlocks.machineBlock});
    	this.addRecipe(new ItemStack(NRBlocks.irradiatorIdle, 1), new Object[] {"TNNNT", "NUUUN", "NUMUN", "NUUUN", "TNNNT", 'N', new ItemStack(NRItems.material, 1, 7), 'T', new ItemStack(NRItems.parts, 1, 8), 'U', new ItemStack(NRItems.parts, 1, 4), 'M', NRBlocks.machineBlock});
    	this.addRecipe(new ItemStack(NRBlocks.coolerIdle, 1), new Object[] {"TNTNT", "NUUUN", "TUMUT", "NUUUN", "TNTNT", 'N', Blocks.quartz_block, 'T', new ItemStack(NRItems.parts, 1, 8), 'U', new ItemStack(NRItems.parts, 1, 4), 'M', NRBlocks.machineBlock});
    	this.addRecipe(new ItemStack(NRBlocks.factoryIdle, 1), new Object[] {"TNNNT", "NUUUN", "NUMUN", "NUUUN", "TNNNT", 'T', new ItemStack(NRItems.material, 1, 7), 'N', new ItemStack(NRItems.parts, 1, 0), 'U', new ItemStack(NRItems.parts, 1, 1), 'M', Blocks.piston});
    	
    	this.addRecipe(new ItemStack(NRBlocks.fusionReactor, 1), new Object[] {"TNNNT", "NTNTN", "NNUNN", "NTNTN", "TNNNT", 'N', new ItemStack(NRItems.parts, 1, 9), 'T', new ItemStack (NRBlocks.reactionGeneratorIdle, 1), 'U', new ItemStack(NRItems.parts, 1, 5)});
    	
    	this.addRecipe(new ItemStack(NRItems.toughAlloySword, 1), new Object[] {"T", "T", "T", "S", 'T', new ItemStack (NRItems.material, 1, 7), 'S', Items.iron_ingot});
    	this.addRecipe(new ItemStack(NRItems.toughAlloyPickaxe, 1), new Object[] {"TTT", " S ", " S ", " S ", 'T', new ItemStack (NRItems.material, 1, 7), 'S', Items.iron_ingot});
    	this.addRecipe(new ItemStack(NRItems.toughAlloyShovel, 1), new Object[] {"T", "S", "S", "S", 'T', new ItemStack (NRItems.material, 1, 7), 'S', Items.iron_ingot});
    	this.addRecipe(new ItemStack(NRItems.toughAlloyAxe, 1), new Object[] {"TT", "TS", " S", " S", 'T', new ItemStack (NRItems.material, 1, 7), 'S', Items.iron_ingot});
    	this.addRecipe(new ItemStack(NRItems.toughAlloyHoe, 1), new Object[] {"TT", " S", " S", " S", 'T', new ItemStack (NRItems.material, 1, 7), 'S', Items.iron_ingot});
    	this.addRecipe(new ItemStack(NRItems.toughAlloyAxe, 1), new Object[] {"TT", "ST", "S ", "S ", 'T', new ItemStack (NRItems.material, 1, 7), 'S', Items.iron_ingot});
    	this.addRecipe(new ItemStack(NRItems.toughAlloyHoe, 1), new Object[] {"TT", "S ", "S ", "S ", 'T', new ItemStack (NRItems.material, 1, 7), 'S', Items.iron_ingot});
    	this.addRecipe(new ItemStack(NRItems.toughAlloyPaxel, 1), new Object[] {"ASP", "HIW", " I ", " I ", 'A', NRItems.toughAlloyAxe, 'S', NRItems.toughAlloyShovel, 'P', NRItems.toughAlloyPickaxe, 'H', NRItems.toughAlloyHoe, 'W', NRItems.toughAlloySword, 'I', Items.iron_ingot});
    	this.addRecipe(new ItemStack(NRItems.toughBow, 1), new Object[] {" TS", "T S", "T S", "T S", " TS", 'T', new ItemStack (NRItems.material, 1, 7), 'S', Items.string});
    	
    	this.addRecipe(new ItemStack(NRItems.toughHelm, 1), new Object[] {"TTTT", "T  T", "T  T", 'T', new ItemStack (NRItems.material, 1, 7)});
    	this.addRecipe(new ItemStack(NRItems.toughChest, 1), new Object[] {"T  T", "TTTT", "TTTT", "TTTT", 'T', new ItemStack (NRItems.material, 1, 7)});
    	this.addRecipe(new ItemStack(NRItems.toughLegs, 1), new Object[] {"TTT", "T T", "T T", "T T", 'T', new ItemStack (NRItems.material, 1, 7)});
    	this.addRecipe(new ItemStack(NRItems.toughBoots, 1), new Object[] {"T  T", "T  T", "T  T", 'T', new ItemStack (NRItems.material, 1, 7)});
    	
    	this.addRecipe(new ItemStack(NRItems.dUSword, 1), new Object[] {"T", "T", "T", "S", 'T', new ItemStack (NRItems.parts, 1, 8), 'S', Items.iron_ingot});
    	this.addRecipe(new ItemStack(NRItems.dUPickaxe, 1), new Object[] {"TTT", " S ", " S ", " S ", 'T', new ItemStack (NRItems.parts, 1, 8), 'S', Items.iron_ingot});
    	this.addRecipe(new ItemStack(NRItems.dUShovel, 1), new Object[] {"T", "S", "S", "S", 'T', new ItemStack (NRItems.parts, 1, 8), 'S', Items.iron_ingot});
    	this.addRecipe(new ItemStack(NRItems.dUAxe, 1), new Object[] {"TT", "TS", " S", " S", 'T', new ItemStack (NRItems.parts, 1, 8), 'S', Items.iron_ingot});
    	this.addRecipe(new ItemStack(NRItems.dUHoe, 1), new Object[] {"TT", " S", " S", " S", 'T', new ItemStack (NRItems.parts, 1, 8), 'S', Items.iron_ingot});
    	this.addRecipe(new ItemStack(NRItems.dUAxe, 1), new Object[] {"TT", "ST", "S ", "S ", 'T', new ItemStack (NRItems.parts, 1, 8), 'S', Items.iron_ingot});
    	this.addRecipe(new ItemStack(NRItems.dUHoe, 1), new Object[] {"TT", "S ", "S ", "S ", 'T', new ItemStack (NRItems.parts, 1, 8), 'S', Items.iron_ingot});
    	this.addRecipe(new ItemStack(NRItems.dUPaxel, 1), new Object[] {"ASP", "HIW", " I ", " I ", 'A', NRItems.dUAxe, 'S', NRItems.dUShovel, 'P', NRItems.dUPickaxe, 'H', NRItems.dUHoe, 'W', NRItems.dUSword, 'I', Items.iron_ingot});
    	
    	this.addRecipe(new ItemStack(NRItems.dUHelm, 1), new Object[] {"TTTT", "T  T", "T  T", 'T', new ItemStack (NRItems.parts, 1, 8)});
    	this.addRecipe(new ItemStack(NRItems.dUChest, 1), new Object[] {"T  T", "TTTT", "TTTT", "TTTT", 'T', new ItemStack (NRItems.parts, 1, 8)});
    	this.addRecipe(new ItemStack(NRItems.dULegs, 1), new Object[] {"TTT", "T T", "T T", "T T", 'T', new ItemStack (NRItems.parts, 1, 8)});
    	this.addRecipe(new ItemStack(NRItems.dUBoots, 1), new Object[] {"T  T", "T  T", "T  T", 'T', new ItemStack (NRItems.parts, 1, 8)});
    	
    	this.addRecipe(new ItemStack(NRBlocks.blockBlock, 1, 7), new Object[] {"TTTTT", "TTTTT", "TTTTT", "TTTTT", "TTTTT", 'T', new ItemStack (NRItems.material, 1, 7)});
    	this.addRecipe(new ItemStack(NRItems.material, 25, 7), new Object[] {"T", 'T', new ItemStack (NRBlocks.blockBlock, 1, 7)});
    	
    	this.addRecipe(new ItemStack(NRItems.fuel, 4, 48), new Object[] {"TXT", "XAX", "TXT", 'X', new ItemStack(NRItems.parts, 1, 0), 'A', new ItemStack(NRItems.material, 1, 7), 'T', new ItemStack (NRItems.parts, 1, 3)});
    	this.addRecipe(new ItemStack(NRItems.fuel, 1, 46), new Object[] {"T", "X", 'X', new ItemStack(NRItems.material, 1, 30), 'T', new ItemStack(NRItems.fuel, 1, 48)});
    	this.addRecipe(new ItemStack(NRItems.fuel, 1, 46), new Object[] {"X", "T", 'X', new ItemStack(NRItems.material, 1, 30), 'T', new ItemStack(NRItems.fuel, 1, 48)});
    	this.addRecipe(new ItemStack(NRItems.fuel, 1, 46), new Object[] {"TX", 'X', new ItemStack(NRItems.material, 1, 30), 'T', new ItemStack(NRItems.fuel, 1, 48)});
    	this.addRecipe(new ItemStack(NRItems.fuel, 1, 46), new Object[] {"XT", 'X', new ItemStack(NRItems.material, 1, 30), 'T', new ItemStack(NRItems.fuel, 1, 48)});
    	this.addRecipe(new ItemStack(NRItems.fuel, 1, 46), new Object[] {"T", "X", 'X', new ItemStack(NRItems.material, 1, 61), 'T', new ItemStack(NRItems.fuel, 1, 48)});
    	this.addRecipe(new ItemStack(NRItems.fuel, 1, 46), new Object[] {"X", "T", 'X', new ItemStack(NRItems.material, 1, 61), 'T', new ItemStack(NRItems.fuel, 1, 48)});
    	this.addRecipe(new ItemStack(NRItems.fuel, 1, 46), new Object[] {"TX", 'X', new ItemStack(NRItems.material, 1, 61), 'T', new ItemStack(NRItems.fuel, 1, 48)});
    	this.addRecipe(new ItemStack(NRItems.fuel, 1, 46), new Object[] {"XT", 'X', new ItemStack(NRItems.material, 1, 61), 'T', new ItemStack(NRItems.fuel, 1, 48)});
    	this.addRecipe(new ItemStack(NRBlocks.RTG, 1), new Object[] {"CTC", "TXT", "CTC", 'T', new ItemStack(NRItems.parts, 1, 3), 'C', new ItemStack(NRItems.parts, 1, 15), 'X', new ItemStack(NRItems.fuel, 1, 46)});
    	this.addRecipe(new ItemStack(NRBlocks.WRTG, 1), new Object[] {"PPPPP", "PUUUP", "PUUUP", "PUUUP", "PPPPP", 'P', new ItemStack(NRItems.parts, 1, 0), 'U', new ItemStack(NRItems.material, 1, 24)});
    	this.addRecipe(new ItemStack(NRBlocks.WRTG, 1), new Object[] {"PPPPP", "PUUUP", "PUUUP", "PUUUP", "PPPPP", 'P', new ItemStack(NRItems.parts, 1, 0), 'U', new ItemStack(NRItems.material, 1, 55)});
    	this.addRecipe(new ItemStack(NRBlocks.nuke, 1), new Object[] {"RLLLR", "LPTPL", "LPTPL", "LPTPL", "RLLLR", 'T', Items.gunpowder, 'L', new ItemStack(NRItems.parts, 1, 0), 'R', new ItemStack(NRItems.parts, 1, 3), 'P', new ItemStack(NRItems.material, 1, 36)});
    	this.addRecipe(new ItemStack(NRBlocks.nuke, 1), new Object[] {"RLLLR", "LPTPL", "LPTPL", "LPTPL", "RLLLR", 'T', Items.gunpowder, 'L', new ItemStack(NRItems.parts, 1, 0), 'R', new ItemStack(NRItems.parts, 1, 3), 'P', new ItemStack(NRItems.material, 1, 67)});
    	this.addRecipe(new ItemStack(NRItems.nuclearGrenade, 3), new Object[] {"    S", "   S ", "TGT  ", "GNG  ", "TGT  ", 'T', new ItemStack(NRItems.material, 1, 7), 'N', NRBlocks.nuke, 'S', Items.string, 'G', Items.gunpowder});
    	this.addRecipe(new ItemStack(NRItems.portableEnderChest, 1), new Object[] {"SSS", "WPW", "OEO", "WPW", 'P', new ItemStack(NRItems.parts, 1, 0), 'E', Items.ender_eye, 'S', Items.string, 'O', Blocks.obsidian, 'W', Blocks.wool});
    	
        Collections.sort(this.recipes, new NuclearWorkspaceRecipeSorter(this));
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public NuclearWorkspaceShapedRecipes addRecipe(ItemStack p_92103_1_, Object ... p_92103_2_)
    {
        String s = "";
        int i = 0;
        int j = 0;
        int k = 0;

        if (p_92103_2_[i] instanceof String[])
        {
            String[] astring = (String[])((String[])p_92103_2_[i++]);

            for (int l = 0; l < astring.length; ++l)
            {
                String s1 = astring[l];
                ++k;
                j = s1.length();
                s = s + s1;
            }
        }
        else
        {
            while (p_92103_2_[i] instanceof String)
            {
                String s2 = (String)p_92103_2_[i++];
                ++k;
                j = s2.length();
                s = s + s2;
            }
        }

        HashMap hashmap;

        for (hashmap = new HashMap(); i < p_92103_2_.length; i += 2)
        {
            Character character = (Character)p_92103_2_[i];
            ItemStack itemstack1 = null;

            if (p_92103_2_[i + 1] instanceof Item)
            {
                itemstack1 = new ItemStack((Item)p_92103_2_[i + 1]);
            }
            else if (p_92103_2_[i + 1] instanceof Block)
            {
                itemstack1 = new ItemStack((Block)p_92103_2_[i + 1], 1, 32767);
            }
            else if (p_92103_2_[i + 1] instanceof ItemStack)
            {
                itemstack1 = (ItemStack)p_92103_2_[i + 1];
            }

            hashmap.put(character, itemstack1);
        }

        ItemStack[] aitemstack = new ItemStack[j * k];

        for (int i1 = 0; i1 < j * k; ++i1)
        {
            char c0 = s.charAt(i1);

            if (hashmap.containsKey(Character.valueOf(c0)))
            {
                aitemstack[i1] = ((ItemStack)hashmap.get(Character.valueOf(c0))).copy();
            }
            else
            {
                aitemstack[i1] = null;
            }
        }

        NuclearWorkspaceShapedRecipes shapedrecipes = new NuclearWorkspaceShapedRecipes(j, k, aitemstack, p_92103_1_);
        this.recipes.add(shapedrecipes);
        return shapedrecipes;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public NuclearWorkspaceShapelessRecipes addShapelessRecipe(ItemStack p_77596_1_, Object ... p_77596_2_)
    {
        ArrayList arraylist = new ArrayList();
        Object[] aobject = p_77596_2_;
        int i = p_77596_2_.length;

        for (int j = 0; j < i; ++j)
        {
            Object object1 = aobject[j];

            if (object1 instanceof ItemStack)
            {
                arraylist.add(((ItemStack)object1).copy());
            }
            else if (object1 instanceof Item)
            {
                arraylist.add(new ItemStack((Item)object1));
            }
            else
            {
                if (!(object1 instanceof Block))
                {
                    throw new RuntimeException("Invalid shapeless recipe!");
                }

                arraylist.add(new ItemStack((Block)object1));
            }
        }

        NuclearWorkspaceShapelessRecipes shapelessrecipes = new NuclearWorkspaceShapelessRecipes(p_77596_1_, arraylist);
        this.recipes.add(shapelessrecipes);
        return shapelessrecipes;
    }

    public ItemStack findMatchingRecipe(InventoryCrafting p_82787_1_, World p_82787_2_)
    {
        int i = 0;
        ItemStack itemstack = null;
        ItemStack itemstack1 = null;
        int j;

        for (j = 0; j < p_82787_1_.getSizeInventory(); ++j)
        {
            ItemStack itemstack2 = p_82787_1_.getStackInSlot(j);

            if (itemstack2 != null)
            {
                if (i == 0)
                {
                    itemstack = itemstack2;
                }

                if (i == 1)
                {
                    itemstack1 = itemstack2;
                }

                ++i;
            }
        }

        if (i == 2 && itemstack.getItem() == itemstack1.getItem() && itemstack.stackSize == 1 && itemstack1.stackSize == 1 && itemstack.getItem().isRepairable())
        {
            Item item = itemstack.getItem();
            int j1 = item.getMaxDamage() - itemstack.getItemDamageForDisplay();
            int k = item.getMaxDamage() - itemstack1.getItemDamageForDisplay();
            int l = j1 + k + item.getMaxDamage() * 5 / 100;
            int i1 = item.getMaxDamage() - l;

            if (i1 < 0)
            {
                i1 = 0;
            }

            return new ItemStack(itemstack.getItem(), 1, i1);
        }
        else
        {
            for (j = 0; j < this.recipes.size(); ++j)
            {
                IRecipe irecipe = (IRecipe)this.recipes.get(j);

                if (irecipe.matches(p_82787_1_, p_82787_2_))
                {
                    return irecipe.getCraftingResult(p_82787_1_);
                }
            }

            return null;
        }
    }

    /**
     * returns the List<> of all recipes
     */
    @SuppressWarnings("rawtypes")
	public List getRecipeList()
    {
        return this.recipes;
    }
}