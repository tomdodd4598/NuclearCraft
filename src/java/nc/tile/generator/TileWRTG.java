package nc.tile.generator;

import nc.NuclearCraft;

public class TileWRTG extends TileContinuousBase {
	public int power = NuclearCraft.WRTGRF;

	public TileWRTG() {
		super("WRTG", NuclearCraft.WRTGRF*2, 1);
	}
	
	public void energy() {
		this.storage.receiveEnergy(power, false);
	}
}