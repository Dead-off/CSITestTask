import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

public class PriceMergerTest extends Assert {

    private PriceMerger merger;

    @Before
    public void init() {
        merger = PriceMergerImpl.INSTANCE;
    }

    @Test
    public void mergeTest() {
        
    }

    public static class PriceBuilder {
        private long id;
        private String productCode;
        private int number;
        private int depart;
        private Date begin;
        private Date end;
        private long value;

        public Price build() {
            return new Price(id, productCode, number, depart, begin, end, value);
        }

        public long getId() {
            return id;
        }

        public PriceBuilder setId(long id) {
            this.id = id;
            return this;
        }

        public String getProductCode() {
            return productCode;
        }

        public PriceBuilder setProductCode(String productCode) {
            this.productCode = productCode;
            return this;
        }

        public int getNumber() {
            return number;
        }

        public PriceBuilder setNumber(int number) {
            this.number = number;
            return this;
        }

        public int getDepart() {
            return depart;
        }

        public PriceBuilder setDepart(int depart) {
            this.depart = depart;
            return this;
        }

        public Date getBegin() {
            return begin;
        }

        public PriceBuilder setBegin(Date begin) {
            this.begin = begin;
            return this;
        }

        public Date getEnd() {
            return end;
        }

        public PriceBuilder setEnd(Date end) {
            this.end = end;
            return this;
        }

        public long getValue() {
            return value;
        }

        public PriceBuilder setValue(long value) {
            this.value = value;
            return this;
        }
    }

}
