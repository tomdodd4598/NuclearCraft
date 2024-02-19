package nc.network.gui;

import io.netty.buffer.ByteBuf;
import nc.network.NCPacket;
import nc.tile.ITile;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.Side;

public abstract class TileGuiPacket extends NCPacket {
	
	protected BlockPos pos;
	
	public TileGuiPacket() {
		super();
	}
	
	public TileGuiPacket(BlockPos pos) {
		super();
		this.pos = pos;
	}
	
	public TileGuiPacket(ITile tile) {
		this(tile.getTilePos());
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
	
	public static abstract class Handler<MESSAGE extends TileGuiPacket> implements IMessageHandler<MESSAGE, IMessage> {
		
		@Override
		public IMessage onMessage(MESSAGE message, MessageContext ctx) {
			if (ctx.side == Side.SERVER) {
				FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
					EntityPlayerMP player = ctx.getServerHandler().player;
					World world = player.getServerWorld();
					if (!world.isBlockLoaded(message.pos) || !world.isBlockModifiable(player, message.pos)) {
						return;
					}
					onPacket(message, player, world.getTileEntity(message.pos));
				});
			}
			return null;
		}
		
		protected abstract void onPacket(MESSAGE message, EntityPlayerMP player, TileEntity tile);
	}
}
