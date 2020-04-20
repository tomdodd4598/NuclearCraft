package nc.util;

/** Only handles square matrices! */
public class Matrix {
	
	public final int dim;
	private final Complex[][] elements;
	
	public Matrix(int dim) {
		this.dim = dim;
		elements = new Complex[dim][dim];
	}
	
	public Matrix(Complex[][] elements) {
		this.dim = elements.length;
		this.elements = elements;
	}
	
	public Matrix copy() {
		Matrix m = new Matrix(dim);
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				m.elements[j][i] = get(i, j).copy();
			}
		}
		return m;
	}
	
	public void set(Complex[][] c) {
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				elements[j][i] = c[j][i];
			}
		}
	}
	
	public void set(Matrix m) {
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				elements[j][i] = m.elements[j][i];
			}
		}
	}
	
	public Complex get(int i, int j) {
		if (elements[j][i] == null) {
			elements[j][i] = Complex._0();
		}
		return elements[j][i];
	}
	
	public void set(int i, int j, Complex c) {
		elements[j][i] = c == null ? Complex._0() : c;
	}
	
	public Matrix id() {
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				elements[j][i] = j == i ? Complex._1() : Complex._0();
			}
		}
		return this;
	}
	
	public Matrix zero() {
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				elements[j][i] = Complex._0();
			}
		}
		return this;
	}
	
	public Matrix add(Matrix m) {
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				get(i, j).add(m.get(i, j));
			}
		}
		return this;
	}
	
	public Matrix subtract(Matrix m) {
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				get(i, j).subtract(m.get(i, j));
			}
		}
		return this;
	}
	
	public Matrix hadamardProduct(Matrix m) {
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				get(i, j).multiply(m.get(i, j));
			}
		}
		return this;
	}
	
	public Matrix multiply(Complex c) {
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				get(i, j).multiply(c);
			}
		}
		return this;
	}
	
	public Matrix multiply(double a) {
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				get(i, j).multiply(a);
			}
		}
		return this;
	}
	
	public Matrix multiply(Matrix m) {
		Matrix copy = copy();
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				elements[j][i] = Complex._0();
				for (int k = 0; k < dim; k++) {
					elements[j][i].add(copy.get(i, k).copy().multiply(m.get(k, j)));
				}
			}
		}
		return this;
	}
	
	public Complex expectation(Vector v) {
		return v.copy().dot(v.map(this));
	}
	
	public Complex trace() {
		Complex c = Complex._0();
		for (int i = 0; i < dim; i++) {
			c.add(get(i, i));
		}
		return c;
	}
	
	public Matrix transpose() {
		Complex c;
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				if (i != j) {
					c = elements[i][j];
					elements[i][j] = elements[j][i];
					elements[j][i] = c;
				}
			}
		}
		return this;
	}
	
	/*public static Complex[][] dyad(Complex[] a, Complex[] b) {
		Complex[][] c = new Complex[a.length][a.length];
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a.length; j++) {
				c[j][i] = a[j].copy().multiply(b[i].copy().conj());
			}
		}
		return c;
	}*/
	
	/** Returns new Matrix! */
	public Matrix tensorProduct(Matrix a) {
		Matrix c = new Matrix(dim*a.dim);
		for (int m = 0; m < dim; m++) {
			for (int n = 0; n < dim; n++) {
				for (int i = 0; i < a.dim; i++) {
					for (int j = 0; j < a.dim; j++) {
						c.elements[j + a.dim*n][i + a.dim*m] = get(m, n).copy().multiply(a.get(i, j));
					}
				}
			}
		}
		return c;
	}
	
	/** Returns new Matrix! */
	public Matrix directSum(Matrix a) {
		Matrix m = new Matrix(dim + a.dim);
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				m.elements[j][i] = elements[j][i].copy();
			}
		}
		for (int i = 0; i < a.dim; i++) {
			for (int j = 0; j < a.dim; j++) {
				m.elements[j + a.dim][i + a.dim] = a.elements[j][i].copy();
			}
		}
		return m;
	}
	
	public Matrix commute(Matrix a) {
		Matrix c = copy();
		return multiply(a).subtract(a.copy().multiply(c));
	}
	
	public Matrix square() {
		return multiply(copy());
	}
	
	public Matrix conjugate() {
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				elements[j][i] = get(i, j).conj();
			}
		}
		return this;
	}
	
	public Matrix hermitian() {
		return conjugate().transpose();
	}
	
	// General Spin Matrices
	/*
	public static int dim(double s) {
		return (int) Math.round(2*s + 1);
	}
	
	public static double rPlus(double s, double m) {
		return Math.sqrt(s*(s + 1) - m*(m + 1));
	}
	
	public static double rMinus(double s, double m) {
		return Math.sqrt(s*(s + 1) - m*(m - 1));
	}
	
	public static Complex[][] spinSquared(double s) {
		Complex[][] c = id(dim(s));
		return multiply(s*(s + 1), c);
	}
	
	public static Complex[][] spinZ(double s) {
		Complex[][] c = new Complex[dim(s)][dim(s)];
		for (int i = 0; i < dim(s); i++) {
			for (int j = 0; j < dim(s); j++) {
				c[j][i] = new Complex(NCMath.kroneckerDelta(j, i)*(s - i), 0);
			}
		}
		return c;
	}
	
	public static Complex[][] spinPlus(double s) {
		Complex[][] c = new Complex[dim(s)][dim(s)];
		for (int i = 0; i < dim(s); i++) {
			for (int j = 0; j < dim(s); j++) {
				if (i >= 0 && j >= 0) c[j][i] = new Complex(NCMath.kroneckerDelta(j, i - 1)*rPlus(s, s - j - 1), 0);
			}
		}
		return c;
	}
	
	public static Complex[][] spinMinus(double s) {
		Complex[][] c = new Complex[dim(s)][dim(s)];
		for (int i = 0; i < dim(s); i++) {
			for (int j = 0; j < dim(s); j++) {
				if (i >= 0 && j >= 0) c[j][i] = new Complex(NCMath.kroneckerDelta(j - 1, i)*rMinus(s, s - i), 0);
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
		return add(multiply(NCMath.cos_d(t), spinZ(s)), add(multiply(NCMath.sin_d(t)*NCMath.cos_d(p), spinX(s)), multiply(NCMath.sin_d(t)*NCMath.sin_d(p), spinY(s))));
	}
	
	public static Complex[][] projection(Complex[] a) {
		Complex[] b = normalize(a);
		return dyad(b, b);
	}
	
	public static double probability(Complex[] u, Complex[] v) {
		return expectation(u, projection(v)).abs();
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
		return tensorProduct(id(x), tensorProduct(spinZ(s[a - 1]), id(y)));
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
		return tensorProduct(id(x), tensorProduct(spinX(s[a - 1]), id(y)));
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
		return tensorProduct(id(x), tensorProduct(spinY(s[a - 1]), id(y)));
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
		return tensorProduct(id(x), tensorProduct(spinW(s[a - 1], t, p), id(y)));
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
		
		return tensorProduct(id(x), tensorProduct(p, id(y)));
	}
	
	public static Complex[][] projection(Complex[] v, int a, double... spin) {
		return projection(projection(v), a, spin);
	}
	*/
}
