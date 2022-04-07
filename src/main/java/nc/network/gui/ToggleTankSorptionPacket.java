package nc.network.gui;

import io.netty.buffer.ByteBuf;
import nc.tile.fluid.ITileFluid;
import nc.tile.internal.fluid.TankSorption;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.Side;

public class ToggleTankSorptionPacket implements IMessage {
	
	protected BlockPos pos;
	protected int side;
	protected int tank;
	protected int sorption;
	
	public ToggleTankSorptionPacket() {
		
	}
	
	public ToggleTankSorptionPacket(ITileFluid machine, EnumFacing side, int tank, TankSorption sorption) {
		pos = machine.getTilePos();
		this.side = side.getIndex();
		this.tank = tank;
		this.sorption = sorption.ordinal();
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		side = buf.readInt();
		tank = buf.readInt();
		sorption = buf.readInt();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeInt(side);
		buf.writeInt(tank);
		buf.writeInt(sorption);
	}
	
	public static class Handler implements IMessageHandler<ToggleTankSorptionPacket, IMessage> {
		
		@Override
		public IMessage onMessage(ToggleTankSorptionPacket message, MessageContext ctx) {
			if (ctx.side == Side.SERVER) {
				FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));
			}
			return null;
		}
		
		void processMessage(ToggleTankSorptionPacket message, MessageContext ctx) {
			EntityPlayerMP player = ctx.getServerHandler().player;
			World world = player.getServerWorld();
			if (!world.isBlockLoaded(message.pos) || !world.isBlockModifiable(player, message.pos)) {
				return;
			}
			TileEntity tile = world.getTileEntity(message.pos);
			if (tile instanceof ITileFluid) {
				ITileFluid machine = (ITileFluid) tile;
				machine.setTankSorption(EnumFacing.byIndex(message.side), message.tank, TankSorption.values()[message.sorption]);
				machine.markDirtyAndNotify(true);
			}
		}
	}
}
