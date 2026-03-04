public class BubbleSort {
    
    /**
     * 冒泡排序算法实现
     * @param arr 待排序的整数数组
     */
    public static void bubbleSort(int[] arr) {
        if (arr == null || arr.length <= 1) {
            return;
        }
        
        int n = arr.length;
        boolean swapped;
        
        // 外层循环控制趟数
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            
            // 内层循环进行相邻元素比较和交换
            for (int j = 0; j < n - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    // 交换元素
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                }
            }
            
            // 如果某一趟没有发生交换，说明数组已经有序，提前结束
            if (!swapped) {
                break;
            }
        }
    }
    
    /**
     * 打印数组
     * @param arr 要打印的数组
     */
    public static void printArray(int[] arr) {
        for (int num : arr) {
            System.out.print(num + " ");
        }
        System.out.println();
    }
    
    public static void main(String[] args) {
        // 测试用例
        int[] arr = {64, 34, 25, 12, 22, 11, 90};
        
        System.out.println("原始数组:");
        printArray(arr);
        
        bubbleSort(arr);
        
        System.out.println("排序后数组:");
        printArray(arr);
        
        // 更多测试用例
        int[] arr2 = {5, 1, 4, 2, 8};
        System.out.println("\n原始数组:");
        printArray(arr2);
        
        bubbleSort(arr2);
        
        System.out.println("排序后数组:");
        printArray(arr2);
    }
}
