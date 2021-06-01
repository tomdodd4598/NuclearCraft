package nc.ncpf.config2;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
class ConfigByte extends ConfigBase{
    private byte data;
    ConfigByte(byte value){
        data = value;
    }
    ConfigByte(){}
    @Override
    void read(DataInputStream in, short version) throws IOException{
        //Version 0:  Read/write key.  Handled outside, ignore.
        //Version 1:  Current
        data = in.readByte();
    }
    @Override
    void write(DataOutputStream out) throws IOException{
        out.writeByte(data);
    }
    @Override
    Byte getData(){
        return data;
    }
}
