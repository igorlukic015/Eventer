import { Component } from '@angular/core';
import {SearchListItemComponent} from "../search-list-item/search-list-item.component";

@Component({
  selector: 'eventer-search-list',
  standalone: true,
  imports: [
    SearchListItemComponent
  ],
  templateUrl: './search-list.component.html',
  styleUrl: './search-list.component.css'
})
export class SearchListComponent {

}
