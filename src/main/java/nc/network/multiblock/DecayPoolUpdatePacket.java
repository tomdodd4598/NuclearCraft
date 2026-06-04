package nc.network.multiblock;

import io.netty.buffer.ByteBuf;
import nc.multiblock.machine.Machine;
import nc.recipe.RecipeUnitInfo;
import nc.tile.TileContainerInfo;
import nc.tile.internal.fluid.Tank;
import nc.tile.machine.*;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class DecayPoolUpdatePacket extends MachineUpdatePacket {
	
	public double totalDecayRate;
	
	public DecayPoolUpdatePacket() {
		super();
	}
	
	public DecayPoolUpdatePacket(BlockPos pos, boolean isMachineOn, boolean isProcessing, double time, double baseProcessTime, double baseProcessPower, List<Tank> tanks, double baseSpeedMultiplier, double basePowerMultiplier, RecipeUnitInfo recipeUnitInfo, boolean readyToProcess, double totalDecayRate) {
		super(pos, isMachineOn, isProcessing, time, baseProcessTime, baseProcessPower, tanks, baseSpeedMultiplier, basePowerMultiplier, recipeUnitInfo, readyToProcess);
		this.totalDecayRate = totalDecayRate;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		totalDecayRate = buf.readDouble();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		buf.writeDouble(totalDecayRate);
	}
	
	public static class Handler extends MultiblockUpdatePacket.Handler<Machine, IMachinePart, MachineUpdatePacket, TileDecayPoolController, TileContainerInfo<TileDecayPoolController>, DecayPoolUpdatePacket> {
		
		public Handler() {
			super(TileDecayPoolController.class);
		}
		
		@Override
		protected void onPacket(DecayPoolUpdatePacket message, Machine multiblock) {
			multiblock.onMultiblockUpdatePacket(message);
		}
	}
}
