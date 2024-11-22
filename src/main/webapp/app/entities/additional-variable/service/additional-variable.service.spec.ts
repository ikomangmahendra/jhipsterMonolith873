import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IAdditionalVariable } from '../additional-variable.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../additional-variable.test-samples';

import { AdditionalVariableService } from './additional-variable.service';

const requireRestSample: IAdditionalVariable = {
  ...sampleWithRequiredData,
};

describe('AdditionalVariable Service', () => {
  let service: AdditionalVariableService;
  let httpMock: HttpTestingController;
  let expectedResult: IAdditionalVariable | IAdditionalVariable[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(AdditionalVariableService);
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

    it('should create a AdditionalVariable', () => {
      const additionalVariable = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(additionalVariable).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AdditionalVariable', () => {
      const additionalVariable = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(additionalVariable).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AdditionalVariable', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AdditionalVariable', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AdditionalVariable', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAdditionalVariableToCollectionIfMissing', () => {
      it('should add a AdditionalVariable to an empty array', () => {
        const additionalVariable: IAdditionalVariable = sampleWithRequiredData;
        expectedResult = service.addAdditionalVariableToCollectionIfMissing([], additionalVariable);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(additionalVariable);
      });

      it('should not add a AdditionalVariable to an array that contains it', () => {
        const additionalVariable: IAdditionalVariable = sampleWithRequiredData;
        const additionalVariableCollection: IAdditionalVariable[] = [
          {
            ...additionalVariable,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAdditionalVariableToCollectionIfMissing(additionalVariableCollection, additionalVariable);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AdditionalVariable to an array that doesn't contain it", () => {
        const additionalVariable: IAdditionalVariable = sampleWithRequiredData;
        const additionalVariableCollection: IAdditionalVariable[] = [sampleWithPartialData];
        expectedResult = service.addAdditionalVariableToCollectionIfMissing(additionalVariableCollection, additionalVariable);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(additionalVariable);
      });

      it('should add only unique AdditionalVariable to an array', () => {
        const additionalVariableArray: IAdditionalVariable[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const additionalVariableCollection: IAdditionalVariable[] = [sampleWithRequiredData];
        expectedResult = service.addAdditionalVariableToCollectionIfMissing(additionalVariableCollection, ...additionalVariableArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const additionalVariable: IAdditionalVariable = sampleWithRequiredData;
        const additionalVariable2: IAdditionalVariable = sampleWithPartialData;
        expectedResult = service.addAdditionalVariableToCollectionIfMissing([], additionalVariable, additionalVariable2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(additionalVariable);
        expect(expectedResult).toContain(additionalVariable2);
      });

      it('should accept null and undefined values', () => {
        const additionalVariable: IAdditionalVariable = sampleWithRequiredData;
        expectedResult = service.addAdditionalVariableToCollectionIfMissing([], null, additionalVariable, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(additionalVariable);
      });

      it('should return initial array if no AdditionalVariable is added', () => {
        const additionalVariableCollection: IAdditionalVariable[] = [sampleWithRequiredData];
        expectedResult = service.addAdditionalVariableToCollectionIfMissing(additionalVariableCollection, undefined, null);
        expect(expectedResult).toEqual(additionalVariableCollection);
      });
    });

    describe('compareAdditionalVariable', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAdditionalVariable(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAdditionalVariable(entity1, entity2);
        const compareResult2 = service.compareAdditionalVariable(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAdditionalVariable(entity1, entity2);
        const compareResult2 = service.compareAdditionalVariable(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAdditionalVariable(entity1, entity2);
        const compareResult2 = service.compareAdditionalVariable(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
