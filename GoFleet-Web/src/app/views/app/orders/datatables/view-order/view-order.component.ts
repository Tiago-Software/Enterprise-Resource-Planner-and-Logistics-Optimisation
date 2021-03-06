import { AuthService } from '../../../../../shared/auth.service';
import { Component, ViewEncapsulation, ViewChild } from '@angular/core';
import { ColumnMode, DatatableComponent, SelectionType } from '@swimlane/ngx-datatable';
import { Router } from '@angular/router';
import { stringify } from 'querystring';

// Generated by https://quicktype.io

export interface IOrders {
  order_Id:              number;
  client:                IClient;
  location:              ILocation;
  order_Total_Volume:    number;
  order_Deliver_By_Date: string;
  order_Date_Placed:     string;
  order_Sequence_Number: number;
  order_Delivered:       boolean;
  order_Postponed:       boolean;
  order_Delayed:         boolean;
  items:                 ItemElement[];
}


export interface IClient 
{
  client_First_Name:          string;
  client_Last_Name:           string;
  client_Id_Number:           string;
  client_Business_Name:       string;
  client_Business_Reg_Number: string;
  client_Contact_Number:      string;
  client_Email:               string;
}

export interface ItemElement {
  item:             ItemItem;
  quantity_Ordered: number;
  combined_Volume:  number;
}

export interface ItemItem {
  stockItem_Name: StockItemName;
}

export enum StockItemName {
  Bricks = "Bricks",
  Cement = "Cement",
  Sand = "Sand",
}

export interface ILocation {
  location_Street:    string;
  location_Suburb:    string;
  location_City:      string;
  location_Zip_Code:  string;
  location_Longitude: number;
  location_Latitude:  number;
  location_Places_Id: string;
}


//route data

// Generated by https://quicktype.io

export interface IRoutes {
  route_Id:        number;
  route_ETA:       string;
  route_Driver_Id: number;
  route_Completed: string;
  driver:         IDriver;
  orders:          Order[];
}

// Generated by https://quicktype.io

export interface IDriver {
  emp_Id:         number;
  emp_First_Name: string;
  emp_Last_Name:  string;
}


export interface Order {
  order_Id:              number;
  order_Sequence_Number: number;
  order_Delivered:       boolean;
  location:              ILocation;
}



@Component({
  selector: 'app-view-order',
  templateUrl: './view-order.component.html'
})

export class ViewOrderComponent{
  @ViewChild('myTable') table: any;

// Generated by https://quicktype.io
  delayedorders : IOrders[] = [];
  upcomingorders : IOrders[] = [];
  activeorders : IOrders[] = [];
  completeorders : IOrders[] = [];

  allorders : IOrders[] = [];
  allorderindex;

  data: string;

  ordercount: number = 0;
  delayedcount: number = 0;
  upcomingcount: number = 0;
  completecount: number = 0;

  expanded: any = {};
  timeout: any;  

  delayedrows;
  upcomingrows;
  activerows;
  completerows;

  //route object  
  public manOrders : IRoutes[] =[];
  public manOrderCount : number = 0 ;

  delayedtemp;
  upcomingtemp;
  activetemp;
  completetemp;

  itemsPerPage = 10;
  ColumnMode = ColumnMode;
  columns = [
    { prop: 'order_Id', name: 'orderID' }
   
  ];
  
  constructor(private authService: AuthService, private router: Router) {

    // Generated by https://quicktype.io




    this.authService.viewOrders().subscribe(data=> {
      console.log(data)
      console.log(data[this.ordercount].order_Delivered)
      this.data = data

      for(let key in data)
      {
        if(data.hasOwnProperty(key))
        {
          this.allorders.push(data[key]);
          this.allorderindex++;
          console.log(data[key])
        }
      }
      console.log(this.allorders)
      console.log(this.allorders.length)

      for(let iallorder = 0; iallorder < this.allorders.length;iallorder++)
      {
        console.log(iallorder)

          if(this.allorders[iallorder].order_Delivered)
          {
             //order completed
                 this.completeorders.push(this.allorders[iallorder]);
                 this.completecount++;
                 this.ordercount++;
             this.completerows = this.completeorders.slice(0,this.completeorders.length).map(({order_Id, client, location,order_Total_Volume}) => ({order_Id,client, location,order_Total_Volume}));
             this.completetemp = [...this.completerows];
             console.log(this.completerows)
             console.log(this.completerows.order_Id)
             console.log(this.completetemp)
             console.log(this.completeorders)
             console.log('complete order count: ',this.completecount)
           
          }else if((this.allorders[iallorder].order_Sequence_Number != 0) && !this.allorders[iallorder].order_Postponed)
          {
            //active

            this.activeorders.push(this.allorders[iallorder]);
            console.log(this.activeorders)
       
            this.activerows = this.activeorders.slice(0,this.activeorders.length).map(({order_Id, order_Date_Placed, order_Deliver_By_Date, client, location,order_Total_Volume}) => ({order_Id, order_Date_Placed, order_Deliver_By_Date, client, location,order_Total_Volume}));
            this.activetemp = [...this.activeorders];

          }else if(this.allorders[iallorder].order_Postponed){
            //delayed
        
                this.delayedorders.push(this.allorders[iallorder]);
                this.delayedcount++;
                this.ordercount++;

            this.delayedrows = this.delayedorders.slice(0,this.delayedorders.length).map(({order_Id, client, location,order_Total_Volume}) => ({order_Id, client, location,order_Total_Volume}));
            this.delayedtemp = [...this.delayedrows];

          }else {
      //upcoming is null
        
          this.upcomingorders.push(this.allorders[iallorder]);
          this.upcomingcount++;
          this.ordercount++;

          this.upcomingrows = this.upcomingorders.slice(0,this.upcomingorders.length).map(({order_Id, client, location,order_Total_Volume}) => ({order_Id, client, location,order_Total_Volume}));
          this.upcomingtemp = [...this.upcomingrows];
           }
        }

        
      console.log(this.manOrders)



      console.log(this.activeorders)
      console.log(this.activeorders[0])
      console.log(this.allorders)
      console.log(this.allorders.length)

   
      
     
        console.log(data[this.ordercount],this.activeorders[this.ordercount])
    

        
     
      console.log('total order count ',this.ordercount)
    })
    
  } 
  

  orderActive(order: IOrders, AllActiveRoutes: IRoutes[]): Boolean{
    for(let ir = 0; ir < AllActiveRoutes.length;ir++)
    { 
      if(AllActiveRoutes[ir].orders.findIndex(d => d.order_Id === order.order_Id) >= 0)
      {
        return true;
      }
    }
    
    return false;
  }

  ngOnInit(): void 
  {
  
  }

  downloadInvoice(orderId: number)
  {
    this.authService.getOrderInvoice(orderId).subscribe((resultBlob: Blob) => {
      var downloadURL = URL.createObjectURL(resultBlob);
      window.open(downloadURL);
    });
  }

  cancelOrder(orderId: number)
  {
    this.authService.postCancelOrder({
       order_Id: orderId,
       cancel_Reason: "The Order is not able to meet the standards of todays route"
      }).subscribe();

  }

  onPage(event) {
  }

  toggleExpandRow(row) {
    this.table.rowDetail.toggleExpandRow(row);
  }

  onDetailToggle(event) {
  }

  updateFilter(event) {
    const val = event.target.value.toLowerCase().trim();
    const count = this.completeorders.length;
    const keys = Object.keys(this.delayedtemp[0]);
    const temp = this.completetemp.filter(item => {
      for (let i = 0; i < count; i++) {
        if ((item[keys[i]] && item[keys[i]].toString().toLowerCase().indexOf(val) !== -1) || !val) {
          return true;
        }
      }
    });
    this.delayedrows = temp;
    this.table.offset = 0;
  }

  
goToOrder(orderId: number)
{
  console.log('order id: ',orderId)
  console.log('route: ',this.router.navigate(['/app/orders/forms/orderDetails', orderId]))
  this.router.navigate(['/app/orders/forms/orderDetails',orderId]);
}

}


export interface IOrder extends Array<IOrders>{}