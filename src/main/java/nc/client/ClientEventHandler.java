package nc.client;

import nc.config.NCConfig;
import nc.init.NCBlocks;
import nc.tile.radiation.TileGeigerCounter;
import nc.tile.radiation.TileRadiationScrubber;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import org.lwjgl.opengl.GL11;

public class ClientEventHandler {

    private ItemStack GEIGER_BLOCK_ITEM;
    private ItemStack SCRUBBER_BLOCK_ITEM;

    public ClientEventHandler() {
        GEIGER_BLOCK_ITEM = new ItemStack(NCBlocks.geiger_block, 1);
        SCRUBBER_BLOCK_ITEM = new ItemStack(NCBlocks.radiation_scrubber, 1);
    }

    @SubscribeEvent
    public void onRenderWorldLastEvent(RenderWorldLastEvent event) {
        //Overlay renderer for the geiger counter and radiation scrubber blocks
        boolean chunkBorders = false;
        Minecraft mc = Minecraft.getMinecraft();

        // Bail fast if rendering is disabled
        if (!NCConfig.render_chunk_boundaries) {
            return;
        }

        // Draw the chunk borders if we're either holding a geiger block OR looking at one
        for(EnumHand hand : EnumHand.values()) {
            ItemStack heldItem = Minecraft.getMinecraft().player.getHeldItem(hand);

            if (OreDictionary.itemMatches(GEIGER_BLOCK_ITEM, heldItem, true) ||
                OreDictionary.itemMatches(SCRUBBER_BLOCK_ITEM, heldItem, true)) {
                chunkBorders = true;
                break;
            }
        }

        TileEntity te = mc.world.getTileEntity(mc.objectMouseOver.getBlockPos());
        if (!chunkBorders && mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK &&
                (te instanceof TileGeigerCounter || (te instanceof TileRadiationScrubber))) {
            chunkBorders = true;
        }

        //
        // Logic/rendering below taken with minor changes from BluSunrize's Immersive Engineering mod
        // github.com/BluSunrize/ImmersiveEngineering/../blusunrize/immersiveengineering/client/ClientEventHandler.java
        //
        if(chunkBorders)
        {
            EntityPlayer player = mc.player;
            double px = TileEntityRendererDispatcher.staticPlayerX;
            double py = TileEntityRendererDispatcher.staticPlayerY;
            double pz = TileEntityRendererDispatcher.staticPlayerZ;
            int chunkX = (int)player.posX >> 4<<4;
            int chunkZ = (int)player.posZ >> 4<<4;
            int y = Math.min((int)player.posY-2, player.getEntityWorld().getChunkFromChunkCoords(chunkX, chunkZ).getLowestHeight());
            float h = (float)Math.max(32, player.posY-y+4);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder BufferBuilder = tessellator.getBuffer();

            GlStateManager.disableTexture2D();
            GlStateManager.enableBlend();
            GlStateManager.disableCull();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
            float r = 255;
            float g = 0;
            float b = 0;
            BufferBuilder.setTranslation(chunkX-px, y+2-py, chunkZ-pz);
            GlStateManager.glLineWidth(5f);
            BufferBuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
            BufferBuilder.pos(0, 0, 0).color(r, g, b, .375f).endVertex();
            BufferBuilder.pos(0, h, 0).color(r, g, b, .375f).endVertex();
            BufferBuilder.pos(16, 0, 0).color(r, g, b, .375f).endVertex();
            BufferBuilder.pos(16, h, 0).color(r, g, b, .375f).endVertex();
            BufferBuilder.pos(16, 0, 16).color(r, g, b, .375f).endVertex();
            BufferBuilder.pos(16, h, 16).color(r, g, b, .375f).endVertex();
            BufferBuilder.pos(0, 0, 16).color(r, g, b, .375f).endVertex();
            BufferBuilder.pos(0, h, 16).color(r, g, b, .375f).endVertex();

            BufferBuilder.pos(0, 2, 0).color(r, g, b, .375f).endVertex();
            BufferBuilder.pos(16, 2, 0).color(r, g, b, .375f).endVertex();
            BufferBuilder.pos(0, 2, 0).color(r, g, b, .375f).endVertex();
            BufferBuilder.pos(0, 2, 16).color(r, g, b, .375f).endVertex();
            BufferBuilder.pos(0, 2, 16).color(r, g, b, .375f).endVertex();
            BufferBuilder.pos(16, 2, 16).color(r, g, b, .375f).endVertex();
            BufferBuilder.pos(16, 2, 0).color(r, g, b, .375f).endVertex();
            BufferBuilder.pos(16, 2, 16).color(r, g, b, .375f).endVertex();
            tessellator.draw();
            BufferBuilder.setTranslation(0, 0, 0);
            GlStateManager.shadeModel(GL11.GL_FLAT);
            GlStateManager.enableCull();
            GlStateManager.disableBlend();
            GlStateManager.enableTexture2D();
        }
    }
}
