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
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
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
	
	private static final Minecraft MC = Minecraft.getMinecraft();
	
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
			return MC.world;
	}

	@Override
	public int getCurrentClientDimension() {
		return MC.world.provider.getDimension();
	}
	
	@Override
	public EntityPlayer getPlayerEntity(MessageContext ctx) {
		return ctx.side.isClient() ? MC.player : super.getPlayerEntity(ctx);
	}
	
	// Fluid Colours
	
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
	
	private static <T extends Fluid> void initFluidColors(List<T> fluidList) {
		if(FMLCommonHandler.instance().getEffectiveSide().isClient()) {
			BlockColors blockcolors = MC.getBlockColors();
			ItemColors itemcolors = MC.getItemColors();
			for(T fluid : fluidList) {
				if (fluid.getBlock() != null) if (NCBlockFluid.class.isAssignableFrom(fluid.getBlock().getClass())) {
					NCBlockFluid block = (NCBlockFluid) fluid.getBlock();
					blockcolors.registerBlockColorHandler(new ColorRenderer.FluidBlockColor(block), block);
					itemcolors.registerItemColorHandler(new ColorRenderer.FluidItemBlockColor(block), block);
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
