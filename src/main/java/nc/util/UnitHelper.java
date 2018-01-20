package nc.util;

import net.minecraft.util.math.MathHelper;

public class UnitHelper {
	
	public static final String[] SI_PREFIX = new String[] {" y", " z", " a", " f", " p", " n", " u", " m", " ", " k", " M", " G", " T", " P", " E", " Z", " Y"};
	
	public static String prefix(int value, int maxValue, int maxLength, String unit, int startingPrefixNo, int lowestPrefixNo) {
		int minPrefixNumber = Math.max(lowestPrefixNo + 8, 0);
		int prefixNumber = MathHelper.clamp(startingPrefixNo + 8, minPrefixNumber, 16);
		String maxVal = maxValue == -1 ? "" : " / " + maxValue;
		if (value == 0) return value + maxVal + SI_PREFIX[minPrefixNumber] + unit;
		
		double newDouble = 1D*value;
		double newMaxDouble = maxValue == -1 ? newDouble : 1D*maxValue;
		while (prefixNumber > minPrefixNumber) {
			newDouble = NCMathHelper.magnitudeMult(newDouble, 3);
			newMaxDouble = NCMathHelper.magnitudeMult(newMaxDouble, 3);
            prefixNumber--;
            if (NCMathHelper.atLimit((int) newDouble, 1000) || NCMathHelper.atLimit((int) newMaxDouble, 1000) || prefixNumber == minPrefixNumber) break;
		}
		int maxLengthFixed = Math.max(maxLength, 3);
		int newValue = (int) newDouble;
		int newMaxValue = (int) newMaxDouble;
		int length = NCMathHelper.numberLength(newValue);
		while (prefixNumber < SI_PREFIX.length - 1) {
			length = NCMathHelper.numberLength(newValue);
			if (length <= maxLengthFixed) {
				maxVal = maxValue == -1 ? "" : " / " + newMaxValue;
				return newValue + maxVal + SI_PREFIX[prefixNumber] + unit;
			}
			newValue = NCMathHelper.magnitudeMult(newValue, -3);
			newMaxValue = NCMathHelper.magnitudeMult(newMaxValue, -3);
            prefixNumber++;
        }
		maxVal = maxValue == -1 ? "" : " / " + NCMathHelper.magnitudeMult(newMaxValue, -3);
		return NCMathHelper.magnitudeMult(newValue, -3) + maxVal + SI_PREFIX[SI_PREFIX.length - 1] + unit;
	}
	
	public static String prefix(int value, int maxLength, String unit, int startingPrefixNo, int lowestPrefixNo) {
		return UnitHelper.prefix(value, -1, maxLength, unit, startingPrefixNo, lowestPrefixNo);
	}
	
	public static String prefix(int value, int maxValue, int maxLength, String unit, int startingPrefixNo) {
		return UnitHelper.prefix(value, maxValue, maxLength, unit, startingPrefixNo, startingPrefixNo);
	}
	
	public static String prefix(int value, int maxLength, String unit, int startingPrefixNo) {
		return UnitHelper.prefix(value, maxLength, unit, startingPrefixNo, startingPrefixNo);
	}
	
	public static String prefix(int value, int maxValue, int maxLength, String unit) {
		return UnitHelper.prefix(value, maxValue, maxLength, unit, 0);
	}
	
	public static String prefix(int value, int maxLength, String unit) {
		return UnitHelper.prefix(value, maxLength, unit, 0);
	}
	
	public static String ratePrefix(int perSecond, int maxLength, String unit, int startingPrefixNo) {
		boolean useTicks = perSecond % 20 == 0;
		return UnitHelper.prefix(useTicks ? perSecond/20 : perSecond, maxLength, unit + (useTicks ? "/t" : "/s"), startingPrefixNo);
	}
	
	public static String ratePrefix(int perSecond, int maxLength, String unit) {
		return UnitHelper.ratePrefix(perSecond, maxLength, unit, 0);
	}
}
