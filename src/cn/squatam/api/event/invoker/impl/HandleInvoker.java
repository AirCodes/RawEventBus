package cn.squatam.api.event.invoker.impl;

import cn.squatam.api.event.invoker.Invoker;

import java.lang.invoke.MethodHandle;

public class HandleInvoker implements Invoker {
     final MethodHandle handle;
     final String owner;
     final int priority;
     public HandleInvoker(MethodHandle handle, String owner, int priority){
          this.handle = handle;
          this.priority = priority;
          this.owner = owner;
     }

     public String getOwner() {
          return owner;
     }

     public int getPriority() {
          return priority;
     }

     @Override
     public Object invoke(Object param)  {
          try{
               return handle.invokeExact(param);
          }catch (Throwable e){
               return null;
          }
     }
}
