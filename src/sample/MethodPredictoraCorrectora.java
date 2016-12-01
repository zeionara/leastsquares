package sample;

import Calculator.Calculator;

import java.util.ArrayList;

/**
 * Created by Zerbs on 21.11.2016.
 */
public class MethodPredictoraCorrectora {
    private static final int beginNumOfPoints = 4;

    public static ArrayList<Point> findPoints(String expression, double x0, double y0, double epsilon, double xn){
        ArrayList<Point> points = MethodRungeKutta.findPoints(expression, x0, y0, epsilon, beginNumOfPoints);
        double h = MethodRungeKutta.getStep();

        ArrayList<Double> funcs = fillFirstFourFuncs(expression, points);
        String newexpression;
        double x = points.get(beginNumOfPoints-1).getX();
        double y;

        double func;

        int i = beginNumOfPoints;
        double y_pre;
        while (x < xn){
            x+=h;
            y_pre = points.get(i-1).getY() + h/24*(59*funcs.get(i-1)-55*funcs.get(i-2)+37*funcs.get(i-3)-9*funcs.get(i-4));

            newexpression = expression.replace("x", x + "");
            newexpression = newexpression.replace("y", y_pre + "");
            func = Double.parseDouble(Calculator.Calculate(newexpression));

            y = points.get(i-1).getY() + h/24*(9*func + 19*funcs.get(i-1) - 5*funcs.get(i-2) + funcs.get(i-3));
            newexpression = expression.replace("x", x + "");
            newexpression = newexpression.replace("y", y + "");
            func = Double.parseDouble(Calculator.Calculate(newexpression));

            funcs.add(func);
            points.add(new Point(x,y,0));
            i++;
        }
        return points;
    }

    private static ArrayList<Double> fillFirstFourFuncs(String expression, ArrayList<Point> points){
        ArrayList<Double> funcs = new ArrayList<>();
        String newexpression;
        double func;
        for (int i = 0; i < 4; i++){
            newexpression = expression.replace("x", points.get(i).getX() + "");
            newexpression = newexpression.replace("y", points.get(i).getY() + "");
            func = Double.parseDouble(Calculator.Calculate(newexpression));
            funcs.add(func);
        }
        return funcs;
    }
}
