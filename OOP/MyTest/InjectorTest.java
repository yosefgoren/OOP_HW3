package OOP.MyTest;

import OOP.Solution.Injector;
import OOP.MyTest.Dummies;

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
}