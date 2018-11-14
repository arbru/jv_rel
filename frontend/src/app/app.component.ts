import { Component, ViewChild, EventEmitter, OnInit } from '@angular/core';

import { MenuItem } from 'primeng/api';

import { GraphService } from './services/graph.service';

import { VisGraphComponent } from './components/vis-graph/vis-graph.component';

import { QueryDialogComponent } from './components/query-dialog/query-dialog.component';
import { TreeViewComponent } from './components/tree-view/tree-view.component';

import { QueryExecution } from './dto/QueryExecution';
import { Query} from './dto/Query';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'jv_rel';

  menu: MenuItem[];
  displayQueryDialog = false;

  @ViewChild(VisGraphComponent)
  private visGraphComponent: VisGraphComponent;

  @ViewChild(QueryDialogComponent)
  private queryDialogComponent: QueryDialogComponent;

  @ViewChild(TreeViewComponent)
  private treeViewComponent: TreeViewComponent;

  constructor(private graphService: GraphService) { }


  onSelectionChange(entities: string[], source: string) {
    console.log('selection on ' + source + ': ' + entities);
  }

  loadGraph(event: EventEmitter<MouseEvent>, graphId: string) {
    this.graphService.getGraph(graphId).subscribe(graph => {
        this.visGraphComponent.updateGraph(graph);
    });
  }

  onQueryExecution(execution: QueryExecution) {
    console.log('onQueryExecution ', execution);
    this.graphService.processQuery(execution).subscribe(graph => {
        console.log('graph = ', graph);
        this.visGraphComponent.updateGraph(graph);
    });
  }

  modifyQuery(event: EventEmitter<MouseEvent>, queryId: string): void {
    console.log('modify query', event);
    this.graphService.getQuery(queryId).subscribe((query) => {
      this.queryDialogComponent.updateQuery(query);
      this.displayQueryDialog = true;
    });
  }

  createQuery(event: EventEmitter<MouseEvent>): void {
    console.log('create query');
    this.queryDialogComponent.clear();
    this.displayQueryDialog = true;
  }

  processQuery(event: EventEmitter<MouseEvent>): void {
    console.log('process query', event);
    // this.msgs.push({severity:'error', summary:'Success Message', detail:'Order submitted'});
    this.graphService.saveQuery(this.queryDialogComponent.query).subscribe(() => {
        console.log('query saved ?');
        this.addQueryToMenu(this.queryDialogComponent.query);
        this.treeViewComponent.addQueryToMenu(this.queryDialogComponent.query);
    });
    console.log('saving query done');
    this.displayQueryDialog = false;
  }

  addQueryToMenu(query: Query) {
    const m: MenuItem[] = this.menu[0].items as MenuItem[];
    const exists: boolean = m.some(item => query.name === item.label);
    if (!exists) {
      m.push({
        label: query.name,
        command: (event) => { this.modifyQuery(event, query.name); }
      });
    }
  }

  ngOnInit() {
      this.menu = [
          {
              label: 'Queries',
              icon: 'fa fa-plus',
              items: [
                 { label: 'New', command: (event) => { this.createQuery(event); } }
              ],
          },
          {
              label: 'Graphs',
              icon: 'pi pi-fw pi-pencil',
              items: []
          }
     ];

     this.graphService.getGraphs().subscribe(graphs => {
         for (const graph of graphs) {
            const gm: MenuItem = {
               label: graph.name,
               command: (event) => { this.loadGraph(event, graph.id); }
            };
            const m: MenuItem[] = this.menu[1].items as MenuItem[];
            m.push(gm);
         }
     });

     this.graphService.getQueries().subscribe(queries => {
         for (const query of queries) {
            const gm: MenuItem = {
               label: query.name,
               command: (event) => { this.modifyQuery(event, query.name); }
            };
            const m: MenuItem[] = this.menu[0].items as MenuItem[];
            m.push(gm);
         }
     });

     this.graphService.getRelationTypes().subscribe(relationTypes => {
        this.queryDialogComponent.initRelationTypes(relationTypes);
     });

   }
}
