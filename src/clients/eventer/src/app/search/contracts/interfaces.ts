import {Image} from "../../shared/contracts/interfaces";

export interface EventCategory {
  id: number;
  name: string;
  description: string;
}

export interface EventData {
  id: number;
  title: string;
  description: string;
  date: Date;
  location: string;
  weatherConditions: string[];
  categories: EventCategory[];
  images: Image[];
}
