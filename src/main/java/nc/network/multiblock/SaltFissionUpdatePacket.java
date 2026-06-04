package nc.network.multiblock;

import io.netty.buffer.ByteBuf;
import nc.multiblock.fission.FissionReactor;
import nc.tile.TileContainerInfo;
import nc.tile.fission.*;
import nc.tile.internal.heat.HeatBuffer;
import net.minecraft.util.math.BlockPos;

public class SaltFissionUpdatePacket extends FissionUpdatePacket {
	
	public double meanHeatingSpeedMultiplier;
	
	public SaltFissionUpdatePacket() {
		super();
	}
	
	public SaltFissionUpdatePacket(BlockPos pos, boolean isReactorOn, HeatBuffer heatBuffer, int clusterCount, long cooling, long rawHeating, double meanHeatMult, long usefulPartCount, double meanEfficiency, double sparsityEfficiencyMult, double meanHeatingSpeedMultiplier) {
		super(pos, isReactorOn, heatBuffer, clusterCount, cooling, rawHeating, meanHeatMult, usefulPartCount, meanEfficiency, sparsityEfficiencyMult);
		this.meanHeatingSpeedMultiplier = meanHeatingSpeedMultiplier;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		meanHeatingSpeedMultiplier = buf.readDouble();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		buf.writeDouble(meanHeatingSpeedMultiplier);
	}
	
	public static class Handler extends MultiblockUpdatePacket.Handler<FissionReactor, IFissionPart, FissionUpdatePacket, TileSaltFissionController, TileContainerInfo<TileSaltFissionController>, SaltFissionUpdatePacket> {
		
		public Handler() {
			super(TileSaltFissionController.class);
		}
		
		@Override
		protected void onPacket(SaltFissionUpdatePacket message, FissionReactor multiblock) {
			multiblock.onMultiblockUpdatePacket(message);
		}
	}
}
