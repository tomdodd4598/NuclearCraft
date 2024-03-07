package nc.ncpf.configuration.overhaul.fissionmsr;
import java.util.ArrayList;
import nc.ncpf.config2.Config;
import nc.ncpf.config2.ConfigList;
import nc.ncpf.config2.ConfigNumberList;
import nc.ncpf.texture.TextureProvider;
public class BlockRecipe{
    public static BlockRecipe heater(String inputName, String inputDisplayName, TextureProvider inputTexture, String outputName, String outputDisplayName, TextureProvider outputTexture, int inputRate, int outputRate, int cooling){
        BlockRecipe recipe = new BlockRecipe(inputName, outputName);
        recipe.inputDisplayName = inputDisplayName;
        recipe.inputTexture = inputTexture;
        recipe.outputDisplayName = outputDisplayName;
        recipe.outputTexture = outputTexture;
        recipe.heaterCooling = cooling;
        recipe.inputRate = inputRate;
        recipe.outputRate = outputRate;
        return recipe;
    }
    public static BlockRecipe irradiatorRecipe(String inputName, String inputDisplayName, TextureProvider inputTexture, String outputName, String outputDisplayName, TextureProvider outputTexture, float efficiency, float heat){
        BlockRecipe recipe = new BlockRecipe(inputName, outputName);
        recipe.inputDisplayName = inputDisplayName;
        recipe.inputTexture = inputTexture;
        recipe.outputDisplayName = outputDisplayName;
        recipe.outputTexture = outputTexture;
        recipe.irradiatorEfficiency = efficiency;
        recipe.irradiatorHeat = heat;
        return recipe;
    }
    public static BlockRecipe fuel(String inputName, String inputDisplayName, TextureProvider inputTexture, String outputName, String outputDisplayName, TextureProvider outputTexture, float efficiency, int heat, int time, int criticality, boolean selfPriming){
        BlockRecipe recipe = new BlockRecipe(inputName, outputName);
        recipe.inputDisplayName = inputDisplayName;
        recipe.inputTexture = inputTexture;
        recipe.outputDisplayName = outputDisplayName;
        recipe.outputTexture = outputTexture;
        recipe.fuelVesselEfficiency = efficiency;
        recipe.fuelVesselHeat = heat;
        recipe.fuelVesselTime = time;
        recipe.fuelVesselCriticality = criticality;
        recipe.fuelVesselSelfPriming = selfPriming;
        recipe.inputRate = recipe.outputRate = 1;
        return recipe;
    }
    public String inputName;
    public String inputDisplayName;
    public ArrayList<String> inputLegacyNames = new ArrayList<>();
    public TextureProvider inputTexture;
    public int inputRate;
    public String outputName;
    public String outputDisplayName;
    public TextureProvider outputTexture;
    public int outputRate;
    public float fuelVesselEfficiency;
    public int fuelVesselHeat;
    public int fuelVesselTime;
    public int fuelVesselCriticality;
    public boolean fuelVesselSelfPriming;
    public float irradiatorEfficiency;
    public float irradiatorHeat;
    public float reflectorEfficiency;
    public float reflectorReflectivity;
    public int moderatorFlux;
    public float moderatorEfficiency;
    public boolean moderatorActive;
    public int shieldHeat;
    public float shieldEfficiency;
    public int heaterCooling;
    public BlockRecipe(String inputName, String outputName){
        this.inputName = inputName;
        this.outputName = outputName;
    }
    public Config save(Block block){
        Config config = Config.newConfig();
        Config inputCfg = Config.newConfig();
        inputCfg.set("name", inputName);
        if(inputDisplayName!=null)inputCfg.set("displayName", inputDisplayName);
        if(!inputLegacyNames.isEmpty()){
            ConfigList lst = new ConfigList();
            for(String s : inputLegacyNames)lst.add(s);
            inputCfg.set("legacyNames", lst);
        }
        if(inputTexture!=null){
            ConfigNumberList tex = new ConfigNumberList();
            tex.add(inputTexture.getSize());
            for(int x = 0; x<inputTexture.getSize(); x++){
                for(int y = 0; y<inputTexture.getSize(); y++){
                    tex.add(inputTexture.getColor(x, y));
                }
            }
            inputCfg.set("texture", tex);
        }
        if(inputRate!=0)inputCfg.set("rate", inputRate);
        config.set("input", inputCfg);
        Config outputCfg = Config.newConfig();
        outputCfg.set("name", outputName);
        if(outputDisplayName!=null)outputCfg.set("displayName", outputDisplayName);
        if(outputTexture!=null){
            ConfigNumberList tex = new ConfigNumberList();
            tex.add(outputTexture.getSize());
            for(int x = 0; x<outputTexture.getSize(); x++){
                for(int y = 0; y<outputTexture.getSize(); y++){
                    tex.add(outputTexture.getColor(x, y));
                }
            }
            outputCfg.set("texture", tex);
        }
        if(outputRate!=0)outputCfg.set("rate", outputRate);
        config.set("output", outputCfg);
        if(block.fuelVessel){
            Config fuelVesselCfg = Config.newConfig();
            fuelVesselCfg.set("efficiency", fuelVesselEfficiency);
            fuelVesselCfg.set("heat", fuelVesselHeat);
            fuelVesselCfg.set("time", fuelVesselTime);
            fuelVesselCfg.set("criticality", fuelVesselCriticality);
            if(fuelVesselSelfPriming)fuelVesselCfg.set("selfPriming", true);
            config.set("fuelVessel", fuelVesselCfg);
        }
        if(block.irradiator){
            Config irradiatorCfg = Config.newConfig();
            irradiatorCfg.set("efficiency", irradiatorEfficiency);
            irradiatorCfg.set("heat", irradiatorHeat);
            config.set("irradiator", irradiatorCfg);
        }
        if(block.reflector){
            Config reflectorCfg = Config.newConfig();
            reflectorCfg.set("efficiency", reflectorEfficiency);
            reflectorCfg.set("reflectivity", reflectorReflectivity);
            config.set("reflector", reflectorCfg);
        }
        if(block.moderator){
            Config moderatorCfg = Config.newConfig();
            moderatorCfg.set("flux", moderatorFlux);
            moderatorCfg.set("efficiency", moderatorEfficiency);
            if(moderatorActive)moderatorCfg.set("active", true);
            config.set("moderator", moderatorCfg);
        }
        if(block.shield){
            Config shieldCfg = Config.newConfig();
            shieldCfg.set("heat", shieldHeat);
            shieldCfg.set("efficiency", shieldEfficiency);
            config.set("shield", shieldCfg);
        }
        if(block.heater){
            Config heaterCfg = Config.newConfig();
            heaterCfg.set("cooling", heaterCooling);
            config.set("heater", heaterCfg);
        }
        return config;
    }
}