package nc.util;

import java.util.Arrays;

public class ValueTracker {
	
	protected final double[] windowSamples;
	protected int windowIndex = 0, windowCount = 0;
	protected double windowSum = 0D;
	
	protected boolean initialized = false;
	
	protected double fast = 0D, noise = 0D;
	protected int driftTicks = 0;
	
	public double fastAlpha, noiseAlpha, noiseFloor, driftSigma;
	public int triggerTicks;
	
	public ValueTracker() {
		this(40);
	}
	
	public ValueTracker(int windowSize) {
		this(windowSize, 0.25D, 0.1D, 0.01D, 2D, 5);
	}
	
	public ValueTracker(int windowSize, double fastAlpha, double noiseAlpha, double noiseFloor, double driftSigma, int triggerTicks) {
		this.windowSamples = new double[windowSize];
		this.fastAlpha = fastAlpha;
		this.noiseAlpha = noiseAlpha;
		this.noiseFloor = noiseFloor;
		this.driftSigma = driftSigma;
		this.triggerTicks = triggerTicks;
	}
	
	public void reset() {
		windowIndex = windowCount = 0;
		windowSum = 0D;
		initialized = false;
		fast = noise = 0D;
		driftTicks = 0;
		Arrays.fill(windowSamples, 0D);
	}
	
	public double update(double value) {
		if (!initialized) {
			initialized = true;
			fast = value;
			noise = 0D;
		}
		
		if (windowCount < windowSamples.length) {
			++windowCount;
		}
		else {
			windowSum -= windowSamples[windowIndex];
		}
		
		windowSamples[windowIndex] = value;
		windowSum += value;
		windowIndex = (windowIndex + 1) % windowSamples.length;
		
		double mean = windowSum / windowCount;
		fast += fastAlpha * (value - fast);
		noise += noiseAlpha * (Math.abs(value - mean) - noise);
		
		if (Math.abs(fast - mean) > Math.max(noiseFloor * Math.abs(mean), driftSigma * noise)) {
			++driftTicks;
		}
		else {
			driftTicks = 0;
		}
		
		return driftTicks >= triggerTicks ? fast : mean;
	}
}
