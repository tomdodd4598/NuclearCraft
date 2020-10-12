package nc.integration.jei;

import java.util.*;

import mezz.jei.api.IGuiHelper;
import nc.integration.jei.NCJEI.IJEIHandler;
import nc.radiation.RadiationHelper;
import nc.recipe.*;
import nc.util.*;
import net.minecraft.util.text.TextFormatting;

public abstract class JEIMachineRecipeWrapper extends JEIBasicRecipeWrapper {
	
	private final int infoX, infoY, infoWidth, infoHeight;
	
	public JEIMachineRecipeWrapper(IGuiHelper guiHelper, IJEIHandler handler, BasicRecipeHandler recipeHandler, BasicRecipe recipe, int backX, int backY, int arrowX, int arrowY, int arrowWidth, int arrowHeight, int arrowPosX, int arrowPosY, int infoX, int infoY, int infoWidth, int infoHeight) {
		super(guiHelper, handler, recipeHandler, recipe, backX, backY, arrowX, arrowY, arrowWidth, arrowHeight, arrowPosX, arrowPosY);
		this.infoX = infoX - backX;
		this.infoY = infoY - backY;
		this.infoWidth = infoWidth;
		this.infoHeight = infoHeight;
	}
	
	@Override
	protected int getProgressArrowTime() {
		return (int) (getBaseProcessTime() / 4D);
	}
	
	protected abstract double getBaseProcessTime();
	
	protected abstract double getBaseProcessPower();
	
	protected double getBaseProcessRadiation() {
		if (recipe == null) {
			return 0D;
		}
		return recipe.getBaseProcessRadiation();
	}
	
	@Override
	public List<String> getTooltipStrings(int mouseX, int mouseY) {
		List<String> tooltip = new ArrayList<>();
		
		if (mouseX >= infoX && mouseY >= infoY && mouseX < infoX + infoWidth + 1 && mouseY < infoY + infoHeight + 1) {
			tooltip.add(TextFormatting.GREEN + BASE_TIME + " " + TextFormatting.WHITE + UnitHelper.applyTimeUnitShort(getBaseProcessTime(), 3));
			tooltip.add(TextFormatting.LIGHT_PURPLE + BASE_POWER + " " + TextFormatting.WHITE + UnitHelper.prefix(getBaseProcessPower(), 5, "RF/t"));
			double radiation = getBaseProcessRadiation();
			if (radiation > 0D) {
				tooltip.add(TextFormatting.GOLD + BASE_RADIATION + " " + RadiationHelper.radsColoredPrefix(radiation, true));
			}
		}
		
		return tooltip;
	}
	
	private static final String BASE_TIME = Lang.localise("jei.nuclearcraft.base_process_time");
	private static final String BASE_POWER = Lang.localise("jei.nuclearcraft.base_process_power");
	private static final String BASE_RADIATION = Lang.localise("jei.nuclearcraft.base_process_radiation");
}
