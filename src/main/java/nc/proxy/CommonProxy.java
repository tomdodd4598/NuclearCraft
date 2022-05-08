package nc.proxy;

import static nc.config.NCConfig.register_projecte_emc;

import java.io.IOException;

import crafttweaker.CraftTweakerAPI;
import nc.*;
import nc.capability.radiation.RadiationCapabilityHandler;
import nc.command.CommandHandler;
import nc.handler.*;
import nc.init.*;
import nc.integration.crafttweaker.CTRegistration;
import nc.integration.crafttweaker.CTRegistration.RegistrationInfo;
import nc.integration.hwyla.NCHWLYA;
import nc.integration.projecte.NCProjectE;
import nc.integration.tconstruct.*;
import nc.integration.tconstruct.conarm.ConArmMaterials;
import nc.item.ItemMultitool;
import nc.multiblock.*;
import nc.network.PacketHandler;
import nc.radiation.*;
import nc.radiation.environment.RadiationEnvironmentHandler;
import nc.recipe.*;
import nc.recipe.vanilla.CraftingRecipeHandler;
import nc.tab.NCTabs;
import nc.util.*;
import nc.worldgen.biome.NCBiomes;
import nc.worldgen.decoration.MushroomGenerator;
import nc.worldgen.dimension.NCWorlds;
import nc.worldgen.ore.OreGenerator;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.registry.GameRegistry;
import slimeknights.tconstruct.library.materials.Material;

public class CommonProxy {
	
	public void onConstruction(FMLConstructionEvent constructionEvent) throws IOException {
		ScriptAddonHandler.init();
	}
	
	public void preInit(FMLPreInitializationEvent preEvent) {
		ModCheck.init();
		
		NCTabs.init();
		
		MinecraftForge.EVENT_BUS.register(new MultiblockHandler());
		
		if (ModCheck.craftTweakerLoaded()) {
			CraftTweakerAPI.tweaker.loadScript(false, "nc_preinit");
		}
		
		TileInfo.init();
		
		NCSounds.init();
		
		NCBlocks.init();
		NCItems.init();
		NCTools.init();
		NCArmor.init();
		
		NCFluids.init();
		NCFissionFluids.init();
		NCCoolantFluids.init();
		
		NCBlocks.register();
		NCItems.register();
		NCTools.register();
		NCArmor.register();
		
		OreDictHandler.registerOres();
		
		NCTiles.register();
		
		for (RegistrationInfo info : CTRegistration.INFO_LIST) {
			info.preInit();
		}
		
		NCFluids.register();
		NCFissionFluids.register();
		NCCoolantFluids.register();
		
		MultiblockHandler.init();
		MultiblockLogic.init();
		PlacementRule.preInit();
		
		PacketHandler.registerMessages(Global.MOD_ID);
		
		if (ModCheck.mekanismLoaded()) {
			GasHelper.preInit();
		}
		MinecraftForge.EVENT_BUS.register(new NCRecipes());
		
		if (ModCheck.tinkersLoaded()) {
			TConstructMaterials.init();
			TConstructIMC.init();
		}
		
		if (ModCheck.constructsArmoryLoaded()) {
			ConArmMaterials.preInit();
		}
	}
	
	public void init(FMLInitializationEvent event) {
		initFluidColors();
		
		CapabilityHandler.init();
		
		NCRecipes.init();
		
		MinecraftForge.EVENT_BUS.register(new DropHandler());
		MinecraftForge.EVENT_BUS.register(new DungeonLootHandler());
		
		RadSources.refreshRadSources(false);
		RadArmor.init();
		
		NCBiomes.initBiomeManagerAndDictionary();
		NCWorlds.registerDimensions();
		
		GameRegistry.registerWorldGenerator(new OreGenerator(), 0);
		GameRegistry.registerWorldGenerator(new MushroomGenerator(NCBlocks.glowing_mushroom.getDefaultState()), 255);
		
		NCEntities.register();
		MinecraftForge.EVENT_BUS.register(new EntityHandler());
		
		PlacementRule.init();
		
		ItemMultitool.registerRightClickLogic();
		
		if (ModCheck.constructsArmoryLoaded()) {
			ConArmMaterials.init();
		}
		
		if (ModCheck.hwylaLoaded()) {
			NCHWLYA.init();
		}
		
		for (RegistrationInfo info : CTRegistration.INFO_LIST) {
			info.init();
		}
		
		NCAdvancements.init();
	}
	
	public void postInit(FMLPostInitializationEvent postEvent) {
		if (ModCheck.mekanismLoaded()) {
			GasHelper.init();
		}
		
		CraftingRecipeHandler.registerRadShieldingCraftingRecipes();
		
		RadArmor.postInit();
		RadWorlds.init();
		RadPotionEffects.init();
		RadSources.postInit();
		RadStructures.init();
		RadEntities.init();
		
		MinecraftForge.EVENT_BUS.register(new RadiationCapabilityHandler());
		MinecraftForge.EVENT_BUS.register(new RadiationHandler());
		MinecraftForge.EVENT_BUS.register(new RadiationEnvironmentHandler());
		
		MinecraftForge.EVENT_BUS.register(new PlayerRespawnHandler());
		
		MinecraftForge.EVENT_BUS.register(new ItemUseHandler());
		
		RecipeStats.init();
		
		PlacementRule.postInit();
		
		if (ModCheck.projectELoaded() && register_projecte_emc) {
			NCProjectE.addEMCValues();
		}
		
		for (RegistrationInfo info : CTRegistration.INFO_LIST) {
			info.postInit();
		}
		CTRegistration.INFO_LIST.clear();
	}
	
	public void serverStart(FMLServerStartingEvent serverStartEvent) {
		RadBiomes.init();
		
		CommandHandler.registerCommands(serverStartEvent);
	}
	
	public void serverStop(FMLServerStoppedEvent serverStopEvent) {
		StructureHelper.CACHE.clear();
	}
	
	public void onIdMapping(FMLModIdMappingEvent idMappingEvent) {
		OreDictHelper.refreshOreDictCache();
		
		NCRecipes.refreshRecipeCaches();
		
		PlacementRule.refreshRecipeCaches();
		
		RadSources.refreshRadSources(true);
		RadArmor.refreshRadiationArmor();
	}
	
	// Packets
	
	public World getWorld(int dimensionId) {
		return FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(dimensionId);
	}
	
	public int getCurrentClientDimension() {
		return Integer.MIN_VALUE;
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
}
