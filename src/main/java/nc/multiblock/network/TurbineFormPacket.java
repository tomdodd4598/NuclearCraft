package nc.multiblock.network;

import javax.vecmath.Vector3f;

import org.apache.commons.lang3.ArrayUtils;

import io.netty.buffer.ByteBuf;
import nc.multiblock.Multiblock.AssemblyState;
import nc.multiblock.turbine.Turbine;
import nc.multiblock.turbine.tile.TileTurbineController;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class TurbineFormPacket extends MultiblockUpdatePacket {
	
	public AssemblyState assemblyState;
	public EnumFacing flowDir;
	public boolean nullData;
	public BlockPos[] bladePosArray;
	public Vector3f[] renderPosArray;
	public float[] bladeAngleArray;
	
	public TurbineFormPacket() {
		messageValid = false;
	}
	
	public TurbineFormPacket(BlockPos pos, AssemblyState assemblyState, EnumFacing flowDir, BlockPos[] bladePosArray, Vector3f[] renderPosArray, float[] bladeAngleArray) {
		this.pos = pos;
		this.assemblyState = assemblyState;
		this.flowDir = flowDir;
		
		nullData = flowDir == null || bladePosArray == null || renderPosArray == null || bladeAngleArray == null;
		if (!nullData && ArrayUtils.contains(bladePosArray, null)) {
			nullData = true;
		}
		if (!nullData && ArrayUtils.contains(renderPosArray, null)) {
			nullData = true;
		}
		
		this.bladePosArray = bladePosArray;
		this.renderPosArray = renderPosArray;
		this.bladeAngleArray = bladeAngleArray;
		
		messageValid = true;
	}
	
	@Override
	public void readMessage(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		byte b = buf.readByte();
		assemblyState = b == 0 ? AssemblyState.Assembled : (b == 1 ? AssemblyState.Disassembled : AssemblyState.Paused);
		
		nullData = buf.readBoolean();
		if (!nullData) {
			flowDir = EnumFacing.byIndex(buf.readInt());
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
		buf.writeByte(assemblyState == AssemblyState.Assembled ? 0 : (assemblyState == AssemblyState.Disassembled ? 1 : 2));
		
		buf.writeBoolean(nullData);
		if (!nullData) {
			buf.writeInt(flowDir.getIndex());
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
