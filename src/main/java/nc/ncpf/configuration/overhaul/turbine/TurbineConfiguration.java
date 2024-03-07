package nc.ncpf.configuration.overhaul.turbine;
import java.util.ArrayList;
import nc.ncpf.config2.Config;
import nc.ncpf.config2.ConfigList;
import nc.ncpf.configuration.Configuration;
public class TurbineConfiguration{
    public ArrayList<Block> allBlocks = new ArrayList<>();
    public ArrayList<Recipe> allRecipes = new ArrayList<>();
    /**
     * @deprecated You should probably be using allBlocks
     */
    @Deprecated
    public ArrayList<Block> blocks = new ArrayList<>();
    /**
     * @deprecated You should probably be using allRecipes
     */
    @Deprecated
    public ArrayList<Recipe> recipes = new ArrayList<>();
    public int minWidth;
    public int minLength;
    public int maxSize;
    public int fluidPerBlade;
    public float throughputFactor;
    public float powerBonus;
    public float throughputEfficiencyLeniencyMult;
    public float throughputEfficiencyLeniencyThreshold;
    public Config save(Configuration parent){
        Config config = Config.newConfig();
        if(parent==null){
            config.set("minWidth", minWidth);
            config.set("minLength", minLength);
            config.set("maxSize", maxSize);
            config.set("fluidPerBlade", fluidPerBlade);
            config.set("throughputEfficiencyLeniencyMult", throughputEfficiencyLeniencyMult);
            config.set("throughputEfficiencyLeniencyThreshold", throughputEfficiencyLeniencyThreshold);
            config.set("throughputFactor", throughputFactor);
            config.set("powerBonus", powerBonus);
        }
        ConfigList blocks = new ConfigList();
        for(Block block : this.blocks){
            blocks.add(block.save(parent, this));
        }
        config.set("blocks", blocks);
        ConfigList recipes = new ConfigList();
        for(Recipe recipe : this.recipes){
            recipes.add(recipe.save());
        }
        config.set("recipes", recipes);
        return config;
    }
}