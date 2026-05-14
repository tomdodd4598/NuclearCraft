package nc.quantum.operator;

import nc.quantum.Matrix;

import java.util.Arrays;

public class DenseOperator extends Operator {

    protected final double[] matrix;

    public DenseOperator(double[] matrix) {
        super(Integer.numberOfTrailingZeros(matrix.length) >> 1);
        this.matrix = matrix;
    }

    @Override
    public void partialMap(double[] source, double[] target, int start, int[] window) {
        if (size == 1) {
            int x0 = start + window[0], x1 = start + window[1];
            double a0 = source[x0], b0 = source[x0 + 1], a1 = source[x1], b1 = source[x1 + 1];
            double c00 = matrix[0], d00 = matrix[1], c01 = matrix[2], d01 = matrix[3];
            double c10 = matrix[4], d10 = matrix[5], c11 = matrix[6], d11 = matrix[7];

            target[x0] = a0 * c00 - b0 * d00 + a1 * c01 - b1 * d01;
            target[x0 + 1] = a0 * d00 + b0 * c00 + a1 * d01 + b1 * c01;
            target[x1] = a0 * c10 - b0 * d10 + a1 * c11 - b1 * d11;
            target[x1 + 1] = a0 * d10 + b0 * c10 + a1 * d11 + b1 * c11;
        }
        else if (size == 2) {
            int x0 = start + window[0], x1 = start + window[1], x2 = start + window[2], x3 = start + window[3];
            double a0 = source[x0], b0 = source[x0 + 1], a1 = source[x1], b1 = source[x1 + 1];
            double a2 = source[x2], b2 = source[x2 + 1], a3 = source[x3], b3 = source[x3 + 1];

            double c00 = matrix[0], d00 = matrix[1], c01 = matrix[2], d01 = matrix[3];
            double c02 = matrix[4], d02 = matrix[5], c03 = matrix[6], d03 = matrix[7];
            double c10 = matrix[8], d10 = matrix[9], c11 = matrix[10], d11 = matrix[11];
            double c12 = matrix[12], d12 = matrix[13], c13 = matrix[14], d13 = matrix[15];
            double c20 = matrix[16], d20 = matrix[17], c21 = matrix[18], d21 = matrix[19];
            double c22 = matrix[20], d22 = matrix[21], c23 = matrix[22], d23 = matrix[23];
            double c30 = matrix[24], d30 = matrix[25], c31 = matrix[26], d31 = matrix[27];
            double c32 = matrix[28], d32 = matrix[29], c33 = matrix[30], d33 = matrix[31];

            target[x0] = a0 * c00 - b0 * d00 + a1 * c01 - b1 * d01 + a2 * c02 - b2 * d02 + a3 * c03 - b3 * d03;
            target[x0 + 1] = a0 * d00 + b0 * c00 + a1 * d01 + b1 * c01 + a2 * d02 + b2 * c02 + a3 * d03 + b3 * c03;
            target[x1] = a0 * c10 - b0 * d10 + a1 * c11 - b1 * d11 + a2 * c12 - b2 * d12 + a3 * c13 - b3 * d13;
            target[x1 + 1] = a0 * d10 + b0 * c10 + a1 * d11 + b1 * c11 + a2 * d12 + b2 * c12 + a3 * d13 + b3 * c13;
            target[x2] = a0 * c20 - b0 * d20 + a1 * c21 - b1 * d21 + a2 * c22 - b2 * d22 + a3 * c23 - b3 * d23;
            target[x2 + 1] = a0 * d20 + b0 * c20 + a1 * d21 + b1 * c21 + a2 * d22 + b2 * c22 + a3 * d23 + b3 * c23;
            target[x3] = a0 * c30 - b0 * d30 + a1 * c31 - b1 * d31 + a2 * c32 - b2 * d32 + a3 * c33 - b3 * d33;
            target[x3 + 1] = a0 * d30 + b0 * c30 + a1 * d31 + b1 * c31 + a2 * d32 + b2 * c32 + a3 * d33 + b3 * c33;
        }
        else {
            for (int i = 0; i < dim; ++i) {
                int x = start + window[i], r = i * dim;
                target[x] = target[x + 1] = 0.0;
                for (int j = 0; j < dim; ++j) {
                    int k = (r + j) << 1, y = start + window[j];
                    double a = source[y], b = source[y + 1], c = matrix[k], d = matrix[k + 1];
                    target[x] += a * c - b * d;
                    target[x + 1] += a * d + b * c;
                }
            }
        }
    }

    @Override
    public Operator rebased(int[] basis) {
        double[] matrix = Arrays.copyOf(this.matrix, this.matrix.length);
        Matrix.changeIndexBasis(dim, matrix, basis);
        return new DenseOperator(matrix);
    }
}
