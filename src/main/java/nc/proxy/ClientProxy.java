package nc.proxy;

import java.util.ArrayList;
import java.util.List;

import nc.Global;
import nc.block.fluid.BlockFluidBase;
import nc.config.NCConfig;
import nc.init.NCArmor;
import nc.init.NCBlocks;
import nc.init.NCCoolantFluids;
import nc.init.NCFissionFluids;
import nc.init.NCItems;
import nc.init.NCTools;
import nc.model.ModelTexturedFluid;
import nc.render.ColorRenderer;
import nc.render.RenderFusionCore;
import nc.tile.generator.TileFusionCore;
import nc.util.NCUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
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
	}

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
	}

	@Override
	public void postInit(FMLPostInitializationEvent postEvent) {
		super.postInit(postEvent);
	}
	
	static {
		ModelLoaderRegistry.registerLoader(ModelTexturedFluid.FluidTexturedLoader.INSTANCE);
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
	
	@Override
	public void initFluidColors() {
		super.initFluidColors();
		List<Fluid> fluidList = new ArrayList<Fluid>();
		fluidList.addAll(NCCoolantFluids.fluidList);
		fluidList.addAll(NCFissionFluids.fluidList);
		initFluidColors(fluidList);
	}
	
	private <T extends Fluid> void initFluidColors(List<T> fluidList) {
		if(FMLCommonHandler.instance().getEffectiveSide().isClient()) {
			BlockColors blockcolors = Minecraft.getMinecraft().getBlockColors();
			ItemColors itemcolors = Minecraft.getMinecraft().getItemColors();
			for(T fluid : fluidList) {
				if (fluid.getBlock() != null) if (NCUtil.isSubclassOf(fluid.getBlock().getClass(), BlockFluidBase.class)) {
					BlockFluidBase block = (BlockFluidBase) fluid.getBlock();
					blockcolors.registerBlockColorHandler(new ColorRenderer.FluidBlockColor(block), block);
					itemcolors.registerItemColorHandler(new ColorRenderer.FluidItemBlockColor(block), block);
				}
			}
		}
	}
}
