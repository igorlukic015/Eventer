import {Component, OnInit, signal, WritableSignal, Signal, computed} from '@angular/core';
import {EventCategory} from "../../contracts/interfaces";
import {allData} from "../../contracts/mock-data";
import {TablePaginatorComponent} from "../../../shared/table-paginator/table-paginator.component";

@Component({
  selector: 'eventer-admin-event-category-list',
  standalone: true,
  imports: [
    TablePaginatorComponent
  ],
  templateUrl: './event-category-list.component.html',
  styleUrl: './event-category-list.component.css'
})
export class EventCategoryListComponent implements OnInit {
  public totalPages: number = 2;
  public pageSize: number = 10;

  public categories: WritableSignal<EventCategory[]> = signal([]);

  pageChanged(currentPage: number) : void {
    this.categories.set(allData.slice(currentPage * this.pageSize, (currentPage + 1) * this.pageSize));
  }

  ngOnInit() {
    this.categories.set(allData.slice(0, this.pageSize));
  }
}
