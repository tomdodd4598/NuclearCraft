package nc.ncpf.config2;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
class ConfigLong extends ConfigBase{
    private long data;
    ConfigLong(long value){
        data = value;
    }
    ConfigLong(){}
    @Override
    void read(DataInputStream in, short version) throws IOException{
        //Version 0:  Read/write key.  Handled outside, ignore.
        //Version 1:  Current
        data = in.readLong();
    }
    @Override
    void write(DataOutputStream out) throws IOException{
        out.writeLong(data);
    }
    @Override
    Long getData(){
        return data;
    }
}
