package Calculator;

/**
 * Created by Zerbs on 21.11.2016.
 */
public class Log implements SingleOperator{
    private String stringReprezentation = "log";

    @Override
    public double getValue(double arg) {
        return Math.log(arg);
    }

    public String toString(){
        return stringReprezentation;
    }
}
