import { Component } from '@angular/core';
import {FormBuilder, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {Register} from "../../contracts/interfaces";

@Component({
  selector: 'eventer-register-main',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule
  ],
  templateUrl: './register-main.component.html',
  styleUrl: './register-main.component.css'
})
export class RegisterMainComponent {
  newUserForm = this.formBuilder.group({
    username: ['', Validators.required],
    password: ['', Validators.required]
  });

  constructor(private formBuilder: FormBuilder) {
  }

  onSubmit() {
    if (!this.newUserForm.valid ||
      !this.newUserForm.controls.password.value ||
      !this.newUserForm.controls.username.value) {
      return;
    }

    const newUser: Register = {
      username: this.newUserForm.controls.username.value,
      password: this.newUserForm.controls.password.value
    };

    // this.adminFacade.registerAdmin(newAdmin);
  }
}
