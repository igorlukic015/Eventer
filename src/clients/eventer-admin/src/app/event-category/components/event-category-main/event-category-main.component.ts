import { Component } from '@angular/core';
import {LayoutMainComponent} from "../../../shared/layout-main/layout-main.component";
import {FooterComponent} from "../../../shared/footer/footer.component";
import {NavbarComponent} from "../../../shared/navbar/navbar.component";

@Component({
  selector: 'eventer-admin-event-category-main',
  standalone: true,
    imports: [
        LayoutMainComponent,
        FooterComponent,
        NavbarComponent
    ],
  templateUrl: './event-category-main.component.html',
  styleUrl: './event-category-main.component.css'
})
export class EventCategoryMainComponent {

}
