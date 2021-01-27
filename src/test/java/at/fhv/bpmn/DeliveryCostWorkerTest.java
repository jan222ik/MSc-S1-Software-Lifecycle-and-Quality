package at.fhv.bpmn;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryCostWorkerTest {
    private final DeliveryCostWorker dcw = new DeliveryCostWorker();

    @ParameterizedTest
    @EnumSource(value = CostCategories.class)
    void calculateCostSmall(CostCategories costCategories) {
        assertEquals(costCategories.cost, dcw.calculateCost(costCategories.amount));
    }

    private enum CostCategories {
        NONE(2, 0.0),
        IS5_50(6, 5.5),
        IS9_9(13, 9.9),
        IS20(25, 20.0),
        IS35(35, 35.0),
        ISSTILL35(Integer.MAX_VALUE, 35.0);


        public final int amount;
        public final double cost;

        CostCategories(int amount, double cost) {
            this.amount = amount;
            this.cost = cost;
        }


    }


}