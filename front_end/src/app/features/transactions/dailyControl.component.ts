import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { DailyControlsService } from '../../services/daily-controls.service';

@Component({
  standalone: true,
  selector: 'app-daily-control',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './dailyControl.component.html',
  styleUrls: ['./dailyControl.component.css']
})
export class DailyControlComponent implements OnInit {

  statusMessage = '';
  recordedAt = '';
  dailyForm: FormGroup;

  constructor(private fb: FormBuilder, private dailyControlsService: DailyControlsService) {
  this.dailyForm = this.fb.group({
    faturamento: [null, [Validators.required, Validators.min(0.01)]],
    date: [''],
    gasolina: [0, [Validators.min(0)]],
    comida: [0, [Validators.min(0)]],
    observation: [''],
    extras: this.fb.array([])
  });

  }

  ngOnInit(): void {}

  // =========================
  // EXTRAS
  // =========================

  get extras(): FormArray {
    return this.dailyForm.get('extras') as FormArray;
  }

  createExtraItem(): FormGroup {
    return this.fb.group({
      description: ['', Validators.required],
      amount: [0, [Validators.required, Validators.min(0.01)]],
      saved: [false]
    });
  }

  addExtra(): void {
    this.extras.push(this.createExtraItem());
  }

  saveExtra(index: number): void {
    const extra = this.extras.at(index);

    if (!extra) return;

    if (extra.invalid) {
      extra.markAllAsTouched();
      return;
    }

    extra.patchValue({ saved: true });
  }

  removeExtra(index: number): void {
    this.extras.removeAt(index);
  }

  // =========================
  // CALCULOS FRONT
  // =========================

  get totalExtras(): number {
    return this.extras.controls.reduce((sum, item) => {
      return item.get('saved')?.value
        ? sum + Number(item.get('amount')?.value || 0)
        : sum;
    }, 0);
  }

  get totalExpenses(): number {
    return (
      Number(this.dailyForm.get('gasolina')?.value || 0) +
      Number(this.dailyForm.get('comida')?.value || 0) +
      this.totalExtras
    );
  }

  get profit(): number {
    return Number(this.dailyForm.get('faturamento')?.value || 0) - this.totalExpenses;
  }

  // =========================
  // SUBMIT (BACKEND)
  // =========================

submit(): void {

  if (this.dailyForm.invalid) {
    this.dailyForm.markAllAsTouched();
    return;
  }

  const request = {
    faturamento: Number(this.dailyForm.value.faturamento),
    gasolina: Number(this.dailyForm.value.gasolina || 0),
    comida: Number(this.dailyForm.value.comida || 0),
    observation: this.dailyForm.value.observation || ''
  };

  this.dailyControlsService.create(request).subscribe({
    next: (response) => {
      this.statusMessage = 'Controle salvo com sucesso';
      this.recordedAt = new Date().toLocaleString();

      console.log('Resposta backend:', response);

      this.dailyForm.reset({
        faturamento: null,
        gasolina: 0,
        comida: 0,
        observation: '',
        extras: []
      });
    },

    error: (error) => {
      console.error('Erro ao salvar controle:', error);
      this.statusMessage = 'Erro ao salvar controle';
    }
  });
}



  // =========================
  // HELPERS
  // =========================

  get control() {
    return this.dailyForm.controls;
  }
}
