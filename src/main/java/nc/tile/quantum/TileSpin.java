package nc.tile.quantum;

import nc.tile.NCTile;
import nc.util.Complex;
import net.minecraft.nbt.NBTTagCompound;

public class TileSpin extends NCTile {
	
	public double measuredSpin = -0.5D;
	public double phi = 0;
	public double theta = 180;
	public int isMeasured;
	
	public Complex[] stateVector;
	
	public TileSpin() {
		stateVector = getStateFromAngles(phi, theta);
	}
	
	public void update() {
		super.update();
		if (isMeasured > 0) isMeasured--;
		if (isMeasured < 0) isMeasured = 0;
		//NCUtil.getLogger().info(measuredSpin);
	}
	
	public void setStateFromAngles(double phi, double theta) {
		stateVector = getStateFromAngles(phi, theta);
	}
	
	public Complex[] getStateFromAngles(double phi, double theta) {
		return new Complex[] {Complex.exp(new Complex(0, phi*Math.PI/180D)).multiply(Math.sin(theta*Math.PI/360D)), new Complex(Math.cos(theta*Math.PI/360D), 0)};
	}
	
	public boolean isMeasured() {
		return isMeasured > 0;
	}
	
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		nbt.setDouble("measuredSpin", measuredSpin);
		nbt.setDouble("phi", phi);
		nbt.setDouble("theta", theta);
		nbt.setDouble("stateVectorUpRe", stateVector[0].re());
		nbt.setDouble("stateVectorUpIm", stateVector[0].im());
		nbt.setDouble("stateVectorDownRe", stateVector[1].re());
		nbt.setDouble("stateVectorDownIm", stateVector[1].im());
		return nbt;
	}
	
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		measuredSpin = nbt.getDouble("measuredSpin");
		phi = nbt.getDouble("phi");
		theta = nbt.getDouble("theta");
		stateVector = new Complex[] {new Complex(nbt.getDouble("stateVectorUpRe"), nbt.getDouble("stateVectorUpIm")), new Complex(nbt.getDouble("stateVectorDownRe"), nbt.getDouble("stateVectorDownIm"))};
	}
}
