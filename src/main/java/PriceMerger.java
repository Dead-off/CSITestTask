import java.util.Set;

public interface PriceMerger {

    Set<Price> merge(Set<Price> currentPrices, Set<Price> newPrices);

}
