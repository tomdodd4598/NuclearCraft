package nc.ncpf.config2;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
class ConfigInteger extends ConfigBase{
    private int data;
    ConfigInteger(int value){
        data = value;
    }
    ConfigInteger(){}
    @Override
    void read(DataInputStream in, short version) throws IOException{
        //Version 0:  Read/write key.  Handled outside, ignore.
        //Version 1:  Current
        data = in.readInt();
    }
    @Override
    void write(DataOutputStream out) throws IOException{
        out.writeInt(data);
    }
    @Override
    Integer getData(){
        return data;
    }
}
