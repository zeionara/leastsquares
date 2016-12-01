package Calculator;

/**
 * Created by Zerbs on 21.11.2016.
 */
public class Sin implements SingleOperator{
    private String stringReprezentation = "sin";

    @Override
    public double getValue(double arg) {
        return Math.sin(arg);
    }

    public String toString(){
        return stringReprezentation;
    }
}
