package nc.network.multiblock;

import io.netty.buffer.ByteBuf;
import nc.multiblock.machine.Machine;
import nc.recipe.RecipeUnitInfo;
import nc.tile.TileContainerInfo;
import nc.tile.internal.fluid.Tank;
import nc.tile.machine.*;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class DistillerUpdatePacket extends MachineUpdatePacket {
	
	public double refluxUnitBonus;
	public double reboilingUnitBonus;
	public double liquidDistributorBonus;
	
	public DistillerUpdatePacket() {
		super();
	}
	
	public DistillerUpdatePacket(BlockPos pos, boolean isMachineOn, boolean isProcessing, double time, double baseProcessTime, double baseProcessPower, List<Tank> tanks, double baseSpeedMultiplier, double basePowerMultiplier, RecipeUnitInfo recipeUnitInfo, boolean readyToProcess, double refluxUnitBonus, double reboilingUnitBonus, double liquidDistributorBonus) {
		super(pos, isMachineOn, isProcessing, time, baseProcessTime, baseProcessPower, tanks, baseSpeedMultiplier, basePowerMultiplier, recipeUnitInfo, readyToProcess);
		this.refluxUnitBonus = refluxUnitBonus;
		this.reboilingUnitBonus = reboilingUnitBonus;
		this.liquidDistributorBonus = liquidDistributorBonus;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		refluxUnitBonus = buf.readDouble();
		reboilingUnitBonus = buf.readDouble();
		liquidDistributorBonus = buf.readDouble();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		buf.writeDouble(refluxUnitBonus);
		buf.writeDouble(reboilingUnitBonus);
		buf.writeDouble(liquidDistributorBonus);
	}
	
	public static class Handler extends MultiblockUpdatePacket.Handler<Machine, IMachinePart, MachineUpdatePacket, TileDistillerController, TileContainerInfo<TileDistillerController>, DistillerUpdatePacket> {
		
		public Handler() {
			super(TileDistillerController.class);
		}
		
		@Override
		protected void onPacket(DistillerUpdatePacket message, Machine multiblock) {
			multiblock.onMultiblockUpdatePacket(message);
		}
	}
}
