package OOP.Solution;

public class InstanceObjectType implements InstanceMaker{
    public Object getObject(){
        return obj;
    }

    public InstanceObjectType(Object obj){
        this.obj = obj;
    }

    final private Object obj;
}
