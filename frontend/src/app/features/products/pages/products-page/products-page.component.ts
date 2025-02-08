import { Component } from '@angular/core';
import {ProductListComponent} from '../../components/product-list/product-list.component';

@Component({
  selector: 'app-products-page',
  templateUrl: './products-page.component.html',
  styleUrls: ['./products-page.component.scss'],
  imports: [
    ProductListComponent
  ]
})
export class ProductsPageComponent {}
