package nc.handler;

import nc.entity.EntityFeralGhoul;
import nc.init.NCArmor;
import nc.init.NCBlocks;
import nc.init.NCItems;
import nc.init.NCTools;
import nc.multiblock.turbine.tile.TileTurbineController;
import nc.render.BlockHighlightHandler;
import nc.render.entity.RenderFeralGhoul;
import nc.render.tile.RenderFusionCore;
import nc.render.tile.RenderTurbineRotor;
import nc.tile.generator.TileFusionCore;
import nc.util.NCUtil;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class RenderHandler {
	
	public static void init() {
		NCBlocks.registerRenders();
		NCItems.registerRenders();
		NCTools.registerRenders();
		NCArmor.registerRenders();
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileFusionCore.class, new RenderFusionCore());
		ClientRegistry.bindTileEntitySpecialRenderer(TileTurbineController.class, new RenderTurbineRotor());
		//ClientRegistry.bindTileEntitySpecialRenderer(TileSpin.class, new RenderSpin());
		
		registerEntityRender(EntityFeralGhoul.class, RenderFeralGhoul.class);
		
		MinecraftForge.EVENT_BUS.register(new BlockHighlightHandler());
	}
	
	private static <E extends Entity, R extends Render<E>> void registerEntityRender(Class<E> entityClass, Class<R> renderClass) {
		RenderingRegistry.registerEntityRenderingHandler(entityClass, new IRenderFactory<E>() {
			@Override
			public R createRenderFor(RenderManager manager) {
				R render = null;
				try {
					render = NCUtil.newInstance(renderClass, manager);
				} catch (Exception e) {
					NCUtil.getLogger().catching(e);
				}
				return render;
			}
		});
	}
}
