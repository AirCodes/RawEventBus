package cn.squatam.api.event.bus;

import java.lang.reflect.Method;

public interface Bus {
     Object call(Object object);

     void register(Object object);

     void unregister(Object object);
}
