package nc.ncpf.config2;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
abstract class ConfigBase{
    protected ConfigBase(){}
    protected ConfigBase newConfig(int index){
        switch(index){
            case 1:
                return new Config();
            case 2:
                return new ConfigString();
            case 3:
                return new ConfigInteger();
            case 4:
                return new ConfigFloat();
            case 5:
                return new ConfigBoolean();
            case 6:
                return new ConfigLong();
            case 7:
                return new ConfigDouble();
            case 8://Old ConfigHugeLong type, removed
                throw new UnsupportedOperationException("The HugeLong type was removed!  Use SimpleLibrary version 10.2.1 or earlier to read this file.");
            case 9:
                return new ConfigList();
            case 10:
                return new ConfigByte();
            case 11:
                return new ConfigShort();
            case 12:
                return new ConfigNumberList();
            default:
                throw new AssertionError(index);
        }
    }
    abstract void read(DataInputStream in, short version) throws IOException;
    abstract void write(DataOutputStream out) throws IOException;
    int getIndex(){
        Class<? extends ConfigBase> clazz = getClass();
        if(clazz==Config.class){
            return 1;
        }else if(clazz==ConfigString.class){
            return 2;
        }else if(clazz==ConfigInteger.class){
            return 3;
        }else if(clazz==ConfigFloat.class){
            return 4;
        }else if(clazz==ConfigBoolean.class){
            return 5;
        }else if(clazz==ConfigLong.class){
            return 6;
        }else if(clazz==ConfigDouble.class){
            return 7;
        }else if(clazz==ConfigList.class){
            return 9;
        }else if(clazz==ConfigByte.class){
            return 10;
        }else if(clazz==ConfigShort.class){
            return 11;
        }else if(clazz==ConfigNumberList.class){
            return 12;
        }else{
            throw new AssertionError(clazz.getName());
        }
    }
    abstract Object getData();
}
