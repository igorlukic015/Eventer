import {AfterViewInit, Component, OnInit, signal, Signal, WritableSignal} from '@angular/core';
import {FooterComponent} from "../../../shared/components/footer/footer.component";
import {NavBarComponent} from "../../../shared/components/nav-bar/nav-bar.component";
import {SearchListComponent} from "../search-list/search-list.component";
import {DestroyableComponent} from "../../../shared/components/destroyable/destroyable.component";
import {SearchFacade} from "../../+state/facade/search.facade";
import {take, takeUntil, withLatestFrom} from "rxjs";
import {Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import {EventData} from "../../contracts/interfaces";
import {NgForOf, NgIf} from "@angular/common";

@Component({
  selector: 'eventer-search-details',
  standalone: true,
  imports: [
    FooterComponent,
    NavBarComponent,
    SearchListComponent,
    NgForOf,
    NgIf
  ],
  providers: [SearchFacade],
  templateUrl: './search-details.component.html',
  styleUrl: './search-details.component.css'
})
export class SearchDetailsComponent extends DestroyableComponent implements OnInit, AfterViewInit {
  event: WritableSignal<EventData> = signal({
    description: '',
    location: '',
    images: [],
    categories: [],
    title: '',
    eventId: 0,
    weatherConditions: [],
    date: new Date()
  })

  lastImageIndex: WritableSignal<number> = signal(0);

  constructor(private readonly searchFacade: SearchFacade,
              private readonly router: Router,
              private readonly toastrService: ToastrService) {
    super();
  }

  ngOnInit() {
    this.searchFacade.events$.pipe(
      take(1),
      takeUntil(this.destroyed$),
      withLatestFrom(this.searchFacade.selectedEventId$)
    ).subscribe(([events, selectedEventId]) => {
      const foundEvent = events.find(e => e.eventId === selectedEventId);

      if (foundEvent === undefined) {
        this.toastrService.error('Event not found')
        this.router.navigate(['']);
        return;
      }

      this.event.set(foundEvent);
    })
  }

  ngAfterViewInit() {
    const carousel = document.getElementById('imageCarousel');

    if (carousel) {
      carousel.addEventListener('wheel', (event) => {
        event.preventDefault();

        carousel.scrollBy({
          left: event.deltaY < 0 ? -300 : 300,
          behavior: 'smooth'
        });
      });
    }
  }
}
