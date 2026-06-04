package nc.quantum.operator;

public class DiagonalOperator extends Operator {
	
	protected final double[] elems;
	
	public DiagonalOperator(double[] elems) {
		super(Integer.numberOfTrailingZeros(elems.length) - 1);
		this.elems = elems;
	}
	
	@Override
	public void partialMap(double[] source, double[] target, int start, int[] window) {
		for (int i = 0; i < dim; ++i) {
			int x = start + window[i], y = i << 1;
			double a = source[x], b = source[x + 1], c = elems[y], d = elems[y + 1];
			target[x] = a * c - b * d;
			target[x + 1] = a * d + b * c;
		}
	}
	
	@Override
	public Operator rebased(int[] basis) {
		int[] perm = perm(basis);
		double[] elems = new double[dim << 1];
		for (int i = 0; i < dim; ++i) {
			int x = perm[i] << 1, y = i << 1;
			elems[y] = this.elems[x];
			elems[y + 1] = this.elems[x + 1];
		}
		return new DiagonalOperator(elems);
	}
}
