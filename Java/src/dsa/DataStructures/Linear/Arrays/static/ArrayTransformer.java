
/**
 * @author onyxwizard
 * @date 22-12-2025
 */

import java.util.Arrays;
import java.util.LinkedHashSet;

public class ArrayTransformer {
  private int[] data;

  // Constructor
  public ArrayTransformer(int[] initialData) {
    this.data = Arrays.copyOf(initialData, initialData.length);
  }

  // 1. Reverse in-place
  public void reverse() {
    // TODO: Implement
    int left = 0, right = data.length - 1;
    while (left < right) {
      int temp = data[left];
      data[left] = data[right];
      data[right] = temp;
      left++;
      right--;
    }
  }

  // 2. Remove duplicates (return new array)
  public int[] removeDuplicates() {
    // TODO: Implement
    // LinkedHashSet maintains insertion order
    LinkedHashSet<Integer> seen = new LinkedHashSet<>();

    for (int num : data) {
      seen.add(num);
    }

    // Convert set to array
    int[] result = new int[seen.size()];
    int i = 0;
    for (int num : seen) {
      result[i++] = num;
    }
    return result;
  }

  // 3. Rotate array right by k positions (in-place, circular)
  public void rotateRight(int k) {
    // TODO: Implement
    int rotate = k % data.length;
    // Early exit
    if (rotate == 0)
      return;

    int start = 0;
    int mid = data.length - rotate - 1;
    int end = data.length - 1;
    reverseHelper(data, start, mid);
    reverseHelper(data, mid + 1, end);
    reverseHelper(data, 0, end);
  }

  private void reverseHelper(int[] arr, int start, int end) {
    while (start < end) {
      int temp = arr[start];
      arr[start] = arr[end];
      arr[end] = temp;
      start++;
      end--;
    }
  }

  // 4. Find maximum subarray sum (Kadane's algorithm)
  public int maxSubarraySum() {
    // TODO: Implement
    if (data.length == 0) return 0;
        
        int currentSum = data[0];
        int maxSum = data[0];
        
        for (int i = 1; i < data.length; i++) {
            // Start new subarray or extend existing one
            currentSum = Math.max(data[i], currentSum + data[i]);
            // Update maximum sum found so far
            maxSum = Math.max(maxSum, currentSum);
        }
        
        return maxSum;
  }

  // 5. Merge with another sorted array (both are sorted)
  public int[] mergeSorted(int[] otherArray) {
    // TODO: Implement

    int[] result = new int[data.length + otherArray.length];
    int i = 0, j = 0, k = 0;

    // Merge while both arrays have elements
    while (i < data.length && j < otherArray.length) {
      if (data[i] <= otherArray[j]) {
        result[k++] = data[i++];
      } else {
        result[k++] = otherArray[j++];
      }
    }

    // Copy remaining elements from data array
    while (i < data.length) {
      result[k++] = data[i++];
    }

    // Copy remaining elements from otherArray
    while (j < otherArray.length) {
      result[k++] = otherArray[j++];
    }

    return result;
  }

  // 6. Get current array
  public int[] getArray() {
    return Arrays.copyOf(data, data.length);
  }

  public static void main(String[] args) {
    // Test cases
        int[] arr = {1, 2, 3, 4, 5};
        ArrayTransformer transformer = new ArrayTransformer(arr);
        
        System.out.println("Original: " + Arrays.toString(transformer.getArray()));
        
        // Test reverse
        transformer.reverse();
        System.out.println("After reverse: " + Arrays.toString(transformer.getArray()));
        
        // Reset
        transformer = new ArrayTransformer(arr);
        
        // Test removeDuplicates
        int[] arrWithDupes = {1, 2, 2, 3, 4, 4, 5};
        ArrayTransformer dupTransformer = new ArrayTransformer(arrWithDupes);
        System.out.println("Remove duplicates: " + 
            Arrays.toString(dupTransformer.removeDuplicates()));
        
        // Test rotateRight
        transformer.rotateRight(2);
        System.out.println("After rotateRight(2): " + 
            Arrays.toString(transformer.getArray()));
        
        // Test maxSubarraySum
        int[] kadaneArr = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        ArrayTransformer kadaneTransformer = new ArrayTransformer(kadaneArr);
        System.out.println("Max subarray sum: " + kadaneTransformer.maxSubarraySum());
        
        // Test mergeSorted
        int[] sorted1 = {1, 3, 5, 7};
        int[] sorted2 = {2, 4, 6, 8};
        ArrayTransformer mergeTransformer = new ArrayTransformer(sorted1);
        int[] merged = mergeTransformer.mergeSorted(sorted2);
        System.out.println("Merged sorted arrays: " + Arrays.toString(merged));
  }
}