package nc.capability.radiation.resistance;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import nc.radiation.RadiationArmor;
import nc.util.ItemInfo;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class RadiationResistanceProvider implements ICapabilitySerializable {
	
	private IRadiationResistance radiationResistance;
	private ItemStack stack = null;
	private TileEntity tile = null;
	
	public RadiationResistanceProvider(TileEntity tile) {
		radiationResistance = new RadiationResistance(0D);
		this.tile = tile;
	}
	
	public RadiationResistanceProvider(ItemStack stack) {
		this.stack = stack;
	}
	
	private IRadiationResistance getRadiationResistance() {
		if (radiationResistance == null) {
			ItemInfo itemInfo = new ItemInfo(stack);
			if (RadiationArmor.ARMOR_RAD_RESISTANCE_MAP.containsKey(itemInfo)) {
				radiationResistance = new RadiationResistance(RadiationArmor.ARMOR_RAD_RESISTANCE_MAP.get(itemInfo));
			}
			else radiationResistance = new RadiationResistance(0D);
		}
		return radiationResistance;
	}
	
	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		if (tile != null) tile.markDirty();
		return capability == IRadiationResistance.CAPABILITY_RADIATION_RESISTANCE;
	}
	
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		if (tile != null) tile.markDirty();
		if (capability == IRadiationResistance.CAPABILITY_RADIATION_RESISTANCE) return IRadiationResistance.CAPABILITY_RADIATION_RESISTANCE.cast(getRadiationResistance());
		return null;
	}
	
	@Override
	public NBTBase serializeNBT() {
		return IRadiationResistance.CAPABILITY_RADIATION_RESISTANCE.writeNBT(getRadiationResistance(), null);
	}
	
	@Override
	public void deserializeNBT(NBTBase nbt) {
		IRadiationResistance.CAPABILITY_RADIATION_RESISTANCE.readNBT(getRadiationResistance(), null, nbt);
	}
}
