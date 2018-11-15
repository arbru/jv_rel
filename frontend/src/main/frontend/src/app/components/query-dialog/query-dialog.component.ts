import { Component, OnInit, EventEmitter } from '@angular/core';

import { SelectItem } from 'primeng/api';

import { Query } from '../../dto/Query';
import { RelationType } from '../../dto/RelationType';


@Component({
  selector: 'app-query-dialog',
  templateUrl: './query-dialog.component.html',
  styleUrls: ['./query-dialog.component.css']
})
export class QueryDialogComponent implements OnInit {
  query: Query = {
    name: '',
    maxHops: 0,
    relationFilter: []
  };

  relationTypes: SelectItem[];
  directions: SelectItem[];

  constructor() { }

  removeFilter(index: number) {
    this.query.relationFilter.splice(index, 1);
  }

  updateQuery(query: Query) {
      this.query = query;
      this.calculateRelationTypeUsage();
  }

  calculateRelationTypeUsage() {
    this.relationTypes.forEach(rt => {
       rt.disabled = this.query.relationFilter.some(v => v.relationType === rt.value);
    });
  }

  onRelationTypeChange(event: EventEmitter<MouseEvent>) {
    this.calculateRelationTypeUsage();
  }

  clear() {
    console.log('clear dialog');
    this.query = {
      name: '',
      maxHops: null,
      relationFilter: []
    };
    this.calculateRelationTypeUsage();
  }

  addFilter(event: EventEmitter<MouseEvent>) {
    console.log('add filter');
    this.query.relationFilter.push({ relationType: null, maxHops: 10, direction: 'FORWARD'});
  }

  initRelationTypes(relationTypes: RelationType[]) {
      console.log('initRelationTypes', relationTypes);
      this.relationTypes = relationTypes.map(relationType => {
          return {label: relationType.id, value: relationType.id};
      });
  }

  ngOnInit() {
    this.directions = [
        {label: 'FORWARD', value: 'FORWARD'},
        {label: 'BACKWARD', value: 'BACKWARD'},
        {label: 'BOTH', value: 'BOTH'},
    ];
  }

}
