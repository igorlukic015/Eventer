import {Component, OnInit, signal, WritableSignal} from '@angular/core';
import {FormBuilder, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {RouterLink} from "@angular/router";
import {DestroyableComponent} from "../../../shared/components/destroyable/destroyable.component";
import {ToastrService} from "ngx-toastr";
import {UserService} from "../../services/user.service";
import {catchError, map, mergeMap, of, switchMap, take, takeUntil} from "rxjs";
import {NavBarComponent} from "../../../shared/components/nav-bar/nav-bar.component";
import {fromPromise} from "rxjs/internal/observable/innerFrom";
import {Image} from "../../../shared/contracts/interfaces";

@Component({
  selector: 'eventer-user-profile',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule,
    RouterLink,
    NavBarComponent
  ],
  templateUrl: './user-profile.component.html',
  styleUrl: './user-profile.component.css'
})
export class UserProfileComponent extends DestroyableComponent implements OnInit {
  userForm = this.formBuilder.group({
    id: [-1],
    name: ['', Validators.required],
    username: ['', Validators.required],
    city: ['', Validators.required]
  });

  uploadedFile: WritableSignal<File | null> = signal(null);
  readFile: WritableSignal<any> = signal(null);

  constructor(private formBuilder: FormBuilder,
              private readonly userService: UserService,
              private readonly toastrService: ToastrService) {
    super();
  }

  onFileUpload($event: Event) {
    const element = $event.currentTarget as HTMLInputElement;

    if (element.files && element.files.length > 0) {
      const image = element.files[0]

      this.uploadedFile.set(image);

      this.previewImage(image);
    }
  }

  onAvatarSubmit() {
    if (!this.uploadedFile()) {
      return
    }

    const formData: FormData = new FormData();

    formData.append('image', this.uploadedFile()!)

    this.userService.uploadAvatar(formData).pipe(
      take(1),
      catchError((error) => {
        if (error?.error?.detail !== undefined) {
          this.toastrService.error(error.error.detail);
          return of();
        }
        this.toastrService.error('Updating avatar failed');
        return of();
      })).subscribe();
  }

  onSubmit() {
    if (!this.userForm.valid ||
      !this.userForm.controls.id.value ||
      !this.userForm.controls.name.value ||
      !this.userForm.controls.username.value ||
      !this.userForm.controls.city.value) {
      return;
    }

    const request = {
      id: this.userForm.controls.id.value,
      name: this.userForm.controls.name.value,
      username: this.userForm.controls.username.value,
      city: this.userForm.controls.city.value
    };

    this.userService.updateProfileData(request).pipe(
      take(1),
      takeUntil(this.destroyed$),
      switchMap((response) => {
        this.toastrService.success('Updated successfully')
        return of();
      }),
      catchError((error) => {
        if (error?.error?.detail !== undefined) {
          this.toastrService.error(error.error.detail);
          return of();
        }

        this.toastrService.error('Update failed');
        return of();
      })
    ).subscribe()
  }

  ngOnInit(): void {
    this.userService.getProfileData().pipe(take(1), catchError((error) => {
      if (error?.error?.detail !== undefined) {
        this.toastrService.error(error.error.detail);
        return of();
      }

      this.toastrService.error('Getting profile data failed');
      return of();
    })).subscribe(user => {
      this.userForm.patchValue({
        id: user.id,
        username: user.username,
        name: user.name,
        city: user.city
      }, {emitEvent: false})

      this.fetchImages(user.profileImage);
    })
  }

  private fetchImages(image: Image) {
    fromPromise(fetch(image.url)).pipe(
      mergeMap((response) => fromPromise(response.blob())),
      map((blob) => (new File([blob], 'profileImage', {type: blob.type})))
    ).subscribe((image) => {
      this.uploadedFile.set(image);
      this.previewImage(image);
    })
  }

  private previewImage(image: File) {
    const fileReader = new FileReader();
    const that = this;

    fileReader.onload = function (event) {
      that.readFile.set(event.target?.result);
    }

    fileReader.readAsDataURL(image);
  }
}
