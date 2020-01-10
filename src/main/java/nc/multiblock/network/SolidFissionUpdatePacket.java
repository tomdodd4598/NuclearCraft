package nc.multiblock.network;

import io.netty.buffer.ByteBuf;
import nc.multiblock.fission.FissionReactor;
import nc.multiblock.fission.solid.tile.TileSolidFissionController;
import nc.tile.internal.heat.HeatBuffer;
import net.minecraft.util.math.BlockPos;

public class SolidFissionUpdatePacket extends FissionUpdatePacket {
	
	public double effectiveHeating, heatingOutputRateFP, reservedEffectiveHeat;
	
	public SolidFissionUpdatePacket() {
		super();
	}
	
	public SolidFissionUpdatePacket(BlockPos pos, boolean isReactorOn, HeatBuffer heatBuffer, int clusterCount, long cooling, long rawHeating, long totalHeatMult, double meanHeatMult, int fuelComponentCount, long usefulPartCount, double totalEfficiency, double meanEfficiency, double sparsityEfficiencyMult, double effectiveHeating, double heatingOutputRateFP, double reservedEffectiveHeat) {
		super(pos, isReactorOn, heatBuffer, clusterCount, cooling, rawHeating, totalHeatMult, meanHeatMult, fuelComponentCount, usefulPartCount, totalEfficiency, meanEfficiency, sparsityEfficiencyMult);
		this.effectiveHeating = effectiveHeating;
		this.heatingOutputRateFP = heatingOutputRateFP;
		this.reservedEffectiveHeat = reservedEffectiveHeat;
	}
	
	@Override
	public void readMessage(ByteBuf buf) {
		super.readMessage(buf);
		effectiveHeating = buf.readDouble();
		heatingOutputRateFP = buf.readDouble();
		reservedEffectiveHeat = buf.readDouble();
	}
	
	@Override
	public void writeMessage(ByteBuf buf) {
		super.writeMessage(buf);
		buf.writeDouble(effectiveHeating);
		buf.writeDouble(heatingOutputRateFP);
		buf.writeDouble(reservedEffectiveHeat);
	}
	
	public static class Handler extends MultiblockUpdatePacket.Handler<SolidFissionUpdatePacket, FissionReactor, TileSolidFissionController> {
		
		public Handler() {
			super(TileSolidFissionController.class);
		}
	}
}
