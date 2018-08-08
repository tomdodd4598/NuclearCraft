package nc.util;

import java.util.Random;

import net.minecraft.util.math.MathHelper;

public class NCMathHelper {
	
	private static Random rand = new Random();
	
	public static int square(int number) {
		return number*number;
	}
	
	public static int cube(int number) {
		return number*number*number;
	}
	
	public static double round(double value, int precision) {
		double scale = Math.pow(10.0D, (double) precision);
		return (double) Math.round(value * scale) / (double) Math.round(scale);
	}
	
	public static double round(double value) {
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
	
	public static boolean atIntLimit(int number, int divider) {
		return Math.abs(number) > Integer.MAX_VALUE/divider;
	}
	
	public static int numberLength(long number) {
		return String.valueOf(number).length();
	}
	
	public static int minus1Power(int pow) {
		if ((pow & 1) == 0) return 1; else return -1;
	}
	
	public static int choose(int n, int k) {
		if (n == k) return 1;
		if (n < k) return minus1Power(k)*choose(k - n - 1, k);
		if (k > n - k) k = n - k;
		
		double x = 1D;
		for (int i = 1, m = n; i <= k; i++, m--) x *= (double)m/(double)i;
		return (int) x;
	}
	
	public static int simplexNumber(int n, int p) {
		return choose(n + p - 1, p);
	}
	
	public static int floorTo(int x, int mult) {
		return mult*(MathHelper.floor(x/mult));
	}
	
	public static int ceilTo(int x, int mult) {
		return mult*(MathHelper.ceil(x/mult));
	}
	
	public static int roundTo(int x, int mult) {
		return mult*(Math.round(x/mult));
	}
	
	public static int getBinomial(int n, double p) {
		int x = 0;
		for(int i = 0; i < n; i++) {
			if(Math.random() < p) x++;
		}
		return x;
	}
	
	public static int getBinomial(int n, int p) {
		int x = 0;
		for(int i = 0; i < n; i++) {
			if(rand.nextInt(100) < p) x++;
		}
		return x;
	}
	
	public static int highestCommonFactor(int a, int b) {
		if (b == 0) return a;
		return highestCommonFactor(b, a % b);
	}
	
	public static int lowestCommonMultiple(int a, int b) {
		return Math.abs(a*b)/highestCommonFactor(a, b);
	}
}
