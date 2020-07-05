package nc.handler;

import nc.entity.EntityFeralGhoul;
import nc.init.*;
import nc.multiblock.qComputer.tile.TileQuantumComputerQubit;
import nc.multiblock.turbine.tile.TileTurbineController;
import nc.render.BlockHighlightHandler;
import nc.render.entity.RenderFeralGhoul;
import nc.render.tile.*;
import nc.util.NCUtil;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.*;

public class RenderHandler {
	
	public static void init() {
		NCBlocks.registerRenders();
		NCItems.registerRenders();
		NCTools.registerRenders();
		NCArmor.registerRenders();
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileTurbineController.class, new RenderTurbineRotor());
		ClientRegistry.bindTileEntitySpecialRenderer(TileQuantumComputerQubit.class, new RenderQuantumComputerQubit());
		
		registerEntityRender(EntityFeralGhoul.class, RenderFeralGhoul.class);
		
		MinecraftForge.EVENT_BUS.register(new BlockHighlightHandler());
	}
	
	private static <E extends Entity, R extends Render<E>> void registerEntityRender(Class<E> entityClass, Class<R> renderClass) {
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
}
