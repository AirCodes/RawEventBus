package cn.squatam.api.event.bus.impl;

import cn.squatam.api.event.EventMark;
import cn.squatam.api.event.invoker.impl.HandleInvoker;
import cn.squatam.api.event.bus.Bus;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class HandleBus
implements Bus {
     protected Map<String, List<HandleInvoker>> registry = new HashMap<>();
     @Override
     public Object call(Object object) {
          final String CASE = object.getClass().getName();
          if(registry.containsKey(CASE))
               registry.get(CASE).forEach(hi -> hi.invoke(object));
          return object;
     }

     @Override
     public void register(Object object) {
          final Class<?> clazz = object.getClass();
          for(final Method method : clazz.getDeclaredMethods()) {
               if(!isMarkedMethod(method))
                    continue;

               makeInvoker(method, clazz);
          }
          sortList();
     }

     @Override
     public void unregister(Object object) {
          final String owner = object.getClass().getName();
          for(final List<HandleInvoker> invokers : registry.values())
               invokers.removeIf(handleInvoker -> handleInvoker.getOwner().equals(owner));
     }

     private void makeInvoker(Method method, Class<?> clazz){
          try{
               final MethodHandles.Lookup lookup = MethodHandles.lookup();
               final MethodType mt = MethodType.fromMethodDescriptorString(org.objectweb.asm.Type.getMethodDescriptor(method), ClassLoader.getSystemClassLoader());
               final int priority = method.getAnnotation(EventMark.class).value();
               final MethodHandle handle = lookup.findVirtual(clazz, method.getName(), mt);
               final HandleInvoker invoker = new HandleInvoker(handle, clazz.getName(), priority);
               final String paramName = method.getParameterTypes()[0].getName();
               if(registry.containsKey(paramName)) {
                    registry.get(paramName).add(invoker);
                    return;
               }
               registry.put(paramName, new CopyOnWriteArrayList<HandleInvoker>(){private static final long serialVersionUID = 666L;{ add(invoker); }});
          }catch (Throwable t){
               t.printStackTrace();
          }
     }

     private void sortList(){
          final Iterator<List<HandleInvoker>> listIterator = registry.values().iterator();
          while (listIterator.hasNext()){
               final List<HandleInvoker> list = listIterator.next();
               list.sort(Comparator.comparingInt(HandleInvoker::getPriority));
          }
     }

     private boolean isMarkedMethod(final Method method){
          return method.isAnnotationPresent(EventMark.class) && method.getParameterCount() == 1;
     }
}
