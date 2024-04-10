import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EventCategoryCreateComponent } from './event-category-create.component';

describe('EventCategoryCreateComponent', () => {
  let component: EventCategoryCreateComponent;
  let fixture: ComponentFixture<EventCategoryCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventCategoryCreateComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(EventCategoryCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
