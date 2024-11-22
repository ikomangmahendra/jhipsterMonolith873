jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, fakeAsync, inject, tick } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { AccuteProgramVariableService } from '../service/accute-program-variable.service';

import { AccuteProgramVariableDeleteDialogComponent } from './accute-program-variable-delete-dialog.component';

describe('AccuteProgramVariable Management Delete Component', () => {
  let comp: AccuteProgramVariableDeleteDialogComponent;
  let fixture: ComponentFixture<AccuteProgramVariableDeleteDialogComponent>;
  let service: AccuteProgramVariableService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AccuteProgramVariableDeleteDialogComponent],
      providers: [provideHttpClient(), NgbActiveModal],
    })
      .overrideTemplate(AccuteProgramVariableDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AccuteProgramVariableDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(AccuteProgramVariableService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      }),
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
