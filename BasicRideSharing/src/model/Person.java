package model;

public class Person {
    private final long id;
    private final String name;

    public Person(long id, String name) {
        if(id <= 0) {
            throw new IllegalArgumentException("Person ID must be positive");
        }
        if(name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name must not be blank");
        }
        this.id = id;
        this.name = name.strip();
    }

    public final long getId() {
        return id;
    }
    public final String getName() {
        return name;
    }
}
