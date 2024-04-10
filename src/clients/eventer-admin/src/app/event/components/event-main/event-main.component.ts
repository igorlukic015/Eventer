import { Component } from '@angular/core';
import {LayoutMainComponent} from "../../../shared/components/layout-main/layout-main.component";
import {NavbarComponent} from "../../../shared/components/navbar/navbar.component";

@Component({
  selector: 'eventer-admin-event-main',
  standalone: true,
  imports: [
    LayoutMainComponent,
    NavbarComponent
  ],
  templateUrl: './event-main.component.html',
  styleUrl: './event-main.component.css'
})
export class EventMainComponent {

}
