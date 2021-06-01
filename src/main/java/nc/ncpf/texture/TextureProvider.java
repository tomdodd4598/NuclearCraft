package nc.ncpf.texture;
public interface TextureProvider{
    public int getSize();
    /**
     * get the color of a pixel at the given location
     * @param x the X coordinate
     * @param y the Y coordinate
     * @return the color in ARGB int format
     */
    public int getColor(int x, int y);
}