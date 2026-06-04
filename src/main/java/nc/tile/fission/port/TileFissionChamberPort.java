package nc.tile.fission.port;

import nc.recipe.NCRecipes;
import nc.tile.fission.TilePebbleFissionChamber;

public class TileFissionChamberPort extends TileFissionItemPort<TileFissionChamberPort, TilePebbleFissionChamber> {
	
	public TileFissionChamberPort() {
		super("fission_chamber_port", TileFissionChamberPort.class, NCRecipes.pebble_fission);
	}
}
