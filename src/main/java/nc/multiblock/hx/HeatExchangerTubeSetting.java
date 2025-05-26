package nc.multiblock.hx;

import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.TextFormatting;

public enum HeatExchangerTubeSetting implements IStringSerializable {
	CLOSED("closed"),
	OPEN("open"),
	CLOSED_BAFFLE("closed_baffle"),
	OPEN_BAFFLE("open_baffle");
	
	private final String name;
	
	HeatExchangerTubeSetting(String name) {
		this.name = name;
	}
	
	public static HeatExchangerTubeSetting of(boolean open, boolean baffle) {
		return open ? (baffle ? OPEN_BAFFLE : OPEN) : (baffle ? CLOSED_BAFFLE : CLOSED);
	}
	
	public boolean isOpen() {
		return equals(OPEN) || equals(OPEN_BAFFLE);
	}
	
	public boolean isBaffle() {
		return equals(CLOSED_BAFFLE) || equals(OPEN_BAFFLE);
	}
	
	public HeatExchangerTubeSetting next() {
		return switch (this) {
			case CLOSED -> OPEN;
			case OPEN -> CLOSED_BAFFLE;
			case CLOSED_BAFFLE -> OPEN_BAFFLE;
			default -> CLOSED;
		};
	}
	
	public TextFormatting getTextColor() {
		return isOpen() ? TextFormatting.BOLD : TextFormatting.GRAY;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return getName();
	}
}
