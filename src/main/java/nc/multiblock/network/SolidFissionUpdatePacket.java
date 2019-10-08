package nc.multiblock.network;

import nc.multiblock.fission.FissionReactor;
import nc.multiblock.fission.solid.tile.TileSolidFissionController;
import net.minecraft.util.math.BlockPos;

public class SolidFissionUpdatePacket extends FissionUpdatePacket {
	
	public SolidFissionUpdatePacket() {
		super();
	}
	
	public SolidFissionUpdatePacket(BlockPos pos, boolean isReactorOn, int clusterCount, long cooling, long rawHeating, double effectiveHeating, long totalHeatMult, double meanHeatMult, int fuelComponentCount, long usefulPartCount, double totalEfficiency, double meanEfficiency, double sparsityEfficiencyMult, long capacity, long heat, double roundedOutputRate, long netHeating) {
		super(pos, isReactorOn, clusterCount, cooling, rawHeating, effectiveHeating, totalHeatMult, meanHeatMult, fuelComponentCount, usefulPartCount, totalEfficiency, meanEfficiency, sparsityEfficiencyMult, capacity, heat, roundedOutputRate, netHeating);
	}
	
	public static class Handler extends MultiblockUpdatePacket.Handler<SolidFissionUpdatePacket, FissionReactor, TileSolidFissionController> {
		
		public Handler() {
			super(TileSolidFissionController.class);
		}
	}
}
