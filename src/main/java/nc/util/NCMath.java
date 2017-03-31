package nc.util;

public class NCMath {
	
	public static double Round(double value, int precision) {
		double scale = Math.pow(10.0D, (double) precision);
		return (double) Math.round(value * scale) / (double) Math.round(scale);
	}   
}
