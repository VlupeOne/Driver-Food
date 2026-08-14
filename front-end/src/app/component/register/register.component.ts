import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { jwtDecode } from 'jwt-decode';
import { environment } from '../../environment/environment';
import { OAuthPayload } from '../../model/OAuthPayload';

import {
  debounceTime,
  distinctUntilChanged,
  filter,
  switchMap
} from 'rxjs/operators';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {

  formulario!: FormGroup;
  carregando = false;
  isOAuth = false;
  private oauthToken: string | null = null;
  emailDisponivel: boolean | null = null;
  cpfDisponivel: boolean | null = null;
  mensagemEmail = '';
  mensagemCpf = '';

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private router: Router
  ){
    this.criarFormulario();
    this.carregarDadosOAuth();
    this.monitorarEmail();
    this.monitorarCpf();
  }

  private criarFormulario(): void {
    this.formulario = this.fb.group({
      nome: [
        '',
        Validators.required
      ],
      email: [
        '',
        [
          Validators.required,
          Validators.email
        ]
      ],
      senha: [
        '',
        [
          Validators.minLength(6)
        ]
      ],
      cpf: [
        '',
        Validators.required
      ],
      dataNascimento: [
        '',
        Validators.required
      ],
      motocicleta: [
        '',
        Validators.required
      ],
      placa: [
        '',
        Validators.required
      ]
    });
  }

private carregarDadosOAuth(): void {

  this.http.get<OAuthPayload>(
    `${environment.apiUrl}/oauth/me`,
    {
      withCredentials: true
    }
  )
  .subscribe({

    next: (dados) => {

      this.isOAuth = true;

      this.formulario.patchValue({
        nome: dados.name,
        email: dados.email
      });


      this.formulario
        .get('nome')
        ?.disable();


      this.formulario
        .get('email')
        ?.disable();

    },


    error: (error) => {

      console.error(
        'Usuário não autenticado via OAuth:',
        error
      );

      this.isOAuth = false;

    }

  });

}

  private monitorarEmail(): void {
    const campo = this.formulario.get('email');

    if (!campo) {
      return;
    }
    campo.valueChanges.pipe(
      debounceTime(500),
      distinctUntilChanged(),
      filter(() => campo.valid),
      switchMap(email =>
        this.http.get<AvailabilityResponse>(
          `${environment.apiUrl}/persons/email-available`,
          {
            params: {
              email
            }
          }
        )
      )

    ).subscribe({
      next: (response) => {

        this.emailDisponivel = response.available;

        this.mensagemEmail = response.message;

      },

      error: () => {
        this.emailDisponivel = null;
        this.mensagemEmail = '';
      }

    });

  }

  private monitorarCpf(): void {
  const campo = this.formulario.get('cpf');

  if (!campo) {
    return;
  }

  campo.valueChanges.pipe(

    debounceTime(500),

    distinctUntilChanged(),

    filter(cpf => !!cpf && cpf.length === 11),

    switchMap(cpf =>
      this.http.get<AvailabilityResponse>(
        `${environment.apiUrl}/persons/cpf-available`,
        {
          params: {
            cpf
          }
        }
      )
    )
  ).subscribe({
    next: (response) => {
      this.cpfDisponivel = response.available;
      this.mensagemCpf = response.message;
    },
    error: () => {
      this.cpfDisponivel = null;
      this.mensagemCpf = '';
    }
  });

  }

  cadastrar(): void {
    if (this.cpfDisponivel === false) {
      return;
    }
    if (this.formulario.invalid) {
      this.formulario.markAllAsTouched();
      return;
    }

    this.carregando = true;

    if (this.isOAuth) {
      this.cadastrarOAuth();
      return;
    }

    this.cadastrarNormal();

  }

private cadastrarOAuth(): void {

  const body = {
    cpf: this.formulario.value.cpf,
    birthDate: this.formulario.value.dataNascimento,
    motorcycle: this.formulario.value.motocicleta,
    plate: this.formulario.value.placa
  };


  this.http.post(
    `${environment.apiUrl}/oauth/complete`,
    body,
    {
      withCredentials: true,
      responseType: 'text'
    }
  )
  .subscribe({
    next: (response) => {

      alert(
        "Cadastro OAuth concluído!"
      );

      this.router.navigate(['/']);

    },

    error: (error) => {

      console.error(error);

      alert(
        "Erro ao completar cadastro."
      );

    }
  });

}

  private cadastrarNormal(): void {
    const body = {
      name:
        this.formulario.value.nome,
      email:
        this.formulario.value.email,
      password:
        this.formulario.value.senha,
      cpf:
        this.formulario.value.cpf,
      birthDate:
        this.formulario.value.dataNascimento,
      motorcycle:
        this.formulario.value.motocicleta,
      plate:
        this.formulario.value.placa
    };
    this.http.post(
      `${environment.apiUrl}/auth/register`,
      body
    )
    .subscribe({
      next: () => {
        alert(
          'Cadastro realizado com sucesso!'
        );
        this.router.navigate(['']);
        this.formulario.reset();
        this.carregando = false;
      },
      error: () => {
        alert(
          'Erro ao cadastrar.'
        );
        this.carregando = false;
      }
    });
  }

}
