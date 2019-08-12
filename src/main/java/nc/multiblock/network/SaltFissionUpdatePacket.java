package nc.multiblock.network;

import io.netty.buffer.ByteBuf;
import nc.multiblock.saltFission.SaltFissionReactor;
import nc.multiblock.saltFission.tile.TileSaltFissionController;
import net.minecraft.util.math.BlockPos;

public class SaltFissionUpdatePacket extends MultiblockUpdatePacket {
	
	public boolean isReactorOn;
	public double cooling, heating, rawEfficiency, maxRawEfficiency, heatMult, maxHeatMult, coolingEfficiency;
	public long capacity, heat;
	
	public SaltFissionUpdatePacket() {
		messageValid = false;
	}
	
	public SaltFissionUpdatePacket(BlockPos pos, boolean isReactorOn, double cooling, double heating, double rawEfficiency, double maxRawEfficiency, double heatMult, double maxHeatMult, double coolingEfficiency, long capacity, long heat) {
		this.pos = pos;
		this.isReactorOn = isReactorOn;
		this.cooling = cooling;
		this.heating = heating;
		this.rawEfficiency = rawEfficiency;
		this.maxRawEfficiency = maxRawEfficiency;
		this.heatMult = heatMult;
		this.maxHeatMult = maxHeatMult;
		this.coolingEfficiency = coolingEfficiency;
		this.capacity = capacity;
		this.heat = heat;
		
		messageValid = true;
	}
	
	@Override
	public void readMessage(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		isReactorOn = buf.readBoolean();
		cooling = buf.readDouble();
		heating = buf.readDouble();
		rawEfficiency = buf.readDouble();
		maxRawEfficiency = buf.readDouble();
		heatMult = buf.readDouble();
		maxHeatMult = buf.readDouble();
		coolingEfficiency = buf.readDouble();
		capacity = buf.readLong();
		heat = buf.readLong();
	}
	
	@Override
	public void writeMessage(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeBoolean(isReactorOn);
		buf.writeDouble(cooling);
		buf.writeDouble(heating);
		buf.writeDouble(rawEfficiency);
		buf.writeDouble(maxRawEfficiency);
		buf.writeDouble(heatMult);
		buf.writeDouble(maxHeatMult);
		buf.writeDouble(coolingEfficiency);
		buf.writeLong(capacity);
		buf.writeLong(heat);
	}
	
	public static class Handler extends MultiblockUpdatePacket.Handler<SaltFissionUpdatePacket, SaltFissionReactor, TileSaltFissionController> {

		public Handler() {
			super(TileSaltFissionController.class);
		}
	}
}
