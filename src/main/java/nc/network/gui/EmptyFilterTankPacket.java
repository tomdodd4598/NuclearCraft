package nc.network.gui;

import io.netty.buffer.ByteBuf;
import nc.tile.fluid.ITileFilteredFluid;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.Side;

public class EmptyFilterTankPacket implements IMessage {
	
	protected BlockPos pos;
	protected int tankNo;
	
	public EmptyFilterTankPacket() {
		
	}
	
	public EmptyFilterTankPacket(ITileFilteredFluid machine, int tankNo) {
		pos = machine.getTilePos();
		this.tankNo = tankNo;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		tankNo = buf.readInt();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeInt(tankNo);
	}
	
	public static class Handler implements IMessageHandler<EmptyFilterTankPacket, IMessage> {
		
		@Override
		public IMessage onMessage(EmptyFilterTankPacket message, MessageContext ctx) {
			if (ctx.side == Side.SERVER) {
				FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));
			}
			return null;
		}
		
		void processMessage(EmptyFilterTankPacket message, MessageContext ctx) {
			EntityPlayerMP player = ctx.getServerHandler().player;
			World world = player.getServerWorld();
			if (!world.isBlockLoaded(message.pos) || !world.isBlockModifiable(player, message.pos)) {
				return;
			}
			TileEntity tile = world.getTileEntity(message.pos);
			if (tile instanceof ITileFilteredFluid) {
				ITileFilteredFluid machine = (ITileFilteredFluid) tile;
				machine.clearFilterTank(message.tankNo);
				tile.markDirty();
			}
		}
	}
}
