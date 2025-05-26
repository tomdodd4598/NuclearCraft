package nc.network.multiblock;

import io.netty.buffer.ByteBuf;
import nc.multiblock.machine.Machine;
import nc.recipe.RecipeUnitInfo;
import nc.tile.TileContainerInfo;
import nc.tile.internal.fluid.Tank;
import nc.tile.machine.*;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class InfiltratorUpdatePacket extends MachineUpdatePacket {
	
	public double pressureFluidEfficiency;
	public double heatingBonus;
	
	public InfiltratorUpdatePacket() {
		super();
	}
	
	public InfiltratorUpdatePacket(BlockPos pos, boolean isMachineOn, boolean isProcessing, double time, double baseProcessTime, double baseProcessPower, List<Tank> tanks, double baseSpeedMultiplier, double basePowerMultiplier, RecipeUnitInfo recipeUnitInfo, double pressureFluidEfficiency, double heatingBonus) {
		super(pos, isMachineOn, isProcessing, time, baseProcessTime, baseProcessPower, tanks, baseSpeedMultiplier, basePowerMultiplier, recipeUnitInfo);
		this.pressureFluidEfficiency = pressureFluidEfficiency;
		this.heatingBonus = heatingBonus;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		pressureFluidEfficiency = buf.readDouble();
		heatingBonus = buf.readDouble();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		buf.writeDouble(pressureFluidEfficiency);
		buf.writeDouble(heatingBonus);
	}
	
	public static class Handler extends MultiblockUpdatePacket.Handler<Machine, IMachinePart, MachineUpdatePacket, TileInfiltratorController, TileContainerInfo<TileInfiltratorController>, InfiltratorUpdatePacket> {
		
		public Handler() {
			super(TileInfiltratorController.class);
		}
		
		@Override
		protected void onPacket(InfiltratorUpdatePacket message, Machine multiblock) {
			multiblock.onMultiblockUpdatePacket(message);
		}
	}
}
