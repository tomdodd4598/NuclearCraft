package nc.network.gui;

import io.netty.buffer.ByteBuf;
import nc.network.PacketHandler;
import nc.tile.fluid.ITileFluid;
import nc.util.NCUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class GetFluidInTankPacket implements IMessage {
	
	boolean messageValid;
	
	BlockPos pos;
	int tankNumber;
	
	String className;
	String fluidFieldName;
	
	public GetFluidInTankPacket() {
		messageValid = false;
	}
	
	public GetFluidInTankPacket(BlockPos pos, int tankNumber, String className, String fluidFieldName) {
		this.pos = pos;
		this.tankNumber = tankNumber;
		this.className = className;
		this.fluidFieldName = fluidFieldName;		
		messageValid = true;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		try {
			pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
			tankNumber = buf.readInt();
			className = ByteBufUtils.readUTF8String(buf);
			fluidFieldName = ByteBufUtils.readUTF8String(buf);
		} catch (IndexOutOfBoundsException ioe) {
			NCUtil.getLogger().catching(ioe);
			return;
		}
		messageValid = true;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		if (!messageValid) return;
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeInt(tankNumber);
		ByteBufUtils.writeUTF8String(buf, className);
		ByteBufUtils.writeUTF8String(buf, fluidFieldName);
	}

	public static class Handler implements IMessageHandler<GetFluidInTankPacket, IMessage> {

		@Override
		public IMessage onMessage(GetFluidInTankPacket message, MessageContext ctx) {
			if (!message.messageValid && ctx.side != Side.SERVER) return null;
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));
			return null;
		}
		
		void processMessage(GetFluidInTankPacket message, MessageContext ctx) {
			TileEntity te = ctx.getServerHandler().player.getServerWorld().getTileEntity(message.pos);
			if (te == null) return;
			if (!(te instanceof ITileFluid)) return;
			FluidStack tankFluid = ((ITileFluid) te).getTanks().get(message.tankNumber).getFluid();
			PacketHandler.instance.sendTo(new ReturnFluidInTankPacket(tankFluid, message.className, message.fluidFieldName), ctx.getServerHandler().player);
		}
	}
}
