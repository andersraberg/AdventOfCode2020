package se.anders_raberg.adventofcode2020.utilities;

import java.util.Objects;

public class Triple<U, V, Q> {
    private final U _first;
    private final V _second;
    private final Q _third;

    public Triple(U first, V second, Q third) {
        _first = first;
        _second = second;
        _third = third;
    }

    public U first() {
        return _first;
    }

    public V second() {
        return _second;
    }

    public Q third() {
        return _third;
    }

    @Override
    public int hashCode() {
        return Objects.hash(_first, _second, _third);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Triple)) {
            return false;
        }
        Triple<?, ?, ?> other = (Triple<?, ?, ?>) obj;
        return Objects.equals(_first, other._first) && Objects.equals(_second, other._second)
                && Objects.equals(_third, other._third);
    }

    @Override
    public String toString() {
        return String.format("[%s, %s, %s]", _first, _second, _third);
    }

}
