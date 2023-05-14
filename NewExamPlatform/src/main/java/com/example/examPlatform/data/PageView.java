package com.example.examPlatform.data;

import java.util.List;

/** ページング用クラス */
public class PageView<T> {
	/** 表示件数 */
	private final int PAGE_SIZE = 20;
	/** 対象リスト */
	private List<T> list;
	/** 全ページ数 */
	private int totalPages;
	/** 全要素数 */
	private int totalElements;
	/** 現在のページ番号 */
	private int pageNum;
	
	/** コントラスタ：ページ番号は１ */
	public PageView(List<T> list) {
		this.list = list;
		this.totalPages = (int) Math.ceil(list.size() / (double)PAGE_SIZE);
		this.totalElements = list.size();
		this.pageNum = 1;
	}
	
	/** コントラスタ：ページ番号を指定する(最大ページ数より大きい場合最大ページ数を設定) */
	public PageView(List<T> list, int pageNum) {
		this.list = list;
		this.totalPages = (int) Math.ceil(list.size() / (double)PAGE_SIZE);
		this.totalElements = list.size();
		
		if(pageNum > totalPages) pageNum = totalPages;
		this.pageNum = pageNum;
	}
	
	
	public int getPAGE_SIZE() {
		return PAGE_SIZE;
	}
	
	public int getPageNum() {
		return pageNum;
	}
	
	public void setPageNum(int pageNum) {
		if(pageNum > totalPages) pageNum = totalPages;
		this.pageNum = pageNum;
	}
	
	/** 対象リストを取得 */
	public List<T> getFullList() {
		return list;
	}
	
	/** 現在のページのリストを取得 */
	public List<T> getPageList() {
		int start = (pageNum - 1) * PAGE_SIZE;
		int end   = start + PAGE_SIZE;
		if(end > totalElements) end = totalElements;
		List<T> pageList = list.subList(start, end);
		
		return pageList;
	}
	
	public int getTotalElements() {
		return totalElements;
	}
	
	public int getTotalPages() {
		return totalPages;
	}
	
	public boolean isFirst() {
		return pageNum == 1;
	}
	
	public boolean isEnd() {
		return pageNum == totalPages;
	}
}