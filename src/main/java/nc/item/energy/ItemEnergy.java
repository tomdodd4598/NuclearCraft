package nc.item.energy;

import java.util.List;

import javax.annotation.Nullable;

import ic2.api.item.IElectricItemManager;
import ic2.api.item.ISpecialElectricItem;
import nc.item.NCItem;
import nc.tile.internal.energy.EnergyConnection;
import nc.util.InfoHelper;
import nc.util.UnitHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.Optional;

@Optional.InterfaceList(value = { @Optional.Interface(iface = "ic2.api.item.ISpecialElectricItem", modid = "ic2") })
public class ItemEnergy extends NCItem implements ISpecialElectricItem, IChargableItem {
	
	private int capacity;
	private int maxTransfer;
	private final EnergyConnection energyConnection;
	private final int energyTier;
	
	public ItemEnergy(int capacity, int energyTier, EnergyConnection connection, String... tooltip) {
		this(capacity, capacity, energyTier, connection, tooltip);
	}
	
	public ItemEnergy(int capacity, int maxTransfer, int energyTier, EnergyConnection connection, String... tooltip) {
		super(tooltip);
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
		InfoHelper.infoLine(tooltip, TextFormatting.LIGHT_PURPLE, "Energy Stored: " + UnitHelper.prefix(getEnergyStored(stack), getMaxEnergyStored(stack), 5, "RF"));
		InfoHelper.infoLine(tooltip, TextFormatting.WHITE, "EU Power Tier: " + getEnergyTier(stack));
		super.addInformation(stack, world, tooltip, flag);
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		if (stack.getTagCompound() == null || !stack.getTagCompound().hasKey("energy")) return false;
		return stack.getTagCompound().getInteger("energy") > 0;
	}
	
	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		return 1D - MathHelper.clamp((double)getEnergyStored(stack)/capacity, 0D, 1D);
	}
	
	@Override
	public int getEnergyStored(ItemStack stack) {
		if (stack.getTagCompound() == null || !stack.getTagCompound().hasKey("energy")) return 0;
		return stack.getTagCompound().getInteger("energy");
	}
	
	@Override
	public void setEnergyStored(ItemStack stack, int amount) {
		if (stack.getTagCompound() != null && stack.getTagCompound().hasKey("energy")) stack.getTagCompound().setInteger("energy", amount);
	}

	@Override
	public int getMaxEnergyStored(ItemStack stack) {
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
		if (nbt != null && nbt.hasKey("energy") && nbt.hasKey("capacity") && nbt.hasKey("maxTransfer")) {
			return new ItemEnergyCapabilityProvider(stack, nbt, energyTier);
		}
		return new ItemEnergyCapabilityProvider(stack, getEnergyStored(stack), capacity, maxTransfer, energyTier);
	}
}
