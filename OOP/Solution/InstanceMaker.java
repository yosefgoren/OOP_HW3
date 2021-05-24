package OOP.Solution;

import OOP.Provided.*;

import java.lang.reflect.InvocationTargetException;

public interface InstanceMaker {
    public Object getObject() throws MultipleInjectConstructorsException, NoConstructorFoundException, InvocationTargetException, IllegalAccessException, NoSuitableProviderFoundException, MultipleAnnotationOnParameterException, InstantiationException, MultipleProvidersException;
}
