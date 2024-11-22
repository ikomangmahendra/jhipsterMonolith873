import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ISiteOrgTemp } from '../site-org-temp.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../site-org-temp.test-samples';

import { SiteOrgTempService } from './site-org-temp.service';

const requireRestSample: ISiteOrgTemp = {
  ...sampleWithRequiredData,
};

describe('SiteOrgTemp Service', () => {
  let service: SiteOrgTempService;
  let httpMock: HttpTestingController;
  let expectedResult: ISiteOrgTemp | ISiteOrgTemp[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(SiteOrgTempService);
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

    it('should create a SiteOrgTemp', () => {
      const siteOrgTemp = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(siteOrgTemp).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SiteOrgTemp', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SiteOrgTemp', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSiteOrgTempToCollectionIfMissing', () => {
      it('should add a SiteOrgTemp to an empty array', () => {
        const siteOrgTemp: ISiteOrgTemp = sampleWithRequiredData;
        expectedResult = service.addSiteOrgTempToCollectionIfMissing([], siteOrgTemp);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(siteOrgTemp);
      });

      it('should not add a SiteOrgTemp to an array that contains it', () => {
        const siteOrgTemp: ISiteOrgTemp = sampleWithRequiredData;
        const siteOrgTempCollection: ISiteOrgTemp[] = [
          {
            ...siteOrgTemp,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSiteOrgTempToCollectionIfMissing(siteOrgTempCollection, siteOrgTemp);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SiteOrgTemp to an array that doesn't contain it", () => {
        const siteOrgTemp: ISiteOrgTemp = sampleWithRequiredData;
        const siteOrgTempCollection: ISiteOrgTemp[] = [sampleWithPartialData];
        expectedResult = service.addSiteOrgTempToCollectionIfMissing(siteOrgTempCollection, siteOrgTemp);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(siteOrgTemp);
      });

      it('should add only unique SiteOrgTemp to an array', () => {
        const siteOrgTempArray: ISiteOrgTemp[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const siteOrgTempCollection: ISiteOrgTemp[] = [sampleWithRequiredData];
        expectedResult = service.addSiteOrgTempToCollectionIfMissing(siteOrgTempCollection, ...siteOrgTempArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const siteOrgTemp: ISiteOrgTemp = sampleWithRequiredData;
        const siteOrgTemp2: ISiteOrgTemp = sampleWithPartialData;
        expectedResult = service.addSiteOrgTempToCollectionIfMissing([], siteOrgTemp, siteOrgTemp2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(siteOrgTemp);
        expect(expectedResult).toContain(siteOrgTemp2);
      });

      it('should accept null and undefined values', () => {
        const siteOrgTemp: ISiteOrgTemp = sampleWithRequiredData;
        expectedResult = service.addSiteOrgTempToCollectionIfMissing([], null, siteOrgTemp, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(siteOrgTemp);
      });

      it('should return initial array if no SiteOrgTemp is added', () => {
        const siteOrgTempCollection: ISiteOrgTemp[] = [sampleWithRequiredData];
        expectedResult = service.addSiteOrgTempToCollectionIfMissing(siteOrgTempCollection, undefined, null);
        expect(expectedResult).toEqual(siteOrgTempCollection);
      });
    });

    describe('compareSiteOrgTemp', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSiteOrgTemp(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSiteOrgTemp(entity1, entity2);
        const compareResult2 = service.compareSiteOrgTemp(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSiteOrgTemp(entity1, entity2);
        const compareResult2 = service.compareSiteOrgTemp(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSiteOrgTemp(entity1, entity2);
        const compareResult2 = service.compareSiteOrgTemp(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
