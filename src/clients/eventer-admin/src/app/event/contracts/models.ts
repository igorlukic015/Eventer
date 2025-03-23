export class WeatherCondition {
  id: number;
  name: string;

  private constructor() {
    this.id = 0;
    this.name = '';
  }

  public static readonly clear: WeatherCondition = {id: 1, name: 'CLEAR'};
  public static readonly rain: WeatherCondition = {id: 2, name: 'RAIN'};
  public static readonly drizzle: WeatherCondition = {id: 3, name: 'DRIZZLE'};
  public static readonly snow: WeatherCondition = {id: 4, name: 'SNOW'};
  public static readonly atmosphericDisturbance: WeatherCondition = {id: 5, name: 'ATMOSPHERIC_DISTURBANCE'};

  public static all(): WeatherCondition[] {
    return [this.clear, this.rain, this.drizzle, this.snow, this.atmosphericDisturbance];
  }

  public static get(name: string): WeatherCondition {
    return this.all().find(c => c.name === name) ?? new WeatherCondition();
  }
}
