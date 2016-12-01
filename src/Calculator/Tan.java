package Calculator;

/**
 * Created by Zerbs on 21.11.2016.
 */
public class Tan implements SingleOperator{
    private String stringReprezentation = "tg";

    @Override
    public double getValue(double arg) {
        return Math.sin(arg)/Math.cos(arg);
    }

    public String toString(){
        return stringReprezentation;
    }
}
