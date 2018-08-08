package nc.network;

import java.io.IOException;
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
	
	abstract protected void read() throws IOException;
	abstract protected void write() throws IOException;
	abstract protected REPLY executeOnClient();
	abstract protected REPLY executeOnServer();
	
	public SimplePacketBase() {
		write = Unpooled.buffer();
	}
	
	public TileEntity readClientTileEntity() throws IOException {
		int dimensionId = readInt();
		int x = readInt();
		int y = readInt();
		int z = readInt();
		return WorldHelper.getTileClient(dimensionId, x, y, z);
	}
	
	
	public TileEntity readServerTileEntity() throws IOException {
		int dimensionId = readInt();
		int x = readInt();
		int y = readInt();
		int z = readInt();
		return WorldHelper.getTileServer(dimensionId, x, y, z);
	}
	
	public byte[] readByteArray() throws IOException {
		return readByteArrayData(read.readUnsignedShort());
	}
	
	public byte[] readByteArrayData(int size) throws IOException {
		byte[] data = new byte[size];
		read.readBytes(data, 0, size);
		return data;
	}
	
	public Object readJSON(Type type) throws IOException {
		return gson.fromJson(ByteBufUtils.readUTF8String(read), type);
	}
	
	public <T> T readJSON(Class<T> type) throws IOException {
		return gson.fromJson(ByteBufUtils.readUTF8String(read), type);
	}
	
	public byte readByte() throws IOException {
		return read.readByte();
	}
	
	public short readShort() throws IOException {
		return read.readShort();
	}
	
	public int readUnsignedByte() throws IOException {
		return read.readUnsignedByte();
	}
	
	public int readUnsignedShort() throws IOException {
		return read.readUnsignedShort();
	}
	
	public int readInt() throws IOException {
		return read.readInt();
	}
	
	public long readLong() throws IOException {
		return read.readLong();
	}
	
	public double readDouble() throws IOException {
		return read.readDouble();
	}
	
	public float readFloat() throws IOException {
		return read.readFloat();
	}
	
	public String readString() throws IOException {
		return ByteBufUtils.readUTF8String(read);
	}
	
	public boolean readBoolean() throws IOException {
		return read.readBoolean();
	}
	
	public SimplePacketBase<REQ, REPLY> writeTileLocation(TileEntity tile) throws IOException, RuntimeException {
		if(tile.getWorld() == null) throw new RuntimeException("World does not exist!");
		if(tile.isInvalid()) throw new RuntimeException("TileEntity is invalid!");
		write.writeInt(tile.getWorld().provider.getDimension());
		write.writeInt(tile.getPos().getX());
		write.writeInt(tile.getPos().getY());
		write.writeInt(tile.getPos().getZ());
		return this;
	}
	
	public SimplePacketBase<REQ, REPLY> writeByteArray(byte[] array) throws IOException, RuntimeException {
		if(array.length > 65535) throw new RuntimeException("Invalid array size!");
		write.writeShort(array.length);
		write.writeBytes(array);
		return this;
	}
	
	public SimplePacketBase<REQ, REPLY> writeByteArrayData(byte[] array) throws IOException {
		write.writeBytes(array);
		return this;
	}
	
	public SimplePacketBase<REQ, REPLY> writeByte(byte b) throws IOException {
		write.writeByte(b);
		return this;
	}
	
	public SimplePacketBase<REQ, REPLY> writeBoolean(boolean b) throws IOException {
		write.writeBoolean(b);
		return this;
	}
	
	public SimplePacketBase<REQ, REPLY> writeString(String s) throws IOException {
		ByteBufUtils.writeUTF8String(write, s);
		return this;
	}
	
	public SimplePacketBase<REQ, REPLY> writeShort(short s) throws IOException {
		write.writeShort(s);
		return this;
	}
	
	public SimplePacketBase<REQ, REPLY> writeInt(int i) throws IOException {
		write.writeInt(i);
		return this;
	}
	
	public SimplePacketBase<REQ, REPLY> writeDouble(double d) throws IOException {
		write.writeDouble(d);
		return this;
	}
	
	public SimplePacketBase<REQ, REPLY> writeFloat(float f) throws IOException {
		write.writeFloat(f);
		return this;
	}
	
	public SimplePacketBase<REQ, REPLY> writeLong(long l) throws IOException {
		write.writeLong(l);
		return this;
	}

	@Override
	public final void fromBytes(ByteBuf buf) {
		read = buf;
		try {
			read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public final void toBytes(ByteBuf buf) {
		try {
			write();
		} catch (IOException e) {
			e.printStackTrace();
		}
		buf.writeBytes(write);
	}

	@Override
	public final REPLY onMessage(REQ message, MessageContext ctx) {
		if(ctx.side == Side.SERVER) return message.executeOnServer();
		else return message.executeOnClient();
	}
}
