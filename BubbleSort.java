import java.util.ArrayList;
import java.util.List;

public class BubbleSort {
    
    /**
     * 冒泡排序算法实现 - 只排序数字元素
     * @param arr 待排序的对象数组（只处理数字类型的元素）
     * @return 排序后的数字数组
     */
    public static int[] bubbleSort(Object[] arr) {
        if (arr == null || arr.length == 0) {
            return new int[0];
        }
        
        // 提取数组中的数字元素
        List<Integer> numbers = new ArrayList<>();
        for (Object obj : arr) {
            if (obj instanceof Number) {
                numbers.add(((Number) obj).intValue());
            } else if (obj instanceof String) {
                // 尝试将字符串解析为数字
                try {
                    String str = ((String) obj).trim();
                    // 检查是否为有效的数字格式
                    if (str.matches("-?\\d+")) {
                        numbers.add(Integer.parseInt(str));
                    }
                } catch (NumberFormatException e) {
                    // 忽略无法解析的字符串
                }
            }
            // 忽略其他非数字类型
        }
        
        // 转换为数组
        int[] numArray = new int[numbers.size()];
        for (int i = 0; i < numbers.size(); i++) {
            numArray[i] = numbers.get(i);
        }
        
        // 如果没有数字元素，返回空数组
        if (numArray.length <= 1) {
            return numArray;
        }
        
        int n = numArray.length;
        boolean swapped;
        
        // 外层循环控制趟数
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            
            // 内层循环进行相邻元素比较和交换
            for (int j = 0; j < n - 1 - i; j++) {
                if (numArray[j] > numArray[j + 1]) {
                    // 交换元素
                    int temp = numArray[j];
                    numArray[j] = numArray[j + 1];
                    numArray[j + 1] = temp;
                    swapped = true;
                }
            }
            
            // 如果某一趟没有发生交换，说明数组已经有序，提前结束
            if (!swapped) {
                break;
            }
        }
        
        return numArray;
    }
    
    /**
     * 打印数组
     * @param arr 要打印的数组
     */
    public static void printArray(int[] arr) {
        if (arr == null || arr.length == 0) {
            System.out.println("[]");
            return;
        }
        System.out.print("[");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
            if (i < arr.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }
    
    public static void main(String[] args) {
        // 测试用例1: 混合类型数组（数字、字符串、其他对象）
        Object[] arr1 = {64, "34", 25, "abc", 12, null, 22, "11", 90, "xyz", 3.14};
        
        System.out.println("原始数组:");
        System.out.print("[");
        for (int i = 0; i < arr1.length; i++) {
            System.out.print(arr1[i]);
            if (i < arr1.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
        
        int[] result1 = bubbleSort(arr1);
        
        System.out.println("排序后的数字:");
        printArray(result1);
        
        // 测试用例2: 纯字符串数组（包含数字和非数字）
        Object[] arr2 = {"5", "hello", "1", "4", "world", "2", "8", "test"};
        System.out.println("\n原始数组:");
        System.out.print("[");
        for (int i = 0; i < arr2.length; i++) {
            System.out.print(arr2[i]);
            if (i < arr2.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
        
        int[] result2 = bubbleSort(arr2);
        System.out.println("排序后的数字:");
        printArray(result2);
        
        // 测试用例3: 没有数字的数组
        Object[] arr3 = {"hello", "world", null, 3.14f};
        System.out.println("\n原始数组:");
        System.out.print("[");
        for (int i = 0; i < arr3.length; i++) {
            System.out.print(arr3[i]);
            if (i < arr3.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
        
        int[] result3 = bubbleSort(arr3);
        System.out.println("排序后的数字:");
        printArray(result3);
    }
}
