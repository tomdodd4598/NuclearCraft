package nc.ncpf.texture;
@Deprecated//placeholder class; just so it exports SOMETHING
public class MissingTextureProvider implements TextureProvider{
    @Override
    public int getSize(){
        return 2;
    }
    @Override
    public int getColor(int x, int y){
        return x+y==1?0xffff00ff:0xff000000;
    }
}