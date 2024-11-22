import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { SiteOrgTempService } from '../service/site-org-temp.service';
import { ISiteOrgTemp } from '../site-org-temp.model';
import { SiteOrgTempFormService } from './site-org-temp-form.service';

import { SiteOrgTempUpdateComponent } from './site-org-temp-update.component';

describe('SiteOrgTemp Management Update Component', () => {
  let comp: SiteOrgTempUpdateComponent;
  let fixture: ComponentFixture<SiteOrgTempUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let siteOrgTempFormService: SiteOrgTempFormService;
  let siteOrgTempService: SiteOrgTempService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SiteOrgTempUpdateComponent],
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
      .overrideTemplate(SiteOrgTempUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SiteOrgTempUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    siteOrgTempFormService = TestBed.inject(SiteOrgTempFormService);
    siteOrgTempService = TestBed.inject(SiteOrgTempService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const siteOrgTemp: ISiteOrgTemp = { id: 456 };

      activatedRoute.data = of({ siteOrgTemp });
      comp.ngOnInit();

      expect(comp.siteOrgTemp).toEqual(siteOrgTemp);
    });
  });

  describe('save', () => {
    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISiteOrgTemp>>();
      const siteOrgTemp = { id: 123 };
      jest.spyOn(siteOrgTempFormService, 'getSiteOrgTemp').mockReturnValue({ id: null });
      jest.spyOn(siteOrgTempService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ siteOrgTemp: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: siteOrgTemp }));
      saveSubject.complete();

      // THEN
      expect(siteOrgTempFormService.getSiteOrgTemp).toHaveBeenCalled();
      expect(siteOrgTempService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });
  });
});
