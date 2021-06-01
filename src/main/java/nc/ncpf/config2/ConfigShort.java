package nc.ncpf.config2;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
class ConfigShort extends ConfigBase{
    private short data;
    ConfigShort(short value){
        data = value;
    }
    ConfigShort(){}
    @Override
    void read(DataInputStream in, short version) throws IOException{
        //Version 0:  Read/write key.  Handled outside, ignore.
        //Version 1:  Current
        data = in.readShort();
    }
    @Override
    void write(DataOutputStream out) throws IOException{
        out.writeShort(data);
    }
    @Override
    Short getData(){
        return data;
    }
}
