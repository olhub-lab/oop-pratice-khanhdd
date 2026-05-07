package com.khanh.ordermanagement.dto.response;

import java.util.List;

public class PageResponse<T> {

  private final List<T> content;
  private final long totalElements;
  private final int totalPages;
  private final int pageNumber;
  private final int pageSize;
  private final boolean hasNext;
  private final boolean hasPrevious;

  public PageResponse(List<T> content, long totalElements, int totalPages,
      int pageNumber, int pageSize, boolean hasNext, boolean hasPrevious) {
    this.content = content;
    this.totalElements = totalElements;
    this.totalPages = totalPages;
    this.pageNumber = pageNumber;
    this.pageSize = pageSize;
    this.hasNext = hasNext;
    this.hasPrevious = hasPrevious;
  }

  public List<T> getContent() { return content; }
  public long getTotalElements() { return totalElements; }
  public int getTotalPages() { return totalPages; }
  public int getPageNumber() { return pageNumber; }
  public int getPageSize() { return pageSize; }
  public boolean isHasNext() { return hasNext; }
  public boolean isHasPrevious() { return hasPrevious; }
}