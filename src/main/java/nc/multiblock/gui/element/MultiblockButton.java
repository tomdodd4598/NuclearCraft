package nc.multiblock.gui.element;

import nc.gui.element.NCGuiButton.ImageButton;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MultiblockButton {
	
	@SideOnly(Side.CLIENT)
	public static class ButtonClearAllFluids extends ImageButton {
		
		public ButtonClearAllFluids(int id, int x, int y) {
			super(id, x, y, 0, 18, 9, 9);
		}
		
		@Override
		public void drawButton(Minecraft minecraft, int x, int y, float partialTicks) {
			visible = isShiftKeyDown();
			super.drawButton(minecraft, x, y, partialTicks);
		}
		
		@Override
		public boolean mousePressed(Minecraft minecraft, int mouseX, int mouseY) {
			isButtonPressed = isShiftKeyDown() && enabled && visible && mouseX >= this.x && mouseY >= this.y && mouseX < this.x + width && mouseY < this.y + height;
			return isButtonPressed;
		}
	}
}
