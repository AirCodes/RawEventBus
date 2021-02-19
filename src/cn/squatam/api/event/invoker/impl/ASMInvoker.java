package cn.squatam.api.event.invoker.impl;

import com.esotericsoftware.reflectasm.MethodAccess;
import cn.squatam.api.event.invoker.Invoker;

public class ASMInvoker implements Invoker {
     final MethodAccess accessor;
     final Object instance;
     final int index;
     final int priority;

     public ASMInvoker(MethodAccess access, Object instance,
                       int index, int priority) {
          this.accessor = access;
          this.instance = instance;
          this.index = index;
          this.priority = priority;
     }

     public int getPriority() {
          return priority;
     }

     public boolean isInInstance(Object object){
          return this.instance.equals(object);
     }

     @Override
     public Object invoke(Object param) {
          return this.accessor.invoke(instance, index, param);
     }
}
