package cn.squatam.api.event.bus.impl;

import cn.squatam.api.event.EventMark;
import cn.squatam.api.event.invoker.impl.ASMInvoker;
import com.esotericsoftware.reflectasm.MethodAccess;
import cn.squatam.api.event.bus.Bus;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class ASMBus
implements Bus {
     protected Map<String, MethodAccess> accesses = new HashMap<>();
     protected Map<String, List<ASMInvoker>> registry = new HashMap<>();

     @Override
     public Object call(Object object) {
          final String CASE = object.getClass().getName();
          if(registry.containsKey(CASE))
               registry.get(CASE).forEach(asmInvoker -> asmInvoker.invoke(object));
          return object;
     }

     @Override
     public void register(Object object) {
          final String CASE = object.getClass().getName();

          MethodAccess access;

          if(accesses.containsKey(CASE)) {
               access = accesses.get(object.getClass());
          } else{
               access = MethodAccess.get(object.getClass());
               accesses.put(CASE, access);
          }

          for(Method method :object.getClass().getDeclaredMethods()) {
               if(!isMarkedMethod(method))
                    continue;

               makeInvoker(method, access, object);
          }

          sortList();
     }

     @Override
     public void unregister(Object object) {
          registry.values().forEach(asmInvokers -> {
               asmInvokers.removeIf(asmInvoker -> asmInvoker.isInInstance(object));
          });
     }

     private void makeInvoker(Method method, final MethodAccess access, final Object instance) {
          final int priority = method.getAnnotation(EventMark.class).value();
          final int index = access.getIndex(method.getName(), method.getParameterCount());
          final String paramName = method.getParameterTypes()[0].getName();
          final ASMInvoker invoker = new ASMInvoker(access, instance, index, priority);
          if(registry.containsKey(paramName)) {
               registry.get(paramName).add(invoker);
               return;
          }
          registry.put(paramName, new CopyOnWriteArrayList<ASMInvoker>(){
               private static final long serialVersionUID = 666L;
               {
                    add(invoker);
               }
          });
     }

     private void sortList(){
          Iterator<List<ASMInvoker>> listIterator = registry.values().iterator();
          while (listIterator.hasNext()){
               List<ASMInvoker> list = listIterator.next();
               list.sort(Comparator.comparingInt(ASMInvoker::getPriority));
          }
     }

     private boolean isMarkedMethod(Method method){
          return method.isAnnotationPresent(EventMark.class) && method.getParameterCount() == 1;
     }
}
