# 文件监控自动上传脚本

这是一个 Node.js 脚本，用于监控文件夹变化并自动上传新增文件。

## 功能特点

- 📁 实时监控指定文件夹的新文件
- 🚀 自动上传新文件到指定服务器
- ⏱️ 智能延迟上传，确保文件写入完成
- 🔄 避免重复上传同一文件
- 📂 支持递归监控子目录
- 🛑 优雅退出处理

## 安装

```bash
npm install
```

## 使用方法

### 1. 配置上传地址

通过环境变量设置上传目标 URL：

```bash
export UPLOAD_URL=http://your-server.com/upload
```

或者直接修改 `index.js` 中的 `CONFIG.uploadUrl` 配置项。

### 2. 启动监控

```bash
npm start
```

或者：

```bash
node index.js
```

### 3. 添加文件

将新文件放入 `./watched` 目录（或配置的监控目录），脚本会自动检测并上传。

## 配置说明

在 `index.js` 中的 `CONFIG` 对象可以自定义以下配置：

| 配置项 | 说明 | 默认值 |
|--------|------|--------|
| `watchFolder` | 要监控的文件夹路径 | `./watched` |
| `uploadUrl` | 上传的目标 URL | `http://localhost:3000/upload` |
| `recursive` | 是否递归监控子目录 | `true` |
| `ignored` | 忽略的文件模式 | 隐藏文件（以.开头） |
| `uploadDelay` | 等待文件写入完成的时间（毫秒） | `1000` |

## 上传请求格式

脚本会以 `multipart/form-data` 格式发送 POST 请求，包含：

- `file`: 文件内容
- `relativePath`: 文件相对于监控目录的路径

## 示例服务器端代码（Express）

```javascript
import express from 'express';
import multer from 'multer';
import path from 'path';

const app = express();
const upload = multer({ dest: 'uploads/' });

app.post('/upload', upload.single('file'), (req, res) => {
  console.log('收到文件:', req.file);
  console.log('相对路径:', req.body.relativePath);
  res.json({ message: '上传成功', file: req.file });
});

app.listen(3000, () => {
  console.log('服务器运行在 http://localhost:3000');
});
```

## 停止监控

按 `Ctrl+C` 可优雅停止监控服务。

## 注意事项

1. 确保上传服务器已启动并可访问
2. 首次运行时会自动创建监控目录
3. 已上传的文件会被记录，避免重复上传
4. 重启脚本后会重置已上传文件记录