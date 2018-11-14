import { Component, OnInit, Output, EventEmitter } from '@angular/core';

import { TreeNode } from 'primeng/api';
import { MenuItem } from 'primeng/api';


import { GraphService } from '../../services/graph.service';

import { Entity } from '../../dto/Entity';
import { Query } from '../../dto/Query';
import { QueryExecution } from '../../dto/QueryExecution';

@Component({
  selector: 'app-tree-view',
  templateUrl: './tree-view.component.html',
  styleUrls: ['./tree-view.component.css']
})
export class TreeViewComponent implements OnInit {
  rootPackages: TreeNode[];
  navigationTypes: string[] = ['HAS_CLASS', 'HAS_METHOD', 'HAS_PACKAGE', 'INNER_CLASS'];
  selectedNodes: TreeNode[] = [];
  @Output() selectionChange = new EventEmitter<string[]>();
  @Output() triggerExecution = new EventEmitter<QueryExecution>();

  contextMenu: MenuItem[];

  constructor(private graphService: GraphService) { }

  ngOnInit() {
    this.graphService.getRootPackages().subscribe(rootPackages => {
        this.rootPackages = rootPackages.map((pkg) => this.toTreeNode(pkg));
    });
    this.contextMenu = [{label: 'Process Query', items: []}];

    this.graphService.getQueries().subscribe(queries => {
          const m: MenuItem[] = this.contextMenu[0].items as MenuItem[];
          queries.map((query) => {
              return {
                label: query.name,
                command: (event: EventEmitter<MouseEvent>) => { this.processQuery(query); }
              };
          }).forEach((gm) => m.push(gm));
    });
  }

  addQueryToMenu(query: Query) {
    const m: MenuItem[] = this.contextMenu[0].items as MenuItem[];
    const exists: boolean = m.some(item => query.name === item.label);
    if (!exists) {
      m.push({
        label: query.name,
        command: (event) => { this.processQuery(query); }
      });
    }
  }

  processQuery(query: Query) {
     if (this.selectedNodes) {
       this.triggerExecution.emit({
         queryName: query.name,
         entities: this.selectedNodes.map((node) => node.data.id)
       });
    }
  }

  nodeSelect(event: EventEmitter<any>) {
    this.selectionChange.emit(this.selectedNodes.map((node) => node.data.id));
  }

  nodeExpand(event: any) {
      if (event.node) {
          this.graphService.getOutgoingRelations(event.node.data.id).subscribe(rels => {
               event.node.children = rels
                  .filter((rel) => this.navigationTypes.indexOf(rel.type) !== -1)
                  .map((rel) => this.toTreeNode(rel.target));
               if (event.node.children.length === 0) {
                 event.node.leaf = true;
                 event.node.children = null;
               }
          });
      }
  }

  toTreeNode(entity: Entity): TreeNode {
    return {
      label: entity.name,
      leaf: false,
      expandedIcon: entity.type,
      collapsedIcon: entity.type,
      icon: entity.type,
      data: entity
    };
  }

}
