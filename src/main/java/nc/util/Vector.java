package nc.util;

import net.minecraft.nbt.NBTTagCompound;

public class Vector {
	
	public final int dim;
	public final double[] re;
	public final double[] im;
	
	public Vector(int dim) {
		this.dim = dim;
		re = new double[dim];
		im = new double[dim];
	}
	
	public Vector copy() {
		Vector v = new Vector(dim);
		for (int i = 0; i < dim; i++) {
			v.re[i] = re[i];
			v.im[i] = im[i];
		}
		return v;
	}
	
	public void zero() {
		for (int i = 0; i < dim; i++) {
			this.re[i] = 0D;
			this.im[i] = 0D;
		}
	}
	
	public Vector map(Matrix m) {
		Vector v = copy();
		double[] c;
		for (int i = 0; i < dim; i++) {
			re[i] = 0D;
			im[i] = 0D;
			for (int j = 0; j < dim; j++) {
				c = Complex.multiply(m.re[i][j], m.im[i][j], v.re[j], v.im[j]);
				re[i] += c[0];
				im[i] += c[1];
			}
		}
		return this;
	}
	
	public double absSq() {
		double n = 0D;
		for (int i = 0; i < dim; i++) {
			n += Complex.absSq(re[i], im[i]);
		}
		return n;
	}
	
	public Vector normalize() {
		double scale = Math.sqrt(absSq());
		for (int i = 0; i < dim; i++) {
			re[i] /= scale;
			im[i] /= scale;
		}
		return this;
	}
	
	public double[] dot(Vector v) {
		double re = 0D, im = 0D;
		double[] c;
		for (int i = 0; i < dim; i++) {
			c = Complex.multiply(this.re[i], -this.im[i], v.re[i], v.im[i]);
			re += c[0];
			im += c[1];
		}
		return new double[] {re, im};
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt, String name) {
		NBTTagCompound vectorTag = new NBTTagCompound();
		vectorTag.setInteger("dim", dim);
		for (int i = 0; i < dim; i++) {
			vectorTag.setDouble("re" + i, re[i]);
			vectorTag.setDouble("im" + i, im[i]);
		}
		nbt.setTag(name, vectorTag);
		return nbt;
	}
	
	public static Vector readFromNBT(NBTTagCompound nbt, String name) {
		if (nbt.hasKey(name, 10)) {
			NBTTagCompound vectorTag = nbt.getCompoundTag(name);
			Vector v = new Vector(vectorTag.getInteger("dim"));
			for (int i = 0; i < v.dim; i++) {
				v.re[i] = vectorTag.getDouble("re" + i);
				v.im[i] = vectorTag.getDouble("im" + i);
			}
			return v;
		}
		return new Vector(0);
	}
}
