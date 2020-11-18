package se.anders_raberg.adventofcode2020.utilities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class DuplicationCollector<T> implements Collector<T, List<T>, List<T>> {
    private final int _factor;

    public DuplicationCollector(int factor) {
        _factor = factor;
    }

    @Override
    public Supplier<List<T>> supplier() {
        return ArrayList::new;
    }

    @Override
    public BiConsumer<List<T>, T> accumulator() {
        return (t, u) -> {
            for (int i = 0; i < _factor; i++) {
                t.add(u);
            }
        };
    }

    @Override
    public BinaryOperator<List<T>> combiner() {
        return (t, u) -> {
            List<T> combined = new ArrayList<>();
            combined.addAll(t);
            combined.addAll(u);
            return combined;
        };
    }

    @Override
    public Function<List<T>, List<T>> finisher() {
        return t -> t;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return new HashSet<>();
    }

}