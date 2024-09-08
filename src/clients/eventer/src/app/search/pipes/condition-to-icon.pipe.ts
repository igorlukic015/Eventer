import { Pipe, PipeTransform } from '@angular/core';
import {WeatherCondition} from "../../shared/contracts/models";

@Pipe({
  name: 'conditionToIcon',
  standalone: true
})
export class ConditionToIconPipe implements PipeTransform {

  transform(value: string, ...args: unknown[]): string {
    switch (value) {
      case WeatherCondition.clear.name:
        return 'https://openweathermap.org/img/wn/01d.png';
      case WeatherCondition.rain.name:
        return "https://openweathermap.org/img/wn/09d.png";
      case WeatherCondition.drizzle.name:
        return "https://openweathermap.org/img/wn/10d.png";
      case WeatherCondition.snow.name:
        return "https://openweathermap.org/img/wn/13d.png";
      case WeatherCondition.atmosphericDisturbance.name:
        return "https://openweathermap.org/img/wn/50d.png"
    }

    return 'https://openweathermap.org/img/wn/01d.png';
  }

}
