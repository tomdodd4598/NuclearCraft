package nc.network.tile;

import io.netty.buffer.ByteBuf;
import nc.network.NCPacket;
import nc.tile.*;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.Side;

public abstract class TileUpdatePacket extends NCPacket {
	
	protected BlockPos pos;
	
	public TileUpdatePacket() {
		super();
	}
	
	public TileUpdatePacket(BlockPos pos) {
		super();
		this.pos = pos;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		pos = readPos(buf);
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		writePos(buf, pos);
	}
	
	public static abstract class Handler<MESSAGE extends TileUpdatePacket, TILE extends ITilePacket<MESSAGE>> implements IMessageHandler<MESSAGE, IMessage> {
		
		@SuppressWarnings("unchecked")
		@Override
		public IMessage onMessage(MESSAGE message, MessageContext ctx) {
			if (ctx.side == Side.CLIENT) {
				Minecraft.getMinecraft().addScheduledTask(() -> {
					TileEntity tile = Minecraft.getMinecraft().player.world.getTileEntity(message.pos);
					if (tile instanceof ITilePacket) {
						((TILE) tile).onTileUpdatePacket(message);
					}
				});
			}
			return null;
		}
	}
}
