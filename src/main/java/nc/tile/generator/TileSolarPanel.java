package nc.tile.generator;

import nc.NuclearCraft;

public class TileSolarPanel extends TileContinuousBase {
	
	public TileSolarPanel() {
		super("RTG", NuclearCraft.solarRF);
	}
	
	public void energy() {
		if (storage.getEnergyStored() == 0 && worldObj.canBlockSeeTheSky(xCoord, yCoord + 1, zCoord) && worldObj.getBlockLightValue(xCoord, yCoord + 1, zCoord) > 0) {
			storage.receiveEnergy((int) Math.ceil(1D*power*worldObj.getBlockLightValue(xCoord, yCoord + 1, zCoord)*worldObj.getBlockLightValue(xCoord, yCoord + 1, zCoord)/225), false);
		}
	}
}