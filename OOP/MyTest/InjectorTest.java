package OOP.MyTest;

import OOP.Provided.MultipleAnnotationOnParameterException;
import OOP.Provided.MultipleInjectConstructorsException;
import OOP.Provided.NoConstructorFoundException;
import OOP.Provided.NoSuitableProviderFoundException;
import OOP.Solution.Injector;
import OOP.MyTest.Dummies;
import OOP.Solution.Injector;

import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

class InjectorTest {
    public static boolean basicBindToSupplierTest() throws Exception{
            Injector injector = new Injector();
            injector.bindToSupplier(Dummies.MyInteger.class, ()->(new Dummies.MyInteger(7)));

            Dummies.MyInteger y = (Dummies.MyInteger) injector.construct(Dummies.MyInteger.class);
            return y.getX() == 7;
    }

    public static boolean basicBindToObjectTest() throws Exception{
            Injector injector = new Injector();
            injector.bindToInstance(Dummies.MyInteger.class, new Dummies.MyInteger(5));

            Dummies.MyInteger y = (Dummies.MyInteger) injector.construct(Dummies.MyInteger.class);
            return y.getX() == 5;
    }

    public static boolean injectBasic() throws Exception{
        Injector injector1 = new Injector();

        Dummies.DummyClass d1 = (Dummies.DummyClass) injector1.construct(Dummies.DummyClass.class);
        return d1.toString().equals("Dependency injection for dummies".toUpperCase()+" 20");
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
    public static boolean namedBasic() throws Exception{
        Injector injector = new Injector();

        injector.bindByName("Very cool name over here!", Dummies.MyInteger.class);
        injector.bindToInstance(Dummies.MyInteger.class, new Dummies.MyInteger(1));
        Dummies.Erectus gorilla_man = (Dummies.Erectus) injector.construct(Dummies.Erectus.class);
        return gorilla_man.num_of_eyebrows == 1;
    }
}