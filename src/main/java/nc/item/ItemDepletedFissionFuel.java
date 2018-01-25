package nc.item;

import nc.enumm.IItemMeta;
import net.minecraft.util.IStringSerializable;

public class ItemDepletedFissionFuel<T extends Enum<T> & IStringSerializable & IItemMeta> extends NCItemMeta {
	
	public ItemDepletedFissionFuel(String nameIn, Class<T> enumm, String[]... tooltips) {
		super(nameIn, enumm, tooltips);
	}
}
