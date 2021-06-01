package nc.ncpf;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Supplier;
import nc.ncpf.config2.Config;
import nc.ncpf.configuration.Configuration;
import nc.ncpf.configuration.overhaul.OverhaulConfiguration;
import nc.ncpf.configuration.overhaul.fissionmsr.FissionMSRConfiguration;
import nc.ncpf.configuration.overhaul.fissionsfr.FissionSFRConfiguration;
import nc.ncpf.configuration.overhaul.turbine.TurbineConfiguration;
import nc.ncpf.texture.TextureProvider;
public class NCPFBuilder{
    public static final byte NCPF_VERSION = 10;
    public Configuration configuration;
    private HashMap<nc.ncpf.configuration.overhaul.fissionsfr.Block, Supplier<nc.ncpf.configuration.overhaul.fissionsfr.PlacementRule[]>> sfrBlocksToHaveRules = new HashMap<>();
    private HashMap<nc.ncpf.configuration.overhaul.fissionmsr.Block, Supplier<nc.ncpf.configuration.overhaul.fissionmsr.PlacementRule[]>> msrBlocksToHaveRules = new HashMap<>();
    private HashMap<nc.ncpf.configuration.overhaul.turbine.Block, Supplier<nc.ncpf.configuration.overhaul.turbine.PlacementRule[]>> turbineBlocksToHaveRules = new HashMap<>();
    public NCPFBuilder(String name, String version){
        configuration = new Configuration(name, version);
        configuration.overhaul = new OverhaulConfiguration();
        configuration.overhaul.fissionSFR = new FissionSFRConfiguration();
        configuration.overhaul.fissionMSR = new FissionMSRConfiguration();
        configuration.overhaul.turbine = new TurbineConfiguration();
    }
    public void write(OutputStream stream){
        //<editor-fold defaultstate="collapsed" desc="Header">
        Config header = Config.newConfig();
        header.set("version", NCPF_VERSION);
        header.set("count", 0);
        header.save(stream);
//</editor-fold>
        buildRules();
        configuration.save(null, Config.newConfig()).save(stream);
    }
    public void buildRules(){
        sfrBlocksToHaveRules.keySet().forEach((block) -> {
            block.rules.addAll(Arrays.asList(sfrBlocksToHaveRules.get(block).get()));
        });
        sfrBlocksToHaveRules.clear();
        msrBlocksToHaveRules.keySet().forEach((block) -> {
            block.rules.addAll(Arrays.asList(msrBlocksToHaveRules.get(block).get()));
        });
        msrBlocksToHaveRules.clear();
        turbineBlocksToHaveRules.keySet().forEach((block) -> {
            block.rules.addAll(Arrays.asList(turbineBlocksToHaveRules.get(block).get()));
        });
        turbineBlocksToHaveRules.clear();
    }
    public nc.ncpf.configuration.overhaul.fissionsfr.Block addSFRBlock(nc.ncpf.configuration.overhaul.fissionsfr.Block block){
        configuration.overhaul.fissionSFR.allBlocks.add(block);
        return block;
    }
    public nc.ncpf.configuration.overhaul.fissionsfr.Block addSFRController(String name, String displayName, TextureProvider texture){
        return addSFRBlock(nc.ncpf.configuration.overhaul.fissionsfr.Block.controller(name, displayName, texture));
    }
    public nc.ncpf.configuration.overhaul.fissionsfr.Block addSFRCasing(String name, String displayName, TextureProvider texture, boolean edge){
        return addSFRBlock(nc.ncpf.configuration.overhaul.fissionsfr.Block.casing(name, displayName, texture, edge));
    }
    public nc.ncpf.configuration.overhaul.fissionsfr.Block addSFRVent(String name, String displayName, TextureProvider texture, String outputDisplayName, TextureProvider outputTexture){
        return addSFRBlock(nc.ncpf.configuration.overhaul.fissionsfr.Block.vent(name, displayName, texture, outputDisplayName, outputTexture));
    }
    public nc.ncpf.configuration.overhaul.fissionsfr.Block addSFRSource(String name, String displayName, TextureProvider texture, float efficiency){
        return addSFRBlock(nc.ncpf.configuration.overhaul.fissionsfr.Block.source(name, displayName, texture, efficiency));
    }
    public nc.ncpf.configuration.overhaul.fissionsfr.Block addSFRHeatsink(String name, String displayName, TextureProvider texture, int cooling, Supplier<nc.ncpf.configuration.overhaul.fissionsfr.PlacementRule[]> ruleSupplier){
        nc.ncpf.configuration.overhaul.fissionsfr.Block heatsink;
        sfrBlocksToHaveRules.put(heatsink = addSFRBlock(nc.ncpf.configuration.overhaul.fissionsfr.Block.heatsink(name, displayName, cooling, texture)), ruleSupplier);
        return heatsink;
    }
    public nc.ncpf.configuration.overhaul.fissionsfr.Block addSFRCell(String name, String displayName, TextureProvider texture, String portName, String portDisplayName, TextureProvider portTexture, String portOutputDisplayName, TextureProvider portOutputTexture){
        nc.ncpf.configuration.overhaul.fissionsfr.Block cell = addSFRBlock(nc.ncpf.configuration.overhaul.fissionsfr.Block.cell(name, displayName, texture));
        cell.port = nc.ncpf.configuration.overhaul.fissionsfr.Block.port(cell, portName, portDisplayName, portTexture, portOutputDisplayName, portOutputTexture);
        return cell;
    }
    public nc.ncpf.configuration.overhaul.fissionsfr.Block addSFRIrradiator(String name, String displayName, TextureProvider texture, String portName, String portDisplayName, TextureProvider portTexture, String portOutputDisplayName, TextureProvider portOutputTexture){
        nc.ncpf.configuration.overhaul.fissionsfr.Block irradiator = addSFRBlock(nc.ncpf.configuration.overhaul.fissionsfr.Block.irradiator(name, displayName, texture));
        irradiator.port = nc.ncpf.configuration.overhaul.fissionsfr.Block.port(irradiator, portName, portDisplayName, portTexture, portOutputDisplayName, portOutputTexture);
        return irradiator;
    }
    public nc.ncpf.configuration.overhaul.fissionsfr.Block addSFRConductor(String name, String displayName, TextureProvider texture){
        return addSFRBlock(nc.ncpf.configuration.overhaul.fissionsfr.Block.conductor(name, displayName, texture));
    }
    public nc.ncpf.configuration.overhaul.fissionsfr.Block addSFRModerator(String name, String displayName, TextureProvider texture, int flux, float efficiency){
        return addSFRBlock(nc.ncpf.configuration.overhaul.fissionsfr.Block.moderator(name, displayName, texture, flux, efficiency));
    }
    public nc.ncpf.configuration.overhaul.fissionsfr.Block addSFRReflector(String name, String displayName, TextureProvider texture, float efficiency, float reflectivity){
        return addSFRBlock(nc.ncpf.configuration.overhaul.fissionsfr.Block.reflector(name, displayName, texture, efficiency, reflectivity));
    }
    public nc.ncpf.configuration.overhaul.fissionsfr.Block addSFRNeutronShield(String name, String displayName, TextureProvider texture, TextureProvider closedTexture, int heatPerFlux, float efficiency){
        return addSFRBlock(nc.ncpf.configuration.overhaul.fissionsfr.Block.shield(name, displayName, texture, closedTexture, heatPerFlux, efficiency));
    }
    public void addSFRFuel(String inputName, String inputDisplayName, TextureProvider inputTexture, String outputName, String outputDisplayName, TextureProvider outputTexture, float efficiency, int heat, int time, int criticality, boolean selfPriming){
        for(nc.ncpf.configuration.overhaul.fissionsfr.Block b : configuration.overhaul.fissionSFR.allBlocks){
            if(b.fuelCell){
                nc.ncpf.configuration.overhaul.fissionsfr.BlockRecipe recipe = nc.ncpf.configuration.overhaul.fissionsfr.BlockRecipe.fuel(inputName, inputDisplayName, inputTexture, outputName, outputDisplayName, outputTexture, efficiency, heat, time, criticality, selfPriming);
                b.recipes.add(recipe);
                b.allRecipes.add(recipe);
            }
        }
    }
    public void addSFRIrradiatorRecipe(String inputName, String inputDisplayName, TextureProvider inputTexture, String outputName, String outputDisplayName, TextureProvider outputTexture, float efficiency, float heat){
        for(nc.ncpf.configuration.overhaul.fissionsfr.Block b : configuration.overhaul.fissionSFR.allBlocks){
            if(b.irradiator){
                nc.ncpf.configuration.overhaul.fissionsfr.BlockRecipe recipe = nc.ncpf.configuration.overhaul.fissionsfr.BlockRecipe.irradiatorRecipe(inputName, inputDisplayName, inputTexture, outputName, outputDisplayName, outputTexture, efficiency, heat);
                b.recipes.add(recipe);
                b.allRecipes.add(recipe);
            }
        }
    }
    public nc.ncpf.configuration.overhaul.fissionsfr.CoolantRecipe addSFRCoolantRecipe(String inputName, String inputDisplayName, TextureProvider inputTexture, String outputName, String outputDisplayName, TextureProvider outputTexture, int heat, float outputRatio){
        nc.ncpf.configuration.overhaul.fissionsfr.CoolantRecipe recipe = nc.ncpf.configuration.overhaul.fissionsfr.CoolantRecipe.coolantRecipe(inputName, inputDisplayName, inputTexture, outputName, outputDisplayName, outputTexture, heat, outputRatio);
        configuration.overhaul.fissionSFR.coolantRecipes.add(recipe);
        configuration.overhaul.fissionSFR.allCoolantRecipes.add(recipe);
        return recipe;
    }
    public nc.ncpf.configuration.overhaul.fissionmsr.Block addMSRBlock(nc.ncpf.configuration.overhaul.fissionmsr.Block block){
        configuration.overhaul.fissionMSR.allBlocks.add(block);
        return block;
    }
    public nc.ncpf.configuration.overhaul.fissionmsr.Block addMSRController(String name, String displayName, TextureProvider texture){
        return addMSRBlock(nc.ncpf.configuration.overhaul.fissionmsr.Block.controller(name, displayName, texture));
    }
    public nc.ncpf.configuration.overhaul.fissionmsr.Block addMSRCasing(String name, String displayName, TextureProvider texture, boolean edge){
        return addMSRBlock(nc.ncpf.configuration.overhaul.fissionmsr.Block.casing(name, displayName, texture, edge));
    }
    public nc.ncpf.configuration.overhaul.fissionmsr.Block addMSRSource(String name, String displayName, TextureProvider texture, float efficiency){
        return addMSRBlock(nc.ncpf.configuration.overhaul.fissionmsr.Block.source(name, displayName, texture, efficiency));
    }
    public nc.ncpf.configuration.overhaul.fissionmsr.Block addMSRHeater(String name, String displayName, TextureProvider texture, int cooling, Supplier<nc.ncpf.configuration.overhaul.fissionmsr.PlacementRule[]> ruleSupplier, String recipeInputName, String recipeInputDisplayName, TextureProvider recipeInputTexture, String recipeOutputName, String recipeOutputDisplayName, TextureProvider recipeOutputTexture){
        nc.ncpf.configuration.overhaul.fissionmsr.Block heater;
        msrBlocksToHaveRules.put(heater = addMSRBlock(nc.ncpf.configuration.overhaul.fissionmsr.Block.heater(name, displayName, texture)), ruleSupplier);
        nc.ncpf.configuration.overhaul.fissionmsr.BlockRecipe recipe = nc.ncpf.configuration.overhaul.fissionmsr.BlockRecipe.heater(recipeInputName, recipeInputDisplayName, recipeInputTexture, recipeOutputName, recipeOutputDisplayName, recipeOutputTexture, 1, 1, cooling);
        heater.recipes.add(recipe);
        heater.allRecipes.add(recipe);
        return heater;
    }
    public nc.ncpf.configuration.overhaul.fissionmsr.Block addMSRVessel(String name, String displayName, TextureProvider texture, String portName, String portDisplayName, TextureProvider portTexture, String portOutputDisplayName, TextureProvider portOutputTexture){
        nc.ncpf.configuration.overhaul.fissionmsr.Block vessel = addMSRBlock(nc.ncpf.configuration.overhaul.fissionmsr.Block.vessel(name, displayName, texture));
        vessel.port = nc.ncpf.configuration.overhaul.fissionmsr.Block.port(vessel, portName, portDisplayName, portTexture, portOutputDisplayName, portOutputTexture);
        return vessel;
    }
    public nc.ncpf.configuration.overhaul.fissionmsr.Block addMSRIrradiator(String name, String displayName, TextureProvider texture, String portName, String portDisplayName, TextureProvider portTexture, String portOutputDisplayName, TextureProvider portOutputTexture){
        nc.ncpf.configuration.overhaul.fissionmsr.Block irradiator = addMSRBlock(nc.ncpf.configuration.overhaul.fissionmsr.Block.irradiator(name, displayName, texture));
        irradiator.port = nc.ncpf.configuration.overhaul.fissionmsr.Block.port(irradiator, portName, portDisplayName, portTexture, portOutputDisplayName, portOutputTexture);
        return irradiator;
    }
    public nc.ncpf.configuration.overhaul.fissionmsr.Block addMSRConductor(String name, String displayName, TextureProvider texture){
        return addMSRBlock(nc.ncpf.configuration.overhaul.fissionmsr.Block.conductor(name, displayName, texture));
    }
    public nc.ncpf.configuration.overhaul.fissionmsr.Block addMSRModerator(String name, String displayName, TextureProvider texture, int flux, float efficiency){
        return addMSRBlock(nc.ncpf.configuration.overhaul.fissionmsr.Block.moderator(name, displayName, texture, flux, efficiency));
    }
    public nc.ncpf.configuration.overhaul.fissionmsr.Block addMSRReflector(String name, String displayName, TextureProvider texture, float efficiency, float reflectivity){
        return addMSRBlock(nc.ncpf.configuration.overhaul.fissionmsr.Block.reflector(name, displayName, texture, efficiency, reflectivity));
    }
    public nc.ncpf.configuration.overhaul.fissionmsr.Block addMSRNeutronShield(String name, String displayName, TextureProvider texture, TextureProvider closedTexture, int heatPerFlux, float efficiency){
        return addMSRBlock(nc.ncpf.configuration.overhaul.fissionmsr.Block.shield(name, displayName, texture, closedTexture, heatPerFlux, efficiency));
    }
    public void addMSRFuel(String inputName, String inputDisplayName, TextureProvider inputTexture, String outputName, String outputDisplayName, TextureProvider outputTexture, float efficiency, int heat, int time, int criticality, boolean selfPriming){
        for(nc.ncpf.configuration.overhaul.fissionmsr.Block b : configuration.overhaul.fissionMSR.allBlocks){
            if(b.fuelVessel){
                nc.ncpf.configuration.overhaul.fissionmsr.BlockRecipe recipe = nc.ncpf.configuration.overhaul.fissionmsr.BlockRecipe.fuel(inputName, inputDisplayName, inputTexture, outputName, outputDisplayName, outputTexture, efficiency, heat, time, criticality, selfPriming);
                b.recipes.add(recipe);
                b.allRecipes.add(recipe);
            }
        }
    }
    public void addMSRIrradiatorRecipe(String inputName, String inputDisplayName, TextureProvider inputTexture, String outputName, String outputDisplayName, TextureProvider outputTexture, float efficiency, float heat){
        for(nc.ncpf.configuration.overhaul.fissionmsr.Block b : configuration.overhaul.fissionMSR.allBlocks){
            if(b.irradiator){
                nc.ncpf.configuration.overhaul.fissionmsr.BlockRecipe recipe = nc.ncpf.configuration.overhaul.fissionmsr.BlockRecipe.irradiatorRecipe(inputName, inputDisplayName, inputTexture, outputName, outputDisplayName, outputTexture, efficiency, heat);
                b.recipes.add(recipe);
                b.allRecipes.add(recipe);
            }
        }
    }
    public void addFissionCasing(String name, String displayName, TextureProvider texture, boolean edge){
        addSFRCasing(name, displayName, texture, edge);
        addMSRCasing(name, displayName, texture, edge);
    }
    public void addFissionSource(String name, String displayName, TextureProvider texture, float efficiency){
        addSFRSource(name, displayName, texture, efficiency);
        addMSRSource(name, displayName, texture, efficiency);
    }
    public void addFissionIrradiator(String name, String displayName, TextureProvider texture, String portName, String portDisplayName, TextureProvider portTexture, String portOutputDisplayName, TextureProvider portOutputTexture){
        addSFRIrradiator(name, displayName, texture, portName, portDisplayName, portTexture, portOutputDisplayName, portOutputTexture);
        addMSRIrradiator(name, displayName, texture, portName, portDisplayName, portTexture, portOutputDisplayName, portOutputTexture);
    }
    public void addFissionConductor(String name, String displayName, TextureProvider texture){
        addSFRConductor(name, displayName, texture);
        addMSRConductor(name, displayName, texture);
    }
    public void addFissionModerator(String name, String displayName, TextureProvider texture, int flux, float efficiency){
        addSFRModerator(name, displayName, texture, flux, efficiency);
        addMSRModerator(name, displayName, texture, flux, efficiency);
    }
    public void addFissionReflector(String name, String displayName, TextureProvider texture, float efficiency, float reflectivity){
        addSFRReflector(name, displayName, texture, efficiency, reflectivity);
        addMSRReflector(name, displayName, texture, efficiency, reflectivity);
    }
    public void addFissionNeutronShield(String name, String displayName, TextureProvider texture, TextureProvider closedTexture, int heatPerFlux, float efficiency){
        addSFRNeutronShield(name, displayName, texture, closedTexture, heatPerFlux, efficiency);
        addMSRNeutronShield(name, displayName, texture, closedTexture, heatPerFlux, efficiency);
    }
    public void addFissionIrradiatorRecipe(String inputName, String inputDisplayName, TextureProvider inputTexture, String outputName, String outputDisplayName, TextureProvider outputTexture, float efficiency, float heat){
        addSFRIrradiatorRecipe(inputName, inputDisplayName, inputTexture, outputName, outputDisplayName, outputTexture, efficiency, heat);
        addMSRIrradiatorRecipe(inputName, inputDisplayName, inputTexture, outputName, outputDisplayName, outputTexture, efficiency, heat);
    }
    public nc.ncpf.configuration.overhaul.turbine.Block addTurbineBlock(nc.ncpf.configuration.overhaul.turbine.Block block){
        configuration.overhaul.turbine.allBlocks.add(block);
        return block;
    }
    public nc.ncpf.configuration.overhaul.turbine.Block addTurbineController(String name, String displayName, TextureProvider texture){
        return addTurbineBlock(nc.ncpf.configuration.overhaul.turbine.Block.controller(name, displayName, texture));
    }
    public nc.ncpf.configuration.overhaul.turbine.Block addTurbineCasing(String name, String displayName, TextureProvider texture, boolean edge){
        return addTurbineBlock(nc.ncpf.configuration.overhaul.turbine.Block.casing(name, displayName, texture, edge));
    }
    public nc.ncpf.configuration.overhaul.turbine.Block addTurbineInlet(String name, String displayName, TextureProvider texture){
        return addTurbineBlock(nc.ncpf.configuration.overhaul.turbine.Block.inlet(name, displayName, texture));
    }
    public nc.ncpf.configuration.overhaul.turbine.Block addTurbineOutlet(String name, String displayName, TextureProvider texture){
        return addTurbineBlock(nc.ncpf.configuration.overhaul.turbine.Block.outlet(name, displayName, texture));
    }
    public nc.ncpf.configuration.overhaul.turbine.Block addTurbineCoil(String name, String displayName, TextureProvider texture, float efficiency, Supplier<nc.ncpf.configuration.overhaul.turbine.PlacementRule[]> ruleSupplier){
        nc.ncpf.configuration.overhaul.turbine.Block coil = addTurbineBlock(nc.ncpf.configuration.overhaul.turbine.Block.coil(name, displayName, texture, efficiency));
        turbineBlocksToHaveRules.put(coil, ruleSupplier);
        return coil;
    }
    public nc.ncpf.configuration.overhaul.turbine.Block addTurbineBearing(String name, String displayName, TextureProvider texture){
        return addTurbineBlock(nc.ncpf.configuration.overhaul.turbine.Block.bearing(name, displayName, texture));
    }
    public nc.ncpf.configuration.overhaul.turbine.Block addTurbineConnector(String name, String displayName, TextureProvider texture){
        return addTurbineBlock(nc.ncpf.configuration.overhaul.turbine.Block.connector(name, displayName, texture));
    }
    public nc.ncpf.configuration.overhaul.turbine.Block addTurbineBlade(String name, String displayName, TextureProvider texture, float efficiency, float expansion){
        return addTurbineBlock(nc.ncpf.configuration.overhaul.turbine.Block.blade(name, displayName, texture, efficiency, expansion));
    }
    public nc.ncpf.configuration.overhaul.turbine.Block addTurbineStator(String name, String displayName, TextureProvider texture, float expansion){
        return addTurbineBlock(nc.ncpf.configuration.overhaul.turbine.Block.stator(name, displayName, texture, expansion));
    }
    public nc.ncpf.configuration.overhaul.turbine.Block addTurbineShaft(String name, String displayName, TextureProvider texture){
        return addTurbineBlock(nc.ncpf.configuration.overhaul.turbine.Block.shaft(name, displayName, texture));
    }
}