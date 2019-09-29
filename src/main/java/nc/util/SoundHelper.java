package nc.util;

public class SoundHelper {
	
	private static final double P = 0.05776193287902400419D;
	
	public static float getPitch(double note) {
		return (float) Math.exp(P*note);
	}
}
