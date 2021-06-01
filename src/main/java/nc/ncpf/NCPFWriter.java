package nc.ncpf;
import java.io.OutputStream;
import nc.ncpf.config2.Config;
import nc.ncpf.configuration.Configuration;
public class NCPFWriter{
    public static final byte NCPF_VERSION = 10;
    public static void write(Configuration configuration, OutputStream stream){
        //<editor-fold defaultstate="collapsed" desc="Header">
        Config header = Config.newConfig();
        header.set("version", NCPF_VERSION);
        header.set("count", 0);
        header.save(stream);
//</editor-fold>
        configuration.save(null, Config.newConfig()).save(stream);
    }
}