package nc.network.multiblock;

import io.netty.buffer.ByteBuf;
import nc.multiblock.hx.HeatExchanger;
import nc.tile.TileContainerInfo;
import nc.tile.hx.*;
import net.minecraft.util.math.BlockPos;

public class HeatExchangerUpdatePacket extends MultiblockUpdatePacket {
	
	public boolean isExchangerOn;
	public int totalNetworkCount;
	public int activeNetworkCount;
	public int activeTubeCount;
	public int activeContactCount;
	public double tubeInputRateFP;
	public double shellInputRateFP;
	public double heatTransferRateFP;
	public double heatDissipationRateFP;
	public double totalTempDiff;
	
	public HeatExchangerUpdatePacket() {
		super();
	}
	
	public HeatExchangerUpdatePacket(BlockPos pos, boolean isExchangerOn, int totalNetworkCount, int activeNetworkCount, int activeTubeCount, int activeContactCount, double tubeInputRateFP, double shellInputRateFP, double heatTransferRateFP, double heatDissipationRateFP, double totalTempDiff) {
		super(pos);
		this.isExchangerOn = isExchangerOn;
		this.totalNetworkCount = totalNetworkCount;
		this.activeNetworkCount = activeNetworkCount;
		this.activeTubeCount = activeTubeCount;
		this.activeContactCount = activeContactCount;
		this.tubeInputRateFP = tubeInputRateFP;
		this.shellInputRateFP = shellInputRateFP;
		this.heatTransferRateFP = heatTransferRateFP;
		this.heatDissipationRateFP = heatDissipationRateFP;
		this.totalTempDiff = totalTempDiff;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		isExchangerOn = buf.readBoolean();
		totalNetworkCount = buf.readInt();
		activeNetworkCount = buf.readInt();
		activeTubeCount = buf.readInt();
		activeContactCount = buf.readInt();
		tubeInputRateFP = buf.readDouble();
		shellInputRateFP = buf.readDouble();
		heatTransferRateFP = buf.readDouble();
		heatDissipationRateFP = buf.readDouble();
		totalTempDiff = buf.readDouble();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		buf.writeBoolean(isExchangerOn);
		buf.writeInt(totalNetworkCount);
		buf.writeInt(activeNetworkCount);
		buf.writeInt(activeTubeCount);
		buf.writeInt(activeContactCount);
		buf.writeDouble(tubeInputRateFP);
		buf.writeDouble(shellInputRateFP);
		buf.writeDouble(heatTransferRateFP);
		buf.writeDouble(heatDissipationRateFP);
		buf.writeDouble(totalTempDiff);
	}
	
	public static class Handler extends MultiblockUpdatePacket.Handler<HeatExchanger, IHeatExchangerPart, HeatExchangerUpdatePacket, TileHeatExchangerController, TileContainerInfo<TileHeatExchangerController>, HeatExchangerUpdatePacket> {
		
		public Handler() {
			super(TileHeatExchangerController.class);
		}
		
		@Override
		protected void onPacket(HeatExchangerUpdatePacket message, HeatExchanger multiblock) {
			multiblock.onMultiblockUpdatePacket(message);
		}
	}
}
