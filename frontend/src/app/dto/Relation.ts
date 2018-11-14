import { Entity } from './Entity';

export class Relation {
  id: string;
  type: string;
  source: Entity;
  target: Entity;
}
