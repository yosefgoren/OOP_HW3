package OOP.MyTest;


import  OOP.*;
import OOP.Solution.Inject;
import OOP.Solution.Inject;
import OOP.Solution.Named;

import java.util.Locale;

public class Dummies {


    public static class DummyClass {

        @Inject
        DummyField1 f1;


        String name;


        @Inject
        public DummyClass(){
            name = "Dependency injection for dummies";

        }

        @Inject
        public void changeName(){
            name = name.toUpperCase();
        }

        @Override
        public String toString(){
          return name+" "+f1.toString();
        }





    }


    public static class DummyField1{


        @Inject
        DummyField1() {
            f1 = 20;
        }

        Integer f1;

        @Override
        public String toString() {
            return f1.toString();
        }
    }




     public static class BadDummy extends DummyClass {

    }


}
