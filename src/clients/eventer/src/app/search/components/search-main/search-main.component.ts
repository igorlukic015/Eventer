import { Component } from '@angular/core';
import {NavBarComponent} from "../../../shared/components/nav-bar/nav-bar.component";
import {SearchListComponent} from "../search-list/search-list.component";

@Component({
  selector: 'eventer-search-main',
  standalone: true,
  imports: [
    NavBarComponent,
    SearchListComponent
  ],
  templateUrl: './search-main.component.html',
  styleUrl: './search-main.component.css'
})
export class SearchMainComponent {

}
