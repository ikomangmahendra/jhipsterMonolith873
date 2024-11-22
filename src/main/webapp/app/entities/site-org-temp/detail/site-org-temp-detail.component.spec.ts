import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { SiteOrgTempDetailComponent } from './site-org-temp-detail.component';

describe('SiteOrgTemp Management Detail Component', () => {
  let comp: SiteOrgTempDetailComponent;
  let fixture: ComponentFixture<SiteOrgTempDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SiteOrgTempDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./site-org-temp-detail.component').then(m => m.SiteOrgTempDetailComponent),
              resolve: { siteOrgTemp: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(SiteOrgTempDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SiteOrgTempDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load siteOrgTemp on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', SiteOrgTempDetailComponent);

      // THEN
      expect(instance.siteOrgTemp()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
