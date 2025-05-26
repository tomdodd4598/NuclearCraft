package nc.multiblock.hx;

import it.unimi.dsi.fastutil.longs.*;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import nc.config.NCConfig;
import nc.tile.hx.*;
import nc.tile.internal.fluid.Tank;
import nc.util.LambdaHelper;
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
		Long2ObjectMap<ObjectSet<Vec3d>> flowMap = HeatExchangerFlowHelper.getFlowMap(
				inletPosLongSet,
				outletPosLongSet,
				x -> LambdaHelper.let(x.toLong(), y -> tubePosLongSet.contains(y) ? tubeMap.get(y).settings : null),
				HeatExchangerTubeSetting::isOpen,
				(x, y) -> tubePosLongSet.contains(x.toLong()),
				x -> outletPosLongSet.contains(x.toLong())
		);
		
		for (Long2ObjectMap.Entry<ObjectSet<Vec3d>> entry : flowMap.long2ObjectEntrySet()) {
			TileHeatExchangerTube tube = tubeMap.get(entry.getLongKey());
			tube.tubeFlow = entry.getValue().stream().reduce(Vec3d.ZERO, Vec3d::add).normalize();
		}
	}
	
	public void setFlowStats(Long2ObjectMap<TileHeatExchangerTube> tubeMap) {
		usefulTubeCount = 0;
		tubeFlow = Vec3d.ZERO;
		shellFlow = Vec3d.ZERO;
		flowCosine = 0D;
		baseHeatingMultiplier = 0D;
		baseCoolingMultiplier = 0D;
		
		for (long tubePosLong : tubePosLongSet) {
			TileHeatExchangerTube tube = tubeMap.get(tubePosLong);
			if (tube.tubeFlow != null && (tube.shellFlow != null || logic.isCondenser())) {
				++usefulTubeCount;
				tubeFlow = tubeFlow.add(tube.tubeFlow);
				if (tube.shellFlow != null) {
					shellFlow = shellFlow.add(tube.shellFlow);
					flowCosine += tube.tubeFlow.dotProduct(tube.shellFlow);
				}
				baseHeatingMultiplier += tube.heatTransferCoefficient * tube.heatRetentionMult;
				baseCoolingMultiplier += tube.heatTransferCoefficient;
			}
		}
		
		if (usefulTubeCount > 0) {
			tubeFlow = tubeFlow.scale(1D / usefulTubeCount);
			shellFlow = shellFlow.scale(1D / usefulTubeCount);
			flowCosine /= usefulTubeCount;
		}
	}
	
	public boolean isContraflow() {
		return flowCosine < 1D / NCConfig.heat_exchanger_max_size;
	}
}
