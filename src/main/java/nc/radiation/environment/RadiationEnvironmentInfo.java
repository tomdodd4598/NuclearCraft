package nc.radiation.environment;

import java.util.List;

import nc.tile.radiation.IRadiationEnvironmentHandler;
import nc.util.SetList;
import net.minecraft.util.math.BlockPos;

public class RadiationEnvironmentInfo {
	
	public final BlockPos pos;
	public final List<IRadiationEnvironmentHandler> tileList = new SetList<IRadiationEnvironmentHandler>();
	
	public RadiationEnvironmentInfo(BlockPos pos) {
		this.pos = pos;
	}
	
	public RadiationEnvironmentInfo(BlockPos pos, IRadiationEnvironmentHandler tile) {
		this.pos = pos;
		tileList.add(tile);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (!(obj instanceof RadiationEnvironmentInfo)) return false;
		return pos.equals(((RadiationEnvironmentInfo)obj).pos);
	}
}
