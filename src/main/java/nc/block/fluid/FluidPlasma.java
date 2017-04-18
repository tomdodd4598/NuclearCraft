package nc.block.fluid;

import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.Fluid;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class FluidPlasma extends Fluid {
	public FluidPlasma(String name) {
		super(name);
		setLuminosity(15);
		setDensity(3000);
		setViscosity(450);
		setTemperature(1000000000);
		setGaseous(true).setUnlocalizedName(name);
		setRarity(net.minecraft.item.EnumRarity.rare);
	}

	@SideOnly(Side.CLIENT)
    public IIcon getIcon() {
    	return getStillIcon();
    }

	@SideOnly(Side.CLIENT)
    public IIcon getStillIcon() {
        return BlockPlasma.stillIcon;
    }

	@SideOnly(Side.CLIENT)
    public IIcon getFlowingIcon() {
        return BlockPlasma.flowingIcon;
    }
}
