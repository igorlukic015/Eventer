import {SortDirection} from "./models";

export interface PageRequest {
  page: number;
  size: number;
  searchTerm: string;
  sort: PageRequestSortProperties;
}

export interface PageRequestSortProperties {
  attributeNames: string[];
  sortDirection: SortDirection;
}

export interface PagedResponse {
  content: any[];
  pageable: Pageable;
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  numberOfElements: number,
  sort: PageableSortProperties;
  last: boolean;
  first: boolean;
  empty: boolean;
}

interface PageableSortProperties {
  empty: boolean;
  sorted: boolean;
  unsorted: boolean;
}

interface Pageable {
  pageNumber: number;
  pageSize: number;
  sort: PageableSortProperties;
  offset: number;
  paged: boolean;
  unpaged: boolean;
}
