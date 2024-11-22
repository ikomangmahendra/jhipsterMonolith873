import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IUserTemp } from '../user-temp.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../user-temp.test-samples';

import { UserTempService } from './user-temp.service';

const requireRestSample: IUserTemp = {
  ...sampleWithRequiredData,
};

describe('UserTemp Service', () => {
  let service: UserTempService;
  let httpMock: HttpTestingController;
  let expectedResult: IUserTemp | IUserTemp[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(UserTempService);
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

    it('should create a UserTemp', () => {
      const userTemp = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(userTemp).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a UserTemp', () => {
      const userTemp = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(userTemp).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a UserTemp', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of UserTemp', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a UserTemp', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addUserTempToCollectionIfMissing', () => {
      it('should add a UserTemp to an empty array', () => {
        const userTemp: IUserTemp = sampleWithRequiredData;
        expectedResult = service.addUserTempToCollectionIfMissing([], userTemp);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(userTemp);
      });

      it('should not add a UserTemp to an array that contains it', () => {
        const userTemp: IUserTemp = sampleWithRequiredData;
        const userTempCollection: IUserTemp[] = [
          {
            ...userTemp,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addUserTempToCollectionIfMissing(userTempCollection, userTemp);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a UserTemp to an array that doesn't contain it", () => {
        const userTemp: IUserTemp = sampleWithRequiredData;
        const userTempCollection: IUserTemp[] = [sampleWithPartialData];
        expectedResult = service.addUserTempToCollectionIfMissing(userTempCollection, userTemp);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(userTemp);
      });

      it('should add only unique UserTemp to an array', () => {
        const userTempArray: IUserTemp[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const userTempCollection: IUserTemp[] = [sampleWithRequiredData];
        expectedResult = service.addUserTempToCollectionIfMissing(userTempCollection, ...userTempArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const userTemp: IUserTemp = sampleWithRequiredData;
        const userTemp2: IUserTemp = sampleWithPartialData;
        expectedResult = service.addUserTempToCollectionIfMissing([], userTemp, userTemp2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(userTemp);
        expect(expectedResult).toContain(userTemp2);
      });

      it('should accept null and undefined values', () => {
        const userTemp: IUserTemp = sampleWithRequiredData;
        expectedResult = service.addUserTempToCollectionIfMissing([], null, userTemp, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(userTemp);
      });

      it('should return initial array if no UserTemp is added', () => {
        const userTempCollection: IUserTemp[] = [sampleWithRequiredData];
        expectedResult = service.addUserTempToCollectionIfMissing(userTempCollection, undefined, null);
        expect(expectedResult).toEqual(userTempCollection);
      });
    });

    describe('compareUserTemp', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareUserTemp(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareUserTemp(entity1, entity2);
        const compareResult2 = service.compareUserTemp(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareUserTemp(entity1, entity2);
        const compareResult2 = service.compareUserTemp(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareUserTemp(entity1, entity2);
        const compareResult2 = service.compareUserTemp(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
