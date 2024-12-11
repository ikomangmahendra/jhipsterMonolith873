import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IUserSiteOrganisation } from '../user-site-organisation.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../user-site-organisation.test-samples';

import { UserSiteOrganisationService } from './user-site-organisation.service';

const requireRestSample: IUserSiteOrganisation = {
  ...sampleWithRequiredData,
};

describe('UserSiteOrganisation Service', () => {
  let service: UserSiteOrganisationService;
  let httpMock: HttpTestingController;
  let expectedResult: IUserSiteOrganisation | IUserSiteOrganisation[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(UserSiteOrganisationService);
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

    it('should create a UserSiteOrganisation', () => {
      const userSiteOrganisation = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(userSiteOrganisation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a UserSiteOrganisation', () => {
      const userSiteOrganisation = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(userSiteOrganisation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a UserSiteOrganisation', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of UserSiteOrganisation', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a UserSiteOrganisation', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addUserSiteOrganisationToCollectionIfMissing', () => {
      it('should add a UserSiteOrganisation to an empty array', () => {
        const userSiteOrganisation: IUserSiteOrganisation = sampleWithRequiredData;
        expectedResult = service.addUserSiteOrganisationToCollectionIfMissing([], userSiteOrganisation);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(userSiteOrganisation);
      });

      it('should not add a UserSiteOrganisation to an array that contains it', () => {
        const userSiteOrganisation: IUserSiteOrganisation = sampleWithRequiredData;
        const userSiteOrganisationCollection: IUserSiteOrganisation[] = [
          {
            ...userSiteOrganisation,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addUserSiteOrganisationToCollectionIfMissing(userSiteOrganisationCollection, userSiteOrganisation);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a UserSiteOrganisation to an array that doesn't contain it", () => {
        const userSiteOrganisation: IUserSiteOrganisation = sampleWithRequiredData;
        const userSiteOrganisationCollection: IUserSiteOrganisation[] = [sampleWithPartialData];
        expectedResult = service.addUserSiteOrganisationToCollectionIfMissing(userSiteOrganisationCollection, userSiteOrganisation);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(userSiteOrganisation);
      });

      it('should add only unique UserSiteOrganisation to an array', () => {
        const userSiteOrganisationArray: IUserSiteOrganisation[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const userSiteOrganisationCollection: IUserSiteOrganisation[] = [sampleWithRequiredData];
        expectedResult = service.addUserSiteOrganisationToCollectionIfMissing(userSiteOrganisationCollection, ...userSiteOrganisationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const userSiteOrganisation: IUserSiteOrganisation = sampleWithRequiredData;
        const userSiteOrganisation2: IUserSiteOrganisation = sampleWithPartialData;
        expectedResult = service.addUserSiteOrganisationToCollectionIfMissing([], userSiteOrganisation, userSiteOrganisation2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(userSiteOrganisation);
        expect(expectedResult).toContain(userSiteOrganisation2);
      });

      it('should accept null and undefined values', () => {
        const userSiteOrganisation: IUserSiteOrganisation = sampleWithRequiredData;
        expectedResult = service.addUserSiteOrganisationToCollectionIfMissing([], null, userSiteOrganisation, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(userSiteOrganisation);
      });

      it('should return initial array if no UserSiteOrganisation is added', () => {
        const userSiteOrganisationCollection: IUserSiteOrganisation[] = [sampleWithRequiredData];
        expectedResult = service.addUserSiteOrganisationToCollectionIfMissing(userSiteOrganisationCollection, undefined, null);
        expect(expectedResult).toEqual(userSiteOrganisationCollection);
      });
    });

    describe('compareUserSiteOrganisation', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareUserSiteOrganisation(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareUserSiteOrganisation(entity1, entity2);
        const compareResult2 = service.compareUserSiteOrganisation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareUserSiteOrganisation(entity1, entity2);
        const compareResult2 = service.compareUserSiteOrganisation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareUserSiteOrganisation(entity1, entity2);
        const compareResult2 = service.compareUserSiteOrganisation(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
