package nc.ncpf.configuration.overhaul.fissionsfr;
import java.util.ArrayList;
import nc.ncpf.config2.Config;
import nc.ncpf.config2.ConfigList;
import nc.ncpf.configuration.Configuration;
public class FissionSFRConfiguration{
    public ArrayList<Block> allBlocks = new ArrayList<>();//because I feel like being complicated, this is filled with parent duplicates for addons with recipes
    public ArrayList<CoolantRecipe> allCoolantRecipes = new ArrayList<>();
    /**
     * @deprecated You should probably be using allBlocks
     */
    @Deprecated
    public ArrayList<Block> blocks = new ArrayList<>();
    /**
     * @deprecated You should probably be using allCoolantRecipes
     */
    @Deprecated
    public ArrayList<CoolantRecipe> coolantRecipes = new ArrayList<>();
    public int minSize;
    public int maxSize;
    public int neutronReach;
    public int coolingEfficiencyLeniency;
    public float sparsityPenaltyMult;
    public float sparsityPenaltyThreshold;
    public Config save(Configuration parent){
        Config config = Config.newConfig();
        if(parent==null){
            config.set("minSize", minSize);
            config.set("maxSize", maxSize);
            config.set("neutronReach", neutronReach);
            config.set("coolingEfficiencyLeniency", coolingEfficiencyLeniency);
            config.set("sparsityPenaltyMult", sparsityPenaltyMult);
            config.set("sparsityPenaltyThreshold", sparsityPenaltyThreshold);
        }
        ConfigList blocks = new ConfigList();
        for(Block block : this.blocks){
            if(block.parent!=null)continue;
            blocks.add(block.save(parent, this));
        }
        config.set("blocks", blocks);
        ConfigList coolantRecipes = new ConfigList();
        for(CoolantRecipe recipe : this.coolantRecipes){
            coolantRecipes.add(recipe.save());
        }
        config.set("coolantRecipes", coolantRecipes);
        return config;
    }
}