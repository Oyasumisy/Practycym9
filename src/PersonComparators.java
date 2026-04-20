import java.util.Comparator;

public class PersonComparators {
    public static Comparator<Person> byName() {
        return (p1, p2) -> p1.getName().compareTo(p2.getName());
    }

    public static Comparator<Person> byAge() {
        return (p1, p2) -> Integer.compare(p1.getAge(), p2.getAge());
    }

    public static Comparator<Person> byGenderThenName() {
        return (p1, p2) -> {
            int cmp = Boolean.compare(p2.isMale(), p1.isMale());
            if (cmp != 0) return cmp;
            return p1.getName().compareTo(p2.getName());
        };
    }
}