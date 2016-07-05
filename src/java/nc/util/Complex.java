package nc.util;

public class Complex {
    private final double re;
    private final double im;

    public Complex(double real, double imag) {
        re = real;
        im = imag;
    }

    public String toString() {
        if (im == 0) return re + "";
        if (re == 0) return im + "i";
        if (im <  0) return re + " - " + (-im) + "i";
        return re + " + " + im + "i";
    }

    public double abs() {
    	return Math.hypot(re, im);
    }
    
    public double arg() {
    	return Math.atan2(im, re);
    }

    public Complex add(Complex b) {
        Complex a = this;
        double real = a.re + b.re;
        double imag = a.im + b.im;
        return new Complex(real, imag);
    }

    public Complex subtract(Complex b) {
        Complex a = this;
        double real = a.re - b.re;
        double imag = a.im - b.im;
        return new Complex(real, imag);
    }

    public Complex multiply(Complex b) {
        Complex a = this;
        double real = a.re * b.re - a.im * b.im;
        double imag = a.re * b.im + a.im * b.re;
        return new Complex(real, imag);
    }

    public Complex multiply(double alpha) {
        return new Complex(alpha * re, alpha * im);
    }

    public Complex conjugate() {
    	return new Complex(re, -im);
    }

    public Complex reciprocal() {
        double scale = re*re + im*im;
        return new Complex(re / scale, -im / scale);
    }

    public double re() {
    	return re;
    }
    public double im() {
    	return im;
    }

    public Complex divide(Complex b) {
        Complex a = this;
        return a.multiply(b.reciprocal());
    }

    public Complex exp() {
        return new Complex(Math.exp(re) * Math.cos(im), Math.exp(re) * Math.sin(im));
    }

    public Complex sin() {
        return new Complex(Math.sin(re) * Math.cosh(im), Math.cos(re) * Math.sinh(im));
    }

    public Complex cos() {
        return new Complex(Math.cos(re) * Math.cosh(im), -Math.sin(re) * Math.sinh(im));
    }

    public Complex tan() {
        return sin().divide(cos());
    }
    
    // Static Methods
    
    public static String toString(Complex a) {
        if (a.im == 0) return a.re + "";
        if (a.re == 0) return a.im + "i";
        if (a.im <  0) return a.re + " - " + (-a.im) + "i";
        return a.re + " + " + a.im + "i";
    }
    
    public static double abs(Complex a) {
    	return Math.hypot(a.re, a.im);
    }
    
    public static double absSq(Complex a) {
    	return a.re*a.re + a.im*a.im;
    }
    
    public static double arg(Complex a) {
    	return Math.atan2(a.im, a.re);
    }
    
    public static Complex add(Complex a, Complex b) {
        double x = a.re + b.re;
        double y = a.im + b.im;
        return new Complex(x, y);
    }
    
    public static Complex subtract(Complex a, Complex b) {
        double x = a.re - b.re;
        double y = a.im - b.im;
        return new Complex(x, y);
    }

    public static Complex multiply(Complex a, Complex b) {
        double x = a.re * b.re - a.im * b.im;
        double y = a.re * b.im + a.im * b.re;
        return new Complex(x, y);
    }
    
    public static Complex multiply(double a, Complex b) {
        return new Complex(a * b.re, a * b.im);
    }
    
    public static Complex divide(Complex a, double b) {
        return new Complex(a.re / b, a.im / b);
    }

    public static Complex conjugate(Complex a) {
    	return new Complex(a.re, -a.im);
    }

    public static Complex reciprocal(Complex a) {
        double scale = a.re*a.re + a.im*a.im;
        return new Complex(a.re / scale, -a.im / scale);
    }

    public static double re(Complex a) {
    	return a.re;
    }
    public static double im(Complex a) {
    	return a.im;
    }
    
    public static Complex divide(Complex a, Complex b) {
        return a.multiply(b.reciprocal());
    }

    public static Complex exp(Complex a) {
        return new Complex(Math.exp(a.re) * Math.cos(a.im), Math.exp(a.re) * Math.sin(a.im));
    }

    public static Complex sin(Complex a) {
        return new Complex(Math.sin(a.re) * Math.cosh(a.im), Math.cos(a.re) * Math.sinh(a.im));
    }

    public static Complex cos(Complex a) {
        return new Complex(Math.cos(a.re) * Math.cosh(a.im), -Math.sin(a.re) * Math.sinh(a.im));
    }

    public static Complex tan(Complex a) {
        return a.sin().divide(a.cos());
    }
}