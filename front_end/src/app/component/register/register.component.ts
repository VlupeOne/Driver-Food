import { Router } from '@angular/router';
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {

  private apiUrl = 'http://localhost:8080/auth/register';

  mensagem = '';
  erro = '';
  form;
  carregando = false;

  constructor(private fb: FormBuilder, private http: HttpClient, private router: Router) {
    this.form = this.fb.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      cpf: ['', Validators.required],
      birthDate: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(6)]],
      motorcycle: ['', Validators.required],
      plate: ['', Validators.required]
    });
  }

  campoInvalido(campo: string): boolean {
    const control = this.form.get(campo);

    return !!(
      control &&
      control.invalid &&
      control.touched
    );

  }

register() {
  if (this.form.invalid) {
    this.form.markAllAsTouched();
    return;
  }

  const data = {
    type: 'MOTOBOY',
    ...this.form.value
  };

  this.http.post<any>(
    this.apiUrl,
    data,
    {
      withCredentials: true
    }
  )
  .subscribe({
    next: (response) => {
      this.mensagem = 'Cadastro realizado';

      if(response.role === 'ROLE_MOTOBOY'){
        this.router.navigate(['/motoboy/address']);
      }

      this.form.reset();
    },

    error: (err) => {
      this.erro = err.error ?? 'Erro no cadastro';
    }
  });
}


}
