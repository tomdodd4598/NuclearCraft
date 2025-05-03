package nc.util;

import net.minecraft.util.math.MathHelper;

import java.math.*;
import java.text.DecimalFormat;
import java.util.*;

public class NCMath {
	
	public static final double SQRT2 = 1.41421356237309504880D;
	public static final double INV_SQRT2 = 0.707106781186547524401D;
	public static final double LN2 = 0.693147180559945309417D;
	public static final double LN10 = 2.30258509299404568402D;
	public static final double EPSILON = Math.ulp(1D);
	
	private static final Random rand = new Random();
	
	public static long clamp(long num, long min, long max) {
		if (num < min) {
			return min;
		}
		else {
			return Math.min(num, max);
		}
	}
	
	public static int sq(int number) {
		return number * number;
	}
	
	public static double sq(double number) {
		return number * number;
	}
	
	public static int cube(int number) {
		return number * number * number;
	}
	
	public static double cube(double number) {
		return number * number * number;
	}
	
	public static int getBit(int number, int position) {
		return number >> position & 1;
	}
	
	public static int onlyBits(int number, int... positions) {
		int out = 0, bit;
		for (int position : positions) {
			bit = getBit(number, position);
			if (bit != 0) {
				out += 1 << position;
			}
		}
		return out;
	}
	
	public static int swap(int number, int i, int j) {
		int bit1 = getBit(number, i), bit2 = getBit(number, j);
		return bit1 == bit2 ? number : number ^ (1 << i | 1 << j);
	}
	
	public static byte[] booleansToBytes(boolean[] arr) {
		byte[] out = new byte[arr.length];
		for (int i = 0; i < arr.length; ++i) {
			out[i] = (byte) (arr[i] ? 1 : 0);
		}
		return out;
	}
	
	public static boolean[] bytesToBooleans(byte[] arr) {
		boolean[] out = new boolean[arr.length];
		for (int i = 0; i < arr.length; ++i) {
			out[i] = arr[i] != 0;
		}
		return out;
	}
	
	public static int kroneckerDelta(int... indices) {
		for (int i = 0; i < indices.length; ++i) {
			if (indices[i] != indices[(i + 1) % indices.length]) {
				return 0;
			}
		}
		return 1;
	}
	
	public static boolean allEqual(int value, int... values) {
		for (int i : values) {
			if (value != i) {
				return false;
			}
		}
		return true;
	}
	
	public static double sin_d(double deg) {
		return Math.sin(Math.toRadians(deg));
	}
	
	public static double cos_d(double deg) {
		return Math.cos(Math.toRadians(deg));
	}
	
	public static double tan_d(double deg) {
		return Math.tan(Math.toRadians(deg));
	}
	
	public static double[] cartesianFromSpherical(double r, double theta, double phi) {
		return new double[] {r * sin_d(theta) * cos_d(phi), r * sin_d(theta) * sin_d(phi), r * cos_d(theta)};
	}
	
	public static long magnitudeMult(long in, int power) {
		double doubleOut = in * Math.pow(10D, 1D * power);
		return Math.round(doubleOut);
	}
	
	public static double magnitudeMult(double in, int power) {
		return in * Math.pow(10D, 1D * power);
	}
	
	public static boolean atIntLimit(int number, int multiplier) {
		return Math.abs(number) > Integer.MAX_VALUE / multiplier;
	}
	
	public static boolean atLongLimit(long number, long multiplier) {
		return Math.abs(number) > Long.MAX_VALUE / multiplier;
	}
	
	public static boolean atDoubleLimit(double number, double multiplier) {
		return Math.abs(number) > Double.MAX_VALUE / multiplier;
	}
	
	public static int numberLength(long number) {
		return String.valueOf(number).length();
	}
	
	public static int minus1Pow(int pow) {
		return (pow & 1) == 0 ? 1 : -1;
	}
	
	public static int choose(int n, int k) {
		if (n == k) {
			return 1;
		}
		if (n < k) {
			return minus1Pow(k) * choose(k - n - 1, k);
		}
		if (k > n - k) {
			k = n - k;
		}
		
		double x = 1D;
		for (int i = 1, m = n; i <= k; ++i, --m) {
			x *= (double) m / (double) i;
		}
		return (int) Math.round(x);
	}
	
	public static int simplexNumber(int n, int p) {
		return choose(n + p - 1, p);
	}
	
	public static double floorTo(double x, double mult) {
		return mult == 0D ? x : x - x % mult;
	}
	
	public static double ceilTo(double x, double mult) {
		if (mult == 0D) {
			return x;
		}
		double mod = x % mult;
		return mod == 0D ? x : x + mult - mod;
	}
	
	public static double roundTo(double x, double mult) {
		if (mult == 0D) {
			return x;
		}
		double mod = x % mult;
		return mod >= mult / 2D ? x + mult - mod : x - mod;
	}
	
	public static int getBinomial(int n, int p) {
		int x = 0;
		for (int i = 0; i < n; ++i) {
			if (rand.nextInt(100) < p) {
				++x;
			}
		}
		return x;
	}
	
	public static int hcf(int... arr) {
		int l = arr.length;
		if (l == 0) {
			return 1;
		}
		else if (l == 1) {
			return arr[0];
		}
		int hcf = hcfInternal(arr[0], arr[1]);
		if (l == 2) {
			return hcf;
		}
		else {
			int[] next = new int[l - 1];
			next[0] = hcf;
			System.arraycopy(arr, 2, next, 1, l - 1 - 1);
			return hcf(next);
		}
	}
	
	private static int hcfInternal(int a, int b) {
		if (b == 0) {
			return a;
		}
		return hcfInternal(b, a % b);
	}
	
	public static int lcm(int... arr) {
		int l = arr.length;
		if (l == 0) {
			return 1;
		}
		else if (l == 1) {
			return arr[0];
		}
		int lcm = lcmInternal(arr[0], arr[1]);
		if (l == 2) {
			return lcm;
		}
		else {
			int[] next = new int[l - 1];
			next[0] = lcm;
			System.arraycopy(arr, 2, next, 1, l - 1 - 1);
			return lcm(next);
		}
	}
	
	private static int lcmInternal(int a, int b) {
		return toInt(Math.abs((long) a * (long) b)) / hcfInternal(a, b);
	}
	
	public static int hollowCuboid(int x, int y, int z) {
		return x * y * z - (x - 2) * (y - 2) * (z - 2);
	}
	
	public static int hollowCube(int length) {
		return hollowCuboid(length, length, length);
	}
	
	public static double trapezoidalWave(double degs, double phase) {
		double angle = degs %= 360D;
		angle = (angle + phase) % 360D;
		if (angle <= 60D) {
			return 1D;
		}
		else if (angle <= 120D) {
			return (120D - angle) / 60D;
		}
		else if (angle <= 240D) {
			return 0D;
		}
		else if (angle <= 300D) {
			return (angle - 240D) / 60D;
		}
		else {
			return 1D;
		}
	}
	
	public static String sigFigs(double number, int sigFigs) {
		if (number == (long) number) {
			return Long.toString((long) number);
		}
		BigDecimal bd = new BigDecimal(number);
		bd = bd.round(new MathContext(Math.max(1, sigFigs)));
		return Double.toString(bd.doubleValue());
	}
	
	public static String decimalPlaces(double number, int places) {
		if (number == (long) number) {
			return Long.toString((long) number);
		}
		char[] arr = new char[Math.max(1, places)];
		Arrays.fill(arr, '#');
		DecimalFormat df = new DecimalFormat("0." + new String(arr));
		return df.format(number);
	}
	
	public static String pcSigFigs(double number, int sigFigs) {
		return sigFigs(100D * number, sigFigs) + "%";
	}
	
	public static String pcDecimalPlaces(double number, int places) {
		return decimalPlaces(100D * number, places) + "%";
	}
	
	public static int toInt(long value) {
		return (int) clamp(value, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}
	
	public static int toInt(double value) {
		return (int) MathHelper.clamp(value, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}
	
	public static int getComparatorSignal(double value, double max, double leeway) {
		return value <= leeway ? 0 : value >= max - leeway ? 15 : toInt(1D + 14D * value / max);
	}
}
