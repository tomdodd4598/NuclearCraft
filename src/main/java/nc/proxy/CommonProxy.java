package nc.proxy;

import nc.ModCheck;
import nc.handlers.AchievementHandler;
import nc.handlers.DropHandler;
import nc.handlers.FuelHandler;
import nc.handlers.FurnaceRecipeHandler;
import nc.handlers.OreDictHandler;
import nc.handlers.RecipeHandler;
import nc.init.NCArmor;
import nc.init.NCBlocks;
import nc.init.NCItems;
import nc.init.NCTiles;
import nc.init.NCTools;
import nc.tabs.NCTab;
import nc.worldgen.OreGen;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {
	
	public static final CreativeTabs NC_TAB = new NCTab();
	
	nc.handlers.EventHandler eventHandler = new nc.handlers.EventHandler();

	public void preInit(FMLPreInitializationEvent preEvent) {
		NCBlocks.init();
		NCItems.init();
		NCTools.init();
		NCArmor.init();
		
		NCBlocks.register();
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
}
