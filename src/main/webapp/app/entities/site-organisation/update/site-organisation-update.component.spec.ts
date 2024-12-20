import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { SiteOrganisationService } from '../service/site-organisation.service';
import { ISiteOrganisation } from '../site-organisation.model';
import { SiteOrganisationFormService } from './site-organisation-form.service';

import { SiteOrganisationUpdateComponent } from './site-organisation-update.component';

describe('SiteOrganisation Management Update Component', () => {
  let comp: SiteOrganisationUpdateComponent;
  let fixture: ComponentFixture<SiteOrganisationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let siteOrganisationFormService: SiteOrganisationFormService;
  let siteOrganisationService: SiteOrganisationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SiteOrganisationUpdateComponent],
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
      .overrideTemplate(SiteOrganisationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SiteOrganisationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    siteOrganisationFormService = TestBed.inject(SiteOrganisationFormService);
    siteOrganisationService = TestBed.inject(SiteOrganisationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const siteOrganisation: ISiteOrganisation = { id: 456 };

      activatedRoute.data = of({ siteOrganisation });
      comp.ngOnInit();

      expect(comp.siteOrganisation).toEqual(siteOrganisation);
    });
  });

  describe('save', () => {
    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISiteOrganisation>>();
      const siteOrganisation = { id: 123 };
      jest.spyOn(siteOrganisationFormService, 'getSiteOrganisation').mockReturnValue({ id: null });
      jest.spyOn(siteOrganisationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ siteOrganisation: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: siteOrganisation }));
      saveSubject.complete();

      // THEN
      expect(siteOrganisationFormService.getSiteOrganisation).toHaveBeenCalled();
      expect(siteOrganisationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });
  });
});
