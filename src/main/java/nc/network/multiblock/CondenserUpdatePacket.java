package nc.network.multiblock;

import io.netty.buffer.ByteBuf;
import nc.multiblock.hx.HeatExchanger;
import nc.tile.TileContainerInfo;
import nc.tile.hx.*;
import net.minecraft.util.math.BlockPos;

public class CondenserUpdatePacket extends HeatExchangerUpdatePacket {
	
	public CondenserUpdatePacket() {
		super();
	}
	
	public CondenserUpdatePacket(BlockPos pos, boolean isExchangerOn, int totalNetworkCount, int activeNetworkCount, int activeTubeCount, int activeContactCount, double tubeInputRateFP, double shellInputRateFP, double heatTransferRateFP, double heatDissipationRateFP, double totalTempDiff) {
		super(pos, isExchangerOn, totalNetworkCount, activeNetworkCount, activeTubeCount, activeContactCount, tubeInputRateFP, shellInputRateFP, heatTransferRateFP, heatDissipationRateFP, totalTempDiff);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
	}
	
	public static class Handler extends MultiblockUpdatePacket.Handler<HeatExchanger, IHeatExchangerPart, HeatExchangerUpdatePacket, TileCondenserController, TileContainerInfo<TileCondenserController>, CondenserUpdatePacket> {
		
		public Handler() {
			super(TileCondenserController.class);
		}
		
		@Override
		protected void onPacket(CondenserUpdatePacket message, HeatExchanger multiblock) {
			multiblock.onMultiblockUpdatePacket(message);
		}
	}
}
