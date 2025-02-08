import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {Product} from '../../models/product.model';
import {ProductService} from '../../services/product.service';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.scss'],
  imports: [CommonModule]
})
export class ProductDetailsComponent implements OnInit {
  product!: Product;

  constructor(private route: ActivatedRoute, private productService: ProductService) {}

  ngOnInit(): void {
    const id = String(this.route.snapshot.paramMap.get('id'));
    this.productService.getProductById(id).subscribe((product) => {
      this.product = product;
    });
  }
}
