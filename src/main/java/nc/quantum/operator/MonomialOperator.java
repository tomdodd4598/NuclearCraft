package nc.quantum.operator;

public class MonomialOperator extends Operator {

    protected final double[] elems;
    protected final int[] offsets;

    public MonomialOperator(double[] elems, int[] offsets) {
        super(Integer.numberOfTrailingZeros(elems.length) - 1);
        this.elems = elems;
        this.offsets = offsets;
    }

    @Override
    public void partialMap(double[] source, double[] target, int start, int[] window) {
        for (int i = 0; i < dim; ++i) {
            int x = start + window[i], y = start + window[offsets[i]], z = i << 1;
            double a = source[y], b = source[y + 1], c = elems[z], d = elems[z + 1];
            target[x] = a * c - b * d;
            target[x + 1] = a * d + b * c;
        }
    }

    @Override
    public Operator rebased(int[] basis) {
        int[] perm = perm(basis), invPerm = new int[dim];
        for (int i = 0; i < dim; ++i) {
            invPerm[perm[i]] = i;
        }

        double[] elems = new double[dim << 1];
        int[] offsets = new int[dim];
        for (int i = 0; i < dim; ++i) {
            int x = perm[i] << 1, y = i << 1;
            elems[y] = this.elems[x];
            elems[y + 1] = this.elems[x + 1];
            offsets[i] = invPerm[this.offsets[perm[i]]];
        }
        return new MonomialOperator(elems, offsets);
    }
}
