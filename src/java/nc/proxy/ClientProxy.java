package nc.proxy;

import nc.block.NCBlocks;
import nc.entity.EntityBullet;
import nc.entity.EntityNuclearGrenade;
import nc.entity.EntityNuclearMonster;
import nc.entity.EntityNukePrimed;
import nc.entity.EntityPaul;
import nc.item.NCItems;
import nc.model.ModelNuclearMonster;
import nc.model.ModelPaul;
import nc.render.ItemRenderFusionReactor;
import nc.render.ItemRenderNuclearWorkspace;
import nc.render.ItemRenderTubing1;
import nc.render.ItemRenderTubing2;
import nc.render.RenderBullet;
import nc.render.RenderFusionReactor;
import nc.render.RenderNuclearMonster;
import nc.render.RenderNuclearWorkspace;
import nc.render.RenderNukePrimed;
import nc.render.RenderPaul;
import nc.render.RenderTubing1;
import nc.render.RenderTubing2;
import nc.tile.crafting.TileNuclearWorkspace;
import nc.tile.generator.TileFusionReactor;
import nc.tile.other.TileTubing1;
import nc.tile.other.TileTubing2;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
	
public void registerRenderThings() {
		//Nuclear Workspace
		TileEntitySpecialRenderer render = new RenderNuclearWorkspace();
		ClientRegistry.bindTileEntitySpecialRenderer(TileNuclearWorkspace.class, render);
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(NCBlocks.nuclearWorkspace), new ItemRenderNuclearWorkspace(render, new TileNuclearWorkspace()));
		
		//Fusion Reactor
		TileEntitySpecialRenderer renderfusion = new RenderFusionReactor();
		ClientRegistry.bindTileEntitySpecialRenderer(TileFusionReactor.class, renderfusion);
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(NCBlocks.fusionReactor), new ItemRenderFusionReactor(renderfusion, new TileFusionReactor()));

		//Acc Straight 1
		TileEntitySpecialRenderer render1 = new RenderTubing1();
		ClientRegistry.bindTileEntitySpecialRenderer(TileTubing1.class, render1);
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(NCBlocks.tubing1), new ItemRenderTubing1(render1, new TileTubing1()));
				
		//Acc Straight 2
		TileEntitySpecialRenderer render2 = new RenderTubing2();
		ClientRegistry.bindTileEntitySpecialRenderer(TileTubing2.class, render2);
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(NCBlocks.tubing2), new ItemRenderTubing2(render2, new TileTubing2()));
		
		//Nuke Primed
		RenderingRegistry.registerEntityRenderingHandler(EntityNukePrimed.class, new RenderNukePrimed());
		
		//Entities
		RenderingRegistry.registerEntityRenderingHandler(EntityNuclearMonster.class, new RenderNuclearMonster(new ModelNuclearMonster(), 0.3F));
		RenderingRegistry.registerEntityRenderingHandler(EntityPaul.class, new RenderPaul(new ModelPaul(), 1.0F));
		
		//Nuclear Grenade
		RenderingRegistry.registerEntityRenderingHandler(EntityNuclearGrenade.class, new RenderSnowball(NCItems.nuclearGrenadeThrown));
		
		//DU Bullet
		RenderingRegistry.registerEntityRenderingHandler(EntityBullet.class, new RenderBullet());
	}

	public void registerSounds() {}

	public void registerTileEntitySpecialRenderer() {}
}
