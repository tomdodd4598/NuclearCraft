package nc.block.fluid;

import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.Fluid;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class FluidDenseSteam extends Fluid {
	public FluidDenseSteam(String name) {
		super(name);
		setLuminosity(0);
		setDensity(-10);
		setViscosity(300);
		setTemperature(5000);
		setGaseous(true);
		setUnlocalizedName(name);
		setRarity(net.minecraft.item.EnumRarity.rare);
	}

	@SideOnly(Side.CLIENT)
    public IIcon getIcon() {
    	return getStillIcon();
    }

	@SideOnly(Side.CLIENT)
    public IIcon getStillIcon() {
        return BlockSteam.stillIcon;
    }

	@SideOnly(Side.CLIENT)
    public IIcon getFlowingIcon() {
        return BlockSteam.flowingIcon;
    }
}
