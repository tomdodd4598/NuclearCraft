package nc.multiblock.gui.element;

import nc.gui.element.NCButton;
import nc.util.NCUtil;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MultiblockButton {
	
	@SideOnly(Side.CLIENT)
	public static class ClearAllMaterial extends NCButton.Image {
		
		public ClearAllMaterial(int id, int x, int y) {
			super(id, x, y, 216, 0, 18, 18);
		}
		
		@Override
		public void drawButton(Minecraft minecraft, int x, int y, float partialTicks) {
			visible = NCUtil.isModifierKeyDown();
			super.drawButton(minecraft, x, y, partialTicks);
		}
		
		@Override
		public boolean mousePressed(Minecraft minecraft, int mouseX, int mouseY) {
			isButtonPressed = NCUtil.isModifierKeyDown() && enabled && visible && mouseX >= this.x && mouseY >= this.y && mouseX < this.x + width && mouseY < this.y + height;
			return isButtonPressed;
		}
	}
}
