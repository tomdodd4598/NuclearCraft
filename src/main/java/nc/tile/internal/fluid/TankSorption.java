package nc.tile.internal.fluid;

import nc.gui.IButtonEnum;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.TextFormatting;

public enum TankSorption implements IStringSerializable, IButtonEnum {
	IN, OUT, BOTH, NON, PUSH;
	
	public boolean canFill() {
		return this == IN || this == BOTH;
	}
	
	public boolean canDrain() {
		return this == OUT || this == PUSH || this == BOTH;
	}
	
	public boolean canConnect() {
		return this != NON;
	}
	
	public TankSorption next(Type type, boolean reverse) {
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
					return PUSH;
				case PUSH:
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
					return PUSH;
				case PUSH:
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
		case PUSH:
			return "push";
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
			return TextFormatting.DARK_AQUA;
		case OUT:
			return TextFormatting.RED;
		case PUSH:
			return TextFormatting.DARK_PURPLE;
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
			return 162;
		case OUT:
		case PUSH:
			return 180;
		case NON:
			return 198;
		default:
			return 198;
		}
	}
	
	@Override
	public int getTextureY() {
		return this == PUSH ? 18 : 0;
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
		DEFAULT, INPUT, OUTPUT;
	}
}
