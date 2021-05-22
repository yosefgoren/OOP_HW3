package OOP.MyTest;

import OOP.*;
import OOP.Solution.Injector;

import java.util.Locale;

class InjectorTest {

    public boolean injectBasic(){ //No params
        Injector injector1 = new Injector();

        try {
            Dummies.DummyClass d1 = (Dummies.DummyClass) injector1.construct(Dummies.DummyClass.class);
            if(d1.toString() == ("Dependency injection for dummies".toUpperCase()+" "+20)) return true;
        }catch(Exception e){
            return false;
        }
        return false;
    }
















}


