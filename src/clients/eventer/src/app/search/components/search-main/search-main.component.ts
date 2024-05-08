import {Component, OnInit} from '@angular/core';
import {NavBarComponent} from "../../../shared/components/nav-bar/nav-bar.component";
import {SearchListComponent} from "../search-list/search-list.component";
import {FooterComponent} from "../../../shared/components/footer/footer.component";
import {DestroyableComponent} from "../../../shared/components/destroyable/destroyable.component";
import {SearchFacade} from "../../+state/facade/search.facade";
import {takeUntil} from "rxjs";

@Component({
  selector: 'eventer-search-main',
  standalone: true,
  imports: [
    NavBarComponent,
    SearchListComponent,
    FooterComponent
  ],
  providers: [SearchFacade],
  templateUrl: './search-main.component.html',
  styleUrl: './search-main.component.css'
})
export class SearchMainComponent extends DestroyableComponent implements OnInit {
  constructor(private readonly searchFacade: SearchFacade) {
    super();
  }

  ngOnInit() {
    this.searchFacade.pageRequest$.pipe(
      takeUntil(this.destroyed$)
    ).subscribe(() => {
      this.searchFacade.loadEvents();
      this.searchFacade.loadCategories();
    })
  }
}
