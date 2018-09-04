package nc.util;

import net.minecraft.util.math.MathHelper;

public class UnitHelper {
	
	public static final String[] SI_PREFIX = new String[] {" y", " z", " a", " f", " p", " n", " u", " m", " ", " k", " M", " G", " T", " P", " E", " Z", " Y"};
	
	// Integer
	
	public static String prefix(long value, long maxValue, int maxLength, String unit, int startingPrefixNo, int lowestPrefixNo) {
		int minPrefixNumber = Math.max(lowestPrefixNo + 8, 0);
		int prefixNumber = MathHelper.clamp(startingPrefixNo + 8, minPrefixNumber, 16);
		String maxVal = maxValue == -1 ? "" : " / " + maxValue;
		if (value == 0) return value + maxVal + SI_PREFIX[minPrefixNumber] + unit;
		
		double newDouble = 1D*value;
		double newMaxDouble = maxValue == -1 ? newDouble : 1D*maxValue;
		while (prefixNumber > minPrefixNumber) {
			newDouble = NCMath.magnitudeMult(newDouble, 3);
			newMaxDouble = NCMath.magnitudeMult(newMaxDouble, 3);
            prefixNumber--;
            if (NCMath.atLongLimit((long) newDouble, 1000) || NCMath.atLongLimit((long) newMaxDouble, 1000) || prefixNumber == minPrefixNumber) break;
		}
		int maxLengthFixed = Math.max(maxLength, 3);
		long newValue = (long) newDouble;
		long newMaxValue = (long) newMaxDouble;
		int length = NCMath.numberLength(newValue);
		while (prefixNumber < SI_PREFIX.length - 1) {
			length = NCMath.numberLength(newValue);
			if (length <= maxLengthFixed) {
				maxVal = maxValue == -1 ? "" : " / " + newMaxValue;
				return newValue + maxVal + SI_PREFIX[prefixNumber] + unit;
			}
			newValue = NCMath.magnitudeMult(newValue, -3);
			newMaxValue = NCMath.magnitudeMult(newMaxValue, -3);
            prefixNumber++;
        }
		maxVal = maxValue == -1 ? "" : " / " + NCMath.magnitudeMult(newMaxValue, -3);
		return NCMath.magnitudeMult(newValue, -3) + maxVal + SI_PREFIX[SI_PREFIX.length - 1] + unit;
	}
	
	public static String prefix(long value, int maxLength, String unit, int startingPrefixNo, int lowestPrefixNo) {
		return UnitHelper.prefix(value, -1, maxLength, unit, startingPrefixNo, lowestPrefixNo);
	}
	
	public static String prefix(long value, long maxValue, int maxLength, String unit, int startingPrefixNo) {
		return UnitHelper.prefix(value, maxValue, maxLength, unit, startingPrefixNo, startingPrefixNo);
	}
	
	public static String prefix(long value, int maxLength, String unit, int startingPrefixNo) {
		return UnitHelper.prefix(value, maxLength, unit, startingPrefixNo, startingPrefixNo);
	}
	
	public static String prefix(long value, long maxValue, int maxLength, String unit) {
		return UnitHelper.prefix(value, maxValue, maxLength, unit, 0);
	}
	
	public static String prefix(long value, int maxLength, String unit) {
		return UnitHelper.prefix(value, maxLength, unit, 0);
	}
	
	public static String ratePrefix(long perSecond, int maxLength, String unit, int startingPrefixNo) {
		boolean useTicks = perSecond % 20 == 0;
		return UnitHelper.prefix(useTicks ? perSecond/20 : perSecond, maxLength, unit + (useTicks ? "/t" : "/s"), startingPrefixNo);
	}
	
	public static String ratePrefix(long perSecond, int maxLength, String unit) {
		return UnitHelper.ratePrefix(perSecond, maxLength, unit, 0);
	}
	
	// Decimal
	
	public static String prefix(double value, double maxValue, int maxLength, String unit, int startingPrefixNo, int lowestPrefixNo) {
		int minPrefixNumber = Math.max(lowestPrefixNo + 8, 0);
		int prefixNumber = MathHelper.clamp(startingPrefixNo + 8, minPrefixNumber, 16);
		String maxVal = maxValue == -1D ? "" : " / " + (long)maxValue;
		if (value == 0D) return (long)value + maxVal + SI_PREFIX[minPrefixNumber] + unit;
		
		double newMax = maxValue == -1D ? value : maxValue;
		while (prefixNumber > minPrefixNumber) {
			value = NCMath.magnitudeMult(value, 3);
			newMax = NCMath.magnitudeMult(newMax, 3);
            prefixNumber--;
            if (NCMath.atDoubleLimit(value, 1000) || NCMath.atDoubleLimit(newMax, 1000) || prefixNumber == minPrefixNumber) break;
		}
		int maxLengthFixed = Math.max(maxLength, 3);
		int length = NCMath.numberLength((long)value);
		while (prefixNumber < SI_PREFIX.length - 1) {
			length = NCMath.numberLength((long)value);
			if (length <= maxLengthFixed) {
				maxVal = maxValue == -1D ? "" : " / " + (long)newMax;
				return (long)value + maxVal + SI_PREFIX[prefixNumber] + unit;
			}
			value = NCMath.magnitudeMult(value, -3);
			newMax = NCMath.magnitudeMult(newMax, -3);
            prefixNumber++;
        }
		maxVal = maxValue == -1D ? "" : " / " + (long)NCMath.magnitudeMult(newMax, -3);
		return (long)NCMath.magnitudeMult(value, -3) + maxVal + SI_PREFIX[SI_PREFIX.length - 1] + unit;
	}
	
	public static String prefix(double value, int maxLength, String unit, int startingPrefixNo, int lowestPrefixNo) {
		return UnitHelper.prefix(value, -1D, maxLength, unit, startingPrefixNo, lowestPrefixNo);
	}
	
	public static String prefix(double value, double maxValue, int maxLength, String unit, int startingPrefixNo) {
		return UnitHelper.prefix(value, maxValue, maxLength, unit, startingPrefixNo, startingPrefixNo);
	}
	
	public static String prefix(double value, int maxLength, String unit, int startingPrefixNo) {
		return UnitHelper.prefix(value, maxLength, unit, startingPrefixNo, startingPrefixNo);
	}
	
	public static String prefix(double value, double maxValue, int maxLength, String unit) {
		return UnitHelper.prefix(value, maxValue, maxLength, unit, 0);
	}
	
	public static String prefix(double value, int maxLength, String unit) {
		return UnitHelper.prefix(value, maxLength, unit, 0);
	}
	
	// Time Units
	
	public static final String[] TIME_UNIT = new String[] {" ticks", " seconds", " minutes", " hours", " days", " weeks", " years"};
	public static final double[] TIME_MULT = new double[] {1D, 20D, 1200D, 72000D, 1728000D, 12096000D, 630720000D};
	
	public static String applyTimeUnit(long ticks, int maxLength) {
		int i = 0;
		while ((Math.round(ticks/TIME_MULT[i]) + "").length() > maxLength) i++;
		return Math.round(ticks/TIME_MULT[i]) + TIME_UNIT[i];
	}
	
	public static String applyTimeUnit(double ticks, int maxLength) {
		int i = 0;
		while ((Math.round(ticks/TIME_MULT[i]) + "").length() > maxLength) i++;
		return Math.round(ticks/TIME_MULT[i]) + TIME_UNIT[i];
	}
}
