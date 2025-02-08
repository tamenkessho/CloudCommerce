import {Component, Input} from '@angular/core';
import {Product} from '../../models/product.model';
import {Router} from '@angular/router';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-product-card',
  templateUrl: './product-card.component.html',
  styleUrls: ['./product-card.component.scss'],
  imports: [CommonModule]
})
export class ProductCardComponent {
  @Input() product!: Product;

  constructor(private router: Router) {}

  viewDetails() {
    this.router.navigate(['/products', this.product.id])
  }
}
