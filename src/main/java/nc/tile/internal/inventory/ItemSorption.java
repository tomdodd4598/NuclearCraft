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
            return switch (type) {
                case INPUT -> switch (this) {
                    case IN -> NON;
                    case NON -> OUT;
                    case OUT -> IN;
                    default -> IN;
                };
                case OUTPUT -> switch (this) {
                    case OUT -> NON;
                    case NON -> OUT;
                    default -> OUT;
                };
                default -> switch (this) {
                    case IN -> NON;
                    case NON -> BOTH;
                    case BOTH -> OUT;
                    case OUT -> IN;
                };
            };
		}
		else {
            return switch (type) {
                case INPUT -> switch (this) {
                    case IN -> OUT;
                    case OUT -> NON;
                    case NON -> IN;
                    default -> IN;
                };
                case OUTPUT -> switch (this) {
                    case OUT -> NON;
                    case NON -> OUT;
                    default -> OUT;
                };
                default -> switch (this) {
                    case IN -> OUT;
                    case OUT -> BOTH;
                    case BOTH -> NON;
                    case NON -> IN;
                };
            };
		}
	}
	
	@Override
	public String getName() {
        return switch (this) {
            case IN -> "in";
            case OUT -> "out";
            case BOTH -> "both";
            case NON -> "non";
        };
	}
	
	public TextFormatting getTextColor() {
        return switch (this) {
            case IN -> TextFormatting.BLUE;
            case OUT -> TextFormatting.GOLD;
            case BOTH -> TextFormatting.BOLD;
            case NON -> TextFormatting.GRAY;
        };
	}
	
	@Override
	public int getTextureX() {
        return switch (this) {
            case IN -> 108;
            case OUT -> 126;
            case NON -> 144;
            default -> 144;
        };
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
	
	public enum Type {
		DEFAULT,
		INPUT,
		OUTPUT
    }
}
