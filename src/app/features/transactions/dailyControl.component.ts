import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { DailyControlRequest, DailyControlService } from '../../core/services/dailyControlService.service';


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

  constructor(
    private fb: FormBuilder,
    private dailyControlService: DailyControlService
  ) {
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
      this.extras.markAllAsTouched();
      return;
    }

    const values = this.dailyForm.value;

    const payload: DailyControlRequest = {
      faturamento: values.faturamento,
      gasolina: values.gasolina,
      comida: values.comida,
      observation: values.observation || undefined,
      extras: values.extras
        ?.filter((e: any) => e.saved)
        .map((e: any) => ({
          description: e.description,
          amount: e.amount
        })) || []
    };

    this.dailyControlService.create(payload).subscribe({
      next: (response) => {

        this.statusMessage = 'Controle diário salvo com sucesso!';

        const date = response.recordedAt ?? new Date().toISOString();

        this.recordedAt = new Date(date).toLocaleString('pt-BR', {
          day: '2-digit',
          month: '2-digit',
          year: 'numeric',
          hour: '2-digit',
          minute: '2-digit'
        });

        this.dailyForm.reset({
          faturamento: null,
          date: '',
          gasolina: 0,
          comida: 0,
          observation: '',
          extras: []
        });

        this.extras.clear();

        setTimeout(() => (this.statusMessage = ''), 3500);
      },
      error: (err) => {
        console.error(err);
        this.statusMessage = 'Erro ao salvar controle diário.';
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
