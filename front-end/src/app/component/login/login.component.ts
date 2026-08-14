import { Component } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environment/environment';
import { SocialLoginComponent } from '../social-login/social-login.component';

@Component({
  selector: 'app-login',
  standalone:true,
  imports:[
    CommonModule,
    ReactiveFormsModule,
    SocialLoginComponent
  ],
  templateUrl:'./login.component.html',
  styleUrls:['./login.component.css']
})
export class LoginComponent {
  formulario:FormGroup;
  carregando=false;

  constructor(
    private fb:FormBuilder,
    private http:HttpClient
  ){

    this.formulario=this.fb.group({
      email:[
        '',
        [
          Validators.required,
          Validators.email
        ]
      ],
      senha:[
        '',
        [
          Validators.required
        ]
      ]
    });
  }

  entrar(){
    if(this.formulario.invalid){

      this.formulario.markAllAsTouched();
      return;
    }

    this.carregando=true;

    const body={
      email:this.formulario.value.email,
      password:this.formulario.value.senha
    };

    this.http.post(
      `${environment.apiUrl}/auth/login`,
      body,
      {
        withCredentials:true
      }
    )
    .subscribe({
      next:()=>{
        alert("Login realizado!");
        this.carregando=false;
      },
      error:()=>{
        alert("Email ou senha inválidos");
        this.carregando=false;
      }
    });
  }
}
