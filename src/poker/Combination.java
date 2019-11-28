package poker;

public enum Combination {
    StraightFlush(9), FourOfAKind(8), FullHouse(7), Flush(6), Straight(5), ThreeOfAKind(4), TwoPair(3), OnePair(2), Highest(1);

    int value;

    Combination(int p) {
        value = p;
    }

    int showValue() {
        return value;
    }
}