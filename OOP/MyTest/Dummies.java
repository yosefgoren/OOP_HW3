package OOP.MyTest;


import OOP.Solution.Inject;
import OOP.Solution.Inject;
import OOP.Solution.Injector;
import OOP.Solution.Named;

import java.util.Locale;

public class Dummies {
    public static class MyInteger{
        Integer x;

        public MyInteger(Integer x) {
            this.x = x;
        }

        public Integer getX() {
            return x;
        }

        public void setX(Integer x) {
            this.x = x;
        }
    }

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




     public static class MultipleCtors extends DummyClass { //Should Throw

        @Inject
        MultipleCtors(Integer p1){}

         @Inject
         MultipleCtors(Integer p1, Integer p2){}



    }


    public static class NoAnnoCtors extends DummyClass { //Should Throw


        NoAnnoCtors(Integer p1){}


    }


    static public class Animal  extends Injector {

        String Name;


    }

    static public class Mammal extends Animal{}

    static public class Primate extends Mammal{}

    static public class Homo extends Primate{
        Integer num_of_eyebrows;
    }

    static public class Sapien extends Homo{}

    static public class Erectus extends Homo {
        @Inject
        public Erectus(@Named(name="Very cool name over here!") MyInteger num_of_eyebrows){
            this.num_of_eyebrows = num_of_eyebrows.getX();
        }
    }

    static public class Neanderthal extends Homo{}









}