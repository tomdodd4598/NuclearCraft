package nc.tab;

import nc.init.NCTools;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class TabMisc extends CreativeTabs {

	public TabMisc() {
		super("nuclearcraftMisc");
	}

	public Item getTabIconItem() {
		return NCTools.spaxelhoe_tough;
	}
}
