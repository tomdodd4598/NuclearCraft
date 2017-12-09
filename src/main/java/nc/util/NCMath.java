package nc.util;

import net.minecraft.util.math.MathHelper;

public class NCMath {
	
	public static double Round(double value, int precision) {
		double scale = Math.pow(10.0D, (double) precision);
		return (double) Math.round(value * scale) / (double) Math.round(scale);
	}
	
	public static int kroneckerDelta(int... indices) {
		for (int i = 0; i < indices.length; i++) {
			if (indices[i] != indices[(i + 1) % indices.length]) return 0;
		}
		return 1;
	}
	
	public static double[] cartesianFromSpherical(double r, double theta, double phi) {
		return new double[] {r*Math.sin(theta*Math.PI/180D)*Math.cos(phi*Math.PI/180D), r*Math.sin(theta*Math.PI/180D)*Math.sin(phi*Math.PI/180D), r*Math.cos(theta*Math.PI/180D)};
	}
	
	public static int magnitudeMult(int in, int power) {
		double doubleOut = (1D*in)*Math.pow(10D, 1D*power);
		double roundedOut = Math.round(doubleOut);
		return (int) roundedOut;
	}
	
	public static double magnitudeMult(double in, int power) {
		return (1D*in)*Math.pow(10D, 1D*power);
	}
	
	private static boolean atLimit(int number, int divider) {
		return Math.abs(number) > Integer.MAX_VALUE/divider;
	}
	
	public static int numberLength(long number) {
		return String.valueOf(number).length();
	}
	
	public static final String[] SI_PREFIX = new String[] {" y", " z", " a", " f", " p", " n", " u", " m", " ", " k", " M", " G", " T", " P", " E", " Z", " Y"};
	
	public static String prefix(int value, int maxValue, int maxLength, String unit, int startingPrefixNo, int lowestPrefixNo) {
		int minPrefixNumber = Math.max(lowestPrefixNo + 8, 0);
		int prefixNumber = MathHelper.clamp(startingPrefixNo + 8, minPrefixNumber, 16);
		String maxVal = maxValue == -1 ? "" : " / " + maxValue;
		if (value == 0) return value + maxVal + SI_PREFIX[minPrefixNumber] + unit;
		
		double newDouble = 1D*value;
		double newMaxDouble = maxValue == -1 ? newDouble : 1D*maxValue;
		while (prefixNumber > minPrefixNumber) {
			newDouble = magnitudeMult(newDouble, 3);
			newMaxDouble = magnitudeMult(newMaxDouble, 3);
            prefixNumber--;
            if (atLimit((int) newDouble, 1000) || atLimit((int) newMaxDouble, 1000) || prefixNumber == minPrefixNumber) break;
		}
		
		int newValue = (int) newDouble;
		int newMaxValue = (int) newMaxDouble;
		int length = numberLength(newValue);
		while (prefixNumber < SI_PREFIX.length - 1) {
			length = numberLength(newValue);
			if (length <= maxLength) {
				maxVal = maxValue == -1 ? "" : " / " + newMaxValue;
				return newValue + maxVal + SI_PREFIX[prefixNumber] + unit;
			}
			newValue = magnitudeMult(newValue, -3);
			newMaxValue = magnitudeMult(newMaxValue, -3);
            prefixNumber++;
        }
		maxVal = maxValue == -1 ? "" : " / " + magnitudeMult(newMaxValue, -3);
		return magnitudeMult(newValue, -3) + maxVal + SI_PREFIX[SI_PREFIX.length - 1] + unit;
	}
	
	public static String prefix(int value, int maxLength, String unit, int startingPrefixNo, int lowestPrefixNo) {
		return NCMath.prefix(value, -1, maxLength, unit, startingPrefixNo, lowestPrefixNo);
	}
	
	public static String prefix(int value, int maxValue, int maxLength, String unit, int startingPrefixNo) {
		return NCMath.prefix(value, maxValue, maxLength, unit, startingPrefixNo, startingPrefixNo);
	}
	
	public static String prefix(int value, int maxLength, String unit, int startingPrefixNo) {
		return NCMath.prefix(value, maxLength, unit, startingPrefixNo, startingPrefixNo);
	}
	
	public static String prefix(int value, int maxValue, int maxLength, String unit) {
		return NCMath.prefix(value, maxValue, maxLength, unit, 0);
	}
	
	public static String prefix(int value, int maxLength, String unit) {
		return NCMath.prefix(value, maxLength, unit, 0);
	}
}
