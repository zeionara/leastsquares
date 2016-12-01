package sample;

import Calculator.Calculator;

import java.util.ArrayList;

public class MethodRungeKutta {
    private static double step = 0;

    public static void setStep(double step){
        MethodRungeKutta.step = step;
    }

    public static double getStep(){
        return step;
    }

    public static ArrayList<Point> findPoints(String expression, double x0, double y0, double epsilon, int numOfPoints){
        ArrayList<Point> points = new ArrayList<>();
        points.add(new Point(x0,y0,0));

        double k0;
        double k1;
        double k2;
        double k3;

        double h = epsilon;
        double x = x0;
        double y = y0;
        double dy = 0;
        String newexpression = null;
        for (int i = 0; i < numOfPoints - 1; i++){
            do {
                newexpression = expression.replace("x", x + "");
                newexpression = newexpression.replace("y", y + "");
                k0 = Double.parseDouble(Calculator.Calculate(newexpression));

                newexpression = expression.replace("x", (x + h / 2) + "");
                newexpression = newexpression.replace("y", (y + h*k0 / 2) + "");
                k1 = Double.parseDouble(Calculator.Calculate(newexpression));

                newexpression = expression.replace("x", (x + h / 2) + "");
                newexpression = newexpression.replace("y", (y + h*k1 / 2) + "");
                k2 = Double.parseDouble(Calculator.Calculate(newexpression));

                newexpression = expression.replace("x", (x + h) + "");
                newexpression = newexpression.replace("y", (y + h*k2) + "");
                k3 = Double.parseDouble(Calculator.Calculate(newexpression));

                dy = h/6*(k0+2*k1+2*k2+k3);

                if(Math.abs(dy) >= epsilon){
                    h = h*0.9;
                }
            } while (Math.abs(dy) >= epsilon);
            x+=h;
            y+=dy;
            points.add(new Point(x,y,0));
        }
        step = h;
        return points;
    }
}
