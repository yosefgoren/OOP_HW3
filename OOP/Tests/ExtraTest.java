package OOP.Tests;

import org.junit.Test;
import OOP.Solution.*;
import OOP.Provided.*;

import javax.management.DescriptorKey;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;


import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;


import static org.junit.Assert.assertEquals;

public class ExtraTest {

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.PARAMETER, ElementType.METHOD})
    @interface Address{}

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.PARAMETER, ElementType.METHOD})
    @interface Message{}

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.PARAMETER, ElementType.METHOD})
    @interface Envelope{}

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.PARAMETER, ElementType.METHOD})
    @interface Stamp{}

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.PARAMETER, ElementType.METHOD})
    @interface Dest{}

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.PARAMETER, ElementType.METHOD})
    @interface Colour{}

    class envelope{


        String size;
        String colour;
        @Inject
        public envelope(@Named(name = "normal") String size, @Colour String colour) {
            this.size = size;
            this.colour = colour;
        }

        @Override
        public String toString() {
            return
                    "size: " + size + '\'' +
                    " colour: " + colour;
        }
    }
    class Person {
        String name;

        public Person(String name) {
            this.name = name;
        }
    }

    class Reciever extends Person{

        public Reciever(String name) {
            super(name);
        }

        @Override
        public String toString() {
            return name;
        }
    }


    public class SuperInjector extends Injector{

        @Provides
        @Address
        private int getAddress(){
            return 123456789;
        }

        @Provides
        @Stamp
        private String getStamp(){
            return "Very fancy Stamp";

        }

    }

    public class DerivedInjector extends SuperInjector{

        @Provides
        @Envelope
        private envelope getEnvelope(){

            try {
                return (envelope) this.construct(envelope.class);
            }catch (Exception e){
                System.out.println("something bad happened" + e.toString());
                e.printStackTrace();

            }
            return null;
        }

        @Provides
        @Message
        private String getMessage(){
            return "Deep and profound message";

        }

        @Provides
        @Colour
        private String getColour(){
            return "White";

        }


        @Provides
        @Address
        private String getAddress(){
            return "Ulmann 703";

        }



    }


    public class Letter{
        envelope pack;
        String stamp;
        String address;
        Person reciever;
        String message;
        @Inject
        public Letter(@Envelope envelope pack,@Stamp String stamp,@Address String address,@Named(name="To whom it may concern") Person reciever,@Message String message) {
            this.pack = pack;
            this.stamp = stamp;
            this.address = address;
            this.reciever = reciever;
            this.message = message;
        }

        @Override
        public String toString() {
            return "Letter: " +
                    " pack: " + pack +'\n'+
                    " stamp: " + stamp + '\n' +
                    " address: " + address + '\n' +
                    " reciever: " + reciever +'\n' +
                    " message: " + message;
        }
    }
    @Test
    public void AdvancedTest_1(){ //Tests a mix of named, provides and Inject
        DerivedInjector inj = new DerivedInjector();

        Reciever daniel = new Reciever("Yosef");

        try {
            inj.bindByName("normal",String.class);
            inj.bindToInstance(String.class,"A4");
            inj.bindByName("To whom it may concern", Reciever.class);
            inj.bind(Person.class,Reciever.class);
            inj.bindToInstance(Reciever.class,daniel);

       Letter l  =  (Letter) inj.construct(Letter.class);
            assertEquals(l.toString(),
                    "Letter:  pack: size: A4' colour: White\n" +
                    " stamp: Very fancy Stamp\n" +
                    " address: Ulmann 703\n" +
                    " reciever: Yosef\n" +
                    " message: Deep and profound message");

        }catch(Exception e) {}

    }


    @Test
    public void Advance(){ //Tests a mix of named, provides and Inject
        DerivedInjector inj = new DerivedInjector();

        Reciever daniel = new Reciever("Yosef");

        try {
            inj.bindByName("normal",String.class);
            inj.bindToInstance(String.class,"A4");
            inj.bindByName("To whom it may concern", Reciever.class);
            inj.bind(Person.class,Reciever.class);
            inj.bindToInstance(Reciever.class,daniel);

            Letter l  =  (Letter) inj.construct(Letter.class);
            assertEquals(l.toString(),
                    "Letter:  pack: size: A4' colour: White\n" +
                            " stamp: Very fancy Stamp\n" +
                            " address: Ulmann 703\n" +
                            " reciever: Yosef\n" +
                            " message: Deep and profound message");

        }catch(Exception e) {}

    }






























}
