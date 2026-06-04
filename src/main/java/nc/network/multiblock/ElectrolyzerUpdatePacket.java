package nc.network.multiblock;

import io.netty.buffer.ByteBuf;
import nc.multiblock.machine.Machine;
import nc.recipe.RecipeUnitInfo;
import nc.tile.TileContainerInfo;
import nc.tile.internal.fluid.Tank;
import nc.tile.machine.*;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class ElectrolyzerUpdatePacket extends MachineUpdatePacket {
	
	public double electrolyteEfficiency;
	
	public ElectrolyzerUpdatePacket() {
		super();
	}
	
	public ElectrolyzerUpdatePacket(BlockPos pos, boolean isMachineOn, boolean isProcessing, double time, double baseProcessTime, double baseProcessPower, List<Tank> tanks, double baseSpeedMultiplier, double basePowerMultiplier, RecipeUnitInfo recipeUnitInfo, boolean readyToProcess, double electrolyteEfficiency) {
		super(pos, isMachineOn, isProcessing, time, baseProcessTime, baseProcessPower, tanks, baseSpeedMultiplier, basePowerMultiplier, recipeUnitInfo, readyToProcess);
		this.electrolyteEfficiency = electrolyteEfficiency;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		electrolyteEfficiency = buf.readDouble();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		buf.writeDouble(electrolyteEfficiency);
	}
	
	public static class Handler extends MultiblockUpdatePacket.Handler<Machine, IMachinePart, MachineUpdatePacket, TileElectrolyzerController, TileContainerInfo<TileElectrolyzerController>, ElectrolyzerUpdatePacket> {
		
		public Handler() {
			super(TileElectrolyzerController.class);
		}
		
		@Override
		protected void onPacket(ElectrolyzerUpdatePacket message, Machine multiblock) {
			multiblock.onMultiblockUpdatePacket(message);
		}
	}
}
