package sample;

import gauss.Algorithm;
import gauss.LinearSystem;
import gauss.MyEquation;
import interfaces.impls.SetPointsContainer;

import java.util.ArrayList;

/**
 * Created by Zerbs on 02.11.2016.
 */
public class LeastSquares {
    private static int range = 2;

    public static int getRange(){
        return range;
    }
    public static void setRange(int range){
        LeastSquares.range = range;
    }

    public static double[] getKoefficients(SetPointsContainer pointsContainer, int numOfMembers){
        ArrayList<Double> koefficients = new ArrayList<>();
        double[][] matrix = getMatrix(pointsContainer,range);
        double[] solve = solveSystem(matrix,0.0001);

        return solve;
    }

    private static double[][] getMatrix(SetPointsContainer pointsContainer, int numOfMembers){
        double[][] matrix = new double[numOfMembers][numOfMembers + 1];
        //init
        for (int i = 0; i < numOfMembers; i++)
        {
            for (int j = 0; j < numOfMembers; j++)
            {
                matrix[i][j] = 0;
            }
        }

        for (int i = 0; i < numOfMembers; i++)
        {
            for (int j = 0; j < numOfMembers; j++)
            {
                double sumA = 0, sumB = 0;
                for (int k = 0; k < pointsContainer.getSet().size(); k++)
                {
                    sumA += Math.pow(pointsContainer.getSet().get(k).getX(), i) * Math.pow(pointsContainer.getSet().get(k).getX(), j);
                    sumB += pointsContainer.getSet().get(k).getY() * Math.pow(pointsContainer.getSet().get(k).getX(), i);
                }
                matrix[i][j] = sumA;
                matrix[i][numOfMembers] = sumB;
            }
        }
        return matrix;
    }

    private static double[] solveSystem(double[][] matrix, double eps){
        // Введем вектор значений неизвестных на предыдущей итерации,
        // размер которого равен числу строк в матрице, т.е. size,
        // причем согласно методу изначально заполняем его нулями
        int size = matrix.length;
        double[] previousVariableValues = new double[size];

        for (int i = 0; i < size; i++) {
            previousVariableValues[i] = 0.0;
        }

        // Будем выполнять итерационный процесс до тех пор,
        // пока не будет достигнута необходимая точность
        while (true) {
            // Введем вектор значений неизвестных на текущем шаге
            double[] currentVariableValues = new double[size];

            // Посчитаем значения неизвестных на текущей итерации
            // в соответствии с теоретическими формулами
            for (int i = 0; i < size; i++) {
                // Инициализируем i-ую неизвестную значением
                // свободного члена i-ой строки матрицы
                currentVariableValues[i] = matrix[i][size];

                // Вычитаем сумму по всем отличным от i-ой неизвестным
                for (int j = 0; j < size; j++) {
                    // При j < i можем использовать уже посчитанные
                    // на этой итерации значения неизвестных
                    if (j < i) {
                        currentVariableValues[i] -= matrix[i][j] * currentVariableValues[j];
                    }

                    // При j > i используем значения с прошлой итерации
                    if (j > i) {
                        currentVariableValues[i] -= matrix[i][j] * previousVariableValues[j];
                    }
                }

                // Делим на коэффициент при i-ой неизвестной
                currentVariableValues[i] /= matrix[i][i];
            }

            // Посчитаем текущую погрешность относительно предыдущей итерации
            double error = 0.0;

            for (int i = 0; i < size; i++) {
                error += Math.abs(currentVariableValues[i] - previousVariableValues[i]);
            }

            // Если необходимая точность достигнута, то завершаем процесс
            if (error < eps) {
                break;
            }

            // Переходим к следующей итерации, так
            // что текущие значения неизвестных
            // становятся значениями на предыдущей итерации
            previousVariableValues = currentVariableValues;
        }
        return previousVariableValues;
    }
}
