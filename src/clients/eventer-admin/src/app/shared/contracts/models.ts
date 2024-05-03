export class SortDirection {
  public static readonly ascending: string = 'ASC';
  public static readonly descending: string = 'DESC';
}

export class ListenedEntity {
  public static readonly event: string = 'Event';
  public static readonly eventCategory: string = 'EventCategory';
}

export class ActionType {
  public static readonly created: string = 'CREATED';
  public static readonly updated: string = 'UPDATED';
  public static readonly deleted: string = 'DELETED';
}

export class Role {
  public static readonly eventManager: string = 'EVENT_MANAGER';
  public static readonly administrator: string = 'ADMINISTRATOR';
}
