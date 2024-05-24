import {Component, OnInit, signal, WritableSignal} from '@angular/core';
import {SearchListItemComponent} from "../search-list-item/search-list-item.component";
import {DestroyableComponent} from "../../../shared/components/destroyable/destroyable.component";
import {Router} from "@angular/router";
import {SearchFacade} from "../../+state/facade/search.facade";
import {takeUntil, withLatestFrom} from "rxjs";
import {EventCategory, EventData} from "../../contracts/interfaces";
import {WeatherCondition} from "../../../shared/contracts/models";

@Component({
  selector: 'eventer-search-list',
  standalone: true,
  imports: [
    SearchListItemComponent
  ],
  providers: [SearchFacade],
  templateUrl: './search-list.component.html',
  styleUrl: './search-list.component.css'
})
export class SearchListComponent extends DestroyableComponent implements OnInit {
  public totalPages: WritableSignal<number> = signal(1);
  public events: WritableSignal<EventData[]> = signal([]);
  public categories: WritableSignal<EventCategory[]> = signal([])
  public areFiltersOpen: WritableSignal<boolean> = signal(false);

  constructor(private readonly router: Router,
              private readonly searchFacade: SearchFacade) {
    super();
  }

  filtersClick() {
    this.areFiltersOpen.set(!this.areFiltersOpen());
  }

  handleCategoryUpdate($event: any, categoryId: number) {
    this.searchFacade.updateFilterCategories(categoryId, $event.target.checked);
  }

  handleConditionUpdate($event: any, conditionName: string) {
    this.searchFacade.updateFilterConditions(conditionName, $event.target.checked);
  }

  ngOnInit() {
    this.searchFacade.events$.pipe(
      withLatestFrom(this.searchFacade.totalPages$),
      takeUntil(this.destroyed$)
    ).subscribe(([events, total]) => {
      this.events.set(events);
      this.totalPages.set(total);
    })

    this.searchFacade.categories$.pipe(
      takeUntil(this.destroyed$)
    ).subscribe((categories) => {
      this.categories.set(categories);
    })
  }

    protected readonly WeatherCondition = WeatherCondition;
}
