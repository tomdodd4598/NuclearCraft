package nc.network.multiblock;

import java.util.List;

import io.netty.buffer.ByteBuf;
import nc.network.tile.TileUpdatePacket;
import nc.tile.ITileGui;
import nc.tile.internal.fluid.Tank;
import nc.tile.internal.fluid.Tank.TankInfo;
import net.minecraft.util.math.BlockPos;

public class FissionHeaterPortUpdatePacket extends FissionPortUpdatePacket {
	
	public List<TankInfo> tanksInfo;
	public List<TankInfo> filterTanksInfo;
	
	public FissionHeaterPortUpdatePacket() {
		super();
	}
	
	public FissionHeaterPortUpdatePacket(BlockPos pos, BlockPos masterPortPos, List<Tank> tanks, List<Tank> filterTanks) {
		super(pos, masterPortPos);
		tanksInfo = TankInfo.infoList(tanks);
		filterTanksInfo = TankInfo.infoList(filterTanks);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		byte numberOfTanks = buf.readByte();
		tanksInfo = TankInfo.readBuf(buf, numberOfTanks);
		byte numberOfFilterTanks = buf.readByte();
		filterTanksInfo = TankInfo.readBuf(buf, numberOfFilterTanks);
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		buf.writeByte(tanksInfo.size());
		for (TankInfo info : tanksInfo) {
			info.writeBuf(buf);
		}
		buf.writeByte(filterTanksInfo.size());
		for (TankInfo info : filterTanksInfo) {
			info.writeBuf(buf);
		}
	}
	
	public static class Handler extends TileUpdatePacket.Handler<FissionHeaterPortUpdatePacket, ITileGui<FissionHeaterPortUpdatePacket>> {
		
		@Override
		protected void onTileUpdatePacket(FissionHeaterPortUpdatePacket message, ITileGui<FissionHeaterPortUpdatePacket> processor) {
			processor.onTileUpdatePacket(message);
		}
	}
}
