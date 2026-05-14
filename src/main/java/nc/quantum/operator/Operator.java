package nc.quantum.operator;

import nc.quantum.Matrix;

public abstract class Operator {

    public final int size, dim;

    protected Operator(int size) {
        this.size = size;
        this.dim = 1 << size;
    }

    protected int[] perm(int[] basis) {
        int[] perm = new int[dim];
        for (int i = 0; i < dim; ++i) {
            perm[i] = Matrix.changeIntBasis(i, basis);
        }
        return perm;
    }

    public abstract void partialMap(double[] source, double[] target, int start, int[] window);

    public abstract Operator rebased(int[] basis);

    public static Operator phase(double angle) {
        return new DiagonalOperator(new double[] {
                1, 0,
                Math.cos(angle), Math.sin(angle),
        });
    }

    public static Operator rotateX(double angle) {
        double half = 0.5 * angle, c = Math.cos(half), s = Math.sin(half);
        return new DenseOperator(new double[] {
                c, 0, 0, -s,
                0, -s, c, 0,
        });
    }

    public static Operator rotateY(double angle) {
        double half = 0.5 * angle, c = Math.cos(half), s = Math.sin(half);
        return new DenseOperator(new double[] {
                c, 0, -s, 0,
                s, 0, c, 0,
        });
    }

    public static Operator rotateZ(double angle) {
        double half = 0.5 * angle, c = Math.cos(half), s = Math.sin(half);
        return new DiagonalOperator(new double[] {
                c, -s,
                c, s,
        });
    }
}
