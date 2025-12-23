# **Arrays in Java - Cheat Sheet**

## **Declaration & Initialization**
```java
// Declaration
int[] arr;                // Preferred
int arr[];                // Alternative

// Initialization
int[] arr = new int[5];   // Size 5, all zeros
int[] arr = {1, 2, 3};    // Initialize with values
int[] arr = new int[]{1, 2, 3}; // Alternative syntax

// Multi-dimensional
int[][] matrix = new int[3][3];
int[][] jagged = new int[3][]; // Jagged array
```

## **Key Properties**
- Fixed size (static)
- Zero-based indexing
- Contiguous memory allocation
- All elements same type
- Length: `arr.length` (NOT a method!)
- Default values: 0 (int), 0.0 (double), false (boolean), null (objects)

## **Common Operations**
```java
// Access/Modify
arr[0] = 10;              // O(1) access
int x = arr[0];           // O(1) read

// Iteration
for (int i = 0; i < arr.length; i++) {
    System.out.println(arr[i]);
}

// Enhanced for-loop
for (int num : arr) {
    System.out.println(num);
}

// Copying
int[] copy = Arrays.copyOf(arr, arr.length);
int[] copy = arr.clone();
System.arraycopy(src, 0, dest, 0, length);
```

## **Important Methods (java.util.Arrays)**
```java
import java.util.Arrays;

Arrays.sort(arr);                     // Quick sort
Arrays.binarySearch(arr, key);        // Array must be sorted
Arrays.fill(arr, value);              // Fill with value
Arrays.equals(arr1, arr2);            // Compare arrays
Arrays.toString(arr);                 // "[1, 2, 3]"
Arrays.deepToString(matrix);          // For multi-dimensional
```

## **Memory & Performance**
- Access: O(1)
- Search (unsorted): O(n)
- Search (sorted): O(log n) with binary search
- Insert/Delete at end: O(1) if space exists
- Insert/Delete at middle: O(n) due to shifting
- Cache-friendly (locality of reference)

---

# **Challenge: Array Mastery**

## **Problem: Array Transformer**

You're given an integer array and must implement several operations **without using any built-in sorting or collection methods** (except `Arrays.copyOf` for copying).

Implement the following class:

```java
public class ArrayTransformer {
    private int[] data;
    
    // Constructor
    public ArrayTransformer(int[] initialData) {
        this.data = Arrays.copyOf(initialData, initialData.length);
    }
    
    // 1. Reverse in-place
    public void reverse() {
        // TODO: Implement
    }
    
    // 2. Remove duplicates (return new array)
    public int[] removeDuplicates() {
        // TODO: Implement
        return new int[0];
    }
    
    // 3. Rotate array right by k positions (in-place, circular)
    public void rotateRight(int k) {
        // TODO: Implement
    }
    
    // 4. Find maximum subarray sum (Kadane's algorithm)
    public int maxSubarraySum() {
        // TODO: Implement
        return 0;
    }
    
    // 5. Merge with another sorted array (both are sorted)
    public int[] mergeSorted(int[] otherArray) {
        // TODO: Implement
        return new int[0];
    }
    
    // 6. Get current array
    public int[] getArray() {
        return Arrays.copyOf(data, data.length);
    }
}
```

## **Constraints & Requirements**
1. **Reverse**: Must be done in-place with O(1) extra space
2. **Remove Duplicates**: Return new array, maintain original order, O(n) time if possible
3. **Rotate Right**: Should handle k > array.length (wrap around), O(1) extra space
4. **Max Subarray**: Must be O(n) time, O(1) space
5. **Merge Sorted**: Both arrays are sorted, result must be sorted, O(n+m) time

## **Example Usage & Output**
```java
int[] test = {3, 1, 2, 2, 4, 1, 5};
ArrayTransformer transformer = new ArrayTransformer(test);

transformer.reverse();
// Result: [5, 1, 4, 2, 2, 1, 3]

int[] noDups = transformer.removeDuplicates();
// Result: [5, 1, 4, 2, 3] (maintain reversed order)

transformer.rotateRight(2);
// Result: [3, 5, 1, 4, 2, 2, 1]

int maxSum = transformer.maxSubarraySum();
// Result: 18 (sum of all elements in this case)

int[] merged = transformer.mergeSorted(new int[]{0, 2, 6});
// Result: [0, 1, 1, 2, 2, 3, 4, 5, 6] (sorted merge)
```

## **Bonus Challenges**
1. Implement `findMissingNumber()` assuming array has numbers 1 to n with one missing
2. Implement `findDuplicate()` where one number appears twice (others appear once)
3. Implement `productExceptSelf()` returning array where output[i] = product of all elements except data[i] (without division, O(n))

## **Learning Objectives**
- Master array indexing and manipulation
- Understand in-place vs. new array operations
- Implement common algorithms with arrays
- Handle edge cases (empty, single element, duplicates)
- Practice space/time complexity analysis

**Remember**: Test with edge cases - empty array, single element, all same elements, already sorted, reverse sorted!