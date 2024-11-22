import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUserTemp } from 'app/entities/user-temp/user-temp.model';
import { UserTempService } from 'app/entities/user-temp/service/user-temp.service';
import { ProgramService } from '../service/program.service';
import { IProgram } from '../program.model';
import { ProgramFormService } from './program-form.service';

import { ProgramUpdateComponent } from './program-update.component';

describe('Program Management Update Component', () => {
  let comp: ProgramUpdateComponent;
  let fixture: ComponentFixture<ProgramUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let programFormService: ProgramFormService;
  let programService: ProgramService;
  let userTempService: UserTempService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ProgramUpdateComponent],
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
      .overrideTemplate(ProgramUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProgramUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    programFormService = TestBed.inject(ProgramFormService);
    programService = TestBed.inject(ProgramService);
    userTempService = TestBed.inject(UserTempService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call UserTemp query and add missing value', () => {
      const program: IProgram = { id: 456 };
      const coordinators: IUserTemp[] = [{ id: 7491 }];
      program.coordinators = coordinators;

      const userTempCollection: IUserTemp[] = [{ id: 30488 }];
      jest.spyOn(userTempService, 'query').mockReturnValue(of(new HttpResponse({ body: userTempCollection })));
      const additionalUserTemps = [...coordinators];
      const expectedCollection: IUserTemp[] = [...additionalUserTemps, ...userTempCollection];
      jest.spyOn(userTempService, 'addUserTempToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ program });
      comp.ngOnInit();

      expect(userTempService.query).toHaveBeenCalled();
      expect(userTempService.addUserTempToCollectionIfMissing).toHaveBeenCalledWith(
        userTempCollection,
        ...additionalUserTemps.map(expect.objectContaining),
      );
      expect(comp.userTempsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const program: IProgram = { id: 456 };
      const coordinators: IUserTemp = { id: 31777 };
      program.coordinators = [coordinators];

      activatedRoute.data = of({ program });
      comp.ngOnInit();

      expect(comp.userTempsSharedCollection).toContain(coordinators);
      expect(comp.program).toEqual(program);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProgram>>();
      const program = { id: 123 };
      jest.spyOn(programFormService, 'getProgram').mockReturnValue(program);
      jest.spyOn(programService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ program });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: program }));
      saveSubject.complete();

      // THEN
      expect(programFormService.getProgram).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(programService.update).toHaveBeenCalledWith(expect.objectContaining(program));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProgram>>();
      const program = { id: 123 };
      jest.spyOn(programFormService, 'getProgram').mockReturnValue({ id: null });
      jest.spyOn(programService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ program: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: program }));
      saveSubject.complete();

      // THEN
      expect(programFormService.getProgram).toHaveBeenCalled();
      expect(programService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProgram>>();
      const program = { id: 123 };
      jest.spyOn(programService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ program });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(programService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUserTemp', () => {
      it('Should forward to userTempService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userTempService, 'compareUserTemp');
        comp.compareUserTemp(entity, entity2);
        expect(userTempService.compareUserTemp).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
