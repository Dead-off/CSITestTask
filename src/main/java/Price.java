import java.util.Date;

public class Price {

    private final String productCode;
    private final int number;
    private final int depart;
    private final Date begin;
    private final Date end;
    private final long value;

    public Price(String productCode, int number, int depart, Date begin, Date end, long value) {
        this.productCode = productCode;
        this.number = number;
        this.depart = depart;
        this.begin = begin;
        this.end = end;
        this.value = value;
    }

    public String getUniqueKey() {
        StringBuilder stringBuilder = new StringBuilder(productCode);
        return stringBuilder.append(number).append(depart).toString();
    }

    public String getProductCode() {
        return productCode;
    }

    public int getNumber() {
        return number;
    }

    public int getDepart() {
        return depart;
    }

    public Date getBegin() {
        return begin;
    }

    public Date getEnd() {
        return end;
    }

    public long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Price price = (Price) o;

        if (number != price.number) return false;
        if (depart != price.depart) return false;
        if (value != price.value) return false;
        if (!productCode.equals(price.productCode)) return false;
        if (!begin.equals(price.begin)) return false;
        return end.equals(price.end);

    }

    @Override
    public int hashCode() {
        int result = productCode.hashCode();
        result = 31 * result + number;
        result = 31 * result + depart;
        result = 31 * result + begin.hashCode();
        result = 31 * result + end.hashCode();
        result = 31 * result + (int) (value ^ (value >>> 32));
        return result;
    }
}
