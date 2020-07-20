import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';

import { Subject } from 'rxjs';


@Injectable()
export class CartService {
  public cartItems = [];
  public vehicles = new Subject();


  getProducts(): Observable<any> {
    console.log('this.cartItems :', this.cartItems);
    return this.vehicles.asObservable();
  }

  setProducts(products) {
    this.cartItems.push(...products);
    this.vehicles.next(products);
  }

  // Add single product to the cart
  addProductToCart(product) {
    this.cartItems.push(product);
    this.vehicles.next(this.cartItems);
  }

  // Remove single product from the cart
  removeProductFromCart(productId) {
    this.cartItems.map((item, index) => {
      if (item.id === productId) {
        this.cartItems.splice(index, 1);
      }
    });

    // Update Observable value
    this.vehicles.next(this.cartItems);
  }

  // Remove all the items added to the cart
  emptryCart() {
    this.cartItems.length = 0;
    this.vehicles.next(this.cartItems);
  }

  // Calculate total price on item added to the cart
  getTotalPrice() {
    let total = 0;

    this.cartItems.map(item => {
      total += item.price;
    });

    return total;
  }

}
