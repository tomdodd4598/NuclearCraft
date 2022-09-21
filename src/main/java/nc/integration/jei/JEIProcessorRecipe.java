package nc.integration.jei;

import java.util.*;

import mezz.jei.api.IGuiHelper;
import nc.radiation.RadiationHelper;
import nc.recipe.BasicRecipe;
import nc.tile.processor.*;
import nc.util.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;

public abstract class JEIProcessorRecipe<TILE extends TileEntity & IProcessor<TILE, INFO>, INFO extends ProcessorContainerInfo<TILE, INFO>, WRAPPER extends JEIProcessorRecipe<TILE, INFO, WRAPPER>> extends JEIBasicRecipe<WRAPPER> {
	
	protected final INFO info;
	protected final int tooltipX, tooltipY, tooltipWidth, tooltipHeight;
	
	public JEIProcessorRecipe(IGuiHelper guiHelper, INFO info, BasicRecipe recipe, String textureLocation, int arrowX, int arrowY, int arrowWidth, int arrowHeight, int arrowU, int arrowV) {
		super(guiHelper, info.getRecipeHandler(), recipe, textureLocation, info.jeiBackgroundX, info.jeiBackgroundY, arrowX, arrowY, arrowWidth, arrowHeight, arrowU, arrowV);
		this.info = info;
		this.tooltipX = info.jeiTooltipX - info.jeiBackgroundX;
		this.tooltipY = info.jeiTooltipY - info.jeiBackgroundY;
		this.tooltipWidth = info.jeiTooltipWidth;
		this.tooltipHeight = info.jeiTooltipHeight;
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
	
	@Override
	public List<String> getTooltipStrings(int mouseX, int mouseY) {
		List<String> tooltip = new ArrayList<>();
		
		if (mouseX >= tooltipX && mouseY >= tooltipY && mouseX <= tooltipX + tooltipWidth && mouseY <= tooltipY + tooltipHeight) {
			tooltip.add(TextFormatting.GREEN + BASE_TIME + " " + TextFormatting.WHITE + UnitHelper.applyTimeUnitShort(getBaseProcessTime(), 3));
			tooltip.add(TextFormatting.LIGHT_PURPLE + BASE_POWER + " " + TextFormatting.WHITE + UnitHelper.prefix(getBaseProcessPower(), 5, "RF/t"));
			double radiation = getBaseProcessRadiation();
			if (radiation > 0D) {
				tooltip.add(TextFormatting.GOLD + BASE_RADIATION + " " + RadiationHelper.radsColoredPrefix(radiation, true));
			}
		}
		
		return tooltip;
	}
	
	public static final String BASE_TIME = Lang.localise("jei.nuclearcraft.base_process_time");
	public static final String BASE_POWER = Lang.localise("jei.nuclearcraft.base_process_power");
	public static final String BASE_RADIATION = Lang.localise("jei.nuclearcraft.base_process_radiation");
}
