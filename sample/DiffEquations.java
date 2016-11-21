package sample;

import Calculator.Calculator;
import interfaces.impls.SetPointsContainer;

import java.util.ArrayList;

/**
 * Created by Zerbs on 21.11.2016.
 */
public class DiffEquations {
    public static ArrayList<Point> solveEquation(String expression, double x0, double y0, double epsilon){
        ArrayList<Point> points = new ArrayList<>();
        ArrayList<Double> funcs = new ArrayList<>();
        points.add(new Point(x0,y0,0));
        //Найдем первые 3 точки при помощи метода Рунге - Кутта
        double k0 = 0.0;
        double k1 = 0.0;
        double k2 = 0.0;
        double k3 = 0.0;
        double h = 1.0;
        double x = 0.0;
        double y = 0.0;
        double y_last = y0;
        double x_last = x0;
        String newexpression = null;
        for (int i = 0; i < 3; i++){
            //h = 1.0;
            x+=h;
            do {
                newexpression = expression.replace("x", x_last + "");
                newexpression = newexpression.replace("y", y_last + "");
                k0 = Double.parseDouble(Calculator.Calculate(newexpression));
                funcs.add(k0);
                newexpression = expression.replace("x", (x_last + h / 2) + "");
                newexpression = newexpression.replace("y", (y_last + k0 / 2) + "");
                k1 = Double.parseDouble(Calculator.Calculate(newexpression));
                newexpression = expression.replace("x", (x_last + h / 2) + "");
                newexpression = newexpression.replace("y", (y_last + k1 / 2) + "");
                k2 = Double.parseDouble(Calculator.Calculate(newexpression));
                newexpression = expression.replace("x", (x_last + h) + "");
                newexpression = newexpression.replace("y", (y_last + k2) + "");
                k3 = Double.parseDouble(Calculator.Calculate(newexpression));
                y = y_last + h/6*(k0+2*k1+2*k2+k3);
                h = h/2;
                System.out.println("Difference : "+(y_last-y));
            } while (Math.abs(y-y_last) >= epsilon);
            y_last = y;
            x_last = x;
            points.add(new Point(x,y,0));
            System.out.println("------------------------"+x+"-----------------"+y+"----------------");
        }

        newexpression = expression.replace("x", x_last + "");
        newexpression = newexpression.replace("y", y_last + "");
        k0 = Double.parseDouble(Calculator.Calculate(newexpression));
        funcs.add(k0);

        int i = 4;
        double y_pre;
        while (x < 50){
            x+=h;
            y_pre = points.get(i-1).getY() + h/24*(59*funcs.get(i-1)-55*funcs.get(i-2)+37*funcs.get(i-3)-9*funcs.get(i-4));
            newexpression = expression.replace("x", x + "");
            newexpression = newexpression.replace("y", y_pre + "");
            k0 = Double.parseDouble(Calculator.Calculate(newexpression));
            y = points.get(i-1).getY() + h/24*(9*k0 + 19*funcs.get(i-1) - 5*funcs.get(i-2) + funcs.get(i-3));
            newexpression = expression.replace("x", x + "");
            newexpression = newexpression.replace("y", y + "");
            k0 = Double.parseDouble(Calculator.Calculate(newexpression));
            funcs.add(k0);
            points.add(new Point(x,y,0));
            i++;
        }

        return points;
    }
}
