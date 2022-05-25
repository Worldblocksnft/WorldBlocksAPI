package world.worldblocks.utilities;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class WeightedChooser<T> extends ConcurrentHashMap<Pair<Double, Double>, T> {
    private static final long serialVersionUID = 1L;

    private double upperBound = 0.0D;

    public double getUpperBound() {
        return this.upperBound;
    }

    public void insertElement(T e, double probability) {
        double upperBound = this.getUpperBound();
        Set<Pair<Double, Double>> set = new HashSet<>();
        set.add(new Pair<>(upperBound, upperBound + probability));
        this.put(set.iterator().next(), e);
        this.upperBound = upperBound + probability;
    }

    public T getRandomElement() {
        if (this.values().size() == 1) {
            return this.values().iterator().next();
        }
        double index = ThreadLocalRandom.current().nextDouble(0.0D, this.upperBound);
        for (Pair<Double, Double> range : this.keySet()) {
            if (range.getKey() < index && range.getValue() > index) {
                return this.get(range);
            }
        }
        return null;
    }

    public double getChance(T e) {
        if (!this.containsValue(e)) {
            return 0;
        }
        
        for (Pair<Double, Double> set : this.keySet()) {
            if (!this.get(set).equals(e)) {
                continue;
            }
            return set.getValue() - set.getKey();
        }
        return 0;
    }

    public void removeElement(T e) {
        for (Pair<Double, Double> set : this.keySet()) {
            if (!this.get(set).equals(e)) {
                continue;
            }
            this.remove(set);
        }
    }

}
