package OOP.MyTest;

import OOP.Provided.*;
import OOP.Solution.Inject;
import OOP.Solution.Injector;
import OOP.Solution.Provides;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class TestRunner {
    public static void main(String[] args) {
        System.out.println("starting TestRunner.");
        try {
            ArrayList<Method> testing_methods = (ArrayList<Method>) Arrays.stream(InjectorTest.class.getDeclaredMethods())
                    .filter(m -> m.canAccess(null)).collect(Collectors.toList());

            for(Method method : testing_methods){
                testRes(method);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private static void testRes(Method test_method) throws InvocationTargetException, IllegalAccessException {
        String msg = test_method.getName() + ": ";
            if((boolean)test_method.invoke(null)){
                msg += "SUCCESS";
            } else {
                msg += "FAIL";
            }
        System.out.println(msg);
    }
}
