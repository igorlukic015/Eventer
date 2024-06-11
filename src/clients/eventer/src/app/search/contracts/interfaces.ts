import {Image, PageRequest} from "../../shared/contracts/interfaces";

export interface EventCategory {
  categoryId: number;
  name: string;
  description: string;
}

export interface EventData {
  eventId: number;
  title: string;
  description: string;
  date: Date;
  location: string;
  weatherConditions: string[];
  categories: EventCategory[];
  images: Image[];
}

export interface ExtendedSearchPageRequest extends PageRequest {
  categoryIds: number[];
  weatherConditions: string[];
}

export interface CommentData {
  id: number;
  text: string;
  eventId: number;
  userId: number;
  userProfileImageUrl: string;
}

export interface CreateCommentRequest {
  text: string;
  eventId: number;
}
