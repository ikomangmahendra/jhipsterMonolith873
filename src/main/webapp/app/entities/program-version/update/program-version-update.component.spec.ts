import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IProgram } from 'app/entities/program/program.model';
import { ProgramService } from 'app/entities/program/service/program.service';
import { ProgramVersionService } from '../service/program-version.service';
import { IProgramVersion } from '../program-version.model';
import { ProgramVersionFormService } from './program-version-form.service';

import { ProgramVersionUpdateComponent } from './program-version-update.component';

describe('ProgramVersion Management Update Component', () => {
  let comp: ProgramVersionUpdateComponent;
  let fixture: ComponentFixture<ProgramVersionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let programVersionFormService: ProgramVersionFormService;
  let programVersionService: ProgramVersionService;
  let programService: ProgramService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ProgramVersionUpdateComponent],
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
      .overrideTemplate(ProgramVersionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProgramVersionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    programVersionFormService = TestBed.inject(ProgramVersionFormService);
    programVersionService = TestBed.inject(ProgramVersionService);
    programService = TestBed.inject(ProgramService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Program query and add missing value', () => {
      const programVersion: IProgramVersion = { id: 456 };
      const program: IProgram = { id: 20654 };
      programVersion.program = program;

      const programCollection: IProgram[] = [{ id: 27336 }];
      jest.spyOn(programService, 'query').mockReturnValue(of(new HttpResponse({ body: programCollection })));
      const additionalPrograms = [program];
      const expectedCollection: IProgram[] = [...additionalPrograms, ...programCollection];
      jest.spyOn(programService, 'addProgramToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ programVersion });
      comp.ngOnInit();

      expect(programService.query).toHaveBeenCalled();
      expect(programService.addProgramToCollectionIfMissing).toHaveBeenCalledWith(
        programCollection,
        ...additionalPrograms.map(expect.objectContaining),
      );
      expect(comp.programsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const programVersion: IProgramVersion = { id: 456 };
      const program: IProgram = { id: 2634 };
      programVersion.program = program;

      activatedRoute.data = of({ programVersion });
      comp.ngOnInit();

      expect(comp.programsSharedCollection).toContain(program);
      expect(comp.programVersion).toEqual(programVersion);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProgramVersion>>();
      const programVersion = { id: 123 };
      jest.spyOn(programVersionFormService, 'getProgramVersion').mockReturnValue(programVersion);
      jest.spyOn(programVersionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ programVersion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: programVersion }));
      saveSubject.complete();

      // THEN
      expect(programVersionFormService.getProgramVersion).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(programVersionService.update).toHaveBeenCalledWith(expect.objectContaining(programVersion));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProgramVersion>>();
      const programVersion = { id: 123 };
      jest.spyOn(programVersionFormService, 'getProgramVersion').mockReturnValue({ id: null });
      jest.spyOn(programVersionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ programVersion: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: programVersion }));
      saveSubject.complete();

      // THEN
      expect(programVersionFormService.getProgramVersion).toHaveBeenCalled();
      expect(programVersionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProgramVersion>>();
      const programVersion = { id: 123 };
      jest.spyOn(programVersionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ programVersion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(programVersionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProgram', () => {
      it('Should forward to programService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(programService, 'compareProgram');
        comp.compareProgram(entity, entity2);
        expect(programService.compareProgram).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
