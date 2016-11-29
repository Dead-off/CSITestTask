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
    public void testInsertEmptyTables() {
        HashSet<Price> currentPrices = new HashSet<>();
        HashSet<Price> newPrices = new HashSet<>();
        HashSet<Price> emptySet = new HashSet<>();

        Price box1_1 = new Price(CODE_BOX, 1, 1,
                getDate(DEFAULT_YEAR, Calendar.DECEMBER, 1), getDate(DEFAULT_YEAR + 1, Calendar.JANUARY, 5), 10);
        Price box1_1_otherDate = new Price(CODE_BOX, 1, 1,
                getDate(DEFAULT_YEAR + 1, Calendar.JANUARY, 10), getDate(DEFAULT_YEAR + 1, Calendar.JANUARY, 15), 50);
        Price milk1_1 = new Price(CODE_MILK, 1, 1,
                getDate(DEFAULT_YEAR + 1, Calendar.JANUARY, 10), getDate(DEFAULT_YEAR + 1, Calendar.JANUARY, 15), 50);

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

        Collection<Price> actual = merger.merge(emptySet, newPrices);
        assertEquals(expected, actual);

        actual = merger.merge(currentPrices, emptySet);
        assertEquals(expected, actual);
    }

    @Test
    public void testInsertWithoutIntersection() {
        HashSet<Price> currentPrices = new HashSet<>();
        HashSet<Price> newPrices = new HashSet<>();

        Price box1_1 = new Price(CODE_BOX, 1, 1, getDefaultDate(1), getDefaultDate(5), 1);
        Price box2_1 = new Price(CODE_BOX, 1, 2, getDefaultDate(1), getDefaultDate(5), 1);
        Price box3_1 = new Price(CODE_BOX, 1, 3, getDefaultDate(1), getDefaultDate(5), 100);
        Price milk1_1 = new Price(CODE_MILK, 1, 1, getDefaultDate(1), getDefaultDate(5), 1);
        Price box1_1_otherDate = new Price(CODE_BOX, 1, 1, getDefaultDate(10), getDefaultDate(15), 10);

        currentPrices.add(box1_1);
        currentPrices.add(box2_1);

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

    @Test
    public void testInsertInsidePrice() {
        HashSet<Price> currentPrices = new HashSet<>();
        HashSet<Price> newPrices = new HashSet<>();

        Price box1_1 = new Price(CODE_BOX, 1, 1, getDefaultDate(1), getDefaultDate(30), 1);
        Price box1_1_from1_to9 = new Price(CODE_BOX, 1, 1, getDefaultDate(1), getDefaultDate(9), 1);
        Price box1_1_from21_to30 = new Price(CODE_BOX, 1, 1, getDefaultDate(21), getDefaultDate(30), 1);
        Price box1_1_from10_to20 = new Price(CODE_BOX, 1, 1, getDefaultDate(10), getDefaultDate(20), 500);
        Price box1_2 = new Price(CODE_BOX, 2, 1, getDefaultDate(1), getDefaultDate(30), 20);

        currentPrices.add(box1_1);
        currentPrices.add(box1_2);
        newPrices.add(box1_1_from10_to20);

        HashSet<Price> expected = new HashSet<>();
        expected.add(box1_2);
        expected.add(box1_1_from10_to20);
        expected.add(box1_1_from21_to30);
        expected.add(box1_1_from1_to9);

        Collection<Price> actual = merger.merge(currentPrices, newPrices);
        assertEquals(expected, actual);
    }

    @Test
    public void testInsertOutsidePrice() {
        HashSet<Price> currentPrices = new HashSet<>();
        HashSet<Price> newPrices = new HashSet<>();

        Price box1_1_from10_to20 = new Price(CODE_BOX, 1, 1, getDefaultDate(10), getDefaultDate(20), 10);
        Price box1_1_from5_to25 = new Price(CODE_BOX, 1, 1, getDefaultDate(5), getDefaultDate(25), 100);
        Price box1_2 = new Price(CODE_BOX, 2, 1, getDefaultDate(1), getDefaultDate(30), 20);

        currentPrices.add(box1_1_from10_to20);
        currentPrices.add(box1_2);
        newPrices.add(box1_1_from5_to25);

        HashSet<Price> expected = new HashSet<>();
        expected.add(box1_2);
        expected.add(box1_1_from5_to25);

        Collection<Price> actual = merger.merge(currentPrices, newPrices);
        assertEquals(expected, actual);
    }

    @Test
    public void testInsertWithIntersection() {
        HashSet<Price> currentPrices = new HashSet<>();
        HashSet<Price> newPrices = new HashSet<>();
        HashSet<Price> expected = new HashSet<>();

        Price box1_1_from10_to15 = new Price(CODE_BOX, 1, 1, getDefaultDate(10), getDefaultDate(15), 10);
        Price box1_1_from5_to11 = new Price(CODE_BOX, 1, 1, getDefaultDate(5), getDefaultDate(11), 10);
        Price box1_1_from12_to20 = new Price(CODE_BOX, 1, 1, getDefaultDate(12), getDefaultDate(20), 100);

        currentPrices.add(box1_1_from10_to15);
        newPrices.add(box1_1_from5_to11);
        newPrices.add(box1_1_from12_to20);

        expected.add(box1_1_from5_to11);
        expected.add(box1_1_from12_to20);

        Collection<Price> actual = merger.merge(currentPrices, newPrices);
        assertEquals(expected, actual);

        currentPrices.add(new Price(CODE_BOX, 1, 1, getDefaultDate(18), getDefaultDate(25), 10));
        currentPrices.add(new Price(CODE_BOX, 2, 1, getDefaultDate(18), getDefaultDate(25), 10));
        newPrices.add(new Price(CODE_BOX, 2, 1, getDefaultDate(10), getDefaultDate(20), 10));
        newPrices.add(new Price(CODE_BOX, 2, 1, getDefaultDate(24), getDefaultDate(30), 10));
        expected.add(new Price(CODE_BOX, 1, 1, getDefaultDate(21), getDefaultDate(25), 10));
        expected.add(new Price(CODE_BOX, 2, 1, getDefaultDate(18), getDefaultDate(30), 10));

        actual = merger.merge(currentPrices, newPrices);
        assertEquals(expected, actual);

        currentPrices.add(new Price(CODE_MILK, 1, 1, getDefaultDate(18), getDefaultDate(25), 10));
        currentPrices.add(new Price(CODE_MILK, 2, 1, getDefaultDate(18), getDefaultDate(25), 10));
        newPrices.add(new Price(CODE_MILK, 1, 1, getDefaultDate(24), getDefaultDate(30), 10));
        newPrices.add(new Price(CODE_MILK, 2, 1, getDefaultDate(24), getDefaultDate(30), 100));

        expected.add(new Price(CODE_MILK, 1, 1, getDefaultDate(18), getDefaultDate(30), 10));
        expected.add(new Price(CODE_MILK, 2, 1, getDefaultDate(18), getDefaultDate(23), 10));
        expected.add(new Price(CODE_MILK, 2, 1, getDefaultDate(24), getDefaultDate(30), 100));

        actual = merger.merge(currentPrices, newPrices);
        assertEquals(expected, actual);

    }

    private Date getDefaultDate(int day) {
        return getDate(DEFAULT_YEAR, DEFAULT_MONTH, day);
    }

    private Date getDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTime();
    }

}
