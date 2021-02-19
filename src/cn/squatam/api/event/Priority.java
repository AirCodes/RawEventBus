package cn.squatam.api.event;

public class Priority {
     public static final byte
           HIGHEST = 0,
           HIGH = 1,
           MEDIUM = 2,
           LOW = 3,
           LOWEST = 4;

     public static final byte[] VALUE_ARRAY;

     static {
          VALUE_ARRAY = new byte[]{
                HIGHEST,
                HIGH,
                MEDIUM,
                LOW,
                LOWEST
          };
     }
}
