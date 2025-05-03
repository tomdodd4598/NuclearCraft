package nc.recipe;

import nc.util.*;
import net.minecraft.nbt.NBTTagCompound;

public class RecipeUnitInfo {
	
	public static final RecipeUnitInfo DEFAULT = new RecipeUnitInfo("R/t", 0, 1D);
	
	public final String unit;
	public final int startingPrefix;
	public final double rateMultiplier;
	
	public RecipeUnitInfo(String unit, int startingPrefix, double rateMultiplier) {
		this.unit = unit;
		this.startingPrefix = startingPrefix;
		this.rateMultiplier = rateMultiplier;
	}
	
	public RecipeUnitInfo withRateMultiplier(double rateMultiplier) {
		return new RecipeUnitInfo(unit, startingPrefix, rateMultiplier);
	}
	
	public void writeToNBT(NBTTagCompound nbt, String name) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("unit", unit);
		tag.setInteger("startingPrefix", startingPrefix);
		tag.setDouble("rateMultiplier", rateMultiplier);
		nbt.setTag(name, tag);
	}
	
	public static RecipeUnitInfo readFromNBT(NBTTagCompound nbt, String name) {
		if (nbt.hasKey(name, 10)) {
			NBTTagCompound tag = nbt.getCompoundTag(name);
			return new RecipeUnitInfo(tag.getString("unit"), tag.getInteger("startingPrefix"), tag.getDouble("rateMultiplier"));
		}
		return DEFAULT;
	}
	
	public String getString(Double processTime, int maxLength) {
		double rate = processTime == null ? 0D : rateMultiplier / processTime;
		if (unit.equals("B") || unit.equals("B/t")) {
			return UnitHelper.prefix(rate, maxLength, unit, startingPrefix);
		}
		else {
			return NCMath.sigFigs(rate, maxLength) + " " + unit;
		}
	}
}
