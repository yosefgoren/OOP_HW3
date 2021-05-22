package OOP.MyTest;

import OOP.Solution.*;

public class WantedClass {
    static @interface MyAnnotation{};
    static class ArgClass { };

    public ArgClass inst_var;

    @Inject
    public WantedClass(@MyAnnotation ArgClass arg){
        this.inst_var = arg;
    }

    @Provides
    @MyAnnotation
    ArgClass providingMethod(){
        return this.inst_var;
    }
}
