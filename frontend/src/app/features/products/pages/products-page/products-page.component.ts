import { Component } from '@angular/core';
import {ProductListComponent} from '../../components/product-list/product-list.component';
import {AddProductComponent} from '../../components/add-product/add-product/add-product.component';

@Component({
  selector: 'app-products-page',
  templateUrl: './products-page.component.html',
  styleUrls: ['./products-page.component.scss'],
  imports: [
    ProductListComponent,
    AddProductComponent
  ]
})
export class ProductsPageComponent {}
