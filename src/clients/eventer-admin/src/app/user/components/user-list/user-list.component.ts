import {Component, OnInit, signal, WritableSignal} from '@angular/core';
import {TablePaginatorComponent} from "../../../shared/components/table-paginator/table-paginator.component";
import {DestroyableComponent} from "../../../shared/components/destroyable/destroyable.component";
import {User} from "../../contracts/interfaces";
import {UserFacade} from "../../+state/facade/user.facade";
import {takeUntil, withLatestFrom} from "rxjs";

@Component({
  selector: 'eventer-admin-user-list',
  standalone: true,
  imports: [
    TablePaginatorComponent
  ],
  templateUrl: './user-list.component.html',
  styleUrl: './user-list.component.css'
})
export class UserListComponent extends DestroyableComponent implements OnInit {
  public totalPages: WritableSignal<number> = signal(1);
  public users: WritableSignal<User[]> = signal([]);
  public checkedRow: WritableSignal<number> = signal(0);

  constructor(private readonly userFacade: UserFacade) {
    super();
  }

  onDeleteClicked($event: void) {
    if (this.checkedRow() !== 0) {
      this.userFacade.deleteUser(this.checkedRow());
    }
  }

  pageChanged(currentPage: number): void {
    this.userFacade.updatePageNumber(currentPage);
  }

  handleCheckClick($event: any, categoryId: number) {
    if (this.checkedRow() === categoryId) {
      this.checkedRow.set(0);
      return;
    }

    this.checkedRow.set(categoryId);
  }

  ngOnInit() {
    this.userFacade.items$.pipe(
      withLatestFrom(this.userFacade.totalPages$),
      takeUntil(this.destroyed$)
    ).subscribe(([items, total])=> {
      this.users.set(items);
      this.totalPages.set(total);
    })
  }
}
