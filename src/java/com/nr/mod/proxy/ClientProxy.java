package com.nr.mod.proxy;

import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;

import com.nr.mod.blocks.NRBlocks;
import com.nr.mod.blocks.tileentities.TileEntityAccStraight1;
import com.nr.mod.blocks.tileentities.TileEntityAccStraight2;
import com.nr.mod.blocks.tileentities.TileEntityFusionReactor;
import com.nr.mod.blocks.tileentities.TileEntityNuclearWorkspace;
import com.nr.mod.entity.EntityNuclearGrenade;
import com.nr.mod.entity.EntityNuclearMonster;
import com.nr.mod.entity.EntityNukePrimed;
import com.nr.mod.entity.EntityPaul;
import com.nr.mod.items.NRItems;
import com.nr.mod.model.ModelNuclearMonster;
import com.nr.mod.model.ModelPaul;
import com.nr.mod.renderer.ItemRenderAccStraight1;
import com.nr.mod.renderer.ItemRenderAccStraight2;
import com.nr.mod.renderer.ItemRenderFusionReactor;
import com.nr.mod.renderer.ItemRenderNuclearWorkspace;
import com.nr.mod.renderer.RenderAccStraight1;
import com.nr.mod.renderer.RenderAccStraight2;
import com.nr.mod.renderer.RenderFusionReactor;
import com.nr.mod.renderer.RenderNuclearMonster;
import com.nr.mod.renderer.RenderNuclearWorkspace;
import com.nr.mod.renderer.RenderNukePrimed;
import com.nr.mod.renderer.RenderPaul;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
	
public void registerRenderThings() {
		//Nuclear Workspace
		TileEntitySpecialRenderer render = new RenderNuclearWorkspace();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityNuclearWorkspace.class, render);
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(NRBlocks.nuclearWorkspace), new ItemRenderNuclearWorkspace(render, new TileEntityNuclearWorkspace()));
		
		//Fusion Reactor
		TileEntitySpecialRenderer renderfusion = new RenderFusionReactor();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFusionReactor.class, renderfusion);
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(NRBlocks.fusionReactor), new ItemRenderFusionReactor(renderfusion, new TileEntityFusionReactor()));

		//Acc Straight 1
		TileEntitySpecialRenderer render1 = new RenderAccStraight1();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAccStraight1.class, render1);
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(NRBlocks.accStraight1), new ItemRenderAccStraight1(render1, new TileEntityAccStraight1()));
				
		//Acc Straight 2
		TileEntitySpecialRenderer render2 = new RenderAccStraight2();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAccStraight2.class, render2);
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(NRBlocks.accStraight2), new ItemRenderAccStraight2(render2, new TileEntityAccStraight2()));
		
		//Nuke Primed
		RenderingRegistry.registerEntityRenderingHandler(EntityNukePrimed.class, new RenderNukePrimed());
		
		//Entities
		RenderingRegistry.registerEntityRenderingHandler(EntityNuclearMonster.class, new RenderNuclearMonster(new ModelNuclearMonster(), 0.3F));
		RenderingRegistry.registerEntityRenderingHandler(EntityPaul.class, new RenderPaul(new ModelPaul(), 1.0F));
		
		//Nuclear Grenade
		RenderingRegistry.registerEntityRenderingHandler(EntityNuclearGrenade.class, new RenderSnowball(NRItems.nuclearGrenadeThrown));
	}

	public void registerSounds() {}

	public void registerTileEntitySpecialRenderer() {}
}
