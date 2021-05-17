package OOP.Solution;

import OOP.Provided.MultipleAnnotationOnParameterException;
import OOP.Provided.MultipleInjectConstructorsException;
import OOP.Provided.NoConstructorFoundException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class Injector {
    public Injector() {
        this.str_bindings = new TreeMap<>();
        this.class_bindings = new HashMap<>();
    }

    Object constructFactory(Class<?> reqc) throws MultipleInjectConstructorsException, NoConstructorFoundException, InvocationTargetException, IllegalAccessException {
        if(class_bindings.containsKey(reqc)) {
            return class_bindings.get(reqc).getObject();
        }
        else return injectFactory(reqc);
    }

    private Object injectFactory(Class<?> reqc) throws MultipleInjectConstructorsException, NoConstructorFoundException, InvocationTargetException, IllegalAccessException {
        //1. initialy get the requested object:
        Object reqo;
        Set<Constructor> inject_anno_ctors = Arrays.stream(reqc.getConstructors())
                .filter(c -> c.isAnnotationPresent(Inject.class)).collect(Collectors.toSet());
        if(inject_anno_ctors.size() > 1){
            throw new MultipleInjectConstructorsException();
        }
        if(inject_anno_ctors.size() == 1){
            reqo = createFromMethod(inject_anno_ctors.toArray()[0]);
        }
        else {
            try {
                reqo = reqc.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new NoConstructorFoundException();
            }
        }

        //2. apply the @inject annotated mathods to the object:
        Set<Method> inject_anno_methods = Arrays.stream(reqc.getDeclaredMethods())// question (2)
                .filter(m -> m.isAnnotationPresent(Inject.class))
                .collect(Collectors.toSet());
        for(Method m : inject_anno_methods){
            boolean is_accessible = m.canAccess(this);//?? does this code do what we expect?
            m.setAccessible(true);
            m.invoke(reqo);
            m.setAccessible(is_accessible);
        }

        //3. get all @inject annotated instance vars and set them as the appropriate values within our object:
        Set<Field> inject_anno_fields = Arrays.stream(reqo.getClass().getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(Inject.class))
                .collect(Collectors.toSet());
        for(Field f : inject_anno_fields){
            boolean is_accessible = f.canAccess(this);//?? does this code do what we expect?
            f.setAccessible(true);
            f.set(reqo, constructFactory(f.getType()));
            f.setAccessible(is_accessible);
        }

        //4.
        return reqo;
    }

    private Object[] getMethodParameters(Method m) throws MultipleInjectConstructorsException, NoConstructorFoundException, InvocationTargetException, IllegalAccessException, MultipleAnnotationOnParameterException {
        Annotation[][] annotations = m.getParameterAnnotations();
        Class<?>[] array_of_args_classes = m.getParameterTypes();
        Object[] evaluated_args = new Object[m.getParameterCount()];

        for(int i = 0; i < m.getParameterCount(); i++){
            Annotation[] annotations_of_arg = annotations[i];
            Class<?> class_of_arg = array_of_args_classes[i];

            if(Arrays.stream(annotations_of_arg).map(Annotation::annotationType)
                    .anyMatch(t -> t == Named.class)) {
                //case 1:
                //TODO: ask daniel: if there is another annotation with @Named, should we throw?
                Named named_annotation = (Named) Arrays.stream(annotations_of_arg)
                        .filter(a -> a.annotationType() == Named.class)
                        .collect(Collectors.toList()).get(0);
                String name = named_annotation.id();
                if(str_bindings.containsKey(name)){
                    evaluated_args[i] = constructFactory(str_bindings.get(name));
                } else {
                    //TODO: what do we do in this case? probably need to throw something.
                }
            } else {
                switch (annotations_of_arg.length) {
                    case 0:
                        evaluated_args[i] = constructFactory(array_of_args_classes[i]);
                        break;
                    case 1:
                        try {
                            evaluated_args[i] = getProvidedParam(m.getReturnType(),
                                    array_of_args_classes[i], annotations_of_arg[0]);
                        } catch (MultipleAnnotationOnParameterException e){
                            evaluated_args[i] = constructFactory(array_of_args_classes[i]);
                        }
                        break;
                    default:
                        throw new MultipleAnnotationOnParameterException();
                }
            }
        }
        return evaluated_args;
    }

    private Object getProvidedParam(Class<?> search_domain, Class<?> search_target, Annotation id_annotation) throws MultipleAnnotationOnParameterException{
        //TODO: implement this function.
    }

    private Object createFromMethod(Method m){
        //TODO: implement this function.
    }

    private Map<String, Class<?>> str_bindings;
    private Map<Class<?>, InstanceMaker> class_bindings;
}
