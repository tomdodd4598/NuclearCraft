package nc.multiblock.network;

import javax.vecmath.Vector3f;

import io.netty.buffer.ByteBuf;
import nc.multiblock.turbine.Turbine;
import nc.multiblock.turbine.tile.TileTurbineController;
import net.minecraft.util.math.BlockPos;

public class TurbineFormPacket extends MultiblockUpdatePacket {
	
	public boolean nullArray;
	public BlockPos[] bladePosArray;
	public Vector3f[] renderPosArray;
	public float[] bladeAngleArray;
	
	public TurbineFormPacket() {
		messageValid = false;
	}
	
	public TurbineFormPacket(BlockPos pos, BlockPos[] bladePosArray, Vector3f[] renderPosArray, float[] bladeAngleArray) {
		this.pos = pos;
		nullArray = bladePosArray == null || renderPosArray == null || bladeAngleArray == null;
		this.bladePosArray = bladePosArray;
		this.renderPosArray = renderPosArray;
		this.bladeAngleArray = bladeAngleArray;
		
		messageValid = true;
	}
	
	@Override
	public void readMessage(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		nullArray = buf.readBoolean();
		if (!nullArray) {
			bladePosArray = new BlockPos[buf.readInt()];
			for (int i = 0; i < bladePosArray.length; i++) {
				bladePosArray[i] = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
			}
			renderPosArray = new Vector3f[buf.readInt()];
			for (int i = 0; i < renderPosArray.length; i++) {
				renderPosArray[i] = new Vector3f(buf.readFloat(), buf.readFloat(), buf.readFloat());
			}
			bladeAngleArray = new float[buf.readInt()];
			for (int i = 0; i < bladeAngleArray.length; i++) {
				bladeAngleArray[i] = buf.readFloat();
			}
		}
	}
	
	@Override
	public void writeMessage(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeBoolean(nullArray);
		if (!nullArray) {
			buf.writeInt(bladePosArray.length);
			for (BlockPos rotorPos : bladePosArray) {
				buf.writeInt(rotorPos.getX());
				buf.writeInt(rotorPos.getY());
				buf.writeInt(rotorPos.getZ());
			}
			buf.writeInt(renderPosArray.length);
			for (Vector3f planePos : renderPosArray) {
				buf.writeFloat(planePos.x);
				buf.writeFloat(planePos.y);
				buf.writeFloat(planePos.z);
			}
			buf.writeInt(bladeAngleArray.length);
			for (float bladeAngle : bladeAngleArray) {
				buf.writeFloat(bladeAngle);
			}
		}
	}
	
	public static class Handler extends MultiblockUpdatePacket.Handler<TurbineFormPacket, Turbine, TileTurbineController> {
		
		public Handler() {
			super(TileTurbineController.class);
		}
		
		@Override
		protected void onPacket(TurbineFormPacket message, Turbine multiblock) {
			multiblock.onFormPacket(message);
		}
	}
}
