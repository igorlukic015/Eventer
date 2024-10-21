import {ActionType, ListenedEntity, SortDirection} from "./models";

export interface Image {
  id: number,
  url: string;
  name: string;
}

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

export interface Message {
  name: string;
  dispatchedAt: Date;
  entityType: ListenedEntity;
  action: ActionType;
  data: any;
}

export interface UpdateEntityData {
  actionType: ActionType,
  entityType: ListenedEntity;
  data: any,
}

export interface Forecast {
  region: string;
  date: string;
  weather: number;
  icon: string;
  temp: number;
  min_temp: number;
  max_temp: number;
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
