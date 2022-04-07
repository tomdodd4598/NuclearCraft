package nc.network.multiblock;

import io.netty.buffer.ByteBuf;
import nc.multiblock.fission.FissionReactor;
import nc.multiblock.fission.solid.tile.TileSolidFissionController;
import nc.multiblock.fission.tile.IFissionPart;
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
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		effectiveHeating = buf.readDouble();
		heatingOutputRateFP = buf.readDouble();
		reservedEffectiveHeat = buf.readDouble();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		buf.writeDouble(effectiveHeating);
		buf.writeDouble(heatingOutputRateFP);
		buf.writeDouble(reservedEffectiveHeat);
	}
	
	public static class Handler extends MultiblockUpdatePacket.Handler<FissionReactor, IFissionPart, FissionUpdatePacket, TileSolidFissionController, SolidFissionUpdatePacket> {
		
		public Handler() {
			super(TileSolidFissionController.class);
		}
		
		@Override
		protected void onPacket(SolidFissionUpdatePacket message, FissionReactor multiblock) {
			multiblock.onMultiblockUpdatePacket(message);
		}
	}
}
