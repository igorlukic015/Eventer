import {Component, computed, OnChanges, OnInit, Signal, signal, SimpleChanges, WritableSignal} from '@angular/core';
import {LayoutMainComponent} from "../../../shared/components/layout-main/layout-main.component";
import {NavBarComponent} from "../../../shared/components/nav-bar/nav-bar.component";
import {FormBuilder, ReactiveFormsModule, Validators} from "@angular/forms";
import {EventCategory} from "../../../event-category/contracts/interfaces";
import {EventFacade} from "../../+state/facade/event.facade";
import {DestroyableComponent} from "../../../shared/components/destroyable/destroyable.component";
import {takeUntil} from "rxjs";
import {SelectListComponent} from "../../../shared/components/select-list/select-list.component";
import {SelectListElement} from "../../../shared/contracts/interfaces";
import {WeatherCondition} from "../../contracts/models";
import flatpickr from "flatpickr";
import {EventCreate} from "../../contracts/interfaces";

@Component({
  selector: 'eventer-admin-event-create',
  standalone: true,
  imports: [
    LayoutMainComponent,
    NavBarComponent,
    ReactiveFormsModule,
    SelectListComponent,
  ],
  providers: [EventFacade],
  templateUrl: './event-create.component.html',
  styleUrl: './event-create.component.css'
})
export class EventCreateComponent extends DestroyableComponent implements OnInit {
  newEventForm = this.formBuilder.group({
    title: ['', Validators.required],
    description: ['', Validators.required],
    date: ['', Validators.required],
    location: ['', Validators.required],
  });

  categories: WritableSignal<EventCategory[]> = signal([]);
  selectedCategories: WritableSignal<number[]> = signal([]);
  categoriesElements: WritableSignal<SelectListElement[]> = signal([]);

  selectedWeatherConditions: WritableSignal<WeatherCondition[]> = signal([]);
  weatherConditionsElements: Signal<SelectListElement[]> = computed((): SelectListElement[] =>
    WeatherCondition.all().map((w) => ({id: w.id, value: w.name}))
  );

  uploadedFiles: WritableSignal<File[]> = signal([]);
  readFiles: WritableSignal<any[]> = signal([]);

  isViewChecked: WritableSignal<boolean> = signal(false);

  constructor(private formBuilder: FormBuilder,
              private readonly eventFacade: EventFacade) {
    super();
  }

  onSubmit() {
    const formData = new FormData();

    if (!this.newEventForm.value.title ||
      !this.newEventForm.value.date ||
      !this.newEventForm.value.description ||
      !this.newEventForm.value.location) {
      return;
    }

    const data: EventCreate = {
      title: this.newEventForm.value.title,
      date: this.newEventForm.value.date,
      description: this.newEventForm.value.description,
      location: this.newEventForm.value.location,
      eventCategories: this.selectedCategories(),
      weatherConditions: this.selectedWeatherConditions().map(w => w.name)
    }

    for (let file of this.uploadedFiles()) {
      formData.append('images', file);
    }

    this.eventFacade.createEvent(formData, data);
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

  onFileUpload($event: any) {
    const files = $event.target.files;

    this.uploadedFiles.set(files);

    const fileData: any[] = [];

    for (let file of files) {
      const fileReader = new FileReader();
      fileReader.onload = function (event) {
        fileData.push(event.target?.result);
      }
      fileReader.readAsDataURL(file);
    }

    this.readFiles.set(fileData);
  }

  ngOnInit(): void {
    this.eventFacade.loadCategories();

    this.eventFacade.categories$.pipe(
      takeUntil(this.destroyed$)
    ).subscribe((categories) => {
      if (categories && categories.length > 0) {
        this.categories.set(categories);
        this.categoriesElements.set(categories.map(c => ({id: c.id, value: c.name})));
      }
    })

    flatpickr("#date", {})
  }
}
