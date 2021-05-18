package OOP.Solution;

import java.util.function.Supplier;

public class InstanceSupplierType implements  InstanceMaker{
    public Object getObject(){
        return sup.get();
    }

    public InstanceSupplierType(Supplier sup){
        this.sup = sup;
    }

        final private Supplier sup;
}
