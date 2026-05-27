package nc.quantum;

import nc.quantum.operator.DenseOperator;
import nc.quantum.operator.DiagonalOperator;
import nc.quantum.operator.MonomialOperator;
import nc.quantum.operator.Operator;

public class Consts {

	public static final double INV_SQRT_2 = 0.707106781186547524400844362104849039;

	public static final Operator X = new MonomialOperator(new double[] {
			1, 0,
			1, 0,
	}, new int[] {1, 0});

	public static final Operator Y = new MonomialOperator(new double[] {
			0, -1,
			0, 1,
	}, new int[] {1, 0});

	public static final Operator Z = new DiagonalOperator(new double[] {
			1, 0,
			-1, 0,
	});

	public static final Operator H = new DenseOperator(new double[] {
			INV_SQRT_2, 0, INV_SQRT_2, 0,
			INV_SQRT_2, 0, -INV_SQRT_2, 0,
	});

	public static final Operator S = new DiagonalOperator(new double[] {
			1, 0,
			0, 1,
	});

	public static final Operator Sdg = new DiagonalOperator(new double[] {
			1, 0,
			0, -1,
	});

	public static final Operator T = new DiagonalOperator(new double[] {
			1, 0,
			INV_SQRT_2, INV_SQRT_2,
	});

	public static final Operator Tdg = new DiagonalOperator(new double[] {
			1, 0,
			INV_SQRT_2, -INV_SQRT_2,
	});

	public static final Operator SWAP = new MonomialOperator(new double[] {
			1, 0,
			1, 0,
			1, 0,
			1, 0,
	}, new int[] {0, 2, 1, 3});
}
