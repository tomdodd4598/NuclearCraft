package nc.multiblock.network;

import io.netty.buffer.ByteBuf;
import nc.multiblock.condenser.Condenser;
import nc.multiblock.condenser.tile.TileCondenserController;
import net.minecraft.util.math.BlockPos;

public class CondenserUpdatePacket extends MultiblockUpdatePacket {
	
	public boolean isCondenserOn;
	
	public CondenserUpdatePacket() {
		messageValid = false;
	}
	
	public CondenserUpdatePacket(BlockPos pos, boolean isCondenserOn) {
		this.pos = pos;
		this.isCondenserOn = isCondenserOn;
		
		messageValid = true;
	}
	
	@Override
	public void readMessage(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		isCondenserOn = buf.readBoolean();
	}
	
	@Override
	public void writeMessage(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeBoolean(isCondenserOn);
	}
	
	public static class Handler extends MultiblockUpdatePacket.Handler<CondenserUpdatePacket, Condenser, TileCondenserController> {

		public Handler() {
			super(TileCondenserController.class);
		}
	}
}
