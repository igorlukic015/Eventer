import { Component } from '@angular/core';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {LayoutMainComponent} from "../../../shared/components/layout-main/layout-main.component";

@Component({
  selector: 'eventer-admin-login-main',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule,
    LayoutMainComponent
  ],
  templateUrl: './login-main.component.html',
  styleUrl: './login-main.component.css'
})
export class LoginMainComponent {

}
