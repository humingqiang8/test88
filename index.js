import chokidar from 'chokidar';
import fs from 'fs';
import path from 'path';
import FormData from 'form-data';
import fetch from 'node-fetch';

// 配置项
const CONFIG = {
  // 要监控的文件夹路径
  watchFolder: './watched',
  // 上传的目标 URL
  uploadUrl: process.env.UPLOAD_URL || 'http://localhost:3000/upload',
  // 是否递归监控子目录
  recursive: true,
  // 忽略的文件模式
  ignored: /(^|[\/\\])\../, // 忽略以点开头的文件（隐藏文件）
  // 等待文件写入完成的时间（毫秒）
  uploadDelay: 1000
};

// 记录已处理过的文件，避免重复上传
const uploadedFiles = new Set();

/**
 * 上传单个文件
 * @param {string} filePath - 文件的完整路径
 */
async function uploadFile(filePath) {
  const relativePath = path.relative(CONFIG.watchFolder, filePath);
  
  console.log(`📤 准备上传: ${relativePath}`);
  
  try {
    const form = new FormData();
    const fileStream = fs.createReadStream(filePath);
    
    form.append('file', fileStream, {
      filename: path.basename(filePath),
      filepath: filePath
    });
    
    // 添加相对路径信息（如果服务器需要）
    form.append('relativePath', relativePath);
    
    const response = await fetch(CONFIG.uploadUrl, {
      method: 'POST',
      body: form,
      headers: form.getHeaders()
    });
    
    if (response.ok) {
      const result = await response.json().catch(() => ({ message: 'Upload successful' }));
      console.log(`✅ 上传成功: ${relativePath}`);
      console.log(`   服务器响应:`, result);
      uploadedFiles.add(filePath);
    } else {
      const errorText = await response.text();
      console.error(`❌ 上传失败: ${relativePath}`);
      console.error(`   状态码: ${response.status}`);
      console.error(`   响应: ${errorText}`);
    }
  } catch (error) {
    console.error(`❌ 上传出错: ${relativePath}`);
    console.error(`   错误信息:`, error.message);
  }
}

/**
 * 处理新文件
 * @param {string} filePath - 文件的完整路径
 */
function handleNewFile(filePath) {
  // 检查是否是文件（而不是目录）
  if (!fs.statSync(filePath).isFile()) {
    return;
  }
  
  // 检查是否已经上传过
  if (uploadedFiles.has(filePath)) {
    console.log(`⏭️  文件已上传过，跳过: ${path.relative(CONFIG.watchFolder, filePath)}`);
    return;
  }
  
  // 延迟上传，确保文件写入完成
  setTimeout(() => {
    uploadFile(filePath);
  }, CONFIG.uploadDelay);
}

/**
 * 初始化监控
 */
function startWatching() {
  // 确保监控目录存在
  if (!fs.existsSync(CONFIG.watchFolder)) {
    console.log(`📁 创建监控目录: ${CONFIG.watchFolder}`);
    fs.mkdirSync(CONFIG.watchFolder, { recursive: true });
  }
  
  console.log('🔍 开始监控文件夹变化...');
  console.log(`   监控目录: ${path.resolve(CONFIG.watchFolder)}`);
  console.log(`   上传地址: ${CONFIG.uploadUrl}`);
  console.log(`   按 Ctrl+C 停止监控\n`);
  
  // 创建监控实例
  const watcher = chokidar.watch(CONFIG.watchFolder, {
    ignored: CONFIG.ignored,
    persistent: true,
    ignoreInitial: true, // 不触发初始添加事件
    awaitWriteFinish: {
      stabilityThreshold: 2000,
      pollInterval: 100
    }
  });
  
  // 监听文件添加事件
  watcher.on('add', (filePath) => {
    console.log(`📄 检测到新文件: ${path.relative(CONFIG.watchFolder, filePath)}`);
    handleNewFile(filePath);
  });
  
  // 监听目录添加事件（可选）
  watcher.on('addDir', (dirPath) => {
    console.log(`📂 检测到新目录: ${path.relative(CONFIG.watchFolder, dirPath)}`);
  });
  
  // 错误处理
  watcher.on('error', (error) => {
    console.error('❌ 监控出错:', error);
  });
  
  // 就绪事件
  watcher.on('ready', () => {
    console.log('✅ 监控服务已就绪\n');
  });
  
  return watcher;
}

// 启动监控
const watcher = startWatching();

// 优雅退出处理
process.on('SIGINT', () => {
  console.log('\n🛑 正在停止监控...');
  watcher.close().then(() => {
    console.log('✅ 监控服务已停止');
    process.exit(0);
  });
});

process.on('SIGTERM', () => {
  console.log('\n🛑 正在停止监控...');
  watcher.close().then(() => {
    console.log('✅ 监控服务已停止');
    process.exit(0);
  });
});
