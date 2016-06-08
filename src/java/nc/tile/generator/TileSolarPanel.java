package nc.tile.generator;

import nc.NuclearCraft;

public class TileSolarPanel extends TileContinuousBase {
	public int power = NuclearCraft.solarRF;

	public TileSolarPanel() {
		super("RTG", NuclearCraft.solarRF*2, 1);
	}
	
	public void energy() {
		if (this.storage.getEnergyStored() == 0 && worldObj.canBlockSeeTheSky(xCoord, yCoord+1, zCoord) /*&& Math.floor(worldObj.getTotalWorldTime()/12000)%2==0*/ && worldObj.getBlockLightValue(xCoord, yCoord+1, zCoord) == 15) {
			this.storage.receiveEnergy(power, false);
		}
	}
}