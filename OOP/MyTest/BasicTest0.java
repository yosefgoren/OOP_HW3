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

public class BasicTest0 {
    static class MyInteger{
        Integer x;

        public MyInteger(Integer x) {
            this.x = x;
        }

        public void setX(Integer x) {
            this.x = x;
        }

        public Integer getX() {
            return x;
        }
    };

    public static void main(String[] args) {
        System.out.println("starting main");
        try {
            //basicBindToSupplierTest();
            //basicBindToObjectTest();

            Class[] cArg = new Class[0];
            testRes(BasicTest0.class.getDeclaredMethod("basicBindToSupplierTest", cArg));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        //Method m1 = BasicTest0.class.getDeclaredMethod("basicBindToSupplierTest");

    }

    private static void testRes(Method test_method) throws InvocationTargetException, IllegalAccessException {
        String msg = test_method.getName() + ": ";
        Class[] params = new Class[1];
        params[0] = Integer.class;
            if((boolean)test_method.invoke(null, params)){
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
