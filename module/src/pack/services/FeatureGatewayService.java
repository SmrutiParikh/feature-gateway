package pack.services;

import pack.models.Condition;
import pack.models.OperatorUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class FeatureGatewayService {
    public static boolean isAllowed(String conditionalExpression, String featureName, Map<String, Object> user){
        if(null == user || user.size() == 0){
            return false;
        }
        if(null == conditionalExpression || conditionalExpression.equals("")){
            return false;
        }

        Stack<Condition> conditionStack = new Stack<>();

        parseConditionalExpression(conditionalExpression.trim(), conditionStack);
        return evaluate(user, conditionStack);
    }

    private static boolean evaluate(Map<String, Object> user, Stack<Condition> conditionStack) {
        Map<String, Boolean> expressionToValue = new HashMap<>();
        boolean evaluatedValue = false;
        while( !conditionStack.isEmpty() ){
            Condition condition = conditionStack.pop();
            String operand1 = condition.getOperand1();
            String operand2 = condition.getOperand2();

            if(expressionToValue.containsKey(operand1) && null != expressionToValue.get(operand1)){
                operand1 = expressionToValue.get(operand1).toString();
            }
            else if(user.containsKey(operand1)) {
                operand1 = String.valueOf(user.get(operand1));
            }

            if(expressionToValue.containsKey(operand2) && null != expressionToValue.get(operand2)){
                operand2 = String.valueOf(expressionToValue.get(operand2));
            }

            boolean value = OperatorUtils.findOperatorAndEvaluate(condition.getOperator(), operand1, operand2);
            expressionToValue.put(condition.getExpression(), value);
            if(conditionStack.isEmpty()){
                evaluatedValue = value;
            }
        }
        System.out.println("map : " + expressionToValue.toString());
        return evaluatedValue;
    }

    private static void parseConditionalExpression(String conditionalExpression, Stack<Condition> conditionStack) {
        if( null == conditionalExpression || conditionalExpression.split(" ").length < 2){
            return;
        }
        String operand1, operand2, operator;
        if(conditionalExpression.contains("(")) {
            int indexOp1OpenBracket = conditionalExpression.indexOf("(");
            int indexOp1CloseBracket = getCloseBracketIndex(conditionalExpression, indexOp1OpenBracket, '(', ')');
            int indexOp2OpenBracket = conditionalExpression.indexOf("(", indexOp1CloseBracket);
            int indexOp2CloseBracket = getCloseBracketIndex(conditionalExpression, indexOp2OpenBracket, '(', ')');

            if( indexOp1OpenBracket != -1 && indexOp1CloseBracket != -1 && indexOp2OpenBracket != -1 && indexOp2CloseBracket != -1
            && indexOp1OpenBracket < indexOp1CloseBracket && indexOp2OpenBracket < indexOp2CloseBracket ){
                operand1 = conditionalExpression.substring(indexOp1OpenBracket+1, indexOp1CloseBracket ).trim();
                operand2 = conditionalExpression.substring(indexOp2OpenBracket+1, indexOp2CloseBracket ).trim();
                operator = conditionalExpression.substring(indexOp1CloseBracket+1, indexOp2OpenBracket).trim();
            }
            else {
                return;
            }
        }
        else{
            operator = OperatorUtils.findOperatorsString(conditionalExpression);
            if(operator == null ){
                return;
            }
            int indexOfOperator = conditionalExpression.indexOf(operator);
            operand1 = conditionalExpression.substring(0, indexOfOperator).trim();
            operand2 = conditionalExpression.substring(indexOfOperator + operator.length()).trim();
        }
        Condition condition = new Condition(operator, conditionalExpression, operand1, operand2 );
        conditionStack.push(condition);

        parseConditionalExpression(operand1, conditionStack);
        parseConditionalExpression(operand2, conditionStack);
    }

    private static int getCloseBracketIndex(String conditionalExpression, int startIndex, char startChar, char endChar) {
        int endIndex = -1;
        int bracketsCount = 0;
        for(int i = startIndex; i < conditionalExpression.length(); i++){
            char c = conditionalExpression.charAt(i);
            if( startChar == c ){
                bracketsCount++;
            }
            else if( endChar == c ){
                bracketsCount--;
            }
            if(bracketsCount == 0){
                endIndex = i;
                break;
            }
        }
        return endIndex;
    }
}
