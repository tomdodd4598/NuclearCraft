package nc.integ.jei;

import java.awt.Rectangle;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import mezz.jei.api.gui.IAdvancedGuiHandler;
import nc.gui.GuiContainerBaseNC;

public class AdvancedGuiHandlerNC implements IAdvancedGuiHandler<GuiContainerBaseNC> {

	public AdvancedGuiHandlerNC() {}

	@Nonnull
	public Class<GuiContainerBaseNC> getGuiContainerClass() {
		return GuiContainerBaseNC.class;
	}

	@Nullable
	public List<Rectangle> getGuiExtraAreas(GuiContainerBaseNC guiContainer) {
		return guiContainer.getBlockingAreas();
	}

	@Nullable
	public Object getIngredientUnderMouse(GuiContainerBaseNC guiContainer, int mouseX, int mouseY) {
		return guiContainer.getIngredientUnderMouse(mouseX - guiContainer.getGuiLeft(), mouseY - guiContainer.getGuiTop());
	}
}
