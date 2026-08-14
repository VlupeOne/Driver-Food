import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-summary-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './summary-card.component.html',
  styleUrls: ['./summary-card.component.css']
})
export class SummaryCardComponent {
  @Input() title = '';
  @Input() value: string | number | null = null;
  @Input() icon = '';
  @Input() accent = '#2563eb';
}
