package OOP.MyTest;

import OOP.Provided.*;
import OOP.Solution.Inject;
import OOP.Solution.Injector;
import OOP.Solution.Provides;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TestRunner {
    public static void main(String[] args) {
        System.out.println("starting main");
        try {
            Method[] testing_methods = InjectorTest.class.getDeclaredMethods();

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

    private static boolean basicBindToSupplierTest(){
        try {
            Injector injector = new Injector();
            injector.bindToSupplier(MyInteger.class, ()->(new MyInteger(7)));

            MyInteger y = (MyInteger) injector.construct(MyInteger.class);
            return y.getX() == 7;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean basicBindToObjectTest() throws Exception{
        try {
            Injector injector = new Injector();
            injector.bindToInstance(MyInteger.class, new MyInteger(5));

            MyInteger y = (MyInteger) injector.construct(MyInteger.class);
            return y.getX() == 5;
        } catch (Exception e) {
            return false;
        }
    }
}
