package cn.squatam.example;

import cn.squatam.api.event.bus.Bus;
import cn.squatam.api.event.bus.impl.ASMBus;
import cn.squatam.api.event.bus.impl.HandleBus;

public class Management {
    /*You only need to initiate one EventBus like the followings.*/

    private Bus javaHandleBus = new HandleBus();
    //The upper seems to be more efficient than the lower one.
    //HandleBus only supports " public void" prefixed methods.
    //OpcodeBus should supports all the " void " prefixed methods. IT'S A DEVELOPMENT WORK.
    private Bus javaOpcodeBus = new ASMBus();
    //Generate getters. Setters are not needed.
    public Bus getJavaHandleBus(){
        return javaHandleBus;
    }

    public Bus getJavaOpcodeBus(){
        return javaOpcodeBus;
    }
    //Create direct register, unregister, call methods to give you a more SIMPLE way to use it.
    //I prefer the opcode bus because it's stable.
    public void call(Object o){
        getJavaOpcodeBus().call(o);
    }

    public void register(Object o){
        getJavaOpcodeBus().register(o);
    }

    public void unregister(Object o){
        getJavaOpcodeBus().unregister(o);
    }
}
