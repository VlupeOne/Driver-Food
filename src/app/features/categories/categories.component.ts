import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CategoryService } from '../../core/services/category.service';
import { Category } from '../../core/models/category.model';
import { Observable } from 'rxjs';

@Component({
  standalone: true,
  selector: 'app-categories',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './categories.component.html',
  styleUrls: ['./categories.component.css']
})
export class CategoriesComponent implements OnInit {
  categories$!: Observable<Category[]>;
  successMessage = '';
  icons = ['local_shipping', 'payments', 'local_gas_station', 'restaurant', 'build', 'more_horiz', 'wallet', 'support_agent'];
  categoryTypes = [
    { value: 'receita', label: 'Ganhos', icon: 'arrow_upward' },
    { value: 'despesa', label: 'Gastos', icon: 'arrow_downward' }
  ];
  categoryForm!: FormGroup;

  constructor(private fb: FormBuilder, private categoryService: CategoryService) {
    this.categoryForm = this.fb.group({
      name: ['', Validators.required],
      icon: ['local_shipping', Validators.required],
      color: ['#4F46E5', Validators.required],
      type: ['despesa', Validators.required]
    });
  }

  ngOnInit(): void {
    this.categories$ = this.categoryService.getCategories();
  }

  submit(): void {
    if (this.categoryForm.invalid) {
      this.categoryForm.markAllAsTouched();
      return;
    }

    const category = this.categoryForm.value as Omit<Category, 'id'>;
    this.categoryService.createCategory(category).subscribe(() => {
      this.successMessage = 'Categoria criada com sucesso!';
      this.categoryForm.reset({
        name: '',
        icon: 'local_shipping',
        color: '#4F46E5',
        type: 'despesa'
      });
      setTimeout(() => (this.successMessage = ''), 3500);
    });
  }

  get control() {
    return this.categoryForm.controls;
  }
}
