package nc.network;

import java.lang.reflect.Field;

import io.netty.buffer.ByteBuf;
import nc.util.NCUtil;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class ReturnFluidInTankPacket implements IMessage {

	boolean messageValid;
	
	FluidStack fluid;
	
	String className;
	String fluidFieldName;
	
	public ReturnFluidInTankPacket() {
		messageValid = false;
	}
	
	public ReturnFluidInTankPacket(FluidStack fluid, String className, String fluidFieldName) {
		this.fluid = fluid;
		this.className = className;
		this.fluidFieldName = fluidFieldName;		
		messageValid = true;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		try {
			String fluidName = ByteBufUtils.readUTF8String(buf);
			int fluidAmount = buf.readInt();
			fluid = (fluidName != "nullFluid" && fluidAmount != 0) ? new FluidStack(FluidRegistry.getFluid(fluidName), fluidAmount) : null;
			className = ByteBufUtils.readUTF8String(buf);
			fluidFieldName = ByteBufUtils.readUTF8String(buf);
		} catch (IndexOutOfBoundsException ioe) {
			NCUtil.getLogger().catching(ioe);
			return;
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		if (!messageValid) return;
		ByteBufUtils.writeUTF8String(buf, fluid != null ? fluid.getFluid().getName() : "nullFluid");
		buf.writeInt(fluid != null ? fluid.amount : 0);
		ByteBufUtils.writeUTF8String(buf, className);
		ByteBufUtils.writeUTF8String(buf, fluidFieldName);
	}

	public static class Handler implements IMessageHandler<ReturnFluidInTankPacket, IMessage> {

		@Override
		public IMessage onMessage(ReturnFluidInTankPacket message, MessageContext ctx) {
			if (!message.messageValid && ctx.side != Side.CLIENT) return null;
			Minecraft.getMinecraft().addScheduledTask(() -> processMessage(message));
			return null;
		}
		
		void processMessage(ReturnFluidInTankPacket message) {
			try {
				Class clazz = Class.forName(message.className);
				Field fluidField = clazz.getDeclaredField(message.fluidFieldName);
				fluidField.set(clazz, message.fluid);
			} catch (Exception e) {
				NCUtil.getLogger().catching(e);
			}
		}
	}
}
