package nc.ncpf.configuration.overhaul.fissionmsr;
import nc.ncpf.config2.Config;
import nc.ncpf.config2.ConfigList;
import nc.ncpf.configuration.Configuration;
public class PlacementRule extends RuleContainer{
    //TODO fromNC
    public RuleType ruleType = RuleType.BETWEEN;
    public BlockType blockType = BlockType.AIR;
    public Block block;
    public byte min;
    public byte max;
    public Config save(Configuration parent, FissionMSRConfiguration configuration){
        Config config = Config.newConfig();
        byte blockIndex = (byte)(configuration.blocks.indexOf(block)+1);
        if(parent!=null){
            blockIndex = (byte)(parent.overhaul.fissionMSR.allBlocks.indexOf(block)+1);
        }
        switch(ruleType){
            case BETWEEN:
                config.set("type", (byte)0);
                config.set("block", blockIndex);
                config.set("min", min);
                config.set("max", max);
                break;
            case AXIAL:
                config.set("type", (byte)1);
                config.set("block", blockIndex);
                config.set("min", min);
                config.set("max", max);
                break;
            case VERTEX:
                config.set("type", (byte)2);
                config.set("block", blockIndex);
                break;
            case BETWEEN_GROUP:
                config.set("type", (byte)3);
                config.set("block", (byte)blockType.ordinal());
                config.set("min", min);
                config.set("max", max);
                break;
            case AXIAL_GROUP:
                config.set("type", (byte)4);
                config.set("block", (byte)blockType.ordinal());
                config.set("min", min);
                config.set("max", max);
                break;
            case VERTEX_GROUP:
                config.set("type", (byte)5);
                config.set("block", (byte)blockType.ordinal());
                break;
            case OR:
                config.set("type", (byte)6);
                ConfigList ruls = new ConfigList();
                for(PlacementRule rule : rules){
                    ruls.add(rule.save(parent, configuration));
                }
                config.set("rules", ruls);
                break;
            case AND:
                config.set("type", (byte)7);
                ruls = new ConfigList();
                for(PlacementRule rule : rules){
                    ruls.add(rule.save(parent, configuration));
                }
                config.set("rules", ruls);
                break;
        }
        return config;
    }
    public static enum RuleType{
        BETWEEN,AXIAL,VERTEX,BETWEEN_GROUP,AXIAL_GROUP,VERTEX_GROUP,OR,AND;
    }
    public static enum BlockType{
        AIR,CASING,HEATER,VESSEL,MODERATOR,REFLECTOR,SHIELD,IRRADIATOR,CONDUCTOR;
    }
}