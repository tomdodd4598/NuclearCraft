package nc.block.property;

import com.google.common.base.Predicate;
import nc.util.StreamHelper;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.*;

import java.util.*;

public class PropertySidedEnum<T extends Enum<T> & IStringSerializable> extends PropertyEnum<T> {
	
	public EnumFacing facing;
	
	public PropertySidedEnum(String name, Class<T> valueClass, Collection<T> allowedValues, EnumFacing facing) {
		super(name, valueClass, allowedValues);
		this.facing = facing;
	}
	
	public static <T extends Enum<T> & IStringSerializable> PropertySidedEnum<T> create(String name, Class<T> clazz, EnumFacing facing) {
		return create(name, clazz, x -> true, facing);
	}
	
	public static <T extends Enum<T> & IStringSerializable> PropertySidedEnum<T> create(String name, Class<T> clazz, Predicate<T> filter, EnumFacing facing) {
		return create(name, clazz, StreamHelper.filter(clazz.getEnumConstants(), filter), facing);
	}
	
	public static <T extends Enum<T> & IStringSerializable> PropertySidedEnum<T> create(String name, Class<T> clazz, T[] values, EnumFacing facing) {
		return create(name, clazz, Arrays.asList(values), facing);
	}
	
	public static <T extends Enum<T> & IStringSerializable> PropertySidedEnum<T> create(String name, Class<T> clazz, Collection<T> values, EnumFacing facing) {
		return new PropertySidedEnum<>(name, clazz, values, facing);
	}
}
