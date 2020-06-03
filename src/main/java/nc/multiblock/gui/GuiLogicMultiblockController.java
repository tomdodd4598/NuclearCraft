package nc.multiblock.gui;

import nc.multiblock.*;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.BlockPos;

public abstract class GuiLogicMultiblockController<MULTIBLOCK extends Multiblock & ILogicMultiblock, LOGIC extends MultiblockLogic> extends GuiMultiblockController<MULTIBLOCK> {
	
	protected LOGIC logic;
	
	public GuiLogicMultiblockController(MULTIBLOCK multiblock, BlockPos controllerPos, Container container) {
		super(multiblock, controllerPos, container);
		this.logic = (LOGIC) multiblock.getLogic();
	}
}
