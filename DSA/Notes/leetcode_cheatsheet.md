# LeetCode Cheatsheet

> **Report Issue**

This article is a collection of cheat sheets to use while solving problems and preparing for interviews. You will find:

- Time complexity (Big O) cheat sheet
- General DS/A flowchart (when to use each DS/A)
- Stages of an interview cheat sheet

---

## Time Complexity (Big O) Cheat Sheet
![](/Development/images/big_o.png)

---

### Arrays (Dynamic Array/List)

Given `n = arr.length`:

- **Add or remove element at the end:** `O(1)` (amortized)
- **Add or remove element from arbitrary index:** `O(n)`
- **Access or modify element at arbitrary index:** `O(1)`
- **Check if element exists:** `O(n)`
- **Two pointers:** `O(n⋅k)`, where `k` is the work done at each iteration (includes sliding window)
- **Building a prefix sum:** `O(n)`
- **Finding the sum of a subarray given a prefix sum:** `O(1)`

### Strings (Immutable)

Given `n = s.length`:

- **Add or remove character:** `O(n)`
- **Access element at arbitrary index:** `O(1)`
- **Concatenation between two strings:** `O(n+m)`, where `m` is the length of the other string
- **Create substring:** `O(m)`, where `m` is the length of the substring
- **Two pointers:** `O(n⋅k)`
- **Building a string from joining an array, stringbuilder, etc.:** `O(n)`

### Linked Lists

Given `n` as the number of nodes:

- **Add or remove element given pointer before add/removal location:** `O(1)`
- **Add or remove element given pointer at add/removal location:** `O(1)` (if doubly linked)
- **Add or remove element at arbitrary position without pointer:** `O(n)`
- **Access element at arbitrary position without pointer:** `O(n)`
- **Check if element exists:** `O(n)`
- **Reverse between position i and j:** `O(j−i)`
- **Detect a cycle:** `O(n)` (using fast-slow pointers or hash map)

### Hash Table/Dictionary

Given `n = dic.length`:

- **Add or remove key-value pair:** `O(1)`
- **Check if key exists:** `O(1)`
- **Check if value exists:** `O(n)`
- **Access or modify value associated with key:** `O(1)`
- **Iterate over all keys, values, or both:** `O(n)`

> Note: The `O(1)` operations are constant relative to `n`, but hashing may be expensive for large keys.

### Set

Given `n = set.length`:

- **Add or remove element:** `O(1)`
- **Check if element exists:** `O(1)`

### Stack

If implemented with a dynamic array (`n = stack.length`):

- **Push element:** `O(1)`
- **Pop element:** `O(1)`
- **Peek (see element at top):** `O(1)`
- **Access or modify element at arbitrary index:** `O(1)`
- **Check if element exists:** `O(n)`

### Queue

If implemented with a doubly linked list (`n = queue.length`):

- **Enqueue element:** `O(1)`
- **Dequeue element:** `O(1)`
- **Peek (see element at front):** `O(1)`
- **Access or modify element at arbitrary index:** `O(n)`
- **Check if element exists:** `O(n)`

### Binary Tree Problems (DFS/BFS)

Given `n` as the number of nodes:

- **Most algorithms:** `O(n⋅k)`, where `k` is the work done at each node (usually `O(1)`)

### Binary Search Tree

Given `n` as the number of nodes:

- **Add or remove element:** `O(n)` worst case, `O(log n)` average case
- **Check if element exists:** `O(n)` worst case, `O(log n)` average case

### Heap/Priority Queue

Given `n = heap.length` (min heap):

- **Add an element:** `O(log n)`
- **Delete the minimum element:** `O(log n)`
- **Find the minimum element:** `O(1)`
- **Check if element exists:** `O(n)`

### Binary Search

- **Worst case:** `O(log n)`, where `n` is the size of your initial search space

### Miscellaneous

- **Sorting:** `O(n⋅log n)`
- **DFS and BFS on a graph:** `O(n⋅k+e)`, where `n` is nodes, `e` is edges, `k` is work per node
- **DFS and BFS space complexity:** typically `O(n)`, or `O(n+e)` to store the graph
- **Dynamic programming time complexity:** `O(n⋅k)`, where `n` is number of states, `k` is work per state
- **Dynamic programming space complexity:** `O(n)`

---

## Input Sizes vs Time Complexity

The constraints of a problem hint at the upper bound for your solution's time complexity.

| Input Size         | Acceptable Time Complexity | Notes                                                                 |
|--------------------|---------------------------|-----------------------------------------------------------------------|
| `n <= 10`          | Factorial/Exponential     | `O(n^2⋅n!)`, `O(4^n)`; backtracking/brute-force is fine               |
| `10 < n <= 20`     | `O(2^n)`                  | Subset/subsequence/backtracking                                       |
| `20 < n <= 100`    | `O(n^3)`                  | Brute force/nested loops                                              |
| `100 < n <= 1,000` | `O(n^2)`                  | Quadratic is acceptable                                               |
| `1,000 < n < 100,000` | `O(n⋅log n)` or `O(n)` | Use hash maps, two pointers, sliding window, monotonic stack, etc.    |
| `100,000 < n < 1,000,000` | `O(n)`             | Linear or `O(n⋅log n)` with small constant factor                     |
| `1,000,000 < n`    | `O(log n)` or `O(1)`      | Binary search, math tricks, clever hash maps                          |

---

## Sorting Algorithms

All major languages have a built-in sort, usually `O(n⋅log n)`. For reference:

| Algorithm      | Time Complexity (Best/Average/Worst) | Space | Stable? |
|----------------|-------------------------------------|-------|---------|
| QuickSort      | O(n log n) / O(n log n) / O(n^2)    | O(log n) | No      |
| MergeSort      | O(n log n) / O(n log n) / O(n log n)| O(n)     | Yes     |
| HeapSort       | O(n log n) / O(n log n) / O(n log n)| O(1)     | No      |
| BubbleSort     | O(n) / O(n^2) / O(n^2)              | O(1)     | Yes     |
| InsertionSort  | O(n) / O(n^2) / O(n^2)              | O(1)     | Yes     |
| SelectionSort  | O(n^2) / O(n^2) / O(n^2)            | O(1)     | No      |

> **Stable sort:** Maintains the relative order of records with equal keys.
Definition of a stable sort from Wikipedia: "Stable sorting algorithms maintain the relative order of records with equal keys (i.e. values). That is, a sorting algorithm is stable if whenever there are two records R and S with the same key and with R appearing before S in the original list, R will appear before S in the sorted list."

![sorting time complexity](/Development/images/sorting.png)

---

## General DS/A Flowchart

> Use a flowchart to decide which data structure or algorithm to use. (Not shown here; refer to your course materials.)

![flowchar](/Development/images/flowchart.png)

---

## Interview Stages Cheat Sheet

### Stage 1: Introductions

- Prepare a 30-60 second intro (education, work, interests)
- Smile and speak confidently
- Listen to the interviewer and reference their work in your questions

### Stage 2: Problem Statement

- Paraphrase the problem back
- Ask clarifying questions (input size, edge cases, invalid inputs)
- Walk through an example test case

### Stage 3: Brainstorming DS&A

- Think out loud
- Break down the problem and discuss possible DS/A
- Be receptive to hints
- Explain your idea before coding

### Stage 4: Implementation

- Explain your decisions as you code
- Write clean, conventional code
- Avoid duplicate code (use helpers/loops)
- If stuck, communicate concerns
- Start with brute force if needed, then optimize
- Keep talking with your interviewer

### Stage 5: Testing & Debugging

- Walk through test cases, track variables
- Use print statements if needed
- Be vocal about problems

### Stage 6: Explanations and Follow-ups

Be ready to answer:

- Time and space complexity (average/worst case)
- Why you chose your DS/A/logic
- How to improve the algorithm

### Stage 7: Outro

- Prepare questions about the company
- Be interested, smile, and ask follow-ups