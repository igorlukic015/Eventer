import {Component, OnInit} from '@angular/core';
import {LayoutMainComponent} from "../../../shared/components/layout-main/layout-main.component";
import {FormBuilder, ReactiveFormsModule, Validators} from "@angular/forms";
import {EventCategoryFacade} from "../../+state/facade/event-category.facade";
import {EventCategory} from "../../contracts/interfaces";
import {take, takeUntil, withLatestFrom} from "rxjs";
import {DestroyableComponent} from "../../../shared/components/destroyable/destroyable.component";
import {NavbarComponent} from "../../../shared/components/navbar/navbar.component";

@Component({
  selector: 'eventer-admin-event-category-update',
  standalone: true,
  imports: [
    LayoutMainComponent,
    ReactiveFormsModule,
    NavbarComponent
  ],
  providers: [EventCategoryFacade],
  templateUrl: './event-category-update.component.html',
  styleUrl: './event-category-update.component.css'
})
export class EventCategoryUpdateComponent extends DestroyableComponent implements OnInit {
  updateForm = this.formBuilder.group({
    id: [0, Validators.required],
    name: ['', Validators.required],
    description: ['', Validators.required]
  });

  constructor(private formBuilder: FormBuilder,
              private readonly eventCategoryFacade: EventCategoryFacade) {
    super();
  }

  ngOnInit(): void {
    this.eventCategoryFacade.items$.pipe(
      withLatestFrom(this.eventCategoryFacade.selectedCategoryId$),
      take(1), takeUntil(this.destroyed$)
    ).subscribe(([categories, id]) => {
      const selectedCategory = categories.find(c => c.id === id);
      if (selectedCategory) {
        this.updateForm.patchValue({
            id: selectedCategory.id,
            name: selectedCategory.name,
            description: selectedCategory.description
          },
          {emitEvent: false}
        )
      }
    });
  }

  onSubmit() {
    if (!this.updateForm.valid ||
      this.updateForm.controls.id.value === null ||
      this.updateForm.controls.name.value === null ||
      this.updateForm.controls.description.value === null
    ) {
      return;
    }

    const updatedCategory: EventCategory = {
      id: this.updateForm.controls.id.value,
      name: this.updateForm.controls.name.value,
      description: this.updateForm.controls.description.value
    }

    this.eventCategoryFacade.updateCategory(updatedCategory);
  }
}
