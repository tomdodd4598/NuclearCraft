package nc.ncpf.config2;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
public class ConfigNumberList extends ConfigBase{
    private ArrayList<Number> lst = new ArrayList<>();
    public ConfigNumberList(){}
    public ConfigNumberList(Collection<? extends Number> lst){
        for(Number o : lst){
            add(o);
        }
    }
    @Override
    ConfigNumberList getData(){
        return this;
    }
    public long get(int index){
        if(index>=size()||index<0) return 0;
        else return (long) lst.get(index);
    }
    public int size(){
        return lst.size();
    }
    public long remove(int index){
        long val = get(index);
        if(index>=0&&index<size()) lst.remove(index);
        return val;
    }
    public <V extends Number> Collection<V> copyTo(Collection<V> lst){
        for(V v : this.<V>iterable()){
            lst.add(v);
        }
        return lst;
    }
    public <V extends Number> Iterable<V> iterable(){
        return new Iterable<V>() {
            @Override
            public Iterator<V> iterator() {
                return ConfigNumberList.this.iterator();
            }
        };
    }
    public <V extends Number> Iterator<V> iterator(){
        return new Iterator<V>(){
            Iterator<Number> it = lst.iterator();
            V next;
            @Override
            public boolean hasNext() {
                while(next==null&&it.hasNext()){
                    try{
                        next = (V)it.next();
                    }catch(ClassCastException ex){}
                }
                return next!=null;
            }
            @Override
            public V next() {
                hasNext();
                V val = next;
                next = null;
                return val;
            }
        };
    }
    public <V extends Number> boolean contains(V value){
        if(value==null) return false;//Block null entries
        for(Number n : lst){
            if(n.equals(value)) return true;
        }
        return false;
    }
    public void add(Number value){
        if(value==null){
            throw new NullPointerException("Cannot add null to a ConfigNumberList!");
        }else if(value instanceof Long||value instanceof Integer||value instanceof Short||value instanceof Byte){
            doAdd(value);
        }else{
            throw new IllegalArgumentException("ConfigNumberList can only take primitive integral types!");
        }
    }
    public void add(long value){
        add((Number)value);
    }
    public void add(int value){
        add((Number)value);
    }
    public void add(short value){
        add((Number)value);
    }
    public void add(byte value){
        add((Number)value);
    }
    private void doAdd(Number value){
        if(value==null) throw new IllegalArgumentException("Cannot set null values to a ConfigNumberList!");//Should never happen
        lst.add(value.longValue());
    }
    @Override
    void write(DataOutputStream out) throws IOException{
        int sizeClass = 0;
        int size = lst.size();
        if(size>0x3f) sizeClass++;//Must use two bytes
        if(size>0x3fff) sizeClass++;//Must use 4 bytes
        if(size>0x3fffffff) sizeClass++;//Must use 5 bytes
        sizeClass = sizeClass<<6;
        //sizeClass is now 0bXX00_0000, where X represents size class and N represents size
        switch (sizeClass) {
            case 0xc0://Must use 5 bytes (0b11000000_NNNNNNNN_NNNNNNNN_NNNNNNNN_NNNNNNNN
                out.write((int)sizeClass);
                out.writeInt(size);
                break;
            case 0x80://Must use 4 bytes (0b10NNNNNN_NNNNNNNN_NNNNNNNN_NNNNNNNN
                out.writeInt((sizeClass<<24)|size);
                break;
            case 0x40://Must use 2 bytes (0b01NNNNNN_NNNNNNNN)
                out.writeShort((short)((sizeClass<<8)|size));
                break;
            case 0x00://One byte (0b00NNNNNN)
                out.write(sizeClass|size);
                break;
            default:
                throw new IllegalStateException("This shouldn't be possible.");
        }
        if(size==0) return;//Empty list
        int maxDigits = 0;
        int digits;
        boolean hasNeg = false;
        boolean verbatum = false;
        for(Number n : lst){
            long l = n.longValue();
            if(l==Long.MIN_VALUE){
                verbatum = true;
                break;
            }
            if(l<0){
                l=-l;
                hasNeg = true;
            }
            digits = 0;
            while(l>0){
                digits++;
                l>>=1;
            }
            if(digits>maxDigits) maxDigits = digits;
        }
        //maxDigits now carries the greatest number of bits required for each number, minus one if hasNeg==true.
        //This is a value from 0 to 63:  0b0000_0000 to 0b0011_1111.
        //THUS:  0bABCC_CCCC
        //Where A is the 'verbatum flag'- to indicate if any entries were equal to Long.MIN_VALUE.  When this flag is set, all other bits are dead.
        //B is the hasNeg flag
        //and C is the needed bit count
        if(hasNeg) maxDigits |= 0x40;
        if(verbatum) maxDigits |= 0x80;
        out.write(maxDigits);
        if(maxDigits==0) return;//List of 0s
        int currentByte = 0;
        int left = 8;
        long nextNumber = 0;
        int nextLeft = 0;
        int bits, txfr;
        long mask;
        for(Number n : lst){
            nextNumber = n.longValue();
            if(verbatum){
                out.writeLong(nextNumber);//Verbatum is boring
                continue;
            }
            if(hasNeg){
                currentByte|=(nextNumber<0?1:0)<<(left-1);
                left--;
                nextNumber = Math.abs(nextNumber);//Make it positive
            }
            nextLeft = maxDigits&0x3f;//Strip the 'verbatum' and 'hasNeg' flags
            while(nextLeft>0||left<1){
                if(left<1){//currentByte is full
                    out.write(currentByte);
                    currentByte = 0;
                    left = 8;
                }else{
                    //nextLeft>0, fill the currentByte
                    bits = Math.min(nextLeft, left);//Number of bits to transfer
                    mask = (0xFFL>>(8-bits))<<(nextLeft-bits);//The mask to apply to nextNumber to extract the desired bits
                    txfr = (int)((nextNumber&mask)>>(nextLeft-bits));//The bits we're transferring
                    currentByte |= txfr<<(left-bits);//Make sure they apply in the right spot
                    left-=bits;//Adjust the write head
                    nextLeft-=bits;//Adjust the read head
                }
            }
        }
        if(left<8){
            out.write(currentByte);//A byte was not completed, just write it.  The remainder will be 0s.
        }
    }
    void read(DataInputStream in, short version) throws IOException{
        if(version<1) throw new IllegalArgumentException("ConfigNumberList didn't exist in version "+version+"!");
        int abyte = in.read();
        int sizeClass = (abyte&0xc0)>>6;//Extract the size class from it- 0bXX00_0000- and shift it to the right, 0bXX (0-3).
        int size = 0;
        switch(sizeClass){
            case 0://Remaining 6 bits are the size- 0b00NNNNNN
                size = abyte&0x3f;
                break;
            case 1://Remaining 6 bits and next byte are the size- 0b01NNNNNN_NNNNNNNN
                size = ((abyte&0x3f)<<8)|in.read();
                break;
            case 2://Remaining 6 bits and next 3 bytes are the size- 0b10NNNNNN_NNNNNNNN_NNNNNNNN_NNNNNNNN
                size = ((abyte&0x3f)<<24)|(in.read()<<16)|(in.read()<<8)|in.read();
                break;
            case 3://Next 4 bytes are the size- 0b11000000_NNNNNNNN_NNNNNNNN_NNNNNNNN_NNNNNNNN
                size = in.readInt();
                break;
            default:
                throw new IllegalStateException("This shouldn't be possible.");
        }
        if(size==0) return;//Empty list
        int digits = in.read();
        boolean verbatum = (digits&0x80)>0;
        if(verbatum){
            for(int i = 0; i<size; i++){
                add(in.readLong());//Verbatum is boring
            }
            return;
        }
        boolean hasNeg = (digits&0x40)>0;
        digits &= 0x3f;//Trim off the flags
        if(digits==0){
            for(int i = 0; i<size; i++){//Lists of 0s are boring
                add(0);
            }
            return;
        }
        int currentByte = 0;
        int left = 0;
        long number = 0;
        int nextLeft = 0;
        boolean isNeg = false;
        int bits;
        long mask, txfr;
        for(int i = 0; i<size; i++){
            if(left<1){
                currentByte = in.read();
                left = 8;
            }
            if(hasNeg){
                isNeg = (currentByte&(1<<(left-1)))>0;
                left--;
            }
            number = 0;
            nextLeft = digits;
            while(nextLeft>0){
                if(left<1){
                    currentByte = in.read();
                    left = 8;
                }
                bits = Math.min(nextLeft, left);//Number of bits to transfer
                mask = (0xFFL>>(8-bits))<<(left-bits);//The mask to apply to currentByte to extract the desired bits
                txfr = (int)((currentByte&mask)>>(left-bits));//The bits we're transferring
                number |= txfr<<(nextLeft-bits);//Make sure they apply in the right spot
                nextLeft-=bits;//Adjust the write head
                left-=bits;//Adjust the read head
            }
            if(isNeg) number = -number;
            add(number);
        }
    }
    @Override
    public boolean equals(Object obj) {
        if(obj==null||obj.getClass()!=getClass()) return false;
        ConfigNumberList l = (ConfigNumberList) obj;
        if(l.lst.size()!=lst.size()) return false;
        for(int i = 0; i<lst.size(); i++){
            if(lst.get(i).longValue()!=l.lst.get(i).longValue()) return false;
        }
        return true;
    }
}