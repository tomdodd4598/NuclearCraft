package nc.enumm;

import nc.block.fluid.BlockFluidAcid;
import nc.block.fluid.BlockFluidChocolate;
import nc.block.fluid.BlockFluidCoolant;
import nc.block.fluid.BlockFluidCorium;
import nc.block.fluid.BlockFluidCryotheum;
import nc.block.fluid.BlockFluidFission;
import nc.block.fluid.BlockFluidFlammable;
import nc.block.fluid.BlockFluidGas;
import nc.block.fluid.BlockFluidHotCoolant;
import nc.block.fluid.BlockFluidHotGas;
import nc.block.fluid.BlockFluidLiquid;
import nc.block.fluid.BlockFluidMolten;
import nc.block.fluid.BlockFluidParticle;
import nc.block.fluid.BlockFluidPlasma;
import nc.block.fluid.BlockFluidSaltSolution;
import nc.block.fluid.BlockFluidSteam;
import nc.block.fluid.BlockFluidSugar;
import nc.block.fluid.BlockSuperFluid;
import nc.block.fluid.NCBlockFluid;
import nc.fluid.FluidAcid;
import nc.fluid.FluidChocolate;
import nc.fluid.FluidCoolant;
import nc.fluid.FluidCorium;
import nc.fluid.FluidCryotheum;
import nc.fluid.FluidFission;
import nc.fluid.FluidFlammable;
import nc.fluid.FluidGas;
import nc.fluid.FluidHotCoolant;
import nc.fluid.FluidHotGas;
import nc.fluid.FluidLiquid;
import nc.fluid.FluidMolten;
import nc.fluid.FluidParticle;
import nc.fluid.FluidPlasma;
import nc.fluid.FluidSaltSolution;
import nc.fluid.FluidSteam;
import nc.fluid.FluidSugar;
import nc.fluid.SuperFluid;
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
	
	public <T extends Fluid> Class<T> getFluidClass() {
		return (Class<T>) fluidClass;
	}
	
	public <T extends NCBlockFluid> Class<T> getBlockClass() {
		return (Class<T>) blockClass;
	}
}
