import {EventCategory} from "../../event-category/contracts/interfaces";
import {Image} from "../../shared/contracts/interfaces";

export interface IEvent {
  id: number;
  title: string;
  description: string;
  location: string;
  weatherConditions: string[];
  categories: EventCategory[];
  images: Image[];
}
