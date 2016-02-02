package nc.block.fluid;

import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.Fluid;

public class FluidHelium extends Fluid {
	public FluidHelium() {
		super("liquidHelium");
	}

    public IIcon getIcon() {
    	return getStillIcon();
    }

    public IIcon getStillIcon() {
        return BlockHelium.stillIcon;
    }

    public IIcon getFlowingIcon() {
        return BlockHelium.flowingIcon;
    }
}
