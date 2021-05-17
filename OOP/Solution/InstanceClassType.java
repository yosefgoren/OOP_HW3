package OOP.Solution;

import OOP.Provided.MultipleInjectConstructorsException;
import OOP.Provided.NoConstructorFoundException;

import java.lang.reflect.InvocationTargetException;

public class InstanceClassType implements InstanceMaker{
    public Object getObject() throws MultipleInjectConstructorsException, NoConstructorFoundException, InvocationTargetException, IllegalAccessException {
        return injector.constructFactory(target);
    }

    public InstanceClassType(Injector injector, Class<?> target) {
        this.injector = injector;
        this.target = target;
    }

    final private Injector injector;
    final private Class<?> target;
}
