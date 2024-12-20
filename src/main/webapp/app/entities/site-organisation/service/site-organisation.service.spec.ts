import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ISiteOrganisation } from '../site-organisation.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../site-organisation.test-samples';

import { SiteOrganisationService } from './site-organisation.service';

const requireRestSample: ISiteOrganisation = {
  ...sampleWithRequiredData,
};

describe('SiteOrganisation Service', () => {
  let service: SiteOrganisationService;
  let httpMock: HttpTestingController;
  let expectedResult: ISiteOrganisation | ISiteOrganisation[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(SiteOrganisationService);
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

    it('should create a SiteOrganisation', () => {
      const siteOrganisation = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(siteOrganisation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SiteOrganisation', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SiteOrganisation', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSiteOrganisationToCollectionIfMissing', () => {
      it('should add a SiteOrganisation to an empty array', () => {
        const siteOrganisation: ISiteOrganisation = sampleWithRequiredData;
        expectedResult = service.addSiteOrganisationToCollectionIfMissing([], siteOrganisation);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(siteOrganisation);
      });

      it('should not add a SiteOrganisation to an array that contains it', () => {
        const siteOrganisation: ISiteOrganisation = sampleWithRequiredData;
        const siteOrganisationCollection: ISiteOrganisation[] = [
          {
            ...siteOrganisation,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSiteOrganisationToCollectionIfMissing(siteOrganisationCollection, siteOrganisation);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SiteOrganisation to an array that doesn't contain it", () => {
        const siteOrganisation: ISiteOrganisation = sampleWithRequiredData;
        const siteOrganisationCollection: ISiteOrganisation[] = [sampleWithPartialData];
        expectedResult = service.addSiteOrganisationToCollectionIfMissing(siteOrganisationCollection, siteOrganisation);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(siteOrganisation);
      });

      it('should add only unique SiteOrganisation to an array', () => {
        const siteOrganisationArray: ISiteOrganisation[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const siteOrganisationCollection: ISiteOrganisation[] = [sampleWithRequiredData];
        expectedResult = service.addSiteOrganisationToCollectionIfMissing(siteOrganisationCollection, ...siteOrganisationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const siteOrganisation: ISiteOrganisation = sampleWithRequiredData;
        const siteOrganisation2: ISiteOrganisation = sampleWithPartialData;
        expectedResult = service.addSiteOrganisationToCollectionIfMissing([], siteOrganisation, siteOrganisation2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(siteOrganisation);
        expect(expectedResult).toContain(siteOrganisation2);
      });

      it('should accept null and undefined values', () => {
        const siteOrganisation: ISiteOrganisation = sampleWithRequiredData;
        expectedResult = service.addSiteOrganisationToCollectionIfMissing([], null, siteOrganisation, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(siteOrganisation);
      });

      it('should return initial array if no SiteOrganisation is added', () => {
        const siteOrganisationCollection: ISiteOrganisation[] = [sampleWithRequiredData];
        expectedResult = service.addSiteOrganisationToCollectionIfMissing(siteOrganisationCollection, undefined, null);
        expect(expectedResult).toEqual(siteOrganisationCollection);
      });
    });

    describe('compareSiteOrganisation', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSiteOrganisation(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSiteOrganisation(entity1, entity2);
        const compareResult2 = service.compareSiteOrganisation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSiteOrganisation(entity1, entity2);
        const compareResult2 = service.compareSiteOrganisation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSiteOrganisation(entity1, entity2);
        const compareResult2 = service.compareSiteOrganisation(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
