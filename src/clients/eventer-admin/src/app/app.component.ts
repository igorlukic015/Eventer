import {Component} from '@angular/core';
import {RouterLink, RouterLinkActive, RouterOutlet} from "@angular/router";
import {CommonModule} from "@angular/common";
import {FooterComponent} from "./shared/components/footer/footer.component";
import {NavbarComponent} from "./shared/components/navbar/navbar.component";
import {HttpClientModule} from "@angular/common/http";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule,
    HttpClientModule,
    RouterOutlet,
    RouterLink,
    RouterLinkActive,
    FooterComponent,
    NavbarComponent,
  ],
  providers:[HttpClientModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
}
