#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
网站标题爬虫
功能：抓取指定网站的页面标题并保存为 CSV 文件
"""

import requests
from bs4 import BeautifulSoup
import csv
import time
from urllib.parse import urljoin, urlparse
import argparse


def get_page_title(url, timeout=10):
    """
    获取单个页面的标题
    
    Args:
        url (str): 页面 URL
        timeout (int): 请求超时时间（秒）
    
    Returns:
        str or None: 页面标题，如果获取失败则返回 None
    """
    try:
        headers = {
            'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36'
        }
        response = requests.get(url, headers=headers, timeout=timeout, verify=False)
        response.raise_for_status()
        response.encoding = response.apparent_encoding
        
        soup = BeautifulSoup(response.text, 'html.parser')
        
        # 尝试获取 title 标签
        title_tag = soup.find('title')
        if title_tag and title_tag.string:
            return title_tag.string.strip()
        
        # 如果 title 标签为空，尝试获取 h1 标签
        h1_tag = soup.find('h1')
        if h1_tag:
            return h1_tag.get_text(strip=True)
        
        return "无标题"
    
    except requests.exceptions.RequestException as e:
        print(f"请求失败 {url}: {e}")
        return None
    except Exception as e:
        print(f"解析失败 {url}: {e}")
        return None


def crawl_website(base_url, max_pages=10, delay=1):
    """
    爬取网站多个页面的标题
    
    Args:
        base_url (str): 起始 URL
        max_pages (int): 最大爬取页面数
        delay (float): 请求间隔时间（秒）
    
    Returns:
        list: 包含 (url, title) 元组的列表
    """
    results = []
    visited = set()
    to_visit = [base_url]
    
    print(f"开始爬取网站: {base_url}")
    print(f"最大爬取页面数: {max_pages}")
    print("-" * 50)
    
    while to_visit and len(results) < max_pages:
        current_url = to_visit.pop(0)
        
        # 规范化 URL
        parsed = urlparse(current_url)
        if not parsed.scheme:
            current_url = 'http://' + current_url
        
        # 跳过已访问的 URL
        if current_url in visited:
            continue
        
        visited.add(current_url)
        
        # 获取页面标题
        title = get_page_title(current_url)
        if title:
            results.append((current_url, title))
            print(f"[{len(results)}] {title}")
            print(f"    URL: {current_url}")
        
        # 尝试发现同一域名下的新链接
        try:
            headers = {
                'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36'
            }
            response = requests.get(current_url, headers=headers, timeout=10, verify=False)
            soup = BeautifulSoup(response.text, 'html.parser')
            
            # 提取同一域名下的链接
            for link in soup.find_all('a', href=True):
                href = link['href']
                full_url = urljoin(current_url, href)
                
                # 只添加同一域名的链接
                if urlparse(full_url).netloc == urlparse(base_url).netloc:
                    if full_url not in visited and full_url not in to_visit:
                        to_visit.append(full_url)
                        
        except Exception as e:
            print(f"发现链接时出错: {e}")
        
        # 礼貌性延迟
        time.sleep(delay)
    
    return results


def save_to_csv(results, filename='website_titles.csv'):
    """
    将结果保存为 CSV 文件
    
    Args:
        results (list): 包含 (url, title) 元组的列表
        filename (str): 输出的 CSV 文件名
    """
    if not results:
        print("没有数据可保存")
        return
    
    try:
        with open(filename, 'w', newline='', encoding='utf-8-sig') as csvfile:
            writer = csv.writer(csvfile)
            writer.writerow(['序号', 'URL', '标题'])
            
            for idx, (url, title) in enumerate(results, 1):
                writer.writerow([idx, url, title])
        
        print("-" * 50)
        print(f"成功保存 {len(results)} 条记录到 {filename}")
    
    except Exception as e:
        print(f"保存 CSV 失败: {e}")


def main():
    parser = argparse.ArgumentParser(description='网站标题爬虫')
    parser.add_argument('url', nargs='?', default='https://www.python.org',
                        help='要爬取的网站 URL (默认: https://www.python.org)')
    parser.add_argument('-n', '--max-pages', type=int, default=10,
                        help='最大爬取页面数 (默认: 10)')
    parser.add_argument('-d', '--delay', type=float, default=1.0,
                        help='请求间隔时间，单位秒 (默认: 1.0)')
    parser.add_argument('-o', '--output', type=str, default='website_titles.csv',
                        help='输出 CSV 文件名 (默认: website_titles.csv)')
    
    args = parser.parse_args()
    
    # 爬取网站
    results = crawl_website(
        base_url=args.url,
        max_pages=args.max_pages,
        delay=args.delay
    )
    
    # 保存到 CSV
    save_to_csv(results, args.output)


if __name__ == '__main__':
    main()
