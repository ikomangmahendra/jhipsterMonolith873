import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IProgramVersion } from 'app/entities/program-version/program-version.model';
import { ProgramVersionService } from 'app/entities/program-version/service/program-version.service';
import { ISiteOrgTemp } from 'app/entities/site-org-temp/site-org-temp.model';
import { SiteOrgTempService } from 'app/entities/site-org-temp/service/site-org-temp.service';
import { IAdditionalVariable } from '../additional-variable.model';
import { AdditionalVariableService } from '../service/additional-variable.service';
import { AdditionalVariableFormService } from './additional-variable-form.service';

import { AdditionalVariableUpdateComponent } from './additional-variable-update.component';

describe('AdditionalVariable Management Update Component', () => {
  let comp: AdditionalVariableUpdateComponent;
  let fixture: ComponentFixture<AdditionalVariableUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let additionalVariableFormService: AdditionalVariableFormService;
  let additionalVariableService: AdditionalVariableService;
  let programVersionService: ProgramVersionService;
  let siteOrgTempService: SiteOrgTempService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AdditionalVariableUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(AdditionalVariableUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AdditionalVariableUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    additionalVariableFormService = TestBed.inject(AdditionalVariableFormService);
    additionalVariableService = TestBed.inject(AdditionalVariableService);
    programVersionService = TestBed.inject(ProgramVersionService);
    siteOrgTempService = TestBed.inject(SiteOrgTempService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ProgramVersion query and add missing value', () => {
      const additionalVariable: IAdditionalVariable = { id: 456 };
      const version: IProgramVersion = { id: 32276 };
      additionalVariable.version = version;

      const programVersionCollection: IProgramVersion[] = [{ id: 12510 }];
      jest.spyOn(programVersionService, 'query').mockReturnValue(of(new HttpResponse({ body: programVersionCollection })));
      const additionalProgramVersions = [version];
      const expectedCollection: IProgramVersion[] = [...additionalProgramVersions, ...programVersionCollection];
      jest.spyOn(programVersionService, 'addProgramVersionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ additionalVariable });
      comp.ngOnInit();

      expect(programVersionService.query).toHaveBeenCalled();
      expect(programVersionService.addProgramVersionToCollectionIfMissing).toHaveBeenCalledWith(
        programVersionCollection,
        ...additionalProgramVersions.map(expect.objectContaining),
      );
      expect(comp.programVersionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call SiteOrgTemp query and add missing value', () => {
      const additionalVariable: IAdditionalVariable = { id: 456 };
      const site: ISiteOrgTemp = { id: 11112 };
      additionalVariable.site = site;

      const siteOrgTempCollection: ISiteOrgTemp[] = [{ id: 8348 }];
      jest.spyOn(siteOrgTempService, 'query').mockReturnValue(of(new HttpResponse({ body: siteOrgTempCollection })));
      const additionalSiteOrgTemps = [site];
      const expectedCollection: ISiteOrgTemp[] = [...additionalSiteOrgTemps, ...siteOrgTempCollection];
      jest.spyOn(siteOrgTempService, 'addSiteOrgTempToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ additionalVariable });
      comp.ngOnInit();

      expect(siteOrgTempService.query).toHaveBeenCalled();
      expect(siteOrgTempService.addSiteOrgTempToCollectionIfMissing).toHaveBeenCalledWith(
        siteOrgTempCollection,
        ...additionalSiteOrgTemps.map(expect.objectContaining),
      );
      expect(comp.siteOrgTempsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const additionalVariable: IAdditionalVariable = { id: 456 };
      const version: IProgramVersion = { id: 18 };
      additionalVariable.version = version;
      const site: ISiteOrgTemp = { id: 25755 };
      additionalVariable.site = site;

      activatedRoute.data = of({ additionalVariable });
      comp.ngOnInit();

      expect(comp.programVersionsSharedCollection).toContain(version);
      expect(comp.siteOrgTempsSharedCollection).toContain(site);
      expect(comp.additionalVariable).toEqual(additionalVariable);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAdditionalVariable>>();
      const additionalVariable = { id: 123 };
      jest.spyOn(additionalVariableFormService, 'getAdditionalVariable').mockReturnValue(additionalVariable);
      jest.spyOn(additionalVariableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ additionalVariable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: additionalVariable }));
      saveSubject.complete();

      // THEN
      expect(additionalVariableFormService.getAdditionalVariable).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(additionalVariableService.update).toHaveBeenCalledWith(expect.objectContaining(additionalVariable));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAdditionalVariable>>();
      const additionalVariable = { id: 123 };
      jest.spyOn(additionalVariableFormService, 'getAdditionalVariable').mockReturnValue({ id: null });
      jest.spyOn(additionalVariableService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ additionalVariable: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: additionalVariable }));
      saveSubject.complete();

      // THEN
      expect(additionalVariableFormService.getAdditionalVariable).toHaveBeenCalled();
      expect(additionalVariableService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAdditionalVariable>>();
      const additionalVariable = { id: 123 };
      jest.spyOn(additionalVariableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ additionalVariable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(additionalVariableService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProgramVersion', () => {
      it('Should forward to programVersionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(programVersionService, 'compareProgramVersion');
        comp.compareProgramVersion(entity, entity2);
        expect(programVersionService.compareProgramVersion).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareSiteOrgTemp', () => {
      it('Should forward to siteOrgTempService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(siteOrgTempService, 'compareSiteOrgTemp');
        comp.compareSiteOrgTemp(entity, entity2);
        expect(siteOrgTempService.compareSiteOrgTemp).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
