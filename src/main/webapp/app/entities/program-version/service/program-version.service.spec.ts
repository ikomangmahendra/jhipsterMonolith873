import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IProgramVersion } from '../program-version.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../program-version.test-samples';

import { ProgramVersionService, RestProgramVersion } from './program-version.service';

const requireRestSample: RestProgramVersion = {
  ...sampleWithRequiredData,
  publishDate: sampleWithRequiredData.publishDate?.format(DATE_FORMAT),
};

describe('ProgramVersion Service', () => {
  let service: ProgramVersionService;
  let httpMock: HttpTestingController;
  let expectedResult: IProgramVersion | IProgramVersion[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ProgramVersionService);
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

    it('should create a ProgramVersion', () => {
      const programVersion = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(programVersion).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProgramVersion', () => {
      const programVersion = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(programVersion).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProgramVersion', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProgramVersion', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ProgramVersion', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addProgramVersionToCollectionIfMissing', () => {
      it('should add a ProgramVersion to an empty array', () => {
        const programVersion: IProgramVersion = sampleWithRequiredData;
        expectedResult = service.addProgramVersionToCollectionIfMissing([], programVersion);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(programVersion);
      });

      it('should not add a ProgramVersion to an array that contains it', () => {
        const programVersion: IProgramVersion = sampleWithRequiredData;
        const programVersionCollection: IProgramVersion[] = [
          {
            ...programVersion,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProgramVersionToCollectionIfMissing(programVersionCollection, programVersion);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProgramVersion to an array that doesn't contain it", () => {
        const programVersion: IProgramVersion = sampleWithRequiredData;
        const programVersionCollection: IProgramVersion[] = [sampleWithPartialData];
        expectedResult = service.addProgramVersionToCollectionIfMissing(programVersionCollection, programVersion);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(programVersion);
      });

      it('should add only unique ProgramVersion to an array', () => {
        const programVersionArray: IProgramVersion[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const programVersionCollection: IProgramVersion[] = [sampleWithRequiredData];
        expectedResult = service.addProgramVersionToCollectionIfMissing(programVersionCollection, ...programVersionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const programVersion: IProgramVersion = sampleWithRequiredData;
        const programVersion2: IProgramVersion = sampleWithPartialData;
        expectedResult = service.addProgramVersionToCollectionIfMissing([], programVersion, programVersion2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(programVersion);
        expect(expectedResult).toContain(programVersion2);
      });

      it('should accept null and undefined values', () => {
        const programVersion: IProgramVersion = sampleWithRequiredData;
        expectedResult = service.addProgramVersionToCollectionIfMissing([], null, programVersion, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(programVersion);
      });

      it('should return initial array if no ProgramVersion is added', () => {
        const programVersionCollection: IProgramVersion[] = [sampleWithRequiredData];
        expectedResult = service.addProgramVersionToCollectionIfMissing(programVersionCollection, undefined, null);
        expect(expectedResult).toEqual(programVersionCollection);
      });
    });

    describe('compareProgramVersion', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProgramVersion(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareProgramVersion(entity1, entity2);
        const compareResult2 = service.compareProgramVersion(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareProgramVersion(entity1, entity2);
        const compareResult2 = service.compareProgramVersion(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareProgramVersion(entity1, entity2);
        const compareResult2 = service.compareProgramVersion(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
