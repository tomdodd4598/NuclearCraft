package nc.util;

import net.minecraft.client.renderer.BufferBuilder;

public class RenderHelper {
	
	public static void renderBlockOutline(BufferBuilder buffer, float x, float y, float z, float r, float g, float b, float a) {
		buffer.pos(x, y, z).color(r, g, b, a).endVertex();
		buffer.pos(x+1, y, z).color(r, g, b, a).endVertex();
		
		buffer.pos(x, y, z).color(r, g, b, a).endVertex();
		buffer.pos(x, y+1, z).color(r, g, b, a).endVertex();
		
		buffer.pos(x, y, z).color(r, g, b, a).endVertex();
		buffer.pos(x, y, z+1).color(r, g, b, a).endVertex();
		
		buffer.pos(x+1, y+1, z+1).color(r, g, b, a).endVertex();
		buffer.pos(x, y+1, z+1).color(r, g, b, a).endVertex();
		
		buffer.pos(x+1, y+1, z+1).color(r, g, b, a).endVertex();
		buffer.pos(x+1, y, z+1).color(r, g, b, a).endVertex();
		
		buffer.pos(x+1, y+1, z+1).color(r, g, b, a).endVertex();
		buffer.pos(x+1, y+1, z).color(r, g, b, a).endVertex();

		buffer.pos(x, y+1, z).color(r, g, b, a).endVertex();
		buffer.pos(x, y+1, z+1).color(r, g, b, a).endVertex();
		
		buffer.pos(x, y+1, z).color(r, g, b, a).endVertex();
		buffer.pos(x+1, y+1, z).color(r, g, b, a).endVertex();

		buffer.pos(x+1, y, z).color(r, g, b, a).endVertex();
		buffer.pos(x+1, y, z+1).color(r, g, b, a).endVertex();
		
		buffer.pos(x+1, y, z).color(r, g, b, a).endVertex();
		buffer.pos(x+1, y+1, z).color(r, g, b, a).endVertex();

		buffer.pos(x, y, z+1).color(r, g, b, a).endVertex();
		buffer.pos(x+1, y, z+1).color(r, g, b, a).endVertex();
		
		buffer.pos(x, y, z+1).color(r, g, b, a).endVertex();
		buffer.pos(x, y+1, z+1).color(r, g, b, a).endVertex();
	}
	
	public static void renderBlockFaces(BufferBuilder buffer, float x, float y, float z, float r, float g, float b, float a) {
		buffer.pos(x, y, z).color(r, g, b, a).endVertex();
		buffer.pos(x+1, y, z).color(r, g, b, a).endVertex();
		buffer.pos(x+1, y, z+1).color(r, g, b, a).endVertex();
		buffer.pos(x, y, z+1).color(r, g, b, a).endVertex();
		
		buffer.pos(x, y+1, z).color(r, g, b, a).endVertex();
		buffer.pos(x, y+1, z+1).color(r, g, b, a).endVertex();
		buffer.pos(x+1, y+1, z+1).color(r, g, b, a).endVertex();
		buffer.pos(x+1, y+1, z).color(r, g, b, a).endVertex();
		
		buffer.pos(x, y, z).color(r, g, b, a).endVertex();
		buffer.pos(x, y+1, z).color(r, g, b, a).endVertex();
		buffer.pos(x+1, y+1, z).color(r, g, b, a).endVertex();
		buffer.pos(x+1, y, z).color(r, g, b, a).endVertex();
		
		buffer.pos(x, y, z+1).color(r, g, b, a).endVertex();
		buffer.pos(x+1, y, z+1).color(r, g, b, a).endVertex();
		buffer.pos(x+1, y+1, z+1).color(r, g, b, a).endVertex();
		buffer.pos(x, y+1, z+1).color(r, g, b, a).endVertex();
		
		buffer.pos(x+1, y, z).color(r, g, b, a).endVertex();
		buffer.pos(x+1, y+1, z).color(r, g, b, a).endVertex();
		buffer.pos(x+1, y+1, z+1).color(r, g, b, a).endVertex();
		buffer.pos(x+1, y, z+1).color(r, g, b, a).endVertex();
		
		buffer.pos(x, y, z).color(r, g, b, a).endVertex();
		buffer.pos(x, y, z+1).color(r, g, b, a).endVertex();
		buffer.pos(x, y+1, z+1).color(r, g, b, a).endVertex();
		buffer.pos(x, y+1, z).color(r, g, b, a).endVertex();
	}
}
