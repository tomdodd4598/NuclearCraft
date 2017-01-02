package nc.crafting.machine;

import nc.block.NCBlocks;
import nc.crafting.NCRecipeHelper;
import nc.item.NCItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class FactoryRecipes extends NCRecipeHelper {

	private static final FactoryRecipes recipes = new FactoryRecipes();

	public FactoryRecipes(){
		super(1, 1);
	}
	public static final NCRecipeHelper instance() {
		return recipes;
	}

	public void addRecipes() {
		addRecipe(oreStack("ingotIron", 1), new ItemStack(NCItems.parts, 1, 1));
		addRecipe(oreStack("plateIron", 2), new ItemStack(NCItems.parts, 1, 10));
    	addRecipe(oreStack("ingotGold", 1), new ItemStack(NCItems.parts, 1, 11));
    	addRecipe(oreStack("ingotCopper", 2), new ItemStack(NCItems.parts, 1, 12));
    	addRecipe(oreStack("ingotTin", 1), new ItemStack(NCItems.parts, 1, 6));
    	addRecipe(oreStack("plateTin", 2), new ItemStack(NCItems.parts, 1, 13));
    	addRecipe(oreStack("ingotLead", 1), new ItemStack(NCItems.parts, 1, 14));
    	addRecipe(oreStack("ingotSilver", 2), new ItemStack(NCItems.parts, 1, 15));
    	addRecipe(oreStack("ingotBronze", 1), new ItemStack(NCItems.parts, 1, 16));
    	addRecipe(oreStack("ingotMagnesiumDiboride", 2), new ItemStack(NCItems.parts, 1, 17));
    	
    	oreIngot("Iron", 2);
    	oreIngot("Gold", 2);
    	oreIngot("Copper", 2);
    	oreIngot("Lead", 2);
    	oreIngot("Tin", 2);
    	oreIngot("Silver", 2);
    	oreIngot("Lead", 2);
    	oreIngot("Uranium", 2);
    	addRecipe(oreStack("oreYellorite", 1), oreStack("ingotYellorite", 2));
    	addRecipe(oreStack("oreYellorium", 1), oreStack("ingotYellorium", 2));
    	oreIngot("Thorium", 2);
    	oreIngot("Lithium", 2);
    	oreIngot("Boron", 2);
    	oreIngot("Aluminium", 2);
    	oreIngot("Aluminum", 2);
    	oreIngot("Zinc", 2);
    	oreIngot("Platinum", 2);
    	oreIngot("Shiny", 2);
    	oreIngot("Osmium", 2);
    	oreIngot("Titanium", 2);
    	oreIngot("Desh", 2);
    	oreIngot("Nickel", 2);
    	oreIngot("ManaInfused", 2);
    	oreIngot("Magnesium", 2);
    	
    	addRecipe(oreStack("plankWood", 1), new ItemStack(Items.stick, 4));
    	addRecipe(oreStack("logWood", 1), new ItemStack(Blocks.planks, 6));
    	addRecipe(new ItemStack(Blocks.brick_block, 1), new ItemStack(Items.brick, 4));
    	addRecipe(new ItemStack(Blocks.chest, 1), new ItemStack(Blocks.planks, 8));
    	addRecipe(new ItemStack(Blocks.trapped_chest, 1), new ItemStack(Blocks.planks, 8));
    	addRecipe(new ItemStack(Blocks.crafting_table, 1), new ItemStack(Blocks.planks, 4));
    	addRecipe(new ItemStack(Blocks.furnace, 1), new ItemStack(Blocks.cobblestone, 8));
    	addRecipe(new ItemStack(Blocks.brick_stairs, 1), new ItemStack(Items.brick, 4));
    	addRecipe(new ItemStack(Blocks.nether_brick, 1), new ItemStack(Items.netherbrick, 4));
    	addRecipe(new ItemStack(Blocks.nether_brick_stairs, 1), new ItemStack(Items.netherbrick, 6));
    	addRecipe(new ItemStack(Blocks.nether_brick_fence, 1), new ItemStack(Items.netherbrick, 4));
    	addRecipe(new ItemStack(Blocks.enchanting_table, 1), new ItemStack(Items.diamond, 2));
    	addRecipe(new ItemStack(Blocks.anvil, 1), new ItemStack(Blocks.iron_block, 3));
    	addRecipe(new ItemStack(Items.bucket, 1), new ItemStack(Items.iron_ingot, 3));
    	addRecipe(new ItemStack(Items.rotten_flesh, 4), new ItemStack(Items.leather, 1));
    	addRecipe(new ItemStack(Items.coal, 64), new ItemStack(Items.diamond, 1));
    	
    	addRecipe(new ItemStack(NCItems.lithiumIonBattery, 8), new ItemStack(NCBlocks.lithiumIonBatteryBlock, 1));
    	addRecipe(new ItemStack(Items.reeds, 2), new ItemStack(NCItems.parts, 1, 20));
    	addRecipe(oreStack("dustCoal", 1), oreStack("ingotGraphite", 1));
    }
	
	public void oreIngot(String type, int amount) {
		addRecipe(oreStack("ore" + type, 1), oreStack("ingot" + type, amount));
	}
}