import {Component, OnInit} from '@angular/core';
import {CartService} from '../shared/api/cart.service';

@Component({
  templateUrl: './cart.component.html',
  selector: 'app-cart-component',
  styleUrls: ['./cart.component.css']

})
export class CartComponent implements OnInit {
   cartItems;
   totalAmmount;

  constructor(
    private cartService: CartService
  ) { }

  ngOnInit() {

    this.cartService.getProducts().subscribe(data => {
      this.cartItems = data;
      this.totalAmmount = this.cartService.getTotalPrice();
    });

  }

  // Remove item from cart list

  removeItemFromCart(productId) {
    /* this.cartItems.map((item, index) => {
      if (item.id === productId) {
        this.cartItems.splice(index, 1);
      }
    });

    this.mySharedService.setProducts(this.cartItems); */

    this.cartService.removeProductFromCart(productId);

  }

  emptyCart() {
    this.cartService.emptryCart();
  }

}
