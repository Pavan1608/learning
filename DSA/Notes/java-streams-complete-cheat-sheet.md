# Java Stream API - Complete Methods & Question Patterns Cheat Sheet

## 🌊 Stream Creation Methods

### Basic Stream Creation
| Method | Example | Description |
|--------|---------|-------------|
| `Collection.stream()` | `list.stream()` | Sequential stream from collection |
| `Collection.parallelStream()` | `list.parallelStream()` | Parallel stream from collection |
| `Stream.of(elements)` | `Stream.of(1,2,3)` | Stream from varargs |
| `Arrays.stream(array)` | `Arrays.stream(arr)` | Stream from array |
| `Stream.empty()` | `Stream.empty()` | Empty stream |
| `Stream.iterate(seed, func)` | `Stream.iterate(0, n -> n+2)` | Infinite stream with iteration |
| `Stream.generate(supplier)` | `Stream.generate(Math::random)` | Infinite stream with supplier |
| `IntStream.range(start, end)` | `IntStream.range(1, 10)` | Range stream (exclusive) |
| `IntStream.rangeClosed(start, end)` | `IntStream.rangeClosed(1, 10)` | Range stream (inclusive) |

## 🔄 Intermediate Operations (Return Stream)

### Filtering & Slicing
| Method | Signature | Example | Description |
|--------|-----------|---------|-------------|
| `filter(Predicate<T>)` | `Stream<T>` | `.filter(x -> x > 5)` | Filter elements by condition |
| `distinct()` | `Stream<T>` | `.distinct()` | Remove duplicates |
| `limit(long n)` | `Stream<T>` | `.limit(10)` | Take first n elements |
| `skip(long n)` | `Stream<T>` | `.skip(5)` | Skip first n elements |
| `dropWhile(Predicate<T>)` | `Stream<T>` | `.dropWhile(x -> x < 5)` | Skip while condition true (Java 9) |
| `takeWhile(Predicate<T>)` | `Stream<T>` | `.takeWhile(x -> x < 10)` | Take while condition true (Java 9) |

### Transformation
| Method | Signature | Example | Description |
|--------|-----------|---------|-------------|
| `map(Function<T,R>)` | `Stream<R>` | `.map(String::toUpperCase)` | Transform each element |
| `flatMap(Function<T,Stream<R>>)` | `Stream<R>` | `.flatMap(Collection::stream)` | Flatten nested structures |
| `mapToInt(ToIntFunction<T>)` | `IntStream` | `.mapToInt(String::length)` | Map to IntStream |
| `mapToLong(ToLongFunction<T>)` | `LongStream` | `.mapToLong(x -> x * 2L)` | Map to LongStream |
| `mapToDouble(ToDoubleFunction<T>)` | `DoubleStream` | `.mapToDouble(Integer::doubleValue)` | Map to DoubleStream |
| `flatMapToInt(Function<T,IntStream>)` | `IntStream` | `.flatMapToInt(s -> s.chars())` | FlatMap to IntStream |

### Sorting & Debugging
| Method | Signature | Example | Description |
|--------|-----------|---------|-------------|
| `sorted()` | `Stream<T>` | `.sorted()` | Sort naturally |
| `sorted(Comparator<T>)` | `Stream<T>` | `.sorted((a,b) -> a.compareTo(b))` | Sort with comparator |
| `peek(Consumer<T>)` | `Stream<T>` | `.peek(System.out::println)` | Side-effect without changing stream |

## 🎯 Terminal Operations (Trigger Execution)

### Collection Operations
| Method | Return Type | Example | Description |
|--------|-------------|---------|-------------|
| `collect(Collector)` | `R` | `.collect(Collectors.toList())` | Collect to mutable container |
| `toArray()` | `Object[]` | `.toArray()` | Convert to Object array |
| `toArray(IntFunction)` | `T[]` | `.toArray(String[]::new)` | Convert to typed array |

### Search & Match Operations
| Method | Return Type | Example | Description |
|--------|-------------|---------|-------------|
| `findFirst()` | `Optional<T>` | `.findFirst()` | First element (if any) |
| `findAny()` | `Optional<T>` | `.findAny()` | Any element (useful in parallel) |
| `anyMatch(Predicate<T>)` | `boolean` | `.anyMatch(x -> x > 0)` | True if any element matches |
| `allMatch(Predicate<T>)` | `boolean` | `.allMatch(x -> x > 0)` | True if all elements match |
| `noneMatch(Predicate<T>)` | `boolean` | `.noneMatch(x -> x < 0)` | True if no elements match |

### Reduction Operations
| Method | Return Type | Example | Description |
|--------|-------------|---------|-------------|
| `reduce(BinaryOperator<T>)` | `Optional<T>` | `.reduce(Integer::sum)` | Reduce to single value |
| `reduce(T, BinaryOperator<T>)` | `T` | `.reduce(0, Integer::sum)` | Reduce with identity |
| `reduce(U, BiFunction, BinaryOperator)` | `U` | `.reduce("", String::concat, String::concat)` | Reduce with different types |
| `min(Comparator<T>)` | `Optional<T>` | `.min(Comparator.naturalOrder())` | Minimum element |
| `max(Comparator<T>)` | `Optional<T>` | `.max(Comparator.naturalOrder())` | Maximum element |
| `count()` | `long` | `.count()` | Number of elements |

### Iteration Operations
| Method | Return Type | Example | Description |
|--------|-------------|---------|-------------|
| `forEach(Consumer<T>)` | `void` | `.forEach(System.out::println)` | Execute action for each element |
| `forEachOrdered(Consumer<T>)` | `void` | `.forEachOrdered(System.out::println)` | Execute action maintaining order |

## 📦 Collectors Class - All Methods

### Basic Collection Collectors
| Method | Return Type | Example | Description |
|--------|-------------|---------|-------------|
| `toList()` | `List<T>` | `.collect(toList())` | Collect to ArrayList |
| `toSet()` | `Set<T>` | `.collect(toSet())` | Collect to HashSet |
| `toCollection(Supplier)` | `Collection<T>` | `.collect(toCollection(LinkedList::new))` | Collect to specific collection |
| `toUnmodifiableList()` | `List<T>` | `.collect(toUnmodifiableList())` | Immutable list (Java 10) |
| `toUnmodifiableSet()` | `Set<T>` | `.collect(toUnmodifiableSet())` | Immutable set (Java 10) |

### Map Collectors
| Method | Return Type | Example | Description |
|--------|-------------|---------|-------------|
| `toMap(keyMapper, valueMapper)` | `Map<K,V>` | `.collect(toMap(Person::getName, Person::getAge))` | Simple map collection |
| `toMap(keyMapper, valueMapper, mergeFunction)` | `Map<K,V>` | `.collect(toMap(k -> k, v -> v, (v1,v2) -> v1))` | With duplicate key handling |
| `toMap(keyMapper, valueMapper, mergeFunction, mapSupplier)` | `Map<K,V>` | `.collect(toMap(k->k, v->v, (v1,v2)->v1, TreeMap::new))` | With custom map type |
| `toConcurrentMap(...)` | `ConcurrentMap<K,V>` | Same as toMap but concurrent | Thread-safe map |
| `toUnmodifiableMap(...)` | `Map<K,V>` | Same as toMap but immutable | Immutable map (Java 10) |

### String Collectors
| Method | Return Type | Example | Description |
|--------|-------------|---------|-------------|
| `joining()` | `String` | `.collect(joining())` | Concatenate strings |
| `joining(delimiter)` | `String` | `.collect(joining(", "))` | Join with delimiter |
| `joining(delimiter, prefix, suffix)` | `String` | `.collect(joining(", ", "[", "]"))` | Join with delimiter and wrapper |

### Grouping & Partitioning
| Method | Return Type | Example | Description |
|--------|-------------|---------|-------------|
| `groupingBy(Function)` | `Map<K, List<V>>` | `.collect(groupingBy(Person::getDept))` | Group by classifier |
| `groupingBy(Function, Collector)` | `Map<K,R>` | `.collect(groupingBy(Person::getDept, counting()))` | Group with downstream collector |
| `groupingBy(Function, Supplier, Collector)` | `Map<K,R>` | `.collect(groupingBy(k->k, TreeMap::new, toList()))` | Group with custom map and downstream |
| `groupingByConcurrent(...)` | `ConcurrentMap<K,R>` | Same as groupingBy but concurrent | Thread-safe grouping |
| `partitioningBy(Predicate)` | `Map<Boolean, List<T>>` | `.collect(partitioningBy(x -> x > 0))` | Partition by true/false |
| `partitioningBy(Predicate, Collector)` | `Map<Boolean, R>` | `.collect(partitioningBy(x->x>0, counting()))` | Partition with downstream |

### Statistical Collectors (Numeric)
| Method | Return Type | Example | Description |
|--------|-------------|---------|-------------|
| `counting()` | `Long` | `.collect(counting())` | Count elements |
| `summingInt(ToIntFunction)` | `Integer` | `.collect(summingInt(Person::getAge))` | Sum integer values |
| `summingLong(ToLongFunction)` | `Long` | `.collect(summingLong(Person::getSalary))` | Sum long values |
| `summingDouble(ToDoubleFunction)` | `Double` | `.collect(summingDouble(Person::getScore))` | Sum double values |
| `averagingInt(ToIntFunction)` | `Double` | `.collect(averagingInt(Person::getAge))` | Average of integers |
| `averagingLong(ToLongFunction)` | `Double` | `.collect(averagingLong(Person::getSalary))` | Average of longs |
| `averagingDouble(ToDoubleFunction)` | `Double` | `.collect(averagingDouble(Person::getScore))` | Average of doubles |

### Statistical Summary Collectors
| Method | Return Type | Example | Description |
|--------|-------------|---------|-------------|
| `summarizingInt(ToIntFunction)` | `IntSummaryStatistics` | `.collect(summarizingInt(Person::getAge))` | Int statistics (count, sum, min, max, avg) |
| `summarizingLong(ToLongFunction)` | `LongSummaryStatistics` | `.collect(summarizingLong(Person::getSalary))` | Long statistics |
| `summarizingDouble(ToDoubleFunction)` | `DoubleSummaryStatistics` | `.collect(summarizingDouble(Person::getScore))` | Double statistics |

### Min/Max Collectors
| Method | Return Type | Example | Description |
|--------|-------------|---------|-------------|
| `maxBy(Comparator)` | `Optional<T>` | `.collect(maxBy(Comparator.comparing(Person::getAge)))` | Maximum by comparator |
| `minBy(Comparator)` | `Optional<T>` | `.collect(minBy(Comparator.comparing(Person::getAge)))` | Minimum by comparator |

### Reducing Collectors
| Method | Return Type | Example | Description |
|--------|-------------|---------|-------------|
| `reducing(BinaryOperator)` | `Optional<T>` | `.collect(reducing(Integer::sum))` | Reduce operation |
| `reducing(identity, BinaryOperator)` | `T` | `.collect(reducing(0, Integer::sum))` | Reduce with identity |
| `reducing(identity, Function, BinaryOperator)` | `U` | `.collect(reducing(0, Person::getAge, Integer::sum))` | Map-reduce |

### Filtering & Mapping Collectors (Java 9+)
| Method | Return Type | Example | Description |
|--------|-------------|---------|-------------|
| `filtering(Predicate, Collector)` | `Collector` | `.collect(groupingBy(Person::getDept, filtering(p->p.getAge()>25, toList())))` | Filter then collect |
| `mapping(Function, Collector)` | `Collector` | `.collect(groupingBy(Person::getDept, mapping(Person::getName, toList())))` | Map then collect |
| `flatMapping(Function, Collector)` | `Collector` | `.collect(groupingBy(Person::getDept, flatMapping(p->p.getSkills().stream(), toList())))` | FlatMap then collect |

### Composition Collectors (Java 12+)
| Method | Return Type | Example | Description |
|--------|-------------|---------|-------------|
| `teeing(Collector, Collector, BiFunction)` | `Collector` | `.collect(teeing(summingInt(Person::getAge), counting(), (sum, count) -> sum/count))` | Combine two collectors |

## 🔍 Common Stream Question Patterns

### Pattern 1: Collection Processing
**When to use:** Basic filtering, mapping, collecting operations
```java
// Find employees with salary > 50000, get their names in uppercase
List<String> highEarners = employees.stream()
    .filter(emp -> emp.getSalary() > 50000)
    .map(Employee::getName)
    .map(String::toUpperCase)
    .collect(Collectors.toList());
```

### Pattern 2: Grouping & Aggregation
**When to use:** Group by some property, then perform aggregation
```java
// Group employees by department, count in each department
Map<String, Long> deptCounts = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::getDepartment,
        Collectors.counting()
    ));

// Group by department, get average salary
Map<String, Double> avgSalaryByDept = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::getDepartment,
        Collectors.averagingDouble(Employee::getSalary)
    ));
```

### Pattern 3: Partitioning
**When to use:** Divide data into two groups based on condition
```java
// Partition employees into high and low earners
Map<Boolean, List<Employee>> partitioned = employees.stream()
    .collect(Collectors.partitioningBy(emp -> emp.getSalary() > 60000));

List<Employee> highEarners = partitioned.get(true);
List<Employee> lowEarners = partitioned.get(false);
```

### Pattern 4: Nested Collections (flatMap)
**When to use:** Working with nested structures, need to flatten
```java
// Get all skills from all employees
List<String> allSkills = employees.stream()
    .flatMap(emp -> emp.getSkills().stream())
    .distinct()
    .collect(Collectors.toList());

// Nested lists flattening
List<List<String>> listOfLists = Arrays.asList(
    Arrays.asList("a", "b"),
    Arrays.asList("c", "d", "e")
);

List<String> flattened = listOfLists.stream()
    .flatMap(List::stream)
    .collect(Collectors.toList());
```

### Pattern 5: Statistical Operations
**When to use:** Need mathematical computations on collections
```java
// Get statistics on employee ages
IntSummaryStatistics ageStats = employees.stream()
    .mapToInt(Employee::getAge)
    .summaryStatistics();

System.out.println("Average age: " + ageStats.getAverage());
System.out.println("Max age: " + ageStats.getMax());
System.out.println("Count: " + ageStats.getCount());
```

### Pattern 6: Finding Extremes
**When to use:** Need min, max, or specific elements
```java
// Find employee with highest salary
Optional<Employee> highestPaid = employees.stream()
    .max(Comparator.comparing(Employee::getSalary));

// Find second highest salary
employees.stream()
    .mapToDouble(Employee::getSalary)
    .boxed()
    .sorted(Comparator.reverseOrder())
    .skip(1)
    .findFirst();
```

### Pattern 7: String Processing
**When to use:** Manipulating strings, counting characters, etc.
```java
// Count unique characters in a string
String text = "hello world";
long uniqueChars = text.chars()
    .distinct()
    .count();

// Get word frequencies
String sentence = "hello world hello java world";
Map<String, Long> wordCount = Arrays.stream(sentence.split(" "))
    .collect(Collectors.groupingBy(
        Function.identity(),
        Collectors.counting()
    ));
```

### Pattern 8: Complex Filtering & Mapping
**When to use:** Multi-step transformations with conditions
```java
// Get names of employees in IT dept with salary > 50000, sorted by name
List<String> result = employees.stream()
    .filter(emp -> "IT".equals(emp.getDepartment()))
    .filter(emp -> emp.getSalary() > 50000)
    .map(Employee::getName)
    .sorted()
    .collect(Collectors.toList());
```

### Pattern 9: Reducing Operations
**When to use:** Need to combine all elements into single result
```java
// Calculate total salary of all employees
double totalSalary = employees.stream()
    .mapToDouble(Employee::getSalary)
    .sum(); // or .reduce(0.0, Double::sum)

// Concatenate all employee names
String allNames = employees.stream()
    .map(Employee::getName)
    .collect(Collectors.joining(", "));
```

### Pattern 10: Parallel Processing
**When to use:** Large datasets, CPU-intensive operations
```java
// Parallel processing for better performance
List<Integer> largeList = IntStream.rangeClosed(1, 1000000)
    .boxed()
    .collect(Collectors.toList());

double average = largeList.parallelStream()
    .mapToInt(Integer::intValue)
    .filter(n -> n % 2 == 0)
    .average()
    .orElse(0.0);
```

## 🎯 Interview Question Types & Solutions

### Type 1: Data Transformation
```java
// Q: Convert list of strings to uppercase, filter length > 3
List<String> words = Arrays.asList("java", "stream", "api", "collect");
List<String> result = words.stream()
    .filter(s -> s.length() > 3)
    .map(String::toUpperCase)
    .collect(Collectors.toList());
```

### Type 2: Aggregation & Statistics
```java
// Q: Find average salary by department
Map<String, Double> avgSalByDept = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::getDepartment,
        Collectors.averagingDouble(Employee::getSalary)
    ));
```

### Type 3: Complex Grouping
```java
// Q: Group employees by department, then by salary range
Map<String, Map<String, List<Employee>>> grouped = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::getDepartment,
        Collectors.groupingBy(emp -> 
            emp.getSalary() > 50000 ? "High" : "Low"
        )
    ));
```

### Type 4: Top N Problems
```java
// Q: Find top 3 highest paid employees
List<Employee> top3 = employees.stream()
    .sorted(Comparator.comparing(Employee::getSalary).reversed())
    .limit(3)
    .collect(Collectors.toList());
```

### Type 5: Existence Checks
```java
// Q: Check if all employees have salary > 30000
boolean allHighPaid = employees.stream()
    .allMatch(emp -> emp.getSalary() > 30000);

// Q: Find if any employee is from "HR"
boolean hasHR = employees.stream()
    .anyMatch(emp -> "HR".equals(emp.getDepartment()));
```

## ⚠️ Common Pitfalls & Best Practices

### Pitfalls to Avoid
1. **Reusing Streams**: Streams are one-time use only
2. **Overusing Parallel Streams**: Not always faster for small datasets
3. **Side Effects in Intermediate Operations**: Avoid modifying external state
4. **Forgetting Terminal Operations**: Intermediate operations are lazy
5. **Boxing/Unboxing Overhead**: Use primitive streams when possible

### Best Practices
1. **Use Method References**: `Employee::getName` instead of `emp -> emp.getName()`
2. **Prefer Specific Collectors**: `Collectors.toList()` over manual collection
3. **Use Primitive Streams**: `IntStream`, `LongStream` for better performance
4. **Chain Operations Logically**: Filter before map for better performance
5. **Use Parallel Carefully**: Profile before using `parallelStream()`

This comprehensive cheat sheet covers all major Stream API methods, collectors, and common question patterns for Java interviews and competitive programming.