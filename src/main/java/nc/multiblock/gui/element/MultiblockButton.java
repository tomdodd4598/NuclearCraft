package nc.multiblock.gui.element;

import nc.gui.element.NCButton;
import nc.util.NCUtil;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.*;

public class MultiblockButton {
	
	@SideOnly(Side.CLIENT)
	public static class ClearAllMaterial extends NCButton.Image {
		
		public ClearAllMaterial(int id, int x, int y) {
			super(id, x, y, BUTTONS_OFF, BUTTONS_ON, 216, 0, 18, 18);
		}
		
		@Override
		public void drawButton(Minecraft minecraft, int drawX, int drawY, float partialTicks) {
			visible = NCUtil.isModifierKeyDown();
			super.drawButton(minecraft, drawX, drawY, partialTicks);
		}
		
		@Override
		public boolean mousePressed(Minecraft minecraft, int mouseX, int mouseY) {
			isButtonPressed = NCUtil.isModifierKeyDown() && enabled && visible && mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
			return isButtonPressed;
		}
	}
}
