package net.ncplanner.ncpf.element;
import java.util.ArrayList;
import java.util.List;
public class NCPFListElement extends NCPFElement{
    public NCPFListElement(){
        super("list");
    }
    public List<NCPFElement> elements = new ArrayList<>();
}
