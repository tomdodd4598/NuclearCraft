package nc.crafting.workspace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import nc.NuclearCraft;
import nc.block.NCBlocks;
import nc.item.NCItems;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class NuclearWorkspaceCraftingManager {
    /** The static instance of this class */
    public static final NuclearWorkspaceCraftingManager instance = new NuclearWorkspaceCraftingManager();
    /** A list of all the recipes added */
    @SuppressWarnings("rawtypes")
	public List recipes = new ArrayList();

    /**
     * Returns the static instance of this class
     */
    public static final NuclearWorkspaceCraftingManager getInstance() {
        /** The static instance of this class */
        return instance;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public NuclearWorkspaceCraftingManager() {
    	recipes = new ArrayList();
    	
    	if (NuclearCraft.workspace) {
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCBlocks.machineBlock, 1), true, new Object[] {"PLCLP", "LBRBL", "GTITG", "LBRBL", "PLCLP", 'P', "plateBasic", 'L', "plateLead", 'C', new ItemStack(NCItems.parts, 1, 12), 'B', new ItemStack (NCItems.parts, 1, 16), 'R', Items.redstone, 'G', new ItemStack (NCItems.parts, 1, 11), 'T', new ItemStack (NCItems.parts, 1, 13), 'I', new ItemStack (NCItems.parts, 1, 10)}));
	    	
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCBlocks.reactorBlock, 16), true, new Object[] {"INNNI", "N   N", "N   N", "N   N", "INNNI", 'N', "plateBasic", 'I', "ingotTough"}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCBlocks.cellBlock, 1), true, new Object[] {"LNTNL", "NP PN", "T   T", "NP PN", "LNTNL", 'N', "plateBasic", 'P', "plateLead", 'L', "blockGlass", 'T', "ingotTough"}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCBlocks.emptyCoolerBlock, 16), true, new Object[] {" NNN ", "NU UN", "N   N", "NU UN", " NNN ", 'N', "plateBasic", 'U', "universalReactant"}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCBlocks.speedBlock, 6), true, new Object[] {" NNN ", "NURUN", "NRURN", "NURUN", " NNN ", 'N', "plateBasic", 'R', Items.blaze_powder, 'U', Items.redstone}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCBlocks.fissionReactorGraphiteIdle, 1), true, new Object[] {"TNNNT", "NUUUN", "NUMUN", "NUUUN", "TNNNT", 'N', "plateReinforced", 'U', "plateBasic", 'T', "ingotTough", 'M', NCBlocks.machineBlock}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCBlocks.fissionReactorSteamIdle, 1), true, new Object[] {"TNNNT", "NUUUN", "NUMUN", "NUUUN", "TNNNT", 'N', new ItemStack(NCItems.parts, 1, 7), 'U', "plateAdvanced", 'T', "ingotTough", 'M', NCBlocks.fissionReactorGraphiteIdle}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCBlocks.blastBlock, 16), true, new Object[] {" NNN ", "NUUUN", "NUUUN", "NUUUN", " NNN ", 'N', NCBlocks.reactorBlock, 'U', "oreObsidian"}));
	    	
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCItems.parts, 4, 3), true, new Object[] {" TT ", "TNNT", "TNNT", " TT ", 'N', "plateBasic", 'T', "ingotTough"}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCItems.parts, 4, 9), true, new Object[] {"RTTTR", "TNNNT", "TNDNT", "TNNNT", "RTTTR", 'N', "plateDU", 'T', new ItemStack (NCItems.material, 1, 48), 'R', "universalReactant", 'D', "dustDiamond"}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCItems.parts, 4, 5), true, new Object[] {"RTTTR", "T   T", "T   T", "RTTTR", 'T', "plateBasic", 'R', "universalReactant"}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCItems.parts, 20, 0), true, new Object[] {"RRRRR", "TTTTT", 'T', "dustTough", 'R', "ingotTough"}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCItems.parts, 1, 7), true, new Object[] {" NNN ", "NWWWN", "NWWWN", " NNN ", 'N', "plateTin", 'W', new ItemStack(NCItems.fuel, 1, 34)}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCItems.parts, 2, 8), true, new Object[] {" WW ", "WNNW", "WNNW", " WW ", 'N', "plateReinforced", 'W', "U238"}));
	    	
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCItems.upgradeSpeed, 1), true, new Object[] {"TTT", "TNT", "TTT", 'N', "plateIron", 'T', "dustLapis"}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCItems.upgradeEnergy, 1), true, new Object[] {"TTT", "TNT", "TTT", 'N', "plateIron", 'T', "universalReactant"}));
	    	
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCBlocks.tubing1, 8), true, new Object[] {" NNN ", "NIIIN", " NNN ", 'N', "plateLead", 'I', "plateIron"}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCBlocks.tubing2, 8), true, new Object[] {" N ", "NIN", "NIN", "NIN", " N ", 'N', "plateLead", 'I', "plateIron"}));
	
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCBlocks.separatorIdle, 1), true, new Object[] {"TNNNT", "NUUUN", "NUMUN", "NUUUN", "TNNNT", 'N', "plateLead", 'T', Items.redstone, 'U', "ingotTough", 'M', NCBlocks.machineBlock}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCBlocks.hastenerIdle, 1), true, new Object[] {"TNNNT", "NUUUN", "NUMUN", "NUUUN", "TNNNT", 'N', "plateLead", 'T', "ingotTough", 'U', "universalReactant", 'M', NCBlocks.machineBlock}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCBlocks.collectorIdle, 1), true, new Object[] {" NNN ", "NUUUN", "NUUUN", "NUUUN", " NNN ", 'N', "plateBasic", 'U', new ItemStack(NCItems.material, 1, 40)}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCBlocks.reactionGeneratorIdle, 1), true, new Object[] {"TNNNT", "NLULN", "NUMUN", "NLULN", "TNNNT", 'N', Items.redstone, 'T', "plateBasic", 'L', "plateLead", 'U', new ItemStack(NCItems.parts, 1, 5), 'M', NCBlocks.machineBlock}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCBlocks.electrolyserIdle, 1), true, new Object[] {"TNNNT", "NLLLN", "NUMUN", "NLLLN", "TNNNT", 'N', new ItemStack (NCItems.parts, 1, 3), 'T', "universalReactant", 'L', "plateLead", 'U', new ItemStack(NCItems.parts, 1, 7), 'M', NCBlocks.machineBlock}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCBlocks.oxidiserIdle, 1), true, new Object[] {"TNNNT", "NUUUN", "NUMUN", "NUUUN", "TNNNT", 'N', "plateLead", 'T', "plateDU", 'U', "universalReactant", 'M', NCBlocks.machineBlock}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCBlocks.ioniserIdle, 1), true, new Object[] {"TNNNT", "NUUUN", "NUMUN", "NUUUN", "TNNNT", 'N', "plateLead", 'T', "plateDU", 'U', Items.redstone, 'M', NCBlocks.machineBlock}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCBlocks.irradiatorIdle, 1), true, new Object[] {"TNNNT", "NUUUN", "NUMUN", "NUUUN", "TNNNT", 'N', "ingotTough", 'T', "plateDU", 'U', "universalReactant", 'M', NCBlocks.machineBlock}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCBlocks.coolerIdle, 1), true, new Object[] {"TNNNT", "NUUUN", "NUMUN", "NUUUN", "TNNNT", 'N', "plateBasic", 'T', "plateDU", 'U', "universalReactant", 'M', NCBlocks.machineBlock}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCBlocks.factoryIdle, 1), true, new Object[] {"TNNNT", "NUUUN", "NUMUN", "NUUUN", "TNNNT", 'T', "ingotTough", 'N', "plateBasic", 'U', "plateIron", 'M', Blocks.piston}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCBlocks.assemblerIdle, 1), true, new Object[] {"TNNNT", "NUUUN", "NUMUN", "NUUUN", "TNNNT", 'T', "ingotTough", 'N', "plateIron", 'U', "plateBasic", 'M', Blocks.piston}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCBlocks.heliumExtractorIdle, 1), true, new Object[] {"TNNNT", "NUUUN", "NUMUN", "NUUUN", "TNNNT", 'T', new ItemStack(NCItems.parts, 1, 5), 'N', "plateReinforced", 'U', "plateTin", 'M', NCBlocks.machineBlock}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCBlocks.recyclerIdle, 1), true, new Object[] {"TNNNT", "NUUUN", "NUMUN", "NUUUN", "TNNNT", 'N', "plateLead", 'T', "ingotHardCarbon", 'U', "ingotTough", 'M', NCBlocks.machineBlock}));
	    	
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCBlocks.electromagnetIdle, 1), true, new Object[] {"AAAAA", "NNNNN", "MMMMM", "NNNNN", "AAAAA", 'A', new ItemStack (NCItems.parts, 1, 0), 'N', new ItemStack(NCItems.parts, 1, 12), 'M', "ingotIron"}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCBlocks.fusionReactor, 1), true, new Object[] {"TNNNT", "NTNTN", "NNUNN", "NTNTN", "TNNNT", 'N', "plateAdvanced", 'T', new ItemStack (NCBlocks.reactionGeneratorIdle, 1), 'U', NCBlocks.electromagnetIdle}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCBlocks.fusionReactorSteam, 1), true, new Object[] {"TNNNT", "NUUUN", "NUMUN", "NUUUN", "TNNNT", 'N', new ItemStack(NCItems.parts, 1, 7), 'U', "plateAdvanced", 'T', "ingotTough", 'M', NCBlocks.fusionReactor}));
	    	
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCBlocks.superElectromagnetIdle, 1), true, new Object[] {"ASSSA", "NNNNN", "MMMMM", "NNNNN", "ASSSA", 'A', new ItemStack (NCItems.parts, 1, 9), 'N', new ItemStack(NCItems.parts, 1, 17), 'S', new ItemStack (NCItems.parts, 1, 3), 'M', "ingotTough"}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCBlocks.supercoolerIdle, 1), true, new Object[] {"ASSSA", "NUNUN", "MMMMM", "NUNUN", "ASSSA", 'A', new ItemStack (NCItems.parts, 1, 9), 'T', new ItemStack (NCItems.parts, 1, 3), 'M', new ItemStack(NCItems.parts, 1, 13), 'U', "universalReactant", 'N', "dustCoal", 'S', new ItemStack (NCItems.parts, 1, 3)}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCBlocks.synchrotronIdle, 1), true, new Object[] {"TNNNT", "NUUUN", "NUMUN", "NUUUN", "TNNNT", 'N', "plateAdvanced", 'U', "plateDU", 'T', NCBlocks.superElectromagnetIdle, 'M', NCBlocks.machineBlock}));
	    	
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCItems.toughAlloySword, 1), true, new Object[] {"T", "T", "T", "T", "S", 'T', "ingotTough", 'S', "ingotIron"}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCItems.toughAlloyPickaxe, 1), true, new Object[] {" TTT ", "T S T", "  S  ", "  S  ", "  S  ", 'T', "ingotTough", 'S', "ingotIron"}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCItems.toughAlloyShovel, 1), true, new Object[] {"T", "T", "S", "S", "S", 'T', "ingotTough", 'S', "ingotIron"}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCItems.toughAlloyAxe, 1), true, new Object[] {" TT", "TTS", " TS", "  S", "  S", 'T', "ingotTough", 'S', "ingotIron"}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCItems.toughAlloyHoe, 1), true, new Object[] {" TT", "T S", "  S", "  S", "  S", 'T', "ingotTough", 'S', "ingotIron"}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCItems.toughAlloyAxe, 1), true, new Object[] {"TT ", "STT", "ST ", "S  ", "S  ", 'T', "ingotTough", 'S', "ingotIron"}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCItems.toughAlloyHoe, 1), true, new Object[] {"TT ", "S T", "S  ", "S  ", "S  ", 'T', "ingotTough", 'S', "ingotIron"}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCItems.toughAlloyPaxel, 1), true, new Object[] {"ASP", "HIW", " I ", " I ", " I ", 'A', NCItems.toughAlloyAxe, 'S', NCItems.toughAlloyShovel, 'P', NCItems.toughAlloyPickaxe, 'H', NCItems.toughAlloyHoe, 'W', NCItems.toughAlloySword, 'I', "ingotIron"}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCItems.toughBow, 1), true, new Object[] {"ST ", "S T", "S T", "S T", "ST ", 'T', "ingotTough", 'S', Items.string}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCItems.toughBow, 1), true, new Object[] {" TS", "T S", "T S", "T S", " TS", 'T', "ingotTough", 'S', Items.string}));
	    	
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCItems.toughHelm, 1), true, new Object[] {" TTT ", "TTTTT", "T   T", "T   T", 'T', "ingotTough"}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCItems.toughChest, 1), true, new Object[] {"T   T", "TT TT", "TTTTT", " TTT ", " TTT ", 'T', "ingotTough"}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCItems.toughLegs, 1), true, new Object[] {"TTT", "TTT", "T T", "T T", "T T", 'T', "ingotTough"}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCItems.toughBoots, 1), true, new Object[] {" T T ", " T T ", " T T ", "T   T", 'T', "ingotTough"}));
	    	
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCItems.dUSword, 1), true, new Object[] {"T", "T", "T", "T", "S", 'T', new ItemStack (NCItems.parts, 1, 8), 'S', "ingotIron"}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCItems.dUPickaxe, 1), true, new Object[] {" TTT ", "T S T", "  S  ", "  S  ", "  S  ", 'T', new ItemStack (NCItems.parts, 1, 8), 'S', "ingotIron"}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCItems.dUShovel, 1), true, new Object[] {"T", "T", "S", "S", "S", 'T', new ItemStack (NCItems.parts, 1, 8), 'S', "ingotIron"}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCItems.dUAxe, 1), true, new Object[] {" TT", "TTS", " TS", "  S", "  S", 'T', new ItemStack (NCItems.parts, 1, 8), 'S', "ingotIron"}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCItems.dUHoe, 1), true, new Object[] {" TT", "T S", "  S", "  S", "  S", 'T', new ItemStack (NCItems.parts, 1, 8), 'S', "ingotIron"}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCItems.dUAxe, 1), true, new Object[] {"TT ", "STT", "ST ", "S  ", "S  ", 'T', new ItemStack (NCItems.parts, 1, 8), 'S', "ingotIron"}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCItems.dUHoe, 1), true, new Object[] {"TT ", "S T", "S  ", "S  ", "S  ", 'T', new ItemStack (NCItems.parts, 1, 8), 'S', "ingotIron"}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCItems.dUPaxel, 1), true, new Object[] {"ASP", "HIW", " I ", " I ", " I ", 'A', NCItems.dUAxe, 'S', NCItems.dUShovel, 'P', NCItems.dUPickaxe, 'H', NCItems.dUHoe, 'W', NCItems.dUSword, 'I', "ingotIron"}));
	    	
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCItems.dUHelm, 1), true, new Object[] {" TTT ", "TTTTT", "T   T", "T   T", 'T', new ItemStack (NCItems.parts, 1, 8)}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCItems.dUChest, 1), true, new Object[] {"T   T", "TT TT", "TTTTT", " TTT ", " TTT ", 'T', new ItemStack (NCItems.parts, 1, 8)}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCItems.dULegs, 1), true, new Object[] {"TTT", "TTT", "T T", "T T", "T T", 'T', new ItemStack (NCItems.parts, 1, 8)}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCItems.dUBoots, 1), true, new Object[] {" T T ", " T T ", " T T ", "T   T", 'T', new ItemStack (NCItems.parts, 1, 8)}));
	    	
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCBlocks.blockBlock, 1, 7), true, new Object[] {"TTTTT", "TTTTT", "TTTTT", "TTTTT", "TTTTT", 'T', "ingotTough"}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCItems.material, 25, 7), true, new Object[] {"T", 'T', "blockTough"}));
	    	
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCItems.fuel, 8, 48), true, new Object[] {"TXT", "XAX", "TXT", 'X', new ItemStack(NCItems.parts, 1, 15), 'A', "ingotTough", 'T', new ItemStack (NCItems.parts, 1, 3)}));
	    	this.addRecipe(new NuclearWorkspaceShapelessOreRecipe(new ItemStack(NCItems.fuel, 1, 46), new Object[] {new ItemStack(NCItems.fuel, 1, 48), "Pu238"}));
	    	this.addRecipe(new NuclearWorkspaceShapelessOreRecipe(new ItemStack(NCItems.fuel, 1, 139), new Object[] {new ItemStack(NCItems.fuel, 1, 48), "Am241"}));
	    	this.addRecipe(new NuclearWorkspaceShapelessOreRecipe(new ItemStack(NCItems.fuel, 1, 140), new Object[] {new ItemStack(NCItems.fuel, 1, 48), "Cf250"}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCItems.fuel, 1, 46), true, new Object[] {"TX", 'X', "Pu238", 'T', new ItemStack(NCItems.fuel, 1, 48)}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCItems.fuel, 1, 139), true, new Object[] {"TX", 'X', "Am241", 'T', new ItemStack(NCItems.fuel, 1, 48)}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCItems.fuel, 1, 140), true, new Object[] {"TX", 'X', "Cf250", 'T', new ItemStack(NCItems.fuel, 1, 48)}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCBlocks.RTG, 1), true, new Object[] {"GCCCG", "CTTTC", "CTXTC", "CTTTC", "GCCCG", 'G', new ItemStack(NCItems.parts, 1, 11), 'C', "plateLead", 'T', new ItemStack(NCItems.parts, 1, 15), 'X', new ItemStack(NCItems.fuel, 1, 46)}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCBlocks.AmRTG, 1), true, new Object[] {"GCCCG", "CTTTC", "CTXTC", "CTTTC", "GCCCG", 'G', new ItemStack(NCItems.parts, 1, 11), 'C', "plateLead", 'T', new ItemStack(NCItems.parts, 1, 15), 'X', new ItemStack(NCItems.fuel, 1, 139)}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCBlocks.CfRTG, 1), true, new Object[] {"GCCCG", "CTTTC", "CTXTC", "CTTTC", "GCCCG", 'G', new ItemStack(NCItems.parts, 1, 11), 'C', "plateLead", 'T', new ItemStack(NCItems.parts, 1, 15), 'X', new ItemStack(NCItems.fuel, 1, 140)}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCBlocks.WRTG, 1), true, new Object[] {" PPP ", "PUUUP", "PUUUP", "PUUUP", " PPP ", 'P', "plateLead", 'U', "U238"}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCBlocks.steamGenerator, 2), true, new Object[] {"PPPPP", "PCCCP", "MMMMM", "PCCCP", "PPPPP", 'P', "plateIron", 'C', new ItemStack(NCItems.parts, 1, 12), 'M', new ItemStack(NCItems.parts, 1, 19)}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCBlocks.steamDecompressor, 2), true, new Object[] {"PPPPP", "PCCCP", "GMMMG", "PCCCP", "PPPPP", 'P', "plateIron", 'C', Blocks.piston, 'G', new ItemStack(NCItems.parts, 1, 10), 'M', new ItemStack(NCItems.parts, 1, 19)}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCBlocks.denseSteamDecompressor, 1), true, new Object[] {"PPPPP", "CCCCC", "PPPPP", 'P', "plateAdvanced", 'C', NCBlocks.steamDecompressor}));
	    	if (NuclearCraft.enableNukes) {
	    		this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCBlocks.nuke, 1), true, new Object[] {"RLLLR", "LPTPL", "LTPTL", "LPTPL", "RLLLR", 'T', Items.gunpowder, 'L', "plateBasic", 'R', "plateReinforced", 'P', new ItemStack(NCItems.material, 1, 67)}));
	    		this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCItems.nuclearGrenade, 3), true, new Object[] {"    S", "   S ", "TGT  ", "GNG  ", "TGT  ", 'T', "ingotTough", 'N', NCBlocks.nuke, 'S', Items.string, 'G', Items.gunpowder}));
	    		this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCBlocks.antimatterBomb, 1), true, new Object[] {"PPPPP", "PAAAP", "PAEAP", "PAAAP", "PPPPP", 'A', NCItems.antimatter, 'P', "plateAdvanced", 'E', NCBlocks.superElectromagnetIdle}));
	    	}
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCBlocks.solarPanel, 1), true, new Object[] {"CTTTC", "CXXXC", "CSSSC", "CGGGC", "CCCCC", 'S', "dustCoal", 'G', new ItemStack(NCItems.parts, 1, 12), 'C', "plateIron", 'T', new ItemStack(NCItems.parts, 1, 15), 'X', "universalReactant"}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCItems.portableEnderChest, 1), true, new Object[] {"WSSSW", " WPW ", " OEO ", "OPPPO", "WWWWW", 'P', "plateBasic", 'E', Items.ender_eye, 'S', Items.string, 'O', "oreObsidian", 'W', Blocks.wool}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCItems.pistol, 1), true, new Object[] {"BBBB ", "TTTTB", "  ATT", "   TT", "   TT", 'A', "plateAdvanced", 'B', "plateReinforced", 'T', "ingotTough"}));
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCItems.dUBullet, 4), true, new Object[] {"TUGT", 'G', Items.gunpowder, 'T', "U238", 'U', "ingotTough"}));
	    	
	    	this.addRecipe(new NuclearWorkspaceShapedOreRecipe(new ItemStack(NCItems.lithiumIonBattery, 1), true, new Object[] {"AAAA", "BCCB", "BCCB", "DDDD", 'A', "ingotLithiumManganeseDioxide", 'B', "plateAdvanced", 'C', "dustLithium", 'D', "ingotHardCarbon"}));
    	}
    	
        Collections.sort(this.recipes, new NuclearWorkspaceRecipeSorter());
    }

    @SuppressWarnings("unchecked")
	public void addRecipe(IRecipe recipe) {
    	//NuclearWorkspaceCraftingManager.getInstance().getRecipeList().add(recipe);
    	this.recipes.add(recipe);
    }

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public NuclearWorkspaceShapedRecipes addRecipe(ItemStack p_92103_1_, Object ... p_92103_2_) {
        String s = "";
        int i = 0;
        int j = 0;
        int k = 0;

        if (p_92103_2_[i] instanceof String[]) {
            String[] astring = (String[])((String[])p_92103_2_[i++]);

            for (int l = 0; l < astring.length; ++l) {
                String s1 = astring[l];
                ++k;
                j = s1.length();
                s = s + s1;
            }
        } else {
            while (p_92103_2_[i] instanceof String) {
                String s2 = (String)p_92103_2_[i++];
                ++k;
                j = s2.length();
                s = s + s2;
            }
        }

        HashMap hashmap;

        for (hashmap = new HashMap(); i < p_92103_2_.length; i += 2) {
            Character character = (Character)p_92103_2_[i];
            ItemStack itemstack1 = null;

            if (p_92103_2_[i + 1] instanceof Item) {
                itemstack1 = new ItemStack((Item)p_92103_2_[i + 1]);
            } else if (p_92103_2_[i + 1] instanceof Block) {
                itemstack1 = new ItemStack((Block)p_92103_2_[i + 1], 1, 32767);
            } else if (p_92103_2_[i + 1] instanceof ItemStack) {
                itemstack1 = (ItemStack)p_92103_2_[i + 1];
            }

            hashmap.put(character, itemstack1);
        }

        ItemStack[] aitemstack = new ItemStack[j * k];

        for (int i1 = 0; i1 < j * k; ++i1) {
            char c0 = s.charAt(i1);

            if (hashmap.containsKey(Character.valueOf(c0))) {
                aitemstack[i1] = ((ItemStack)hashmap.get(Character.valueOf(c0))).copy();
            } else {
                aitemstack[i1] = null;
            }
        }

        NuclearWorkspaceShapedRecipes shapedrecipes = new NuclearWorkspaceShapedRecipes(j, k, aitemstack, p_92103_1_);
        this.recipes.add(shapedrecipes);
        return shapedrecipes;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public NuclearWorkspaceShapelessRecipes addShapelessRecipe(ItemStack p_77596_1_, Object ... p_77596_2_) {
        ArrayList arraylist = new ArrayList();
        Object[] aobject = p_77596_2_;
        int i = p_77596_2_.length;

        for (int j = 0; j < i; ++j) {
            Object object1 = aobject[j];

            if (object1 instanceof ItemStack) {
                arraylist.add(((ItemStack)object1).copy());
            } else if (object1 instanceof Item) {
                arraylist.add(new ItemStack((Item)object1));
            } else {
                if (!(object1 instanceof Block)) {
                    throw new RuntimeException("Invalid shapeless recipe!");
                }

                arraylist.add(new ItemStack((Block)object1));
            }
        }

        NuclearWorkspaceShapelessRecipes shapelessrecipes = new NuclearWorkspaceShapelessRecipes(p_77596_1_, arraylist);
        this.recipes.add(shapelessrecipes);
        return shapelessrecipes;
    }

    public ItemStack findMatchingRecipe(InventoryCrafting p_82787_1_, World p_82787_2_) {
        int i = 0;
        ItemStack itemstack = null;
        ItemStack itemstack1 = null;
        int j;

        for (j = 0; j < p_82787_1_.getSizeInventory(); ++j) {
            ItemStack itemstack2 = p_82787_1_.getStackInSlot(j);

            if (itemstack2 != null) {
                if (i == 0) {
                    itemstack = itemstack2;
                }
                if (i == 1) {
                    itemstack1 = itemstack2;
                }

                ++i;
            }
        }

        if (i == 2 && itemstack.getItem() == itemstack1.getItem() && itemstack.stackSize == 1 && itemstack1.stackSize == 1 && itemstack.getItem().isRepairable()) {
            Item item = itemstack.getItem();
            int j1 = item.getMaxDamage() - itemstack.getItemDamageForDisplay();
            int k = item.getMaxDamage() - itemstack1.getItemDamageForDisplay();
            int l = j1 + k + item.getMaxDamage() * 5 / 100;
            int i1 = item.getMaxDamage() - l;

            if (i1 < 0) {
                i1 = 0;
            }

            return new ItemStack(itemstack.getItem(), 1, i1);
        } else {
            for (j = 0; j < this.recipes.size(); ++j) {
                IRecipe irecipe = (IRecipe)this.recipes.get(j);

                if (irecipe.matches(p_82787_1_, p_82787_2_)) {
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
	public List getRecipeList() {
        return this.recipes;
    }
}