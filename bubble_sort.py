def bubble_sort(arr):
    """
    冒泡排序算法实现
    
    参数:
        arr: 待排序的列表
    
    返回:
        排序后的列表
    """
    n = len(arr)
    
    # 外层循环控制趟数
    for i in range(n):
        # 内层循环进行相邻元素比较和交换
        # 每趟结束后，最大的元素会"冒泡"到末尾，所以减去 i
        for j in range(0, n - i - 1):
            # 如果当前元素大于下一个元素，则交换
            if arr[j] > arr[j + 1]:
                arr[j], arr[j + 1] = arr[j + 1], arr[j]
    
    return arr


# 测试示例
if __name__ == "__main__":
    # 测试数据
    test_array = [64, 34, 25, 12, 22, 11, 90]
    
    print("原始数组:", test_array)
    sorted_array = bubble_sort(test_array.copy())
    print("排序后数组:", sorted_array)
    
    # 更多测试用例
    test_cases = [
        [5, 2, 8, 1, 9],
        [1, 2, 3, 4, 5],  # 已经有序
        [9, 8, 7, 6, 5],  # 逆序
        [3],              # 单个元素
        []                # 空数组
    ]
    
    print("\n更多测试用例:")
    for i, case in enumerate(test_cases, 1):
        result = bubble_sort(case.copy())
        print(f"测试{i}: {case} -> {result}")
