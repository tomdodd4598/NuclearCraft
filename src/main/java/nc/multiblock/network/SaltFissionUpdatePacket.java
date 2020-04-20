package nc.multiblock.network;

import io.netty.buffer.ByteBuf;
import nc.multiblock.fission.FissionReactor;
import nc.multiblock.fission.salt.tile.TileSaltFissionController;
import nc.tile.internal.heat.HeatBuffer;
import net.minecraft.util.math.BlockPos;

public class SaltFissionUpdatePacket extends FissionUpdatePacket {
	
	public double meanHeatingSpeedMultiplier, totalHeatingSpeedMultiplier;
	
	public SaltFissionUpdatePacket() {
		super();
	}
	
	public SaltFissionUpdatePacket(BlockPos pos, boolean isReactorOn, HeatBuffer heatBuffer, int clusterCount, long cooling, long rawHeating, long totalHeatMult, double meanHeatMult, int fuelComponentCount, long usefulPartCount, double totalEfficiency, double meanEfficiency, double sparsityEfficiencyMult, double meanHeatingSpeedMultiplier, double totalHeatingSpeedMultiplier) {
		super(pos, isReactorOn, heatBuffer, clusterCount, cooling, rawHeating, totalHeatMult, meanHeatMult, fuelComponentCount, usefulPartCount, totalEfficiency, meanEfficiency, sparsityEfficiencyMult);
		this.meanHeatingSpeedMultiplier = meanHeatingSpeedMultiplier;
		this.totalHeatingSpeedMultiplier = totalHeatingSpeedMultiplier;
	}
	
	@Override
	public void readMessage(ByteBuf buf) {
		super.readMessage(buf);
		meanHeatingSpeedMultiplier = buf.readDouble();
		totalHeatingSpeedMultiplier = buf.readDouble();
	}
	
	@Override
	public void writeMessage(ByteBuf buf) {
		super.writeMessage(buf);
		buf.writeDouble(meanHeatingSpeedMultiplier);
		buf.writeDouble(totalHeatingSpeedMultiplier);
	}
	
	public static class Handler extends MultiblockUpdatePacket.Handler<SaltFissionUpdatePacket, FissionReactor, TileSaltFissionController> {
		
		public Handler() {
			super(TileSaltFissionController.class);
		}
	}
}
