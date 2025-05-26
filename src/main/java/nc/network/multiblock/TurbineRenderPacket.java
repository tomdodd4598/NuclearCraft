package nc.network.multiblock;

import io.netty.buffer.ByteBuf;
import nc.multiblock.turbine.Turbine;
import nc.tile.TileContainerInfo;
import nc.tile.internal.fluid.Tank;
import nc.tile.internal.fluid.Tank.TankInfo;
import nc.tile.turbine.*;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class TurbineRenderPacket extends MultiblockUpdatePacket {
	
	public List<TankInfo> tankInfos;
	public String particleEffect;
	public double particleSpeedMult, recipeInputRateFP;
	public float angVel;
	public boolean isProcessing;
	public int recipeInputRate;
	
	public TurbineRenderPacket() {
		super();
	}
	
	public TurbineRenderPacket(BlockPos pos, List<Tank> tanks, String particleEffect, double particleSpeedMult, float angVel, boolean isProcessing, int recipeInputRate, double recipeInputRateFP) {
		super(pos);
		tankInfos = TankInfo.getInfoList(tanks);
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
		tankInfos = readTankInfos(buf);
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
		writeTankInfos(buf, tankInfos);
		writeString(buf, particleEffect);
		buf.writeDouble(particleSpeedMult);
		buf.writeFloat(angVel);
		buf.writeBoolean(isProcessing);
		buf.writeInt(recipeInputRate);
		buf.writeDouble(recipeInputRateFP);
	}
	
	public static class Handler extends MultiblockUpdatePacket.Handler<Turbine, ITurbinePart, TurbineUpdatePacket, TileTurbineController, TileContainerInfo<TileTurbineController>, TurbineRenderPacket> {
		
		public Handler() {
			super(TileTurbineController.class);
		}
		
		@Override
		protected void onPacket(TurbineRenderPacket message, Turbine multiblock) {
			multiblock.onRenderPacket(message);
		}
	}
}
