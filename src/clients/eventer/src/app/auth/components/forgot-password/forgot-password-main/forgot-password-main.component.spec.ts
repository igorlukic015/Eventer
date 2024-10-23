import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ForgotPasswordMainComponent } from './forgot-password-main.component';

describe('ForgotPasswordMainComponent', () => {
  let component: ForgotPasswordMainComponent;
  let fixture: ComponentFixture<ForgotPasswordMainComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ForgotPasswordMainComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ForgotPasswordMainComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
