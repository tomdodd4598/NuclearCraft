package nc.proxy;

import java.util.ArrayList;
import java.util.List;

import nc.Global;
import nc.block.fluid.NCBlockFluid;
import nc.config.NCConfig;
import nc.handler.RenderHandler;
import nc.handler.SoundHandler;
import nc.handler.TooltipHandler;
import nc.init.NCCoolantFluids;
import nc.init.NCFissionFluids;
import nc.model.ModelTexturedFluid;
import nc.radiation.RadiationRenders;
import nc.render.ColorRenderer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import slimeknights.tconstruct.library.client.MaterialRenderInfo;
import slimeknights.tconstruct.library.materials.Material;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void preInit(FMLPreInitializationEvent preEvent) {
		super.preInit(preEvent);
		
		//OBJLoader.INSTANCE.addDomain(Global.MOD_ID);
		
		NCConfig.clientPreInit();
		
		RenderHandler.init();
	}

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		
		MinecraftForge.EVENT_BUS.register(SoundHandler.class);
	}

	@Override
	public void postInit(FMLPostInitializationEvent postEvent) {
		super.postInit(postEvent);
		
		MinecraftForge.EVENT_BUS.register(new TooltipHandler());
		
		MinecraftForge.EVENT_BUS.register(new RadiationRenders());
	}
	
	// Packets
	
	@Override
	public World getWorld(int dimensionId) {
		if (getCurrentClientDimension() != dimensionId) {
			return null;
		} else
			return Minecraft.getMinecraft().world;
	}

	@Override
	public int getCurrentClientDimension() {
		return Minecraft.getMinecraft().world.provider.getDimension();
	}
	
	@Override
	public EntityPlayer getPlayerEntity(MessageContext ctx) {
		return ctx.side.isClient() ? Minecraft.getMinecraft().player : super.getPlayerEntity(ctx);
	}
	
	// Fluid Colours
	
	static {
		ModelLoaderRegistry.registerLoader(ModelTexturedFluid.FluidTexturedLoader.INSTANCE);
	}
	
	@Override
	public void registerFluidBlockRendering(Block block, String name) {
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
		if (NCConfig.register_fluid_blocks && FMLCommonHandler.instance().getEffectiveSide().isClient()) {
			List<Fluid> fluidList = new ArrayList<>();
			fluidList.addAll(NCCoolantFluids.fluidList);
			fluidList.addAll(NCFissionFluids.fluidList);
			
			for (Fluid fluid : fluidList) {
				if (fluid.getBlock() != null && NCBlockFluid.class.isAssignableFrom(fluid.getBlock().getClass())) {
					NCBlockFluid fluidBlock = (NCBlockFluid) fluid.getBlock();
					Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(new ColorRenderer.FluidBlockColor(fluidBlock), fluidBlock);
					Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new ColorRenderer.FluidItemBlockColor(fluidBlock), fluidBlock);
				}
			}
		}
	}
	
	// TiC
	
	@Override
	@Optional.Method(modid = "tconstruct")
	public void setRenderInfo(Material mat, int color) {
		mat.setRenderInfo(color);
	}
	
	@Override
	@Optional.Method(modid = "tconstruct")
	public void setRenderInfo(Material mat, int lo, int mid, int hi) {
		mat.setRenderInfo(new MaterialRenderInfo.MultiColor(lo, mid, hi));
	}
}
