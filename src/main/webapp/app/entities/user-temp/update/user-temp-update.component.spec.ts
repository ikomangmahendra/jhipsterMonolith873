import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IProgram } from 'app/entities/program/program.model';
import { ProgramService } from 'app/entities/program/service/program.service';
import { UserTempService } from '../service/user-temp.service';
import { IUserTemp } from '../user-temp.model';
import { UserTempFormService } from './user-temp-form.service';

import { UserTempUpdateComponent } from './user-temp-update.component';

describe('UserTemp Management Update Component', () => {
  let comp: UserTempUpdateComponent;
  let fixture: ComponentFixture<UserTempUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let userTempFormService: UserTempFormService;
  let userTempService: UserTempService;
  let programService: ProgramService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [UserTempUpdateComponent],
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
      .overrideTemplate(UserTempUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UserTempUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    userTempFormService = TestBed.inject(UserTempFormService);
    userTempService = TestBed.inject(UserTempService);
    programService = TestBed.inject(ProgramService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Program query and add missing value', () => {
      const userTemp: IUserTemp = { id: 456 };
      const programs: IProgram[] = [{ id: 25760 }];
      userTemp.programs = programs;

      const programCollection: IProgram[] = [{ id: 21379 }];
      jest.spyOn(programService, 'query').mockReturnValue(of(new HttpResponse({ body: programCollection })));
      const additionalPrograms = [...programs];
      const expectedCollection: IProgram[] = [...additionalPrograms, ...programCollection];
      jest.spyOn(programService, 'addProgramToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userTemp });
      comp.ngOnInit();

      expect(programService.query).toHaveBeenCalled();
      expect(programService.addProgramToCollectionIfMissing).toHaveBeenCalledWith(
        programCollection,
        ...additionalPrograms.map(expect.objectContaining),
      );
      expect(comp.programsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const userTemp: IUserTemp = { id: 456 };
      const program: IProgram = { id: 10566 };
      userTemp.programs = [program];

      activatedRoute.data = of({ userTemp });
      comp.ngOnInit();

      expect(comp.programsSharedCollection).toContain(program);
      expect(comp.userTemp).toEqual(userTemp);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserTemp>>();
      const userTemp = { id: 123 };
      jest.spyOn(userTempFormService, 'getUserTemp').mockReturnValue(userTemp);
      jest.spyOn(userTempService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userTemp });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userTemp }));
      saveSubject.complete();

      // THEN
      expect(userTempFormService.getUserTemp).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(userTempService.update).toHaveBeenCalledWith(expect.objectContaining(userTemp));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserTemp>>();
      const userTemp = { id: 123 };
      jest.spyOn(userTempFormService, 'getUserTemp').mockReturnValue({ id: null });
      jest.spyOn(userTempService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userTemp: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userTemp }));
      saveSubject.complete();

      // THEN
      expect(userTempFormService.getUserTemp).toHaveBeenCalled();
      expect(userTempService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserTemp>>();
      const userTemp = { id: 123 };
      jest.spyOn(userTempService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userTemp });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(userTempService.update).toHaveBeenCalled();
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
