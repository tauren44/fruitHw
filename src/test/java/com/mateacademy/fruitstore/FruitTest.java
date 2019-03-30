package com.mateacademy.fruitstore;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static junit.framework.TestCase.assertTrue;

public class FruitTest {
    private Fruit apple;

    @Before
    public void init() {
        apple = new Fruit(FruitType.APPLE, 10, 5, "29/03/19");
    }

    @Test
    public void convertStringToDate() {
        LocalDate expected = LocalDate.of(2019, 3 ,29);
        LocalDate actual = FruitUtil.convertStringToDate(apple.getDateOfDelivery());
        assertTrue(actual.isEqual(expected));
    }

    @Test
    public void getDateOfSpoil() {
        LocalDate expected = LocalDate.of(2019, 4, 3);
        LocalDate actual = apple.getDateOfSpoil();
        assertTrue(actual.isEqual(expected));
    }
}
