package nc.multiblock.network;

import java.util.List;

import io.netty.buffer.ByteBuf;
import nc.network.tile.TileUpdatePacket;
import nc.tile.ITileGui;
import nc.tile.internal.fluid.Tank;
import nc.tile.internal.fluid.Tank.TankInfo;
import net.minecraft.util.math.BlockPos;

public class FissionVesselPortUpdatePacket extends FissionPortUpdatePacket {
	
	public List<TankInfo> tanksInfo;
	public List<TankInfo> filterTanksInfo;
	
	public FissionVesselPortUpdatePacket() {
		super();
	}
	
	public FissionVesselPortUpdatePacket(BlockPos pos, BlockPos masterPortPos, List<Tank> tanks, List<Tank> filterTanks) {
		super(pos, masterPortPos);
		tanksInfo = TankInfo.infoList(tanks);
		filterTanksInfo = TankInfo.infoList(filterTanks);
	}
	
	@Override
	public void readMessage(ByteBuf buf) {
		super.readMessage(buf);
		byte numberOfTanks = buf.readByte();
		tanksInfo = TankInfo.readBuf(buf, numberOfTanks);
		byte numberOfFilterTanks = buf.readByte();
		filterTanksInfo = TankInfo.readBuf(buf, numberOfFilterTanks);
	}
	
	@Override
	public void writeMessage(ByteBuf buf) {
		super.writeMessage(buf);
		buf.writeByte(tanksInfo.size());
		for (TankInfo info : tanksInfo) info.writeBuf(buf);
		buf.writeByte(filterTanksInfo.size());
		for (TankInfo info : filterTanksInfo) info.writeBuf(buf);
	}
	
	public static class Handler extends TileUpdatePacket.Handler<FissionVesselPortUpdatePacket, ITileGui> {
		
		@Override
		protected void onPacket(FissionVesselPortUpdatePacket message, ITileGui processor) {
			processor.onGuiPacket(message);
		}
	}
}
