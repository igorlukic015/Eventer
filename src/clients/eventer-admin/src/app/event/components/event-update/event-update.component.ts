import {Component, computed, OnInit, Signal, signal, WritableSignal} from '@angular/core';
import {LayoutMainComponent} from "../../../shared/components/layout-main/layout-main.component";
import {NavBarComponent} from "../../../shared/components/nav-bar/nav-bar.component";
import {FormBuilder, ReactiveFormsModule, Validators} from "@angular/forms";
import {SelectListComponent} from "../../../shared/components/select-list/select-list.component";
import {EventFacade} from "../../+state/facade/event.facade";
import {DestroyableComponent} from "../../../shared/components/destroyable/destroyable.component";
import {EventCategory} from "../../../event-category/contracts/interfaces";
import {Image, SelectListElement} from "../../../shared/contracts/interfaces";
import {WeatherCondition} from "../../contracts/models";
import {concatMap, from, map, take, takeUntil, toArray, withLatestFrom} from "rxjs";
import flatpickr from "flatpickr";
import {EventUpdate} from "../../contracts/interfaces";
import {fromPromise} from "rxjs/internal/observable/innerFrom";

@Component({
  selector: 'eventer-admin-event-update',
  standalone: true,
  imports: [
    LayoutMainComponent,
    NavBarComponent,
    ReactiveFormsModule,
    SelectListComponent
  ],
  providers: [EventFacade],
  templateUrl: './event-update.component.html',
  styleUrl: './event-update.component.css'
})
export class EventUpdateComponent extends DestroyableComponent implements OnInit {
  updateEventForm = this.formBuilder.group({
    id: [0, Validators.required],
    title: ['', Validators.required],
    description: ['', Validators.required],
    date: ['', Validators.required],
    location: ['', Validators.required],
  });

  categories: WritableSignal<EventCategory[]> = signal([]);
  selectedCategories: WritableSignal<number[]> = signal([]);
  categoriesElements: Signal<SelectListElement[]> = computed((): SelectListElement[] =>
    this.categories().map(c => ({id: c.id, value: c.name})));

  selectedWeatherConditions: WritableSignal<WeatherCondition[]> = signal([]);
  weatherConditionsElements: Signal<SelectListElement[]> = computed((): SelectListElement[] =>
    WeatherCondition.all().map((w) => ({id: w.id, value: w.name}))
  );

  uploadedImages: WritableSignal<{ imageId: string, file: File }[]> = signal([]);
  readFiles: WritableSignal<any[]> = signal([]);

  isViewChecked: WritableSignal<boolean> = signal(false);

  oldWeatherConditions: WritableSignal<SelectListElement[]> = signal([]);
  oldCategories: WritableSignal<SelectListElement[]> = signal([]);

  constructor(private formBuilder: FormBuilder,
              private readonly eventFacade: EventFacade) {
    super();
  }

  onViewChange($event: any) {
    this.isViewChecked.set(!this.isViewChecked());
  }

  onCategoriesChanged($event: SelectListElement[]) {
    const newList =
      this.categories()
        .filter(element =>
          $event.some(c => c.id === element.id)
        ).map(element => element.id);

    this.selectedCategories.set(newList);
  }

  onWeatherConditionsChanged($event: SelectListElement[]) {
    const newList =
      WeatherCondition.all()
        .filter(element =>
          $event.some(w => w.id === element.id)
        );

    this.selectedWeatherConditions.set(newList);
  }

  onFileUpload($event: Event) {
    const element = $event.currentTarget as HTMLInputElement;

    if (element.files && element.files.length > 0) {
      const files =
        Array.from(element.files)
          .map((f: File) => ({imageId: crypto.randomUUID(), file: f}));

      const newFileList = [...this.uploadedImages(), ...files];

      this.uploadedImages.set(newFileList);

      this.previewImages(newFileList);
    }
  }

  onSubmit() {
    if (
      !this.updateEventForm.value.id ||
      !this.updateEventForm.value.title ||
      !this.updateEventForm.value.date ||
      !this.updateEventForm.value.description ||
      !this.updateEventForm.value.location) {
      return;
    }

    const data: EventUpdate = {
      id: this.updateEventForm.value.id,
      title: this.updateEventForm.value.title,
      date: this.updateEventForm.value.date,
      description: this.updateEventForm.value.description,
      location: this.updateEventForm.value.location,
      eventCategories: this.selectedCategories(),
      weatherConditions: this.selectedWeatherConditions().map(w => w.name)
    }

    const formData = new FormData();

    let imageIds = "";

    for (let image of this.uploadedImages()) {
      imageIds += `${image.imageId};`;
      formData.append('images', image.file);
    }

    imageIds = imageIds.substring(0, imageIds.length - 1);
    formData.append('ids', imageIds);

    this.eventFacade.updateEvent(formData, data);
  }

  removeImage(imageId: string) {
    const filteredImages = this.uploadedImages().filter(f => f.imageId !== imageId);
    this.uploadedImages.set(filteredImages);
    this.previewImages(filteredImages);
  }

  ngOnInit(): void {
    this.eventFacade.loadCategories();

    this.eventFacade.categories$.pipe(
      takeUntil(this.destroyed$)
    ).subscribe((categories) => {
      this.categories.set(categories);
    })

    this.eventFacade.items$.pipe(
      withLatestFrom(this.eventFacade.selectedEventId$),
      take(1),
      takeUntil(this.destroyed$)
    ).subscribe(([events, id]) => {
      const selectedEvent = events.find(e => e.id === id);
      if (selectedEvent) {
        this.updateEventForm.patchValue({
            id: selectedEvent.id,
            title: selectedEvent.title,
            date: `${selectedEvent.date.getFullYear()}-${selectedEvent.date.getMonth() + 1}-${selectedEvent.date.getDate()}`,
            description: selectedEvent.description,
            location: selectedEvent.location
          },
          {emitEvent: false}
        );

        const conditions = selectedEvent.weatherConditions.map(e => WeatherCondition.get(e));

        this.selectedWeatherConditions.set(conditions);
        this.oldWeatherConditions.set(conditions.map(c => ({id: c.id, value: c.name})));

        this.selectedCategories.set(selectedEvent.categories.map(c => c.id));
        this.oldCategories.set(selectedEvent.categories.map(c => ({id: c.id, value: c.name})))

        this.fetchImages(selectedEvent.images)
      }
    })

    flatpickr("#date", {})
  }

  private previewImages(images: { file: File, imageId: string }[]) {
    const fileData: any[] = [];

    for (let image of images) {
      const fileReader = new FileReader();
      fileReader.onload = function (event) {
        fileData.push({data: event.target?.result, imageId: image.imageId});
      }
      fileReader.readAsDataURL(image.file);
    }

    this.readFiles.set(fileData);
  }

  private fetchImages(images: Image[]) {
    from(images).pipe(
      concatMap((image) =>
        fromPromise(fetch(image.url)).pipe(
          concatMap((response) =>
            fromPromise(response.blob()).pipe(
              map((blob) => ({blob, imageName: image.name}))
            )
          )
        )
      ),
      map(({blob, imageName}) => ({
        imageId: crypto.randomUUID(),
        file: new File([blob], imageName, {type: blob.type})
      })),
      toArray()
    ).subscribe((images) => {
      this.uploadedImages.set(images);
      this.previewImages(images);
    })
  }
}
