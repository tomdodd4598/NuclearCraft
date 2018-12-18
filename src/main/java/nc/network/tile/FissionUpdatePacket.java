package nc.network.tile;

import io.netty.buffer.ByteBuf;
import nc.tile.generator.TileFissionController;
import net.minecraft.util.math.BlockPos;

public class FissionUpdatePacket extends TileUpdatePacket {
	
	public double time;
	public int energyStored;
	public double baseProcessTime;
	public double baseProcessPower;
	public double heat;
	public double cooling;
	public double efficiency;
	public int cells;
	public double speedMultiplier;
	public int lengthX;
	public int lengthY;
	public int lengthZ;
	public double heatChange;
	public int complete;
	public int ready;
	public int problemPosX;
	public int problemPosY;
	public int problemPosZ;
	public double heatMult;
	public boolean hasConsumed;
	public boolean computerActivated;
	
	public FissionUpdatePacket() {
		messageValid = false;
	}
	
	public FissionUpdatePacket(BlockPos pos, double time, int energyStored, double baseProcessTime, double baseProcessPower, double heat, double cooling, double efficiency, int cells, double speedMultiplier, int lengthX, int lengthY, int lengthZ, double heatChange, int complete, int ready, int problemPosX, int problemPosY, int problemPosZ, double heatMult, boolean hasConsumed, boolean computerActivated) {
		this.pos = pos;
		this.time = time;
		this.energyStored = energyStored;
		this.baseProcessTime = baseProcessTime;
		this.baseProcessPower = baseProcessPower;
		this.heat = heat;
		this.cooling = cooling;
		this.efficiency = efficiency;
		this.cells = cells;
		this.speedMultiplier = speedMultiplier;
		this.lengthX = lengthX;
		this.lengthY = lengthY;
		this.lengthZ = lengthZ;
		this.heatChange = heatChange;
		this.complete = complete;
		this.ready = ready;
		this.problemPosX = problemPosX;
		this.problemPosY = problemPosY;
		this.problemPosZ = problemPosZ;
		this.heatMult = heatMult;
		this.hasConsumed = hasConsumed;
		this.computerActivated = computerActivated;
		
		messageValid = true;
	}
	
	@Override
	public void readMessage(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		time = buf.readDouble();
		energyStored = buf.readInt();
		baseProcessTime = buf.readDouble();
		baseProcessPower = buf.readDouble();
		heat = buf.readDouble();
		cooling = buf.readDouble();
		efficiency = buf.readDouble();
		cells = buf.readInt();
		speedMultiplier = buf.readDouble();
		lengthX = buf.readInt();
		lengthY = buf.readInt();
		lengthZ = buf.readInt();
		heatChange = buf.readDouble();
		complete = buf.readInt();
		ready = buf.readInt();
		problemPosX = buf.readInt();
		problemPosY = buf.readInt();
		problemPosZ = buf.readInt();
		heatMult = buf.readDouble();
		hasConsumed = buf.readBoolean();
		computerActivated = buf.readBoolean();
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
		buf.writeDouble(heat);
		buf.writeDouble(cooling);
		buf.writeDouble(efficiency);
		buf.writeInt(cells);
		buf.writeDouble(speedMultiplier);
		buf.writeInt(lengthX);
		buf.writeInt(lengthY);
		buf.writeInt(lengthZ);
		buf.writeDouble(heatChange);
		buf.writeInt(complete);
		buf.writeInt(ready);
		buf.writeInt(problemPosX);
		buf.writeInt(problemPosY);
		buf.writeInt(problemPosZ);
		buf.writeDouble(heatMult);
		buf.writeBoolean(hasConsumed);
		buf.writeBoolean(computerActivated);
	}
	
	public static class Handler extends TileUpdatePacket.Handler<FissionUpdatePacket, TileFissionController> {
		
		@Override
		protected void onPacket(FissionUpdatePacket message, TileFissionController controller) {
			controller.onGuiPacket(message);
		}
	}
}
