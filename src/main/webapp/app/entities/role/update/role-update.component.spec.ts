import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUserTemp } from 'app/entities/user-temp/user-temp.model';
import { UserTempService } from 'app/entities/user-temp/service/user-temp.service';
import { IUserSiteOrganisation } from 'app/entities/user-site-organisation/user-site-organisation.model';
import { UserSiteOrganisationService } from 'app/entities/user-site-organisation/service/user-site-organisation.service';
import { IRole } from '../role.model';
import { RoleService } from '../service/role.service';
import { RoleFormService } from './role-form.service';

import { RoleUpdateComponent } from './role-update.component';

describe('Role Management Update Component', () => {
  let comp: RoleUpdateComponent;
  let fixture: ComponentFixture<RoleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let roleFormService: RoleFormService;
  let roleService: RoleService;
  let userTempService: UserTempService;
  let userSiteOrganisationService: UserSiteOrganisationService;

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
    userTempService = TestBed.inject(UserTempService);
    userSiteOrganisationService = TestBed.inject(UserSiteOrganisationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call UserTemp query and add missing value', () => {
      const role: IRole = { id: 456 };
      const user: IUserTemp = { id: 32083 };
      role.user = user;

      const userTempCollection: IUserTemp[] = [{ id: 19405 }];
      jest.spyOn(userTempService, 'query').mockReturnValue(of(new HttpResponse({ body: userTempCollection })));
      const additionalUserTemps = [user];
      const expectedCollection: IUserTemp[] = [...additionalUserTemps, ...userTempCollection];
      jest.spyOn(userTempService, 'addUserTempToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ role });
      comp.ngOnInit();

      expect(userTempService.query).toHaveBeenCalled();
      expect(userTempService.addUserTempToCollectionIfMissing).toHaveBeenCalledWith(
        userTempCollection,
        ...additionalUserTemps.map(expect.objectContaining),
      );
      expect(comp.userTempsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call UserSiteOrganisation query and add missing value', () => {
      const role: IRole = { id: 456 };
      const userSiteOrganisations: IUserSiteOrganisation[] = [{ id: 14150 }];
      role.userSiteOrganisations = userSiteOrganisations;

      const userSiteOrganisationCollection: IUserSiteOrganisation[] = [{ id: 316 }];
      jest.spyOn(userSiteOrganisationService, 'query').mockReturnValue(of(new HttpResponse({ body: userSiteOrganisationCollection })));
      const additionalUserSiteOrganisations = [...userSiteOrganisations];
      const expectedCollection: IUserSiteOrganisation[] = [...additionalUserSiteOrganisations, ...userSiteOrganisationCollection];
      jest.spyOn(userSiteOrganisationService, 'addUserSiteOrganisationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ role });
      comp.ngOnInit();

      expect(userSiteOrganisationService.query).toHaveBeenCalled();
      expect(userSiteOrganisationService.addUserSiteOrganisationToCollectionIfMissing).toHaveBeenCalledWith(
        userSiteOrganisationCollection,
        ...additionalUserSiteOrganisations.map(expect.objectContaining),
      );
      expect(comp.userSiteOrganisationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const role: IRole = { id: 456 };
      const user: IUserTemp = { id: 6735 };
      role.user = user;
      const userSiteOrganisation: IUserSiteOrganisation = { id: 7396 };
      role.userSiteOrganisations = [userSiteOrganisation];

      activatedRoute.data = of({ role });
      comp.ngOnInit();

      expect(comp.userTempsSharedCollection).toContain(user);
      expect(comp.userSiteOrganisationsSharedCollection).toContain(userSiteOrganisation);
      expect(comp.role).toEqual(role);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRole>>();
      const role = { id: 123 };
      jest.spyOn(roleFormService, 'getRole').mockReturnValue(role);
      jest.spyOn(roleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ role });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: role }));
      saveSubject.complete();

      // THEN
      expect(roleFormService.getRole).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(roleService.update).toHaveBeenCalledWith(expect.objectContaining(role));
      expect(comp.isSaving).toEqual(false);
    });

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

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRole>>();
      const role = { id: 123 };
      jest.spyOn(roleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ role });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(roleService.update).toHaveBeenCalled();
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

    describe('compareUserSiteOrganisation', () => {
      it('Should forward to userSiteOrganisationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userSiteOrganisationService, 'compareUserSiteOrganisation');
        comp.compareUserSiteOrganisation(entity, entity2);
        expect(userSiteOrganisationService.compareUserSiteOrganisation).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
