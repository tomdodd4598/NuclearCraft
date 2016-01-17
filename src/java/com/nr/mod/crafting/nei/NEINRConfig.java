package com.nr.mod.crafting.nei;

import net.minecraft.item.ItemStack;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.recipe.DefaultOverlayHandler;

import com.nr.mod.NuclearRelativistics;
import com.nr.mod.blocks.NRBlocks;
import com.nr.mod.gui.GuiCollector;
import com.nr.mod.gui.GuiCooler;
import com.nr.mod.gui.GuiCrusher;
import com.nr.mod.gui.GuiElectricCrusher;
import com.nr.mod.gui.GuiElectricFurnace;
import com.nr.mod.gui.GuiElectrolyser;
import com.nr.mod.gui.GuiFactory;
import com.nr.mod.gui.GuiFissionReactorGraphite;
import com.nr.mod.gui.GuiFurnace;
import com.nr.mod.gui.GuiHastener;
import com.nr.mod.gui.GuiIoniser;
import com.nr.mod.gui.GuiIrradiator;
import com.nr.mod.gui.GuiNuclearFurnace;
import com.nr.mod.gui.GuiNuclearWorkspace;
import com.nr.mod.gui.GuiOxidiser;
import com.nr.mod.gui.GuiSeparator;
import com.nr.mod.items.NRItems;

public class NEINRConfig implements IConfigureNEI {

  public void loadConfig() {		
	   	 API.registerRecipeHandler(new CrusherRecipeHandler());
	     API.registerUsageHandler(new CrusherRecipeHandler());
	     API.registerRecipeHandler(new CrusherFuelRecipeHandler());
	     API.registerUsageHandler(new CrusherFuelRecipeHandler());
	     API.registerRecipeHandler(new MetalFurnaceRecipeHandler());
	     API.registerUsageHandler(new MetalFurnaceRecipeHandler());
	     API.registerRecipeHandler(new MetalFurnaceFuelRecipeHandler());
	     API.registerUsageHandler(new MetalFurnaceFuelRecipeHandler());
	     API.registerRecipeHandler(new NuclearFurnaceRecipeHandler());
	     API.registerUsageHandler(new NuclearFurnaceRecipeHandler());
	     API.registerRecipeHandler(new NuclearFurnaceFuelRecipeHandler());
	     API.registerUsageHandler(new NuclearFurnaceFuelRecipeHandler());
	     API.registerRecipeHandler(new ElectricCrusherRecipeHandler());
	     API.registerUsageHandler(new ElectricCrusherRecipeHandler());
	     API.registerRecipeHandler(new ElectricFurnaceRecipeHandler());
	     API.registerUsageHandler(new ElectricFurnaceRecipeHandler());
	     API.registerRecipeHandler(new HastenerRecipeHandler());
	     API.registerUsageHandler(new HastenerRecipeHandler());
	     API.registerRecipeHandler(new SeparatorRecipeHandler());
	     API.registerUsageHandler(new SeparatorRecipeHandler());
	     API.registerRecipeHandler(new NuclearWorkspaceRecipeHandler());
	     API.registerUsageHandler(new NuclearWorkspaceRecipeHandler());
	     //API.registerRecipeHandler(new NuclearWorkspaceShapelessRecipeHandler());
	     //API.registerUsageHandler(new NuclearWorkspaceShapelessRecipeHandler());
	     API.registerRecipeHandler(new FissionRecipeHandler());
	     API.registerUsageHandler(new FissionRecipeHandler());
	     API.registerRecipeHandler(new CollectorRecipeHandler());
	     API.registerUsageHandler(new CollectorRecipeHandler());
	     API.registerRecipeHandler(new ElectrolyserRecipeHandler());
	     API.registerUsageHandler(new ElectrolyserRecipeHandler());
	     API.registerRecipeHandler(new OxidiserRecipeHandler());
	     API.registerUsageHandler(new OxidiserRecipeHandler());
	     API.registerRecipeHandler(new IoniserRecipeHandler());
	     API.registerUsageHandler(new IoniserRecipeHandler());
	     API.registerRecipeHandler(new FactoryRecipeHandler());
	     API.registerUsageHandler(new FactoryRecipeHandler());
	     API.registerRecipeHandler(new IrradiatorRecipeHandler());
	     API.registerUsageHandler(new IrradiatorRecipeHandler());
	     API.registerRecipeHandler(new CoolerRecipeHandler());
	     API.registerUsageHandler(new CoolerRecipeHandler());
	     
	     API.registerRecipeHandler(new InfoUsageHandler());
	     API.registerUsageHandler(new InfoUsageHandler());
	     
	     API.registerGuiOverlayHandler(GuiNuclearWorkspace.class, new DefaultOverlayHandler(), "nwcrafting");
	     
	     API.registerGuiOverlay(GuiCrusher.class, "crushing");
	     API.registerGuiOverlay(GuiCrusher.class, "crushfuel");
	     API.registerGuiOverlay(GuiFurnace.class, "metalsmelting");
	     API.registerGuiOverlay(GuiFurnace.class, "metalfuel");
	     API.registerGuiOverlay(GuiNuclearFurnace.class, "nuclearsmelting");
	     API.registerGuiOverlay(GuiNuclearFurnace.class, "nuclearfuel");
	     
	     API.registerGuiOverlay(GuiElectricCrusher.class, "ecrushing");
	     API.registerGuiOverlay(GuiElectricFurnace.class, "esmelting");
	     API.registerGuiOverlay(GuiHastener.class, "hastening");
	     API.registerGuiOverlay(GuiSeparator.class, "separating");
	     API.registerGuiOverlay(GuiNuclearWorkspace.class, "nwcrafting");
	     API.registerGuiOverlay(GuiFissionReactorGraphite.class, "fission");
	     API.registerGuiOverlay(GuiCollector.class, "collecting");
	     API.registerGuiOverlay(GuiElectrolyser.class, "electrolysing");
	     API.registerGuiOverlay(GuiOxidiser.class, "oxidising");
	     API.registerGuiOverlay(GuiIoniser.class, "ionising");
	     API.registerGuiOverlay(GuiIrradiator.class, "irradiating");
	     API.registerGuiOverlay(GuiCooler.class, "cooling");
	     API.registerGuiOverlay(GuiFactory.class, "manufactoring");
	     
	     API.hideItem(new ItemStack(NRBlocks.electricCrusherActive));
	     API.hideItem(new ItemStack(NRBlocks.electricFurnaceActive));
	     API.hideItem(new ItemStack(NRBlocks.crusherActive));
	     API.hideItem(new ItemStack(NRBlocks.fissionReactorGraphiteActive));
	     API.hideItem(new ItemStack(NRBlocks.furnaceActive));
	     API.hideItem(new ItemStack(NRBlocks.hastenerActive));
	     API.hideItem(new ItemStack(NRBlocks.nuclearFurnaceActive));
	     API.hideItem(new ItemStack(NRBlocks.reactionGeneratorActive));
	     API.hideItem(new ItemStack(NRBlocks.separatorActive));
	     API.hideItem(new ItemStack(NRBlocks.collectorActive));
	     API.hideItem(new ItemStack(NRBlocks.electrolyserActive));
	     API.hideItem(new ItemStack(NRBlocks.oxidiserActive));
	     API.hideItem(new ItemStack(NRBlocks.ioniserActive));
	     API.hideItem(new ItemStack(NRBlocks.irradiatorActive));
	     API.hideItem(new ItemStack(NRBlocks.coolerActive));
	     API.hideItem(new ItemStack(NRBlocks.factoryActive));
	     API.hideItem(new ItemStack(NRItems.tabItem));
	     API.hideItem(new ItemStack(NRBlocks.fusionReactorBlock));
	     API.hideItem(new ItemStack(NRBlocks.nukeE));
	     API.hideItem(new ItemStack(NRItems.nuclearGrenadeThrown));
  }

  public String getName() {
    return "NuclearCraft NEI Plugin";
  }

  public String getVersion() {
    return NuclearRelativistics.version;
  }
}