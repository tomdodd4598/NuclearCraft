package nc.multiblock.gui;

import nc.multiblock.*;
import nc.multiblock.tile.IMultiblockGuiPart;
import net.minecraft.entity.player.EntityPlayer;

public abstract class GuiLogicMultiblock<MULTIBLOCK extends Multiblock & ILogicMultiblock, LOGIC extends MultiblockLogic, TILE extends IMultiblockGuiPart<MULTIBLOCK>> extends GuiMultiblock<MULTIBLOCK, TILE> {
	
	protected LOGIC logic;
	
	public GuiLogicMultiblock(EntityPlayer player, TILE tile) {
		super(player, tile);
		this.logic = (LOGIC) multiblock.getLogic();
	}
}
