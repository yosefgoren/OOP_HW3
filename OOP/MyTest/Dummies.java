package OOP.MyTest;


import OOP.Solution.Inject;
import OOP.Solution.Inject;

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



    public class DummyClass {

        @Inject
        public DummyClass(){


        }

    }


    public class DummyField1{
        Integer f1;

        @Inject
        Integer f2;

        public
        @Inject
        Integer f3;

    }



}
