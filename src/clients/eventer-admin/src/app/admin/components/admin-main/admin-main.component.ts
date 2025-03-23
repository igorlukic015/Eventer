import {Component, OnInit} from '@angular/core';
import {NavBarComponent} from "../../../shared/components/nav-bar/nav-bar.component";
import {LayoutMainComponent} from "../../../shared/components/layout-main/layout-main.component";
import {ActionBarComponent} from "../../../shared/components/action-bar/action-bar.component";
import {AdminListComponent} from "../admin-list/admin-list.component";
import {DestroyableComponent} from "../../../shared/components/destroyable/destroyable.component";
import {AdminFacade} from "../../+state/facade/admin.facade";
import {takeUntil} from "rxjs";

@Component({
  selector: 'eventer-admin-admin-main',
  standalone: true,
  imports: [
    NavBarComponent,
    LayoutMainComponent,
    ActionBarComponent,
    AdminListComponent
  ],
  providers: [AdminFacade],
  templateUrl: './admin-main.component.html',
  styleUrl: './admin-main.component.css'
})
export class AdminMainComponent extends DestroyableComponent implements OnInit {

  constructor(private readonly adminFacade: AdminFacade) {
    super();
  }

  onSearchTermChanged(searchTerm: string) {
    this.adminFacade.updateSearchTerm(searchTerm);
  }

  ngOnInit() {
    this.adminFacade.pageRequest$.pipe(
      takeUntil(this.destroyed$)
    ).subscribe(() => {
      this.adminFacade.loadAdmins();
    })
  }
}
