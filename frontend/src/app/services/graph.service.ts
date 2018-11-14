import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

import { Entity } from '../dto/Entity';
import { Relation } from '../dto/Relation';
import { Graph } from '../dto/Graph';
import { Query } from '../dto/Query';
import { RelationType } from '../dto/RelationType';
import { QueryExecution } from '../dto/QueryExecution';


const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class GraphService {

  constructor(private http: HttpClient) { }

  getRootPackages(): Observable<Entity[]> {
      return this.http.get<Entity[]>('/rest/entities/toplevel');
  }

  getOutgoingRelations(id: string): Observable<Relation[]> {
      return this.http.get<Relation[]>('/rest/entities/' + id + '/targets');
  }

  getGraph(id: string): Observable<Graph> {
      return this.http.get<Graph>('/rest/graphs/' + id);
  }

  getGraphs(): Observable<Graph[]> {
      return this.http.get<Graph[]>('/rest/graphs');
  }

  getQueries(): Observable<Query[]> {
     return this.http.get<Query[]>('/rest/queries');
  }

  getQuery(queryId: string): Observable<Query> {
     return this.http.get<Query>('/rest/queries/' + queryId);
  }

  saveQuery(query: Query): Observable<any> {
    console.log('put query ', JSON.stringify(query));
    return this.http.put('/rest/queries', query, httpOptions).pipe(
      tap(_ => console.log(`updated query id=${query.name}`))
    );
  }

  processQuery(queryExecution: QueryExecution): Observable<Graph> {
    console.log('process query ', JSON.stringify(queryExecution));
    return this.http.post<Graph>('/rest/process-query', queryExecution, httpOptions);
  }

  getRelationTypes(): Observable<RelationType[]> {
    return this.http.get<RelationType[]>('/rest/relation-types');
  }
}
