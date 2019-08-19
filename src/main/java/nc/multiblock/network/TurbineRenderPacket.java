package nc.multiblock.network;

import io.netty.buffer.ByteBuf;
import nc.multiblock.turbine.Turbine;
import nc.multiblock.turbine.tile.TileTurbineController;
import net.minecraft.util.math.BlockPos;

public class TurbineRenderPacket extends MultiblockUpdatePacket {
	
	public float angVel;
	public boolean isProcessing;
	public int recipeRate;
	
	public TurbineRenderPacket() {
		messageValid = false;
	}
	
	public TurbineRenderPacket(BlockPos pos, float angVel, boolean isProcessing, int recipeRate) {
		this.pos = pos;
		this.angVel = angVel;
		this.isProcessing = isProcessing;
		this.recipeRate = recipeRate;
		
		messageValid = true;
	}
	
	@Override
	public void readMessage(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		angVel = buf.readFloat();
		isProcessing = buf.readBoolean();
		recipeRate = buf.readInt();
	}
	
	@Override
	public void writeMessage(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeFloat(angVel);
		buf.writeBoolean(isProcessing);
		buf.writeInt(recipeRate);
	}
	
	public static class Handler extends MultiblockUpdatePacket.Handler<TurbineRenderPacket, Turbine, TileTurbineController> {

		public Handler() {
			super(TileTurbineController.class);
		}
		
		@Override
		protected void onPacket(TurbineRenderPacket message, Turbine multiblock) {
			multiblock.onRenderPacket(message);
		}
	}
}
