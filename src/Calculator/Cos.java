package Calculator;

/**
 * Created by Zerbs on 21.11.2016.
 */
public class Cos implements SingleOperator{
    private String stringReprezentation = "cos";

    @Override
    public double getValue(double arg) {
        return Math.cos(arg);
    }

    public String toString(){
        return stringReprezentation;
    }
}
