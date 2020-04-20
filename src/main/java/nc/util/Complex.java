package nc.util;

public class Complex {
	
	private double re, im;
	
	public Complex(double real, double imag) {
		re = real;
		im = imag;
	}
	
	public Complex copy() {
		return new Complex(re, im);
	}
	
	public void set(double real, double imag) {
		re = real;
		im = imag;
	}
	
	public void set(Complex c) {
		set(c.re, c.im);
	}
	
	public double re() {
		return re;
	}
	
	public double im() {
		return im;
	}
	
	public double abs() {
		return Math.hypot(re, im);
	}
	
	public double absSq() {
		return re*re + im*im;
	}
	
	public double arg() {
		return Math.atan2(im, re);
	}
	
	public Complex conj() {
		im = -im;
		return this;
	}
	
	public Complex add(Complex c) {
		re += c.re;
		im += c.im;
		return this;
	}
	
	public Complex subtract(Complex c) {
		re -= c.re;
		im -= c.im;
		return this;
	}
	
	public Complex multiply(Complex c) {
		double real = re*c.re - im*c.im;
		double imag = re*c.im + im*c.re;
		set(real, imag);
		return this;
	}
	
	public Complex multiply(double a) {
		re *= a;
		im *= a;
		return this;
	}
	
	public Complex reciprocal() {
		double scale = absSq();
		re /= scale;
		im /= -scale;
		return this;
	}
	
	public Complex divide(Complex c) {
		double scale = c.absSq();
		double real = (re*c.re + im*c.im)/scale;
		double imag = (im*c.re - re*c.im)/scale;
		set(real, imag);
		return this;
	}
	
	public Complex divide(double a) {
		re /= a;
		im /= a;
		return this;
	}
	
	public Complex exp() {
		double real = Math.exp(re)*Math.cos(im);
		double imag = Math.exp(re)*Math.sin(im);
		set(real, imag);
		return this;
		
	}
	
	public Complex sin() {
		double real = Math.sin(re)*Math.cosh(im);
		double imag = Math.cos(re)*Math.sinh(im);
		set(real, imag);
		return this;
	}
	
	public Complex cos() {
		double real = Math.cos(re)*Math.cosh(im);
		double imag = -Math.sin(re)*Math.sinh(im);
		set(real, imag);
		return this;
	}
	
	public Complex tan() {
		double real = Math.sin(re)*Math.cosh(im)/(Math.cos(re)*Math.cosh(im));
		double imag = Math.cos(re)*Math.sinh(im)/(-Math.sin(re)*Math.sinh(im));
		set(real, imag);
		return this;
	}
	
	public Complex normalize() {
		double scale = abs();
		re /= scale;
		im /= scale;
		return this;
	}
	
	@Override
	public String toString() {
		if (im == 0) return re + "";
		if (re == 0) return im + "i";
		if (im < 0) return re + " - " + (-im) + "i";
		return re + " + " + im + "i";
	}
	
	// Static
	
	public static Complex _0() {
		return new Complex(0, 0);
	}
	
	public static Complex _1() {
		return new Complex(1, 0);
	}
	
	public static Complex _I() {
		return new Complex(0, 1);
	}
	
	public static Complex _m1() {
		return new Complex(-1, 0);
	}
	
	public static Complex _mI() {
		return new Complex(0, -1);
	}
	
	public static Complex phase(double phi) {
		return new Complex(Math.cos(phi), Math.sin(phi));
	}
	
	public static Complex phase_d(double phi) {
		return new Complex(NCMath.cos_d(phi), NCMath.sin_d(phi));
	}
}
