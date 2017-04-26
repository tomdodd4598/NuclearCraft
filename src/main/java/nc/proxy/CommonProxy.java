package nc.proxy;

import nc.ModCheck;
import nc.handler.AchievementHandler;
import nc.handler.DropHandler;
import nc.handler.FuelHandler;
import nc.handler.FurnaceRecipeHandler;
import nc.handler.OreDictHandler;
import nc.handler.RecipeHandler;
import nc.init.NCArmor;
import nc.init.NCBlocks;
import nc.init.NCFluids;
import nc.init.NCItems;
import nc.init.NCTiles;
import nc.init.NCTools;
import nc.tab.TabBaseBlockMaterials;
import nc.tab.TabBaseItemMaterials;
import nc.tab.TabFissionBlocks;
import nc.tab.TabFissionFuelRods;
import nc.tab.TabFissionMaterials;
import nc.tab.TabFluids;
import nc.tab.TabMachines;
import nc.tab.TabMisc;
import nc.worldgen.OreGen;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {
	
	public static final CreativeTabs TAB_BASE_BLOCK_MATERIALS = new TabBaseBlockMaterials();
	public static final CreativeTabs TAB_BASE_ITEM_MATERIALS = new TabBaseItemMaterials();
	public static final CreativeTabs TAB_MACHINES = new TabMachines();
	public static final CreativeTabs TAB_FISSION_BLOCKS = new TabFissionBlocks();
	public static final CreativeTabs TAB_FISSION_MATERIALS = new TabFissionMaterials();
	public static final CreativeTabs TAB_FISSION_FUEL_RODS = new TabFissionFuelRods();
	public static final CreativeTabs TAB_FLUIDS = new TabFluids();
	public static final CreativeTabs TAB_MISC = new TabMisc();
	
	nc.handler.EventHandler eventHandler = new nc.handler.EventHandler();

	public void preInit(FMLPreInitializationEvent preEvent) {
		NCBlocks.init();
		NCFluids.init();
		NCItems.init();
		NCTools.init();
		NCArmor.init();
		
		NCBlocks.register();
		NCFluids.register();
		NCItems.register();
		NCTools.register();
		NCArmor.register();
		
		NCTiles.register();
		
		AchievementHandler.registerAchievements();
	}

	public void init(FMLInitializationEvent event) {
		eventHandler.registerEvents();
		ModCheck.init();
		MinecraftForge.EVENT_BUS.register(new DropHandler());
		
		RecipeHandler.registerCraftingRecipes();
		FurnaceRecipeHandler.registerFurnaceRecipes();
		GameRegistry.registerFuelHandler(new FuelHandler());
		OreDictHandler.registerOres();
		GameRegistry.registerWorldGenerator(new OreGen(), 0);
	}

	public void postInit(FMLPostInitializationEvent postEvent) {
		
	}
	
	public void registerFluidBlockRendering(Block block, String name) {
		
	}
}
