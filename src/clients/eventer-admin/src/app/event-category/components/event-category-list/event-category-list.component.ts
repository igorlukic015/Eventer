import {Component, OnInit, signal, WritableSignal} from '@angular/core';
import {EventCategory} from "../../contracts/interfaces";
import {TablePaginatorComponent} from "../../../shared/components/table-paginator/table-paginator.component";
import {EventCategoryFacade} from "../../+state/facade/event-category.facade";
import {take, takeUntil, withLatestFrom} from "rxjs";
import {DestroyableComponent} from "../../../shared/components/destroyable/destroyable.component";

@Component({
  selector: 'eventer-admin-event-category-list',
  standalone: true,
  imports: [
    TablePaginatorComponent,
  ],
  templateUrl: './event-category-list.component.html',
  styleUrl: './event-category-list.component.css'
})
export class EventCategoryListComponent extends DestroyableComponent implements OnInit {
  public totalPages: WritableSignal<number> = signal(1);
  public categories: WritableSignal<EventCategory[]> = signal([]);

  constructor(private readonly eventCategoryFacade: EventCategoryFacade) {
    super();
  }

  pageChanged(currentPage: number): void {
    this.eventCategoryFacade.updatePageNumber(currentPage);
  }

  ngOnInit() {
    this.eventCategoryFacade.items$.pipe(
      withLatestFrom(this.eventCategoryFacade.totalPages$),
      takeUntil(this.destroyed$)
    ).subscribe(([items, total]) => {
      this.categories.set(items);
      this.totalPages.set(total);
    })
  }
}
