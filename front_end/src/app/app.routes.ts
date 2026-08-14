import { Routes } from '@angular/router';
import { RegisterComponent } from './component/register/register.component';
import { LoginComponent } from './component/login/login.component';
import { MotoboyAddressComponent } from './component/MotoboyAddressComponent/motoboyAddress.component';
import { AuthGuard } from './auth/AuthGuard';
import { LayoutComponent } from './layout/layout.component';
import { DailyControlComponent } from './features/transactions/dailyControl.component';

export const routes: Routes = [
  {
    path: '',
    component: LayoutComponent,
    canActivate: [AuthGuard],
    children: [
      { path: 'transactions', component: DailyControlComponent }
    ]
  },

  {
    path: 'login',
    component: LoginComponent
  },

  {
    path: 'register',
    component: RegisterComponent
  },

  {
    path: 'motoboy/address',
    component: MotoboyAddressComponent,
    canActivate: [AuthGuard]
  }
];
