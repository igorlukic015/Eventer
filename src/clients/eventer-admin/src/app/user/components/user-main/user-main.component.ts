import {Component, OnInit} from '@angular/core';
import {NavBarComponent} from "../../../shared/components/nav-bar/nav-bar.component";
import {LayoutMainComponent} from "../../../shared/components/layout-main/layout-main.component";
import {ActionBarComponent} from "../../../shared/components/action-bar/action-bar.component";
import {UserListComponent} from "../user-list/user-list.component";
import {skip, take, takeUntil} from "rxjs";
import {UserFacade} from "../../+state/facade/user.facade";
import {DestroyableComponent} from "../../../shared/components/destroyable/destroyable.component";

@Component({
  selector: 'eventer-admin-user-main',
  standalone: true,
  imports: [
    NavBarComponent,
    LayoutMainComponent,
    ActionBarComponent,
    UserListComponent
  ],
  providers: [UserFacade],
  templateUrl: './user-main.component.html',
  styleUrl: './user-main.component.css'
})
export class UserMainComponent extends DestroyableComponent implements OnInit {

  constructor(private readonly userFacade: UserFacade) {
    super();
  }

  onSearchTermChanged(searchTerm: string) {
    this.userFacade.updateSearchTerm(searchTerm);
  }

  ngOnInit() {
    this.userFacade.loadUsers();

    this.userFacade.pageRequest$.pipe(
      takeUntil(this.destroyed$),
    ).subscribe(() => {
      this.userFacade.searchUsers();
    })
  }
}
