package pack.models;

import java.util.Map;

public class TestModel {
    private String featureName;
    private String conditionalExpression;
    private Map<String,Object> user;
    private boolean idealAnswer;

    public TestModel(String featureName, String conditionalExpression, Map<String, Object> user, boolean idealAnswer) {
        this.featureName = featureName;
        this.conditionalExpression = conditionalExpression;
        this.user = user;
        this.idealAnswer = idealAnswer;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public String getConditionalExpression() {
        return conditionalExpression;
    }

    public void setConditionalExpression(String conditionalExpression) {
        this.conditionalExpression = conditionalExpression;
    }

    public Map<String, Object> getUser() {
        return user;
    }

    public void setUser(Map<String, Object> user) {
        this.user = user;
    }

    public boolean isIdealAnswer() {
        return idealAnswer;
    }

    public void setIdealAnswer(boolean idealAnswer) {
        this.idealAnswer = idealAnswer;
    }
}
