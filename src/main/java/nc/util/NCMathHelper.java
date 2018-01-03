package nc.util;

public class NCMathHelper {
	
	public static double round(double value, int precision) {
		double scale = Math.pow(10.0D, (double) precision);
		return (double) Math.round(value * scale) / (double) Math.round(scale);
	}
	
	public static double Round(double value) {
		return round(value, 2);
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
	
	public static boolean atLimit(int number, int divider) {
		return Math.abs(number) > Integer.MAX_VALUE/divider;
	}
	
	public static int numberLength(long number) {
		return String.valueOf(number).length();
	}
}
