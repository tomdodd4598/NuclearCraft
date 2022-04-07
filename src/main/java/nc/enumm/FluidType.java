package nc.enumm;

import nc.block.fluid.*;
import nc.fluid.*;
import net.minecraftforge.fluids.Fluid;

public enum FluidType {
	
	LIQUID(FluidLiquid.class, BlockFluidLiquid.class),
	GAS(FluidGas.class, BlockFluidGas.class),
	HOT_GAS(FluidHotGas.class, BlockFluidHotGas.class),
	STEAM(FluidSteam.class, BlockFluidSteam.class),
	MOLTEN(FluidMolten.class, BlockFluidMolten.class),
	SUPERFLUID(SuperFluid.class, BlockSuperFluid.class),
	PLASMA(FluidPlasma.class, BlockFluidPlasma.class),
	PARTICLE(FluidParticle.class, BlockFluidParticle.class),
	FLAMMABLE(FluidFlammable.class, BlockFluidFlammable.class),
	ACID(FluidAcid.class, BlockFluidAcid.class),
	SALT_SOLUTION(FluidSaltSolution.class, BlockFluidSaltSolution.class),
	FISSION(FluidFission.class, BlockFluidFission.class),
	CORIUM(FluidCorium.class, BlockFluidCorium.class),
	CHOCOLATE(FluidChocolate.class, BlockFluidChocolate.class),
	SUGAR(FluidSugar.class, BlockFluidSugar.class),
	COOLANT(FluidCoolant.class, BlockFluidCoolant.class),
	HOT_COOLANT(FluidHotCoolant.class, BlockFluidHotCoolant.class),
	CRYOTHEUM(FluidCryotheum.class, BlockFluidCryotheum.class);
	
	private final Class<? extends Fluid> fluidClass;
	private final Class<? extends NCBlockFluid> blockClass;
	
	private <T extends Fluid, V extends NCBlockFluid> FluidType(Class<T> fluidClass, Class<V> blockClass) {
		this.fluidClass = fluidClass;
		this.blockClass = blockClass;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Fluid> Class<T> getFluidClass() {
		return (Class<T>) fluidClass;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends NCBlockFluid> Class<T> getBlockClass() {
		return (Class<T>) blockClass;
	}
}
