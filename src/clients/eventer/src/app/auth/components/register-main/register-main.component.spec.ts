import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisterMainComponent } from './register-main.component';

describe('RegisterMainComponent', () => {
  let component: RegisterMainComponent;
  let fixture: ComponentFixture<RegisterMainComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegisterMainComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(RegisterMainComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
