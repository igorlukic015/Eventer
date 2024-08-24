import { Component } from '@angular/core';
import {FormBuilder, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {Router, RouterLink} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import {catchError, of, switchMap, take, takeUntil} from "rxjs";
import {DestroyableComponent} from "../../../shared/components/destroyable/destroyable.component";
import {AuthService} from "../../services/auth.service";
import {LoginRequest} from "../../contracts/interfaces";

@Component({
  selector: 'eventer-login-main',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule,
    RouterLink,
  ],
  providers: [AuthService],
  templateUrl: './login-main.component.html',
  styleUrl: './login-main.component.css'
})
export class LoginMainComponent extends DestroyableComponent {
  loginForm = this.formBuilder.group({
    username: ['', Validators.required],
    password: ['', Validators.required]
  });

  constructor(private formBuilder: FormBuilder,
              private readonly authService: AuthService,
              private readonly router: Router,
              private readonly toastrService: ToastrService) {
    super();
  }

  handleLoginClick($event: any) {
    if (!this.loginForm.valid ||
      !this.loginForm.controls.username.value ||
      !this.loginForm.controls.password.value) {
      return;
    }

    const loginRequest: LoginRequest = {
      username: this.loginForm.controls.username.value,
      password: this.loginForm.controls.password.value
    }

    this.authService.authenticate(loginRequest)
      .pipe(
        take(1),
        takeUntil(this.destroyed$),
        switchMap(response => {
          localStorage.setItem('token', response.accessToken);
          localStorage.setItem('profileImageUrl', response.userProfileImageUrl);
          localStorage.setItem('username', loginRequest.username)
          this.toastrService.success('Login successful');

          this.router.navigate([''])
          return of();
        }),
        catchError((error) => {
          if (error?.error?.detail !== undefined) {
            this.toastrService.error(error.error.detail);
            return of();
          }

          this.toastrService.error('Login failed')
          return of();
        })
      ).subscribe();
  }
}
