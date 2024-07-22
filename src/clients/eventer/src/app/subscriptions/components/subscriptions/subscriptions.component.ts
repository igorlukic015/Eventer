import {Component, OnInit, signal, WritableSignal} from '@angular/core';
import {NavBarComponent} from "../../../shared/components/nav-bar/nav-bar.component";
import {ReactiveFormsModule} from "@angular/forms";
import {DestroyableComponent} from "../../../shared/components/destroyable/destroyable.component";
import {EventCategory, EventData} from "../../../search/contracts/interfaces";
import {SubscriptionsService} from "../../services/subscriptions.service";
import {ToastrService} from "ngx-toastr";
import {catchError, combineLatest, of, take, takeUntil} from "rxjs";

@Component({
  selector: 'eventer-subscriptions',
  standalone: true,
  imports: [
    NavBarComponent,
    ReactiveFormsModule
  ],
  templateUrl: './subscriptions.component.html',
  styleUrl: './subscriptions.component.css'
})
export class SubscriptionsComponent extends DestroyableComponent implements OnInit {
  public totalPages: WritableSignal<number> = signal(1);
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
  }

}
