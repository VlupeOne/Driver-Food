import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  selector: 'app-footer',
  imports: [CommonModule],
  template: `<footer class="app-footer">© 2026 Gestão Financeira • Preparado para API REST</footer>`,
  styles: [
    `.app-footer { padding: 1rem 1.5rem; text-align: center; color: #6b7280; font-size: 0.9rem; }`
  ]
})
export class FooterComponent {}
