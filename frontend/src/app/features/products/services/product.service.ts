import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject, Observable, tap} from 'rxjs';
import {Product} from '../models/product.model';
import {AddProduct} from '../models/add-product.model';

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  private apiUrl = 'http://localhost:8080/api/products';
  private productsSubject = new BehaviorSubject<Product[]>([]);
  products$ = this.productsSubject.asObservable();

  constructor(private http: HttpClient) {}

  loadProducts() {
    return this.http.get<Product[]>(this.apiUrl).pipe(
      tap(products => this.productsSubject.next(products)),
    ).subscribe()
  }

  getProductById(id: string): Observable<Product> {
    return this.http.get<Product>(`${this.apiUrl}/${id}`);
  }

  addProduct(newProduct: AddProduct): Observable<Product> {
    return this.http.post<Product>(`${this.apiUrl}`, newProduct).pipe(
      tap(() => this.loadProducts())
    )
  }
}
