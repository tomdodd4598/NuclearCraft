package nc;


import nc.block.NCBlocks;
import nc.item.NCItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import cofh.api.modhelpers.ThermalExpansionHelper;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional.Method;

public final class TERecipes {
	
	public boolean IC2Loaded = false;
	public boolean TELoaded = false;
	
	public void hook() {
		if(Loader.isModLoaded("Thermal Expansion") || Loader.isModLoaded("ThermalExpansion")) TELoaded = true;
		if(TELoaded) addTERecipes();
	}
	
	@Method(modid = "ThermalExpansion")
	public static void addTERecipes() {
		
		//Induction Smelter
		try {ThermalExpansionHelper.addSmelterRecipe(1600, new ItemStack(NCItems.material, 1, 51), new ItemStack(NCItems.material, 2, 45), new ItemStack(NCItems.material, 3, 71));} catch(Exception e) {}
		try {ThermalExpansionHelper.addSmelterRecipe(2400, new ItemStack(NCItems.material, 1, 50), new ItemStack(NCItems.material, 2, 43), new ItemStack(NCItems.material, 3, 71));} catch(Exception e) {}
		try {ThermalExpansionHelper.addSmelterRecipe(4000, new ItemStack(Items.redstone, 1), new ItemStack(Items.dye, 1, 4), new ItemStack(NCItems.parts, 3, 4));} catch(Exception e) {}
		try {ThermalExpansionHelper.addSmelterRecipe(2000, new ItemStack(Items.redstone, 1), new ItemStack(NCItems.material, 1, 10), new ItemStack(NCItems.parts, 3, 4));} catch(Exception e) {}
		try {ThermalExpansionHelper.addSmelterRecipe(1600, new ItemStack(NCItems.parts, 1, 3), new ItemStack(Items.redstone, 4), new ItemStack(NCItems.upgrade, 1));} catch(Exception e) {}
		try {ThermalExpansionHelper.addSmelterRecipe(1200, new ItemStack(NCItems.parts, 1, 1), new ItemStack(NCItems.material, 4, 10), new ItemStack(NCItems.upgradeSpeed, 1));} catch(Exception e) {}
		try {ThermalExpansionHelper.addSmelterRecipe(1200, new ItemStack(NCItems.parts, 1, 1), new ItemStack(NCItems.parts, 4, 4), new ItemStack(NCItems.upgradeEnergy, 1));} catch(Exception e) {}
		try {ThermalExpansionHelper.addSmelterRecipe(1600, new ItemStack(NCItems.material, 1, 2), new ItemStack(NCItems.material, 1, 14), new ItemStack(NCItems.parts, 1, 0));} catch(Exception e) {}
		try {ThermalExpansionHelper.addSmelterRecipe(2000, new ItemStack(NCItems.parts, 1, 0), new ItemStack(NCItems.material, 2, 7), new ItemStack(NCItems.parts, 1, 3));} catch(Exception e) {}
		try {ThermalExpansionHelper.addSmelterRecipe(2400, new ItemStack(NCItems.parts, 1, 0), new ItemStack(NCItems.material, 2, 22), new ItemStack(NCItems.parts, 1, 3));} catch(Exception e) {}
		try {ThermalExpansionHelper.addSmelterRecipe(2000, new ItemStack(NCItems.parts, 2, 3), new ItemStack(NCItems.material, 4, 24), new ItemStack(NCItems.parts, 1, 8));} catch(Exception e) {}
		try {ThermalExpansionHelper.addSmelterRecipe(2000, new ItemStack(NCItems.parts, 2, 3), new ItemStack(NCItems.material, 4, 55), new ItemStack(NCItems.parts, 1, 8));} catch(Exception e) {}
		try {ThermalExpansionHelper.addSmelterRecipe(2400, new ItemStack(NCItems.parts, 2, 8), new ItemStack(NCItems.material, 3, 48), new ItemStack(NCItems.parts, 1, 9));} catch(Exception e) {}
		
		//Fluid Transposer
		try {ThermalExpansionHelper.addTransposerFill(1000, new ItemStack(NCItems.fuel, 1, 45), new ItemStack(NCItems.fuel, 1, 34), new FluidStack(FluidRegistry.WATER, 1000), true);} catch(Exception e) {}
		try {ThermalExpansionHelper.addTransposerFill(1000, new ItemStack(NCItems.fuel, 1, 45), new ItemStack(NCItems.fuel, 1, 75), new FluidStack(NuclearCraft.liquidHelium, 1000), true);} catch(Exception e) {}
		
		try {ThermalExpansionHelper.addTransposerFill(1000, new ItemStack(NCBlocks.emptyCoolerBlock, 1), new ItemStack(NCBlocks.waterCoolerBlock, 1), new FluidStack(FluidRegistry.WATER, 1000), true);} catch(Exception e) {}
		try {ThermalExpansionHelper.addTransposerFill(1000, new ItemStack(NCBlocks.emptyCoolerBlock, 1), new ItemStack(NCBlocks.cryotheumCoolerBlock, 1), new FluidStack(FluidRegistry.getFluid("cryotheum"), 1000), true);} catch(Exception e) {}
		try {ThermalExpansionHelper.addTransposerFill(1000, new ItemStack(NCBlocks.emptyCoolerBlock, 1), new ItemStack(NCBlocks.redstoneCoolerBlock, 1), new FluidStack(FluidRegistry.getFluid("redstone"), 1000), true);} catch(Exception e) {}
		try {ThermalExpansionHelper.addTransposerFill(1000, new ItemStack(NCBlocks.emptyCoolerBlock, 1), new ItemStack(NCBlocks.enderiumCoolerBlock, 1), new FluidStack(FluidRegistry.getFluid("ender"), 1000), true);} catch(Exception e) {}
		try {ThermalExpansionHelper.addTransposerFill(1000, new ItemStack(NCBlocks.emptyCoolerBlock, 1), new ItemStack(NCBlocks.glowstoneCoolerBlock, 1), new FluidStack(FluidRegistry.getFluid("glowstone"), 1000), true);} catch(Exception e) {}
		try {ThermalExpansionHelper.addTransposerFill(1000, new ItemStack(NCBlocks.emptyCoolerBlock, 1), new ItemStack(NCBlocks.heliumCoolerBlock, 1), new FluidStack(NuclearCraft.liquidHelium, 1000), true);} catch(Exception e) {}
		try {ThermalExpansionHelper.addTransposerFill(1000, new ItemStack(NCBlocks.emptyCoolerBlock, 1), new ItemStack(NCBlocks.coolantCoolerBlock, 1), new FluidStack(FluidRegistry.getFluid("ic2coolant"), 1000), true);} catch(Exception e) {}
		
		//Reactant Dynamo Fluid
		try {ThermalExpansionHelper.addReactantFuel("liquidHelium", 750000);} catch(Exception e) {}
		
		//Compression Dynamo Fluid
		try {ThermalExpansionHelper.addCoolant("liquidHelium", 4000000);} catch(Exception e) {}
	}
}

