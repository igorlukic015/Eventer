export interface EventCategory {
  id: number;
  name: string;
  description: string;
}

export interface EventCategoryCreate {
  name: string | null;
  description: string | null;
}
