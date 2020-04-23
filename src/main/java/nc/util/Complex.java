package nc.util;

public class Complex {
	
	private Complex() {}
	
	public static double abs(double re, double im) {
		return Math.hypot(re, im);
	}
	
	public static double absSq(double re, double im) {
		return re*re + im*im;
	}
	
	public static double arg(double re, double im) {
		return Math.atan2(im, re);
	}
	
	public static double[] multiply(double re1, double im1, double re2, double im2) {
		return new double[] {re1*re2 - im1*im2, re1*im2 + im1*re2};
	}
	
	public static double[] reciprocal(double re, double im) {
		double scale = absSq(re, im);
		return new double[] {re/scale, -im/scale};
	}
	
	public static double[] divide(double re1, double im1, double re2, double im2) {
		double scale = absSq(re2, im2);
		return new double[] {(re1*re2 + im1*im2)/scale, (im1*re2 - re1*im2)/scale};
	}
	
	public static double[] exp(double re, double im) {
		return new double[] {Math.exp(re)*Math.cos(im), Math.exp(re)*Math.sin(im)};
	}
	
	public static double[] sin(double re, double im) {
		return new double[] {Math.sin(re)*Math.cosh(im), Math.cos(re)*Math.sinh(im)};
	}
	
	public static double[] cos(double re, double im) {
		return new double[] {Math.cos(re)*Math.cosh(im), -Math.sin(re)*Math.sinh(im)};
	}
	
	public static double[] tan(double re, double im) {
		return new double[] {Math.sin(re)*Math.cosh(im)/(Math.cos(re)*Math.cosh(im)), Math.cos(re)*Math.sinh(im)/(-Math.sin(re)*Math.sinh(im))};
	}
	
	public static double[] normalize(double re, double im) {
		double scale = abs(re, im);
		return new double[] {re/scale, im/scale};
	}
	
	public static double[] phase(double phi) {
		return new double[] {Math.cos(phi), Math.sin(phi)};
	}
	
	public static double[] phase_d(double phi) {
		return new double[] {NCMath.cos_d(phi), NCMath.sin_d(phi)};
	}
	
	public static String toString(double re, double im) {
		if (im == 0) return re + "";
		if (re == 0) return im + "i";
		if (im < 0) return re + " - " + (-im) + "i";
		return re + " + " + im + "i";
	}
}
