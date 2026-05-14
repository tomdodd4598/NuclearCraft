package nc.network.multiblock;

import io.netty.buffer.ByteBuf;
import nc.multiblock.fission.FissionReactor;
import nc.tile.TileContainerInfo;
import nc.tile.fission.*;
import nc.tile.internal.heat.HeatBuffer;
import net.minecraft.util.math.BlockPos;

public class PebbleFissionUpdatePacket extends FissionUpdatePacket {
	
	public double meanHeatingSpeedMultiplier, totalHeatingSpeedMultiplier;
	
	public PebbleFissionUpdatePacket() {
		super();
	}
	
	public PebbleFissionUpdatePacket(BlockPos pos, boolean isReactorOn, HeatBuffer heatBuffer, int clusterCount, long cooling, long rawHeating, long totalHeatMult, double meanHeatMult, int fuelComponentCount, long usefulPartCount, double totalEfficiency, double meanEfficiency, double sparsityEfficiencyMult, double meanHeatingSpeedMultiplier, double totalHeatingSpeedMultiplier) {
		super(pos, isReactorOn, heatBuffer, clusterCount, cooling, rawHeating, totalHeatMult, meanHeatMult, fuelComponentCount, usefulPartCount, totalEfficiency, meanEfficiency, sparsityEfficiencyMult);
		this.meanHeatingSpeedMultiplier = meanHeatingSpeedMultiplier;
		this.totalHeatingSpeedMultiplier = totalHeatingSpeedMultiplier;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		meanHeatingSpeedMultiplier = buf.readDouble();
		totalHeatingSpeedMultiplier = buf.readDouble();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		buf.writeDouble(meanHeatingSpeedMultiplier);
		buf.writeDouble(totalHeatingSpeedMultiplier);
	}
	
	public static class Handler extends MultiblockUpdatePacket.Handler<FissionReactor, IFissionPart, FissionUpdatePacket, TilePebbleFissionController, TileContainerInfo<TilePebbleFissionController>, PebbleFissionUpdatePacket> {
		
		public Handler() {
			super(TilePebbleFissionController.class);
		}
		
		@Override
		protected void onPacket(PebbleFissionUpdatePacket message, FissionReactor multiblock) {
			multiblock.onMultiblockUpdatePacket(message);
		}
	}
}
