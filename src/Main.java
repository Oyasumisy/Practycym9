import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Comparator;

public class Main {
    public static class Person implements Comparable<Person> {
        private String name;
        private int age;
        private boolean isMale;

        public Person(String name, int age, boolean isMale) {
            this.name = name;
            this.age = age;
            this.isMale = isMale;
        }
        public String getName() { return name; }
        public int getAge() { return age; }
        public boolean isMale() { return isMale; }
        @Override
        public int compareTo(Person other) {
            return Integer.compare(this.age, other.age);
        }
        @Override
        public String toString() {
            return name + " (" + age + ", " + (isMale ? "чол" : "жін") + ")";
        }
    }

    public static class PersonComparators {
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

    private static <T> String listToString(MyLinkedList<T> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(list.get(i));
        }
        return sb.toString();
    }
    private static void printPeople(MyLinkedList<Person> list, String title) {
        System.out.println(title);
        for (int i = 0; i < list.size(); i++) {
            System.out.println("  " + list.get(i));
        }
    }

    public static void main(String[] args) throws ListFullException {
        try {
            System.setOut(new PrintStream(System.out, true, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            System.err.println("UTF-8 not supported, using default encoding");
        }

        System.out.println("1. ПЕРЕВІРКА ВИКЛЮЧЕНЬ");
        try {
            MyLinkedList<String> badList = new MyLinkedList<>(-5);
        } catch (InvalidCapacityException e) {
            System.out.println("Помилка створення: " + e.getMessage());
        }

        try {
            MyLinkedList<String> stringList = new MyLinkedList<>(3);
            stringList.addLast("A");
            stringList.addLast("B");
            stringList.addLast("C");
            System.out.println("Список рядків: " + listToString(stringList));
            stringList.addLast(null);
        } catch (ListFullException | NullElementException | InvalidCapacityException e) {
            System.out.println("Помилка: " + e.getMessage());
        }
        MyLinkedList<Double> emptyList = new MyLinkedList<>();
        try {
            emptyList.get(0);
        } catch (EmptyListException e) {
            System.out.println("Помилка (get на порожньому): " + e.getMessage());
        }
        try {
            emptyList.remove(0);
        } catch (EmptyListException e) {
            System.out.println("Помилка (remove на порожньому): " + e.getMessage());
        }
        MyLinkedList<Character> charList = new MyLinkedList<>();
        try {
            charList.addLast('X');
            charList.remove(5);
        } catch (InvalidIndexException e) {
            System.out.println("Помилка індексу: " + e.getMessage());
        }

        System.out.println("\n2. СОРТУВАННЯ РЯДКІВ");
        MyLinkedList<String> fruits = new MyLinkedList<>();
        fruits.addLast("банан");
        fruits.addLast("яблуко");
        fruits.addLast("вишня");
        fruits.addLast("апельсин");
        System.out.println("До сортування: " + listToString(fruits));
        fruits.sort();
        System.out.println("Після sort() (за абеткою): " + listToString(fruits));

        System.out.println("\n3. СОРТУВАННЯ ЛЮДЕЙ");
        MyLinkedList<Person> people = new MyLinkedList<>();
        people.addLast(new Person("Іван", 25, true));
        people.addLast(new Person("Марія", 30, false));
        people.addLast(new Person("Петро", 20, true));
        people.addLast(new Person("Олена", 28, false));
        people.addLast(new Person("Андрій", 22, true));
        printPeople(people, "Початковий список:");
        people.sort();
        printPeople(people, "Після sort() (за віком):");
        people.sort(PersonComparators.byName());
        printPeople(people, "Після sort(byName) (за ім'ям):");
        people.sort(PersonComparators.byGenderThenName());
        printPeople(people, "Після sort(byGenderThenName):");

        System.out.println("\n4. ЧИСЛА ТА ListFullException");
        try {
            MyLinkedList<Integer> numbers = new MyLinkedList<>(2);
            numbers.addLast(100);
            numbers.addLast(200);
            System.out.println("Числа: " + listToString(numbers));
            numbers.addLast(300);
        } catch (ListFullException e) {
            System.out.println("Помилка переповнення: " + e.getMessage());
        } catch (InvalidCapacityException e) {
            System.out.println("Помилка створення: " + e.getMessage());
        }
        System.out.println("\nПрограма завершена успішно.");
    }
}