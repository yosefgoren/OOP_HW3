package OOP.MyTest;


import java.lang.annotation.*;
import java.util.Arrays;
import java.util.stream.Collectors;

public class MyTest {
    public static void main(String[] args){
        Integer num_anno_methods = Arrays.stream(MyTest.class.getMethods())
                .filter(m -> m.isAnnotationPresent(MyAnno.class))
                .collect(Collectors.toSet()).size();
        Annotation[][] params_anos = MyTest.class.getMethods()[1].getParameterAnnotations();
        for(Annotation[] param : params_anos) {
            for (Annotation ano : param)
            {
                System.out.println(ano.annotationType());
            }
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.PARAMETER)
    public static @interface ArgAnno{
    }

    @MyAnno
    public static void myMethod(@ArgAnno Integer n){
        System.out.println("myMethod: n = "+n.toString());
    }

    @Retention(RetentionPolicy.RUNTIME)
    public static @interface MyAnno {
    }

}
