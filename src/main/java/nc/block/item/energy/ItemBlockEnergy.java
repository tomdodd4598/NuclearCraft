package nc.block.item.energy;

import java.util.List;

import javax.annotation.Nullable;

import ic2.api.item.*;
import nc.block.item.NCItemBlock;
import nc.item.energy.*;
import nc.tile.internal.energy.EnergyConnection;
import nc.util.*;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.Optional;

@Optional.InterfaceList(value = {@Optional.Interface(iface = "ic2.api.item.ISpecialElectricItem", modid = "ic2")})
public class ItemBlockEnergy extends NCItemBlock implements ISpecialElectricItem, IChargableItem {
	
	private final long capacity;
	private final int maxTransfer;
	private final EnergyConnection energyConnection;
	private final int energyTier;
	
	public ItemBlockEnergy(Block block, long capacity, int energyTier, EnergyConnection connection, String... tooltip) {
		this(block, capacity, NCMath.toInt(capacity), energyTier, connection, tooltip);
	}
	
	public ItemBlockEnergy(Block block, long capacity, int maxTransfer, int energyTier, EnergyConnection connection, String... tooltip) {
		super(block, tooltip);
		this.capacity = capacity;
		this.maxTransfer = maxTransfer;
		energyConnection = connection;
		this.energyTier = energyTier;
	}
	
	@Override
	@Optional.Method(modid = "ic2")
	public IElectricItemManager getManager(ItemStack itemStack) {
		return ElectricItemManager.getElectricItemManager(this);
	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
		InfoHelper.infoLine(tooltip, TextFormatting.LIGHT_PURPLE, Lang.localise("info.nuclearcraft.item_energy.energy_stored") + " " + UnitHelper.prefix(getEnergyStored(stack), getMaxEnergyStored(stack), 5, "RF"));
		InfoHelper.infoLine(tooltip, TextFormatting.WHITE, Lang.localise("info.nuclearcraft.item_energy.power_tier") + " " + getEnergyTier(stack));
		super.addInformation(stack, world, tooltip, flag);
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		NBTTagCompound nbt = IChargableItem.getEnergyStorageNBT(stack);
		return nbt != null && nbt.getLong("energy") > 0;
	}
	
	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		return 1D - MathHelper.clamp((double) getEnergyStored(stack) / capacity, 0D, 1D);
	}
	
	@Override
	public long getMaxEnergyStored(ItemStack stack) {
		return capacity;
	}
	
	@Override
	public int getMaxTransfer(ItemStack stack) {
		return maxTransfer;
	}
	
	@Override
	public boolean canReceive(ItemStack stack) {
		return energyConnection.canReceive();
	}
	
	@Override
	public boolean canExtract(ItemStack stack) {
		return energyConnection.canExtract();
	}
	
	@Override
	public EnergyConnection getEnergyConnection(ItemStack stack) {
		return energyConnection;
	}
	
	@Override
	public int getEnergyTier(ItemStack stack) {
		return energyTier;
	}
	
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
		return new ItemEnergyCapabilityProvider(stack, capacity, maxTransfer, getEnergyStored(stack), energyConnection, energyTier);
	}
}
