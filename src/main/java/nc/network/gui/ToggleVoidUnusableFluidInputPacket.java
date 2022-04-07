package nc.network.gui;

import io.netty.buffer.ByteBuf;
import nc.tile.fluid.ITileFluid;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.Side;

public class ToggleVoidUnusableFluidInputPacket implements IMessage {
	
	protected BlockPos pos;
	protected boolean voidUnusableFluidInput;
	protected int tankNumber;
	
	public ToggleVoidUnusableFluidInputPacket() {
		
	}
	
	public ToggleVoidUnusableFluidInputPacket(ITileFluid machine, int tankNumber) {
		pos = machine.getTilePos();
		voidUnusableFluidInput = machine.getVoidUnusableFluidInput(tankNumber);
		this.tankNumber = tankNumber;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		voidUnusableFluidInput = buf.readBoolean();
		tankNumber = buf.readInt();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeBoolean(voidUnusableFluidInput);
		buf.writeInt(tankNumber);
	}
	
	public static class Handler implements IMessageHandler<ToggleVoidUnusableFluidInputPacket, IMessage> {
		
		@Override
		public IMessage onMessage(ToggleVoidUnusableFluidInputPacket message, MessageContext ctx) {
			if (ctx.side == Side.SERVER) {
				FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));
			}
			return null;
		}
		
		void processMessage(ToggleVoidUnusableFluidInputPacket message, MessageContext ctx) {
			EntityPlayerMP player = ctx.getServerHandler().player;
			World world = player.getServerWorld();
			if (!world.isBlockLoaded(message.pos) || !world.isBlockModifiable(player, message.pos)) {
				return;
			}
			TileEntity tile = world.getTileEntity(message.pos);
			if (tile instanceof ITileFluid) {
				ITileFluid machine = (ITileFluid) tile;
				machine.setVoidUnusableFluidInput(message.tankNumber, message.voidUnusableFluidInput);
				tile.markDirty();
			}
		}
	}
}
