package nc.integration.hwyla;

import java.util.List;

import javax.annotation.Nonnull;

import mcp.mobius.waila.api.*;
import mcp.mobius.waila.api.impl.ModuleRegistrar;
import nc.config.NCConfig;
import nc.tile.ITile;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class NCHWLYA {
	
	public static void init() {
		ModuleRegistrar.instance().registerBodyProvider(new TileDataProvider(), ITile.class);
	}
	
	public static class TileDataProvider implements IWailaDataProvider {
		
		@Override
		public @Nonnull List<String> getWailaBody(ItemStack stack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
			if (NCConfig.hwyla_enabled) {
				TileEntity tile = accessor.getTileEntity();
				if (tile instanceof ITile) {
					((ITile) tile).addToHWYLATooltip(tooltip);
				}
			}
			return tooltip;
		}
	}
}
