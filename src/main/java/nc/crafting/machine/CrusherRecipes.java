package nc.crafting.machine;

import nc.crafting.NCRecipeHelper;
import nc.item.NCItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class CrusherRecipes extends NCRecipeHelper {

	private static final CrusherRecipes recipes = new CrusherRecipes();

	public CrusherRecipes(){
		super(1, 1);
	}
	public static final NCRecipeHelper instance() {
		return recipes;
	}

	public void addRecipes() {
    	//Ores to Gems and Dust
    	oreDust("Iron", 2);
    	oreDust("Gold", 2);
    	oreGem("Lapis", 8);
    	oreGem("Diamond", 2);
    	oreDust("Redstone", 8);
    	oreGem("Emerald", 2);
    	oreGem("Quartz", 2);
    	oreGem("Coal", 2);
    	addRecipe(oreStack("oreObsidian", 1), oreStack("dustObsidian", 8));
    	oreDust("Copper", 2);
    	oreDust("Lead", 2);
    	oreDust("Tin", 2);
    	oreDust("Silver", 2);
    	oreDust("Lead", 2);
    	oreDust("Uranium", 2);
    	addRecipe(oreStack("oreYellorite", 1), oreStack("dustYellorite", 2));
    	addRecipe(oreStack("oreYellorium", 1), oreStack("dustYellorium", 2));
    	oreDust("Thorium", 2);
    	addRecipe(oreStack("orePlutonium", 1), new ItemStack(NCItems.material, 2, 33));
    	oreDust("Lithium", 2);
    	oreDust("Boron", 2);
    	oreDust("Aluminium", 2);
    	oreDust("Aluminum", 2);
    	oreDust("Zinc", 2);
    	oreDust("Platinum", 2);
    	oreDust("Shiny", 2);
    	oreDust("Osmium", 2);
    	oreGem("Silicon", 2);
    	oreDust("Titanium", 2);
    	oreDust("Desh", 2);
    	oreGem("Ruby", 2);
    	oreGem("Sapphire", 2);
    	oreGem("Peridot", 2);
    	oreDust("Iridium", 2);
    	oreDust("Sulphur", 2);
    	oreDust("Sulfur", 2);
    	oreDust("Saltpeter", 2);
    	oreDust("Nickel", 2);
    	oreDust("ManaInfused", 2);
    	oreDust("Magnesium", 2);
    	oreDust("MagnesiumDiboride", 2);
    	
    	//Gems to Dust
    	gemDust("Lapis");
    	gemDust("Diamond");
    	gemDust("Emerald");
    	gemDust("Quartz");
    	gemDust("Coal");
    	gemDust("Apatite");
    	addRecipe(oreStack("crystalCertusQuartz", 1), oreStack("dustCertusQuartz", 1));
    	addRecipe(oreStack("crystalCertusQuartzCharged", 1), oreStack("dustCertusQuartz", 1));
    	addRecipe(oreStack("crystalFluix", 1), oreStack("dustFluix", 1));
    	addRecipe(oreStack("gemRhodochrosite", 1), oreStack("dustManganeseOxide", 1));
    	
    	//Ingots to Dust
    	ingotDust("Iron");
    	ingotDust("Gold");
    	ingotDust("Copper");
    	ingotDust("Lead");
    	ingotDust("Tin");
    	ingotDust("Silver");
    	ingotDust("Uranium");
    	ingotDust("Tough");
    	addRecipe(oreStack("ingotYellorite", 1), oreStack("dustYellorite", 1));
    	addRecipe(oreStack("ingotYellorium", 1), oreStack("dustYellorium", 1));
    	ingotDust("Thorium");
    	ingotDust("Bronze");
    	ingotDust("Lithium");
    	ingotDust("Boron");
    	ingotDust("Aluminium");
    	ingotDust("Aluminum");
    	ingotDust("Zinc");
    	ingotDust("Platinum");
    	ingotDust("Shiny");
    	ingotDust("Osmium");
    	ingotDust("Brass");
    	ingotDust("Electrum");
    	ingotDust("Steel");
    	ingotDust("Cyanite");
    	ingotDust("Plutonium");
    	ingotDust("Ludicrite");
    	ingotDust("Titanium");
    	ingotDust("Desh");
    	ingotDust("FluxedElectrum");
    	ingotDust("Nickel");
    	ingotDust("ManaInfused");
    	ingotDust("Invar");
    	ingotDust("Signalum");
    	ingotDust("Lumium");
    	ingotDust("Enderium");
    	ingotDust("UraniumOxide");
    	ingotDust("ThoriumOxide");
    	ingotDust("Magnesium");
    	ingotDust("MagnesiumDiboride");
    	ingotDust("Graphite");
    	ingotDust("HardCarbon");
    	ingotDust("LithiumManganeseDioxide");
    	
    	//Other Recipes
    	addRecipe(oreStack("stone", 1), new ItemStack(Blocks.cobblestone, 1));
    	addRecipe(oreStack("cobblestone", 1), new ItemStack(Blocks.gravel, 1));
    	addRecipe(new ItemStack(Blocks.dirt), new ItemStack(Blocks.sand, 1));
    	addRecipe(new ItemStack(Blocks.gravel), new ItemStack(Blocks.sand, 1));
    	addRecipe(new ItemStack(Blocks.sapling), new ItemStack(Items.stick, 4));
    	addRecipe(new ItemStack(Blocks.leaves), new ItemStack(Items.stick, 4));
    	addRecipe(new ItemStack(Blocks.leaves2), new ItemStack(Items.stick, 4));
    	addRecipe(oreStack("blockGlass", 1), new ItemStack(Blocks.sand, 1));
    	addRecipe(oreStack("sandstone", 1), new ItemStack(Blocks.sand, 4));
    	addRecipe(new ItemStack(Blocks.web), new ItemStack(Items.string, 3));
    	addRecipe(new ItemStack(Blocks.wool), new ItemStack(Items.string, 4));
    	addRecipe(new ItemStack(Blocks.tnt), new ItemStack(Items.gunpowder, 5));
    	addRecipe(new ItemStack(Blocks.bookshelf), new ItemStack(Items.book, 3));
    	addRecipe(new ItemStack(Blocks.farmland), new ItemStack(Blocks.sand, 1));
    	addRecipe(new ItemStack(Blocks.snow), new ItemStack(Items.snowball, 4));
    	addRecipe(new ItemStack(Blocks.cactus), new ItemStack(Items.dye, 3, 2));
    	addRecipe(new ItemStack(Blocks.clay), new ItemStack(Items.clay_ball, 4));
    	addRecipe(oreStack("glowstone", 1), oreStack("dustGlowstone", 4));
    	addRecipe(new ItemStack(Blocks.stonebrick), new ItemStack(Blocks.stone, 1));
    	addRecipe(new ItemStack(Blocks.mycelium), new ItemStack(Blocks.sand, 1));
    	addRecipe(new ItemStack(Blocks.melon_block), new ItemStack(Items.melon, 9));
    	addRecipe(new ItemStack(Blocks.redstone_lamp), oreStack("dustGlowstone", 4));
    	addRecipe(new ItemStack(Blocks.beacon), new ItemStack(Items.nether_star, 1));
    	addRecipe(new ItemStack(Blocks.quartz_block), new ItemStack(Items.quartz, 4));
    	addRecipe(new ItemStack(Blocks.quartz_stairs), new ItemStack(Items.quartz, 4));
    	addRecipe(new ItemStack(Blocks.carpet), new ItemStack(Items.string, 2));
    	addRecipe(new ItemStack(Items.arrow), new ItemStack(Items.flint, 1));
    	addRecipe(new ItemStack(Items.golden_apple), new ItemStack(Items.gold_nugget, 64));
    	addRecipe(new ItemStack(Items.saddle), new ItemStack(Items.leather, 1));
    	addRecipe(new ItemStack(Items.reeds), new ItemStack(Items.sugar, 2));
    	addRecipe(new ItemStack(Items.book), new ItemStack(Items.leather, 1));
    	addRecipe(new ItemStack(Items.bone), new ItemStack(Items.dye, 6, 15));
    	addRecipe(new ItemStack(Items.bed), new ItemStack(Items.string, 12));
    	addRecipe(new ItemStack(Items.blaze_rod), new ItemStack(Items.blaze_powder, 4));
    	addRecipe(new ItemStack(Items.skull, 1, 0), new ItemStack(Items.dye, 12, 15));
    	addRecipe(new ItemStack(Items.skull, 1, 1), new ItemStack(Items.dye, 12, 15));
    	addRecipe(new ItemStack(Items.skull, 1, 2), new ItemStack(Items.rotten_flesh, 5));
    	addRecipe(new ItemStack(Items.skull, 1, 3), new ItemStack(NCItems.dominoes, 1));
    	addRecipe(new ItemStack(Items.skull, 1, 4), new ItemStack(Items.gunpowder, 5));
    	addRecipe(new ItemStack(Items.firework_charge), new ItemStack(Items.gunpowder, 1));
    	addRecipe(new ItemStack(Items.fireworks), new ItemStack(Items.gunpowder, 1));
    	addRecipe(new ItemStack(Items.enchanted_book), new ItemStack(Items.leather, 1));
    	
    	addRecipe(oreStack("blockGraphite", 1), new ItemStack(NCItems.material, 9, 77));
    	
    	//Lithium and Boron Cells
    	addRecipe(new ItemStack(NCItems.fuel, 1, 41), new ItemStack(NCItems.material, 1, 46));
    	addRecipe(new ItemStack(NCItems.fuel, 1, 42), new ItemStack(NCItems.material, 1, 47));
    	addRecipe(new ItemStack(NCItems.fuel, 1, 43), new ItemStack(NCItems.material, 1, 48));
    	addRecipe(new ItemStack(NCItems.fuel, 1, 44), new ItemStack(NCItems.material, 1, 49));
    }
	
	public void oreDust(String type, int amount) {
		addRecipe(oreStack("ore" + type, 1), oreStack("dust" + type, amount));
	}
	
	public void oreGem(String type, int amount) {
		addRecipe(oreStack("ore" + type, 1), oreStack("gem" + type, amount));
	}
	
	public void gemDust(String type) {
		addRecipe(oreStack("gem" + type, 1), oreStack("dust" + type, 1));
	}
	
	public void ingotDust(String type) {
		addRecipe(oreStack("ingot" + type, 1), oreStack("dust" + type, 1));
	}
}