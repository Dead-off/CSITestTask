import java.util.*;
import java.util.stream.Collectors;

public class PriceMergerImpl implements PriceMerger {

    public static final PriceMergerImpl INSTANCE = new PriceMergerImpl();

    private PriceMergerImpl() {

    }

    @Override
    public Set<Price> merge(Set<Price> currentPrices, Set<Price> newPrices) {
        Set<Price> result = new HashSet<>();
        Map<String, List<Price>> currentPricesMap = groupPricesByPeriod(currentPrices);
        Map<String, List<Price>> newPricesMap = groupPricesByPeriod(newPrices);
        for (Map.Entry<String, List<Price>> e : currentPricesMap.entrySet()) {
            List<Price> newPricesByKey = newPricesMap.get(e.getKey());
            if (newPricesByKey == null) {
                result.addAll(e.getValue());
                continue;
            }
            result.addAll(getMergedPriceList(e.getValue(), newPricesByKey));
            newPricesMap.remove(e.getKey());

        }
        result.addAll(newPricesMap.values().stream().reduce(
                (l1, l2) -> {
                    l1.addAll(l2);
                    return l1;
                }
        ).orElse(new ArrayList<>()));
        return result;
    }

    private List<Price> getMergedPriceList(List<Price> currentPrices, List<Price> newPrices) {
        List<Price> resultUnmerged = new ArrayList<>();
        List<Date> dates = new ArrayList<>();
        dates.addAll(currentPrices.stream().map(Price::getBegin).collect(Collectors.toList()));
        dates.addAll(currentPrices.stream().map(Price::getEnd).collect(Collectors.toList()));
        dates.addAll(newPrices.stream().map(Price::getBegin).collect(Collectors.toList()));
        dates.addAll(newPrices.stream().map(Price::getEnd).collect(Collectors.toList()));
        List<Date> datesUnique = dates.stream().unordered().distinct().collect(Collectors.toList());
        datesUnique.sort(Date::compareTo);
        for (int i = 1; i < datesUnique.size(); i++) {
            Date begin = datesUnique.get(i - 1);
            Date end = datesUnique.get(i);
            Price currentPrice = findPriceForPeriod(begin, end, currentPrices);
            Price newPrice = findPriceForPeriod(begin, end, newPrices);
            Price finalPrice = getFinalPrice(begin, end, currentPrice, newPrice);
            if (finalPrice != null) {
                resultUnmerged.add(finalPrice);
            }
        }
        return joinSamePrices(resultUnmerged);
    }

    private List<Price> joinSamePrices(List<Price> prices) {
        List<Price> result = new ArrayList<>();
        if (prices.size() == 0) {
            return result;
        }
        Date begin = prices.get(0).getBegin();
        Price lastPrice = prices.get(0);
        for (Price price : prices) {
            if (price.getValue() != lastPrice.getValue() ||
                    (!price.getBegin().equals(lastPrice.getEnd()) && !price.equals(lastPrice))) {
                result.add(new Price(lastPrice.getProductCode(), lastPrice.getNumber(), lastPrice.getDepart(),
                        begin, lastPrice.getEnd(), lastPrice.getValue()));
                begin = price.getBegin();
            }
            lastPrice = price;
        }
        result.add(new Price(lastPrice.getProductCode(), lastPrice.getNumber(), lastPrice.getDepart(),
                begin, lastPrice.getEnd(), lastPrice.getValue()));
        return result;
    }

    private Price getFinalPrice(Date begin, Date end, Price currentPrice, Price newPrice) {
        if (currentPrice == null && newPrice == null) {
            return null;
        }
        if (newPrice == null) {
            return new Price(currentPrice.getProductCode(), currentPrice.getNumber(), currentPrice.getDepart(),
                    begin, end, currentPrice.getValue());
        }
        return new Price(newPrice.getProductCode(), newPrice.getNumber(), newPrice.getDepart(),
                begin, end, newPrice.getValue());
    }

    private Price findPriceForPeriod(Date beginDate, Date endDate, List<Price> prices) {
        return prices.stream().filter(price ->
                price.getBegin().compareTo(beginDate) <= 0 && price.getEnd().compareTo(endDate) >= 0
        ).findAny().orElse(null);
    }

    private Map<String, List<Price>> groupPricesByPeriod(Collection<Price> prices) {
        final Map<String, List<Price>> result = new HashMap<>();
        prices.forEach(
                price -> {
                    String key = price.getUniqueKey();
                    result.putIfAbsent(key, new ArrayList<>());
                    result.get(key).add(price);
                }
        );
        return result;
    }

}
