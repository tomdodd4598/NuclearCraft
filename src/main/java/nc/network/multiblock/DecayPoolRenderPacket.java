package nc.network.multiblock;

import io.netty.buffer.ByteBuf;
import nc.multiblock.machine.Machine;
import nc.tile.TileContainerInfo;
import nc.tile.internal.fluid.Tank;
import nc.tile.internal.fluid.Tank.TankInfo;
import nc.tile.machine.*;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class DecayPoolRenderPacket extends MachineRenderPacket {
	
	public boolean isProcessing;
	public List<TankInfo> tankInfos;
	
	public DecayPoolRenderPacket() {
		super();
	}
	
	public DecayPoolRenderPacket(BlockPos pos, boolean isMachineOn, boolean isProcessing, List<Tank> tanks) {
		super(pos, isMachineOn);
		this.isProcessing = isProcessing;
		tankInfos = TankInfo.getInfoList(tanks);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		isProcessing = buf.readBoolean();
		tankInfos = readTankInfos(buf);
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		buf.writeBoolean(isProcessing);
		writeTankInfos(buf, tankInfos);
	}
	
	public static class Handler extends MultiblockUpdatePacket.Handler<Machine, IMachinePart, MachineUpdatePacket, TileDecayPoolController, TileContainerInfo<TileDecayPoolController>, DecayPoolRenderPacket> {
		
		public Handler() {
			super(TileDecayPoolController.class);
		}
		
		@Override
		protected void onPacket(DecayPoolRenderPacket message, Machine multiblock) {
			multiblock.onRenderPacket(message);
		}
	}
}
