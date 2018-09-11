package nc.multiblock.gui;

import nc.multiblock.turbine.Turbine;
import net.minecraft.inventory.Container;

public class GuiHighTurbineController extends GuiTurbineController {
	
	public GuiHighTurbineController(Turbine multiblock, Container container) {
		super(multiblock, "high", container);
	}
}
