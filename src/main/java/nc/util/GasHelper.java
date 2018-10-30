package nc.util;

import java.util.HashMap;
import java.util.Map;

import mekanism.api.gas.Gas;
import mekanism.api.gas.GasRegistry;
import mekanism.api.gas.GasStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class GasHelper {
	
	// Fluid -> Gas
	public static final Map<String, String> GAS_MAP = new HashMap<String, String>();
	
	public static void init() {
		for (Gas gas : GasRegistry.getRegisteredGasses()) {
			if (gas.getFluid() == null) continue;
			GAS_MAP.put(gas.getFluid().getName(), gas.getName());
		}
	}
	
	public static FluidStack getFluidFromGas(GasStack gasStack) {
		if (gasStack == null || gasStack.getGas().getFluid() == null) return null;
		Fluid gasFluid = gasStack.getGas().getFluid();
		if (gasFluid == null) return null;
		Fluid fluid = FluidRegistry.getFluid(gasFluid.getName());
		if (fluid == null) return null;
		return new FluidStack(fluid, gasStack.amount);
	}
	
	public static GasStack getGasFromFluid(FluidStack fluidStack) {
		if (fluidStack == null) return null;
		String fluidName = fluidStack.getFluid().getName();
		if (!StringHelper.beginsWith("liquid", fluidName)) fluidName = "liquid" + fluidName;
		String gasName = GAS_MAP.get(fluidName);
		if (gasName == null) return null;
		Gas gas = GasRegistry.getGas(gasName);
		if (gas == null) return null;
		return new GasStack(gas, fluidStack.amount);
	}
	
	public static boolean isGasCapability(Capability capability) {
		try {
			capability.getDefaultInstance();
		} catch (Exception e) {
			return false;
		}
		String name = capability.getDefaultInstance().getClass().getName();
		return name.equals("mekanism.common.capabilities.DefaultTubeConnection") || name.equals("mekanism.common.capabilities.DefaultGasHandler");
	}
}
