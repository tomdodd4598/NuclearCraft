package nc.util;

import it.unimi.dsi.fastutil.longs.LongCollection;
import nc.tile.internal.fluid.Tank;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.*;

import java.util.Collection;

public class OCHelper {
	
	public static Object[] posInfo(BlockPos pos) {
		return pos == null ? new Object[] {0, 0, 0} : new Object[] {pos.getX(), pos.getY(), pos.getZ()};
	}
	
	public static Object[] posInfoArray(Collection<BlockPos> posCollection) {
		return posCollection.stream().map(OCHelper::posInfo).toArray();
	}
	
	public static Object[] posInfo(long posLong) {
		return posInfo(BlockPos.fromLong(posLong));
	}
	
	public static Object[] posInfoArray(LongCollection posLongCollection) {
		return posLongCollection.stream().map(OCHelper::posInfo).toArray();
	}
	
	public static Object[] vec3dInfo(Vec3d vec) {
		return vec == null ? vec3dInfo(Vec3d.ZERO) : new Object[] {vec.x, vec.y, vec.z};
	}
	
	public static Object[] vec3dInfoArray(Collection<Vec3d> vecCollection) {
		return vecCollection.stream().map(OCHelper::vec3dInfo).toArray();
	}
	
	public static Object[] stackInfo(ItemStack stack) {
		return stack == null || stack.isEmpty() ? new Object[] {0, "null"} : new Object[] {stack.getCount(), StackHelper.stackName(stack)};
	}
	
	public static Object[] stackInfoArray(Collection<ItemStack> stackCollection) {
		return stackCollection.stream().map(OCHelper::stackInfo).toArray();
	}
	
	public static Object[] tankInfo(Tank tank) {
		return tank == null || tank.isEmpty() ? new Object[] {0, "null"} : new Object[] {tank.getFluidAmount(), tank.getFluidName()};
	}
	
	public static Object[] tankInfoArray(Collection<Tank> tankCollection) {
		return tankCollection.stream().map(OCHelper::tankInfo).toArray();
	}
}
