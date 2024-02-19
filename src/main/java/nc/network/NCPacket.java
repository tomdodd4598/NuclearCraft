package nc.network;

import java.util.*;

import io.netty.buffer.ByteBuf;
import nc.tile.internal.fluid.Tank.TankInfo;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public abstract class NCPacket implements IMessage {
	
	public NCPacket() {
		
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		
	}
	
	protected static String readString(ByteBuf buf) {
		return ByteBufUtils.readUTF8String(buf);
	}
	
	protected static void writeString(ByteBuf buf, String string) {
		ByteBufUtils.writeUTF8String(buf, string);
	}
	
	protected static BlockPos readPos(ByteBuf buf) {
		return new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
	}
	
	protected static void writePos(ByteBuf buf, BlockPos pos) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
	}
	
	protected static ItemStack readStack(ByteBuf buf) {
		return ByteBufUtils.readItemStack(buf);
	}
	
	protected static void writeStack(ByteBuf buf, ItemStack stack) {
		ByteBufUtils.writeItemStack(buf, stack);
	}
	
	protected static List<ItemStack> readStacks(ByteBuf buf) {
		int count = buf.readInt();
		List<ItemStack> stackList = new ArrayList<>();
		for (int i = 0; i < count; ++i) {
			stackList.add(readStack(buf));
		}
		return stackList;
	}
	
	protected static void writeStacks(ByteBuf buf, List<ItemStack> stacks) {
		buf.writeInt(stacks.size());
		for (ItemStack stack : stacks) {
			writeStack(buf, stack);
		}
	}
	
	protected static List<TankInfo> readTankInfos(ByteBuf buf) {
		int count = buf.readInt();
		List<TankInfo> infos = new ArrayList<>();
		for (int i = 0; i < count; ++i) {
			infos.add(new TankInfo(readString(buf), buf.readInt()));
		}
		return infos;
	}
	
	protected static void writeTankInfos(ByteBuf buf, List<TankInfo> infos) {
		buf.writeInt(infos.size());
		for (TankInfo info : infos) {
			writeString(buf, info.name);
			buf.writeInt(info.amount);
		}
	}
	
}
