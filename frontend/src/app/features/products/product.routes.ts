import {Routes} from '@angular/router';
import {ProductsPageComponent} from './pages/products-page/products-page.component';
import {ProductDetailsComponent} from './pages/product-details/product-details.component';

export const routes: Routes = [
  { path: '', component: ProductsPageComponent },
  { path: 'products/:id', component: ProductDetailsComponent },
];
