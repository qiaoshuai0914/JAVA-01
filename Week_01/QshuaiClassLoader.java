import java.io.InputStream;
import java.lang.reflect.Method;

// 破坏了委托机制
public class QshuaiClassLoader extends ClassLoader{

    public static void main(String[] args) {
        try {
            Class<?> clazz = new QshuaiClassLoader().findClass("Hello");
            Method declaredMethod = clazz.getDeclaredMethod("hello");
            declaredMethod.invoke(clazz.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected Class<?> findClass(String name)throws ClassNotFoundException{
        byte[] bytes =readFile(name);
        return defineClass(name, bytes, 0, bytes.length);
    }
  
    public   byte[] readFile(String name){
        InputStream is = null;
        try{
            is =  this.getClass().getClassLoader().getResourceAsStream(name+".xlass");;
            int iAvail = is.available();
            byte[] bytes = new byte[iAvail];
            is.read(bytes);
            return decode(bytes);
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            try {
                is.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }
    private byte[] decode(byte[] xlass) {
        for (int i = 0; i < xlass.length; i++) {
            xlass[i] = (byte) (255 - xlass[i]);
        }
        return xlass;
    }
}
