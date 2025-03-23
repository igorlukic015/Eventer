import {Component, Input} from '@angular/core';
import {EventData} from "../../contracts/interfaces";

@Component({
  selector: 'eventer-search-list-item',
  standalone: true,
  imports: [],
  templateUrl: './search-list-item.component.html',
  styleUrl: './search-list-item.component.css'
})
export class SearchListItemComponent {
  @Input({required: true})
  public event: EventData = {
    images: [],
    description: '',
    location: '',
    title: '',
    categories: [],
    eventId: 0,
    date: new Date(),
    weatherConditions: []
  };

}
