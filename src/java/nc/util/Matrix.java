package nc.util;

public class Matrix {
	
	public static int delta(int a, int b) {
		return a == b ? 1 : 0;
	}
	
	public static Complex[][] I(int n) {
		Complex[][] c = new Complex[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				c[j][i] = new Complex(delta(j, i), 0);
			}
		}
		return c;
	}
	
	public static Complex[][] zero(int n) {
		Complex[][] c = new Complex[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				c[j][i] = new Complex(0, 0);
			}
		}
		return c;
	}
	
	public static Complex[][] add(Complex[][] a, Complex[][] b) {
		Complex[][] c = new Complex[a[0].length][a[0].length];
		for (int i = 0; i < a[0].length; i++) {
			for (int j = 0; j < a[0].length; j++) {
				c[j][i] = Complex.add(a[j][i], b[j][i]);
			}
		}
		return c;
	}
	
	public static Complex[][] subtract(Complex[][] a, Complex[][] b) {
		Complex[][] c = new Complex[a[0].length][a[0].length];
		for (int i = 0; i < a[0].length; i++) {
			for (int j = 0; j < a[0].length; j++) {
				c[j][i] = Complex.subtract(a[j][i], b[j][i]);
			}
		}
		return c;
	}
	
	public static Complex[][] hadamard(Complex[][] a, Complex[][] b) {
		Complex[][] c = new Complex[a[0].length][a[0].length];
		for (int i = 0; i < a[0].length; i++) {
			for (int j = 0; j < a[0].length; j++) {
				c[j][i] = Complex.multiply(a[j][i], b[j][i]);
			}
		}
		return c;
	}
	
	public static Complex[][] multiply(Complex a, Complex[][] b) {
		Complex[][] c = new Complex[b[0].length][b[0].length];
		for (int i = 0; i < b[0].length; i++) {
			for (int j = 0; j < b[0].length; j++) {
				c[j][i] = Complex.multiply(a, b[j][i]);
			}
		}
		return c;
	}
	
	public static Complex[][] multiply(double a, Complex[][] b) {
		return multiply(new Complex(a, 0), b);
	}
	
	public static Complex[][] multiply(Complex[][] a, Complex[][] b) {
		Complex[][] c = new Complex[a[0].length][a[0].length];
		for (int i = 0; i < a[0].length; i++) {
			for (int j = 0; j < a[0].length; j++) {
				c[j][i] = new Complex(0, 0);
				for (int k = 0; k < a[0].length; k++) {
					c[j][i].add(Complex.multiply(a[k][i], b[j][k]));
				}
			}
		}
		return c;
	}
	
	public static Complex[] transform(Complex[] a, Complex[][] b) {
		Complex[] c = new Complex[a.length];
		for (int i = 0; i < a.length; i++) {
			c[i] = new Complex(0, 0);
			for (int j = 0; j < a.length; j++) {
				c[i].add(Complex.multiply(b[i][j], a[j]));
			}
		}
		return c;
	}
	
	public static Complex[] normalise(Complex[] a) {
		Complex[] b = new Complex[a.length];
		double n = 0;
		for (int i = 0; i < a.length; i++) {
			n += Complex.absSq(a[i]);
		}
		for (int i = 0; i < a.length; i++) {
			b[i] = Complex.divide(a[i], Math.sqrt(n));
		}
		return b;
	}
	
	public static Complex dot(Complex[] a, Complex[] b) {
		Complex c = new Complex(0, 0);
		for (int i = 0; i < a.length; i++) {
			c.add(Complex.multiply(a[i].conjugate(), b[i]));
		}
		return c;
	}
	
	public static Complex expectation(Complex[] a, Complex[][] b) {
		return dot(a, transform(a, b));
	}
	
	public static Complex trace(Complex[][] a) {
		Complex b = new Complex(0, 0);
		for (int i = 0; i < a.length; i++) {
			b.add(a[i][i]);
		}
		return b;
	}
	
	public static Complex[][] transpose(Complex[][] a) {
		Complex[][] b = new Complex[a[0].length][a[0].length];
		for (int i = 0; i < a[0].length; i++) {
			for (int j = 0; j < a[0].length; j++) {
				b[j][i] = a[i][j];
			}
		}
		return b;
	}
	
	public static Complex[][] dyad(Complex[] a, Complex[] b) {
		Complex[][] c = new Complex[a.length][a.length];
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a.length; j++) {
				c[j][i] = Complex.multiply(a[j], b[i].conjugate());
			}
		}
		return c;
	}
	
	public static Complex[][] tensorProduct(Complex[][] a, Complex[][] b) {
		Complex[][] c = new Complex[a[0].length*b[0].length][a[0].length*b[0].length];
		for (int m = 0; m < a[0].length; m++) {
			for (int n = 0; n < a[0].length; n++) {
				for (int i = 0; i < b[0].length; i++) {
					for (int j = 0; j < b[0].length; j++) {
						c[j + b[0].length*n][i + b[0].length*m] = Complex.multiply(a[n][m], b[j][i]);
					}
				}
			}
		}
		return c;
	}
	
	public static Complex[][] commutator(Complex[][] a, Complex[][] b) {
		return subtract(multiply(a, b), multiply(b, a));
	}
	
	public static Complex[][] square(Complex[][] a) {
		return multiply(a, a);
	}
	
	public static Complex[][] conjugate(Complex[][] a) {
		Complex[][] b = new Complex[a[0].length][a[0].length];
		for (int i = 0; i < a[0].length; i++) {
			for (int j = 0; j < a[0].length; j++) {
				b[j][i] = a[j][i].conjugate();
			}
		}
		return b;
	}
	
	public static Complex[][] hermitian(Complex[][] a) {
		return conjugate(transpose(a));
	}
	
	// Spin Matrices
	
	public static int dim(double s) {
		return (int) (2*s + 1);
	}
	
	public static double rPlus(double s, double m) {
		return Math.sqrt(s*(s + 1) - m*(m + 1));
	}
	
	public static double rMinus(double s, double m) {
		return Math.sqrt(s*(s + 1) - m*(m - 1));
	}
	
	public static Complex[][] spinSquared(double s) {
		Complex[][] c = I(dim(s));
		return multiply(s*(s + 1), c);
	}
	
	public static Complex[][] spinZ(double s) {
		Complex[][] c = new Complex[dim(s)][dim(s)];
		for (int i = 0; i < dim(s); i++) {
			for (int j = 0; j < dim(s); j++) {
				c[j][i] = new Complex(delta(j, i)*(s - i), 0);
			}
		}
		return c;
	}
	
	public static Complex[][] spinPlus(double s) {
		Complex[][] c = new Complex[dim(s)][dim(s)];
		for (int i = 0; i < dim(s); i++) {
			for (int j = 0; j < dim(s); j++) {
				if (i >= 0 && j >= 0) c[j][i] = new Complex(delta(j, i - 1)*rPlus(s, s - j - 1), 0);
			}
		}
		return c;
	}
	
	public static Complex[][] spinMinus(double s) {
		Complex[][] c = new Complex[dim(s)][dim(s)];
		for (int i = 0; i < dim(s); i++) {
			for (int j = 0; j < dim(s); j++) {
				if (i >= 0 && j >= 0) c[j][i] = new Complex(delta(j - 1, i)*rMinus(s, s - i), 0);
			}
		}
		return c;
	}
	
	public static Complex[][] spinX(double s) {
		return multiply(0.5, add(spinPlus(s), spinMinus(s)));
	}
	
	public static Complex[][] spinY(double s) {
		return multiply(new Complex(0, 0.5), subtract(spinMinus(s), spinPlus(s)));
	}
	
	public static Complex[][] spinW(double s, double t, double p) {
		return add(multiply(Math.cos(t*Math.PI/180), spinZ(s)), add(multiply(Math.sin(t*Math.PI/180)*Math.cos(p*Math.PI/180), spinX(s)), multiply(Math.sin(t*Math.PI/180)*Math.sin(p*Math.PI/180), spinY(s))));
	}
	
	public static Complex[][] projection(Complex[] a) {
		Complex[] b = normalise(a);
		return dyad(b, b);
	}
	
	public static Complex[][] spinZ(int a, double... spin) {
		double[] s = new double[spin.length];
		for (int i = 0; i < spin.length; i++) {
			s[i] = spin[i];
		}
		int x = 1;
		int y = 1;
		for (int i = 1; i <= a - 1; i++) {
			x *= dim(s[i - 1]);
		}
		for (int i = a + 1; i <= spin.length; i++) {
			y *= dim(s[i - 1]);
		}
		return tensorProduct(I(x), tensorProduct(spinZ(s[a - 1]), I(y)));
	}
	
	public static Complex[][] spinX(int a, double... spin) {
		double[] s = new double[spin.length];
		for (int i = 0; i < spin.length; i++) {
			s[i] = spin[i];
		}
		int x = 1;
		int y = 1;
		for (int i = 1; i <= a - 1; i++) {
			x *= dim(s[i - 1]);
		}
		for (int i = a + 1; i <= spin.length; i++) {
			y *= dim(s[i - 1]);
		}
		return tensorProduct(I(x), tensorProduct(spinX(s[a - 1]), I(y)));
	}
	
	public static Complex[][] spinY(int a, double... spin) {
		double[] s = new double[spin.length];
		for (int i = 0; i < spin.length; i++) {
			s[i] = spin[i];
		}
		int x = 1;
		int y = 1;
		for (int i = 1; i <= a - 1; i++) {
			x *= dim(s[i - 1]);
		}
		for (int i = a + 1; i <= spin.length; i++) {
			y *= dim(s[i - 1]);
		}
		return tensorProduct(I(x), tensorProduct(spinY(s[a - 1]), I(y)));
	}
	
	public static Complex[][] spinW(int a, double t, double p, double... spin) {
		double[] s = new double[spin.length];
		for (int i = 0; i < spin.length; i++) {
			s[i] = spin[i];
		}
		int x = 1;
		int y = 1;
		for (int i = 1; i <= a - 1; i++) {
			x *= dim(s[i - 1]);
		}
		for (int i = a + 1; i <= spin.length; i++) {
			y *= dim(s[i - 1]);
		}
		return tensorProduct(I(x), tensorProduct(spinW(s[a - 1], t, p), I(y)));
	}
	
	public static int dim(double... spin) {
		double[] s = new double[spin.length];
		for (int i = 0; i < spin.length; i++) {
			s[i] = spin[i];
		}
		int x = 1;
		for (int i = 1; i <= spin.length; i++) {
			x *= dim(s[i - 1]);
		}
		return x;
	}
	
	public static Complex[][] twoSpinInteraction(int a, int b, double... spin) {
		return add(multiply(spinZ(a, spin), spinZ(b, spin)), add(multiply(spinX(a, spin), spinX(b, spin)), multiply(spinY(a, spin), spinY(b, spin))));
	}
	
	public static Complex[][] totalSpinSquared(double... spin) {
		double[] s = new double[spin.length];
		for (int i = 0; i < spin.length; i++) {
			s[i] = spin[i];
		}
		Complex[][] c = zero(dim(s));
		for (int i = 1; i <= s.length; i++) {
			for (int j = 1; j <= s.length; j++) {
				c = add(c, twoSpinInteraction(i, j, spin));
			}
		}
		return c;
	}
	
	public static Complex[][] totalSpinHamiltonian(double... spin) {
		double[] s = new double[spin.length];
		for (int i = 0; i < spin.length; i++) {
			s[i] = spin[i];
		}
		Complex[][] c = zero(dim(s));
		for (int i = 1; i <= s.length; i++) {
			for (int j = 1; j <= s.length; j++) {
				if (i != j) c = add(c, twoSpinInteraction(i, j, spin));
			}
		}
		return Matrix.multiply(new Complex(0.5, 0), c);
	}
	
	public static Complex[][] projection(Complex[][] p, int a, double... spin) {
		double[] s = new double[spin.length];
		for (int i = 0; i < spin.length; i++) {
			s[i] = spin[i];
		}
		
		if (spin.length == 1) return p;
		
		int x = 1;
		int y = 1;
		for (int i = 1; i <= a - 1; i++) {
			x *= dim(s[i - 1]);
		}
		for (int i = a + 1; i <= spin.length; i++) {
			y *= dim(s[i - 1]);
		}
		
		return tensorProduct(I(x), tensorProduct(p, I(y)));
	}
	
	public static Complex[][] projection(Complex[] v, int a, double... spin) {
		return projection(projection(v), a, spin);
	}
}
