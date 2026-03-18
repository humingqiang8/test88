#!/bin/bash

# 批量压缩指定目录下的所有文件夹为 zip 文件
# 用法: ./batch_zip.sh [目标目录]
# 如果不指定目录，默认为当前目录

# 检查是否提供了目录参数
if [ -n "$1" ]; then
    TARGET_DIR="$1"
else
    TARGET_DIR="."
fi

# 检查目录是否存在
if [ ! -d "$TARGET_DIR" ]; then
    echo "错误: 目录 '$TARGET_DIR' 不存在"
    exit 1
fi

# 切换到目标目录
cd "$TARGET_DIR" || exit 1

echo "开始压缩目录: $(pwd)"
echo "----------------------------------------"

# 计数器
count=0
success=0
failed=0

# 遍历当前目录下的所有文件夹
for dir in */; do
    # 检查是否为目录（排除非目录项）
    if [ -d "$dir" ]; then
        # 去除末尾的斜杠获取文件夹名
        folder_name="${dir%/}"
        
        # 定义输出的 zip 文件名
        zip_file="${folder_name}.zip"
        
        echo "正在压缩: $folder_name -> $zip_file"
        
        # 使用 zip 命令递归压缩文件夹
        # -r: 递归处理子目录
        # -q: 安静模式，减少输出
        if zip -rq "$zip_file" "$folder_name"; then
            echo "  ✓ 成功: $zip_file"
            ((success++))
        else
            echo "  ✗ 失败: $folder_name"
            ((failed++))
        fi
        
        ((count++))
    fi
done

echo "----------------------------------------"
echo "压缩完成!"
echo "总计文件夹数: $count"
echo "成功: $success"
echo "失败: $failed"

# 如果没有找到任何文件夹
if [ $count -eq 0 ]; then
    echo "提示: 在目录 '$TARGET_DIR' 中未找到任何文件夹"
fi
