package OOP.Solution;

import OOP.Provided.MultipleAnnotationOnParameterException;
import OOP.Provided.MultipleInjectConstructorsException;
import OOP.Provided.NoConstructorFoundException;
import OOP.Provided.NoSuitableProviderFoundException;

import java.lang.reflect.InvocationTargetException;

public interface InstanceMaker {
    public Object getObject() throws MultipleInjectConstructorsException, NoConstructorFoundException, InvocationTargetException, IllegalAccessException, NoSuitableProviderFoundException, MultipleAnnotationOnParameterException, InstantiationException;
}
