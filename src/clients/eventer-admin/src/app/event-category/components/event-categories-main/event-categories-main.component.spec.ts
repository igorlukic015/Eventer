import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EventCategoriesMainComponent } from './event-categories-main.component';

describe('EventCategoriesMainComponent', () => {
  let component: EventCategoriesMainComponent;
  let fixture: ComponentFixture<EventCategoriesMainComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventCategoriesMainComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(EventCategoriesMainComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
