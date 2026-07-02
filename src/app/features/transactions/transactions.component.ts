import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';

@Component({
  standalone: true,
  selector: 'app-transactions',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './transactions.component.html',
  styleUrls: ['./transactions.component.css']
})
export class TransactionsComponent implements OnInit {
  statusMessage = '';
  recordedAt = '';
  dailyForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this.dailyForm = this.fb.group({
      faturamento: [null, [Validators.required, Validators.min(0.01)]],
      gasolina: [0, [Validators.min(0)]],
      comida: [0, [Validators.min(0)]],
      date: [''],
      observation: [''],
      extras: this.fb.array([])
    });
  }

  ngOnInit(): void {}

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
    if (!extra) {
      return;
    }

    if (extra.invalid) {
      extra.markAllAsTouched();
      return;
    }

    extra.patchValue({ saved: true });
  }

  removeExtra(index: number): void {
    this.extras.removeAt(index);
  }

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

  submit(): void {
    if (this.dailyForm.invalid) {
      this.dailyForm.markAllAsTouched();
      this.extras.markAllAsTouched();
      return;
    }

    const values = this.dailyForm.value;
    const chosenDate = values.date?.trim() ? new Date(values.date) : new Date();
    const recordedDate = chosenDate.toISOString();

    this.recordedAt = new Date(recordedDate).toLocaleString('pt-BR', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
    this.statusMessage = 'Controle diário salvo com sucesso!';

    this.dailyForm.reset({
      faturamento: null,
      gasolina: 0,
      comida: 0,
      date: '',
      observation: '',
      extras: []
    });
    this.extras.clear();

    setTimeout(() => (this.statusMessage = ''), 3500);
  }

  get control() {
    return this.dailyForm.controls;
  }
}
