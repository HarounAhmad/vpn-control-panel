import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientFilesComponent } from './client-files.component';

describe('ClientFilesComponent', () => {
  let component: ClientFilesComponent;
  let fixture: ComponentFixture<ClientFilesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClientFilesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ClientFilesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
