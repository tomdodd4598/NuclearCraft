package nc.ncpf.configuration;
public enum Vertex{
    PPP(Direction.PX,Direction.PY,Direction.PZ),
    PPN(Direction.PX,Direction.PY,Direction.NZ),
    PNP(Direction.PX,Direction.NY,Direction.PZ),
    PNN(Direction.PX,Direction.NY,Direction.NZ),
    NPP(Direction.NX,Direction.PY,Direction.PZ),
    NPN(Direction.NX,Direction.PY,Direction.NZ),
    NNP(Direction.NX,Direction.NY,Direction.PZ),
    NNN(Direction.NX,Direction.NY,Direction.NZ);
    public final Direction[] directions;
    private Vertex(Direction... directions){
        this.directions = directions;
    }
}