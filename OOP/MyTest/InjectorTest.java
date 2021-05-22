package OOP.MyTest;

import OOP.Solution.Injector;
import OOP.MyTest.Dummies;
import OOP.Solution.Injector;

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

//    public static boolean namedBasic() throws Exception{
//        Injector injector = new Injector();
//
//        injector.bindByName("Very cool name over here!", Dummies.MyInteger.class);
//        injector.bindToInstance(Dummies.MyInteger.class, new Dummies.MyInteger(8));
//
//    }
}