package pack.models;

public class Condition {
    private String operator;
    private String expression;
    private String operand1;
    private String operand2;

    public Condition() {
    }

    public Condition(String operator, String expression, String operand1, String operand2) {
        this.operator = operator;
        this.expression = expression;
        this.operand1 = operand1;
        this.operand2 = operand2;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getOperand1() {
        return operand1;
    }

    public void setOperand1(String operand1) {
        this.operand1 = operand1;
    }

    public String getOperand2() {
        return operand2;
    }

    public void setOperand2(String operand2) {
        this.operand2 = operand2;
    }
}
