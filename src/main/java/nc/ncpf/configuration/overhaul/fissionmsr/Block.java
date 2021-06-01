package nc.ncpf.configuration.overhaul.fissionmsr;
import java.util.ArrayList;
import nc.ncpf.config2.Config;
import nc.ncpf.config2.ConfigList;
import nc.ncpf.config2.ConfigNumberList;
import nc.ncpf.configuration.Configuration;
import nc.ncpf.texture.TextureProvider;
public class Block extends RuleContainer{
    public static Block controller(String name, String displayName, TextureProvider texture){
        Block block = new Block(name);
        block.displayName = displayName;
        block.legacyNames.add(displayName);
        block.texture = texture;
        block.controller = true;
        block.casing = true;
        return block;
    }
    public static Block casing(String name, String displayName, TextureProvider texture, boolean edge){
        Block block = new Block(name);
        block.displayName = displayName;
        block.legacyNames.add(displayName);
        block.texture = texture;
        block.casing = true;
        block.casingEdge = edge;
        return block;
    }
    public static Block port(Block parent, String name, String displayName, TextureProvider texture, String outputDisplayName, TextureProvider outputTexture){
        Block block = new Block(name);
        block.parent = parent;
        block.displayName = displayName;
        block.legacyNames.add(displayName);
        block.texture = texture;
        block.portOutputDisplayName = outputDisplayName;
        block.portOutputTexture = outputTexture;
        return block;
    }
    public static Block source(String name, String displayName, TextureProvider texture, float efficiency){
        Block block = new Block(name);
        block.displayName = displayName;
        block.legacyNames.add(displayName.replace(" Neutron Source", ""));
        block.texture = texture;
        block.casing = true;
        block.source = true;
        block.sourceEfficiency = efficiency;
        return block;
    }
    public static Block heater(String name, String displayName, TextureProvider texture, PlacementRule... rules){
        Block block = new Block(name);
        block.displayName = displayName;
        block.legacyNames.add(displayName);
        block.heater = true;
        for(PlacementRule r : rules){
            block.rules.add(r);
        }
        block.texture = texture;
        block.functional = true;
        block.cluster = true;
        block.moderator = true;
        block.moderatorHasBaseStats = true;
        return block;
    }
    public static Block vessel(String name, String displayName, TextureProvider texture){
        Block block = new Block(name);
        block.displayName = displayName;
        block.legacyNames.add(displayName);
        block.fuelVessel = true;
        block.cluster = true;
        block.createCluster = true;
        block.blocksLOS = true;
        block.functional = true;
        block.texture = texture;
        return block;
    }
    public static Block irradiator(String name, String displayName, TextureProvider texture){
        Block block = new Block(name);
        block.displayName = displayName;
        block.legacyNames.add(displayName);
        block.cluster = true;
        block.createCluster = true;
        block.irradiator = true;
        block.functional = true;
        block.blocksLOS = true;
        block.texture = texture;
        return block;
    }
    public static Block conductor(String name, String displayName, TextureProvider texture){
        Block block = new Block(name);
        block.displayName = displayName;
        block.legacyNames.add(displayName);
        block.cluster = true;//because conductors connect clusters together
        block.texture = texture;
        return block;
    }
    public static Block moderator(String name, String displayName, TextureProvider texture, int flux, float efficiency){
        Block block = new Block(name);
        block.displayName = displayName;
        block.legacyNames.add(displayName);
        block.moderator = true;
        block.moderatorHasBaseStats = true;
        block.moderatorActive = true;
        block.moderatorFlux = flux;
        block.moderatorEfficiency = efficiency;
        block.texture = texture;
        block.functional = true;
        return block;
    }
    public static Block reflector(String name, String displayName, TextureProvider texture, float efficiency, float reflectivity){
        Block block = new Block(name);
        block.displayName = displayName;
        block.legacyNames.add(displayName);
        block.reflector = true;
        block.reflectorHasBaseStats = true;
        block.reflectorEfficiency = efficiency;
        block.reflectorReflectivity = reflectivity;
        block.functional = true;
        block.blocksLOS = true;
        block.texture = texture;
        return block;
    }
    public static Block shield(String name, String displayName, TextureProvider texture, TextureProvider closedTexture, int heatPerFlux, float efficiency){
        Block block = new Block(name);
        block.displayName = displayName;
        block.legacyNames.add(displayName);
        block.shield = true;
        block.moderator = true;
        block.functional = true;
        block.cluster = true;
        block.createCluster = true;
        block.shieldHasBaseStats = true;
        block.shieldHeat = heatPerFlux;
        block.shieldEfficiency = efficiency;
        block.moderatorHasBaseStats = true;
        block.moderatorEfficiency = efficiency;
        block.texture = texture;
        block.shieldClosedTexture = closedTexture;
        return block;
    }
    public String name;
    public String displayName;
    public ArrayList<String> legacyNames = new ArrayList<>();
    public boolean cluster = false;
    public boolean createCluster = false;
    public boolean conductor = false;
    public boolean functional = false;
    public boolean blocksLOS = false;
    public boolean casing = false;
    public boolean casingEdge = false;
    public boolean controller = false;
    public boolean fuelVessel = false;
    public boolean fuelVesselHasBaseStats;
    public float fuelVesselEfficiency;
    public int fuelVesselHeat;
    public int fuelVesselCriticality;
    public boolean fuelVesselSelfPriming;
    public boolean irradiator = false;
    public boolean irradiatorHasBaseStats;
    public float irradiatorEfficiency;
    public float irradiatorHeat;
    public boolean reflector = false;
    public boolean reflectorHasBaseStats;
    public float reflectorEfficiency;
    public float reflectorReflectivity;
    public boolean moderator = false;
    public boolean moderatorHasBaseStats;
    public int moderatorFlux;
    public float moderatorEfficiency;
    public boolean moderatorActive;
    public boolean shield = false;
    public boolean shieldHasBaseStats;
    public int shieldHeat;
    public float shieldEfficiency;
    public TextureProvider shieldClosedTexture;
    public boolean heater;
    public boolean heaterHasBaseStats;
    public int heaterCooling;
    public boolean source;
    public float sourceEfficiency;
    public TextureProvider texture;
    public Block port;
    public String portOutputDisplayName;
    public TextureProvider portOutputTexture;
    public Block parent;//if this is a port
    public ArrayList<BlockRecipe> allRecipes = new ArrayList<>();
    /**
     * @deprecated You should probably be using allRecipes
     */
    @Deprecated
    public ArrayList<BlockRecipe> recipes = new ArrayList<>();
    public Block(String name){
        this.name = name;
    }
    public Config save(Configuration parent, FissionMSRConfiguration configuration){
        Config config = Config.newConfig();
        config.set("name", name);
        boolean isHereJustToHoldRecipes = false;
        if(parent!=null){
            for(Block b : parent.overhaul.fissionMSR.blocks){
                if(b.name.equals(name))isHereJustToHoldRecipes = true;
            }
        }
        if(isHereJustToHoldRecipes){
            if(fuelVessel){
                config.set("fuelVessel", Config.newConfig());
            }
            if(irradiator){
                config.set("irradiator", Config.newConfig());
            }
            if(reflector){
                config.set("reflector", Config.newConfig());
            }
            if(moderator){
                config.set("moderator", Config.newConfig());
            }
            if(shield){
                config.set("shield", Config.newConfig());
            }
            if(heater){
                config.set("heater", Config.newConfig());
            }
        }else{
            if(displayName!=null)config.set("displayName", displayName);
            if(!legacyNames.isEmpty()){
                ConfigList lst = new ConfigList();
                for(String s : legacyNames)lst.add(s);
                config.set("legacyNames", lst);
            }
            if(cluster)config.set("cluster", cluster);
            if(createCluster)config.set("createCluster", createCluster);
            if(conductor)config.set("conductor", conductor);
            if(functional)config.set("functional", functional);
            if(blocksLOS)config.set("blocksLOS", blocksLOS);
            if(casing)config.set("casing", casing);
            if(casingEdge)config.set("casingEdge", casingEdge);
            if(controller)config.set("controller", controller);
            if(fuelVessel){
                Config fuelVesselCfg = Config.newConfig();
                if(fuelVesselHasBaseStats){
                    if(!recipes.isEmpty())fuelVesselCfg.set("hasBaseStats", true);
                    fuelVesselCfg.set("efficiency", fuelVesselEfficiency);
                    fuelVesselCfg.set("heat", fuelVesselHeat);
                    fuelVesselCfg.set("criticality", fuelVesselCriticality);
                    if(fuelVesselSelfPriming)fuelVesselCfg.set("selfPriming", fuelVesselSelfPriming);
                }
                config.set("fuelVessel", fuelVesselCfg);
            }
            if(irradiator){
                Config irradiatorCfg = Config.newConfig();
                if(irradiatorHasBaseStats){
                    if(!recipes.isEmpty())irradiatorCfg.set("hasBaseStats", true);
                    irradiatorCfg.set("efficiency", irradiatorEfficiency);
                    irradiatorCfg.set("heat", irradiatorHeat);
                }
                config.set("irradiator", irradiatorCfg);
            }
            if(reflector){
                Config reflectorCfg = Config.newConfig();
                if(reflectorHasBaseStats){
                    if(!recipes.isEmpty())reflectorCfg.set("hasBaseStats", true);
                    reflectorCfg.set("efficiency", reflectorEfficiency);
                    reflectorCfg.set("reflectivity", reflectorReflectivity);
                }
                config.set("reflector", reflectorCfg);
            }
            if(moderator){
                Config moderatorCfg = Config.newConfig();
                if(moderatorHasBaseStats){
                    if(!recipes.isEmpty())moderatorCfg.set("hasBaseStats", true);
                    moderatorCfg.set("flux", moderatorFlux);
                    moderatorCfg.set("efficiency", moderatorEfficiency);
                    if(moderatorActive)moderatorCfg.set("active", true);
                }
                config.set("moderator", moderatorCfg);
            }
            if(shield){
                Config shieldCfg = Config.newConfig();
                if(shieldHasBaseStats){
                    if(!recipes.isEmpty())shieldCfg.set("hasBaseStats", true);
                    shieldCfg.set("heat", shieldHeat);
                    shieldCfg.set("efficiency", shieldEfficiency);
                    if(shieldClosedTexture!=null){
                        ConfigNumberList tex = new ConfigNumberList();
                        tex.add(shieldClosedTexture.getSize());
                        for(int x = 0; x<shieldClosedTexture.getSize(); x++){
                            for(int y = 0; y<shieldClosedTexture.getSize(); y++){
                                tex.add(shieldClosedTexture.getColor(x, y));
                            }
                        }
                        shieldCfg.set("closedTexture", tex);
                    }
                }
                config.set("shield", shieldCfg);
            }
            if(heater){
                Config heaterCfg = Config.newConfig();
                if(heaterHasBaseStats){
                    if(!recipes.isEmpty())heaterCfg.set("hasBaseStats", true);
                    heaterCfg.set("cooling", heaterCooling);
                }
                config.set("heater", heaterCfg);
            }
            if(source){
                Config sourceCfg = Config.newConfig();
                sourceCfg.set("efficiency", sourceEfficiency);
                config.set("source", sourceCfg);
            }
            if(texture!=null){
                ConfigNumberList tex = new ConfigNumberList();
                tex.add(texture.getSize());
                for(int x = 0; x<texture.getSize(); x++){
                    for(int y = 0; y<texture.getSize(); y++){
                        tex.add(texture.getColor(x, y));
                    }
                }
                config.set("texture", tex);
            }
            if(!recipes.isEmpty()){
                Config portCfg = Config.newConfig();
                portCfg.set("name", port.name);
                if(port.displayName!=null)portCfg.set("inputDisplayName", port.displayName);
                if(port.texture!=null){
                    ConfigNumberList tex = new ConfigNumberList();
                    tex.add(port.texture.getSize());
                    for(int x = 0; x<port.texture.getSize(); x++){
                        for(int y = 0; y<port.texture.getSize(); y++){
                            tex.add(port.texture.getColor(x, y));
                        }
                    }
                    portCfg.set("inputTexture", tex);
                }
                if(port.portOutputDisplayName!=null)portCfg.set("outputDisplayName", port.portOutputDisplayName);
                if(port.portOutputTexture!=null){
                    ConfigNumberList tex = new ConfigNumberList();
                    tex.add(port.portOutputTexture.getSize());
                    for(int x = 0; x<port.portOutputTexture.getSize(); x++){
                        for(int y = 0; y<port.portOutputTexture.getSize(); y++){
                            tex.add(port.portOutputTexture.getColor(x, y));
                        }
                    }
                    portCfg.set("outputTexture", tex);
                }
                config.set("port", portCfg);
            }
            if(!rules.isEmpty()){
                ConfigList ruls = new ConfigList();
                for(PlacementRule rule : rules){
                    ruls.add(rule.save(parent, configuration));
                }
                config.set("rules", ruls);
            }
        }
        if(!recipes.isEmpty()){
            ConfigList recipesCfg = new ConfigList();
            for(BlockRecipe recipe : recipes){
                recipesCfg.add(recipe.save(this));
            }
            config.set("recipes", recipesCfg);
        }
        return config;
    }
}