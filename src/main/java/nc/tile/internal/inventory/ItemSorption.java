package nc.tile.internal.inventory;

import nc.gui.IGuiButton;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.TextFormatting;

public enum ItemSorption implements IStringSerializable, IGuiButton {
	
	IN,
	OUT,
	BOTH,
	NON;
	
	public boolean canReceive() {
		return this == IN || this == BOTH;
	}
	
	public boolean canExtract() {
		return this == OUT || this == BOTH;
	}
	
	public boolean canConnect() {
		return this != NON;
	}
	
	public ItemSorption next(Type type, boolean reverse) {
		if (reverse) {
			switch (type) {
				case INPUT:
					switch (this) {
						case IN:
							return NON;
						case NON:
							return OUT;
						case OUT:
							return IN;
						default:
							return IN;
					}
				case OUTPUT:
					switch (this) {
						case OUT:
							return NON;
						case NON:
							return OUT;
						default:
							return OUT;
					}
				default:
					switch (this) {
						case IN:
							return NON;
						case NON:
							return BOTH;
						case BOTH:
							return OUT;
						case OUT:
							return IN;
						default:
							return NON;
					}
			}
		}
		else {
			switch (type) {
				case INPUT:
					switch (this) {
						case IN:
							return OUT;
						case OUT:
							return NON;
						case NON:
							return IN;
						default:
							return IN;
					}
				case OUTPUT:
					switch (this) {
						case OUT:
							return NON;
						case NON:
							return OUT;
						default:
							return OUT;
					}
				default:
					switch (this) {
						case IN:
							return OUT;
						case OUT:
							return BOTH;
						case BOTH:
							return NON;
						case NON:
							return IN;
						default:
							return NON;
					}
			}
		}
	}
	
	@Override
	public String getName() {
		switch (this) {
			case IN:
				return "in";
			case OUT:
				return "out";
			case BOTH:
				return "both";
			case NON:
				return "non";
			default:
				return "non";
		}
	}
	
	public TextFormatting getTextColor() {
		switch (this) {
			case IN:
				return TextFormatting.BLUE;
			case OUT:
				return TextFormatting.GOLD;
			case BOTH:
				return TextFormatting.BOLD;
			case NON:
				return TextFormatting.GRAY;
			default:
				return TextFormatting.GRAY;
		}
	}
	
	@Override
	public int getTextureX() {
		switch (this) {
			case IN:
				return 108;
			case OUT:
				return 126;
			case NON:
				return 144;
			default:
				return 144;
		}
	}
	
	@Override
	public int getTextureY() {
		return 0;
	}
	
	@Override
	public int getTextureWidth() {
		return 18;
	}
	
	@Override
	public int getTextureHeight() {
		return 18;
	}
	
	public static enum Type {
		DEFAULT,
		INPUT,
		OUTPUT;
	}
}
