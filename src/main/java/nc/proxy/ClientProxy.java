package nc.proxy;

import nc.Global;
import nc.config.NCConfig;
import nc.init.NCArmor;
import nc.init.NCBlocks;
import nc.init.NCItems;
import nc.init.NCTools;
import nc.render.RenderFusionCore;
import nc.tile.generator.TileFusionCore;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void preInit(FMLPreInitializationEvent preEvent) {
		super.preInit(preEvent);
		
		OBJLoader.INSTANCE.addDomain(Global.MOD_ID);
		
		NCConfig.clientPreInit();
		
		NCBlocks.registerRenders();
		NCItems.registerRenders();
		NCTools.registerRenders();
		NCArmor.registerRenders();
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileFusionCore.class, new RenderFusionCore());
		//ClientRegistry.bindTileEntitySpecialRenderer(TileSpin.class, new RenderSpin());
		
		registerModelBakeryVariants();
	}

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
	}

	@Override
	public void postInit(FMLPostInitializationEvent postEvent) {
		super.postInit(postEvent);
	}
	
	public void registerModelBakeryVariants() {
		ModelBakery.registerItemVariants(Item.getItemFromBlock(NCBlocks.ore),
		new ResourceLocation(Global.MOD_ID, "ore_copper"),
		new ResourceLocation(Global.MOD_ID, "ore_tin"),
		new ResourceLocation(Global.MOD_ID, "ore_lead"),
		new ResourceLocation(Global.MOD_ID, "ore_thorium"),
		new ResourceLocation(Global.MOD_ID, "ore_uranium"),
		new ResourceLocation(Global.MOD_ID, "ore_boron"),
		new ResourceLocation(Global.MOD_ID, "ore_lithium"),
		new ResourceLocation(Global.MOD_ID, "ore_magnesium"));
		
		ModelBakery.registerItemVariants(Item.getItemFromBlock(NCBlocks.ingot_block),
		new ResourceLocation(Global.MOD_ID, "ingot_block_copper"),
		new ResourceLocation(Global.MOD_ID, "ingot_block_tin"),
		new ResourceLocation(Global.MOD_ID, "ingot_block_lead"),
		new ResourceLocation(Global.MOD_ID, "ingot_block_thorium"),
		new ResourceLocation(Global.MOD_ID, "ingot_block_uranium"),
		new ResourceLocation(Global.MOD_ID, "ingot_block_boron"),
		new ResourceLocation(Global.MOD_ID, "ingot_block_lithium"),
		new ResourceLocation(Global.MOD_ID, "ingot_block_magnesium"),
		new ResourceLocation(Global.MOD_ID, "ingot_block_graphite"),
		new ResourceLocation(Global.MOD_ID, "ingot_block_beryllium"),
		new ResourceLocation(Global.MOD_ID, "ingot_block_zirconium"));
		
		ModelBakery.registerItemVariants(Item.getItemFromBlock(NCBlocks.fission_block),
		new ResourceLocation(Global.MOD_ID, "fission_block_casing"),
		new ResourceLocation(Global.MOD_ID, "fission_block_blast"));
		
		ModelBakery.registerItemVariants(Item.getItemFromBlock(NCBlocks.cooler),
		new ResourceLocation(Global.MOD_ID, "cooler_empty"),
		new ResourceLocation(Global.MOD_ID, "cooler_water"),
		new ResourceLocation(Global.MOD_ID, "cooler_redstone"),
		new ResourceLocation(Global.MOD_ID, "cooler_quartz"),
		new ResourceLocation(Global.MOD_ID, "cooler_gold"),
		new ResourceLocation(Global.MOD_ID, "cooler_glowstone"),
		new ResourceLocation(Global.MOD_ID, "cooler_lapis"),
		new ResourceLocation(Global.MOD_ID, "cooler_diamond"),
		new ResourceLocation(Global.MOD_ID, "cooler_helium"),
		new ResourceLocation(Global.MOD_ID, "cooler_enderium"),
		new ResourceLocation(Global.MOD_ID, "cooler_cryotheum"));
		
		ModelBakery.registerItemVariants(NCItems.ingot,
		new ResourceLocation(Global.MOD_ID, "ingot_copper"),
		new ResourceLocation(Global.MOD_ID, "ingot_tin"),
		new ResourceLocation(Global.MOD_ID, "ingot_lead"),
		new ResourceLocation(Global.MOD_ID, "ingot_thorium"),
		new ResourceLocation(Global.MOD_ID, "ingot_uranium"),
		new ResourceLocation(Global.MOD_ID, "ingot_boron"),
		new ResourceLocation(Global.MOD_ID, "ingot_lithium"),
		new ResourceLocation(Global.MOD_ID, "ingot_magnesium"),
		new ResourceLocation(Global.MOD_ID, "ingot_graphite"),
		new ResourceLocation(Global.MOD_ID, "ingot_beryllium"),
		new ResourceLocation(Global.MOD_ID, "ingot_zirconium"));
		
		ModelBakery.registerItemVariants(NCItems.ingot_oxide,
		new ResourceLocation(Global.MOD_ID, "ingot_oxide_thorium"),
		new ResourceLocation(Global.MOD_ID, "ingot_oxide_uranium"),
		new ResourceLocation(Global.MOD_ID, "ingot_oxide_manganese"),
		new ResourceLocation(Global.MOD_ID, "ingot_oxide_manganese2"));
		
		ModelBakery.registerItemVariants(NCItems.dust,
		new ResourceLocation(Global.MOD_ID, "dust_copper"),
		new ResourceLocation(Global.MOD_ID, "dust_tin"),
		new ResourceLocation(Global.MOD_ID, "dust_lead"),
		new ResourceLocation(Global.MOD_ID, "dust_thorium"),
		new ResourceLocation(Global.MOD_ID, "dust_uranium"),
		new ResourceLocation(Global.MOD_ID, "dust_boron"),
		new ResourceLocation(Global.MOD_ID, "dust_lithium"),
		new ResourceLocation(Global.MOD_ID, "dust_magnesium"),
		new ResourceLocation(Global.MOD_ID, "dust_graphite"),
		new ResourceLocation(Global.MOD_ID, "dust_beryllium"),
		new ResourceLocation(Global.MOD_ID, "dust_zirconium"));
		
		ModelBakery.registerItemVariants(NCItems.dust_oxide,
		new ResourceLocation(Global.MOD_ID, "dust_oxide_thorium"),
		new ResourceLocation(Global.MOD_ID, "dust_oxide_uranium"),
		new ResourceLocation(Global.MOD_ID, "dust_oxide_manganese"),
		new ResourceLocation(Global.MOD_ID, "dust_oxide_manganese2"));
		
		ModelBakery.registerItemVariants(NCItems.gem,
		new ResourceLocation(Global.MOD_ID, "gem_rhodochrosite"),
		new ResourceLocation(Global.MOD_ID, "gem_boron_nitride"),
		new ResourceLocation(Global.MOD_ID, "gem_fluorite"));
		
		ModelBakery.registerItemVariants(NCItems.gem_dust,
		new ResourceLocation(Global.MOD_ID, "gem_dust_diamond"),
		new ResourceLocation(Global.MOD_ID, "gem_dust_rhodochrosite"),
		new ResourceLocation(Global.MOD_ID, "gem_dust_quartz"),
		new ResourceLocation(Global.MOD_ID, "gem_dust_obsidian"),
		new ResourceLocation(Global.MOD_ID, "gem_dust_boron_nitride"),
		new ResourceLocation(Global.MOD_ID, "gem_dust_fluorite"),
		new ResourceLocation(Global.MOD_ID, "gem_dust_sulfur"));
		
		ModelBakery.registerItemVariants(NCItems.alloy,
		new ResourceLocation(Global.MOD_ID, "alloy_bronze"),
		new ResourceLocation(Global.MOD_ID, "alloy_tough"),
		new ResourceLocation(Global.MOD_ID, "alloy_hard_carbon"),
		new ResourceLocation(Global.MOD_ID, "alloy_magnesium_diboride"),
		new ResourceLocation(Global.MOD_ID, "alloy_lithium_manganese_dioxide"),
		new ResourceLocation(Global.MOD_ID, "alloy_steel"),
		new ResourceLocation(Global.MOD_ID, "alloy_ferroboron"),
		new ResourceLocation(Global.MOD_ID, "alloy_shibuichi"),
		new ResourceLocation(Global.MOD_ID, "alloy_tin_silver"),
		new ResourceLocation(Global.MOD_ID, "alloy_lead_platinum"));
		
		ModelBakery.registerItemVariants(NCItems.compound,
		new ResourceLocation(Global.MOD_ID, "compound_calcium_sulfate"),
		new ResourceLocation(Global.MOD_ID, "compound_crystal_binder"));
		
		ModelBakery.registerItemVariants(NCItems.part,
		new ResourceLocation(Global.MOD_ID, "part_plate_basic"),
		new ResourceLocation(Global.MOD_ID, "part_plate_advanced"),
		new ResourceLocation(Global.MOD_ID, "part_plate_du"),
		new ResourceLocation(Global.MOD_ID, "part_plate_elite"),
		new ResourceLocation(Global.MOD_ID, "part_wire_copper"),
		new ResourceLocation(Global.MOD_ID, "part_wire_magnesium_diboride"),
		new ResourceLocation(Global.MOD_ID, "part_bioplastic"));
		
		ModelBakery.registerItemVariants(NCItems.upgrade,
		new ResourceLocation(Global.MOD_ID, "upgrade_speed"));
		
		ModelBakery.registerItemVariants(NCItems.thorium,
		new ResourceLocation(Global.MOD_ID, "thorium_230"),
		new ResourceLocation(Global.MOD_ID, "thorium_230_oxide"),
		new ResourceLocation(Global.MOD_ID, "thorium_230_tiny"),
		new ResourceLocation(Global.MOD_ID, "thorium_230_oxide_tiny"),
		new ResourceLocation(Global.MOD_ID, "thorium_232"),
		new ResourceLocation(Global.MOD_ID, "thorium_232_oxide"),
		new ResourceLocation(Global.MOD_ID, "thorium_232_tiny"),
		new ResourceLocation(Global.MOD_ID, "thorium_232_oxide_tiny"));
		
		ModelBakery.registerItemVariants(NCItems.uranium,
		new ResourceLocation(Global.MOD_ID, "uranium_233"),
		new ResourceLocation(Global.MOD_ID, "uranium_233_oxide"),
		new ResourceLocation(Global.MOD_ID, "uranium_233_tiny"),
		new ResourceLocation(Global.MOD_ID, "uranium_233_oxide_tiny"),
		new ResourceLocation(Global.MOD_ID, "uranium_235"),
		new ResourceLocation(Global.MOD_ID, "uranium_235_oxide"),
		new ResourceLocation(Global.MOD_ID, "uranium_235_tiny"),
		new ResourceLocation(Global.MOD_ID, "uranium_235_oxide_tiny"),
		new ResourceLocation(Global.MOD_ID, "uranium_238"),
		new ResourceLocation(Global.MOD_ID, "uranium_238_oxide"),
		new ResourceLocation(Global.MOD_ID, "uranium_238_tiny"),
		new ResourceLocation(Global.MOD_ID, "uranium_238_oxide_tiny"));
		
		ModelBakery.registerItemVariants(NCItems.neptunium,
		new ResourceLocation(Global.MOD_ID, "neptunium_236"),
		new ResourceLocation(Global.MOD_ID, "neptunium_236_oxide"),
		new ResourceLocation(Global.MOD_ID, "neptunium_236_tiny"),
		new ResourceLocation(Global.MOD_ID, "neptunium_236_oxide_tiny"),
		new ResourceLocation(Global.MOD_ID, "neptunium_237"),
		new ResourceLocation(Global.MOD_ID, "neptunium_237_oxide"),
		new ResourceLocation(Global.MOD_ID, "neptunium_237_tiny"),
		new ResourceLocation(Global.MOD_ID, "neptunium_237_oxide_tiny"));
		
		ModelBakery.registerItemVariants(NCItems.plutonium,
		new ResourceLocation(Global.MOD_ID, "plutonium_238"),
		new ResourceLocation(Global.MOD_ID, "plutonium_238_oxide"),
		new ResourceLocation(Global.MOD_ID, "plutonium_238_tiny"),
		new ResourceLocation(Global.MOD_ID, "plutonium_238_oxide_tiny"),
		new ResourceLocation(Global.MOD_ID, "plutonium_239"),
		new ResourceLocation(Global.MOD_ID, "plutonium_239_oxide"),
		new ResourceLocation(Global.MOD_ID, "plutonium_239_tiny"),
		new ResourceLocation(Global.MOD_ID, "plutonium_239_oxide_tiny"),
		new ResourceLocation(Global.MOD_ID, "plutonium_241"),
		new ResourceLocation(Global.MOD_ID, "plutonium_241_oxide"),
		new ResourceLocation(Global.MOD_ID, "plutonium_241_tiny"),
		new ResourceLocation(Global.MOD_ID, "plutonium_241_oxide_tiny"),
		new ResourceLocation(Global.MOD_ID, "plutonium_242"),
		new ResourceLocation(Global.MOD_ID, "plutonium_242_oxide"),
		new ResourceLocation(Global.MOD_ID, "plutonium_242_tiny"),
		new ResourceLocation(Global.MOD_ID, "plutonium_242_oxide_tiny"));
		
		ModelBakery.registerItemVariants(NCItems.americium,
		new ResourceLocation(Global.MOD_ID, "americium_241"),
		new ResourceLocation(Global.MOD_ID, "americium_241_oxide"),
		new ResourceLocation(Global.MOD_ID, "americium_241_tiny"),
		new ResourceLocation(Global.MOD_ID, "americium_241_oxide_tiny"),
		new ResourceLocation(Global.MOD_ID, "americium_242"),
		new ResourceLocation(Global.MOD_ID, "americium_242_oxide"),
		new ResourceLocation(Global.MOD_ID, "americium_242_tiny"),
		new ResourceLocation(Global.MOD_ID, "americium_242_oxide_tiny"),
		new ResourceLocation(Global.MOD_ID, "americium_243"),
		new ResourceLocation(Global.MOD_ID, "americium_243_oxide"),
		new ResourceLocation(Global.MOD_ID, "americium_243_tiny"),
		new ResourceLocation(Global.MOD_ID, "americium_243_oxide_tiny"));
		
		ModelBakery.registerItemVariants(NCItems.curium,
		new ResourceLocation(Global.MOD_ID, "curium_243"),
		new ResourceLocation(Global.MOD_ID, "curium_243_oxide"),
		new ResourceLocation(Global.MOD_ID, "curium_243_tiny"),
		new ResourceLocation(Global.MOD_ID, "curium_243_oxide_tiny"),
		new ResourceLocation(Global.MOD_ID, "curium_245"),
		new ResourceLocation(Global.MOD_ID, "curium_245_oxide"),
		new ResourceLocation(Global.MOD_ID, "curium_245_tiny"),
		new ResourceLocation(Global.MOD_ID, "curium_245_oxide_tiny"),
		new ResourceLocation(Global.MOD_ID, "curium_246"),
		new ResourceLocation(Global.MOD_ID, "curium_246_oxide"),
		new ResourceLocation(Global.MOD_ID, "curium_246_tiny"),
		new ResourceLocation(Global.MOD_ID, "curium_246_oxide_tiny"),
		new ResourceLocation(Global.MOD_ID, "curium_247"),
		new ResourceLocation(Global.MOD_ID, "curium_247_oxide"),
		new ResourceLocation(Global.MOD_ID, "curium_247_tiny"),
		new ResourceLocation(Global.MOD_ID, "curium_247_oxide_tiny"));
		
		ModelBakery.registerItemVariants(NCItems.berkelium,
		new ResourceLocation(Global.MOD_ID, "berkelium_247"),
		new ResourceLocation(Global.MOD_ID, "berkelium_247_oxide"),
		new ResourceLocation(Global.MOD_ID, "berkelium_247_tiny"),
		new ResourceLocation(Global.MOD_ID, "berkelium_247_oxide_tiny"),
		new ResourceLocation(Global.MOD_ID, "berkelium_248"),
		new ResourceLocation(Global.MOD_ID, "berkelium_248_oxide"),
		new ResourceLocation(Global.MOD_ID, "berkelium_248_tiny"),
		new ResourceLocation(Global.MOD_ID, "berkelium_248_oxide_tiny"));
		
		ModelBakery.registerItemVariants(NCItems.californium,
		new ResourceLocation(Global.MOD_ID, "californium_249"),
		new ResourceLocation(Global.MOD_ID, "californium_249_oxide"),
		new ResourceLocation(Global.MOD_ID, "californium_249_tiny"),
		new ResourceLocation(Global.MOD_ID, "californium_249_oxide_tiny"),
		new ResourceLocation(Global.MOD_ID, "californium_250"),
		new ResourceLocation(Global.MOD_ID, "californium_250_oxide"),
		new ResourceLocation(Global.MOD_ID, "californium_250_tiny"),
		new ResourceLocation(Global.MOD_ID, "californium_250_oxide_tiny"),
		new ResourceLocation(Global.MOD_ID, "californium_251"),
		new ResourceLocation(Global.MOD_ID, "californium_251_oxide"),
		new ResourceLocation(Global.MOD_ID, "californium_251_tiny"),
		new ResourceLocation(Global.MOD_ID, "californium_251_oxide_tiny"),
		new ResourceLocation(Global.MOD_ID, "californium_252"),
		new ResourceLocation(Global.MOD_ID, "californium_252_oxide"),
		new ResourceLocation(Global.MOD_ID, "californium_252_tiny"),
		new ResourceLocation(Global.MOD_ID, "californium_252_oxide_tiny"));
		
		ModelBakery.registerItemVariants(NCItems.fuel_thorium,
		new ResourceLocation(Global.MOD_ID, "fuel_thorium_tbu"),
		new ResourceLocation(Global.MOD_ID, "fuel_thorium_tbu_oxide"));
		
		ModelBakery.registerItemVariants(NCItems.fuel_uranium,
		new ResourceLocation(Global.MOD_ID, "fuel_uranium_leu_233"),
		new ResourceLocation(Global.MOD_ID, "fuel_uranium_leu_233_oxide"),
		new ResourceLocation(Global.MOD_ID, "fuel_uranium_heu_233"),
		new ResourceLocation(Global.MOD_ID, "fuel_uranium_heu_233_oxide"),
		new ResourceLocation(Global.MOD_ID, "fuel_uranium_leu_235"),
		new ResourceLocation(Global.MOD_ID, "fuel_uranium_leu_235_oxide"),
		new ResourceLocation(Global.MOD_ID, "fuel_uranium_heu_235"),
		new ResourceLocation(Global.MOD_ID, "fuel_uranium_heu_235_oxide"));
		
		ModelBakery.registerItemVariants(NCItems.fuel_neptunium,
		new ResourceLocation(Global.MOD_ID, "fuel_neptunium_len_236"),
		new ResourceLocation(Global.MOD_ID, "fuel_neptunium_len_236_oxide"),
		new ResourceLocation(Global.MOD_ID, "fuel_neptunium_hen_236"),
		new ResourceLocation(Global.MOD_ID, "fuel_neptunium_hen_236_oxide"));
		
		ModelBakery.registerItemVariants(NCItems.fuel_plutonium,
		new ResourceLocation(Global.MOD_ID, "fuel_plutonium_lep_239"),
		new ResourceLocation(Global.MOD_ID, "fuel_plutonium_lep_239_oxide"),
		new ResourceLocation(Global.MOD_ID, "fuel_plutonium_hep_239"),
		new ResourceLocation(Global.MOD_ID, "fuel_plutonium_hep_239_oxide"),
		new ResourceLocation(Global.MOD_ID, "fuel_plutonium_lep_241"),
		new ResourceLocation(Global.MOD_ID, "fuel_plutonium_lep_241_oxide"),
		new ResourceLocation(Global.MOD_ID, "fuel_plutonium_hep_241"),
		new ResourceLocation(Global.MOD_ID, "fuel_plutonium_hep_241_oxide"));
		
		ModelBakery.registerItemVariants(NCItems.fuel_mixed_oxide,
		new ResourceLocation(Global.MOD_ID, "fuel_mixed_oxide_mox_239"),
		new ResourceLocation(Global.MOD_ID, "fuel_mixed_oxide_mox_241"));
		
		ModelBakery.registerItemVariants(NCItems.fuel_americium,
		new ResourceLocation(Global.MOD_ID, "fuel_americium_lea_242"),
		new ResourceLocation(Global.MOD_ID, "fuel_americium_lea_242_oxide"),
		new ResourceLocation(Global.MOD_ID, "fuel_americium_hea_242"),
		new ResourceLocation(Global.MOD_ID, "fuel_americium_hea_242_oxide"));
		
		ModelBakery.registerItemVariants(NCItems.fuel_curium,
		new ResourceLocation(Global.MOD_ID, "fuel_curium_lec_243"),
		new ResourceLocation(Global.MOD_ID, "fuel_curium_lec_243_oxide"),
		new ResourceLocation(Global.MOD_ID, "fuel_curium_hec_243"),
		new ResourceLocation(Global.MOD_ID, "fuel_curium_hec_243_oxide"),
		new ResourceLocation(Global.MOD_ID, "fuel_curium_lec_245"),
		new ResourceLocation(Global.MOD_ID, "fuel_curium_lec_245_oxide"),
		new ResourceLocation(Global.MOD_ID, "fuel_curium_hec_245"),
		new ResourceLocation(Global.MOD_ID, "fuel_curium_hec_245_oxide"),
		new ResourceLocation(Global.MOD_ID, "fuel_curium_lec_247"),
		new ResourceLocation(Global.MOD_ID, "fuel_curium_lec_247_oxide"),
		new ResourceLocation(Global.MOD_ID, "fuel_curium_hec_247"),
		new ResourceLocation(Global.MOD_ID, "fuel_curium_hec_247_oxide"));
		
		ModelBakery.registerItemVariants(NCItems.fuel_berkelium,
		new ResourceLocation(Global.MOD_ID, "fuel_berkelium_leb_248"),
		new ResourceLocation(Global.MOD_ID, "fuel_berkelium_leb_248_oxide"),
		new ResourceLocation(Global.MOD_ID, "fuel_berkelium_heb_248"),
		new ResourceLocation(Global.MOD_ID, "fuel_berkelium_heb_248_oxide"));
		
		ModelBakery.registerItemVariants(NCItems.fuel_californium,
		new ResourceLocation(Global.MOD_ID, "fuel_californium_lec_249"),
		new ResourceLocation(Global.MOD_ID, "fuel_californium_lec_249_oxide"),
		new ResourceLocation(Global.MOD_ID, "fuel_californium_hec_249"),
		new ResourceLocation(Global.MOD_ID, "fuel_californium_hec_249_oxide"),
		new ResourceLocation(Global.MOD_ID, "fuel_californium_lec_251"),
		new ResourceLocation(Global.MOD_ID, "fuel_californium_lec_251_oxide"),
		new ResourceLocation(Global.MOD_ID, "fuel_californium_hec_251"),
		new ResourceLocation(Global.MOD_ID, "fuel_californium_hec_251_oxide"));
		
		ModelBakery.registerItemVariants(NCItems.fuel_rod_thorium,
		new ResourceLocation(Global.MOD_ID, "fuel_rod_thorium_tbu"),
		new ResourceLocation(Global.MOD_ID, "fuel_rod_thorium_tbu_oxide"));
		
		ModelBakery.registerItemVariants(NCItems.fuel_rod_uranium,
		new ResourceLocation(Global.MOD_ID, "fuel_rod_uranium_leu_233"),
		new ResourceLocation(Global.MOD_ID, "fuel_rod_uranium_leu_233_oxide"),
		new ResourceLocation(Global.MOD_ID, "fuel_rod_uranium_heu_233"),
		new ResourceLocation(Global.MOD_ID, "fuel_rod_uranium_heu_233_oxide"),
		new ResourceLocation(Global.MOD_ID, "fuel_rod_uranium_leu_235"),
		new ResourceLocation(Global.MOD_ID, "fuel_rod_uranium_leu_235_oxide"),
		new ResourceLocation(Global.MOD_ID, "fuel_rod_uranium_heu_235"),
		new ResourceLocation(Global.MOD_ID, "fuel_rod_uranium_heu_235_oxide"));
		
		ModelBakery.registerItemVariants(NCItems.fuel_rod_neptunium,
		new ResourceLocation(Global.MOD_ID, "fuel_rod_neptunium_len_236"),
		new ResourceLocation(Global.MOD_ID, "fuel_rod_neptunium_len_236_oxide"),
		new ResourceLocation(Global.MOD_ID, "fuel_rod_neptunium_hen_236"),
		new ResourceLocation(Global.MOD_ID, "fuel_rod_neptunium_hen_236_oxide"));
		
		ModelBakery.registerItemVariants(NCItems.fuel_rod_plutonium,
		new ResourceLocation(Global.MOD_ID, "fuel_rod_plutonium_lep_239"),
		new ResourceLocation(Global.MOD_ID, "fuel_rod_plutonium_lep_239_oxide"),
		new ResourceLocation(Global.MOD_ID, "fuel_rod_plutonium_hep_239"),
		new ResourceLocation(Global.MOD_ID, "fuel_rod_plutonium_hep_239_oxide"),
		new ResourceLocation(Global.MOD_ID, "fuel_rod_plutonium_lep_241"),
		new ResourceLocation(Global.MOD_ID, "fuel_rod_plutonium_lep_241_oxide"),
		new ResourceLocation(Global.MOD_ID, "fuel_rod_plutonium_hep_241"),
		new ResourceLocation(Global.MOD_ID, "fuel_rod_plutonium_hep_241_oxide"));
		
		ModelBakery.registerItemVariants(NCItems.fuel_rod_mixed_oxide,
		new ResourceLocation(Global.MOD_ID, "fuel_rod_mixed_oxide_mox_239"),
		new ResourceLocation(Global.MOD_ID, "fuel_rod_mixed_oxide_mox_241"));
		
		ModelBakery.registerItemVariants(NCItems.fuel_rod_americium,
		new ResourceLocation(Global.MOD_ID, "fuel_rod_americium_lea_242"),
		new ResourceLocation(Global.MOD_ID, "fuel_rod_americium_lea_242_oxide"),
		new ResourceLocation(Global.MOD_ID, "fuel_rod_americium_hea_242"),
		new ResourceLocation(Global.MOD_ID, "fuel_rod_americium_hea_242_oxide"));
		
		ModelBakery.registerItemVariants(NCItems.fuel_rod_curium,
		new ResourceLocation(Global.MOD_ID, "fuel_rod_curium_lec_243"),
		new ResourceLocation(Global.MOD_ID, "fuel_rod_curium_lec_243_oxide"),
		new ResourceLocation(Global.MOD_ID, "fuel_rod_curium_hec_243"),
		new ResourceLocation(Global.MOD_ID, "fuel_rod_curium_hec_243_oxide"),
		new ResourceLocation(Global.MOD_ID, "fuel_rod_curium_lec_245"),
		new ResourceLocation(Global.MOD_ID, "fuel_rod_curium_lec_245_oxide"),
		new ResourceLocation(Global.MOD_ID, "fuel_rod_curium_hec_245"),
		new ResourceLocation(Global.MOD_ID, "fuel_rod_curium_hec_245_oxide"),
		new ResourceLocation(Global.MOD_ID, "fuel_rod_curium_lec_247"),
		new ResourceLocation(Global.MOD_ID, "fuel_rod_curium_lec_247_oxide"),
		new ResourceLocation(Global.MOD_ID, "fuel_rod_curium_hec_247"),
		new ResourceLocation(Global.MOD_ID, "fuel_rod_curium_hec_247_oxide"));
		
		ModelBakery.registerItemVariants(NCItems.fuel_rod_berkelium,
		new ResourceLocation(Global.MOD_ID, "fuel_rod_berkelium_leb_248"),
		new ResourceLocation(Global.MOD_ID, "fuel_rod_berkelium_leb_248_oxide"),
		new ResourceLocation(Global.MOD_ID, "fuel_rod_berkelium_heb_248"),
		new ResourceLocation(Global.MOD_ID, "fuel_rod_berkelium_heb_248_oxide"));
		
		ModelBakery.registerItemVariants(NCItems.fuel_rod_californium,
		new ResourceLocation(Global.MOD_ID, "fuel_rod_californium_lec_249"),
		new ResourceLocation(Global.MOD_ID, "fuel_rod_californium_lec_249_oxide"),
		new ResourceLocation(Global.MOD_ID, "fuel_rod_californium_hec_249"),
		new ResourceLocation(Global.MOD_ID, "fuel_rod_californium_hec_249_oxide"),
		new ResourceLocation(Global.MOD_ID, "fuel_rod_californium_lec_251"),
		new ResourceLocation(Global.MOD_ID, "fuel_rod_californium_lec_251_oxide"),
		new ResourceLocation(Global.MOD_ID, "fuel_rod_californium_hec_251"),
		new ResourceLocation(Global.MOD_ID, "fuel_rod_californium_hec_251_oxide"));
		
		ModelBakery.registerItemVariants(NCItems.depleted_fuel_rod_thorium,
		new ResourceLocation(Global.MOD_ID, "depleted_fuel_rod_thorium_tbu"),
		new ResourceLocation(Global.MOD_ID, "depleted_fuel_rod_thorium_tbu_oxide"));
		
		ModelBakery.registerItemVariants(NCItems.depleted_fuel_rod_uranium,
		new ResourceLocation(Global.MOD_ID, "depleted_fuel_rod_uranium_leu_233"),
		new ResourceLocation(Global.MOD_ID, "depleted_fuel_rod_uranium_leu_233_oxide"),
		new ResourceLocation(Global.MOD_ID, "depleted_fuel_rod_uranium_heu_233"),
		new ResourceLocation(Global.MOD_ID, "depleted_fuel_rod_uranium_heu_233_oxide"),
		new ResourceLocation(Global.MOD_ID, "depleted_fuel_rod_uranium_leu_235"),
		new ResourceLocation(Global.MOD_ID, "depleted_fuel_rod_uranium_leu_235_oxide"),
		new ResourceLocation(Global.MOD_ID, "depleted_fuel_rod_uranium_heu_235"),
		new ResourceLocation(Global.MOD_ID, "depleted_fuel_rod_uranium_heu_235_oxide"));
		
		ModelBakery.registerItemVariants(NCItems.depleted_fuel_rod_neptunium,
		new ResourceLocation(Global.MOD_ID, "depleted_fuel_rod_neptunium_len_236"),
		new ResourceLocation(Global.MOD_ID, "depleted_fuel_rod_neptunium_len_236_oxide"),
		new ResourceLocation(Global.MOD_ID, "depleted_fuel_rod_neptunium_hen_236"),
		new ResourceLocation(Global.MOD_ID, "depleted_fuel_rod_neptunium_hen_236_oxide"));
		
		ModelBakery.registerItemVariants(NCItems.depleted_fuel_rod_plutonium,
		new ResourceLocation(Global.MOD_ID, "depleted_fuel_rod_plutonium_lep_239"),
		new ResourceLocation(Global.MOD_ID, "depleted_fuel_rod_plutonium_lep_239_oxide"),
		new ResourceLocation(Global.MOD_ID, "depleted_fuel_rod_plutonium_hep_239"),
		new ResourceLocation(Global.MOD_ID, "depleted_fuel_rod_plutonium_hep_239_oxide"),
		new ResourceLocation(Global.MOD_ID, "depleted_fuel_rod_plutonium_lep_241"),
		new ResourceLocation(Global.MOD_ID, "depleted_fuel_rod_plutonium_lep_241_oxide"),
		new ResourceLocation(Global.MOD_ID, "depleted_fuel_rod_plutonium_hep_241"),
		new ResourceLocation(Global.MOD_ID, "depleted_fuel_rod_plutonium_hep_241_oxide"));
		
		ModelBakery.registerItemVariants(NCItems.depleted_fuel_rod_mixed_oxide,
		new ResourceLocation(Global.MOD_ID, "depleted_fuel_rod_mixed_oxide_mox_239"),
		new ResourceLocation(Global.MOD_ID, "depleted_fuel_rod_mixed_oxide_mox_241"));
		
		ModelBakery.registerItemVariants(NCItems.depleted_fuel_rod_americium,
		new ResourceLocation(Global.MOD_ID, "depleted_fuel_rod_americium_lea_242"),
		new ResourceLocation(Global.MOD_ID, "depleted_fuel_rod_americium_lea_242_oxide"),
		new ResourceLocation(Global.MOD_ID, "depleted_fuel_rod_americium_hea_242"),
		new ResourceLocation(Global.MOD_ID, "depleted_fuel_rod_americium_hea_242_oxide"));
		
		ModelBakery.registerItemVariants(NCItems.depleted_fuel_rod_curium,
		new ResourceLocation(Global.MOD_ID, "depleted_fuel_rod_curium_lec_243"),
		new ResourceLocation(Global.MOD_ID, "depleted_fuel_rod_curium_lec_243_oxide"),
		new ResourceLocation(Global.MOD_ID, "depleted_fuel_rod_curium_hec_243"),
		new ResourceLocation(Global.MOD_ID, "depleted_fuel_rod_curium_hec_243_oxide"),
		new ResourceLocation(Global.MOD_ID, "depleted_fuel_rod_curium_lec_245"),
		new ResourceLocation(Global.MOD_ID, "depleted_fuel_rod_curium_lec_245_oxide"),
		new ResourceLocation(Global.MOD_ID, "depleted_fuel_rod_curium_hec_245"),
		new ResourceLocation(Global.MOD_ID, "depleted_fuel_rod_curium_hec_245_oxide"),
		new ResourceLocation(Global.MOD_ID, "depleted_fuel_rod_curium_lec_247"),
		new ResourceLocation(Global.MOD_ID, "depleted_fuel_rod_curium_lec_247_oxide"),
		new ResourceLocation(Global.MOD_ID, "depleted_fuel_rod_curium_hec_247"),
		new ResourceLocation(Global.MOD_ID, "depleted_fuel_rod_curium_hec_247_oxide"));
		
		ModelBakery.registerItemVariants(NCItems.depleted_fuel_rod_berkelium,
		new ResourceLocation(Global.MOD_ID, "depleted_fuel_rod_berkelium_leb_248"),
		new ResourceLocation(Global.MOD_ID, "depleted_fuel_rod_berkelium_leb_248_oxide"),
		new ResourceLocation(Global.MOD_ID, "depleted_fuel_rod_berkelium_heb_248"),
		new ResourceLocation(Global.MOD_ID, "depleted_fuel_rod_berkelium_heb_248_oxide"));
		
		ModelBakery.registerItemVariants(NCItems.depleted_fuel_rod_californium,
		new ResourceLocation(Global.MOD_ID, "depleted_fuel_rod_californium_lec_249"),
		new ResourceLocation(Global.MOD_ID, "depleted_fuel_rod_californium_lec_249_oxide"),
		new ResourceLocation(Global.MOD_ID, "depleted_fuel_rod_californium_hec_249"),
		new ResourceLocation(Global.MOD_ID, "depleted_fuel_rod_californium_hec_249_oxide"),
		new ResourceLocation(Global.MOD_ID, "depleted_fuel_rod_californium_lec_251"),
		new ResourceLocation(Global.MOD_ID, "depleted_fuel_rod_californium_lec_251_oxide"),
		new ResourceLocation(Global.MOD_ID, "depleted_fuel_rod_californium_hec_251"),
		new ResourceLocation(Global.MOD_ID, "depleted_fuel_rod_californium_hec_251_oxide"));
		
		ModelBakery.registerItemVariants(NCItems.boron,
		new ResourceLocation(Global.MOD_ID, "boron_10"),
		new ResourceLocation(Global.MOD_ID, "boron_10_tiny"),
		new ResourceLocation(Global.MOD_ID, "boron_11"),
		new ResourceLocation(Global.MOD_ID, "boron_11_tiny"));
		
		ModelBakery.registerItemVariants(NCItems.lithium,
		new ResourceLocation(Global.MOD_ID, "lithium_6"),
		new ResourceLocation(Global.MOD_ID, "lithium_6_tiny"),
		new ResourceLocation(Global.MOD_ID, "lithium_7"),
		new ResourceLocation(Global.MOD_ID, "lithium_7_tiny"));
	}
	
	@Override
	public void registerFluidBlockRendering(Block block, String name) {
		name = name.toLowerCase();
		super.registerFluidBlockRendering(block, name);
		FluidStateMapper mapper = new FluidStateMapper(name);
		
		Item item = Item.getItemFromBlock(block);
		ModelBakery.registerItemVariants(item);
		ModelLoader.setCustomMeshDefinition(item, mapper);

		//ModelLoader.setCustomStateMapper(block, new StateMap.Builder().ignore(block.LEVEL).build());
		ModelLoader.setCustomStateMapper(block, mapper);
	}
	
	public static class FluidStateMapper extends StateMapperBase implements ItemMeshDefinition {
		public final ModelResourceLocation location;

		public FluidStateMapper(String name) {
			location = new ModelResourceLocation(Global.MOD_ID + ":fluids", name);
		}

		@Override
		protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
			return location;
		}

		@Override
		public ModelResourceLocation getModelLocation(ItemStack stack) {
			return location;
		}
	}
}
