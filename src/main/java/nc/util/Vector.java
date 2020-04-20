package nc.util;

import net.minecraft.nbt.NBTTagCompound;

public class Vector {
	
	public final int dim;
	private final Complex[] components;
	
	public Vector(int dim) {
		this.dim = dim;
		components = new Complex[dim];
	}
	
	public Vector(Complex[] components) {
		dim = components.length;
		this.components = components;
	}
	
	public Vector copy() {
		Vector v = new Vector(dim);
		for (int i = 0; i < dim; i++) {
			v.components[i] = components[i];
		}
		return v;
	}
	
	public Complex get(int i) {
		if (components[i] == null) {
			components[i] = Complex._0();
		}
		return components[i];
	}
	
	public void set(int i, Complex c) {
		components[i] = c == null ? Complex._0() : c;
	}
	
	public Vector zero() {
		for (int i = 0; i < dim; i++) {
			components[i] = Complex._0();
		}
		return this;
	}
	
	public Vector map(Matrix m) {
		Vector copy = copy();
		for (int i = 0; i < dim; i++) {
			components[i] = Complex._0();
			for (int j = 0; j < dim; j++) {
				components[i].add(m.get(j, i).copy().multiply(copy.get(j)));
			}
		}
		return this;
	}
	
	public double absSq() {
		double n = 0;
		for (int i = 0; i < dim; i++) {
			n += get(i).absSq();
		}
		return n;
	}
	
	public Vector normalize() {
		double scale = Math.sqrt(absSq());
		for (int i = 0; i < dim; i++) {
			get(i).divide(scale);
		}
		return this;
	}
	
	public Complex dot(Vector v) {
		Complex c = Complex._0();
		for (int i = 0; i < dim; i++) {
			c.add(get(i).copy().conj().multiply(v.get(i)));
		}
		return c;
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt, String name) {
		NBTTagCompound vectorTag = new NBTTagCompound();
		vectorTag.setInteger("dim", dim);
		for (int i = 0; i < dim; i++) {
			vectorTag.setDouble("componentRe" + i, get(i).re());
			vectorTag.setDouble("componentIm" + i, get(i).im());
		}
		nbt.setTag(name, vectorTag);
		return nbt;
	}
	
	public static Vector readFromNBT(NBTTagCompound nbt, String name) {
		if (nbt.hasKey(name, 10)) {
			NBTTagCompound vectorTag = nbt.getCompoundTag(name);
			Vector v = new Vector(vectorTag.getInteger("dim"));
			for (int i = 0; i < v.dim; i++) {
				v.set(i, new Complex(vectorTag.getDouble("componentRe" + i), vectorTag.getDouble("componentIm" + i)));
			}
			return v;
		}
		return new Vector(0);
	}
}
