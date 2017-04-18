package nc.block.item;

import nc.config.NCConfig;
import nc.handler.EnumHandler.CoolerTypes;
import net.minecraft.block.Block;
import net.minecraft.util.text.translation.I18n;

public class ItemBlockCooler extends ItemBlockMeta {
	
	public ItemBlockCooler(Block block) {
		super(block, INFO());
	}
	
	public final static String[][] INFO() {
		String[][] info = new String[CoolerTypes.values().length][];
		info[0] = new String[] {};
		for (int i = 1; i < CoolerTypes.values().length; i++) {
			info[i] = new String[] {I18n.translateToLocalFormatted("tile.cooler.cooling_rate") + " " + NCConfig.fission_cooling_rate[i - 1] + " H/t", I18n.translateToLocalFormatted("tile.cooler." + CoolerTypes.values()[i].name().toLowerCase() + ".des0"), I18n.translateToLocalFormatted("tile.cooler." + CoolerTypes.values()[i].name().toLowerCase() + ".des1")};
		}
		
		return info;
	}
}
