import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  standalone: true,
  selector: 'app-sidebar',
  imports: [CommonModule, RouterLink, RouterLinkActive],
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent {
  public menu = [
    { label: 'Dashboard', path: '/', icon: 'dashboard' },
    { label: 'Transações', path: '/transactions', icon: 'swap_horiz' },
    { label: 'Categorias', path: '/categories', icon: 'category' },
    { label: 'Relatórios', path: '/reports', icon: 'bar_chart' },
    { label: 'Perfil', path: '/profile', icon: 'person' },
    { label: 'Configurações', path: '/settings', icon: 'settings' }
  ];
}
