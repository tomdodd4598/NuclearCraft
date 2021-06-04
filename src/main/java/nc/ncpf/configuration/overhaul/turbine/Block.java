package nc.ncpf.configuration.overhaul.turbine;
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
        block.texture = texture;
        block.controller = true;
        block.casing = true;
        return block;
    }
    public static Block casing(String name, String displayName, TextureProvider texture, boolean edge){
        Block block = new Block(name);
        block.displayName = displayName;
        block.texture = texture;
        block.casing = true;
        block.casingEdge = edge;
        return block;
    }
    public static Block inlet(String name, String displayName, TextureProvider texture){
        Block block = new Block(name);
        block.displayName = displayName;
        block.texture = texture;
        block.inlet = true;
        return block;
    }
    public static Block outlet(String name, String displayName, TextureProvider texture){
        Block block = new Block(name);
        block.displayName = displayName;
        block.texture = texture;
        block.outlet = true;
        return block;
    }
    public static Block coil(String name, String displayName, TextureProvider texture, float efficiency){
        Block coil = new Block(name);
        coil.displayName = displayName;
        coil.coil = true;
        coil.coilEfficiency = efficiency;
        coil.texture = texture;
        return coil;
    }
    public static Block bearing(String name, String displayName, TextureProvider texture){
        Block bearing = new Block(name);
        bearing.displayName = displayName;
        bearing.bearing = true;
        bearing.texture = texture;
        return bearing;
    }
    public static Block connector(String name, String displayName, TextureProvider texture){
        Block connector = new Block(name);
        connector.displayName = displayName;
        connector.connector = true;
        connector.texture = texture;
        return connector;
    }
    public static Block blade(String name, String displayName, TextureProvider texture, float efficiency, float expansion){
        Block blade = new Block(name);
        blade.displayName = displayName;
        blade.blade = true;
        blade.bladeEfficiency = efficiency;
        blade.bladeExpansion = expansion;
        blade.texture = texture;
        return blade;
    }
    public static Block stator(String name, String displayName, TextureProvider texture, float expansion){
        Block blade = new Block(name);
        blade.displayName = displayName;
        blade.blade = true;
        blade.bladeEfficiency = 0;
        blade.bladeExpansion = expansion;
        blade.bladeStator = true;
        blade.texture = texture;
        return blade;
    }
    public static Block shaft(String name, String displayName, TextureProvider texture){
        Block shaft = new Block(name);
        shaft.displayName = displayName;
        shaft.shaft = true;
        shaft.texture = texture;
        return shaft;
    }
    public String name;
    public String displayName;
    public ArrayList<String> legacyNames = new ArrayList<>();
    public boolean blade = false;
    public float bladeEfficiency;
    public float bladeExpansion;
    public boolean bladeStator;//not just stator cuz it's the stator stat of the blade. makes sense.
    public boolean coil = false;
    public float coilEfficiency;
    public boolean bearing = false;
    public boolean shaft = false;
    public boolean connector = false;
    public boolean controller = false;
    public boolean casing = false;
    public boolean casingEdge = false;
    public boolean inlet = false;
    public boolean outlet = false;
    public TextureProvider texture;
    public Block(String name){
        this.name = name;
    }
    public Config save(Configuration parent, TurbineConfiguration configuration){
        Config config = Config.newConfig();
        config.set("name", name);
        if(displayName!=null)config.set("displayName", displayName);
        if(!legacyNames.isEmpty()){
            ConfigList lst = new ConfigList();
            for(String s : legacyNames)lst.add(s);
            config.set("legacyNames", lst);
        }
        if(bearing)config.set("bearing", true);
        if(shaft)config.set("shaft", true);
        if(connector)config.set("connector", true);
        if(controller)config.set("controller", true);
        if(casing)config.set("casing", true);
        if(casingEdge)config.set("casingEdge", true);
        if(inlet)config.set("inlet", true);
        if(outlet)config.set("outlet", true);
        if(blade){
            Config bladeCfg = Config.newConfig();
            bladeCfg.set("efficiency", bladeEfficiency);
            bladeCfg.set("expansion", bladeExpansion);
            bladeCfg.set("stator", bladeStator);
            config.set("blade", bladeCfg);
        }
        if(coil){
            Config coilCfg = Config.newConfig();
            coilCfg.set("efficiency", coilEfficiency);
            config.set("coil", coilCfg);
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
        if(!rules.isEmpty()){
            ConfigList ruls = new ConfigList();
            for(PlacementRule rule : rules){
                ruls.add(rule.save(parent, configuration));
            }
            config.set("rules", ruls);
        }
        return config;
    }
}