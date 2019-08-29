package pack.models;

import java.util.*;

public enum OperatorUtils {

    AND("AND", "&&"),
    OR("OR", "||"),
    NOT("NOT", "!"),
    GTE("GREATER THAN EQUAL TO", ">="),
    GT("GREATER THAN",  ">"),
    LTE("LESS THAN EQUAL TO", "<="),
    LT("LESS THAN",  "<"),
    EQ("EQUALS", "=="),
    NEQ("NOT EQUALS", "!="),
    BETWEEN("BETWEEN", null),
    NONEOF("NONEOF", null),
    ALLOF("ALLOF", null),
    ANYOF("ANYOF", null);

    private String opName;
    private String symbol;

    OperatorUtils(String opName, String symbol) {
        this.opName = opName;
        this.symbol = symbol;
    }

    private static Map<String, OperatorUtils> nameToOperator = new HashMap<>();
    private static Map<String, OperatorUtils> symbolToOperator = new HashMap<>();

    static {
        for (OperatorUtils operator : EnumSet.allOf(OperatorUtils.class)) {
            if(null != operator.opName) {
                nameToOperator.put(operator.opName, operator);
            }
            if(null != operator.symbol) {
                symbolToOperator.put(operator.symbol, operator);
            }
        }
    }

    public static boolean findOperatorAndEvaluate(String operatorString, String operand1, String operand2){
        operatorString = operatorString.toUpperCase();
        boolean isNot = false;
        if(operatorString.contains(" ")){
            String[] split = operatorString.split(" ");
            if (NOT.equals(findOperator(split[0]))) {
                isNot = true;
                operatorString = split[1];
            }
        }
        OperatorUtils operator = findOperator(operatorString);
        if(null == operator){
            return false;
        }
        return isNot != evaluateOperator(operator, operand1, operand2);
    }

    public static String findOperatorsString(String expression){
        List<String> operators = new ArrayList<>();
        String[] words = expression.split(" ");
        if(words.length == 0){
            return null;
        }

        for (int i = 0 ; i<words.length; i++) {
            String word = words[i].trim().toUpperCase();
            if (NOT.equals(findOperator(word))) {
                continue;
            }
            OperatorUtils operator = findOperator(word);
            if (operator != null) {
                if (i > 0 && NOT.equals(findOperator(words[i-1]))) {
                    operators.add(NOT.opName + " " + word);
                }
                else {
                    operators.add(word);
                }
            }
        }
        if(operators.size() == 1){
            return operators.get(0);
        }
        if(operators.contains(AND.opName)){
            return AND.opName;
        }
        if(operators.contains(OR.opName)){
            return OR.opName;
        }
        return null;
    }

    private static OperatorUtils findOperator(String operatorString){
        if(operatorString == null || operatorString.equals("")){
            return null;
        }
        if(nameToOperator.containsKey(operatorString)){
            return nameToOperator.get(operatorString);
        }
        if(symbolToOperator.containsKey(operatorString)){
            return symbolToOperator.get(operatorString);
        }
        return null;
    }

    private static boolean evaluateOperator(OperatorUtils operator, String operand1, String operand2){
        try {
            operand1 = operand1.replace("\"", "").replace("\'", "");
            operand2 = operand2.replace("\"", "").replace("\'", "");

            switch (operator) {
                case AND:
                    return evaluateAndOperator(operand1, operand2);
                case OR:
                    return evaluateOrOperator(operand1, operand2);
                case EQ:
                    return evaluateEqualsOperator(operand1, operand2);
                case GT:
                    return evaluateGTOperator(operand1, operand2, false);
                case GTE:
                    return evaluateGTOperator(operand1, operand2, true);
                case LT:
                    return evaluateLTOperator(operand1, operand2, false);
                case LTE:
                    return evaluateLTOperator(operand1, operand2, true);
                case NEQ:
                    return !evaluateEqualsOperator(operand1, operand2);
                case BETWEEN:
                    return evaluateBetweenOperator(operand1, operand2);
                case NONEOF:
                    return evaluateNoneOfOperator(operand1, operand2);
                case ALLOF:
                    return evaluateAllOfOperator(operand1, operand2);
                case ANYOF:
                    return evaluateAnyOfOperator(operand1, operand2);

                default:
                    return false;
            }
        }catch (Exception e){
            return false;
        }
    }

    private static boolean evaluateAnyOfOperator(String operand1, String operand2) {
        List<String> array = parseList(operand2);
        return array.contains(operand1.trim().toLowerCase());
    }

    private static boolean evaluateAllOfOperator(String operand1, String operand2) {
        List<String> array = parseList(operand2);
        List<String> value = parseList(operand1);
        return array.containsAll(value);
    }

    private static boolean evaluateNoneOfOperator(String operand1, String operand2) {
        List<String> array = parseList(operand2);
        List<String> value = parseList(operand1);
        for(String val : value){
         if(array.contains(val)){
             return false;
         }
        }
        return true;
    }

    private static boolean evaluateBetweenOperator(String operand1, String operand2) {
        List<String> array = parseList(operand2);
        if( array.size() > 2){
            return false;
        }
        long startRange = Long.parseLong(array.get(0));
        long endRange = Long.parseLong(array.get(1));
        long value = Long.parseLong(operand1);
        return value > startRange && value < endRange;
    }

    private static List<String> parseList(String operand){
        String[] split = operand
                .replace("[", "")
                .replace("]", "")
                .replace(" ", "")
                .replace("\"", "")
                .replace("\'", "")
                .toLowerCase()
                .split(",");
        if (split.length == 0) {
            return new ArrayList<>(0);
        }
        return new ArrayList<>(Arrays.asList(split));
    }
    private static boolean evaluateGTOperator(String operand1, String operand2, boolean isEqualCheck) {
        Long var1 = Long.parseLong(operand1);
        Long var2 = Long.parseLong(operand2);
        if(isEqualCheck){
            return Objects.equals(var1, var2);
        }
        return var1 > var2;
    }

    private static boolean evaluateLTOperator(String operand1, String operand2, boolean isEqualCheck) {
        Long var1 = Long.parseLong(operand1);
        Long var2 = Long.parseLong(operand2);
        if(isEqualCheck){
            return Objects.equals(var1, var2);
        }
        return var1 < var2;
    }

    private static boolean evaluateEqualsOperator(String operand1, String operand2) {
        return Objects.equals(operand1, operand2);
    }

    private static boolean evaluateOrOperator(String operand1, String operand2) {
        return Boolean.valueOf(operand1) || Boolean.valueOf(operand2);
    }

    private static boolean evaluateAndOperator(String operand1, String operand2) {
        return Boolean.valueOf(operand1) && Boolean.valueOf(operand2);
    }
}
