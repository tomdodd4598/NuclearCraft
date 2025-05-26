package nc.network.multiblock;

import io.netty.buffer.ByteBuf;
import nc.multiblock.hx.HeatExchanger;
import nc.tile.TileContainerInfo;
import nc.tile.hx.*;
import nc.tile.internal.fluid.Tank;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class CondenserRenderPacket extends HeatExchangerRenderPacket {
	
	public CondenserRenderPacket() {
		super();
	}
	
	public CondenserRenderPacket(BlockPos pos, List<Tank> shellTanks) {
		super(pos, shellTanks);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
	}
	
	public static class Handler extends MultiblockUpdatePacket.Handler<HeatExchanger, IHeatExchangerPart, HeatExchangerUpdatePacket, TileCondenserController, TileContainerInfo<TileCondenserController>, CondenserRenderPacket> {
		
		public Handler() {
			super(TileCondenserController.class);
		}
		
		@Override
		protected void onPacket(CondenserRenderPacket message, HeatExchanger multiblock) {
			multiblock.onRenderPacket(message);
		}
	}
}
