import { Component, OnInit, Output, EventEmitter } from '@angular/core';

import { Options, Network, Data, DataSet, Edge, Node, IdType } from 'vis';

import { EntityView } from '../../dto/EntityView';
import { RelationView } from '../../dto/RelationView';
import { Graph } from '../../dto/Graph';

@Component({
  selector: 'app-vis-graph',
  templateUrl: './vis-graph.component.html',
  styleUrls: ['./vis-graph.component.css']
})
export class VisGraphComponent implements OnInit {
  options: Options;
  network: Network;
  nodes: DataSet<Node>;
  edges: DataSet<Edge>;
  graph: Graph;
  @Output() selectionChange = new EventEmitter<string[]>();

  constructor() { }

  initGraph() {
    this.options = {
      edges: {
          arrows: 'to',
          dashes: true,
          font: {
              size: 6,
          },
          shadow: true,
          smooth: {
              enabled: true,
              forceDirection: 'none',
              roundness: 0.5,
              type: 'dynamic',
          },
          width: 2,
      },
      configure: {
        container: document.getElementById('visgraph-canvas'),
        filter: false
      },
      interaction: {
        multiselect: true
      },
      groups: {
        CLASS: {
          image: '/assets/icons/class_obj_2x.png',
          shape: 'image',
        },
        ENUM: {
          centralGravity: 1.9,
          color: '#5A1E5C',
          image: '/assets/icons/enum_obj_2x.png',
          shape: 'image',
        },
        INTERFACE: {
          color: '#2B7CE9',
          image: '/assets/icons/int_obj_2x.png',
          shape: 'image',
        },
        LAMBDA: {
          color: '#006677',
          image: '/assets/icons/lambda_obj_2x.png',
          shape: 'image',
        },
        METHOD: {
          color: '#006688',
          image: '/assets/icons/methpub_obj_2x.png',
          shape: 'image',
        },
        PACKAGE: {
          color: '#006699',
          image: '/assets/icons/package_obj_2x.png',
          shape: 'image',
        },
      },
      layout: {
          improvedLayout: false,
      },
    };

    const n: Node[] = [];
    const e: Edge[] = [];

    this.nodes = new DataSet<Node>(n);
    this.edges = new DataSet<Edge>(e);

    const data: Data = {
        edges: this.edges,
        nodes: this.nodes,
    };
    this.network = new Network(this.options.configure.container, data, this.options);
    this.network.on('click', (params: any) => {
      this.selectionChange.emit(this.network.getSelectedNodes() as string[]);
    });
  }

  private toNode(entity: EntityView) {
      return {id: entity.id, label: entity.name, group: entity.type};
  }

  private toEdge(relation: RelationView) {
      return {id: relation.id, from: relation.sourceId, to: relation.targetId, label: relation.type};
  }

  public syncDataSet<T = RelationView|EntityView>(dataSet: DataSet<T>, items: T[]) {
      const itemIds: string[] = [];
      const toBeAdded: T[] = [];
      for (const item of items) {
          itemIds.push((item as any).id);
          if (!dataSet.get((item as any).id)) {
              toBeAdded.push(item);
          }
      }

      dataSet.add(toBeAdded);

      const toBeRemoved: IdType[] = [];

      for (const itemId of dataSet.getIds()) {
          if (itemIds.indexOf(itemId as string) !== -1) {
              continue;
          }
          toBeRemoved.push(itemId);
      }
      dataSet.remove(toBeRemoved);
  }

  public updateGraph(graph: Graph) {
      const nodes: Node[] = graph.entities.map((value: EntityView, index: number, array: EntityView[]) => {
          return this.toNode(value);
      }, this);
      this.syncDataSet(this.nodes, nodes);

      const edges: Edge[] = graph.relations.map((value: RelationView) => {
          return this.toEdge(value);
      }, this);
      this.syncDataSet(this.edges, edges);
      this.graph = graph;
      this.network.fit();
  }

  ngOnInit() {
      this.initGraph();
  }

}
