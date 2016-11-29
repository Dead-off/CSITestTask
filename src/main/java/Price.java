import java.util.Date;

public class Price {

    private final long id;
    private final String productCode;
    private final int number;
    private final int depart;
    private final Date begin;
    private final Date end;
    private final long value;

    public Price(long id, String productCode, int number, int depart, Date begin, Date end, long value) {
        this.id = id;
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

    public long getId() {
        return id;
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

}
