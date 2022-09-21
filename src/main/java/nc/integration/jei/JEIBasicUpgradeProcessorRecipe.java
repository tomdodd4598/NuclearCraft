package nc.integration.jei;

import mezz.jei.api.IGuiHelper;
import nc.recipe.BasicRecipe;
import nc.tile.processor.*;

public class JEIBasicUpgradeProcessorRecipe<TILE extends TileBasicUpgradeProcessor<TILE>> extends JEIProcessorRecipe<TILE, BasicUpgradeProcessorContainerInfo<TILE>, JEIBasicUpgradeProcessorRecipe<TILE>> {
	
	public JEIBasicUpgradeProcessorRecipe(IGuiHelper guiHelper, BasicUpgradeProcessorContainerInfo<TILE> info, BasicRecipe recipe) {
		super(guiHelper, info, recipe, info.modId + ":textures/gui/container/" + info.name + ".png", info.progressBarGuiX, info.progressBarGuiY, info.progressBarGuiWidth, info.progressBarGuiHeight, info.progressBarGuiU, info.progressBarGuiV);
	}
}
