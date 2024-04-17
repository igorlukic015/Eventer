import {EventCategory} from "../../event-category/contracts/interfaces";
import {Image} from "../../shared/contracts/interfaces";

export interface Event {
  id: number;
  title: string;
  description: string;
  date: Date;
  location: string;
  weatherConditions: string[];
  categories: EventCategory[];
  images: Image[];
}

export interface EventCreate {
  title: string;
  description: string;
  date: string;
  location: string;
  weatherConditions: string[];
  eventCategories: number[];
}
