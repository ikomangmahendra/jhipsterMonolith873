import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUserTemp } from 'app/entities/user-temp/user-temp.model';
import { UserTempService } from 'app/entities/user-temp/service/user-temp.service';
import { ISiteOrganisation } from 'app/entities/site-organisation/site-organisation.model';
import { SiteOrganisationService } from 'app/entities/site-organisation/service/site-organisation.service';
import { IRole } from 'app/entities/role/role.model';
import { RoleService } from 'app/entities/role/service/role.service';
import { IUserSiteOrganisation } from '../user-site-organisation.model';
import { UserSiteOrganisationService } from '../service/user-site-organisation.service';
import { UserSiteOrganisationFormService } from './user-site-organisation-form.service';

import { UserSiteOrganisationUpdateComponent } from './user-site-organisation-update.component';

describe('UserSiteOrganisation Management Update Component', () => {
  let comp: UserSiteOrganisationUpdateComponent;
  let fixture: ComponentFixture<UserSiteOrganisationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let userSiteOrganisationFormService: UserSiteOrganisationFormService;
  let userSiteOrganisationService: UserSiteOrganisationService;
  let userTempService: UserTempService;
  let siteOrganisationService: SiteOrganisationService;
  let roleService: RoleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [UserSiteOrganisationUpdateComponent],
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
      .overrideTemplate(UserSiteOrganisationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UserSiteOrganisationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    userSiteOrganisationFormService = TestBed.inject(UserSiteOrganisationFormService);
    userSiteOrganisationService = TestBed.inject(UserSiteOrganisationService);
    userTempService = TestBed.inject(UserTempService);
    siteOrganisationService = TestBed.inject(SiteOrganisationService);
    roleService = TestBed.inject(RoleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call UserTemp query and add missing value', () => {
      const userSiteOrganisation: IUserSiteOrganisation = { id: 456 };
      const user: IUserTemp = { id: 27417 };
      userSiteOrganisation.user = user;

      const userTempCollection: IUserTemp[] = [{ id: 18487 }];
      jest.spyOn(userTempService, 'query').mockReturnValue(of(new HttpResponse({ body: userTempCollection })));
      const additionalUserTemps = [user];
      const expectedCollection: IUserTemp[] = [...additionalUserTemps, ...userTempCollection];
      jest.spyOn(userTempService, 'addUserTempToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userSiteOrganisation });
      comp.ngOnInit();

      expect(userTempService.query).toHaveBeenCalled();
      expect(userTempService.addUserTempToCollectionIfMissing).toHaveBeenCalledWith(
        userTempCollection,
        ...additionalUserTemps.map(expect.objectContaining),
      );
      expect(comp.userTempsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call SiteOrganisation query and add missing value', () => {
      const userSiteOrganisation: IUserSiteOrganisation = { id: 456 };
      const siteOrganisation: ISiteOrganisation = { id: 10361 };
      userSiteOrganisation.siteOrganisation = siteOrganisation;

      const siteOrganisationCollection: ISiteOrganisation[] = [{ id: 30447 }];
      jest.spyOn(siteOrganisationService, 'query').mockReturnValue(of(new HttpResponse({ body: siteOrganisationCollection })));
      const additionalSiteOrganisations = [siteOrganisation];
      const expectedCollection: ISiteOrganisation[] = [...additionalSiteOrganisations, ...siteOrganisationCollection];
      jest.spyOn(siteOrganisationService, 'addSiteOrganisationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userSiteOrganisation });
      comp.ngOnInit();

      expect(siteOrganisationService.query).toHaveBeenCalled();
      expect(siteOrganisationService.addSiteOrganisationToCollectionIfMissing).toHaveBeenCalledWith(
        siteOrganisationCollection,
        ...additionalSiteOrganisations.map(expect.objectContaining),
      );
      expect(comp.siteOrganisationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Role query and add missing value', () => {
      const userSiteOrganisation: IUserSiteOrganisation = { id: 456 };
      const role: IRole = { id: 30097 };
      userSiteOrganisation.role = role;

      const roleCollection: IRole[] = [{ id: 16922 }];
      jest.spyOn(roleService, 'query').mockReturnValue(of(new HttpResponse({ body: roleCollection })));
      const additionalRoles = [role];
      const expectedCollection: IRole[] = [...additionalRoles, ...roleCollection];
      jest.spyOn(roleService, 'addRoleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userSiteOrganisation });
      comp.ngOnInit();

      expect(roleService.query).toHaveBeenCalled();
      expect(roleService.addRoleToCollectionIfMissing).toHaveBeenCalledWith(
        roleCollection,
        ...additionalRoles.map(expect.objectContaining),
      );
      expect(comp.rolesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const userSiteOrganisation: IUserSiteOrganisation = { id: 456 };
      const user: IUserTemp = { id: 1299 };
      userSiteOrganisation.user = user;
      const siteOrganisation: ISiteOrganisation = { id: 5821 };
      userSiteOrganisation.siteOrganisation = siteOrganisation;
      const role: IRole = { id: 11353 };
      userSiteOrganisation.role = role;

      activatedRoute.data = of({ userSiteOrganisation });
      comp.ngOnInit();

      expect(comp.userTempsSharedCollection).toContain(user);
      expect(comp.siteOrganisationsSharedCollection).toContain(siteOrganisation);
      expect(comp.rolesSharedCollection).toContain(role);
      expect(comp.userSiteOrganisation).toEqual(userSiteOrganisation);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserSiteOrganisation>>();
      const userSiteOrganisation = { id: 123 };
      jest.spyOn(userSiteOrganisationFormService, 'getUserSiteOrganisation').mockReturnValue(userSiteOrganisation);
      jest.spyOn(userSiteOrganisationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userSiteOrganisation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userSiteOrganisation }));
      saveSubject.complete();

      // THEN
      expect(userSiteOrganisationFormService.getUserSiteOrganisation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(userSiteOrganisationService.update).toHaveBeenCalledWith(expect.objectContaining(userSiteOrganisation));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserSiteOrganisation>>();
      const userSiteOrganisation = { id: 123 };
      jest.spyOn(userSiteOrganisationFormService, 'getUserSiteOrganisation').mockReturnValue({ id: null });
      jest.spyOn(userSiteOrganisationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userSiteOrganisation: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userSiteOrganisation }));
      saveSubject.complete();

      // THEN
      expect(userSiteOrganisationFormService.getUserSiteOrganisation).toHaveBeenCalled();
      expect(userSiteOrganisationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserSiteOrganisation>>();
      const userSiteOrganisation = { id: 123 };
      jest.spyOn(userSiteOrganisationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userSiteOrganisation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(userSiteOrganisationService.update).toHaveBeenCalled();
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

    describe('compareSiteOrganisation', () => {
      it('Should forward to siteOrganisationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(siteOrganisationService, 'compareSiteOrganisation');
        comp.compareSiteOrganisation(entity, entity2);
        expect(siteOrganisationService.compareSiteOrganisation).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareRole', () => {
      it('Should forward to roleService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(roleService, 'compareRole');
        comp.compareRole(entity, entity2);
        expect(roleService.compareRole).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});