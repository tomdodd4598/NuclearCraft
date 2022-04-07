package nc.handler;

import org.apache.commons.lang3.tuple.Pair;

import nc.block.fluid.NCBlockFluid;
import nc.entity.EntityFeralGhoul;
import nc.fluid.FluidFission;
import nc.init.*;
import nc.multiblock.qComputer.tile.TileQuantumComputerQubit;
import nc.multiblock.turbine.tile.TileTurbineController;
import nc.render.BlockHighlightHandler;
import nc.render.entity.RenderFeralGhoul;
import nc.render.tile.*;
import nc.util.NCUtil;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.client.registry.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RenderHandler {
	
	public static void init() {
		NCBlocks.registerRenders();
		NCItems.registerRenders();
		NCTools.registerRenders();
		NCArmor.registerRenders();
		
		// MinecraftForge.EVENT_BUS.register(new TextureStichHandler());
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileTurbineController.class, new RenderTurbineRotor());
		ClientRegistry.bindTileEntitySpecialRenderer(TileQuantumComputerQubit.class, new RenderQuantumComputerQubit());
		
		registerEntityRender(EntityFeralGhoul.class, RenderFeralGhoul.class);
		
		MinecraftForge.EVENT_BUS.register(new BlockHighlightHandler());
	}
	
	protected static <E extends Entity, R extends Render<E>> void registerEntityRender(Class<E> entityClass, Class<R> renderClass) {
		RenderingRegistry.registerEntityRenderingHandler(entityClass, manager -> {
			R render = null;
			try {
				render = NCUtil.newInstance(renderClass, manager);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			return render;
		});
	}
	
	protected static class TextureStichHandler {
		
		@SubscribeEvent
		public void stitchTextures(TextureStitchEvent.Pre event) {
			TextureMap map = event.getMap();
			for (Pair<Fluid, NCBlockFluid> pair : NCFluids.fluidPairList) {
				map.registerSprite(pair.getLeft().getStill());
				map.registerSprite(pair.getLeft().getFlowing());
			}
			for (FluidFission fluid : NCFissionFluids.fluidList) {
				map.registerSprite(fluid.getStill());
				map.registerSprite(fluid.getFlowing());
			}
			for (Pair<Fluid, NCBlockFluid> pair : NCCoolantFluids.fluidPairList) {
				map.registerSprite(pair.getLeft().getStill());
				map.registerSprite(pair.getLeft().getFlowing());
			}
		}
	}
}
