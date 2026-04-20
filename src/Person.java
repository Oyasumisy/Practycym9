public class Person implements Comparable<Person> {
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