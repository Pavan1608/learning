# Java LeetCode All Patterns & Algorithms Cheat Sheet

[LeetCode interview sheet](https://leetcode.com/explore/interview/card/cheatsheets/720/resources/4723/)

## 🎯 Essential LeetCode Patterns

### 1. Two Pointers
**When to use:** Sorted arrays, linked lists, finding pairs, palindromes
**Time Complexity:** O(n)
**Template:**
```java
public void twoPointers(int[] arr) {
    int left = 0, right = arr.length - 1;
    while (left < right) {
        // Process elements at both pointers
        if (condition) {
            left++;
        } else {
            right--;
        }
    }
}
```

### 2. Sliding Window
**When to use:** Contiguous subarrays/substrings, max/min in window
**Time Complexity:** O(n)
**Template:**
```java
public int slidingWindow(int[] arr, int k) {
    int left = 0, result = 0;
    for (int right = 0; right < arr.length; right++) {
        // Expand window
        // windowSum += arr[right];
        
        while (windowConditionViolated) {
            // Shrink window
            left++;
        }
        
        // Update result
        result = Math.max(result, right - left + 1);
    }
    return result;
}
```

### 3. Fast & Slow Pointers (Floyd's Algorithm)
**When to use:** Cycle detection, finding middle element
**Time Complexity:** O(n)
**Template:**
```java
public ListNode detectCycle(ListNode head) {
    ListNode slow = head, fast = head;
    
    while (fast != null && fast.next != null) {
        slow = slow.next;
        fast = fast.next.next;
        
        if (slow == fast) {
            // Cycle detected
            return slow;
        }
    }
    return null; // No cycle
}
```

### 4. Binary Search
**When to use:** Sorted arrays, search space reduction
**Time Complexity:** O(log n)
**Templates:**
```java
// Classic Binary Search
public int binarySearch(int[] arr, int target) {
    int left = 0, right = arr.length - 1;
    
    while (left <= right) {
        int mid = left + (right - left) / 2;
        
        if (arr[mid] == target) return mid;
        else if (arr[mid] < target) left = mid + 1;
        else right = mid - 1;
    }
    return -1;
}

// Find First/Last Occurrence
public int findFirst(int[] arr, int target) {
    int left = 0, right = arr.length - 1;
    int result = -1;
    
    while (left <= right) {
        int mid = left + (right - left) / 2;
        
        if (arr[mid] == target) {
            result = mid;
            right = mid - 1; // Continue searching left
        } else if (arr[mid] < target) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    return result;
}
```

### 5. Tree Traversals (DFS)
**Time Complexity:** O(n)
**Templates:**
```java
// Preorder (Root -> Left -> Right)
public void preorder(TreeNode root, List<Integer> result) {
    if (root == null) return;
    result.add(root.val);
    preorder(root.left, result);
    preorder(root.right, result);
}

// Inorder (Left -> Root -> Right)
public void inorder(TreeNode root, List<Integer> result) {
    if (root == null) return;
    inorder(root.left, result);
    result.add(root.val);
    inorder(root.right, result);
}

// Postorder (Left -> Right -> Root)
public void postorder(TreeNode root, List<Integer> result) {
    if (root == null) return;
    postorder(root.left, result);
    postorder(root.right, result);
    result.add(root.val);
}
```

### 6. Tree/Graph BFS
**When to use:** Level-order traversal, shortest path
**Time Complexity:** O(n)
**Template:**
```java
public List<List<Integer>> levelOrder(TreeNode root) {
    List<List<Integer>> result = new ArrayList<>();
    if (root == null) return result;
    
    Queue<TreeNode> queue = new LinkedList<>();
    queue.offer(root);
    
    while (!queue.isEmpty()) {
        int levelSize = queue.size();
        List<Integer> currentLevel = new ArrayList<>();
        
        for (int i = 0; i < levelSize; i++) {
            TreeNode node = queue.poll();
            currentLevel.add(node.val);
            
            if (node.left != null) queue.offer(node.left);
            if (node.right != null) queue.offer(node.right);
        }
        result.add(currentLevel);
    }
    return result;
}
```

### 7. Backtracking
**When to use:** Permutations, combinations, subsets, constraint satisfaction
**Time Complexity:** O(2^n) typically
**Template:**
```java
public List<List<Integer>> backtrack(int[] nums) {
    List<List<Integer>> result = new ArrayList<>();
    List<Integer> current = new ArrayList<>();
    backtrackHelper(nums, 0, current, result);
    return result;
}

private void backtrackHelper(int[] nums, int start, List<Integer> current, List<List<Integer>> result) {
    // Base case - add current combination
    result.add(new ArrayList<>(current));
    
    for (int i = start; i < nums.length; i++) {
        // Choose
        current.add(nums[i]);
        
        // Explore
        backtrackHelper(nums, i + 1, current, result);
        
        // Unchoose (backtrack)
        current.remove(current.size() - 1);
    }
}
```

### 8. Dynamic Programming
**When to use:** Optimal substructure, overlapping subproblems
**Templates:**
```java
// 1D DP
public int dpOneDimension(int[] arr) {
    int n = arr.length;
    int[] dp = new int[n];
    
    // Base case
    dp[0] = arr[0];
    
    for (int i = 1; i < n; i++) {
        dp[i] = Math.max(dp[i-1], arr[i]); // Example recurrence
    }
    
    return dp[n-1];
}

// 2D DP
public int dpTwoDimension(int[][] grid) {
    int m = grid.length, n = grid[0].length;
    int[][] dp = new int[m][n];
    
    // Base cases
    dp[0][0] = grid[0][0];
    
    for (int i = 0; i < m; i++) {
        for (int j = 0; j < n; j++) {
            if (i == 0 && j == 0) continue;
            
            int fromTop = (i > 0) ? dp[i-1][j] : 0;
            int fromLeft = (j > 0) ? dp[i][j-1] : 0;
            
            dp[i][j] = grid[i][j] + Math.max(fromTop, fromLeft);
        }
    }
    
    return dp[m-1][n-1];
}
```

### 9. Union-Find (Disjoint Set)
**When to use:** Connected components, cycle detection in undirected graphs
**Time Complexity:** O(α(n)) with path compression
**Template:**
```java
class UnionFind {
    private int[] parent;
    private int[] rank;
    
    public UnionFind(int n) {
        parent = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }
    
    public int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]); // Path compression
        }
        return parent[x];
    }
    
    public boolean union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        
        if (rootX == rootY) return false; // Already connected
        
        // Union by rank
        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else {
            parent[rootY] = rootX;
            rank[rootX]++;
        }
        return true;
    }
}
```

### 10. Merge Intervals
**When to use:** Overlapping intervals, scheduling problems
**Time Complexity:** O(n log n)
**Template:**
```java
public int[][] merge(int[][] intervals) {
    if (intervals.length <= 1) return intervals;
    
    // Sort by start time
    Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
    
    List<int[]> result = new ArrayList<>();
    int[] current = intervals[0];
    
    for (int i = 1; i < intervals.length; i++) {
        if (current[1] >= intervals[i][0]) {
            // Overlapping intervals, merge them
            current[1] = Math.max(current[1], intervals[i][1]);
        } else {
            // Non-overlapping interval
            result.add(current);
            current = intervals[i];
        }
    }
    
    result.add(current);
    return result.toArray(new int[result.size()][]);
}
```

## 🏗️ Core Data Structures in Java

### Arrays
```java
// Declaration and initialization
int[] arr = new int[10];
int[] arr2 = {1, 2, 3, 4, 5};

// Common operations
Arrays.sort(arr);                    // O(n log n)
int index = Arrays.binarySearch(arr, target); // O(log n)
Arrays.fill(arr, value);            // O(n)
```

### ArrayList
```java
List<Integer> list = new ArrayList<>();
list.add(element);           // O(1) amortized
list.get(index);            // O(1)
list.remove(index);         // O(n)
list.contains(element);     // O(n)
Collections.sort(list);     // O(n log n)
```

### LinkedList
```java
LinkedList<Integer> list = new LinkedList<>();
list.addFirst(element);     // O(1)
list.addLast(element);      // O(1)
list.removeFirst();         // O(1)
list.removeLast();          // O(1)
```

### Stack
```java
Stack<Integer> stack = new Stack<>();
// Better to use Deque
Deque<Integer> stack = new ArrayDeque<>();

stack.push(element);        // O(1)
stack.pop();               // O(1)
stack.peek();              // O(1)
stack.isEmpty();           // O(1)
```

### Queue
```java
Queue<Integer> queue = new LinkedList<>();
// Better performance with ArrayDeque
Queue<Integer> queue = new ArrayDeque<>();

queue.offer(element);       // O(1)
queue.poll();              // O(1)
queue.peek();              // O(1)
queue.isEmpty();           // O(1)
```

### Priority Queue (Heap)
```java
// Min Heap (default)
PriorityQueue<Integer> minHeap = new PriorityQueue<>();

// Max Heap
PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());

// Custom comparator
PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);

pq.offer(element);          // O(log n)
pq.poll();                 // O(log n)
pq.peek();                 // O(1)
```

### HashMap
```java
Map<String, Integer> map = new HashMap<>();
map.put(key, value);        // O(1) average
map.get(key);              // O(1) average
map.containsKey(key);      // O(1) average
map.remove(key);           // O(1) average

// Iteration
for (Map.Entry<String, Integer> entry : map.entrySet()) {
    String key = entry.getKey();
    Integer value = entry.getValue();
}
```

### HashSet
```java
Set<Integer> set = new HashSet<>();
set.add(element);          // O(1) average
set.contains(element);     // O(1) average
set.remove(element);       // O(1) average
```

### TreeMap/TreeSet (Red-Black Tree)
```java
TreeMap<String, Integer> treeMap = new TreeMap<>();
TreeSet<Integer> treeSet = new TreeSet<>();

// All operations: O(log n)
treeMap.put(key, value);
treeMap.get(key);
treeSet.add(element);
treeSet.contains(element);
```

## 🔧 Common Java Algorithms

### Sorting Algorithms
```java
// Built-in sorting
Arrays.sort(arr);                    // Dual-Pivot Quicksort O(n log n)
Collections.sort(list);              // Timsort O(n log n)

// Custom comparator
Arrays.sort(arr, (a, b) -> a - b);   // Ascending
Arrays.sort(arr, (a, b) -> b - a);   // Descending

// Quick Sort implementation
public void quickSort(int[] arr, int low, int high) {
    if (low < high) {
        int pi = partition(arr, low, high);
        quickSort(arr, low, pi - 1);
        quickSort(arr, pi + 1, high);
    }
}

private int partition(int[] arr, int low, int high) {
    int pivot = arr[high];
    int i = low - 1;
    
    for (int j = low; j < high; j++) {
        if (arr[j] <= pivot) {
            i++;
            swap(arr, i, j);
        }
    }
    swap(arr, i + 1, high);
    return i + 1;
}

// Merge Sort implementation
public void mergeSort(int[] arr, int left, int right) {
    if (left < right) {
        int mid = left + (right - left) / 2;
        mergeSort(arr, left, mid);
        mergeSort(arr, mid + 1, right);
        merge(arr, left, mid, right);
    }
}

private void merge(int[] arr, int left, int mid, int right) {
    int[] temp = new int[right - left + 1];
    int i = left, j = mid + 1, k = 0;
    
    while (i <= mid && j <= right) {
        if (arr[i] <= arr[j]) {
            temp[k++] = arr[i++];
        } else {
            temp[k++] = arr[j++];
        }
    }
    
    while (i <= mid) temp[k++] = arr[i++];
    while (j <= right) temp[k++] = arr[j++];
    
    System.arraycopy(temp, 0, arr, left, temp.length);
}
```

### Graph Algorithms
```java
// DFS for Graph
public void dfs(List<List<Integer>> adj, int node, boolean[] visited) {
    visited[node] = true;
    System.out.print(node + " ");
    
    for (int neighbor : adj.get(node)) {
        if (!visited[neighbor]) {
            dfs(adj, neighbor, visited);
        }
    }
}

// BFS for Graph
public void bfs(List<List<Integer>> adj, int start) {
    Queue<Integer> queue = new LinkedList<>();
    boolean[] visited = new boolean[adj.size()];
    
    queue.offer(start);
    visited[start] = true;
    
    while (!queue.isEmpty()) {
        int node = queue.poll();
        System.out.print(node + " ");
        
        for (int neighbor : adj.get(node)) {
            if (!visited[neighbor]) {
                visited[neighbor] = true;
                queue.offer(neighbor);
            }
        }
    }
}

// Dijkstra's Algorithm
public int[] dijkstra(List<List<int[]>> adj, int start) {
    int n = adj.size();
    int[] dist = new int[n];
    Arrays.fill(dist, Integer.MAX_VALUE);
    dist[start] = 0;
    
    PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]);
    pq.offer(new int[]{start, 0});
    
    while (!pq.isEmpty()) {
        int[] curr = pq.poll();
        int node = curr[0];
        int currDist = curr[1];
        
        if (currDist > dist[node]) continue;
        
        for (int[] edge : adj.get(node)) {
            int neighbor = edge[0];
            int weight = edge[1];
            int newDist = currDist + weight;
            
            if (newDist < dist[neighbor]) {
                dist[neighbor] = newDist;
                pq.offer(new int[]{neighbor, newDist});
            }
        }
    }
    
    return dist;
}
```

## 🎨 Problem-Specific Patterns

### Prefix Sum
```java
public int[] prefixSum(int[] arr) {
    int n = arr.length;
    int[] prefix = new int[n + 1];
    
    for (int i = 0; i < n; i++) {
        prefix[i + 1] = prefix[i] + arr[i];
    }
    
    return prefix;
}

// Range sum query: sum from index i to j
public int rangeSum(int[] prefix, int i, int j) {
    return prefix[j + 1] - prefix[i];
}
```

### Trie (Prefix Tree)
```java
class TrieNode {
    TrieNode[] children = new TrieNode[26];
    boolean isEndOfWord = false;
}

class Trie {
    private TrieNode root;
    
    public Trie() {
        root = new TrieNode();
    }
    
    public void insert(String word) {
        TrieNode curr = root;
        for (char c : word.toCharArray()) {
            int index = c - 'a';
            if (curr.children[index] == null) {
                curr.children[index] = new TrieNode();
            }
            curr = curr.children[index];
        }
        curr.isEndOfWord = true;
    }
    
    public boolean search(String word) {
        TrieNode node = searchPrefix(word);
        return node != null && node.isEndOfWord;
    }
    
    public boolean startsWith(String prefix) {
        return searchPrefix(prefix) != null;
    }
    
    private TrieNode searchPrefix(String prefix) {
        TrieNode curr = root;
        for (char c : prefix.toCharArray()) {
            int index = c - 'a';
            if (curr.children[index] == null) {
                return null;
            }
            curr = curr.children[index];
        }
        return curr;
    }
}
```

## ⚡ Time & Space Complexity Quick Reference

| Data Structure | Access | Search | Insertion | Deletion | Space |
|----------------|--------|--------|-----------|----------|-------|
| Array | O(1) | O(n) | O(n) | O(n) | O(n) |
| ArrayList | O(1) | O(n) | O(1)* | O(n) | O(n) |
| LinkedList | O(n) | O(n) | O(1) | O(1) | O(n) |
| Stack | O(n) | O(n) | O(1) | O(1) | O(n) |
| Queue | O(n) | O(n) | O(1) | O(1) | O(n) |
| HashMap | - | O(1)* | O(1)* | O(1)* | O(n) |
| TreeMap | O(log n) | O(log n) | O(log n) | O(log n) | O(n) |
| PriorityQueue | O(n) | O(n) | O(log n) | O(log n) | O(n) |

*Amortized time complexity

## 🧠 Problem Recognition Patterns

| Problem Type | Key Indicators | Recommended Pattern |
|--------------|----------------|-------------------|
| Array sum/product | Contiguous subarray | Sliding Window |
| Find pairs | Sorted array, two elements | Two Pointers |
| Cycle detection | Linked list/graph | Fast & Slow Pointers |
| Range queries | Multiple queries on array | Prefix Sum |
| Path finding | Grid/graph shortest path | BFS |
| All combinations | Generate all possibilities | Backtracking |
| Optimization | Overlapping subproblems | Dynamic Programming |
| Connected components | Graph connectivity | Union-Find |
| Top K elements | K largest/smallest | Heap/Priority Queue |
| Searching | Sorted data | Binary Search |

## 📝 Common Java Tricks & Tips

### String Operations
```java
// StringBuilder for concatenation
StringBuilder sb = new StringBuilder();
sb.append("text");
String result = sb.toString();

// String comparison
str1.equals(str2);              // Content comparison
str1.compareTo(str2);           // Lexicographic comparison

// Character operations
Character.isDigit(c);
Character.isLetter(c);
Character.toLowerCase(c);
Character.getNumericValue(c);   // '5' -> 5
```

### Math Operations
```java
Math.max(a, b);
Math.min(a, b);
Math.abs(x);
Math.pow(base, exponent);
Math.sqrt(x);
Math.ceil(x);
Math.floor(x);

// Bit operations
x & y;          // AND
x | y;          // OR
x ^ y;          // XOR
~x;             // NOT
x << 1;         // Left shift (multiply by 2)
x >> 1;         // Right shift (divide by 2)
```

### Array Operations
```java
int[] copy = Arrays.copyOf(original, newLength);
int[] range = Arrays.copyOfRange(original, from, to);
Arrays.equals(arr1, arr2);      // Deep comparison
Arrays.toString(arr);           // Print array
```

This comprehensive cheat sheet covers the most important patterns, algorithms, and data structures needed for LeetCode problems in Java. Practice these patterns systematically to improve your problem-solving skills!
