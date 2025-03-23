import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EventCategoryMainComponent } from './event-category-main.component';

describe('EventCategoryMainComponent', () => {
  let component: EventCategoryMainComponent;
  let fixture: ComponentFixture<EventCategoryMainComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventCategoryMainComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(EventCategoryMainComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
