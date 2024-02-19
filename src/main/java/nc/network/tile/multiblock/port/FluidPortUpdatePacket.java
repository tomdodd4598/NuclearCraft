package nc.network.tile.multiblock.port;

import java.util.List;

import io.netty.buffer.ByteBuf;
import nc.network.tile.TileUpdatePacket;
import nc.tile.ITilePacket;
import nc.tile.internal.fluid.Tank;
import nc.tile.internal.fluid.Tank.TankInfo;
import net.minecraft.util.math.BlockPos;

public class FluidPortUpdatePacket extends TileUpdatePacket {
	
	public BlockPos masterPortPos;
	public List<TankInfo> tankInfos;
	public List<TankInfo> filterTankInfos;
	
	public FluidPortUpdatePacket() {
		super();
	}
	
	public FluidPortUpdatePacket(BlockPos pos, BlockPos masterPortPos, List<Tank> tanks, List<Tank> filterTanks) {
		super(pos);
		this.masterPortPos = masterPortPos;
		tankInfos = TankInfo.getInfoList(tanks);
		filterTankInfos = TankInfo.getInfoList(filterTanks);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		masterPortPos = readPos(buf);
		tankInfos = readTankInfos(buf);
		filterTankInfos = readTankInfos(buf);
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		writePos(buf, masterPortPos);
		writeTankInfos(buf, tankInfos);
		writeTankInfos(buf, filterTankInfos);
	}
	
	public static class Handler extends TileUpdatePacket.Handler<FluidPortUpdatePacket, ITilePacket<FluidPortUpdatePacket>> {}
}
