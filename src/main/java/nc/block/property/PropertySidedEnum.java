package nc.block.property;

import java.util.Collection;

import com.google.common.base.*;
import com.google.common.collect.*;

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.*;

public class PropertySidedEnum<T extends Enum<T> & IStringSerializable> extends PropertyEnum<T> {
	
	public EnumFacing facing;
	
	public PropertySidedEnum(String name, Class<T> valueClass, Collection<T> allowedValues, EnumFacing facing) {
		super(name, valueClass, allowedValues);
		this.facing = facing;
	}
	
	public static <T extends Enum<T> & IStringSerializable> PropertySidedEnum<T> create(String name, Class<T> clazz, EnumFacing facing) {
		return create(name, clazz, Predicates.alwaysTrue(), facing);
	}
	
	public static <T extends Enum<T> & IStringSerializable> PropertySidedEnum<T> create(String name, Class<T> clazz, Predicate<T> filter, EnumFacing facing) {
		return create(name, clazz, Collections2.filter(Lists.newArrayList(clazz.getEnumConstants()), filter), facing);
	}
	
	public static <T extends Enum<T> & IStringSerializable> PropertySidedEnum<T> create(String name, Class<T> clazz, T[] values, EnumFacing facing) {
		return create(name, clazz, Lists.newArrayList(values), facing);
	}
	
	public static <T extends Enum<T> & IStringSerializable> PropertySidedEnum<T> create(String name, Class<T> clazz, Collection<T> values, EnumFacing facing) {
		return new PropertySidedEnum<>(name, clazz, values, facing);
	}
}
