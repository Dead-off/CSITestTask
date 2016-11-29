import java.util.Collection;

public interface PriceMerger {

    Collection<Price> merge(Collection<Price> currentPrices, Collection<Price> newPrices);

}
