package nc.blocks.items;

import nc.config.NCConfig;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;

public class ItemBlockCooler extends ItemBlockMeta {
	
	public final static String[][] INFO =
		{
			{
			},
			{
				I18n.format("tile.cooler.cooling_rate") + " " + NCConfig.fission_cooling_rate[0] + " H/t"
			},
			{
				I18n.format("tile.cooler.cooling_rate") + " " + NCConfig.fission_cooling_rate[1] + " H/t"
			},
			{
				I18n.format("tile.cooler.cooling_rate") + " " + NCConfig.fission_cooling_rate[2] + " H/t"
			},
			{
				I18n.format("tile.cooler.cooling_rate") + " " + NCConfig.fission_cooling_rate[3] + " H/t"
			},
			{
				I18n.format("tile.cooler.cooling_rate") + " " + NCConfig.fission_cooling_rate[4] + " H/t"
			},
			{
				I18n.format("tile.cooler.cooling_rate") + " " + NCConfig.fission_cooling_rate[5] + " H/t"
			},
			{
				I18n.format("tile.cooler.cooling_rate") + " " + NCConfig.fission_cooling_rate[6] + " H/t"
			}
		};
	
	public ItemBlockCooler(Block block) {
		super(block, INFO);
	}
}
