import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { UserSiteOrganisationDetailComponent } from './user-site-organisation-detail.component';

describe('UserSiteOrganisation Management Detail Component', () => {
  let comp: UserSiteOrganisationDetailComponent;
  let fixture: ComponentFixture<UserSiteOrganisationDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserSiteOrganisationDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./user-site-organisation-detail.component').then(m => m.UserSiteOrganisationDetailComponent),
              resolve: { userSiteOrganisation: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(UserSiteOrganisationDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UserSiteOrganisationDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load userSiteOrganisation on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', UserSiteOrganisationDetailComponent);

      // THEN
      expect(instance.userSiteOrganisation()).toEqual(expect.objectContaining({ id: 123 }));
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
