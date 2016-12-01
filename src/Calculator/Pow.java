package Calculator;

public class Pow implements BinaryOperator {
    private String stringReprezentation = "^";

    @Override
    public double getValue(double arg1, double arg2) {
        return Math.pow(arg1,arg2);
    }

    public String toString(){
        return stringReprezentation;
    }
}
