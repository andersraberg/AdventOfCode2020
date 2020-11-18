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

public class SublistCollector<T> implements Collector<T, List<List<T>>, List<List<T>>> {
    private final int _size;
    private final boolean _includeIncomplete;
    
    private List<T> _tmpList = new ArrayList<>();

    public SublistCollector(int size, boolean includeIncomplete) {
        _size = size;
        _includeIncomplete = includeIncomplete;
    }

    @Override
    public Supplier<List<List<T>>> supplier() {
        return ArrayList::new;
    }

    @Override
    public BiConsumer<List<List<T>>, T> accumulator() {
        return (t, u) -> {
            synchronized (_tmpList) {
                _tmpList.add(u);
                if (_tmpList.size() == _size) {
                    t.add(new ArrayList<>(_tmpList));
                    _tmpList.clear();
                }
            }
        };
    }

    @Override
    public BinaryOperator<List<List<T>>> combiner() {
        return (t, u) -> {
            List<List<T>> combined = new ArrayList<>();
            combined.addAll(t);
            combined.addAll(u);
            return combined;
        };
    }

    @Override
    public Function<List<List<T>>, List<List<T>>> finisher() {
        return t -> {
            synchronized (_tmpList) {
                if (!_tmpList.isEmpty() && _includeIncomplete) {
                    t.add(_tmpList);
                }
            }
            return t;
        };
    }

    @Override
    public Set<Characteristics> characteristics() {
        return new HashSet<>();
    }

}
