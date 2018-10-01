package nc.network;

import java.lang.reflect.Type;

import com.google.gson.Gson;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import nc.util.WorldHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public abstract class SimplePacketBase<REQ extends SimplePacketBase<REQ, REPLY>, REPLY extends IMessage> implements IMessage, IMessageHandler<REQ, REPLY> {
	private final ByteBuf write;
	private ByteBuf read;
	private final Gson gson = new Gson();
	
	abstract protected void read();
	abstract protected void write();
	abstract protected REPLY executeOnClient();
	abstract protected REPLY executeOnServer();
	
	public SimplePacketBase() {
		write = Unpooled.buffer();
	}
	
	public TileEntity readClientTileEntity() {
		int dimensionId = readInt();
		int x = readInt();
		int y = readInt();
		int z = readInt();
		return WorldHelper.getTileClient(dimensionId, x, y, z);
	}
	
	
	public TileEntity readServerTileEntity() {
		int dimensionId = readInt();
		int x = readInt();
		int y = readInt();
		int z = readInt();
		return WorldHelper.getTileServer(dimensionId, x, y, z);
	}
	
	public byte[] readByteArray() {
		return readByteArrayData(read.readUnsignedShort());
	}
	
	public byte[] readByteArrayData(int size) {
		byte[] data = new byte[size];
		read.readBytes(data, 0, size);
		return data;
	}
	
	public Object readJSON(Type type) {
		return gson.fromJson(ByteBufUtils.readUTF8String(read), type);
	}
	
	public <T> T readJSON(Class<T> type) {
		return gson.fromJson(ByteBufUtils.readUTF8String(read), type);
	}
	
	public byte readByte() {
		return read.readByte();
	}
	
	public short readShort() {
		return read.readShort();
	}
	
	public int readUnsignedByte() {
		return read.readUnsignedByte();
	}
	
	public int readUnsignedShort() {
		return read.readUnsignedShort();
	}
	
	public int readInt() {
		return read.readInt();
	}
	
	public long readLong() {
		return read.readLong();
	}
	
	public double readDouble() {
		return read.readDouble();
	}
	
	public float readFloat() {
		return read.readFloat();
	}
	
	public String readString() {
		return ByteBufUtils.readUTF8String(read);
	}
	
	public boolean readBoolean() {
		return read.readBoolean();
	}
	
	public SimplePacketBase<REQ, REPLY> writeTileLocation(TileEntity tile) throws RuntimeException {
		if(tile.getWorld() == null) throw new RuntimeException("World does not exist!");
		if(tile.isInvalid()) throw new RuntimeException("TileEntity is invalid!");
		write.writeInt(tile.getWorld().provider.getDimension());
		write.writeInt(tile.getPos().getX());
		write.writeInt(tile.getPos().getY());
		write.writeInt(tile.getPos().getZ());
		return this;
	}
	
	public SimplePacketBase<REQ, REPLY> writeByteArray(byte[] array) throws RuntimeException {
		if(array.length > 65535) throw new RuntimeException("Invalid array size!");
		write.writeShort(array.length);
		write.writeBytes(array);
		return this;
	}
	
	public SimplePacketBase<REQ, REPLY> writeByteArrayData(byte[] array) {
		write.writeBytes(array);
		return this;
	}
	
	public SimplePacketBase<REQ, REPLY> writeByte(byte b) {
		write.writeByte(b);
		return this;
	}
	
	public SimplePacketBase<REQ, REPLY> writeBoolean(boolean b) {
		write.writeBoolean(b);
		return this;
	}
	
	public SimplePacketBase<REQ, REPLY> writeString(String s) {
		ByteBufUtils.writeUTF8String(write, s);
		return this;
	}
	
	public SimplePacketBase<REQ, REPLY> writeShort(short s) {
		write.writeShort(s);
		return this;
	}
	
	public SimplePacketBase<REQ, REPLY> writeInt(int i) {
		write.writeInt(i);
		return this;
	}
	
	public SimplePacketBase<REQ, REPLY> writeDouble(double d) {
		write.writeDouble(d);
		return this;
	}
	
	public SimplePacketBase<REQ, REPLY> writeFloat(float f) {
		write.writeFloat(f);
		return this;
	}
	
	public SimplePacketBase<REQ, REPLY> writeLong(long l) {
		write.writeLong(l);
		return this;
	}

	@Override
	public final void fromBytes(ByteBuf buf) {
		read = buf;
		read();
	}

	@Override
	public final void toBytes(ByteBuf buf) {
		write();
		buf.writeBytes(write);
	}

	@Override
	public final REPLY onMessage(REQ message, MessageContext ctx) {
		if(ctx.side == Side.SERVER) return message.executeOnServer();
		else return message.executeOnClient();
	}
}
