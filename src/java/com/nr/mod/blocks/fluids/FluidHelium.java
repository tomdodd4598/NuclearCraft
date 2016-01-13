package com.nr.mod.blocks.fluids;

import net.minecraftforge.fluids.Fluid;

public class FluidHelium extends Fluid {

	public FluidHelium() {
		super("liquidhelium");
		this.density = 125;
		this.temperature = 4;
		this.viscosity = 0;
	}
}
