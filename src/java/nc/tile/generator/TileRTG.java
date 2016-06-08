package nc.tile.generator;

import nc.NuclearCraft;

public class TileRTG extends TileContinuousBase {
	public int power = NuclearCraft.RTGRF;

	public TileRTG() {
		super("RTG", NuclearCraft.RTGRF*2, 1);
	}
	
	public void energy() {
		this.storage.receiveEnergy(power, false);
	}
}