package nc.multiblock.gui;

import nc.multiblock.turbine.Turbine;
import net.minecraft.inventory.Container;

public class GuiLowTurbineController extends GuiTurbineController {
	
	public GuiLowTurbineController(Turbine multiblock, Container container) {
		super(multiblock, "low", container);
	}
}
