package nc.proxy;

import java.util.Locale;

import nc.Global;
import nc.ModCheck;
import nc.capability.radiation.RadiationCapabilityHandler;
import nc.command.CommandHandler;
import nc.config.NCConfig;
import nc.handler.CapabilityHandler;
import nc.handler.DropHandler;
import nc.handler.DungeonLootHandler;
import nc.handler.EntityHandler;
import nc.handler.ItemUseHandler;
import nc.handler.OreDictHandler;
import nc.handler.PlayerRespawnHandler;
import nc.init.NCArmor;
import nc.init.NCBlocks;
import nc.init.NCCoolantFluids;
import nc.init.NCEntities;
import nc.init.NCFissionFluids;
import nc.init.NCFluids;
import nc.init.NCItems;
import nc.init.NCSounds;
import nc.init.NCTiles;
import nc.init.NCTools;
import nc.integration.crafttweaker.CTRegistration;
import nc.integration.crafttweaker.CTRegistration.RegistrationInfo;
import nc.integration.hwyla.NCHWLYA;
import nc.integration.projecte.NCProjectE;
import nc.integration.tconstruct.TConstructExtras;
import nc.integration.tconstruct.TConstructIMC;
import nc.integration.tconstruct.TConstructMaterials;
import nc.integration.tconstruct.conarm.ConArmMaterials;
import nc.multiblock.MultiblockHandler;
import nc.multiblock.MultiblockLogic;
import nc.multiblock.MultiblockRegistry;
import nc.multiblock.PlacementRule;
import nc.network.PacketHandler;
import nc.radiation.RadArmor;
import nc.radiation.RadBiomes;
import nc.radiation.RadEntities;
import nc.radiation.RadPotionEffects;
import nc.radiation.RadSources;
import nc.radiation.RadStructures;
import nc.radiation.RadWorlds;
import nc.radiation.RadiationHandler;
import nc.radiation.environment.RadiationEnvironmentHandler;
import nc.recipe.NCRecipes;
import nc.recipe.vanilla.CraftingRecipeHandler;
import nc.util.GasHelper;
import nc.util.OreDictHelper;
import nc.util.StructureHelper;
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
import net.minecraftforge.fml.common.event.FMLModIdMappingEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.registry.GameRegistry;
import slimeknights.tconstruct.library.materials.Material;

public class CommonProxy {
	
	public void preInit(FMLPreInitializationEvent preEvent) {
		ModCheck.init();
		
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
		
		NCFluids.register();
		NCFissionFluids.register();
		NCCoolantFluids.register();
		
		NCTiles.register();
		
		MultiblockHandler.init();
		MultiblockLogic.init();
		PlacementRule.preInit();
		
		OreDictHandler.registerOres();
		
		PacketHandler.registerMessages(Global.MOD_ID);
		
		if (ModCheck.mekanismLoaded()) {
			GasHelper.preInit();
		}
		MinecraftForge.EVENT_BUS.register(new NCRecipes());
		
		if (ModCheck.tinkersLoaded()) {
			TConstructIMC.sendIMCs();
			TConstructMaterials.init();
			
			if (ModCheck.constructsArmoryLoaded()) {
				ConArmMaterials.preInit();
			}
		}
		
		for (RegistrationInfo info : CTRegistration.INFO_LIST) {
			info.preInit();
		}
	}
	
	public void init(FMLInitializationEvent event) {
		initFluidColors();
		
		CapabilityHandler.init();
		
		NCRecipes.init();
		
		MinecraftForge.EVENT_BUS.register(new DropHandler());
		MinecraftForge.EVENT_BUS.register(new DungeonLootHandler());
		
		RadArmor.init();
		
		NCBiomes.initBiomeManagerAndDictionary();
		NCWorlds.registerDimensions();
		
		GameRegistry.registerWorldGenerator(new OreGenerator(), 0);
		GameRegistry.registerWorldGenerator(new BushGenerator(), 100);
		// GameRegistry.registerWorldGenerator(new WastelandPortalGenerator(), 10);
		
		NCEntities.register();
		MinecraftForge.EVENT_BUS.register(new EntityHandler());
		
		PlacementRule.init();
		
		if (ModCheck.tinkersLoaded()) {
			TConstructExtras.init();
			
			if (ModCheck.constructsArmoryLoaded()) {
				ConArmMaterials.init();
			}
		}
		
		if (ModCheck.hwylaLoaded()) {
			NCHWLYA.init();
		}
		
		for (RegistrationInfo info : CTRegistration.INFO_LIST) {
			info.init();
		}
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
		
		PlacementRule.postInit();
		
		if (ModCheck.projectELoaded() && NCConfig.register_projecte_emc) {
			NCProjectE.addEMCValues();
		}
		
		for (RegistrationInfo info : CTRegistration.INFO_LIST) {
			info.postInit();
		}
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
		
		PlacementRule.refreshTooltipRecipeHandlers();
		
		RadSources.refreshRadSources();
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
		name = name.toLowerCase(Locale.ROOT);
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
	
	public MultiblockRegistry initMultiblockRegistry() {
		
		if (multiblockEventHandler == null) {
			MinecraftForge.EVENT_BUS.register(multiblockEventHandler = new MultiblockHandler());
		}
		
		return MultiblockRegistry.INSTANCE;
	}
	
	private static MultiblockHandler multiblockEventHandler = null;
}
