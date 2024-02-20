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
            return switch (this) {
                case DEFAULT -> VOID;
                case VOID_EXCESS -> DEFAULT;
                case VOID -> VOID_EXCESS;
            };
		}
		else {
            return switch (this) {
                case DEFAULT -> VOID_EXCESS;
                case VOID_EXCESS -> VOID;
                case VOID -> DEFAULT;
            };
		}
	}
	
	@Override
	public String getName() {
        return switch (this) {
            case DEFAULT -> "default";
            case VOID_EXCESS -> "void_excess";
            case VOID -> "void";
        };
	}
	
	public TextFormatting getTextColor() {
        return switch (this) {
            case DEFAULT -> TextFormatting.WHITE;
            case VOID_EXCESS -> TextFormatting.LIGHT_PURPLE;
            case VOID -> TextFormatting.DARK_PURPLE;
        };
	}
	
	@Override
	public int getTextureX() {
        return switch (this) {
            case DEFAULT -> 54;
            case VOID_EXCESS -> 72;
            case VOID -> 90;
        };
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
