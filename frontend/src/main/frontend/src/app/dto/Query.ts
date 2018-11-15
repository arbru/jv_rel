import { RelationFilter } from './RelationFilter';

export class Query {
  name: string;
  maxHops: number;
  relationFilter: RelationFilter[];
}
