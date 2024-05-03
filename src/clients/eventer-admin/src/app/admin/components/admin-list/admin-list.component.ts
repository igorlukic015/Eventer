import {Component, OnInit, signal, WritableSignal} from '@angular/core';
import {TablePaginatorComponent} from "../../../shared/components/table-paginator/table-paginator.component";
import {DestroyableComponent} from "../../../shared/components/destroyable/destroyable.component";
import {Router} from "@angular/router";
import {AdminFacade} from "../../+state/facade/admin.facade";
import {takeUntil, withLatestFrom} from "rxjs";
import {Admin} from "../../contracts/interfaces";

@Component({
  selector: 'eventer-admin-admin-list',
  standalone: true,
    imports: [
        TablePaginatorComponent
    ],
  templateUrl: './admin-list.component.html',
  styleUrl: './admin-list.component.css'
})
export class AdminListComponent extends DestroyableComponent implements OnInit {
  public totalPages: WritableSignal<number> = signal(1);
  public admins: WritableSignal<Admin[]> = signal([]);
  public checkedRow: WritableSignal<number> = signal(0);

  constructor(private readonly router: Router,
              private readonly adminFacade: AdminFacade) {
    super();
  }

  onDeleteClicked($event: void) {
    if (this.checkedRow() !== 0) {
      this.adminFacade.deleteAdmin(this.checkedRow());
    }
  }

  pageChanged(currentPage: number): void {
    this.adminFacade.updatePageNumber(currentPage);
  }

  handleCheckClick($event: any, categoryId: number) {
    if (this.checkedRow() === categoryId) {
      this.checkedRow.set(0);
      return;
    }

    this.checkedRow.set(categoryId);
  }

  ngOnInit() {
    this.adminFacade.items$.pipe(
      withLatestFrom(this.adminFacade.totalPages$),
      takeUntil(this.destroyed$)
    ).subscribe(([items, total])=> {
      this.admins.set(items);
      this.totalPages.set(total);
    })
  }
}
