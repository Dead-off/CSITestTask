import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

public class PriceMergerTest extends Assert {

    private static final String CODE_BOX = "box";
    private static final String CODE_MILK = "milk";
    private final int DEFAULT_YEAR = 2016;
    private final int DEFAULT_MONTH = Calendar.APRIL;

    private PriceMerger merger;

    @Before
    public void init() {
        merger = PriceMergerImpl.INSTANCE;
    }

    @Test
    public void mergeTest() {
        testInsertEmptyTables();
        testInsertWithoutIntersection();
        testInsertWithIntersection();
        testInsertInsidePrice();
    }

    private void testInsertEmptyTables() {
        Price.PriceBuilder priceBuilder = new Price.PriceBuilder();
        HashSet<Price> currentPrices = new HashSet<>();
        HashSet<Price> newPrices = new HashSet<>();

        priceBuilder
                .setProductCode(CODE_BOX)
                .setDepart(1)
                .setNumber(1)
                .setBegin(getDate(DEFAULT_YEAR, Calendar.DECEMBER, 1))
                .setEnd(getDate(DEFAULT_YEAR + 1, Calendar.JANUARY, 5));

        Price box1_1 = priceBuilder.build();
        priceBuilder
                .setBegin(getDate(DEFAULT_YEAR + 1, Calendar.JANUARY, 10))
                .setEnd(getDate(DEFAULT_YEAR + 1, Calendar.JANUARY, 15));
        Price box1_1_otherDate = priceBuilder.build();
        priceBuilder.setProductCode(CODE_MILK);
        Price milk1_1 = priceBuilder.build();
        newPrices.add(box1_1);
        newPrices.add(box1_1_otherDate);
        newPrices.add(milk1_1);

        currentPrices.add(milk1_1);
        currentPrices.add(box1_1_otherDate);
        currentPrices.add(box1_1);

        HashSet<Price> expected = new HashSet<>();
        expected.add(box1_1_otherDate);
        expected.add(box1_1);
        expected.add(milk1_1);
        Collection<Price> actual = merger.merge(new HashSet<>(), newPrices);
        assertEquals(expected, actual);

        actual = merger.merge(currentPrices, new HashSet<>());
        assertEquals(expected, actual);
    }

    private void testInsertWithoutIntersection() {
        Price.PriceBuilder priceBuilder = new Price.PriceBuilder();
        HashSet<Price> currentPrices = new HashSet<>();
        HashSet<Price> newPrices = new HashSet<>();
        priceBuilder
                .setProductCode(CODE_BOX)
                .setDepart(1)
                .setNumber(1)
                .setBegin(getDate(DEFAULT_YEAR, DEFAULT_MONTH, 1))
                .setEnd(getDate(DEFAULT_YEAR, DEFAULT_MONTH, 5));

        Price box1_1 = priceBuilder.build();
        priceBuilder.setDepart(2);
        Price box2_1 = priceBuilder.build();
        currentPrices.add(box1_1);
        currentPrices.add(box2_1);

        priceBuilder.setDepart(3);
        Price box3_1 = priceBuilder.build();
        priceBuilder.setDepart(1);
        priceBuilder.setProductCode(CODE_MILK);
        Price milk1_1 = priceBuilder.build();
        priceBuilder.setProductCode(CODE_BOX);
        priceBuilder.setBegin(getDate(DEFAULT_YEAR, DEFAULT_MONTH, 10));
        priceBuilder.setEnd(getDate(DEFAULT_YEAR, DEFAULT_MONTH, 15));
        Price box1_1_otherDate = priceBuilder.build();
        newPrices.add(box3_1);
        newPrices.add(milk1_1);
        newPrices.add(box1_1_otherDate);

        HashSet<Price> expected = new HashSet<>();
        expected.add(box1_1);
        expected.add(box2_1);
        expected.add(box3_1);
        expected.add(milk1_1);
        expected.add(box1_1_otherDate);

        Collection<Price> actual = merger.merge(currentPrices, newPrices);
        assertEquals(expected, actual);
    }

    private void testInsertInsidePrice() {
        Price.PriceBuilder priceBuilder = new Price.PriceBuilder();
        HashSet<Price> currentPrices = new HashSet<>();
        HashSet<Price> newPrices = new HashSet<>();
        priceBuilder
                .setProductCode(CODE_BOX)
                .setDepart(1)
                .setNumber(1)
                .setBegin(getDate(DEFAULT_YEAR, DEFAULT_MONTH, 1))
                .setEnd(getDate(DEFAULT_YEAR, DEFAULT_MONTH, 30));
        Price box1_1 = priceBuilder.build();
        priceBuilder.setNumber(2);
        Price box1_2 = priceBuilder.build();
        priceBuilder
                .setNumber(1)
                .setBegin(getDate(DEFAULT_YEAR, DEFAULT_MONTH, 10))
                .setEnd(getDate(DEFAULT_YEAR, DEFAULT_MONTH, 20));
        Price box1_1_from10_to20 = priceBuilder.build();
        currentPrices.add(box1_1);
        currentPrices.add(box1_2);
        newPrices.add(box1_1_from10_to20);

        HashSet<Price> expected = new HashSet<>();
        priceBuilder
                .setBegin(getDate(DEFAULT_YEAR, DEFAULT_MONTH, 1))
                .setEnd(getDate(DEFAULT_YEAR, DEFAULT_MONTH, 9));
        Price box1_1_from1_to9 = priceBuilder.build();
        priceBuilder
                .setBegin(getDate(DEFAULT_YEAR, DEFAULT_MONTH, 21))
                .setEnd(getDate(DEFAULT_YEAR, DEFAULT_MONTH, 30));
        Price box1_1_from21_to30 = priceBuilder.build();
        expected.add(box1_2);
        expected.add(box1_1_from10_to20);
        expected.add(box1_1_from21_to30);
        expected.add(box1_1_from1_to9);

        Collection<Price> actual = merger.merge(currentPrices, newPrices);
        assertEquals(expected, actual);
    }

    private void testInsertWithIntersection() {

    }

    private Date getDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTime();
    }

}
