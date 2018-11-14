import { EntityView } from './EntityView';
import { RelationView } from './RelationView';

export class Graph {
  id: string;
  name: string;
  entities: EntityView[] = [];
  relations: RelationView[] = [];
}
