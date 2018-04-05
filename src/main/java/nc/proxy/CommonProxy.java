package nc.proxy;

import nc.Global;
import nc.ModCheck;
import nc.handler.DropHandler;
import nc.handler.DungeonLootHandler;
import nc.handler.OreDictHandler;
import nc.handler.SoundHandler;
import nc.init.NCArmor;
import nc.init.NCBlocks;
import nc.init.NCCoolantFluids;
import nc.init.NCFissionFluids;
import nc.init.NCFluids;
import nc.init.NCItems;
import nc.init.NCTiles;
import nc.init.NCTools;
import nc.multiblock.IMultiblockRegistry;
import nc.multiblock.MultiblockEventHandler;
import nc.multiblock.MultiblockRegistry;
import nc.network.PacketHandler;
import nc.recipe.NCRecipes;
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
import nc.tab.TabSaltFissionBlocks;
import nc.worldgen.biome.NCBiomes;
import nc.worldgen.decoration.BushGenerator;
import nc.worldgen.dimension.NCWorlds;
import nc.worldgen.ore.OreGenerator;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
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
	public static final CreativeTabs TAB_SALT_FISSION_BLOCKS = new TabSaltFissionBlocks();
	public static final CreativeTabs TAB_ACCELERATOR = new TabAccelerator();
	public static final CreativeTabs TAB_FLUIDS = new TabFluids();
	public static final CreativeTabs TAB_MISC = new TabMisc();

	public void preInit(FMLPreInitializationEvent preEvent) {
		SoundHandler.init();
		
		NCBlocks.init();
		NCItems.init();
		NCTools.init();
		NCArmor.init();
		
		NCBlocks.register();
		NCItems.register();
		NCTools.register();
		NCArmor.register();
		
		NCFluids.register();
		NCFissionFluids.register();
		NCCoolantFluids.register();
		
		NCTiles.register();
		
		OreDictHandler.registerOres();
		
		PacketHandler.registerMessages(Global.MOD_ID);
	}

	public void init(FMLInitializationEvent event) {
		initFluidColors();
		
		ModCheck.init();
		MinecraftForge.EVENT_BUS.register(new DropHandler());
		MinecraftForge.EVENT_BUS.register(new DungeonLootHandler());
		
		NCRecipes.init();
		CraftingRecipeHandler.registerCraftingRecipes();
		FurnaceRecipeHandler.registerFurnaceRecipes();
		GameRegistry.registerFuelHandler(new FurnaceFuelHandler());
		
		NCBiomes.initBiomeManagerAndDictionary();
		NCWorlds.registerDimensions();
		
		GameRegistry.registerWorldGenerator(new OreGenerator(), 0);
		GameRegistry.registerWorldGenerator(new BushGenerator(), 100);
		//GameRegistry.registerWorldGenerator(new WastelandPortalGenerator(), 10);
	}

	public void postInit(FMLPostInitializationEvent postEvent) {
		
	}
	
	public void registerFluidBlockRendering(Block block, String name) {
		
	}
	
	public void initFluidColors() {
		
	}
	
	public IMultiblockRegistry initMultiblockRegistry() {

        if (multiblockEventHandler == null)
            MinecraftForge.EVENT_BUS.register(multiblockEventHandler = new MultiblockEventHandler());

        return MultiblockRegistry.INSTANCE;
    }

    private static MultiblockEventHandler multiblockEventHandler = null;
	
	public World getWorld(int dimension) {
		return FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(dimension);
	}
}
