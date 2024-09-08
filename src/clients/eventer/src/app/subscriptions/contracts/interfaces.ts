import {Image} from "../../shared/contracts/interfaces";

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
