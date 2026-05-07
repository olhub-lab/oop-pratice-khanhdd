package com.khanh.ordermanagement.dto.response;


import java.util.List;

public class PageResponse<T> {

  private final List<T> content;
  private final int totalElements;
  private final int totalPages;
  private final boolean hasNext;
  private final boolean hasPrevious;
  public PageResponse(List<T> content, int totalElements, int totalPages, boolean hasNext,
      boolean hasPrevious) {
    this.content = content;
    this.totalElements = totalElements;
    this.totalPages = totalPages;
    this.hasNext = hasNext;
    this.hasPrevious = hasPrevious;
  }
  public List<T> getContent() { return content; }
  public int getTotalElements() { return totalElements; }
  public int getTotalPages() { return totalPages; }
  public boolean isHasNext() { return hasNext; }
  public boolean isHasPrevious() { return hasPrevious; }
}