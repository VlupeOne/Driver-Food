import { Router } from '@angular/router';
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-motoboy-address',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './motoboyAddress.component.html',
  styleUrl: '../register/register.component.css'
})
export class MotoboyAddressComponent {

  cep = '';

  mensagem = '';
  erro = '';

  address = {
    cep: '',
    logradouro: '',
    complemento: '',
    bairro: '',
    localidade: '',
    uf: ''
  };

  constructor(private http: HttpClient,private router: Router) {}

  buscarCep() {

    if (!this.cep) {
      return;
    }

    this.http.get<any>(
      `http://localhost:8080/viaCep/${this.cep}`,
      {
        withCredentials: true
      }
    )
    .subscribe({
      next: (response) => {
        this.address = response;
      },
      error: () => {
        this.erro = 'CEP não encontrado';
      }
    });

  }

  salvar() {

    this.http.post(
      'http://localhost:8080/motoboy/saveAdress',
      this.address,
      {
        withCredentials: true
      }
    )
    .subscribe({
      next: () => {
        this.mensagem = 'Endereço salvo';

        this.router.navigate(['/layout']);
      },
      error: () => {
        this.erro = 'Erro ao salvar endereço';
      }
    });

  }


}
