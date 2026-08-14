import { Routes } from '@angular/router';
import { RegisterComponent } from './component/register/register.component';
import { LoginComponent } from './component/login/login.component';
import { LayoutComponent } from './layout/layout.component';
import { ProfileComponent } from './features/profile/profile.component';

export const routes: Routes = [

  {
    path: '',
    component: LayoutComponent,
    children: [
      {
        path: 'profile',
        component: ProfileComponent
      }
    ]
  },

  {
    path:'login',
    component:LoginComponent
  },

  {
    path:'register',
    component:RegisterComponent
  },

  {
    path:'**',
    redirectTo:''
  }

];
