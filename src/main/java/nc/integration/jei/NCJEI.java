package nc.integration.jei;

import java.util.ArrayList;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import nc.container.generator.ContainerFissionController;
import nc.container.generator.ContainerFusionCore;
import nc.container.processor.ContainerAlloyFurnace;
import nc.container.processor.ContainerChemicalReactor;
import nc.container.processor.ContainerCrystallizer;
import nc.container.processor.ContainerDecayHastener;
import nc.container.processor.ContainerDissolver;
import nc.container.processor.ContainerElectrolyser;
import nc.container.processor.ContainerFuelReprocessor;
import nc.container.processor.ContainerInfuser;
import nc.container.processor.ContainerIngotFormer;
import nc.container.processor.ContainerIrradiator;
import nc.container.processor.ContainerIsotopeSeparator;
import nc.container.processor.ContainerManufactory;
import nc.container.processor.ContainerMelter;
import nc.container.processor.ContainerPressurizer;
import nc.container.processor.ContainerSaltMixer;
import nc.container.processor.ContainerSupercooler;
import nc.gui.generator.GuiFissionController;
import nc.gui.generator.GuiFusionCore;
import nc.gui.processor.GuiAlloyFurnace;
import nc.gui.processor.GuiChemicalReactor;
import nc.gui.processor.GuiCrystallizer;
import nc.gui.processor.GuiDecayHastener;
import nc.gui.processor.GuiDissolver;
import nc.gui.processor.GuiElectrolyser;
import nc.gui.processor.GuiFuelReprocessor;
import nc.gui.processor.GuiInfuser;
import nc.gui.processor.GuiIngotFormer;
import nc.gui.processor.GuiIrradiator;
import nc.gui.processor.GuiIsotopeSeparator;
import nc.gui.processor.GuiManufactory;
import nc.gui.processor.GuiMelter;
import nc.gui.processor.GuiPressurizer;
import nc.gui.processor.GuiSaltMixer;
import nc.gui.processor.GuiSupercooler;
import nc.init.NCBlocks;
import nc.init.NCItems;
import nc.integration.jei.generator.FissionCategory;
import nc.integration.jei.generator.FusionCategory;
import nc.integration.jei.processor.AlloyFurnaceCategory;
import nc.integration.jei.processor.ChemicalReactorCategory;
import nc.integration.jei.processor.CrystallizerCategory;
import nc.integration.jei.processor.DecayHastenerCategory;
import nc.integration.jei.processor.DissolverCategory;
import nc.integration.jei.processor.ElectrolyserCategory;
import nc.integration.jei.processor.FuelReprocessorCategory;
import nc.integration.jei.processor.InfuserCategory;
import nc.integration.jei.processor.IngotFormerCategory;
import nc.integration.jei.processor.IrradiatorCategory;
import nc.integration.jei.processor.IsotopeSeparatorCategory;
import nc.integration.jei.processor.ManufactoryCategory;
import nc.integration.jei.processor.MelterCategory;
import nc.integration.jei.processor.PressurizerCategory;
import nc.integration.jei.processor.SaltMixerCategory;
import nc.integration.jei.processor.SupercoolerCategory;
import nc.recipe.BaseRecipeHandler;
import nc.recipe.IRecipe;
import nc.recipe.generator.FissionRecipes;
import nc.recipe.generator.FusionRecipes;
import nc.recipe.processor.AlloyFurnaceRecipes;
import nc.recipe.processor.ChemicalReactorRecipes;
import nc.recipe.processor.CrystallizerRecipes;
import nc.recipe.processor.DecayHastenerRecipes;
import nc.recipe.processor.DissolverRecipes;
import nc.recipe.processor.ElectrolyserRecipes;
import nc.recipe.processor.FuelReprocessorRecipes;
import nc.recipe.processor.InfuserRecipes;
import nc.recipe.processor.IngotFormerRecipes;
import nc.recipe.processor.IrradiatorRecipes;
import nc.recipe.processor.IsotopeSeparatorRecipes;
import nc.recipe.processor.ManufactoryRecipes;
import nc.recipe.processor.MelterRecipes;
import nc.recipe.processor.PressurizerRecipes;
import nc.recipe.processor.SaltMixerRecipes;
import nc.recipe.processor.SupercoolerRecipes;
import nc.util.NCStackHelper;
import nc.util.NCUtil;
import nc.worldgen.OreGen;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;

@JEIPlugin
public class NCJEI implements IModPlugin, IJEIRecipeBuilder {
	
	{
		JEIMethods.registerRecipeBuilder(this);
	}
	
	public void register(IModRegistry registry) {
		IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		
		for (IJEIHandler handler : Handlers.values()) {
			registry.addRecipes(handler.getJEIRecipes());
			JEICategory category = handler.getCategory(guiHelper);
			registry.addRecipeCategories(category);
			registry.addRecipeHandlers(category);
			if (handler.getCrafterItemStack() != null) registry.addRecipeCatalyst(handler.getCrafterItemStack(), handler.getUUID());
			
			NCUtil.getLogger().info("Registering JEI recipe handler " + handler.getUUID());
		}
		IRecipeTransferRegistry recipeTransferRegistry = registry.getRecipeTransferRegistry();
		
		registry.addRecipeClickArea(GuiManufactory.class, 73, 34, 37, 18, Handlers.MANUFACTORY.getUUID());
		registry.addRecipeClickArea(GuiIsotopeSeparator.class, 59, 34, 37, 18, Handlers.ISOTOPE_SEPARATOR.getUUID());
		registry.addRecipeClickArea(GuiDecayHastener.class, 73, 34, 37, 18, Handlers.DECAY_HASTENER.getUUID());
		registry.addRecipeClickArea(GuiFuelReprocessor.class, 67, 30, 37, 38, Handlers.FUEL_REPROCESSOR.getUUID());
		registry.addRecipeClickArea(GuiAlloyFurnace.class, 83, 34, 37, 18, Handlers.ALLOY_FURNACE.getUUID());
		registry.addRecipeClickArea(GuiInfuser.class, 83, 34, 37, 18, Handlers.INFUSER.getUUID());
		registry.addRecipeClickArea(GuiMelter.class, 73, 34, 37, 18, Handlers.MELTER.getUUID());
		registry.addRecipeClickArea(GuiSupercooler.class, 73, 34, 37, 18, Handlers.SUPERCOOLER.getUUID());
		registry.addRecipeClickArea(GuiElectrolyser.class, 67, 30, 37, 38, Handlers.ELECTROLYSER.getUUID());
		registry.addRecipeClickArea(GuiIrradiator.class, 69, 34, 37, 18, Handlers.IRRADIATOR.getUUID());
		registry.addRecipeClickArea(GuiIngotFormer.class, 73, 34, 37, 18, Handlers.INGOT_FORMER.getUUID());
		registry.addRecipeClickArea(GuiPressurizer.class, 73, 34, 37, 18, Handlers.PRESSURIZER.getUUID());
		registry.addRecipeClickArea(GuiChemicalReactor.class, 69, 34, 37, 18, Handlers.CHEMICAL_REACTOR.getUUID());
		registry.addRecipeClickArea(GuiSaltMixer.class, 83, 34, 37, 18, Handlers.SALT_MIXER.getUUID());
		registry.addRecipeClickArea(GuiCrystallizer.class, 73, 34, 37, 18, Handlers.CRYSTALLIZER.getUUID());
		registry.addRecipeClickArea(GuiDissolver.class, 83, 34, 37, 18, Handlers.DISSOLVER.getUUID());
		registry.addRecipeClickArea(GuiFissionController.class, 73, 34, 37, 18, Handlers.FISSION.getUUID());
		registry.addRecipeClickArea(GuiFusionCore.class, 47, 5, 121, 97, Handlers.FUSION.getUUID());
		
		recipeTransferRegistry.addRecipeTransferHandler(ContainerManufactory.class, Handlers.MANUFACTORY.getUUID(), 0, 1, 4, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerIsotopeSeparator.class, Handlers.ISOTOPE_SEPARATOR.getUUID(), 0, 1, 5, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerDecayHastener.class, Handlers.DECAY_HASTENER.getUUID(), 0, 1, 4, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerFuelReprocessor.class, Handlers.FUEL_REPROCESSOR.getUUID(), 0, 1, 7, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerAlloyFurnace.class, Handlers.ALLOY_FURNACE.getUUID(), 0, 2, 5, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerInfuser.class, Handlers.INFUSER.getUUID(), 0, 1, 4, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerMelter.class, Handlers.MELTER.getUUID(), 0, 1, 3, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerSupercooler.class, Handlers.SUPERCOOLER.getUUID(), 0, 0, 2, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerElectrolyser.class, Handlers.ELECTROLYSER.getUUID(), 0, 0, 2, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerIrradiator.class, Handlers.IRRADIATOR.getUUID(), 0, 0, 2, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerIngotFormer.class, Handlers.INGOT_FORMER.getUUID(), 0, 0, 3, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerPressurizer.class, Handlers.PRESSURIZER.getUUID(), 0, 1, 4, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerChemicalReactor.class, Handlers.CHEMICAL_REACTOR.getUUID(), 0, 0, 2, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerSaltMixer.class, Handlers.SALT_MIXER.getUUID(), 0, 0, 2, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerCrystallizer.class, Handlers.CRYSTALLIZER.getUUID(), 0, 0, 3, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerDissolver.class, Handlers.DISSOLVER.getUUID(), 0, 1, 3, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerFissionController.class, Handlers.FISSION.getUUID(), 0, 1, 3, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerFusionCore.class, Handlers.FUSION.getUUID(), 0, 0, 0, 36);
		
		for (int i = 0; i < 8; i++) {
			if (!OreGen.showOre(i)) {
				blacklist(jeiHelpers, new ItemStack(NCBlocks.ore, 1, i));
				blacklist(jeiHelpers, new ItemStack(NCBlocks.ingot_block, 1, i));
				blacklist(jeiHelpers, new ItemStack(NCItems.ingot, 1, i));
				blacklist(jeiHelpers, new ItemStack(NCItems.dust, 1, i));
			}
		}
		
		blacklist(jeiHelpers, NCBlocks.reactor_door);
		
		blacklist(jeiHelpers, NCBlocks.nuclear_furnace_active);
		blacklist(jeiHelpers, NCBlocks.manufactory_active);
		blacklist(jeiHelpers, NCBlocks.isotope_separator_active);
		blacklist(jeiHelpers, NCBlocks.decay_hastener_active);
		blacklist(jeiHelpers, NCBlocks.fuel_reprocessor_active);
		blacklist(jeiHelpers, NCBlocks.alloy_furnace_active);
		blacklist(jeiHelpers, NCBlocks.infuser_active);
		blacklist(jeiHelpers, NCBlocks.melter_active);
		blacklist(jeiHelpers, NCBlocks.supercooler_active);
		blacklist(jeiHelpers, NCBlocks.electrolyser_active);
		blacklist(jeiHelpers, NCBlocks.irradiator_active);
		blacklist(jeiHelpers, NCBlocks.ingot_former_active);
		blacklist(jeiHelpers, NCBlocks.pressurizer_active);
		blacklist(jeiHelpers, NCBlocks.chemical_reactor_active);
		blacklist(jeiHelpers, NCBlocks.salt_mixer_active);
		blacklist(jeiHelpers, NCBlocks.crystallizer_active);
		blacklist(jeiHelpers, NCBlocks.dissolver_active);
		
		blacklist(jeiHelpers, NCBlocks.fission_controller_active);
		
		blacklist(jeiHelpers, NCBlocks.fusion_dummy_side);
		blacklist(jeiHelpers, NCBlocks.fusion_dummy_top);
		
		blacklist(jeiHelpers, NCBlocks.fusion_electromagnet_active);
		blacklist(jeiHelpers, NCBlocks.fusion_electromagnet_transparent_active);
		blacklist(jeiHelpers, NCBlocks.accelerator_electromagnet_active);
		blacklist(jeiHelpers, NCBlocks.electromagnet_supercooler_active);
		
		NCUtil.getLogger().info("JEI integration complete");
	}
	
	public Object buildRecipe(IRecipe recipe, BaseRecipeHandler<IRecipe> methods) {
		if ((Loader.isModLoaded("jei") || Loader.isModLoaded("JEI"))) {
			for (Handlers handler : NCJEI.Handlers.values()) {
				if (handler.methods.getRecipeName().equals(methods.getRecipeName())) {
					try {
						return handler.recipeClass.getConstructor(BaseRecipeHandler.class, IRecipe.class).newInstance(handler.methods, recipe);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}
	
	private void blacklist(IJeiHelpers jeiHelpers, Object ingredient) {
		jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(NCStackHelper.fixItemStack(ingredient));
	}
	
	public enum Handlers implements IJEIHandler {
		MANUFACTORY(ManufactoryRecipes.instance(), NCBlocks.manufactory_idle, "manufactory", Recipes.Manufactory.class),
		ISOTOPE_SEPARATOR(IsotopeSeparatorRecipes.instance(), NCBlocks.isotope_separator_idle, "isotope_separator", Recipes.IsotopeSeparator.class),
		DECAY_HASTENER(DecayHastenerRecipes.instance(), NCBlocks.decay_hastener_idle, "decay_hastener", Recipes.DecayHastener.class),
		FUEL_REPROCESSOR(FuelReprocessorRecipes.instance(), NCBlocks.fuel_reprocessor_idle, "fuel_reprocessor", Recipes.FuelReprocessor.class),
		ALLOY_FURNACE(AlloyFurnaceRecipes.instance(), NCBlocks.alloy_furnace_idle, "alloy_furnace", Recipes.AlloyFurnace.class),
		INFUSER(InfuserRecipes.instance(), NCBlocks.infuser_idle, "infuser", Recipes.Infuser.class),
		MELTER(MelterRecipes.instance(), NCBlocks.melter_idle, "melter", Recipes.Melter.class),
		SUPERCOOLER(SupercoolerRecipes.instance(), NCBlocks.supercooler_idle, "supercooler", Recipes.Supercooler.class),
		ELECTROLYSER(ElectrolyserRecipes.instance(), NCBlocks.electrolyser_idle, "electrolyser", Recipes.Electrolyser.class),
		IRRADIATOR(IrradiatorRecipes.instance(), NCBlocks.irradiator_idle, "irradiator", Recipes.Irradiator.class),
		INGOT_FORMER(IngotFormerRecipes.instance(), NCBlocks.ingot_former_idle, "ingot_former", Recipes.IngotFormer.class),
		PRESSURIZER(PressurizerRecipes.instance(), NCBlocks.pressurizer_idle, "pressurizer", Recipes.Pressurizer.class),
		CHEMICAL_REACTOR(ChemicalReactorRecipes.instance(), NCBlocks.chemical_reactor_idle, "chemical_reactor", Recipes.ChemicalReactor.class),
		SALT_MIXER(SaltMixerRecipes.instance(), NCBlocks.salt_mixer_idle, "salt_mixer", Recipes.SaltMixer.class),
		CRYSTALLIZER(CrystallizerRecipes.instance(), NCBlocks.crystallizer_idle, "crystallizer", Recipes.Crystallizer.class),
		DISSOLVER(DissolverRecipes.instance(), NCBlocks.dissolver_idle, "dissolver", Recipes.Dissolver.class),
		FISSION(FissionRecipes.instance(), NCBlocks.fission_controller_idle, "fission_controller", Recipes.Fission.class),
		FUSION(FusionRecipes.instance(), NCBlocks.fusion_core, "fusion_core", Recipes.Fusion.class);
		
		public BaseRecipeHandler methods;
		public String unlocalizedName;
		public String textureName;
		public Class<? extends JEIRecipe> recipeClass;
		public ItemStack crafterType;
		
		Handlers(BaseRecipeHandler methods, Object crafter, String textureName, Class<? extends JEIRecipe> recipeClass) {
			this.methods = methods;
			crafterType = NCStackHelper.fixItemStack(crafter);
			this.unlocalizedName = crafterType.getUnlocalizedName() + ".name";
			this.textureName = textureName;
			this.recipeClass = recipeClass;
		}
		
		public JEICategory getCategory(IGuiHelper guiHelper) {
			switch (this) {
			case MANUFACTORY:
				return new ManufactoryCategory(guiHelper, this);
			case ISOTOPE_SEPARATOR:
				return new IsotopeSeparatorCategory(guiHelper, this);
			case DECAY_HASTENER:
				return new DecayHastenerCategory(guiHelper, this);
			case FUEL_REPROCESSOR:
				return new FuelReprocessorCategory(guiHelper, this);
			case ALLOY_FURNACE:
				return new AlloyFurnaceCategory(guiHelper, this);
			case INFUSER:
				return new InfuserCategory(guiHelper, this);
			case MELTER:
				return new MelterCategory(guiHelper, this);
			case SUPERCOOLER:
				return new SupercoolerCategory(guiHelper, this);
			case ELECTROLYSER:
				return new ElectrolyserCategory(guiHelper, this);
			case IRRADIATOR:
				return new IrradiatorCategory(guiHelper, this);
			case INGOT_FORMER:
				return new IngotFormerCategory(guiHelper, this);
			case PRESSURIZER:
				return new PressurizerCategory(guiHelper, this);
			case CHEMICAL_REACTOR:
				return new ChemicalReactorCategory(guiHelper, this);
			case SALT_MIXER:
				return new SaltMixerCategory(guiHelper, this);
			case CRYSTALLIZER:
				return new CrystallizerCategory(guiHelper, this);
			case DISSOLVER:
				return new DissolverCategory(guiHelper, this);
			case FISSION:
				return new FissionCategory(guiHelper, this);
			case FUSION:
				return new FusionCategory(guiHelper, this);
			default:
				return null;
			}
		}
		
		public String getTextureName() {
			return textureName;
		}
		
		public String getTitle() {
			return unlocalizedName;
		}
		
		public Class<? extends JEIRecipe> getRecipeClass() {
			return recipeClass;
		}

		public BaseRecipeHandler getRecipeHandler() {
			return methods;
		}

		public ArrayList<JEIRecipe> getJEIRecipes() {
			ArrayList<JEIRecipe> recipes = new ArrayList();
			if (methods != null && methods instanceof BaseRecipeHandler) {
				BaseRecipeHandler methods = (BaseRecipeHandler) this.methods;
				for (IRecipe recipe : (ArrayList<IRecipe>) methods.getRecipes()) {
					try {
						recipes.add(recipeClass.getConstructor(BaseRecipeHandler.class, IRecipe.class).newInstance(methods, recipe));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			return recipes;
		}

		public ItemStack getCrafterItemStack() {
			return crafterType;
		}
		
		public String getUUID() {
			return methods.getRecipeName();
		}
	}
}
