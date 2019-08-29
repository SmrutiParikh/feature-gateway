package pack;

import pack.models.TestModel;
import pack.services.FeatureGatewayService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestClass {
    public static void main(String[] args) {
        Map<String, Object> user1 = new HashMap<>();
        user1.put("age", 30);
        user1.put("name", "Rahul");
        user1.put("gender", "Male");
        user1.put("past_order_amount", 10000);
        user1.put("location", "Bengaluru");
        user1.put("points", "150");

        Map<String, Object> user2 = new HashMap<>();
        user2.put("age", 22);
        user2.put("name", "Priti");
        user2.put("gender", "Female");
        user2.put("past_order_amount", 60000);
        user2.put("location", "Bengaluru");
        user2.put("points", "300");

        List<TestModel> testModels = new ArrayList<>();
        testModels.add(new TestModel("Feature1", "(age > 25 AND gender == \"Male\") OR (past_order_amount > 10000)", user1, true));
        testModels.add(new TestModel("Same Day Delivery", "location == \"Bengaluru\" AND past_order_amount > 15000", user1, false));
        testModels.add(new TestModel("Special Discount", "location == \"Bengaluru\" AND past_order_amount > 20000 AND points >= 300", user2, true));
        testModels.add(new TestModel("Target Young Male", "age NOT BETWEEN 20,30 AND gender == 'Male' ", user1, true));
        testModels.add(new TestModel("Points Captain", "points ANYOF [140, 160, 150] AND location NONEOF [ \"GOA\", \"TIBBET\" ]", user1, true));

        for (TestModel testModel: testModels) {
            String featureName = testModel.getFeatureName();
            String expression = testModel.getConditionalExpression();
            boolean allowed = FeatureGatewayService.isAllowed(expression, featureName, testModel.getUser());

            System.out.println("TestModel : " + featureName +
                    ",\nExpression : " + expression +
                    ",\nisAllowed : " + allowed +
                    ",\nidealAnswer : " + testModel.isIdealAnswer() +
                    ",\nuser : " + testModel.getUser().get("name") + "\n\n");
        }
    }
}
