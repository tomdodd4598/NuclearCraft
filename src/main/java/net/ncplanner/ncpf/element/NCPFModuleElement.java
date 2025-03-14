package net.ncplanner.ncpf.element;
public class NCPFModuleElement extends NCPFElement{
    public NCPFModuleElement(){
        super("module");
    }
    public NCPFModuleElement(String name){
        this();
        this.name = name;
    }
    public String name;
}
