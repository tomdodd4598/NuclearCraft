package net.ncplanner.ncpf.element;
import java.util.Map;
public class NCPFBlockTag extends NCPFElement{
    public NCPFBlockTag(){
        super("block_tag");
    }
    public String name;
    public Map<String, Object> blockstate;
    public String nbt;
}
