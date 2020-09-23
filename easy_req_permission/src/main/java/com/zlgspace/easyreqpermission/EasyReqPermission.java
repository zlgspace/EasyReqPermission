package com.zlgspace.easyreqpermission;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

public class EasyReqPermission {
    private static final Map<Class<?>, Constructor<? extends Unbinder>> BINDINGS = new LinkedHashMap<>();

    public static<T> T bind(Object object){
        Constructor<? extends Unbinder> constructor = findBindingConstructorForClass(object.getClass());
        if(constructor==null)
            return null;
        Unbinder unbinder = null;
        try {
            unbinder = constructor.newInstance(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return (T)unbinder;
    }


    private static Constructor<? extends Unbinder> findBindingConstructorForClass(Class<?> cls) {
        Constructor<? extends Unbinder> bindingCtor = BINDINGS.get(cls);
        if (bindingCtor != null || BINDINGS.containsKey(cls)) {
            return bindingCtor;
        }
        String clsName = cls.getName();
        if (clsName.startsWith("android.") || clsName.startsWith("java.")
                || clsName.startsWith("androidx.")) {
            return null;
        }
        try {
            Class<?> bindingClass = cls.getClassLoader().loadClass(clsName + "_ReqPermission");
            if(bindingClass==null)
                return null;
            //noinspection unchecked
//            bindingCtor = (Constructor<? extends Unbinder>) bindingClass.getConstructor(cls);
            bindingCtor = (Constructor<? extends Unbinder>) bindingClass.getConstructor(Object.class);
        } catch (ClassNotFoundException e) {
            bindingCtor = findBindingConstructorForClass(cls.getSuperclass());
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Unable to find binding constructor for " + clsName, e);
        }
        BINDINGS.put(cls, bindingCtor);
        return bindingCtor;
    }
}
