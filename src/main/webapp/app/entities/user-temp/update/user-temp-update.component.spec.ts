import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

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

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const userTemp: IUserTemp = { id: 456 };

      activatedRoute.data = of({ userTemp });
      comp.ngOnInit();

      expect(comp.userTemp).toEqual(userTemp);
    });
  });

  describe('save', () => {
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
  });
});
