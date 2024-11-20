import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ConsumerService } from '../service/consumer.service';
import { IConsumer } from '../consumer.model';
import { ConsumerFormService } from './consumer-form.service';

import { ConsumerUpdateComponent } from './consumer-update.component';

describe('Consumer Management Update Component', () => {
  let comp: ConsumerUpdateComponent;
  let fixture: ComponentFixture<ConsumerUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let consumerFormService: ConsumerFormService;
  let consumerService: ConsumerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ConsumerUpdateComponent],
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
      .overrideTemplate(ConsumerUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ConsumerUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    consumerFormService = TestBed.inject(ConsumerFormService);
    consumerService = TestBed.inject(ConsumerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const consumer: IConsumer = { id: 456 };

      activatedRoute.data = of({ consumer });
      comp.ngOnInit();

      expect(comp.consumer).toEqual(consumer);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConsumer>>();
      const consumer = { id: 123 };
      jest.spyOn(consumerFormService, 'getConsumer').mockReturnValue(consumer);
      jest.spyOn(consumerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ consumer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: consumer }));
      saveSubject.complete();

      // THEN
      expect(consumerFormService.getConsumer).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(consumerService.update).toHaveBeenCalledWith(expect.objectContaining(consumer));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConsumer>>();
      const consumer = { id: 123 };
      jest.spyOn(consumerFormService, 'getConsumer').mockReturnValue({ id: null });
      jest.spyOn(consumerService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ consumer: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: consumer }));
      saveSubject.complete();

      // THEN
      expect(consumerFormService.getConsumer).toHaveBeenCalled();
      expect(consumerService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConsumer>>();
      const consumer = { id: 123 };
      jest.spyOn(consumerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ consumer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(consumerService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
