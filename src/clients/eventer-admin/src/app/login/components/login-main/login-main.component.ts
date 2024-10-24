import { Component } from '@angular/core';
import {FormBuilder, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {LayoutMainComponent} from "../../../shared/components/layout-main/layout-main.component";
import {LoginService} from "../../services/login.service";
import {LoginRequest} from "../../contracts/interfaces";
import {DestroyableComponent} from "../../../shared/components/destroyable/destroyable.component";
import {catchError, of, switchMap, take, takeUntil} from "rxjs";
import {Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import {Role} from "../../../shared/contracts/models";

@Component({
  selector: 'eventer-admin-login-main',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule,
    LayoutMainComponent
  ],
  providers: [LoginService],
  templateUrl: './login-main.component.html',
  styleUrl: './login-main.component.css'
})
export class LoginMainComponent extends DestroyableComponent {
  loginForm = this.formBuilder.group({
    username: ['', Validators.required],
    password: ['', Validators.required]
  });

  constructor(private formBuilder: FormBuilder,
              private readonly loginService: LoginService,
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

    this.loginService.authenticate(loginRequest)
      .pipe(
        take(1),
        takeUntil(this.destroyed$),
        switchMap(response => {
          localStorage.setItem('token', response.accessToken);
          localStorage.setItem('role', response.role.toLowerCase());
          this.toastrService.success('Login successful');

          if (response.role.toLowerCase() === Role.administrator.toLowerCase()){
            this.router.navigate(['admin'])
            return of();
          }

          this.router.navigate(['event'])
          return of();
        }),
        catchError((error) => {
          this.toastrService.error('Login failed')
          return of();
        })
      ).subscribe();
  }
}
