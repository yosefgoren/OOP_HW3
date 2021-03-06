package OOP.Solution;

import OOP.Provided.*;
import OOP.Provided.MultipleAnnotationOnParameterException;
import OOP.Provided.MultipleInjectConstructorsException;
import OOP.Provided.NoConstructorFoundException;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Injector {
    public Injector() {
        this.str_bindings = new TreeMap<>();
        this.class_bindings = new HashMap<>();
    }


                     /******* Bindings ******/
    public void bind(Class clazz1 , Class clazz2) throws IllegalBindException{

        if(!clazz1.isAssignableFrom(clazz2)) throw new IllegalBindException();
        if(clazz1 == clazz2) {
            class_bindings.remove(clazz1);
        }else {
            class_bindings.put(clazz1, new InstanceClassType(this, clazz2));
        }
    }

    public void bindToInstance(Class clazz, Object obj) throws IllegalBindException {
        if(!clazz.isInstance(obj)) throw new IllegalBindException(); //isInstance also checks if obj is of a subclass

        class_bindings.put(clazz, new InstanceObjectType(obj));

    }


    public void bindToSupplier(Class clazz, Supplier sup){

        class_bindings.put(clazz, new InstanceSupplierType(sup));
    }

    public void bindByName(String s,Class clazz){
        str_bindings.put(s,clazz);
    }

                 /******* construct ******/

    public Object construct(Class clazz) throws MultipleInjectConstructorsException, NoConstructorFoundException, NoSuitableProviderFoundException, MultipleAnnotationOnParameterException, MultipleProvidersException{

        try {
            return constructFactory(clazz);
        }catch(InvocationTargetException | IllegalAccessException | InstantiationException e){

        }
        return null;
    }


                 /******* Factories ******/
    Object constructFactory(Class<?> reqc) throws MultipleInjectConstructorsException, NoConstructorFoundException, InvocationTargetException, IllegalAccessException, NoSuitableProviderFoundException, MultipleAnnotationOnParameterException, InstantiationException, MultipleProvidersException {
        if(class_bindings.containsKey(reqc)) {
            return class_bindings.get(reqc).getObject();
        }
        else return injectFactory(reqc);
    }

    private Object injectFactory(Class<?> reqc) throws MultipleInjectConstructorsException, NoConstructorFoundException, InvocationTargetException, IllegalAccessException, NoSuitableProviderFoundException, MultipleAnnotationOnParameterException, InstantiationException, MultipleProvidersException {
        //1. initialy get the requested object:
        Object reqo;
         List<Constructor> inject_anno_ctors = Arrays.stream(reqc.getDeclaredConstructors())
                .filter(c -> c.isAnnotationPresent(Inject.class)).collect(Collectors.toList());
        if(inject_anno_ctors.size() > 1){
            throw new MultipleInjectConstructorsException();
        }
        if(inject_anno_ctors.size() == 1){
            Constructor c = inject_anno_ctors.get(0);
            boolean is_accessible = c.canAccess(null);
            c.setAccessible(true);
            reqo = createFromConstructor(c);
            c.setAccessible(is_accessible);
        }
        else {
            Constructor default_ctor;
            try {
                default_ctor = reqc.getDeclaredConstructor();
            } catch (Exception e) {
                throw new NoConstructorFoundException();
            }
            boolean is_accessible = default_ctor.canAccess(null);
            default_ctor.setAccessible(true);
            reqo = default_ctor.newInstance();
            default_ctor.setAccessible(is_accessible);
        }

        //2. apply the @inject annotated methods to the object:
        Set<Method> inject_anno_methods = Arrays.stream(reqc.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(Inject.class))
                .collect(Collectors.toSet());
        for(Method m : inject_anno_methods){
            boolean is_accessible = m.canAccess(Modifier.isStatic(m.getModifiers()) ? null : reqo);
            m.setAccessible(true);
            Object[] args = evaluateParams(m.getParameterAnnotations(), m.getParameterTypes(), reqo, reqc);
            m.invoke(reqo, args);
            m.setAccessible(is_accessible);
        }

        //3. get all @inject annotated instance vars and set them as the appropriate values within our object:
        Set<Field> inject_anno_fields = Arrays.stream(reqo.getClass().getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(Inject.class))
                .collect(Collectors.toSet());
        for(Field f : inject_anno_fields){
            Object access_provider_obj = null;
            if(!Modifier.isStatic(f.getModifiers()))
            {
                access_provider_obj = reqo;
            }
            boolean is_accessible = f.canAccess(Modifier.isStatic(f.getModifiers()) ? null : reqo);
            f.setAccessible(true);
            f.set(reqo, constructFactory(f.getType()));
            f.setAccessible(is_accessible);
        }

        //4.
        return reqo;
    }

    private Object[] evaluateParams(Annotation[][] annotations, Class<?>[] array_of_args_classes, Object obj_domain, Class<?> target_class) throws MultipleInjectConstructorsException, NoConstructorFoundException, InvocationTargetException, IllegalAccessException, MultipleAnnotationOnParameterException, NoSuitableProviderFoundException, InstantiationException, MultipleProvidersException {
        //Annotation[][] annotations = m.getParameterAnnotations();
        //Class<?>[] array_of_args_classes = m.getParameterTypes();
        Object[] evaluated_args = new Object[array_of_args_classes.length];

        for(int i = 0; i < array_of_args_classes.length; i++){
            Annotation[] annotations_of_arg = annotations[i];
            Class<?> class_of_arg = array_of_args_classes[i];

            if(Arrays.stream(annotations_of_arg).map(Annotation::annotationType)
                    .anyMatch(t -> t == Named.class)) {
                Named named_annotation = (Named) Arrays.stream(annotations_of_arg)
                        .filter(a -> a.annotationType() == Named.class)
                        .collect(Collectors.toList()).get(0);
                String name = named_annotation.value();
                if(str_bindings.containsKey(name)){
                    evaluated_args[i] = constructFactory(str_bindings.get(name));
                }
            } else {
                switch (annotations_of_arg.length) {
                    case 0:
                        evaluated_args[i] = constructFactory(array_of_args_classes[i]);
                        break;
                    case 1:
                        evaluated_args[i] = getProvidedParam(target_class,
                                array_of_args_classes[i], obj_domain, annotations_of_arg[0]);
                        break;
                    default:
                        throw new MultipleAnnotationOnParameterException();
                }
            }
        }
        return evaluated_args;
    }

    private Object getProvidedParam(Class<?> search_domain, Class<?> search_target,Object obj_target, Annotation id_annotation) throws MultipleAnnotationOnParameterException, MultipleInjectConstructorsException, NoConstructorFoundException, InvocationTargetException, IllegalAccessException, NoSuitableProviderFoundException, InstantiationException, MultipleProvidersException {
        //Old impl: Class c = search_domain;
        Class c = this.getClass();
        List<Method> provides_anno_methods = new ArrayList<>();


        do {
            provides_anno_methods.addAll(Arrays.stream(c.getDeclaredMethods())
                    .filter(m -> (m.isAnnotationPresent(Provides.class) && m.isAnnotationPresent(id_annotation.annotationType())))
                    .collect(Collectors.toList()));


            c = c.getSuperclass();
        }while (c!=null);

        if(provides_anno_methods.size()==0)
            return injectFactory(search_target);

        List<Method> matching_methods = provides_anno_methods.stream()
                .filter(m->m.getReturnType()==search_target)
                .collect(Collectors.toList());

        switch(matching_methods.size()){

            case 0:
                throw new NoSuitableProviderFoundException();

            case 1:
                return createFromMethod(matching_methods.get(0), obj_target);

            default:
                throw new MultipleProvidersException();
        }
    }

    private Object createFromMethod(Method m, Object o) throws MultipleInjectConstructorsException, NoSuitableProviderFoundException, NoConstructorFoundException, InvocationTargetException, MultipleAnnotationOnParameterException, IllegalAccessException, InstantiationException, MultipleProvidersException {


        m.setAccessible(true);
           Object[] args_list = evaluateParams(m.getParameterAnnotations(), m.getParameterTypes(), o, m.getDeclaringClass());
           return m.invoke(this, args_list);


     //   return created_obj;
    }

    private Object createFromConstructor(Constructor c) throws MultipleInjectConstructorsException, NoSuitableProviderFoundException, NoConstructorFoundException, InvocationTargetException, MultipleAnnotationOnParameterException, IllegalAccessException, InstantiationException, MultipleProvidersException {
        Object[] args_list = evaluateParams(c.getParameterAnnotations(), c.getParameterTypes(), null, c.getDeclaringClass());
        return c.newInstance(args_list);
    }

    private Map<String, Class<?>> str_bindings;
    private Map<Class<?>, InstanceMaker> class_bindings;
}
