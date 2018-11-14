import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FormsModule } from '@angular/forms';

import { SpinnerModule } from 'primeng/spinner';
import { TableModule } from 'primeng/table';
import { DropdownModule } from 'primeng/dropdown';

import { QueryDialogComponent } from './query-dialog.component';

describe('QueryDialogComponent', () => {
  let component: QueryDialogComponent;
  let fixture: ComponentFixture<QueryDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ QueryDialogComponent ],
      imports: [
         SpinnerModule,
         FormsModule,
         TableModule,
         DropdownModule
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(QueryDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
