package nc.multiblock.hx;

import it.unimi.dsi.fastutil.longs.*;
import nc.config.NCConfig;
import nc.tile.hx.*;
import nc.tile.internal.fluid.Tank;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;
import java.util.*;

public class HeatExchangerTubeNetwork {
	
	public final HeatExchangerLogic logic;
	
	public final LongSet tubePosLongSet = new LongOpenHashSet();
	public final LongSet inletPosLongSet = new LongRBTreeSet();
	public final LongSet outletPosLongSet = new LongOpenHashSet();
	
	public @Nullable TileHeatExchangerInlet masterInlet = null;
	
	public int usefulTubeCount = 0;
	public Vec3d tubeFlow = Vec3d.ZERO, shellFlow = Vec3d.ZERO;
	public double flowCosine = 0D, baseHeatingMultiplier = 0D, baseCoolingMultiplier = 0D;
	
	public HeatExchangerTubeNetwork(HeatExchangerLogic logic) {
		this.logic = logic;
	}
	
	public List<Tank> getTanks() {
		return masterInlet == null ? Collections.emptyList() : masterInlet.masterTanks;
	}
	
	public void setTubeFlows(Long2ObjectMap<TileHeatExchangerTube> tubeMap) {
		logic.setNetworkTubeFlows(this, tubeMap);
	}
	
	public void setFlowStats(Long2ObjectMap<TileHeatExchangerTube> tubeMap) {
		logic.setNetworkFlowStats(this, tubeMap);
	}
	
	public boolean isContraflow() {
		return flowCosine < 1D / NCConfig.heat_exchanger_max_size;
	}
}
