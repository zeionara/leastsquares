package Calculator;

/**
 * Created by Zerbs on 21.11.2016.
 */
public class Calculator {
    public static String Calculate(String expression){
        expression = expression.replaceAll(" ","");
        expression = replaceSingleOperator(expression, new Sin());
        expression = replaceSingleOperator(expression, new Cos());
        expression = replaceSingleOperator(expression, new Log());
        expression = replaceSingleOperator(expression, new Tan());

        //System.out.println("after single operators : "+expression);

        expression = replaceBinaryOperator(expression, new Pow());
        expression = replaceBinaryOperator(expression, new Mul());
        expression = replaceBinaryOperator(expression, new Div());
        expression = replaceBinaryOperator(expression, new Add());
        expression = replaceBinaryOperator(expression, new Sub());

        //System.out.println("after binary operators : "+expression);

        return expression;
    }

    private static String replaceSin(String expression){
        int position = expression.indexOf("sin(");
        String argument = "";
        while (position >= 0) {
            argument = expression.substring(position + 4, expression.indexOf(")", position));
            expression = expression.replace("sin(" + argument + ")", Math.sin(Double.parseDouble(argument))+"");
            position = expression.indexOf("sin(");
        }
        return expression;
    }

    private static String replaceCos(String expression){
        int position = expression.indexOf("cos(");
        String argument = "";
        while (position >= 0) {
            argument = expression.substring(position + 4, expression.indexOf(")", position));
            expression = expression.replace("cos(" + argument + ")", Math.cos(Double.parseDouble(argument))+"");
            position = expression.indexOf("cos(");
        }
        return expression;
    }

    private static String replaceTg(String expression){
        int position = expression.indexOf("tg(");
        String argument = "";
        while (position >= 0) {
            argument = expression.substring(position + 3, expression.indexOf(")", position));
            expression = expression.replace("tg(" + argument + ")", Math.sin(Double.parseDouble(argument))/Math.cos(Double.parseDouble(argument))+"");
            position = expression.indexOf("tg(");
        }
        return expression;
    }

    private static String replaceLn(String expression){
        int position = expression.indexOf("ln(");
        String argument = "";
        while (position >= 0) {
            argument = expression.substring(position + 3, expression.indexOf(")", position));
            expression = expression.replace("ln(" + argument + ")", Math.log(Double.parseDouble(argument))+"");
            position = expression.indexOf("ln(");
        }
        return expression;
    }

    private static String replaceDiv(String expression){
        int position = expression.indexOf("/");
        int pos1 = 0;
        int pos2 = 0;
        int begin = 0;
        int end = 0;
        String argument1 = "";
        String argument2 = "";
        while (position >= 0) {
            pos1 = position - 1;
            if (expression.charAt(pos1) == ')'){
                end = pos1;
                while (expression.charAt(pos1) != ')'){
                    pos1 -= 1;
                }
                begin = pos1;
                expression = replaceBraces(begin, end, expression);
            }
            position = expression.indexOf("/");
            pos1 = position - 1;
            while ((isNumericChar(expression, pos1)) && (pos1 >= 0)){
                pos1--;
            }
            argument1 = expression.substring(pos1 + 1, position);
            pos2 = position + 1;
            if (expression.charAt(pos2) == '('){
                begin = pos2;
                while (expression.charAt(pos2) != ')'){
                    pos2 += 1;
                }
                end = pos2 + 1;
                expression = replaceBraces(begin, end, expression);
            }
            pos2 = position + 1;
            while ((isNumericChar(expression, pos2)) && (pos2 < expression.length())){
                pos2++;
            }
            argument2 = expression.substring(position+1, pos2);
            //System.out.println(argument1+"/"+argument2);
            expression = expression.replace(argument1+"/"+argument2,Double.parseDouble(argument1)/Double.parseDouble(argument2)+"");
            position = expression.indexOf("/");
        }
        return expression;
    }

    private static String replaceAdd(String expression){
        int position = expression.indexOf("+");
        int pos1 = 0;
        int pos2 = 0;
        String argument1 = "";
        String argument2 = "";
        while (position >= 0) {
            pos1 = position - 1;
            while ((isNumericChar(expression, pos1)) && (pos1 >= 0)){
                pos1--;
            }
            argument1 = expression.substring(pos1 + 1, position);
            pos2 = position + 1;
            while ((isNumericChar(expression, pos2)) && (pos2 < expression.length())){
                pos2++;
            }
            argument2 = expression.substring(position+1, pos2);
            //System.out.println(argument1+"+"+argument2);
            expression = expression.replace(argument1+"+"+argument2,Double.parseDouble(argument1)+Double.parseDouble(argument2)+"");
            //System.out.println(" expression : "+expression);
            position = expression.indexOf("+");
        }
        return expression;
    }

    private static String replaceBraces(int begin, int end, String expression){
        //System.out.println("braces : "+expression.substring(begin,end));
        String replaced = Calculate(expression.substring(begin+1,end-1));

        expression = expression.replace(expression.substring(begin,end),replaced);
        return expression;
    }

    private static boolean isNumericChar(String expression, int index){
        if (index < 0){
            return false;
        }
        if (index >= expression.length()){
            return false;
        }
        if ((expression.charAt(index) != '0') && (expression.charAt(index) != '1') && (expression.charAt(index) != '2') &&
                (expression.charAt(index) != '3') && (expression.charAt(index) != '4') && (expression.charAt(index) != '5')
                && (expression.charAt(index) != '6') && (expression.charAt(index) != '7') && (expression.charAt(index) != '8')
                && (expression.charAt(index) != '9') && (expression.charAt(index) != '.') && (expression.charAt(index) != 'E')){

            if ((expression.charAt(index) == '-') && (((index - 1 >= 0) && (!isDigitOrBrace(expression.charAt(index-1)))) || (index -1 < 0))){
                return true;
            }

            if (((expression.charAt(index) == '-') || (expression.charAt(index) == '+'))  && (index - 1 >= 0) && (expression.charAt(index-1) == 'E')){
                return true;
            }
            return false;
        }
        return true;
    }

    private static boolean isDigitOrBrace(char c){
        if ((c != '0') && (c != '1') && (c != '2') &&
                (c != '3') && (c != '4') && (c != '5')
                && (c != '6') && (c != '7') && (c != '8')
                && (c != '9') && (c != ')')){
            return false;
        }
        return true;
    }

    private static String replaceBinaryOperator(String expression, BinaryOperator operator){
        //System.out.println("pre-indexof");
        int position = indexOf(expression,operator.toString());
        //System.out.println("post-indexof");
        //= expression.indexOf(operator.toString());
        int pos1 = 0;
        int pos2 = 0;
        int begin = 0;
        int end = 0;
        String argument1 = "";
        String argument2 = "";
        while (position > 0) {
            //System.out.println("Definded");
            pos1 = position - 1;
            if (expression.charAt(pos1) == ')'){
                end = pos1 + 1;
                while (expression.charAt(pos1) != '('){
                    pos1 -= 1;
                }
                begin = pos1;
                expression = replaceBraces(begin, end, expression);
            }

            position = expression.indexOf(operator.toString());
            pos1 = position - 1;
            while ((isNumericChar(expression, pos1)) && (pos1 >= 0)){
                pos1--;
            }
            argument1 = expression.substring(pos1 + 1, position);
            //System.out.println("argument1 = "+argument1);

            pos2 = position + 1;
            if (expression.charAt(pos2) == '('){
                begin = pos2;
                while (expression.charAt(pos2) != ')'){
                    pos2 += 1;
                }
                end = pos2 + 1;
                expression = replaceBraces(begin, end, expression);
            }
            pos2 = position + 1;
            while ((isNumericChar(expression, pos2)) && (pos2 < expression.length())){
                pos2++;
            }
            argument2 = expression.substring(position+1, pos2);

            //System.out.println("argument2 = "+argument2);

            //System.out.println(argument1+operator.toString()+argument2);
            expression = expression.replace(argument1+operator+argument2,operator.getValue(Double.parseDouble(argument1),Double.parseDouble(argument2))+"");
            //position = expression.indexOf(operator.toString());
            position = indexOf(expression,operator.toString());
        }
        return expression;
    }

    private static int indexOf(String expression, String substring){
        int pos = expression.indexOf(substring);
        if (pos <= 0){
            return pos;
        }
        while (expression.charAt(pos-1) == 'E'){
            //System.out.println(expression);
            pos = expression.indexOf(substring,pos+1);
            //System.out.println(pos);
            if (pos <= 0){
                return pos;
            }
        }
        return pos;
    }

    private static String replaceSingleOperator(String expression, SingleOperator operator){
        int position = expression.indexOf(operator+"(");
        int brace = 0;
        String argument = "";
        while (position >= 0) {
            int i = position + operator.toString().length() + 1;

            while (brace >= 0){
                if (expression.charAt(i) == '('){
                    brace++;

                }
                if (expression.charAt(i) == ')'){
                    brace--;

                }
                i++;
            }


            argument = expression.substring(position + operator.toString().length() + 1, i-1);
            //System.out.println("argument = "+argument);
            expression = expression.replace(operator+"(" + argument + ")", operator.getValue(Double.parseDouble(Calculate(argument)))+"");
            position = expression.indexOf(operator+"(");
        }
        //System.out.println("result : "+expression);
        return expression;

    }
}
