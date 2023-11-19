package nc.integration.jei.wrapper;

import java.util.*;

import mezz.jei.api.IGuiHelper;
import nc.integration.jei.category.info.JEIProcessorCategoryInfo;
import nc.radiation.RadiationHelper;
import nc.recipe.BasicRecipe;
import nc.tile.processor.IProcessor;
import nc.tile.processor.info.ProcessorContainerInfo;
import nc.util.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;

public abstract class JEIProcessorRecipeWrapper<TILE extends TileEntity & IProcessor<TILE, INFO>, INFO extends ProcessorContainerInfo<TILE, INFO>, WRAPPER extends JEIProcessorRecipeWrapper<TILE, INFO, WRAPPER>> extends JEIRecipeWrapper {
	
	protected final INFO info;
	
	protected final int tooltipX, tooltipY, tooltipW, tooltipH;
	
	protected JEIProcessorRecipeWrapper(IGuiHelper guiHelper, JEIProcessorCategoryInfo<TILE, INFO, WRAPPER> categoryInfo, BasicRecipe recipe) {
		this(guiHelper, categoryInfo.containerInfo, recipe);
	}
	
	private JEIProcessorRecipeWrapper(IGuiHelper guiHelper, INFO info, BasicRecipe recipe) {
		super(guiHelper, info.getRecipeHandler(), recipe, info.jeiTexture, info.jeiBackgroundX, info.jeiBackgroundY, info.progressBarGuiX, info.progressBarGuiY, info.progressBarGuiW, info.progressBarGuiH, info.progressBarGuiU, info.progressBarGuiV);
		this.info = info;
		
		this.tooltipX = info.jeiTooltipX - info.jeiBackgroundX;
		this.tooltipY = info.jeiTooltipY - info.jeiBackgroundY;
		this.tooltipW = info.jeiTooltipW;
		this.tooltipH = info.jeiTooltipH;
	}
	
	@Override
	protected int getProgressArrowTime() {
		return (int) (getBaseProcessTime() / 4D);
	}
	
	protected double getBaseProcessTime() {
		if (recipe == null) {
			return info.defaultProcessTime;
		}
		return recipe.getBaseProcessTime(info.defaultProcessTime);
	}
	
	protected double getBaseProcessPower() {
		if (recipe == null) {
			return info.defaultProcessPower;
		}
		return recipe.getBaseProcessPower(info.defaultProcessPower);
	}
	
	protected double getBaseProcessRadiation() {
		if (recipe == null) {
			return 0D;
		}
		return recipe.getBaseProcessRadiation();
	}
	
	protected boolean showTooltip(int mouseX, int mouseY) {
		return mouseX >= tooltipX && mouseY >= tooltipY && mouseX <= tooltipX + tooltipW && mouseY <= tooltipY + tooltipH;
	}
	
	@Override
	public List<String> getTooltipStrings(int mouseX, int mouseY) {
		List<String> tooltip = new ArrayList<>();
		
		if (showTooltip(mouseX, mouseY)) {
			tooltip.add(TextFormatting.GREEN + BASE_TIME + " " + TextFormatting.WHITE + UnitHelper.applyTimeUnitShort(getBaseProcessTime(), 3));
			tooltip.add(TextFormatting.LIGHT_PURPLE + BASE_POWER + " " + TextFormatting.WHITE + UnitHelper.prefix(getBaseProcessPower(), 5, "RF/t"));
			double radiation = getBaseProcessRadiation();
			if (radiation > 0D) {
				tooltip.add(TextFormatting.GOLD + BASE_RADIATION + " " + RadiationHelper.radsColoredPrefix(radiation, true));
			}
		}
		
		return tooltip;
	}
	
	public static final String BASE_TIME = Lang.localize("jei.nuclearcraft.base_process_time");
	public static final String BASE_POWER = Lang.localize("jei.nuclearcraft.base_process_power");
	public static final String BASE_RADIATION = Lang.localize("jei.nuclearcraft.base_process_radiation");
}
