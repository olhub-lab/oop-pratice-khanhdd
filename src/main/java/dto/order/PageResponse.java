package dto.order;


import java.util.List;

public class PageResponse<T> {

  public List<T> content;
  public int totalElements;
  public int totalPages;
  public boolean hasNext;
  public boolean hasPrevious;

  public PageResponse(List<T> content, int totalElements, int totalPages, boolean hasNext,
      boolean hasPrevious) {
    this.content = content;
    this.totalElements = totalElements;
    this.totalPages = totalPages;
    this.hasNext = hasNext;
    this.hasPrevious = hasPrevious;
  }
}