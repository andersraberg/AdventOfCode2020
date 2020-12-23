package se.anders_raberg.adventofcode2020.utilities;

import java.util.Objects;

public class Quadruple<U, V, Q, W> {
    private final U _first;
    private final V _second;
    private final Q _third;
    private final W _fourth;

    public Quadruple(U first, V second, Q third, W forth) {
        _first = first;
        _second = second;
        _third = third;
        _fourth = forth;
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

    public W fourth() {
        return _fourth;
    }

    @Override
    public int hashCode() {
        return Objects.hash(_first, _second, _third, _fourth);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Quadruple)) {
            return false;
        }
        Quadruple other = (Quadruple) obj;
        return Objects.equals(_first, other._first) && Objects.equals(_fourth, other._fourth)
                && Objects.equals(_second, other._second) && Objects.equals(_third, other._third);
    }

    @Override
    public String toString() {
        return String.format("[%s, %s, %s, %s]", _first, _second, _third, _fourth);
    }

}
