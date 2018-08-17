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
import nc.integration.tconstruct.TConstructExtras;
import nc.integration.tconstruct.TConstructIMC;
import nc.integration.tconstruct.TConstructMaterials;
import nc.multiblock.IMultiblockRegistry;
import nc.multiblock.MultiblockEventHandler;
import nc.multiblock.MultiblockRegistry;
import nc.network.PacketHandler;
import nc.recipe.NCRecipes;
import nc.recipe.vanilla.CraftingRecipeHandler;
import nc.recipe.vanilla.FurnaceFuelHandler;
import nc.recipe.vanilla.FurnaceRecipeHandler;
import nc.worldgen.biome.NCBiomes;
import nc.worldgen.decoration.BushGenerator;
import nc.worldgen.dimension.NCWorlds;
import nc.worldgen.ore.OreGenerator;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.registry.GameRegistry;
import slimeknights.tconstruct.library.materials.Material;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent preEvent) {
		ModCheck.init();
		
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
		
		TConstructIMC.sendIMCs();
		if (ModCheck.tinkersLoaded()) TConstructMaterials.init();
	}

	public void init(FMLInitializationEvent event) {
		initFluidColors();
		
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
		
		if (ModCheck.tinkersLoaded()) TConstructExtras.init();
	}

	public void postInit(FMLPostInitializationEvent postEvent) {
		
	}
	
	// Packets
	
	public World getWorld(int dimensionId) {
		return FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(dimensionId);
	}

	public int getCurrentClientDimension() {
		return -8954;
	}
	
	public EntityPlayer getPlayerEntity(MessageContext ctx) {
		return ctx.getServerHandler().player;
	}
	
	
	
	// Fluid Colours
	
	public void registerFluidBlockRendering(Block block, String name) {
		
	}
	
	public void initFluidColors() {
		
	}
	
	// TiC
	
	@Optional.Method(modid = "tconstruct")
	public void setRenderInfo(Material mat, int color) {
		
	}
	
	@Optional.Method(modid = "tconstruct")
	public void setRenderInfo(Material mat, int lo, int mid, int hi) {
		
	}
	
	// Multiblocks
	
	public IMultiblockRegistry initMultiblockRegistry() {

        if (multiblockEventHandler == null)
            MinecraftForge.EVENT_BUS.register(multiblockEventHandler = new MultiblockEventHandler());

        return MultiblockRegistry.INSTANCE;
    }

    private static MultiblockEventHandler multiblockEventHandler = null;
}
