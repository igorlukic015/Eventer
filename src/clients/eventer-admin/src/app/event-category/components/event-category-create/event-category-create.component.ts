import { Component } from '@angular/core';
import {LayoutMainComponent} from "../../../shared/components/layout-main/layout-main.component";
import {FormBuilder, ReactiveFormsModule, Validators} from "@angular/forms";
import {EventCategoryCreate} from "../../contracts/interfaces";
import {EventCategoryFacade} from "../../+state/facade/event-category.facade";

@Component({
  selector: 'eventer-admin-event-category-create',
  standalone: true,
  imports: [
    LayoutMainComponent,
    ReactiveFormsModule
  ],
  providers:[EventCategoryFacade],
  templateUrl: './event-category-create.component.html',
  styleUrl: './event-category-create.component.css'
})
export class EventCategoryCreateComponent {
  newCategoryForm = this.formBuilder.group({
    name: ['', Validators.required],
    description: ['', Validators.required]
  });

  constructor(private formBuilder: FormBuilder,
              private readonly eventCategoryFacade: EventCategoryFacade) {
  }

  onSubmit() {
    if (!this.newCategoryForm.valid){
      return;
    }

    const newCategory: EventCategoryCreate = {
      name: this.newCategoryForm.controls.name.value,
      description: this.newCategoryForm.controls.description.value
    }

    this.eventCategoryFacade.createCategory(newCategory);
  }
}
