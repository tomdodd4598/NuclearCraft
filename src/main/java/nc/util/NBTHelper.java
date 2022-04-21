package nc.util;

import java.util.*;
import java.util.Map.Entry;

import javax.vecmath.Vector3f;

import it.unimi.dsi.fastutil.doubles.DoubleCollection;
import it.unimi.dsi.fastutil.floats.FloatCollection;
import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.longs.LongCollection;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.util.math.BlockPos;

public class NBTHelper {
	
	// ItemStack
	
	public static NBTTagCompound getStackNBT(ItemStack stack) {
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt == null && !stack.isEmpty()) {
			stack.setTagCompound(nbt = new NBTTagCompound());
		}
		return nbt;
	}
	
	// Inventory
	
	@SafeVarargs
	public static NBTTagCompound writeAllItems(NBTTagCompound tag, List<ItemStack>... lists) {
		if (lists.length == 0) {
			return tag;
		}
		NBTTagList nbttaglist = new NBTTagList();
		
		int i = 0;
		for (List<ItemStack> list : lists) {
			for (ItemStack stack : list) {
				if (!stack.isEmpty()) {
					NBTTagCompound nbttagcompound = new NBTTagCompound();
					nbttagcompound.setByte("Slot", (byte) i);
					stack.writeToNBT(nbttagcompound);
					nbttaglist.appendTag(nbttagcompound);
				}
				++i;
			}
		}
		
		tag.setTag("Items", nbttaglist);
		
		return tag;
	}
	
	@SafeVarargs
	public static void readAllItems(NBTTagCompound tag, List<ItemStack>... lists) {
		if (lists.length == 0) {
			return;
		}
		NBTTagList nbttaglist = tag.getTagList("Items", 10);
		
		int n = 0, offset = 0;
		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
			int j = nbttagcompound.getByte("Slot") & 255;
			
			while (j - offset >= lists[n].size()) {
				offset += lists[n].size();
				++n;
			}
			if (j - offset >= 0 && j - offset < lists[n].size()) {
				lists[n].set(j - offset, new ItemStack(nbttagcompound));
			}
		}
	}
	
	// BlockPos
	
	public static NBTTagCompound writeBlockPos(NBTTagCompound nbt, BlockPos pos, String name) {
		if (pos != null) {
			nbt.setIntArray(name, new int[] {pos.getX(), pos.getY(), pos.getZ()});
		}
		return nbt;
	}
	
	public static BlockPos readBlockPos(NBTTagCompound nbt, String name) {
		if (nbt.hasKey(name, 11)) {
			int[] array = nbt.getIntArray(name);
			return new BlockPos(array[0], array[1], array[2]);
		}
		return PosHelper.DEFAULT_NON;
	}
	
	// Vector3f
	
	public static NBTTagCompound writeVector3f(NBTTagCompound nbt, Vector3f vector, String name) {
		if (vector != null) {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setFloat("floatx", vector.x);
			tag.setFloat("floaty", vector.y);
			tag.setFloat("floatz", vector.z);
			nbt.setTag(name, tag);
		}
		return nbt;
	}
	
	public static Vector3f readVector3f(NBTTagCompound nbt, String name) {
		if (nbt.hasKey(name, 10)) {
			NBTTagCompound tag = nbt.getCompoundTag(name);
			return new Vector3f(tag.getFloat("floatx"), tag.getFloat("floaty"), tag.getFloat("floatz"));
		}
		return new Vector3f(0, -1, 0);
	}
	
	// long[]
	
	public static NBTTagCompound writeLongArray(NBTTagCompound nbt, long[] array, String name) {
		if (array != null) {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setInteger("length", array.length);
			for (int i = 0; i < array.length; ++i) {
				tag.setLong("long" + i, array[i]);
			}
			nbt.setTag(name, tag);
		}
		return nbt;
	}
	
	public static long[] readLongArray(NBTTagCompound nbt, String name) {
		if (nbt.hasKey(name, 10)) {
			NBTTagCompound tag = nbt.getCompoundTag(name);
			long[] array = new long[tag.getInteger("length")];
			for (int i = 0; i < array.length; ++i) {
				array[i] = tag.getLong("long" + i);
			}
			return array;
		}
		return new long[0];
	}
	
	// float[]
	
	public static NBTTagCompound writeFloatArray(NBTTagCompound nbt, float[] array, String name) {
		if (array != null) {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setInteger("length", array.length);
			for (int i = 0; i < array.length; ++i) {
				tag.setFloat("float" + i, array[i]);
			}
			nbt.setTag(name, tag);
		}
		return nbt;
	}
	
	public static float[] readFloatArray(NBTTagCompound nbt, String name) {
		if (nbt.hasKey(name, 10)) {
			NBTTagCompound tag = nbt.getCompoundTag(name);
			float[] array = new float[tag.getInteger("length")];
			for (int i = 0; i < array.length; ++i) {
				array[i] = tag.getFloat("float" + i);
			}
			return array;
		}
		return new float[0];
	}
	
	// double[]
	
	public static NBTTagCompound writeDoubleArray(NBTTagCompound nbt, double[] array, String name) {
		if (array != null) {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setInteger("length", array.length);
			for (int i = 0; i < array.length; ++i) {
				tag.setDouble("double" + i, array[i]);
			}
			nbt.setTag(name, tag);
		}
		return nbt;
	}
	
	public static double[] readDoubleArray(NBTTagCompound nbt, String name) {
		if (nbt.hasKey(name, 10)) {
			NBTTagCompound tag = nbt.getCompoundTag(name);
			double[] array = new double[tag.getInteger("length")];
			for (int i = 0; i < array.length; ++i) {
				array[i] = tag.getDouble("double" + i);
			}
			return array;
		}
		return new double[0];
	}
	
	// BlockPos[]
	
	public static NBTTagCompound writeBlockPosArray(NBTTagCompound nbt, BlockPos[] array, String name) {
		if (array != null) {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setInteger("length", array.length);
			for (int i = 0; i < array.length; ++i) {
				writeBlockPos(tag, array[i], "pos" + i);
			}
			nbt.setTag(name, tag);
		}
		return nbt;
	}
	
	public static BlockPos[] readBlockPosArray(NBTTagCompound nbt, String name) {
		if (nbt.hasKey(name, 10)) {
			NBTTagCompound tag = nbt.getCompoundTag(name);
			BlockPos[] array = new BlockPos[tag.getInteger("length")];
			for (int i = 0; i < array.length; ++i) {
				array[i] = readBlockPos(tag, "pos" + i);
			}
			return array;
		}
		return null;
	}
	
	// Vector3f[]
	
	public static NBTTagCompound writeVector3fArray(NBTTagCompound nbt, Vector3f[] array, String name) {
		if (array != null) {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setInteger("length", array.length);
			for (int i = 0; i < array.length; ++i) {
				writeVector3f(tag, array[i], "vector" + i);
			}
			nbt.setTag(name, tag);
		}
		return nbt;
	}
	
	public static Vector3f[] readVector3fArray(NBTTagCompound nbt, String name) {
		if (nbt.hasKey(name, 10)) {
			NBTTagCompound tag = nbt.getCompoundTag(name);
			Vector3f[] array = new Vector3f[tag.getInteger("length")];
			for (int i = 0; i < array.length; ++i) {
				array[i] = readVector3f(tag, "vector" + i);
			}
			return array;
		}
		return null;
	}
	
	// IntCollection
	
	public static NBTTagCompound writeIntCollection(NBTTagCompound nbt, IntCollection collection, String name) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setIntArray("ints", collection.toIntArray());
		nbt.setTag(name, tag);
		return nbt;
	}
	
	public static void readIntCollection(NBTTagCompound nbt, IntCollection collection, String name) {
		if (nbt.hasKey(name, 10)) {
			NBTTagCompound tag = nbt.getCompoundTag(name);
			collection.clear();
			collection.addAll(new IntArrayList(tag.getIntArray("ints")));
		}
	}
	
	// LongCollection
	
	public static NBTTagCompound writeLongCollection(NBTTagCompound nbt, LongCollection collection, String name) {
		NBTTagCompound tag = new NBTTagCompound();
		int i = 0;
		for (long l : collection) {
			tag.setLong("long" + i, l);
			++i;
		}
		nbt.setTag(name, tag);
		return nbt;
	}
	
	public static void readLongCollection(NBTTagCompound nbt, LongCollection collection, String name) {
		if (nbt.hasKey(name, 10)) {
			NBTTagCompound tag = nbt.getCompoundTag(name);
			collection.clear();
			int i = 0;
			while (tag.hasKey("long" + i)) {
				collection.add(tag.getLong("long" + i));
				++i;
			}
		}
	}
	
	// FloatCollection
	
	public static NBTTagCompound writeFloatCollection(NBTTagCompound nbt, FloatCollection collection, String name) {
		NBTTagCompound tag = new NBTTagCompound();
		int i = 0;
		for (float f : collection) {
			tag.setFloat("float" + i, f);
			++i;
		}
		nbt.setTag(name, tag);
		return nbt;
	}
	
	public static void readFloatCollection(NBTTagCompound nbt, FloatCollection collection, String name) {
		if (nbt.hasKey(name, 10)) {
			NBTTagCompound tag = nbt.getCompoundTag(name);
			collection.clear();
			int i = 0;
			while (tag.hasKey("float" + i)) {
				collection.add(tag.getFloat("float" + i));
				++i;
			}
		}
	}
	
	// DoubleCollection
	
	public static NBTTagCompound writeDoubleCollection(NBTTagCompound nbt, DoubleCollection collection, String name) {
		NBTTagCompound tag = new NBTTagCompound();
		int i = 0;
		for (double d : collection) {
			tag.setDouble("double" + i, d);
			++i;
		}
		nbt.setTag(name, tag);
		return nbt;
	}
	
	public static void readDoubleCollection(NBTTagCompound nbt, DoubleCollection collection, String name) {
		if (nbt.hasKey(name, 10)) {
			NBTTagCompound tag = nbt.getCompoundTag(name);
			collection.clear();
			int i = 0;
			while (tag.hasKey("double" + i)) {
				collection.add(tag.getDouble("double" + i));
				++i;
			}
		}
	}
	
	// Map<BlockPos, Integer>
	
	public static NBTTagCompound writeBlockPosToIntegerMap(NBTTagCompound nbt, Map<BlockPos, Integer> map, String name) {
		NBTTagCompound tag = new NBTTagCompound();
		int i = 0;
		for (Entry<BlockPos, Integer> entry : map.entrySet()) {
			writeBlockPos(tag, entry.getKey(), "pos" + i);
			tag.setInteger("int" + i, entry.getValue());
			++i;
		}
		nbt.setTag(name, tag);
		return nbt;
	}
	
	public static void readBlockPosToIntegerMap(NBTTagCompound nbt, Map<BlockPos, Integer> map, String name) {
		if (nbt.hasKey(name, 10)) {
			NBTTagCompound tag = nbt.getCompoundTag(name);
			map.clear();
			int i = 0;
			while (tag.hasKey("pos" + i)) {
				map.put(readBlockPos(tag, "pos" + i), tag.getInteger("int" + i));
				++i;
			}
		}
	}
}
