import { Component } from '@angular/core';
import {FormBuilder, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {RegisterRequest} from "../../contracts/interfaces";
import {AuthService} from "../../services/auth.service";
import {catchError, of, switchMap, take, takeUntil} from "rxjs";
import {DestroyableComponent} from "../../../shared/components/destroyable/destroyable.component";
import {ToastrService} from "ngx-toastr";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'eventer-register-main',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule,
    RouterLink
  ],
  providers: [AuthService],
  templateUrl: './register-main.component.html',
  styleUrl: './register-main.component.css'
})
export class RegisterMainComponent extends DestroyableComponent {
  newUserForm = this.formBuilder.group({
    name: ['', Validators.required],
    username: ['', Validators.required],
    password: ['', Validators.required],
    city: ['', Validators.required]
  });

  constructor(private formBuilder: FormBuilder,
              private readonly authService: AuthService,
              private readonly toastrService: ToastrService) {
    super();
  }

  onSubmit() {
    if (!this.newUserForm.valid ||
      !this.newUserForm.controls.name.value ||
      !this.newUserForm.controls.password.value ||
      !this.newUserForm.controls.username.value ||
      !this.newUserForm.controls.city.value) {
      return;
    }

    const newUser: RegisterRequest = {
      name: this.newUserForm.controls.name.value,
      username: this.newUserForm.controls.username.value,
      password: this.newUserForm.controls.password.value,
      city: this.newUserForm.controls.city.value
    };

    this.authService.register(newUser).pipe(
      take(1),
      takeUntil(this.destroyed$),
      switchMap((response) => {
        this.toastrService.success('Registered successfully');
        return of()
      }),
      catchError((error) => {
        if (error?.error?.detail !== undefined) {
          this.toastrService.error(error.error.detail);
          return of();
        }

        this.toastrService.error('Register failed');
        return of();
      })
    ).subscribe();
  }
}
