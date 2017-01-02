package nc.block.fluid;

import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.Fluid;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class FluidHelium extends Fluid {
	public FluidHelium(String name) {
		super(name);
		setLuminosity(0);
		setDensity(125);
		setViscosity(1);
		setTemperature(4);
		setUnlocalizedName(name);
		setRarity(net.minecraft.item.EnumRarity.rare);
	}

	@SideOnly(Side.CLIENT)
    public IIcon getIcon() {
    	return getStillIcon();
    }

	@SideOnly(Side.CLIENT)
    public IIcon getStillIcon() {
        return BlockHelium.stillIcon;
    }

	@SideOnly(Side.CLIENT)
    public IIcon getFlowingIcon() {
        return BlockHelium.flowingIcon;
    }
}
