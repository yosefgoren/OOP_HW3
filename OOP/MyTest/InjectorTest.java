package OOP.MyTest;

import OOP.Provided.*;
import OOP.Solution.Injector;
import OOP.MyTest.Dummies;
import OOP.Solution.Injector;
import OOP.Solution.Provides;

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
        return d1.toString().equals("Dependency injection for dummies".toUpperCase());
    }


    public static boolean injectErrors() throws MultipleInjectConstructorsException, NoSuitableProviderFoundException, NoConstructorFoundException, InvocationTargetException, MultipleAnnotationOnParameterException, IllegalAccessException, InstantiationException, MultipleProvidersException {//test all inject's exceptions
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

    static Injector inj = new Injector(){

        @Provides
        @Weight
        Integer heavyMethod(){
            return 20;
        }


    };




    public static boolean namedBasic() throws Exception{


        inj.bindByName("Very cool name over here!", Dummies.MyInteger.class);
        inj.bindToInstance(Dummies.MyInteger.class, new Dummies.MyInteger(1));
        Dummies.Erectus gorilla_man = (Dummies.Erectus) inj.construct(Dummies.Erectus.class);
        return gorilla_man.num_of_eyebrows == 1;
    }



    public static boolean providesBasic() throws Exception{
        Dummies.Animal animal = (Dummies.Animal) inj.construct(Dummies.Animal.class);

        return animal.Mass == 20;
    }

    static Injector badInj = new Injector() {


        @Provides
        @Weight
        Integer heavyMethod1(){
            return 1;
        }




        @Provides
        @Weight
        Integer heavyMethod2(){
            return 2;
        }

        @Provides
        @Height
        String TallMethod(){
            return "Tall";
        }

    };



    public static  boolean providesErrors() throws  Exception{
    Boolean bad_params = false;
    Boolean no_matching_providers = false;
    Boolean dup_providers = false;
        try{
            Dummies.badParams  obj = (Dummies.badParams) inj.construct(Dummies.badParams.class);
        }catch(MultipleAnnotationOnParameterException e){
            bad_params = true;
        }

        try{
            Dummies.NoMatchingProvider  obj = (Dummies.NoMatchingProvider) badInj.construct(Dummies.NoMatchingProvider.class);
        }catch(NoSuitableProviderFoundException e){
            no_matching_providers = true;
        }


        try{
            Dummies.DupProviders obj = (Dummies.DupProviders) badInj.construct(Dummies.DupProviders.class);
        }catch(MultipleProvidersException e){
           dup_providers = true;
        }


        return dup_providers&&bad_params&&no_matching_providers;


    }

   // public static  boolean ProvidesAdvanced() throws  Exception{}




}