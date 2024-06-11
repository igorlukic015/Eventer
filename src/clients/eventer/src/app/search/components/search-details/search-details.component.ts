import {AfterViewInit, Component, OnInit, signal, WritableSignal} from '@angular/core';
import {FooterComponent} from "../../../shared/components/footer/footer.component";
import {NavBarComponent} from "../../../shared/components/nav-bar/nav-bar.component";
import {SearchListComponent} from "../search-list/search-list.component";
import {DestroyableComponent} from "../../../shared/components/destroyable/destroyable.component";
import {SearchFacade} from "../../+state/facade/search.facade";
import {take, takeUntil, withLatestFrom} from "rxjs";
import {Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import {CommentData, EventData} from "../../contracts/interfaces";
import {Location, NgForOf, NgIf} from "@angular/common";
import {CommentSectionComponent} from "../../../shared/components/comment-section/comment-section.component";
import {ImageCarouselComponent} from "../../../shared/components/image-carousel/image-carousel.component";

@Component({
  selector: 'eventer-search-details',
  standalone: true,
  imports: [
    FooterComponent,
    NavBarComponent,
    SearchListComponent,
    NgForOf,
    NgIf,
    CommentSectionComponent,
    ImageCarouselComponent
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

  comments: WritableSignal<CommentData[]> = signal([])

  constructor(private readonly searchFacade: SearchFacade,
              private readonly router: Router,
              private readonly toastrService: ToastrService,
              private readonly location: Location) {
    super();
  }

  handleBackClick() {
    this.location.back()
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

    this.searchFacade.selectedEventId$.pipe(
      takeUntil(this.destroyed$)
    ).subscribe((eventId) => {
      this.searchFacade.getComments();
    })

    this.searchFacade.comments$.pipe(
      takeUntil(this.destroyed$),
    ).subscribe((comments) => {
      this.comments.set(comments)
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
