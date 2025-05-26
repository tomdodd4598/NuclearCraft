package nc.handler;

import nc.entity.EntityFeralGhoul;
import nc.init.*;
import nc.render.BlockHighlightHandler;
import nc.render.entity.RenderFeralGhoul;
import nc.render.tile.*;
import nc.tile.hx.*;
import nc.tile.machine.*;
import nc.tile.quantum.TileQuantumComputerQubit;
import nc.tile.turbine.TileTurbineController;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.client.registry.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.function.Consumer;

public class RenderHandler {
	
	public static void init() {
		NCBlocks.registerRenders();
		NCItems.registerRenders();
		NCTools.registerRenders();
		NCArmor.registerRenders();
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileElectrolyzerController.class, new RenderMultiblockElectrolyzer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileDistillerController.class, new RenderMultiblockDistiller());
		ClientRegistry.bindTileEntitySpecialRenderer(TileInfiltratorController.class, new RenderMultiblockInfiltrator());
		ClientRegistry.bindTileEntitySpecialRenderer(TileHeatExchangerController.class, new RenderMultiblockHeatExchanger());
		ClientRegistry.bindTileEntitySpecialRenderer(TileCondenserController.class, new RenderMultiblockCondenser());
		ClientRegistry.bindTileEntitySpecialRenderer(TileTurbineController.class, new RenderTurbineRotor());
		ClientRegistry.bindTileEntitySpecialRenderer(TileQuantumComputerQubit.class, new RenderQuantumComputerQubit());
		
		RenderingRegistry.registerEntityRenderingHandler(EntityFeralGhoul.class, RenderFeralGhoul::new);
		
		MinecraftForge.EVENT_BUS.register(new BlockHighlightHandler());
	}
	
	protected static class TextureStitchHandler {
		
		@SubscribeEvent
		public void stitchTextures(TextureStitchEvent.Pre event) {
			TextureMap map = event.getMap();
			
			Consumer<Fluid> consumer = x -> {
				map.registerSprite(x.getStill());
				map.registerSprite(x.getFlowing());
			};
			
			NCFluids.fluidPairList.forEach(x -> consumer.accept(x.getLeft()));
			NCFissionFluids.fluidList.forEach(consumer);
			NCCoolantFluids.fluidPairList.forEach(x -> consumer.accept(x.getLeft()));
		}
	}
}
