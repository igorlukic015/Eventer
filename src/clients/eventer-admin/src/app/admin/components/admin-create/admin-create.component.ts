import {Component} from '@angular/core';
import {FormBuilder, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {AdminFacade} from "../../+state/facade/admin.facade";
import {LayoutMainComponent} from "../../../shared/components/layout-main/layout-main.component";
import {NavBarComponent} from "../../../shared/components/nav-bar/nav-bar.component";
import {Register} from "../../contracts/interfaces";

@Component({
  selector: 'eventer-admin-admin-create',
  standalone: true,
  imports: [
    LayoutMainComponent,
    NavBarComponent,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [AdminFacade],
  templateUrl: './admin-create.component.html',
  styleUrl: './admin-create.component.css'
})
export class AdminCreateComponent {
  newAdminForm = this.formBuilder.group({
    username: ['', Validators.required],
    password: ['', Validators.required]
  });

  constructor(private formBuilder: FormBuilder,
              private readonly adminFacade: AdminFacade) {
  }

  onSubmit() {
    if (!this.newAdminForm.valid ||
      !this.newAdminForm.controls.password.value ||
      !this.newAdminForm.controls.username.value) {
      return;
    }

    const newAdmin: Register = {
      username: this.newAdminForm.controls.username.value,
      password: this.newAdminForm.controls.password.value
    };

    this.adminFacade.registerAdmin(newAdmin);
  }
}
