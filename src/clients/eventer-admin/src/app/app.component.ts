import {Component, OnInit} from '@angular/core';
import {RouterLink, RouterLinkActive, RouterOutlet} from "@angular/router";
import {CommonModule} from "@angular/common";
import {FooterComponent} from "./shared/components/footer/footer.component";
import {HttpClientModule} from "@angular/common/http";
import {NavBarComponent} from "./shared/components/nav-bar/nav-bar.component";
import {Store} from "@ngrx/store";
import {subscribeToChanges} from "./shared/+state/actions/real-time.actions";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    CommonModule,
    HttpClientModule,
    RouterOutlet,
    RouterLink,
    RouterLinkActive,
    FooterComponent,
    NavBarComponent,
  ],
  providers:[HttpClientModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  constructor(private readonly store: Store) {
  }

  ngOnInit(): void {
    this.store.dispatch(subscribeToChanges());
  }
}
