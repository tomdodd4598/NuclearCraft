package nc.network.multiblock;

import io.netty.buffer.ByteBuf;
import nc.recipe.RecipeUnitInfo;
import nc.tile.internal.fluid.Tank;
import nc.tile.internal.fluid.Tank.TankInfo;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class MachineUpdatePacket extends MultiblockUpdatePacket {
	
	public boolean isMachineOn;
	public boolean isProcessing;
	public double time;
	public double baseProcessTime;
	public double baseProcessPower;
	public List<TankInfo> tankInfos;
	public double baseSpeedMultiplier;
	public double basePowerMultiplier;
	public RecipeUnitInfo recipeUnitInfo;
	public boolean readyToProcess;
	
	public MachineUpdatePacket() {
		super();
	}
	
	public MachineUpdatePacket(BlockPos pos, boolean isMachineOn, boolean isProcessing, double time, double baseProcessTime, double baseProcessPower, List<Tank> tanks, double baseSpeedMultiplier, double basePowerMultiplier, RecipeUnitInfo recipeUnitInfo, boolean readyToProcess) {
		super(pos);
		this.isMachineOn = isMachineOn;
		this.isProcessing = isProcessing;
		this.time = time;
		this.baseProcessTime = baseProcessTime;
		this.baseProcessPower = baseProcessPower;
		tankInfos = TankInfo.getInfoList(tanks);
		this.baseSpeedMultiplier = baseSpeedMultiplier;
		this.basePowerMultiplier = basePowerMultiplier;
		this.recipeUnitInfo = recipeUnitInfo;
		this.readyToProcess = readyToProcess;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		isMachineOn = buf.readBoolean();
		isProcessing = buf.readBoolean();
		time = buf.readDouble();
		baseProcessTime = buf.readDouble();
		baseProcessPower = buf.readDouble();
		tankInfos = readTankInfos(buf);
		baseSpeedMultiplier = buf.readDouble();
		basePowerMultiplier = buf.readDouble();
		recipeUnitInfo = readRecipeUnitInfo(buf);
		readyToProcess = buf.readBoolean();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		buf.writeBoolean(isMachineOn);
		buf.writeBoolean(isProcessing);
		buf.writeDouble(time);
		buf.writeDouble(baseProcessTime);
		buf.writeDouble(baseProcessPower);
		writeTankInfos(buf, tankInfos);
		buf.writeDouble(baseSpeedMultiplier);
		buf.writeDouble(basePowerMultiplier);
		writeRecipeUnitInfo(buf, recipeUnitInfo);
		buf.writeBoolean(readyToProcess);
	}
}
