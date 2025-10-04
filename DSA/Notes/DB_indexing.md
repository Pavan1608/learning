#[link of youtube tutorial](https://youtu.be/6ZquiVH8AGU?si=UnAdElGgg0msWfkD)


[![DB Indexing YouTube Video](https://img.youtube.com/vi/6ZquiVH8AGU/0.jpg)](https://youtu.be/6ZquiVH8AGU)

### How Data is Stored in DBMS

- Data in a relational database is logically represented as tables (rows and columns) but physically stored in **data pages**.
- A typical data page size is **8 KB**, though this can vary by DBMS.
- Each data page is split into:
    - **Header** (96 bytes): Contains page number, free space, checksum, etc.
    - **Data Records Area** (~8060 bytes): Stores actual row data.
    - **Offset Array** (36 bytes): Contains pointers (offsets) to the actual row positions within the data area.
- The **offset array** holds an array of pointers, each pointing to an individual row within the page, allowing quick access to rows even if they aren’t in sequential order.
- Multiple data pages may be required to store all the rows for a large table.
- **Data blocks** are the smallest units of physical read/write on disk (typically 4 KB to 32 KB, often 8 KB). Data pages are stored within data blocks, but the DBMS does not control which page goes into which block; that’s managed by the storage subsystem.
- The DBMS maintains a **mapping from data page to data block addresses** for efficient retrieval.

***

### Why Indexing is Needed

- Without indexing, searching for data (e.g., a specific employee ID) can require a **full table scan**, checking every row on every data page (O(n) time complexity).
- **Indexing** is used to boost the performance of database queries, enabling much faster lookups.

***

### What is Indexing? Types of Indexes

- **Indexing** is the method by which the DBMS makes data retrieval more efficient by improving the performance of query.
- Two main types:
    - **Clustered Indexing**: The actual data rows are stored in the order of the index; there is only one clustered index per table.
    -                         by default the Primary gets the periority for cluster key, if Pk is not present then DBMS creates an additional                                  incremental key which will be use for B+ tree creation.
    - **Non-Clustered Indexing**: The index contains pointers to the row data, which can be stored in various locations. there can multiple secondary cluster key.
- *Note :* without indexing the seach operarion would be O(N) time complexity
***

### Data Structures Used in Indexing

           [25]
          /     \
     [17,19]   [25,30]
    /   |   \   /   |   \
[1,6] [17] [19] [25] [30]

- Most RDBMSs use the **B+ Tree (or B-Tree)** data structure for implementing indexes.
- B+ Trees have these properties:
    - All leaf nodes are at the same level.
    - Each node can have at most “m” child nodes.
    - Each internal node contains up to (m-1) keys and m child pointers.
    - Keys in nodes help direct the search path, dividing the data range.
- **Searching, insertion, and deletion in B+ Trees is O(log n)**.

***

### How B+ Tree Indexing Works
- Leaf nodes of the B+ Tree store the **actual index values** and pointers to their corresponding data rows/pages.
- Internal/root nodes store key values strictly to aid traversal, not actual data pointers.(since it is in sorted order so help in searching by   pointing wheather to go to left of right)
- When rows are inserted, the B+ Tree index is updated. If a data page is full, it is split and appropriate pointers are updated. The index reflects changes immediately for consistent search paths.
- Each index value entry points to the page (or row) location where the full row data is stored.

***

### Example: Inserting Data and Updating the Index

- When a new row is inserted:
    - The DBMS finds the correct place in the B+ Tree index for this row’s value.
    - If the relevant data page is not full, the row is inserted and the B+ Tree is updated with its page pointer.
    - Also each page has pointer to data block also
    - If the data page is full, a **page split** occurs: contents are distributed between old and new pages, and the index is updated for all involved keys and pointers.
- The index entries maintain the mapping between indexed values and the actual data pages (and, via the DBMS mapping, to the physical data
block).

Insert key K:  
    ↓  
Traverse B+ Tree index for correct leaf for K  
    ↓  
Find Data Page indicated by leaf node/pointer  
    ↓  
IF Page has space:  
    Insert record  
ELSE:  
    Split page, redistribute, update index  

***

### Physical Storage and Mapping

- **Data pages** are managed by the DBMS.
- **Data blocks** are managed by the underlying storage system (disk/SSD).
- **DBMS maintains mapping** from pages to blocks; it can control which rows go into which page, but not the physical location of the page.
- For each page, the index value pointers are updated so that queries know exactly where to look (which data page and, transitively, which physical block).

***

### Summary Table: Key Concepts

| Concept | Description / Role |
| :-- | :-- |
| Data Page | Logical container (usually 8 KB), managed by DBMS, contains multiple rows and an offset array |
| Data Block | Smallest physical I/O unit on disk (4 KB to 32 KB), stores data pages, managed by storage |
| Offset Array | Pointer list at end of data page, maps indexes to actual rows within the page |
| B+ Tree | Data structure for indexes, provides O(log n) search, leaf nodes hold actual keys/pointers |
| Clustered Index | Data physically ordered by index, only one per table |
| Non-Clustered Index | Index points to data row, row order independent of index |
| Page Splitting | Happens when data page fills; new page is allocated, index is updated to split data/pointers |
| Index Entry Pointer | Part of B+ Tree leaf; each entry directly points to the page holding the actual row data |


***

### Practical Takeaways

- A table’s apparent row-and-column format is a logical abstraction; physically, data is organized and accessed via pages and indexes.[^1]
- Indexing (especially B+ tree-based) is crucial for high-performance search operations in large tables; without it, queries become slow as they require scanning many disk pages.[^1]
- Understanding the page/offset/index structure is vital for DB design, query tuning, and troubleshooting performance bottlenecks.[^1]

***

This summary captures the core technical explanations, examples, and the step-by-step logic explained in the video, condensing the main points into a clear guide for interview prep or deep understanding.

<div align="center">⁂</div>

[^1]: https://www.youtube.com/watch?v=6ZquiVH8AGU

