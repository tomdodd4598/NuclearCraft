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

import com.nr.mod.NuclearRelativistics;
import com.nr.mod.blocks.NRBlocks;
import com.nr.mod.items.NRItems;

public class NuclearWorkspaceCraftingManager
{
    /** The static instance of this class */
    public static final NuclearWorkspaceCraftingManager instance = new NuclearWorkspaceCraftingManager();
    /** A list of all the recipes added */
    @SuppressWarnings("rawtypes")
	public List recipes = new ArrayList();

    /**
     * Returns the static instance of this class
     */
    public static final NuclearWorkspaceCraftingManager getInstance()
    {
        /** The static instance of this class */
        return instance;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public NuclearWorkspaceCraftingManager()
    {
    	recipes = new ArrayList();
    	
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRBlocks.machineBlock, 1), true, new Object[] {"PLCLP", "LBRBL", "GTITG", "LBRBL", "PLCLP", 'P', new ItemStack(NRItems.parts, 1, 0), 'L', new ItemStack(NRItems.parts, 1, 14), 'C', new ItemStack(NRItems.parts, 1, 12), 'B', new ItemStack (NRItems.parts, 1, 16), 'R', Items.redstone, 'G', new ItemStack (NRItems.parts, 1, 11), 'T', new ItemStack (NRItems.parts, 1, 13), 'I', new ItemStack (NRItems.parts, 1, 10)}));
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRBlocks.reactorBlock, 16), true, new Object[] {"NNNNN", "NI IN", "N   N", "NI IN", "NNNNN", 'N', new ItemStack(NRItems.parts, 1, 0), 'I', "ingotTough"}));
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRBlocks.cellBlock, 4), true, new Object[] {"NNTNN", "NPLPN", "TL LT", "NPLPN", "NNTNN", 'N', new ItemStack(NRItems.parts, 1, 0), 'P', new ItemStack(NRItems.parts, 1, 14), 'L', "blockGlass", 'T', "ingotTough"}));
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRBlocks.coolerBlock, 10), true, new Object[] {"UNUNU", "NUNUN", "UNUNU", "NUNUN", "UNUNU", 'N', new ItemStack(NRItems.parts, 1, 0), 'R', Items.redstone, 'U', "universalReactant"}));
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRBlocks.speedBlock, 6), true, new Object[] {"UNRNU", "NUNUN", "RNUNR", "NUNUN", "UNRNU", 'N', new ItemStack(NRItems.parts, 1, 0), 'R', Items.blaze_powder, 'U', Items.redstone}));
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRBlocks.fissionReactorGraphiteIdle, 1), true, new Object[] {"TNNNT", "NUUUN", "NUMUN", "NUUUN", "TNNNT", 'N', new ItemStack(NRItems.parts, 1, 3), 'U', new ItemStack(NRItems.parts, 1, 0), 'T', "ingotTough", 'M', NRBlocks.machineBlock}));
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRBlocks.blastBlock, 16), true, new Object[] {"UNNNU", "NUUUN", "NUUUN", "NUUUN", "UNNNU", 'N', NRBlocks.reactorBlock, 'U', "oreObsidian"}));
    	
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRItems.parts, 4, 3), true, new Object[] {"  T  ", " TNT ", "TNRNT", " TNT ", "  T  ", 'N', new ItemStack(NRItems.parts, 1, 0), 'T', "ingotTough", 'R', Items.redstone}));
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRItems.parts, 4, 9), true, new Object[] {"RTTTR", "TNNNT", "TNNNT", "TNNNT", "RTTTR", 'N', new ItemStack(NRItems.parts, 1, 8), 'T', new ItemStack (NRItems.material, 1, 48), 'R', "universalReactant"}));
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRItems.parts, 3, 5), true, new Object[] {"RTTTR", "T   T", "T   T", "RTTTR", 'T', new ItemStack(NRItems.parts, 1, 0), 'R', "universalReactant"}));
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRItems.parts, 20, 0), true, new Object[] {"RRRRR", "TTTTT", 'T', "dustTough", 'R', "ingotTough"}));
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRItems.parts, 1, 7), true, new Object[] {" NNN ", "NWWWN", "NWWWN", " NNN ", 'N', new ItemStack(NRItems.parts, 1, 6), 'W', new ItemStack(NRItems.fuel, 1, 34)}));
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRItems.parts, 2, 8), true, new Object[] {"  W  ", " WNW ", "WNWNW", " WNW ", "  W  ", 'N', new ItemStack(NRItems.parts, 1, 3), 'W', "U238"}));
    	
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRItems.upgrade, 1), true, new Object[] {"  T  ", " TTT ", "TTNTT", " TTT ", "  T  ", 'N', new ItemStack(NRItems.parts, 1, 3), 'T', Items.redstone}));
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRItems.upgradeSpeed, 1), true, new Object[] {"TTT", "TNT", "TTT", 'N', new ItemStack(NRItems.parts, 1, 1), 'T', "dustLapis"}));
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRItems.upgradeEnergy, 1), true, new Object[] {"TTT", "TNT", "TTT", 'N', new ItemStack(NRItems.parts, 1, 1), 'T', "universalReactant"}));
    	
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRBlocks.accStraight1, 8), true, new Object[] {" NNN ", "NIIIN", " NNN ", 'N', new ItemStack(NRItems.parts, 1, 14), 'I', new ItemStack(NRItems.parts, 1, 1)}));
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRBlocks.accStraight2, 8), true, new Object[] {" N ", "NIN", "NIN", "NIN", " N ", 'N', new ItemStack(NRItems.parts, 1, 14), 'I', new ItemStack(NRItems.parts, 1, 1)}));

    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRBlocks.separatorIdle, 1), true, new Object[] {"TNNNT", "NUUUN", "NUMUN", "NUUUN", "TNNNT", 'N', new ItemStack(NRItems.parts, 1, 14), 'T', Items.redstone, 'U', "ingotTough", 'M', NRBlocks.machineBlock}));
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRBlocks.hastenerIdle, 1), true, new Object[] {"TNNNT", "NUUUN", "NUMUN", "NUUUN", "TNNNT", 'N', new ItemStack(NRItems.parts, 1, 14), 'T', "ingotTough", 'U', "universalReactant", 'M', NRBlocks.machineBlock}));
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRBlocks.collectorIdle, 1), true, new Object[] {" NNN ", "NUUUN", "NUUUN", "NUUUN", " NNN ", 'N', new ItemStack(NRItems.parts, 1, 0), 'U', new ItemStack(NRItems.material, 1, 40)}));
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRBlocks.reactionGeneratorIdle, 1), true, new Object[] {"TNNNT", "NLULN", "NUMUN", "NLULN", "TNNNT", 'N', Items.redstone, 'T', new ItemStack (NRItems.parts, 1, 0), 'L', new ItemStack (NRItems.parts, 1, 14), 'U', new ItemStack(NRItems.parts, 1, 5), 'M', NRBlocks.machineBlock}));
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRBlocks.electrolyserIdle, 1), true, new Object[] {"TNNNT", "NLLLN", "NUMUN", "NLLLN", "TNNNT", 'N', new ItemStack (NRItems.parts, 1, 3), 'T', "universalReactant", 'L', new ItemStack (NRItems.parts, 1, 14), 'U', new ItemStack(NRItems.parts, 1, 7), 'M', NRBlocks.machineBlock}));
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRBlocks.oxidiserIdle, 1), true, new Object[] {"TNNNT", "NUUUN", "NUMUN", "NUUUN", "TNNNT", 'N', new ItemStack(NRItems.parts, 1, 14), 'T', new ItemStack(NRItems.parts, 1, 8), 'U', "universalReactant", 'M', NRBlocks.machineBlock}));
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRBlocks.ioniserIdle, 1), true, new Object[] {"TNNNT", "NUUUN", "NUMUN", "NUUUN", "TNNNT", 'N', new ItemStack(NRItems.parts, 1, 14), 'T', new ItemStack(NRItems.parts, 1, 8), 'U', Items.redstone, 'M', NRBlocks.machineBlock}));
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRBlocks.irradiatorIdle, 1), true, new Object[] {"TNNNT", "NUUUN", "NUMUN", "NUUUN", "TNNNT", 'N', "ingotTough", 'T', new ItemStack(NRItems.parts, 1, 8), 'U', "universalReactant", 'M', NRBlocks.machineBlock}));
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRBlocks.coolerIdle, 1), true, new Object[] {"TNTNT", "NUUUN", "TUMUT", "NUUUN", "TNTNT", 'N', Blocks.quartz_block, 'T', new ItemStack(NRItems.parts, 1, 8), 'U', "universalReactant", 'M', NRBlocks.machineBlock}));
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRBlocks.factoryIdle, 1), true, new Object[] {"TNNNT", "NUUUN", "NUMUN", "NUUUN", "TNNNT", 'T', "ingotTough", 'N', new ItemStack(NRItems.parts, 1, 0), 'U', new ItemStack(NRItems.parts, 1, 1), 'M', Blocks.piston}));
    	
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRBlocks.fusionReactor, 1), true, new Object[] {"TNNNT", "NTNTN", "NNUNN", "NTNTN", "TNNNT", 'N', new ItemStack(NRItems.parts, 1, 9), 'T', new ItemStack (NRBlocks.reactionGeneratorIdle, 1), 'U', new ItemStack(NRItems.parts, 1, 5)}));
    	
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRItems.toughAlloySword, 1), true, new Object[] {" T ", " T ", " T ", "STS", " S ", 'T', "ingotTough", 'S', "ingotIron"}));
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRItems.toughAlloyPickaxe, 1), true, new Object[] {" TTT ", "T S T", "  S  ", "  S  ", "  S  ", 'T', "ingotTough", 'S', "ingotIron"}));
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRItems.toughAlloyShovel, 1), true, new Object[] {"T", "T", "S", "S", "S", 'T', "ingotTough", 'S', "ingotIron"}));
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRItems.toughAlloyAxe, 1), true, new Object[] {" TT", "TTS", " TS", "  S", "  S", 'T', "ingotTough", 'S', "ingotIron"}));
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRItems.toughAlloyHoe, 1), true, new Object[] {" TT", "T S", "  S", "  S", "  S", 'T', "ingotTough", 'S', "ingotIron"}));
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRItems.toughAlloyAxe, 1), true, new Object[] {"TT ", "STT", "ST ", "S  ", "S  ", 'T', "ingotTough", 'S', "ingotIron"}));
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRItems.toughAlloyHoe, 1), true, new Object[] {"TT ", "S T", "S  ", "S  ", "S  ", 'T', "ingotTough", 'S', "ingotIron"}));
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRItems.toughAlloyPaxel, 1), true, new Object[] {"ASP", "HIW", " I ", " I ", " I ", 'A', NRItems.toughAlloyAxe, 'S', NRItems.toughAlloyShovel, 'P', NRItems.toughAlloyPickaxe, 'H', NRItems.toughAlloyHoe, 'W', NRItems.toughAlloySword, 'I', "ingotIron"}));
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRItems.toughBow, 1), true, new Object[] {" TS", "T S", "T S", "T S", " TS", 'T', "ingotTough", 'S', Items.string}));
    	
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRItems.toughHelm, 1), true, new Object[] {" TTT ", "TTTTT", "T   T", "T   T", 'T', "ingotTough"}));
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRItems.toughChest, 1), true, new Object[] {"T   T", "TT TT", "TTTTT", " TTT ", " TTT ", 'T', "ingotTough"}));
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRItems.toughLegs, 1), true, new Object[] {"TTT", "TTT", "T T", "T T", "T T", 'T', "ingotTough"}));
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRItems.toughBoots, 1), true, new Object[] {" T T ", " T T ", " T T ", "T   T", 'T', "ingotTough"}));
    	
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRItems.dUSword, 1), true, new Object[] {" T ", " T ", " T ", "STS", " S ", 'T', new ItemStack (NRItems.parts, 1, 8), 'S', "ingotIron"}));
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRItems.dUPickaxe, 1), true, new Object[] {" TTT ", "T S T", "  S  ", "  S  ", "  S  ", 'T', new ItemStack (NRItems.parts, 1, 8), 'S', "ingotIron"}));
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRItems.dUShovel, 1), true, new Object[] {"T", "T", "S", "S", "S", 'T', new ItemStack (NRItems.parts, 1, 8), 'S', "ingotIron"}));
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRItems.dUAxe, 1), true, new Object[] {" TT", "TTS", " TS", "  S", "  S", 'T', new ItemStack (NRItems.parts, 1, 8), 'S', "ingotIron"}));
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRItems.dUHoe, 1), true, new Object[] {" TT", "T S", "  S", "  S", "  S", 'T', new ItemStack (NRItems.parts, 1, 8), 'S', "ingotIron"}));
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRItems.dUAxe, 1), true, new Object[] {"TT ", "STT", "ST ", "S  ", "S  ", 'T', new ItemStack (NRItems.parts, 1, 8), 'S', "ingotIron"}));
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRItems.dUHoe, 1), true, new Object[] {"TT ", "S T", "S  ", "S  ", "S  ", 'T', new ItemStack (NRItems.parts, 1, 8), 'S', "ingotIron"}));
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRItems.dUPaxel, 1), true, new Object[] {"ASP", "HIW", " I ", " I ", " I ", 'A', NRItems.dUAxe, 'S', NRItems.dUShovel, 'P', NRItems.dUPickaxe, 'H', NRItems.dUHoe, 'W', NRItems.dUSword, 'I', "ingotIron"}));
    	
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRItems.dUHelm, 1), true, new Object[] {" TTT ", "TTTTT", "T   T", "T   T", 'T', new ItemStack (NRItems.parts, 1, 8)}));
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRItems.dUChest, 1), true, new Object[] {"T   T", "TT TT", "TTTTT", " TTT ", " TTT ", 'T', new ItemStack (NRItems.parts, 1, 8)}));
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRItems.dULegs, 1), true, new Object[] {"TTT", "TTT", "T T", "T T", "T T", 'T', new ItemStack (NRItems.parts, 1, 8)}));
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRItems.dUBoots, 1), true, new Object[] {" T T ", " T T ", " T T ", "T   T", 'T', new ItemStack (NRItems.parts, 1, 8)}));
    	
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRBlocks.blockBlock, 1, 7), true, new Object[] {"TTTTT", "TTTTT", "TTTTT", "TTTTT", "TTTTT", 'T', "ingotTough"}));
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRItems.material, 25, 7), true, new Object[] {"T", 'T', "blockTough"}));
    	
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRItems.fuel, 8, 48), true, new Object[] {"TXT", "XAX", "TXT", 'X', new ItemStack(NRItems.parts, 1, 15), 'A', "ingotTough", 'T', new ItemStack (NRItems.parts, 1, 3)}));
    	this.addRecipe(new NuclearWorkspaceShapelessOreRecipe(new ItemStack(NRItems.fuel, 1, 46), new Object[] {new ItemStack(NRItems.fuel, 1, 48), "Pu238"}));
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRItems.fuel, 1, 46), true, new Object[] {"TX", 'X', "Pu238", 'T', new ItemStack(NRItems.fuel, 1, 48)}));
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRBlocks.RTG, 1), true, new Object[] {"GCCCG", "CTTTC", "CTXTC", "CTTTC", "GCCCG", 'G', new ItemStack(NRItems.parts, 1, 11), 'C', new ItemStack(NRItems.parts, 1, 0), 'T', new ItemStack(NRItems.parts, 1, 15), 'X', new ItemStack(NRItems.fuel, 1, 46)}));
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRBlocks.WRTG, 1), true, new Object[] {"PPPPP", "PUUUP", "PUUUP", "PUUUP", "PPPPP", 'P', new ItemStack(NRItems.parts, 1, 0), 'U', "U238"}));
    	if (NuclearRelativistics.enableNukes) {
    		this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRBlocks.nuke, 1), true, new Object[] {"RLLLR", "LPTPL", "LPTPL", "LPTPL", "RLLLR", 'T', Items.gunpowder, 'L', new ItemStack(NRItems.parts, 1, 0), 'R', new ItemStack(NRItems.parts, 1, 3), 'P', "Pu241"}));
    		this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRItems.nuclearGrenade, 3), true, new Object[] {"    S", "   S ", "TGT  ", "GNG  ", "TGT  ", 'T', "ingotTough", 'N', NRBlocks.nuke, 'S', Items.string, 'G', Items.gunpowder}));
    	}
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRItems.portableEnderChest, 1), true, new Object[] {"WSSSW", " WPW ", " OEO ", " WPW ", "WWWWW", 'P', new ItemStack(NRItems.parts, 1, 0), 'E', Items.ender_eye, 'S', Items.string, 'O', "oreObsidian", 'W', Blocks.wool}));
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRItems.pistol, 1), true, new Object[] {"BBBB ", "TTTTB", "  ATT", "   TT", "   TT", 'A', new ItemStack(NRItems.parts, 1, 9), 'B', new ItemStack(NRItems.parts, 1, 3), 'T', "ingotTough"}));
    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NRItems.dUBullet, 4), true, new Object[] {"TUGT", 'G', Items.gunpowder, 'T', "U238", 'U', "ingotTough"}));

        Collections.sort(this.recipes, new NuclearWorkspaceRecipeSorter());
    }

    @SuppressWarnings("unchecked")
	public void addRecipe(IRecipe recipe)
    {
    	//NuclearWorkspaceCraftingManager.getInstance().getRecipeList().add(recipe);
    	this.recipes.add(recipe);
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