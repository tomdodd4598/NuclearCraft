package nc.util;

import net.minecraft.util.math.MathHelper;

public class UnitHelper {
	
	public static final String[] SI_PREFIX = new String[] {" y", " z", " a", " f", " p", " n", " u", " m", " ", " k", " M", " G", " T", " P", " E", " Z", " Y"};
	
	// Long
	
	public static String prefix(long value, long max, int maxLength, String unit, int startingPrefix) {
		int minPrefixNumber = MathHelper.clamp(startingPrefix + 8, 0, 16);
		int prefixNumber = minPrefixNumber;
		
		boolean hasMax = max != Long.MIN_VALUE;
		String slashMaxVal = !hasMax ? "" : " / " + max;
		String sign = value < 0D ? "-" : "";
		if (value == 0D) {
			return value + slashMaxVal + SI_PREFIX[prefixNumber] + unit;
		}
		value = Math.abs(value);
		max = !hasMax ? value : max;
		
		long testValue = value, testMax = max;
		while (minPrefixNumber > 0) {
			testValue = NCMath.magnitudeMult(testValue, 3);
			testMax = NCMath.magnitudeMult(testMax, 3);
			minPrefixNumber--;
			if (NCMath.atLongLimit(testValue, 1000L) || NCMath.atLongLimit(testMax, 1000L)) break;
		}
		
		boolean descending = true;
		int length, maxLengthFixed = Math.max(maxLength, 3);
		while (prefixNumber < SI_PREFIX.length - 1) {
			length = NCMath.numberLength(value);
			if (descending) {
				if (length > maxLengthFixed) {
					descending = false;
					continue;
				}
				if (value != 0L) {
					slashMaxVal = !hasMax ? "" : " / " + max;
					return sign + value + slashMaxVal + SI_PREFIX[prefixNumber] + unit;
				}
				value = NCMath.magnitudeMult(value, 3);
				max = NCMath.magnitudeMult(max, 3);
				prefixNumber--;
			}
			else {
				if (length <= maxLengthFixed) {
					slashMaxVal = !hasMax ? "" : " / " + max;
					return sign + value + slashMaxVal + SI_PREFIX[prefixNumber] + unit;
				}
				value = NCMath.magnitudeMult(value, -3);
				max = NCMath.magnitudeMult(max, -3);
				prefixNumber++;
			}
			if (prefixNumber == minPrefixNumber) {
				descending = false;
			}
		}
		slashMaxVal = !hasMax ? "" : " / " + NCMath.magnitudeMult(max, -3);
		return sign + NCMath.magnitudeMult(value, -3) + slashMaxVal + SI_PREFIX[SI_PREFIX.length - 1] + unit;
	}
	
	public static String prefix(long value, int maxLength, String unit, int startingPrefix) {
		return UnitHelper.prefix(value, Long.MIN_VALUE, maxLength, unit, startingPrefix);
	}
	
	public static String prefix(long value, long max, int maxLength, String unit) {
		return UnitHelper.prefix(value, max, maxLength, unit, 0);
	}
	
	public static String prefix(long value, int maxLength, String unit) {
		return UnitHelper.prefix(value, maxLength, unit, 0);
	}
	
	// Double
	
	public static String prefix(double value, double max, int maxLength, String unit, int startingPrefix) {
		int minPrefixNumber = MathHelper.clamp(startingPrefix + 8, 0, 16);
		int prefixNumber = minPrefixNumber;
		
		boolean hasMax = max != Double.MIN_VALUE;
		String slashMaxVal = !hasMax ? "" : " / " + (long)max;
		String sign = value < 0D ? "-" : "";
		if (value == 0D) {
			return (long)value + slashMaxVal + SI_PREFIX[prefixNumber] + unit;
		}
		value = Math.abs(value);
		max = !hasMax ? value : max;
		
		double testValue = value, testMax = max;
		while (minPrefixNumber > 0) {
			testValue = NCMath.magnitudeMult(testValue, 3);
			testMax = NCMath.magnitudeMult(testMax, 3);
			minPrefixNumber--;
			if (NCMath.atDoubleLimit(testValue, 1000D) || NCMath.atDoubleLimit(testMax, 1000D)) break;
		}
		
		boolean descending = true;
		int length, maxLengthFixed = Math.max(maxLength, 3);
		while (prefixNumber < SI_PREFIX.length - 1) {
			length = NCMath.numberLength((long)value);
			if (descending) {
				if (length > maxLengthFixed) {
					descending = false;
					continue;
				}
				if ((long)value != 0L) {
					slashMaxVal = !hasMax ? "" : " / " + (long)max;
					return sign + (long)value + slashMaxVal + SI_PREFIX[prefixNumber] + unit;
				}
				value = NCMath.magnitudeMult(value, 3);
				max = NCMath.magnitudeMult(max, 3);
				prefixNumber--;
			}
			else {
				if (length <= maxLengthFixed) {
					slashMaxVal = !hasMax ? "" : " / " + (long)max;
					return sign + (long)value + slashMaxVal + SI_PREFIX[prefixNumber] + unit;
				}
				value = NCMath.magnitudeMult(value, -3);
				max = NCMath.magnitudeMult(max, -3);
				prefixNumber++;
			}
			if (prefixNumber == minPrefixNumber) {
				descending = false;
			}
		}
		slashMaxVal = !hasMax ? "" : " / " + (long)NCMath.magnitudeMult(max, -3);
		return sign + (long)NCMath.magnitudeMult(value, -3) + slashMaxVal + SI_PREFIX[SI_PREFIX.length - 1] + unit;
	}
	
	public static String prefix(double value, int maxLength, String unit, int startingPrefix) {
		return UnitHelper.prefix(value, Double.MIN_VALUE, maxLength, unit, startingPrefix);
	}
	
	public static String prefix(double value, double max, int maxLength, String unit) {
		return UnitHelper.prefix(value, max, maxLength, unit, 0);
	}
	
	public static String prefix(double value, int maxLength, String unit) {
		return UnitHelper.prefix(value, maxLength, unit, 0);
	}
	
	// Time
	
	public static final String[] TIME_UNIT = new String[] {" ticks", " seconds", " minutes", " hours", " days", " weeks", " years"};
	public static final String[] TIME_UNIT_SHORT = new String[] {" t", " s", " min", " hr", " d", " wk", " y"};
	public static final double[] TIME_MULT = new double[] {1D, 20D, 1200D, 72000D, 1728000D, 12096000D, 630720000D};
	
	public static String applyTimeUnit(long ticks, int maxLength) {
		return applyTimeUnit(ticks, maxLength, 0);
	}
	
	public static String applyTimeUnit(double ticks, int maxLength) {
		return applyTimeUnit(ticks, maxLength, 0);
	}
	
	public static String applyTimeUnit(long ticks, int maxLength, int startUnit) {
		int i = startUnit;
		while ((Math.round(ticks/TIME_MULT[i]) + "").length() > maxLength) i++;
		return Math.round(ticks/TIME_MULT[i]) + TIME_UNIT[i];
	}
	
	public static String applyTimeUnit(double ticks, int maxLength, int startUnit) {
		if (ticks < 1D) {
			return NCMath.decimalPlaces(ticks, 3) + " ticks";
		}
		int i = startUnit;
		while ((Math.round(ticks/TIME_MULT[i]) + "").length() > maxLength) i++;
		return Math.round(ticks/TIME_MULT[i]) + TIME_UNIT[i];
	}
	
	public static String applyTimeUnitShort(long ticks, int maxLength) {
		return applyTimeUnitShort(ticks, maxLength, 0);
	}
	
	public static String applyTimeUnitShort(double ticks, int maxLength) {
		return applyTimeUnitShort(ticks, maxLength, 0);
	}
	
	public static String applyTimeUnitShort(long ticks, int maxLength, int startUnit) {
		int i = startUnit;
		while ((Math.round(ticks/TIME_MULT[i]) + "").length() > maxLength) i++;
		return Math.round(ticks/TIME_MULT[i]) + TIME_UNIT_SHORT[i];
	}
	
	public static String applyTimeUnitShort(double ticks, int maxLength, int startUnit) {
		if (ticks < 1D) {
			return NCMath.decimalPlaces(ticks, 3) + " t";
		}
		int i = startUnit;
		while ((Math.round(ticks/TIME_MULT[i]) + "").length() > maxLength) i++;
		return Math.round(ticks/TIME_MULT[i]) + TIME_UNIT_SHORT[i];
	}
}
