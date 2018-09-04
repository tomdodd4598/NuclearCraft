package nc.item.energy;

import java.util.List;

import ic2.api.item.IElectricItemManager;
import ic2.api.item.ISpecialElectricItem;
import nc.item.NCItem;
import nc.tile.internal.energy.EnergyConnection;
import nc.util.InfoHelper;
import nc.util.UnitHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.Optional;

@Optional.InterfaceList(value = { @Optional.Interface(iface = "ic2.api.item.ISpecialElectricItem", modid = "ic2") })
public class ItemEnergy extends NCItem implements ISpecialElectricItem, IChargableItem {
	
	private int capacity;
	private int maxReceive;
	private int maxExtract;
	private final EnergyConnection energyConnection;
	private final int energyTier;
	
	public ItemEnergy(String name, int capacity, int energyTier, EnergyConnection connection, String... tooltip) {
		this(name, capacity, capacity, capacity, energyTier, connection, tooltip);
	}
	
	public ItemEnergy(String name, int capacity, int maxTransfer, int energyTier, EnergyConnection connection, String... tooltip) {
		this(name, capacity, maxTransfer, maxTransfer, energyTier, connection, tooltip);
	}
	
	public ItemEnergy(String name, int capacity, int maxReceive, int maxExtract, int energyTier, EnergyConnection connection, String... tooltip) {
		super(name, tooltip);
		this.capacity = capacity;
		this.maxReceive = maxReceive;
		this.maxExtract = maxExtract;
		energyConnection = connection;
		this.energyTier = energyTier;
	}
	
	@Override
	@Optional.Method(modid = "ic2")
	public IElectricItemManager getManager(ItemStack itemStack) {
		return ElectricItemManager.getElectricItemManager(this);
	}
	
	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
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
		return 1 - (double)getEnergyStored(stack) / (double)capacity;
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
		return Math.max(maxReceive, maxExtract);
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
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		if (nbt != null && nbt.hasKey("energy") && nbt.hasKey("capacity") && nbt.hasKey("maxReceive") && nbt.hasKey("maxExtract")) {
			return new ItemEnergyCapabilityProvider(stack, nbt, energyTier);
		}
		return new ItemEnergyCapabilityProvider(stack, getEnergyStored(stack), capacity, maxReceive, maxExtract, energyTier);
	}
}
