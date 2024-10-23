import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {FormBuilder, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {AuthService} from "../../../services/auth.service";
import {catchError, of, take, takeUntil} from "rxjs";
import {DestroyableComponent} from "../../../../shared/components/destroyable/destroyable.component";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'eventer-forgot-password-reset',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [AuthService],
  templateUrl: './forgot-password-reset.component.html',
  styleUrl: './forgot-password-reset.component.css'
})
export class ForgotPasswordResetComponent extends DestroyableComponent implements OnInit {
  resetForm = this.formBuilder.group({
    username: ['', Validators.required],
    password: ['', Validators.required],
    code: ['', Validators.required]
  });

  constructor(private formBuilder: FormBuilder,
              private authService: AuthService,
              private route: ActivatedRoute,
              private toastrService: ToastrService,
              private router: Router) {
    super();
  }

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      const email = params['email'];
      this.resetForm.patchValue({username: email}, {emitEvent: false});
    });
  }

  handleLoginClick($event: any) {
    this.router.navigate(["/login"]);
  }

  handleResetClick($event: any) {
    if (!this.resetForm.valid ||
      !this.resetForm.controls.username.value ||
      !this.resetForm.controls.password.value ||
      !this.resetForm.controls.code.value
    ) {
      return;
    }

    this.authService.resetPassword({
      email: this.resetForm.controls.username.value,
      password: this.resetForm.controls.password.value,
      code: this.resetForm.controls.code.value
    }).pipe(take(1), takeUntil(this.destroyed$), catchError(error => {
      this.toastrService.error("Greška pri zahtevu restarta lozinke.")
      return of()
    })).subscribe(() => {
      this.router.navigate(['/login']);
    })
  }
}
