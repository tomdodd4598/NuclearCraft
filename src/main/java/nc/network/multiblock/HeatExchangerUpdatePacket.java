package nc.network.multiblock;

import io.netty.buffer.ByteBuf;
import nc.multiblock.heatExchanger.HeatExchanger;
import nc.multiblock.heatExchanger.tile.*;
import net.minecraft.util.math.BlockPos;

public class HeatExchangerUpdatePacket extends MultiblockUpdatePacket {
	
	public boolean isHeatExchangerOn;
	public double fractionOfTubesActive;
	public double efficiency;
	public double maxEfficiency;
	
	public HeatExchangerUpdatePacket() {
		
	}
	
	public HeatExchangerUpdatePacket(BlockPos pos, boolean isHeatExchangerOn, double fractionOfTubesActive, double efficiency, double maxEfficiency) {
		this.pos = pos;
		this.isHeatExchangerOn = isHeatExchangerOn;
		this.fractionOfTubesActive = fractionOfTubesActive;
		this.efficiency = efficiency;
		this.maxEfficiency = maxEfficiency;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		isHeatExchangerOn = buf.readBoolean();
		fractionOfTubesActive = buf.readDouble();
		efficiency = buf.readDouble();
		maxEfficiency = buf.readDouble();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeBoolean(isHeatExchangerOn);
		buf.writeDouble(fractionOfTubesActive);
		buf.writeDouble(efficiency);
		buf.writeDouble(maxEfficiency);
	}
	
	public static class Handler extends MultiblockUpdatePacket.Handler<HeatExchanger, IHeatExchangerPart, HeatExchangerUpdatePacket, TileHeatExchangerController, HeatExchangerUpdatePacket> {
		
		public Handler() {
			super(TileHeatExchangerController.class);
		}
		
		@Override
		protected void onPacket(HeatExchangerUpdatePacket message, HeatExchanger multiblock) {
			multiblock.onMultiblockUpdatePacket(message);
		}
	}
}
