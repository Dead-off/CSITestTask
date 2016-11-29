import java.util.ArrayList;
import java.util.Collection;

public class PriceMergerImpl implements PriceMerger {

    public static final PriceMergerImpl INSTANCE = new PriceMergerImpl();

    private PriceMergerImpl() {

    }


    @Override
    public Collection<Price> merge(Collection<Price> currentPrices, Collection<Price> newPrices) {
        ArrayList<Price> result = new ArrayList<>();

        return null;
    }
}
