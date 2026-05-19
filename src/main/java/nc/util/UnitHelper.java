package nc.util;

import net.minecraft.util.math.MathHelper;

import java.math.*;

public class UnitHelper {
	
	public static final String[] SI_PREFIX = new String[] {"q", "r", "y", "z", "a", "f", "p", "n", "u", "m", "", "k", "M", "G", "T", "P", "E", "Z", "Y", "R", "Q"};
	
	// Long
	
	public static String prefix(long value, long max, int maxLength, String unit, int startingPrefix) {
		return prefixInternal(BigDecimal.valueOf(value), BigDecimal.valueOf(max), maxLength, unit, startingPrefix);
	}
	
	public static String prefix(long value, int maxLength, String unit, int startingPrefix) {
		return prefixInternal(BigDecimal.valueOf(value), null, maxLength, unit, startingPrefix);
	}
	
	public static String prefix(long value, long max, int maxLength, String unit) {
		return prefix(value, max, maxLength, unit, 0);
	}
	
	public static String prefix(long value, int maxLength, String unit) {
		return prefix(value, maxLength, unit, 0);
	}
	
	// Double
	
	public static String prefix(double value, double max, int maxLength, String unit, int startingPrefix) {
		return prefixInternal(BigDecimal.valueOf(value), BigDecimal.valueOf(max), maxLength, unit, startingPrefix);
	}
	
	public static String prefix(double value, int maxLength, String unit, int startingPrefix) {
		return prefixInternal(BigDecimal.valueOf(value), null, maxLength, unit, startingPrefix);
	}
	
	public static String prefix(double value, double max, int maxLength, String unit) {
		return prefix(value, max, maxLength, unit, 0);
	}
	
	public static String prefix(double value, int maxLength, String unit) {
		return prefix(value, maxLength, unit, 0);
	}
	
	// Time
	
	public static final String[] TIME_UNIT = new String[] {"ticks", "seconds", "minutes", "hours", "days", "weeks", "years"};
	
	public static final String[] TIME_UNIT_SHORT = new String[] {"t", "s", "min", "hr", "d", "wk", "y"};
	
	public static final double[] TIME_MULT = new double[] {1D, 20D, 1200D, 72000D, 1728000D, 12096000D, 630720000D};
	
	public static String applyTimeUnit(long ticks, int maxLength) {
		return applyTimeUnitInternal(BigDecimal.valueOf(ticks), maxLength, 0, TIME_UNIT);
	}
	
	public static String applyTimeUnit(double ticks, int maxLength) {
		return applyTimeUnitInternal(BigDecimal.valueOf(ticks), maxLength, 0, TIME_UNIT);
	}
	
	public static String applyTimeUnit(long ticks, int maxLength, int startUnit) {
		return applyTimeUnitInternal(BigDecimal.valueOf(ticks), maxLength, startUnit, TIME_UNIT);
	}
	
	public static String applyTimeUnit(double ticks, int maxLength, int startUnit) {
		return applyTimeUnitInternal(BigDecimal.valueOf(ticks), maxLength, startUnit, TIME_UNIT);
	}
	
	public static String applyTimeUnitShort(long ticks, int maxLength) {
		return applyTimeUnitInternal(BigDecimal.valueOf(ticks), maxLength, 0, TIME_UNIT_SHORT);
	}
	
	public static String applyTimeUnitShort(double ticks, int maxLength) {
		return applyTimeUnitInternal(BigDecimal.valueOf(ticks), maxLength, 0, TIME_UNIT_SHORT);
	}
	
	public static String applyTimeUnitShort(long ticks, int maxLength, int startingPrefix) {
		return applyTimeUnitInternal(BigDecimal.valueOf(ticks), maxLength, startingPrefix, TIME_UNIT_SHORT);
	}
	
	public static String applyTimeUnitShort(double ticks, int maxLength, int startingPrefix) {
		return applyTimeUnitInternal(BigDecimal.valueOf(ticks), maxLength, startingPrefix, TIME_UNIT_SHORT);
	}
	
	// Internal
	
	private static String prefixInternal(BigDecimal value, BigDecimal max, int maxLength, String unit, int startingPrefix) {
		maxLength = Math.max(1, maxLength);
		startingPrefix = MathHelper.clamp(10 + startingPrefix, 0, SI_PREFIX.length - 1);
		
		BigDecimal reference = (value.signum() == 0 && max != null ? max : value).abs();
		int prefixIndex = startingPrefix;
		
		if (reference.signum() != 0) {
			BigDecimal rounded = roundToMaxDigits(reference, maxLength);
			
			if (rounded.abs().compareTo(BigDecimal.ONE) < 0) {
				for (int i = startingPrefix; i >= 0; --i) {
					BigDecimal scaled = reference.scaleByPowerOfTen(-3 * (i - startingPrefix));
					rounded = roundToMaxDigits(scaled, maxLength);
					
					if (rounded.abs().compareTo(BigDecimal.ONE) >= 0 && displayedDigitCount(rounded) <= maxLength) {
						prefixIndex = i;
						break;
					}
				}
			}
			else {
				while (prefixIndex < SI_PREFIX.length - 1) {
					BigDecimal scaled = reference.scaleByPowerOfTen(-3 * (prefixIndex - startingPrefix));
					rounded = roundToMaxDigits(scaled, maxLength);
					
					if (displayedDigitCount(rounded) <= maxLength) {
						break;
					}
					
					++prefixIndex;
				}
			}
		}
		
		String prefix = SI_PREFIX[prefixIndex];
		String suffix = prefix.isEmpty() ? " " + unit : " " + prefix + unit;
		
		BigDecimal rounded = roundToMaxDigits(value.scaleByPowerOfTen(-3 * (prefixIndex - startingPrefix)), maxLength);
		BigDecimal stripped = rounded.stripTrailingZeros();
		String valueString = stripped.signum() == 0 ? "0" : stripped.toPlainString();
		
		if (max == null) {
			return valueString + suffix;
		}
		
		BigDecimal roundedMax = roundToMaxDigits(max.scaleByPowerOfTen(-3 * (prefixIndex - startingPrefix)), maxLength);
		BigDecimal strippedMax = roundedMax.stripTrailingZeros();
		String maxString = strippedMax.signum() == 0 ? "0" : strippedMax.toPlainString();
		
		return valueString + suffix + " / " + maxString + suffix;
	}
	
	private static String applyTimeUnitInternal(BigDecimal ticks, int maxLength, int startingPrefix, String[] units) {
		maxLength = Math.max(1, maxLength);
		startingPrefix = MathHelper.clamp(startingPrefix, 0, units.length - 1);
		
		while (startingPrefix < TIME_MULT.length - 1) {
			BigDecimal scaled = ticks.divide(BigDecimal.valueOf(TIME_MULT[startingPrefix]), 16, RoundingMode.HALF_UP);
			BigDecimal rounded = roundToMaxDigits(scaled, maxLength);
			
			if (displayedDigitCount(rounded) <= maxLength) {
				break;
			}
			
			++startingPrefix;
		}
		
		BigDecimal scaled = ticks.divide(BigDecimal.valueOf(TIME_MULT[startingPrefix]), 16, RoundingMode.HALF_UP);
		BigDecimal rounded = roundToMaxDigits(scaled, maxLength);
		BigDecimal stripped = rounded.stripTrailingZeros();
		String string = stripped.signum() == 0 ? "0" : stripped.toPlainString();
		
		return string + " " + units[startingPrefix];
	}
	
	private static int displayedDigitCount(BigDecimal value) {
		BigDecimal stripped = value.abs().stripTrailingZeros();
		String string = stripped.signum() == 0 ? "0" : stripped.toPlainString();
		
		int digitCount = 0;
		for (int i = 0; i < string.length(); ++i) {
			char c = string.charAt(i);
			
			if (c >= '0' && c <= '9') {
				++digitCount;
			}
		}
		
		return Math.max(1, digitCount);
	}
	
	private static BigDecimal roundToMaxDigits(BigDecimal value, int maxDigits) {
		maxDigits = Math.max(1, maxDigits);
		
		if (value.signum() == 0) {
			return BigDecimal.ZERO;
		}
		
		BigDecimal abs = value.abs();
		int digits;
		
		if (abs.compareTo(BigDecimal.ONE) < 0) {
			digits = 1;
		}
		else {
			BigDecimal stripped = abs.stripTrailingZeros();
			digits = Math.max(1, stripped.precision() - stripped.scale());
		}
		
		return value.setScale(Math.max(0, maxDigits - digits), RoundingMode.HALF_UP);
	}
}
