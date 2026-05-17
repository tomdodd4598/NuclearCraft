package nc.util;

public class PixelTracker {
	
	protected final ValueTracker tracker;
	
	protected boolean initialized = false;
	
	public int pixel = 0;
	
	public double hysteresis;
	
	public PixelTracker(int windowSize) {
		this(new ValueTracker(windowSize), 0.75D);
	}
	
	public PixelTracker(ValueTracker tracker, double hysteresis) {
		this.tracker = tracker;
		this.hysteresis = hysteresis;
	}
	
	public void reset() {
		tracker.reset();
		initialized = false;
		pixel = 0;
	}
	
	public int update(double value) {
		value = tracker.update(value);
		int rounded = (int) Math.round(value);
		
		if (!initialized) {
			initialized = true;
			pixel = rounded;
			return pixel;
		}
		
		if (Math.abs(value - pixel) >= hysteresis) {
			pixel = rounded;
		}
		
		return pixel;
	}
}
