import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {CarListComponent} from './car-list/car-list.component';
import {HttpClientModule} from '@angular/common/http';
import {CartComponent} from './cart/cart.component';
import {CartService} from './shared/api/cart.service';

@NgModule({
  declarations: [
    AppComponent,
    CarListComponent,
    CartComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    BrowserAnimationsModule
  ],
  providers: [CartService],
  bootstrap: [AppComponent]
})
export class AppModule { }
