package net.ncplanner.ncpf.element;
import java.util.Map;
public class NCPFBlock extends NCPFElement{
    public NCPFBlock(){
        super("block");
    }
    public String name;
    public Map<String, Object> blockstate;
    public String nbt;
}
