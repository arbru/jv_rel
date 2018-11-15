import { TestBed } from '@angular/core/testing';

import { GraphService } from './graph.service';
import { HttpClientModule } from '@angular/common/http';

describe('GraphService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    imports: [
      HttpClientModule
    ]
  }));

  it('should be created', () => {
    const service: GraphService = TestBed.get(GraphService);
    expect(service).toBeTruthy();
  });
});
