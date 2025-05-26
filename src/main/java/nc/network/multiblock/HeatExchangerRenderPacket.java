package nc.network.multiblock;

import io.netty.buffer.ByteBuf;
import nc.multiblock.hx.HeatExchanger;
import nc.tile.TileContainerInfo;
import nc.tile.hx.*;
import nc.tile.internal.fluid.Tank;
import nc.tile.internal.fluid.Tank.TankInfo;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class HeatExchangerRenderPacket extends MultiblockUpdatePacket {
	
	public List<TankInfo> shellTankInfos;
	
	public HeatExchangerRenderPacket() {
		super();
	}
	
	public HeatExchangerRenderPacket(BlockPos pos, List<Tank> shellTanks) {
		super(pos);
		shellTankInfos = TankInfo.getInfoList(shellTanks);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		shellTankInfos = readTankInfos(buf);
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		writeTankInfos(buf, shellTankInfos);
	}
	
	public static class Handler extends MultiblockUpdatePacket.Handler<HeatExchanger, IHeatExchangerPart, HeatExchangerUpdatePacket, TileHeatExchangerController, TileContainerInfo<TileHeatExchangerController>, HeatExchangerRenderPacket> {
		
		public Handler() {
			super(TileHeatExchangerController.class);
		}
		
		@Override
		protected void onPacket(HeatExchangerRenderPacket message, HeatExchanger multiblock) {
			multiblock.onRenderPacket(message);
		}
	}
}
