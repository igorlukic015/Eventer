import {Component, OnInit, signal, WritableSignal} from '@angular/core';
import {NavBarComponent} from "../../../shared/components/nav-bar/nav-bar.component";
import {ReactiveFormsModule} from "@angular/forms";
import {DestroyableComponent} from "../../../shared/components/destroyable/destroyable.component";
import {EventCategory, EventData} from "../../../search/contracts/interfaces";
import {SubscriptionsService} from "../../services/subscriptions.service";
import {ToastrService} from "ngx-toastr";
import {catchError, combineLatest, of, take, takeUntil} from "rxjs";
import {SearchListItemComponent} from "../../../search/components/search-list-item/search-list-item.component";
import {TablePaginatorComponent} from "../../../shared/components/table-paginator/table-paginator.component";

@Component({
  selector: 'eventer-subscriptions',
  standalone: true,
  imports: [
    NavBarComponent,
    ReactiveFormsModule,
    SearchListItemComponent,
    TablePaginatorComponent
  ],
  templateUrl: './subscriptions.component.html',
  styleUrl: './subscriptions.component.css'
})
export class SubscriptionsComponent extends DestroyableComponent implements OnInit {
  public totalPages: WritableSignal<number> = signal(0);
  public eventsPageNumber = signal(0);
  public events: WritableSignal<EventData[]> = signal([]);
  public categories: WritableSignal<EventCategory[]> = signal([]);
  public subscribedCategoryIds: WritableSignal<number[]> = signal([]);

  constructor(private readonly subscriptionsService: SubscriptionsService,
              private readonly toastrService: ToastrService) {
    super();
  }

  handleSubscribedCategoryChange(categoryId: number) {
    this.subscriptionsService.subscribeToCategory(categoryId).pipe(
      take(1),
      takeUntil(this.destroyed$),
      catchError(error => {
        this.toastrService.error('Unable to subscribe to category')
        return of()
      })
    ).subscribe();
  }

  handlePageChanged(currentPage: number): void {
    this.eventsPageNumber.set(currentPage)
    this.getEvents()
  }

  ngOnInit(): void {
    const categoriesQuery$ = this.subscriptionsService.getEventCategories().pipe(
      take(1),
      takeUntil(this.destroyed$),
      catchError(error => {
        this.toastrService.error('Categories not found');
        return of()
      })
    )

    const subscribedCategoriesQuery$ = this.subscriptionsService.getSubscribedCategoryIds().pipe(
      take(1),
      takeUntil(this.destroyed$),
      catchError(error => {
        this.toastrService.error('Categories not found');
        return of()
      }),
    )

    combineLatest([categoriesQuery$, subscribedCategoriesQuery$]).pipe(
      take(1),
      takeUntil(this.destroyed$),
    ).subscribe(([categories, subscribedCategoryIds]) => {
        this.subscribedCategoryIds.set(subscribedCategoryIds);
        this.categories.set(categories);
    })

    this.getEvents()
  }

  private getEvents() {
    this.subscriptionsService.getEvents(this.eventsPageNumber()).pipe(
      take(1),
      takeUntil(this.destroyed$),
      catchError(error => {
        this.toastrService.error('Events not found');
        return of();
      })
    ).subscribe((pagedResponse) => {
      this.totalPages.set(pagedResponse.totalPages);
      this.events.set(pagedResponse.content);
    })
  }

}
