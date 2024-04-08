import {Component, OnInit} from '@angular/core';
import {FooterComponent} from "../../../shared/components/footer/footer.component";
import {NavbarComponent} from "../../../shared/components/navbar/navbar.component";
import {EventCategoryListComponent} from "../event-category-list/event-category-list.component";
import {ActionBarComponent} from "../../../shared/components/action-bar/action-bar.component";
import {LayoutMainComponent} from "../../../shared/components/layout-main/layout-main.component";
import {DestroyableComponent} from "../../../shared/components/destroyable/destroyable.component";
import {EventCategoryFacade} from "../../+state/facade/event-category.facade";

@Component({
  selector: 'eventer-admin-event-category-main',
  standalone: true,
  imports: [
    FooterComponent,
    NavbarComponent,
    EventCategoryListComponent,
    ActionBarComponent,
    LayoutMainComponent
  ],
  providers: [EventCategoryFacade],
  templateUrl: './event-category-main.component.html',
  styleUrl: './event-category-main.component.css'
})
export class EventCategoryMainComponent extends DestroyableComponent implements OnInit {

  constructor(private readonly eventCategoryFacade: EventCategoryFacade) {
    super();
  }

  ngOnInit(): void {
    this.eventCategoryFacade.loadEventCategories();
  }
}
