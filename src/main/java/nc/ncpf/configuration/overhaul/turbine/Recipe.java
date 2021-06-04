package nc.ncpf.configuration.overhaul.turbine;
import java.util.ArrayList;
import nc.ncpf.config2.Config;
import nc.ncpf.config2.ConfigList;
import nc.ncpf.config2.ConfigNumberList;
import nc.ncpf.texture.TextureProvider;
public class Recipe{
    public static Recipe recipe(String inputName, String inputDisplayName, TextureProvider inputTexture, String outputName, String outputDisplayName, TextureProvider outputTexture, double power, double coefficient){
        Recipe recipe = new Recipe(inputName, outputName, power, coefficient);
        recipe.inputDisplayName = inputDisplayName;
        recipe.inputTexture = inputTexture;
        recipe.outputDisplayName = outputDisplayName;
        recipe.outputTexture = outputTexture;
        return recipe;
    }
    public String inputName;
    public String inputDisplayName;
    public ArrayList<String> inputLegacyNames = new ArrayList<>();
    public TextureProvider inputTexture;
    public String outputName;
    public String outputDisplayName;
    public TextureProvider outputTexture;
    public double power;
    public double coefficient;
    public Recipe(String inputName, String outputName, double power, double coefficient){
        this.inputName = inputName;
        this.outputName = outputName;
        this.power = power;
        this.coefficient = coefficient;
    }
    public Config save(){
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
        config.set("output", outputCfg);
        config.set("power", power);
        config.set("coefficient", coefficient);
        return config;
    }
}