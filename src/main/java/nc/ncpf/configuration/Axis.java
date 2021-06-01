package nc.ncpf.configuration;
public enum Axis{
    X(1,0,0),
    Y(0,1,0),
    Z(0,0,1),
    Y_INVERTED(0,1,0);
    public final int x;
    public final int y;
    public final int z;
    private Axis(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Axis get2DXAxis(){
        switch(this){
            case X:
                return Z;
            case Y:
                return X;
            case Y_INVERTED:
                return Z;
            case Z:
                return X;
        }
        return null;
    }
    public Axis get2DYAxis(){
        switch(this){
            case X:
                return Y;
            case Y:
                return Z;
            case Y_INVERTED:
                return X;
            case Z:
                return Y;
        }
        return null;
    }
    public static Axis fromDirection(Direction d){
        for(Axis a : values()){
            if(a.x==Math.abs(d.x)&&a.y==Math.abs(d.y)&&a.z==Math.abs(d.z))return a;
        }
        return null;
    }
}