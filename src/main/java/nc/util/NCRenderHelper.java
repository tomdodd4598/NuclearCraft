package nc.util;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;

public class NCRenderHelper {
	
	public static final float PIXEL = 0.0625F;
	
	public static void renderBlockFrame(BlockPos pos, float r, float g, float b, float a) {
		renderFrame(pos.getX(), pos.getY(), pos.getZ(), 1F, 1F, 1F, r, g, b, a);
	}
	
	public static void renderCubeFrame(BlockPos pos, float size, float r, float g, float b, float a) {
		renderFrame(pos.getX() + (8F - size/2F)*PIXEL, pos.getY() + (8F - size/2F)*PIXEL, pos.getZ() + (8F - size/2F)*PIXEL, size*PIXEL, size*PIXEL, size*PIXEL, r, g, b, a);
	}
	
	public static void renderFrame(float oX, float oY, float oZ, float lX, float lY, float lZ, float r, float g, float b, float a) {
		BufferBuilder buffer = Tessellator.getInstance().getBuffer();
		buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
		
		drawLine(buffer, oX, oY, oZ, lX, 0F, 0F, r, g, b, a);
		drawLine(buffer, oX, oY, oZ, 0F, lY, 0F, r, g, b, a);
		drawLine(buffer, oX, oY, oZ, 0F, 0F, lZ, r, g, b, a);
		drawLine(buffer, oX+lX, oY, oZ, 0F, lY, 0F, r, g, b, a);
		drawLine(buffer, oX+lX, oY, oZ, 0F, 0F, lZ, r, g, b, a);
		drawLine(buffer, oX, oY+lY, oZ, lX, 0F, 0F, r, g, b, a);
		drawLine(buffer, oX, oY+lY, oZ, 0F, 0F, lZ, r, g, b, a);
		drawLine(buffer, oX, oY, oZ+lZ, lX, 0F, 0F, r, g, b, a);
		drawLine(buffer, oX, oY, oZ+lZ, 0F, lY, 0F, r, g, b, a);
		drawLine(buffer, oX+lX, oY+lY, oZ, 0F, 0F, lZ, r, g, b, a);
		drawLine(buffer, oX+lX, oY, oZ+lZ, 0F, lY, 0F, r, g, b, a);
		drawLine(buffer, oX, oY+lY, oZ+lZ, lX, 0F, 0F, r, g, b, a);
	}
	
	private static void drawLine(BufferBuilder buffer, float oX, float oY, float oZ, float lX, float lY, float lZ, float r, float g, float b, float a) {
		buffer.pos(oX, oY, oZ).color(r, g, b, a).endVertex();
		buffer.pos(oX+lX, oY+lY, oZ+lZ).color(r, g, b, a).endVertex();
	}
	
	public static void renderBlockFaces(BlockPos pos, float r, float g, float b, float a) {
		renderFaces(pos.getX(), pos.getY(), pos.getZ(), 1F, 1F, 1F, r, g, b, a);
	}
	
	public static void renderBlockFaces(BlockPos pos, float size, float r, float g, float b, float a) {
		renderFaces(pos.getX() + (8F - size/2F)*PIXEL, pos.getY() + (8F - size/2F)*PIXEL, pos.getZ() + (8F - size/2F)*PIXEL, size*PIXEL, size*PIXEL, size*PIXEL, r, g, b, a);
	}
	
	public static void renderFaces(float oX, float oY, float oZ, float lX, float lY, float lZ, float r, float g, float b, float a) {
		BufferBuilder buffer = Tessellator.getInstance().getBuffer();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
		
		drawRect(buffer, oX, oY, oZ, 0F, lY, 0F, lX, 0F, 0F, r, g, b, a);
		drawRect(buffer, oX, oY, oZ, 0F, 0F, lZ, 0F, lY, 0F, r, g, b, a);
		drawRect(buffer, oX, oY, oZ, lX, 0F, 0F, 0F, 0F, lZ, r, g, b, a);
		drawRect(buffer, oX+lX, oY+lY, oZ+lZ, -lX, 0F, 0F, 0F, -lY, 0F, r, g, b, a);
		drawRect(buffer, oX+lX, oY+lY, oZ+lZ, 0F, 0F, -lZ, -lX, 0F, 0F, r, g, b, a);
		drawRect(buffer, oX+lX, oY+lY, oZ+lZ, 0F, -lY, 0F, 0F, 0F, -lZ, r, g, b, a);
	}
	
	private static void drawRect(BufferBuilder buffer, float oX, float oY, float oZ, float x1, float y1, float z1, float x3, float y3, float z3, float r, float g, float b, float a) {
		buffer.pos(oX, oY, oZ).color(r, g, b, a).endVertex();
		buffer.pos(oX+x1, oY+y1, oZ+z1).color(r, g, b, a).endVertex();
		buffer.pos(oX+x1+x3, oY+y1+y3, oZ+z1+z3).color(r, g, b, a).endVertex();
		buffer.pos(oX+x3, oY+y3, oZ+z3).color(r, g, b, a).endVertex();
	}
}
