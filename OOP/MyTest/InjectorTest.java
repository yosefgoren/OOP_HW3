package OOP.MyTest;

import OOP.Provided.MultipleAnnotationOnParameterException;
import OOP.Provided.MultipleInjectConstructorsException;
import OOP.Provided.NoConstructorFoundException;
import OOP.Provided.NoSuitableProviderFoundException;
import OOP.Solution.Injector;
import OOP.MyTest.Dummies;
import OOP.*;
import OOP.Solution.Injector;
import org.junit.jupiter.api.Assertions;

import java.lang.reflect.InvocationTargetException;
import java.util.Locale;



class InjectorTest {
    public static boolean basicBindToSupplierTest(){
        try {
            Injector injector = new Injector();
            injector.bindToSupplier(Dummies.MyInteger.class, ()->(new Dummies.MyInteger(7)));

            Dummies.MyInteger y = (Dummies.MyInteger) injector.construct(Dummies.MyInteger.class);
            return y.getX() == 7;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean basicBindToObjectTest() throws Exception{
        try {
            Injector injector = new Injector();
            injector.bindToInstance(Dummies.MyInteger.class, new Dummies.MyInteger(5));

            Dummies.MyInteger y = (Dummies.MyInteger) injector.construct(Dummies.MyInteger.class);
            return y.getX() == 5;
        } catch (Exception e) {
            return false;
        }
    }



    public static boolean injectBasic(){ //No params
        Injector injector1 = new Injector();

        try {
            Dummies.DummyClass d1 = (Dummies.DummyClass) injector1.construct(Dummies.DummyClass.class);
            if(d1.toString() == ("Dependency injection for dummies".toUpperCase()+" "+20)) return true;
        }catch(Exception e){
            return false;
        }
        return false;
    }


    public static boolean injectErrors() throws MultipleInjectConstructorsException, NoSuitableProviderFoundException, NoConstructorFoundException, InvocationTargetException, MultipleAnnotationOnParameterException, IllegalAccessException, InstantiationException {//test all inject's exceptions
        Injector injector1 = new Injector();
        boolean did_throw = false;
        try{
         Dummies.MultipleCtors mc = (Dummies.MultipleCtors) injector1.construct(Dummies.MultipleCtors.class);
        }catch(MultipleInjectConstructorsException e){
            did_throw = true;
        }

        if(!did_throw) return false;

        try{
            Dummies.NoAnnoCtors na = (Dummies.NoAnnoCtors) injector1.construct(Dummies.NoAnnoCtors.class);
        }catch(NoConstructorFoundException e){
            did_throw = true;
        }

        return did_throw;




    }












}