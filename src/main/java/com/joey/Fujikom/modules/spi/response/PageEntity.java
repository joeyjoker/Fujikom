package com.joey.Fujikom.modules.spi.response;

import java.util.ArrayList;
import java.util.List;

public class PageEntity<T> {
	
	private Integer maxSize;
	private Integer maxPage;
	private List<T> list = new ArrayList<T>(); 

	private Integer pageIndex;

	public Integer getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(Integer maxSize) {
		this.maxSize = maxSize;
	}

	public Integer getMaxPage() {
		return maxPage;
	}

	public void setMaxPage(Integer maxPage) {
		this.maxPage = maxPage;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public Integer getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}
	
	
}
