import { Component } from '@angular/core';
import {NavBarComponent} from "../../../shared/components/nav-bar/nav-bar.component";

@Component({
  selector: 'eventer-search-main',
  standalone: true,
  imports: [
    NavBarComponent
  ],
  templateUrl: './search-main.component.html',
  styleUrl: './search-main.component.css'
})
export class SearchMainComponent {

}
