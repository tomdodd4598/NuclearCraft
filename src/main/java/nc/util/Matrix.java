package nc.util;

import java.util.Arrays;

import net.minecraft.nbt.NBTTagCompound;

/** Only handles square matrices! */
public class Matrix {
	
	public final int dim;
	public final double[][] re;
	public final double[][] im;
	
	public Matrix(int dim) {
		this.dim = dim;
		re = new double[dim][dim];
		im = new double[dim][dim];
	}
	
	public Matrix(double[][] c) {
		this(c.length);
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				re[i][j] = c[i][2*j];
				im[i][j] = c[i][2*j+1];
			}
		}
	}
	
	public Matrix copy() {
		Matrix m = new Matrix(dim);
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				m.re[i][j] = re[i][j];
				m.im[i][j] = im[i][j];
			}
		}
		return m;
	}
	
	public Matrix id() {
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				re[i][j] = j == i ? 1D : 0D;
				im[i][j] = 0D;
			}
		}
		return this;
	}
	
	public Matrix zero() {
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				re[i][j] = im[i][j] = 0D;
			}
		}
		return this;
	}
	
	public Matrix add(Matrix m) {
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				re[i][j] += m.re[i][j];
				im[i][j] += m.im[i][j];
			}
		}
		return this;
	}
	
	public Matrix subtract(Matrix m) {
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				re[i][j] -= m.re[i][j];
				im[i][j] -= m.im[i][j];
			}
		}
		return this;
	}
	
	public Matrix hadamardProduct(Matrix m) {
		double[] c;
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				c = Complex.multiply(re[i][j], im[i][j], m.re[i][j], m.im[i][j]);
				re[i][j] = c[0];
				im[i][j] = c[1];
			}
		}
		return this;
	}
	
	public Matrix multiply(double re, double im) {
		double[] c;
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				c = Complex.multiply(this.re[i][j], this.im[i][j], re, im);
				this.re[i][j] = c[0];
				this.im[i][j] = c[1];
			}
		}
		return this;
	}
	
	public Matrix multiply(double a) {
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				this.re[i][j] *= a;
				this.im[i][j] *= a;
			}
		}
		return this;
	}
	
	public Matrix multiply(Matrix m) {
		Matrix copy = copy();
		double[] c;
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				re[i][j] = im[i][j] = 0D;
				for (int k = 0; k < dim; k++) {
					c = Complex.multiply(copy.re[i][k], copy.im[i][k], m.re[k][j], m.im[k][j]);
					re[i][j] += c[0];
					im[i][j] += c[1];
				}
			}
		}
		return this;
	}
	
	public double[] expectation(Vector v) {
		return v.copy().dot(v.map(this));
	}
	
	public double[] trace() {
		double re = 0D, im = 0D;
		for (int i = 0; i < dim; i++) {
			re += this.re[i][i];
			im += this.im[i][i];
		}
		return new double[] {re, im};
	}
	
	public Matrix transpose() {
		double re, im;
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < i; j++) {
				if (i != j) {
					re = this.re[i][j];
					im = this.im[i][j];
					this.re[i][j] = this.re[j][i];
					this.im[i][j] = this.im[j][i];
					this.re[j][i] = re;
					this.im[j][i] = im;
				}
			}
		}
		return this;
	}
	
	public static Matrix tensorProduct(Matrix... a) {
		if (a.length == 0) {
			return new Matrix(0);
		}
		if (a.length == 1) {
			return a[0];
		}
		
		int dim = a[0].dim, mul = 1, _j;
		for (int j = 1; j < a.length; j++) {
			dim *= a[j].dim;
		}
		
		Matrix m = new Matrix(dim);
		double[] c;
		int[] length = new int[a.length], count = new int[2*a.length], mult = new int[a.length];
		for (int j = 0; j < a.length; j++) {
			length[j] = a[j].dim;
			if (j > 0) mul *= a[a.length - j].dim;
			mult[a.length - j - 1] = mul;
		}
		Arrays.fill(count, 0);
		
		boolean end;
		while (true) {
			int u = 0, v = 0;
			for (int j = 0; j < a.length; j++) {
				u += mult[j]*count[2*j];
				v += mult[j]*count[2*j + 1];
			}
			
			c = Complex.multiply(a[0].re[count[0]][count[1]], a[0].im[count[0]][count[1]], a[1].re[count[2]][count[3]], a[1].im[count[2]][count[3]]);
			
			for (int j = 2; j < a.length; j++) {
				c = Complex.multiply(c[0], c[1], a[j].re[count[2*j]][count[2*j + 1]], a[j].im[count[2*j]][count[2*j + 1]]);
			}
			m.re[u][v] = c[0];
			m.im[u][v] = c[1];
			
			end = true;
			for (int j = 0; j < count.length; j++) {
				_j = count.length - j - 1;
				if (count[_j] < length[_j/2] - 1) {
					++count[_j];
					end = false;
					break;
				}
				else {
					count[_j] = 0;
				}
			}
			
			if (end) {
				return m;
			}
		}
	}
	
	/*public static Matrix tensorProduct(Matrix... a) {
		if (a.length == 0) {
			return new Matrix(0);
		}
		if (a.length == 1) {
			return a[0];
		}
		
		Matrix m = new Matrix(a[0].dim*a[1].dim);
		double[] c;
		for (int u = 0; u < a[0].dim; u++) {
			for (int v = 0; v < a[0].dim; v++) {
				for (int i = 0; i < a[1].dim; i++) {
					for (int j = 0; j < a[1].dim; j++) {
						c = Complex.multiply(a[0].re[v][u], a[0].im[v][u], a[1].re[j][i], a[1].im[j][i]);
						m.re[j + a[1].dim*v][i + a[1].dim*u] = c[0];
						m.im[j + a[1].dim*v][i + a[1].dim*u] = c[1];
					}
				}
			}
		}
		
		if (a.length == 2) return m;
		else {
			Matrix[] m_ = new Matrix[a.length - 1];
			m_[0] = m;
			for (int i = 1; i < m_.length; i++) {
				m_[i] = a[i + 1];
			}
			return tensorProduct(m_);
		}
	}*/
	
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
				re[i][j] = re[i][j];
				im[i][j] = -im[i][j];
			}
		}
		return this;
	}
	
	public Matrix hermitian() {
		double re, im;
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < i; j++) {
				if (i != j) {
					re = this.re[i][j];
					im = this.im[i][j];
					this.re[i][j] = this.re[j][i];
					this.im[i][j] = -this.im[j][i];
					this.re[j][i] = re;
					this.im[j][i] = -im;
				}
			}
		}
		return this;
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt, String name) {
		NBTTagCompound matrixTag = new NBTTagCompound();
		matrixTag.setInteger("dim", dim);
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				matrixTag.setDouble("re" + i + "_" + j, re[i][j]);
				matrixTag.setDouble("im" + i + "_" + j, im[i][j]);
			}
		}
		nbt.setTag(name, matrixTag);
		return nbt;
	}
	
	public static Matrix readFromNBT(NBTTagCompound nbt, String name) {
		if (nbt.hasKey(name, 10)) {
			NBTTagCompound matrixTag = nbt.getCompoundTag(name);
			Matrix m = new Matrix(matrixTag.getInteger("dim"));
			for (int i = 0; i < m.dim; i++) {
				for (int j = 0; j < m.dim; j++) {
					m.re[i][j] = matrixTag.getDouble("re" + i + "_" + j);
					m.im[i][j] = matrixTag.getDouble("im" + i + "_" + j);
				}
			}
			return m;
		}
		return null;
	}
}
