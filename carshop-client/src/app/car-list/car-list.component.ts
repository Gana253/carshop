import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {CarsService} from '../shared/api/cars.service';

@Component({
  selector: 'app-car-list',
  templateUrl: './car-list.component.html',
  styleUrls: ['./car-list.component.css']
})
export class CarListComponent implements OnInit {
  cars: Array<any>;
  headers = ['MAKE', 'MODEL', 'YEAR MODEL', 'PRICE', 'LICENSED', 'DATE ADDED', 'MORE DETAILS'];
  vehicles: Array<any> = [];
  @Input() active = false;
  @Output() toggleAccordion: EventEmitter<boolean> = new EventEmitter();

  constructor(private carsService: CarsService) {
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
}
