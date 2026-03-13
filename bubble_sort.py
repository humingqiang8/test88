def bubble_sort(arr):
    """
    冒泡排序算法
    :param arr: 待排序的列表
    :return: 排序后的列表
    """
    n = len(arr)
    # 外层循环控制趟数
    for i in range(n - 1):
        # 内层循环进行相邻元素比较和交换
        # 每趟排序后，最大的元素会"冒泡"到末尾
        for j in range(n - 1 - i):
            if arr[j] > arr[j + 1]:
                # 交换元素
                arr[j], arr[j + 1] = arr[j + 1], arr[j]
    return arr

# 测试示例
if __name__ == "__main__":
    test_arr = [64, 34, 25, 12, 22, 11, 90]
    print("原始数组:", test_arr)
    sorted_arr = bubble_sort(test_arr.copy())
    print("排序后数组:", sorted_arr)
    
    # 更多测试用例
    test_cases = [
        [5, 2, 8, 1, 9],
        [1, 2, 3, 4, 5],  # 已排序
        [5, 4, 3, 2, 1],  # 逆序
        [3],              # 单个元素
        []                # 空数组
    ]
    
    print("\n更多测试:")
    for i, case in enumerate(test_cases):
        result = bubble_sort(case.copy())
        print(f"测试{i+1}: {case} -> {result}")
