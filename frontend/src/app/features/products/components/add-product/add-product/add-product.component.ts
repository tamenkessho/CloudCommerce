import { Component } from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {AddProduct} from '../../../models/add-product.model';
import {ProductService} from '../../../services/product.service';

@Component({
  selector: 'app-add-product',
  templateUrl: './add-product.component.html',
  styleUrl: './add-product.component.scss',
  imports: [
    FormsModule,
    ReactiveFormsModule
  ],
})
export class AddProductComponent {
  addProductForm!: FormGroup;

  constructor(private fb: FormBuilder, private productService: ProductService) {
  }

  ngOnInit(){
    this.addProductForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      description: ['', [Validators.required]],
      categoryId: ['', [Validators.required]],
      price: ['', [Validators.required, Validators.min(0.01)]]
    });
  }

  onSubmit(): void {
    if (this.addProductForm.valid) {
      const product: AddProduct = this.addProductForm.value;
      this.productService.addProduct(product).subscribe({
        next: () => {
          this.addProductForm.reset();
        },
        error: (err) => console.error('Error adding product:', err),
      });
    }
  }

}
