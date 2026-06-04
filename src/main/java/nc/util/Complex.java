package nc.util;

public class Complex {
	
	public double re, im;
	
	public Complex(double re, double im) {
		this.re = re;
		this.im = im;
	}
	
	public double absSq() {
		return re * re + im * im;
	}
	
	public double abs() {
		return Math.sqrt(absSq());
	}
	
	public double arg() {
		return Math.atan2(im, re);
	}
	
	public static double absSq(double a, double b) {
		return a * a + b * b;
	}
	
	public static double abs(double a, double b) {
		return Math.sqrt(absSq(a, b));
	}
	
	public static double arg(double a, double b) {
		return Math.atan2(a, b);
	}
	
	public void multiplyBy(double a, double b) {
		double re = this.re, im = this.im;
		this.re = re * a - im * b;
		this.im = re * b + im * a;
	}
	
	public void divideBy(double a, double b) {
		double re = this.re, im = this.im, absSq = a * a + b * b;
		this.re = (re * a + im * b) / absSq;
		this.im = (im * a - re * b) / absSq;
	}
	
	public static Complex multiply(double a, double b, double c, double d) {
		return new Complex(a * c - b * d, a * d + b * c);
	}
	
	public static Complex divide(double a, double b, double c, double d) {
		double absSq = c * c + d * d;
		return new Complex((a * c + b * d) / absSq, (b * c - a * d) / absSq);
	}
	
	public static Complex sqrt(double re, double im) {
		if (im == 0D) {
			if (re >= 0D) {
				return new Complex(Math.sqrt(re), 0D);
			}
			else {
				return new Complex(0D, Math.sqrt(-re));
			}
		}
		else {
			double a = Math.sqrt(0.5D * (re + Math.sqrt(re * re + im * im)));
			return new Complex(a, 0.5D * im / a);
		}
	}
	
	public static Complex invSqrt(double re, double im) {
		double absSq = re * re + im * im;
		return sqrt(re / absSq, -im / absSq);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder().append(re);
		if (im >= 0D) {
			sb.append('+');
		}
		return sb.append(im).append('i').toString();
	}
}
