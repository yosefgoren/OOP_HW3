package OOP.MyTest;


import OOP.Solution.*;
import OOP.Solution.Inject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Locale;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.METHOD})
@interface Weight{}


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.METHOD})
@interface Height{}


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.METHOD})
@interface Lifespan{}



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
        public String toString() {
            return name;
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


    static public class Animal {

        Integer Mass;

        @Inject
        private Animal(@Weight Integer mass){
            this.Mass = mass;
        }





    }

    static public class Mammal extends Animal{
        private Mammal(Integer mass) {
            super(mass);
        }
    }

    static public class Primate extends Mammal{
        private Primate(Integer mass) {
            super(mass);
        }
    }

    static public class Homo extends Primate{
        Integer num_of_eyebrows;

        private Homo(Integer mass) {
            super(mass);
        }
    }

    static public class Sapien extends Homo{
        private Sapien(Integer mass) {
            super(mass);
        }
    }

    static public class Erectus extends Homo {
        @Inject
        public Erectus(@Named(name="Very cool name over here!") MyInteger num_of_eyebrows ,@Weight Integer mass){
            super(mass);
            this.num_of_eyebrows = num_of_eyebrows.getX();
        }
    }

    static public class Neanderthal extends Homo{
        @Inject
        private Neanderthal(Integer mass) {



            super(mass);
        }
    }


    static public class badParams {

        Integer Mass;


        @Inject
        private badParams(@Weight @Height Integer mass) { //should throw
            this.Mass = mass;
        }

    }


        static public class DupProviders {

            Integer Mass;


            @Inject
            private DupProviders(@Weight Integer mass) { //BadInj1 Should throw
                this.Mass = mass;
            }
        }


    static public class NoMatchingProvider {

        Integer Mass;


        @Inject
        private NoMatchingProvider(@Height Integer mass) { //BadInj1 Should throw
            this.Mass = mass;
        }
    }


    static public class MatchingProvider {

        Integer Mass;


        @Inject
        private MatchingProvider(@Height Integer mass) { //BadInj1 Should throw
            this.Mass = mass;
        }
    }




}







