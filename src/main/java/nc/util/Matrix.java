package nc.util;

import java.util.Arrays;

import it.unimi.dsi.fastutil.HashCommon;
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
				re[i][j] = c[i][2 * j];
				im[i][j] = c[i][2 * j + 1];
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
				re[i][j] *= a;
				im[i][j] *= a;
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
	
	public void swap(int i1, int j1, int i2, int j2) {
		double re = this.re[i1][j1];
		double im = this.im[i1][j1];
		this.re[i1][j1] = this.re[i2][j2];
		this.im[i1][j1] = this.im[i2][j2];
		this.re[i2][j2] = re;
		this.im[i2][j2] = im;
	}
	
	/** Modified from Srikanth A's answer at https://stackoverflow.com/a/45951007 */
	public double[] det() {
		if (dim == 0) {
			return new double[] {1D, 0D};
		}
		else if (dim == 1) {
			return new double[] {re[0][0], im[0][0]};
		}
		else if (dim == 2) {
			double[] ad = Complex.multiply(re[0][0], im[0][0], re[1][1], im[1][1]);
			double[] bc = Complex.multiply(re[0][1], im[0][1], re[1][0], im[1][0]);
			return new double[] {ad[0] - bc[0], ad[1] - bc[1]};
		}
		
		double re1, im1, re2, im2;
		int index;
		
		double[] det = new double[] {1D, 0D}, total = new double[] {1D, 0D};
		double[] tempRe = new double[dim + 1], tempIm = new double[dim + 1];
		
		Matrix m = copy();
		
		for (int i = 0; i < dim; i++) {
			index = i;
			
			while (index < dim && m.re[index][i] == 0D && m.im[index][i] == 0D) {
				index++;
			}
			
			if (index == dim) {
				continue;
			}
			
			if (index != i) {
				for (int j = 0; j < dim; j++) {
					m.swap(index, j, i, j);
				}
				det = Complex.multiply(det[0], det[1], -1D, 0D);
			}
			
			for (int j = 0; j < dim; j++) {
				tempRe[j] = m.re[i][j];
				tempIm[j] = m.im[i][j];
			}
			
			for (int j = i + 1; j < dim; j++) {
				re1 = tempRe[i];
				im1 = tempIm[i];
				re2 = m.re[j][i];
				im2 = m.im[j][i];
				
				for (int k = 0; k < dim; k++) {
					double[] ad = Complex.multiply(re1, im1, m.re[j][k], m.im[j][k]);
					double[] bc = Complex.multiply(re2, im2, tempRe[k], tempIm[k]);
					m.re[j][k] = ad[0] - bc[0];
					m.im[j][k] = ad[1] - bc[1];
				}
				total = Complex.multiply(total[0], total[1], re1, im1);
			}
		}
		
		for (int i = 0; i < dim; i++) {
			det = Complex.multiply(det[0], det[1], m.re[i][i], m.im[i][i]);
		}
		
		return Complex.divide(det[0], det[1], total[0], total[1]);
	}
	
	public Matrix transpose() {
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < i; j++) {
				if (i != j) {
					swap(i, j, j, i);
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
		int[] length = new int[a.length], count = new int[2 * a.length], mult = new int[a.length];
		for (int j = 0; j < a.length; j++) {
			length[j] = a[j].dim;
			if (j > 0) {
				mul *= a[a.length - j].dim;
			}
			mult[a.length - j - 1] = mul;
		}
		Arrays.fill(count, 0);
		
		boolean end;
		while (true) {
			int u = 0, v = 0;
			for (int j = 0; j < a.length; j++) {
				u += mult[j] * count[2 * j];
				v += mult[j] * count[2 * j + 1];
			}
			
			c = Complex.multiply(a[0].re[count[0]][count[1]], a[0].im[count[0]][count[1]], a[1].re[count[2]][count[3]], a[1].im[count[2]][count[3]]);
			
			for (int j = 2; j < a.length; j++) {
				c = Complex.multiply(c[0], c[1], a[j].re[count[2 * j]][count[2 * j + 1]], a[j].im[count[2 * j]][count[2 * j + 1]]);
			}
			m.re[u][v] = c[0];
			m.im[u][v] = c[1];
			
			end = true;
			for (int j = 0; j < count.length; j++) {
				_j = count.length - j - 1;
				if (count[_j] < length[_j / 2] - 1) {
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
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < i; j++) {
				if (i != j) {
					swap(i, j, j, i);
				}
			}
		}
		return this;
	}
	
	@Override
	public int hashCode() {
		int h = 1;
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				h = 31 * h + HashCommon.double2int(re[i][j]);
				h = 31 * h + HashCommon.double2int(im[i][j]);
			}
		}
		return h;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (!(obj instanceof Matrix)) {
			return false;
		}
		
		Matrix other = (Matrix) obj;
		
		if (dim != other.dim) {
			return false;
		}
		
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				if (re[i][j] != other.re[i][j]) {
					return false;
				}
				if (im[i][j] != other.im[i][j]) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	@Override
	public String toString() {
		String s = "", v;
		for (int i = 0; i < dim; i++) {
			v = "";
			for (int j = 0; j < dim; j++) {
				v = v + ", " + Complex.toString(re[i][j], im[i][j]);
			}
			s = s + ", [" + v.substring(2) + "]";
		}
		return "[" + s.substring(2) + "]";
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
