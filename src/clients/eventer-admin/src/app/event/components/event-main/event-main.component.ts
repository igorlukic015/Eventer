import { Component } from '@angular/core';
import {LayoutMainComponent} from "../../../shared/components/layout-main/layout-main.component";
import {NavBarComponent} from "../../../shared/components/nav-bar/nav-bar.component";

@Component({
  selector: 'eventer-admin-event-main',
  standalone: true,
  imports: [
    LayoutMainComponent,
    NavBarComponent
  ],
  templateUrl: './event-main.component.html',
  styleUrl: './event-main.component.css'
})
export class EventMainComponent {

}
