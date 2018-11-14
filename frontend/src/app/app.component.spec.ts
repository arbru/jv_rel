import { TestBed, async } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';

import { TreeViewComponent } from './components/tree-view/tree-view.component';

import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { TreeModule } from 'primeng/tree';
import { ContextMenuModule } from 'primeng/contextmenu';
import { MenubarModule } from 'primeng/menubar';
import { DropdownModule } from 'primeng/dropdown';
import { SpinnerModule } from 'primeng/spinner';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { DialogModule } from 'primeng/dialog';


import { VisGraphComponent } from './components/vis-graph/vis-graph.component';
import { QueryDialogComponent } from './components/query-dialog/query-dialog.component';


describe('AppComponent', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        AppComponent,
        TreeViewComponent,
        VisGraphComponent,
        QueryDialogComponent
      ],
      imports: [
        BrowserModule,
        FormsModule,
        TreeModule,
        ContextMenuModule,
        HttpClientModule,
        MenubarModule,
        DropdownModule,
        SpinnerModule,
        ButtonModule,
        TableModule,
        DialogModule,
        BrowserAnimationsModule
      ]
    }).compileComponents();
  }));

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  });

  it(`should have as title 'jv_rel'`, () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app.title).toEqual('jv_rel');
  });

});
