import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environment/environment';

interface PerfilResponse {
  nome: string;
  email: string;
  motocicleta: string | null;
  placa: string | null;
  cidade: string | null;
  foto: string | null;
}

@Component({
  standalone: true,
  selector: 'app-profile',
  imports: [CommonModule],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  perfil: PerfilResponse = {
    nome: '',
    email: '',
    motocicleta: null,
    placa: null,
    cidade: null,
    foto: null
  };


  constructor(
    private http: HttpClient
  ) {}


  ngOnInit(): void {

    this.http.get<PerfilResponse>(
      `${environment.apiUrl}/motoboyFindAttributes`,
      {
        withCredentials: true
      }
    )
    .subscribe({
      next: (response) => {
        this.perfil = response;
      },
      error: () => {
        // tratar erro de carregamento do perfil
      }
    });

  }


  get dadosPerfil() {
    return [
      {
        label: 'Nome',
        value: this.perfil.nome
      },
      {
        label: 'Email',
        value: this.perfil.email
      },
      {
        label: 'Motocicleta',
        value: this.perfil.motocicleta
      },
      {
        label: 'Placa',
        value: this.perfil.placa
      },
      {
        label: 'Cidade',
        value: this.perfil.cidade
      }
    ];
  }

}
