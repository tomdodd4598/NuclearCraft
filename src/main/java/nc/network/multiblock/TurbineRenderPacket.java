package nc.network.multiblock;

import io.netty.buffer.ByteBuf;
import nc.multiblock.turbine.Turbine;
import nc.tile.turbine.*;
import net.minecraft.util.math.BlockPos;

public class TurbineRenderPacket extends MultiblockUpdatePacket {
	
	public String particleEffect;
	public double particleSpeedMult, recipeInputRateFP;
	public float angVel;
	public boolean isProcessing;
	public int recipeInputRate;
	
	public TurbineRenderPacket() {
		super();
	}
	
	public TurbineRenderPacket(BlockPos pos, String particleEffect, double particleSpeedMult, float angVel, boolean isProcessing, int recipeInputRate, double recipeInputRateFP) {
		super(pos);
		this.particleEffect = particleEffect;
		this.particleSpeedMult = particleSpeedMult;
		this.angVel = angVel;
		this.isProcessing = isProcessing;
		this.recipeInputRate = recipeInputRate;
		this.recipeInputRateFP = recipeInputRateFP;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		particleEffect = readString(buf);
		particleSpeedMult = buf.readDouble();
		angVel = buf.readFloat();
		isProcessing = buf.readBoolean();
		recipeInputRate = buf.readInt();
		recipeInputRateFP = buf.readDouble();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		writeString(buf, particleEffect);
		buf.writeDouble(particleSpeedMult);
		buf.writeFloat(angVel);
		buf.writeBoolean(isProcessing);
		buf.writeInt(recipeInputRate);
		buf.writeDouble(recipeInputRateFP);
	}
	
	public static class Handler extends MultiblockUpdatePacket.Handler<Turbine, ITurbinePart, TurbineUpdatePacket, TileTurbineController, TurbineRenderPacket> {
		
		public Handler() {
			super(TileTurbineController.class);
		}
		
		@Override
		protected void onPacket(TurbineRenderPacket message, Turbine multiblock) {
			multiblock.onRenderPacket(message);
		}
	}
}
