import {Component, EventEmitter, Input, OnInit, Output, Renderer2} from '@angular/core';
import {CarsService} from '../shared/api/cars.service';
import {CartService} from '../shared/api/cart.service';

@Component({
  selector: 'app-car-list',
  templateUrl: './car-list.component.html',
  styleUrls: ['./car-list.component.css']
})
export class CarListComponent implements OnInit {
  cars: Array<any>;
  headers = ['MAKE', 'MODEL', 'YEAR MODEL', 'PRICE', 'LICENSED', 'DATE ADDED', 'MORE DETAILS'];
  vehicles: Array<any> = [];
  cartProductCount = 0;
 /* @Input() products: any = [];*/
  singleProduct;
  isAdded;

  @Input() active = false;
  @Output() toggleAccordion: EventEmitter<boolean> = new EventEmitter();

  constructor(private carsService: CarsService, private cartService: CartService) {
  }

  ngOnInit() {
    this.carsService.getAll().subscribe(data => {
      this.cars = data;
      this.getVehicles(this.cars, this.vehicles);
      this.vehicles.sort((val1, val2) => {
        // @ts-ignore
        return new Date(val1.dateAdded) - new Date(val2.dateAdded);
      });
      console.log(this.vehicles);
    });
    this.isAdded = new Array(this.vehicles.length);
    this.isAdded.fill(false, 0, this.vehicles.length);
    console.log('this.isAdded -> ', this.isAdded, this.vehicles);
    this.cartService.getProducts().subscribe(data => {
      this.cartProductCount = data.length;
    });

    this.cartService.getProducts().subscribe(data => {

      if (data && data.length > 0) {

      } else {
        this.vehicles.map((item, index) => {
          this.isAdded[index] = false;
        });
      }

    });
  }

  getVehicles(cars, vehicles) {
    for (const car of cars) {
      for (const vehicle of car.vehicles) {
        vehicles.push({...vehicle, car});
      }
    }
  }

  toggleAccordian(event) {
    const element = event.target;
    element.classList.toggle('active');
    const panel = element.nextElementSibling;
    if (panel.style.maxHeight) {
      panel.style.maxHeight = null;
    } else {
      panel.style.maxHeight = panel.scrollHeight + 'px';
    }
  }
  // Add item in cart on Button click
  // ===============================

  addToCart(event, productId) {

    // If Item is already added then display alert message
    if (event.target.classList.contains('btn-success')) {
      alert('This product is already added into cart.');
      return false;
    }

    // Change button color to green
    this.vehicles.map((item, index) => {
      if (item.id === productId) {
        this.isAdded[index] = true;
      }
    });

    this.singleProduct = this.vehicles.filter(product => {
      return product.id === productId;
    });

    this.cartService.addProductToCart(this.singleProduct[0]);
  }
}
