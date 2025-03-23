import { Component } from '@angular/core';
import {FormBuilder, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {Router, RouterLink} from "@angular/router";
import {DestroyableComponent} from "../../../../shared/components/destroyable/destroyable.component";
import {AuthService} from "../../../services/auth.service";
import {catchError, of, take, takeUntil} from "rxjs";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'eventer-forgot-password-main',
  standalone: true,
  imports: [
      FormsModule,
      ReactiveFormsModule,
      RouterLink
  ],
  providers:[AuthService],
  templateUrl: './forgot-password-main.component.html',
  styleUrl: './forgot-password-main.component.css'
})
export class ForgotPasswordMainComponent extends DestroyableComponent{
  resetForm = this.formBuilder.group({
    username: ['', Validators.required]
  });

  constructor(private formBuilder: FormBuilder,
              private readonly router: Router,
              private toastrService: ToastrService,
              private readonly authService: AuthService) {
    super();
  }

  handleLoginClick($event: any) {
    this.router.navigate(["/login"]);
  }

  handleResetClick($event: any) {
    if (!this.resetForm.valid ||
      !this.resetForm.controls.username.value) {
      return;
    }

    this.authService.requestPasswordReset(this.resetForm.controls.username.value)
      .pipe(take(1), takeUntil(this.destroyed$), catchError(error => {
      this.toastrService.error("Greška pri zahtevu restarta lozinke.")
      return of()
    })).subscribe(() => {
      this.router.navigate(['/forgot-password-reset'], { queryParams: { email: this.resetForm.controls.username.value } });
    })
  }
}
