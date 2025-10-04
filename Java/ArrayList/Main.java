package Java.ArrayList;
import java.util.ArrayList;
import java.util.List;

class Person {
    String name;
    int age;

    Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
}

class ValidatedList<T> extends ArrayList<T> {
    
    // Override the add method to include validation
    @Override
    public boolean add(T element) {
        // Custom validation logic here
        if (element instanceof Person) {
            Person person = (Person) element;

            // Validation: check if the person's name is null or empty
            if (person.name == null || person.name.isEmpty()) {
                throw new IllegalArgumentException("Person's name cannot be null or empty.");
            }

            // Validation: check if the person's age is valid
            if (person.age < 0 || person.age > 150) {
                throw new IllegalArgumentException("Person's age must be between 0 and 150.");
            }
        }
        
        // If no exception was thrown, add the element to the list
        return super.add(element);
    }
    public ValidatedList<Person>giveArrayListAndReturnValidatedList(ArrayList<Person>list)
    {
        return null;

    }
}

public class Main {
    public static void main(String[] args) {
        ValidatedList<Person> personList = new ArrayList<Person>();

        try {
            // Attempt to add an invalid person (age < 0)
            personList.add(new Person("Alice", -5));
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }

        try {
            // Attempt to add an invalid person (name is empty)
            personList.add(new Person("", 25));
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Valid person
        personList.add(new Person("Bob", 30));
        System.out.println("People in the list:");
        for (Person person : personList) {
            System.out.println(person.name + " - " + person.age);
        }

      
    }
    public ValidatedList<Person> giveArrayListAndReturnValidatedList(ArrayList<Person> inputList)
    {
        return (ValidatedList) inputList;
    }
}
