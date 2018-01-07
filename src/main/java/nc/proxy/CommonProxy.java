package nc.proxy;

import nc.Global;
import nc.ModCheck;
import nc.handler.DropHandler;
import nc.handler.OreDictHandler;
import nc.handler.SoundHandler;
import nc.init.NCArmor;
import nc.init.NCBlocks;
import nc.init.NCFluids;
import nc.init.NCItems;
import nc.init.NCTiles;
import nc.init.NCTools;
import nc.network.PacketHandler;
import nc.recipe.vanilla.CraftingRecipeHandler;
import nc.recipe.vanilla.FurnaceFuelHandler;
import nc.recipe.vanilla.FurnaceRecipeHandler;
import nc.tab.TabAccelerator;
import nc.tab.TabBaseBlockMaterials;
import nc.tab.TabBaseItemMaterials;
import nc.tab.TabFissionBlocks;
import nc.tab.TabFissionFuelRods;
import nc.tab.TabFissionMaterials;
import nc.tab.TabFluids;
import nc.tab.TabFusion;
import nc.tab.TabMachines;
import nc.tab.TabMisc;
import nc.worldgen.DecorGen;
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
	public static final CreativeTabs TAB_FUSION = new TabFusion();
	public static final CreativeTabs TAB_ACCELERATOR = new TabAccelerator();
	public static final CreativeTabs TAB_FLUIDS = new TabFluids();
	public static final CreativeTabs TAB_MISC = new TabMisc();
	
	nc.handler.EventHandler eventHandler = new nc.handler.EventHandler();

	public void preInit(FMLPreInitializationEvent preEvent) {
		SoundHandler.init();
		
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
		
		OreDictHandler.registerOres();
		
		PacketHandler.registerMessages(Global.MOD_ID);
		
		//AdvancementHandler.registerAdvancements();
	}

	public void init(FMLInitializationEvent event) {
		eventHandler.registerEvents();
		ModCheck.init();
		MinecraftForge.EVENT_BUS.register(new DropHandler());
		
		CraftingRecipeHandler.registerCraftingRecipes();
		FurnaceRecipeHandler.registerFurnaceRecipes();
		GameRegistry.registerFuelHandler(new FurnaceFuelHandler());
		
		GameRegistry.registerWorldGenerator(new OreGen(), 0);
		GameRegistry.registerWorldGenerator(new DecorGen(), 100);
	}

	public void postInit(FMLPostInitializationEvent postEvent) {
		//if (ModCheck.craftTweakerLoaded()) NCCraftTweaker.init(); Not needed as ZenRegister registers handlers
	}
	
	public void registerFluidBlockRendering(Block block, String name) {
		
	}
}
