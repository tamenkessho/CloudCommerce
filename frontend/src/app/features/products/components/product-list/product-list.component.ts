import { Component, OnInit } from '@angular/core';
import {Product} from '../../models/product.model';
import {ProductService} from '../../services/product.service';
import {ProductCardComponent} from '../product-card/product-card.component';
import {Observable} from 'rxjs';
import {AsyncPipe} from '@angular/common';
import {ReactiveFormsModule} from '@angular/forms';


@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.scss'],
  imports: [
    ProductCardComponent,
    AsyncPipe,
    ReactiveFormsModule
  ]
})
export class ProductListComponent implements OnInit {
  products$!: Observable<Product[]>;

  constructor(private productService: ProductService) {}

  ngOnInit(): void {
    this.productService.loadProducts()
    this.products$ = this.productService.products$
  }

}
