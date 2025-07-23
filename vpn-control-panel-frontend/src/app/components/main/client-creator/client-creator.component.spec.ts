import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientCreatorComponent } from './client-creator.component';

describe('ClientCreatorComponent', () => {
  let component: ClientCreatorComponent;
  let fixture: ComponentFixture<ClientCreatorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClientCreatorComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ClientCreatorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
