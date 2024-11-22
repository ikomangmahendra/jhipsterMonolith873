import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IAccuteProgramVariable } from '../accute-program-variable.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../accute-program-variable.test-samples';

import { AccuteProgramVariableService } from './accute-program-variable.service';

const requireRestSample: IAccuteProgramVariable = {
  ...sampleWithRequiredData,
};

describe('AccuteProgramVariable Service', () => {
  let service: AccuteProgramVariableService;
  let httpMock: HttpTestingController;
  let expectedResult: IAccuteProgramVariable | IAccuteProgramVariable[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(AccuteProgramVariableService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a AccuteProgramVariable', () => {
      const accuteProgramVariable = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(accuteProgramVariable).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AccuteProgramVariable', () => {
      const accuteProgramVariable = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(accuteProgramVariable).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AccuteProgramVariable', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AccuteProgramVariable', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AccuteProgramVariable', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAccuteProgramVariableToCollectionIfMissing', () => {
      it('should add a AccuteProgramVariable to an empty array', () => {
        const accuteProgramVariable: IAccuteProgramVariable = sampleWithRequiredData;
        expectedResult = service.addAccuteProgramVariableToCollectionIfMissing([], accuteProgramVariable);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(accuteProgramVariable);
      });

      it('should not add a AccuteProgramVariable to an array that contains it', () => {
        const accuteProgramVariable: IAccuteProgramVariable = sampleWithRequiredData;
        const accuteProgramVariableCollection: IAccuteProgramVariable[] = [
          {
            ...accuteProgramVariable,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAccuteProgramVariableToCollectionIfMissing(accuteProgramVariableCollection, accuteProgramVariable);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AccuteProgramVariable to an array that doesn't contain it", () => {
        const accuteProgramVariable: IAccuteProgramVariable = sampleWithRequiredData;
        const accuteProgramVariableCollection: IAccuteProgramVariable[] = [sampleWithPartialData];
        expectedResult = service.addAccuteProgramVariableToCollectionIfMissing(accuteProgramVariableCollection, accuteProgramVariable);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(accuteProgramVariable);
      });

      it('should add only unique AccuteProgramVariable to an array', () => {
        const accuteProgramVariableArray: IAccuteProgramVariable[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const accuteProgramVariableCollection: IAccuteProgramVariable[] = [sampleWithRequiredData];
        expectedResult = service.addAccuteProgramVariableToCollectionIfMissing(
          accuteProgramVariableCollection,
          ...accuteProgramVariableArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const accuteProgramVariable: IAccuteProgramVariable = sampleWithRequiredData;
        const accuteProgramVariable2: IAccuteProgramVariable = sampleWithPartialData;
        expectedResult = service.addAccuteProgramVariableToCollectionIfMissing([], accuteProgramVariable, accuteProgramVariable2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(accuteProgramVariable);
        expect(expectedResult).toContain(accuteProgramVariable2);
      });

      it('should accept null and undefined values', () => {
        const accuteProgramVariable: IAccuteProgramVariable = sampleWithRequiredData;
        expectedResult = service.addAccuteProgramVariableToCollectionIfMissing([], null, accuteProgramVariable, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(accuteProgramVariable);
      });

      it('should return initial array if no AccuteProgramVariable is added', () => {
        const accuteProgramVariableCollection: IAccuteProgramVariable[] = [sampleWithRequiredData];
        expectedResult = service.addAccuteProgramVariableToCollectionIfMissing(accuteProgramVariableCollection, undefined, null);
        expect(expectedResult).toEqual(accuteProgramVariableCollection);
      });
    });

    describe('compareAccuteProgramVariable', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAccuteProgramVariable(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAccuteProgramVariable(entity1, entity2);
        const compareResult2 = service.compareAccuteProgramVariable(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAccuteProgramVariable(entity1, entity2);
        const compareResult2 = service.compareAccuteProgramVariable(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAccuteProgramVariable(entity1, entity2);
        const compareResult2 = service.compareAccuteProgramVariable(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
