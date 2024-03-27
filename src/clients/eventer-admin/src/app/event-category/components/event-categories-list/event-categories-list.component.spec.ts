import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EventCategoriesListComponent } from './event-categories-list.component';

describe('EventCategoriesListComponent', () => {
  let component: EventCategoriesListComponent;
  let fixture: ComponentFixture<EventCategoriesListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventCategoriesListComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(EventCategoriesListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
