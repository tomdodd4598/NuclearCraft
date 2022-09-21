package nc.gui.processor;

import nc.container.processor.*;
import nc.tile.processor.TileBasicProcessor.ChemicalReactor;
import net.minecraft.entity.player.EntityPlayer;

public class GuiChemicalReactor extends GuiBasicUpgradeProcessor<ChemicalReactor> {
	
	public GuiChemicalReactor(EntityPlayer player, ChemicalReactor tile) {
		this(player, tile, new ContainerChemicalReactor(player, tile));
	}
	
	protected GuiChemicalReactor(EntityPlayer player, ChemicalReactor tile, ContainerBasicUpgradeProcessor<ChemicalReactor> container) {
		super("chemical_reactor", player, tile, container);
		xSize = 176;
		ySize = 166;
	}
	
	public static class SideConfig extends GuiChemicalReactor {
		
		public SideConfig(EntityPlayer player, ChemicalReactor tile) {
			super(player, tile, new ContainerMachineConfig<>(player, tile));
		}
	}
}
