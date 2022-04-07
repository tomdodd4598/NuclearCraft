package nc.tile.internal.fluid;

import nc.gui.IGuiButton;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.TextFormatting;

public enum TankOutputSetting implements IStringSerializable, IGuiButton {
	
	DEFAULT,
	VOID_EXCESS,
	VOID;
	
	public TankOutputSetting next(boolean reverse) {
		if (reverse) {
			switch (this) {
				case DEFAULT:
					return VOID;
				case VOID_EXCESS:
					return DEFAULT;
				case VOID:
					return VOID_EXCESS;
				default:
					return DEFAULT;
			}
		}
		else {
			switch (this) {
				case DEFAULT:
					return VOID_EXCESS;
				case VOID_EXCESS:
					return VOID;
				case VOID:
					return DEFAULT;
				default:
					return DEFAULT;
			}
		}
	}
	
	@Override
	public String getName() {
		switch (this) {
			case DEFAULT:
				return "default";
			case VOID_EXCESS:
				return "void_excess";
			case VOID:
				return "void";
			default:
				return "default";
		}
	}
	
	public TextFormatting getTextColor() {
		switch (this) {
			case DEFAULT:
				return TextFormatting.WHITE;
			case VOID_EXCESS:
				return TextFormatting.LIGHT_PURPLE;
			case VOID:
				return TextFormatting.DARK_PURPLE;
			default:
				return TextFormatting.WHITE;
		}
	}
	
	@Override
	public int getTextureX() {
		switch (this) {
			case DEFAULT:
				return 54;
			case VOID_EXCESS:
				return 72;
			case VOID:
				return 90;
			default:
				return 54;
		}
	}
	
	@Override
	public int getTextureY() {
		return 18;
	}
	
	@Override
	public int getTextureWidth() {
		return 18;
	}
	
	@Override
	public int getTextureHeight() {
		return 18;
	}
}
