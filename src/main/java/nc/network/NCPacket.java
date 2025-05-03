package nc.network;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.ints.*;
import nc.init.NCPackets;
import nc.recipe.RecipeUnitInfo;
import nc.tile.internal.fluid.Tank.TankInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.*;
import net.minecraftforge.fml.common.network.simpleimpl.*;

import java.util.*;

public abstract class NCPacket implements IMessage {
	
	public NCPacket() {}
	
	public SimpleNetworkWrapper getWrapper() {
		return NCPackets.wrapper;
	}
	
	public void sendToAll() {
		getWrapper().sendToAll(this);
	}
	
	public void sendTo(EntityPlayer player) {
		if (player instanceof EntityPlayerMP playerMP) {
			getWrapper().sendTo(this, playerMP);
		}
	}
	
	public <T extends EntityPlayer> void sendTo(Iterable<T> players) {
		for (T player : players) {
			sendTo(player);
		}
	}
	
	public void sendToAllAround(NetworkRegistry.TargetPoint point) {
		getWrapper().sendToAllAround(this, point);
	}
	
	public void sendToAllTracking(NetworkRegistry.TargetPoint point) {
		getWrapper().sendToAllTracking(this, point);
	}
	
	public void sendToAllTracking(Entity entity) {
		getWrapper().sendToAllTracking(this, entity);
	}
	
	public void sendToDimension(int dimensionId) {
		getWrapper().sendToDimension(this, dimensionId);
	}
	
	public void sendToServer() {
		getWrapper().sendToServer(this);
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
	
	protected static IntList readInts(ByteBuf buf) {
		int count = buf.readInt();
		IntList intList = new IntArrayList();
		for (int i = 0; i < count; ++i) {
			intList.add(buf.readInt());
		}
		return intList;
	}
	
	protected static void writeInts(ByteBuf buf, IntList ints) {
		buf.writeInt(ints.size());
		for (int i : ints) {
			buf.writeInt(i);
		}
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
	
	protected static RecipeUnitInfo readRecipeUnitInfo(ByteBuf buf) {
		return new RecipeUnitInfo(readString(buf), buf.readInt(), buf.readDouble());
	}
	
	protected static void writeRecipeUnitInfo(ByteBuf buf, RecipeUnitInfo info) {
		writeString(buf, info.unit);
		buf.writeInt(info.startingPrefix);
		buf.writeDouble(info.rateMultiplier);
	}
}
