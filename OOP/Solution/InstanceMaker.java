package OOP.Solution;

import OOP.Provided.MultipleInjectConstructorsException;
import OOP.Provided.NoConstructorFoundException;

import java.lang.reflect.InvocationTargetException;

public interface InstanceMaker {
    public Object getObject() throws MultipleInjectConstructorsException, NoConstructorFoundException, InvocationTargetException, IllegalAccessException;
}
