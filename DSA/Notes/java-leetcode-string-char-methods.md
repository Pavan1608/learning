# Java LeetCode All Patterns, String & Char Array Methods Cheat Sheet

## 📝 String Methods in Java
### Core String Methods
| Method | Description | Example | Return Type |
|--------|-------------|---------|-------------|
| `length()` | Returns the length of the string | `"hello".length()` → 5 | int |
| `charAt(int index)` | Returns character at specified index | `"hello".charAt(1)` → 'e' | char |
| `substring(int start)` | Returns substring from start to end | `"hello".substring(2)` → "llo" | String |
| `substring(int start, int end)` | Returns substring from start to end-1 | `"hello".substring(1,4)` → "ell" | String |
| `indexOf(String str)` | First occurrence of substring | `"hello".indexOf("ll")` → 2 | int |
| `lastIndexOf(String str)` | Last occurrence of substring | `"hello".lastIndexOf("l")` → 3 | int |
| `contains(CharSequence s)` | Checks if string contains sequence | `"hello".contains("ell")` → true | boolean |
| `startsWith(String prefix)` | Checks if starts with prefix | `"hello".startsWith("he")` → true | boolean |
| `endsWith(String suffix)` | Checks if ends with suffix | `"hello".endsWith("lo")` → true | boolean |

### String Modification and Utility Methods
| Method | Description | Example | Return Type |
|--------|-------------|---------|-------------|
| `equals(Object obj)` | Compare for equality | `"hello".equals("hello")` → true | boolean |
| `equalsIgnoreCase(String str)` | Case-insensitive equality | `"Hello".equalsIgnoreCase("hello")` → true | boolean |
| `concat(String str)` | Concatenate strings | `"hello".concat(" world")` → "hello world" | String |
| `replace(char old, char new)` | Replace all occurrences | `"hello".replace('l', 'x')` → "hexxo" | String |
| `toLowerCase()` | Convert to lowercase | `"HELLO".toLowerCase()` → "hello" | String |
| `toUpperCase()` | Convert to uppercase | `"hello".toUpperCase()` → "HELLO" | String |
| `trim()` | Remove leading/trailing whitespace | `" hello ".trim()` → "hello" | String |
| `split(String regex)` | Split by regex | `"a,b,c".split(",")` → ["a","b","c"] | String[] |
| `toCharArray()` | To char array | `"abc".toCharArray()` → ['a','b','c'] | char[] |
| `isEmpty()` | Check if empty | `"".isEmpty()` → true | boolean |
| `isBlank()` | Check if blank | `" ".isBlank()` → true | boolean |
| `matches(String regex)` | Match regex | `"123".matches("\\d+")` → true | boolean |
| `repeat(int count)` | Repeat n times | `"ab".repeat(3)` → ababab | String |

## 🔤 Character Array Methods
### Declaration and Initialization
```java
char[] charArray = {'a', 'b', 'c'};
char[] arr = new char[5];
```
### Operations and Utilities
| Operation | Example |
|-----------|---------|
| Access element | `charArray[0]` |
| Modify element | `charArray[1] = 'z'` |
| Length | `charArray.length` |
| Iterate | `for(char c : charArray){}` |
| Sort | `Arrays.sort(charArray)` |
| Convert to String | `new String(charArray)` or `String.valueOf(charArray)` |
| Convert from String | `"str".toCharArray()` |
| Arrays.toString | `Arrays.toString(charArray)` |
| Arrays.equals | `Arrays.equals(arr1, arr2)` |
| Arrays.fill | `Arrays.fill(charArray, 'x')` |
| Binary search | `Arrays.binarySearch(charArray, 'c')` |

### Algorithms
- Reverse:
```java
for(int i = 0, j = array.length-1; i < j; i++, j--) {
    char tmp = array[i]; array[i]=array[j]; array[j]=tmp;
}
```
- Contains:
```java
boolean found = false;
for(char c: array) { if(c == 'x') {found = true; break;} }
```

## 🏗️ StringBuilder Methods
| Method | Example |
|--------|---------|
| append | `sb.append("a")` |
| insert | `sb.insert(1, "b")` |
| delete | `sb.delete(0,2)` |
| reverse | `sb.reverse()` |
| replace | `sb.replace(1,3,"xyz")` |
| setCharAt | `sb.setCharAt(2,'p')` |
| toString | `sb.toString()` |
| length | `sb.length()` |
| capacity | `sb.capacity()` |
| ensureCapacity | `sb.ensureCapacity(40)` |

StringBuilder is preferred for efficient string concatenation in loops.

## 🎯 Character Methods
| Method | Description | Example |
|--------|-------------|---------|
| `Character.isDigit(char c)` | Check if digit | `Character.isDigit('8')` |
| `Character.isLetter(char c)` | Check if letter | `Character.isLetter('a')` |
| `Character.isUpperCase(char c)` | Is uppercase | `Character.isUpperCase('A')` |
| `Character.isLowerCase(char c)` | Is lowercase | `Character.isLowerCase('a')` |
| `Character.toUpperCase(char c)` | To uppercase | `Character.toUpperCase('a')` |
| `Character.toLowerCase(char c)` | To lowercase | `Character.toLowerCase('Z')` |

_This updated cheat sheet covers all the essential Java String and Char array methods for quick reference during coding interviews and LeetCode practice._
