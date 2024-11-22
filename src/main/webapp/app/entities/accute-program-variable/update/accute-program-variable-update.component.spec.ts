import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IProgramVersion } from 'app/entities/program-version/program-version.model';
import { ProgramVersionService } from 'app/entities/program-version/service/program-version.service';
import { AccuteProgramVariableService } from '../service/accute-program-variable.service';
import { IAccuteProgramVariable } from '../accute-program-variable.model';
import { AccuteProgramVariableFormService } from './accute-program-variable-form.service';

import { AccuteProgramVariableUpdateComponent } from './accute-program-variable-update.component';

describe('AccuteProgramVariable Management Update Component', () => {
  let comp: AccuteProgramVariableUpdateComponent;
  let fixture: ComponentFixture<AccuteProgramVariableUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let accuteProgramVariableFormService: AccuteProgramVariableFormService;
  let accuteProgramVariableService: AccuteProgramVariableService;
  let programVersionService: ProgramVersionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AccuteProgramVariableUpdateComponent],
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
      .overrideTemplate(AccuteProgramVariableUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AccuteProgramVariableUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    accuteProgramVariableFormService = TestBed.inject(AccuteProgramVariableFormService);
    accuteProgramVariableService = TestBed.inject(AccuteProgramVariableService);
    programVersionService = TestBed.inject(ProgramVersionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ProgramVersion query and add missing value', () => {
      const accuteProgramVariable: IAccuteProgramVariable = { id: 456 };
      const version: IProgramVersion = { id: 10331 };
      accuteProgramVariable.version = version;

      const programVersionCollection: IProgramVersion[] = [{ id: 5251 }];
      jest.spyOn(programVersionService, 'query').mockReturnValue(of(new HttpResponse({ body: programVersionCollection })));
      const additionalProgramVersions = [version];
      const expectedCollection: IProgramVersion[] = [...additionalProgramVersions, ...programVersionCollection];
      jest.spyOn(programVersionService, 'addProgramVersionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ accuteProgramVariable });
      comp.ngOnInit();

      expect(programVersionService.query).toHaveBeenCalled();
      expect(programVersionService.addProgramVersionToCollectionIfMissing).toHaveBeenCalledWith(
        programVersionCollection,
        ...additionalProgramVersions.map(expect.objectContaining),
      );
      expect(comp.programVersionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const accuteProgramVariable: IAccuteProgramVariable = { id: 456 };
      const version: IProgramVersion = { id: 19496 };
      accuteProgramVariable.version = version;

      activatedRoute.data = of({ accuteProgramVariable });
      comp.ngOnInit();

      expect(comp.programVersionsSharedCollection).toContain(version);
      expect(comp.accuteProgramVariable).toEqual(accuteProgramVariable);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAccuteProgramVariable>>();
      const accuteProgramVariable = { id: 123 };
      jest.spyOn(accuteProgramVariableFormService, 'getAccuteProgramVariable').mockReturnValue(accuteProgramVariable);
      jest.spyOn(accuteProgramVariableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accuteProgramVariable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: accuteProgramVariable }));
      saveSubject.complete();

      // THEN
      expect(accuteProgramVariableFormService.getAccuteProgramVariable).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(accuteProgramVariableService.update).toHaveBeenCalledWith(expect.objectContaining(accuteProgramVariable));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAccuteProgramVariable>>();
      const accuteProgramVariable = { id: 123 };
      jest.spyOn(accuteProgramVariableFormService, 'getAccuteProgramVariable').mockReturnValue({ id: null });
      jest.spyOn(accuteProgramVariableService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accuteProgramVariable: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: accuteProgramVariable }));
      saveSubject.complete();

      // THEN
      expect(accuteProgramVariableFormService.getAccuteProgramVariable).toHaveBeenCalled();
      expect(accuteProgramVariableService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAccuteProgramVariable>>();
      const accuteProgramVariable = { id: 123 };
      jest.spyOn(accuteProgramVariableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accuteProgramVariable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(accuteProgramVariableService.update).toHaveBeenCalled();
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
  });
});
