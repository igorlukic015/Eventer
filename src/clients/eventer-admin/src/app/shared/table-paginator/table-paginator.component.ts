import {Component, computed, EventEmitter, Input, Output, Signal, signal, WritableSignal} from '@angular/core';

@Component({
  selector: 'eventer-admin-table-paginator',
  standalone: true,
  imports: [],
  templateUrl: './table-paginator.component.html',
  styleUrl: './table-paginator.component.css'
})
export class TablePaginatorComponent {
  @Input()
  public totalPages: number = 1;

  @Input()
  public pageSize: number = 10;

  @Output()
  pageChanged: EventEmitter<number> = new EventEmitter<number>();

  public currentPage: WritableSignal<number> = signal(0);
  public isPreviousDisabled: Signal<boolean> = computed((): boolean => this.currentPage() === 0);
  public isNextDisabled: Signal<boolean> = computed((): boolean => this.currentPage() + 1 === this.totalPages);

  public onPreviousPageClick($event: MouseEvent) {
    if (this.currentPage() === 0) {
      return;
    }

    this.currentPage.update((value) => value - 1)
    this.pageChanged.emit(this.currentPage())
  }

  public onNextPageClick($event: MouseEvent) {
    if (this.currentPage() + 1 === this.totalPages) {
      return;
    }

    this.currentPage.update((value) => value + 1)
    this.pageChanged.emit(this.currentPage())
  }
}
