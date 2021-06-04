package nc.ncpf.configuration.overhaul.fissionsfr;
import java.util.ArrayList;
import nc.ncpf.config2.Config;
import nc.ncpf.config2.ConfigList;
import nc.ncpf.config2.ConfigNumberList;
import nc.ncpf.texture.TextureProvider;
public class BlockRecipe{
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
        recipe.fuelCellEfficiency = efficiency;
        recipe.fuelCellHeat = heat;
        recipe.fuelCellTime = time;
        recipe.fuelCellCriticality = criticality;
        recipe.fuelCellSelfPriming = selfPriming;
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
    public float fuelCellEfficiency;
    public int fuelCellHeat;
    public int fuelCellTime;
    public int fuelCellCriticality;
    public boolean fuelCellSelfPriming;
    public float irradiatorEfficiency;
    public float irradiatorHeat;
    public float reflectorEfficiency;
    public float reflectorReflectivity;
    public int moderatorFlux;
    public float moderatorEfficiency;
    public boolean moderatorActive;
    public int shieldHeat;
    public float shieldEfficiency;
    public int heatsinkCooling;
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
        if(block.fuelCell){
            Config fuelCellCfg = Config.newConfig();
            fuelCellCfg.set("efficiency", fuelCellEfficiency);
            fuelCellCfg.set("heat", fuelCellHeat);
            fuelCellCfg.set("time", fuelCellTime);
            fuelCellCfg.set("criticality", fuelCellCriticality);
            if(fuelCellSelfPriming)fuelCellCfg.set("selfPriming", true);
            config.set("fuelCell", fuelCellCfg);
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
        if(block.heatsink){
            Config heatsinkCfg = Config.newConfig();
            heatsinkCfg.set("cooling", heatsinkCooling);
            config.set("heatsink", heatsinkCfg);
        }
        return config;
    }
}