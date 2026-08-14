import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  private apiUrl = 'http://localhost:8080/auth/login';

  carregando = false;
  erro = '';

  form;

  constructor(private fb: FormBuilder, private http: HttpClient, private router: Router) {
    this.form = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
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

  login() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.carregando = true;
    this.erro = '';

    this.http.post(
      this.apiUrl,
      this.form.value,
      {
        withCredentials: true
      }
    ).subscribe({

      next: () => {
        this.carregando = false;

        // Temporariamente
        // Depois da implementação da consulta da role
        // faça o redirecionamento correto.
        this.router.navigate(['/']);
      },
      error: (err) => {
        this.carregando = false;
        this.erro = err.error ?? 'Email ou senha inválidos';
      }
    });

  }

}
