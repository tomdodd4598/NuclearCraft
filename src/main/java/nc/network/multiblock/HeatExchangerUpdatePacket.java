package nc.network.multiblock;

import io.netty.buffer.ByteBuf;
import nc.multiblock.hx.HeatExchanger;
import nc.tile.hx.*;
import net.minecraft.util.math.BlockPos;

public class HeatExchangerUpdatePacket extends MultiblockUpdatePacket {
	
	public boolean isHeatExchangerOn;
	public double fractionOfTubesActive;
	public double efficiency;
	public double maxEfficiency;
	
	public HeatExchangerUpdatePacket() {
		super();
	}
	
	public HeatExchangerUpdatePacket(BlockPos pos, boolean isHeatExchangerOn, double fractionOfTubesActive, double efficiency, double maxEfficiency) {
		super(pos);
		this.isHeatExchangerOn = isHeatExchangerOn;
		this.fractionOfTubesActive = fractionOfTubesActive;
		this.efficiency = efficiency;
		this.maxEfficiency = maxEfficiency;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		isHeatExchangerOn = buf.readBoolean();
		fractionOfTubesActive = buf.readDouble();
		efficiency = buf.readDouble();
		maxEfficiency = buf.readDouble();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
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
