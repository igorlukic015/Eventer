import {Component, OnInit, signal, WritableSignal} from '@angular/core';
import {EventCategory} from "../../contracts/interfaces";
import {TablePaginatorComponent} from "../../../shared/components/table-paginator/table-paginator.component";
import {EventCategoryService} from "../../services/event-category.service";
import {PagedResponse, PageRequest} from "../../../shared/contracts/interfaces";
import {SortDirection} from "../../../shared/contracts/models";
import {HttpClientModule} from "@angular/common/http";

@Component({
  selector: 'eventer-admin-event-category-list',
  standalone: true,
  imports: [
    TablePaginatorComponent,
    HttpClientModule
  ],
  providers: [EventCategoryService, HttpClientModule],
  templateUrl: './event-category-list.component.html',
  styleUrl: './event-category-list.component.css'
})
export class EventCategoryListComponent implements OnInit {
  public pageSize: number = 5;

  public totalPages: WritableSignal<number> = signal(1);
  public categories: WritableSignal<EventCategory[]> = signal([]);

  constructor(public eventCategoryService: EventCategoryService) {
  }

  pageChanged(currentPage: number): void {
    this.getData(currentPage);
  }

  getData(page: number) {
    const pageRequest: PageRequest = {
      page: page,
      size: this.pageSize,
      sort: {sortDirection: SortDirection.ascending, attributeNames: ['name']}
    }
    this.eventCategoryService.getEventCategories(pageRequest).subscribe((response:PagedResponse) => {
      this.categories.set(response.content);
      this.totalPages.set(response.totalPages);
    });
  }

  ngOnInit() {
    this.getData(0);
  }
}
