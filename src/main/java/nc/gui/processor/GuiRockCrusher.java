package nc.gui.processor;

import nc.container.processor.*;
import nc.tile.processor.TileBasicProcessor.RockCrusher;
import net.minecraft.entity.player.EntityPlayer;

public class GuiRockCrusher extends GuiBasicUpgradeProcessor<RockCrusher> {
	
	public GuiRockCrusher(EntityPlayer player, RockCrusher tile) {
		this(player, tile, new ContainerRockCrusher(player, tile));
	}
	
	protected GuiRockCrusher(EntityPlayer player, RockCrusher tile, ContainerBasicUpgradeProcessor<RockCrusher> container) {
		super("rock_crusher", player, tile, container);
		xSize = 176;
		ySize = 166;
	}
	
	public static class SideConfig extends GuiRockCrusher {
		
		public SideConfig(EntityPlayer player, RockCrusher tile) {
			super(player, tile, new ContainerMachineConfig<>(player, tile));
		}
	}
}
