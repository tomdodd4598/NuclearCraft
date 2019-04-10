package nc.network.tile;

import java.util.List;

import io.netty.buffer.ByteBuf;
import nc.tile.generator.TileFusionCore;
import nc.tile.internal.fluid.Tank;
import nc.tile.internal.fluid.Tank.TankInfo;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class FusionUpdatePacket extends TileUpdatePacket {
	
	public double time;
	public int energyStored;
	public double baseProcessTime;
	public double baseProcessPower;
	public double processPower;
	public boolean isProcessing;
	public double heat;
	public double efficiency;
	public double speedMultiplier;
	public int size;
	public int complete;
	public double cooling;
	public double heatChange;
	public boolean hasConsumed;
	public boolean computerActivated;
	public String problem;
	public byte numberOfTanks;
	public List<TankInfo> tanksInfo;
	
	public FusionUpdatePacket() {
		messageValid = false;
	}
	
	public FusionUpdatePacket(BlockPos pos, double time, int energyStored, double baseProcessTime, double baseProcessPower, double processPower, boolean isProcessing, double heat, double efficiency, double speedMultiplier, int size, int complete, double cooling, double heatChange, boolean hasConsumed, boolean computerActivated, String problem, List<Tank> tanks) {
		this.pos = pos;
		this.time = time;
		this.energyStored = energyStored;
		this.baseProcessTime = baseProcessTime;
		this.baseProcessPower = baseProcessPower;
		this.processPower = processPower;
		this.isProcessing = isProcessing;
		this.heat = heat;
		this.efficiency = efficiency;
		this.speedMultiplier = speedMultiplier;
		this.size = size;
		this.complete = complete;
		this.cooling = cooling;
		this.heatChange = heatChange;
		this.hasConsumed = hasConsumed;
		this.computerActivated = computerActivated;
		this.problem = problem;
		numberOfTanks = (byte) tanks.size();
		tanksInfo = TankInfo.infoList(tanks);
		
		messageValid = true;
	}
	
	@Override
	public void readMessage(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		time = buf.readDouble();
		energyStored = buf.readInt();
		baseProcessTime = buf.readDouble();
		baseProcessPower = buf.readDouble();
		processPower = buf.readDouble();
		isProcessing = buf.readBoolean();
		heat = buf.readDouble();
		efficiency = buf.readDouble();
		speedMultiplier = buf.readDouble();
		size = buf.readInt();
		complete = buf.readInt();
		cooling = buf.readDouble();
		heatChange = buf.readDouble();
		hasConsumed = buf.readBoolean();
		computerActivated = buf.readBoolean();
		problem = ByteBufUtils.readUTF8String(buf);
		numberOfTanks = buf.readByte();
		tanksInfo = TankInfo.readBuf(buf, numberOfTanks);
	}
	
	@Override
	public void writeMessage(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeDouble(time);
		buf.writeInt(energyStored);
		buf.writeDouble(baseProcessTime);
		buf.writeDouble(baseProcessPower);
		buf.writeDouble(processPower);
		buf.writeBoolean(isProcessing);
		buf.writeDouble(heat);
		buf.writeDouble(efficiency);
		buf.writeDouble(speedMultiplier);
		buf.writeInt(size);
		buf.writeInt(complete);
		buf.writeDouble(cooling);
		buf.writeDouble(heatChange);
		buf.writeBoolean(hasConsumed);
		buf.writeBoolean(computerActivated);
		ByteBufUtils.writeUTF8String(buf, problem);
		buf.writeByte(numberOfTanks);
		for (TankInfo info : tanksInfo) info.writeBuf(buf);
	}
	
	public static class Handler extends TileUpdatePacket.Handler<FusionUpdatePacket, TileFusionCore> {
		
		@Override
		protected void onPacket(FusionUpdatePacket message, TileFusionCore core) {
			core.onGuiPacket(message);
		}
	}
}
