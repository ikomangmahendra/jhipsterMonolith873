import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { RoleService } from '../service/role.service';
import { IRole } from '../role.model';
import { RoleFormService } from './role-form.service';

import { RoleUpdateComponent } from './role-update.component';

describe('Role Management Update Component', () => {
  let comp: RoleUpdateComponent;
  let fixture: ComponentFixture<RoleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let roleFormService: RoleFormService;
  let roleService: RoleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RoleUpdateComponent],
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
      .overrideTemplate(RoleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RoleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    roleFormService = TestBed.inject(RoleFormService);
    roleService = TestBed.inject(RoleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const role: IRole = { id: 456 };

      activatedRoute.data = of({ role });
      comp.ngOnInit();

      expect(comp.role).toEqual(role);
    });
  });

  describe('save', () => {
    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRole>>();
      const role = { id: 123 };
      jest.spyOn(roleFormService, 'getRole').mockReturnValue({ id: null });
      jest.spyOn(roleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ role: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: role }));
      saveSubject.complete();

      // THEN
      expect(roleFormService.getRole).toHaveBeenCalled();
      expect(roleService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });
  });
});
