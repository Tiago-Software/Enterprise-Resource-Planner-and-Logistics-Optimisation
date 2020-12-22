import { Component, OnInit, ViewChild } from '@angular/core';
import { AddNewProductModalComponent } from 'src/app/containers/pages/add-new-product-modal/add-new-product-modal.component';
import { AuthService } from 'src/app/shared/auth.service';
import { Router } from '@angular/router';
import { FormControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { IProduct } from 'src/app/data/api.service';

@Component({
  selector: 'app-layouts',
  templateUrl: './layouts.component.html'
})
export class LayoutsComponent implements OnInit {
  
  orderForm: FormGroup;

  displayMode = 'image';
  selectAllState = '';
  selected: IProduct[] = [];
  data: IProduct[] = [];
  currentPage = 1;
  itemsPerPage = 8;
  search = '';
  orderBy = '';
  isLoading: boolean;
  endOfTheList = false;
  totalItem = 0;
  totalPage = 0;


  @ViewChild('addNewModalRef', { static: true }) addNewModalRef: AddNewProductModalComponent;

  constructor( private fb: FormBuilder, private authService: AuthService, private router: Router) { 
    
    this.orderForm= this.fb.group({
    //order_Location
    'consultant_Id': [,[Validators.required]],
    'deliver_By_Date': ['',[Validators.required]],
    'date_placed': ['',[Validators.required]],
    'location_Street': ['',[Validators.required]],
    'location_Suburb': ['',[Validators.required]],
    'location_City': ['',[Validators.required]],
    'location_Zip_Code': ['',[Validators.required]], 
    'location_Longitude': ['',[Validators.required]],
    'location_Latitude': ['',[Validators.required]],
     //order_client information
    'client_Date_registered': ['',[Validators.required]],
    'client_First_Name': ['',[Validators.required]],
    'client_Last_Name': ['',[Validators.required]],
    'client_Id_Number': ['',[Validators.required]],
    'client_Business_Name': ['',[Validators.required]],
    'client_Business_Reg_Number': ['',[Validators.required]],
    'client_Contact_Number': ['',[Validators.required]],
    'client_Email': ['',[Validators.required]],
  });
  }

  ngOnInit() {
    this.loadData(this.itemsPerPage, this.currentPage, this.search, this.orderBy);
  }

  loadData(pageSize: number = 10, currentPage: number = 1, search: string = '', orderBy: string = '') {
    this.itemsPerPage = pageSize;
    this.currentPage = currentPage;
    this.search = search;
    this.orderBy = orderBy;

   /* this.apiService.getProducts(pageSize, currentPage, search, orderBy).subscribe(
      data => {
        if (data.status) {
          this.isLoading = false;
          this.data = data.data;
          this.totalItem = data.totalItem;
          this.totalPage = data.totalPage;
          this.setSelectAllState();
        } else {
          this.endOfTheList = true;
        }
      },
      error => {
        this.isLoading = false;
      }
    );
    */
  }

  changeDisplayMode(mode) {
    this.displayMode = mode;
  }

  showAddNewModal() {
    this.addNewModalRef.show();
  }

  isSelected(p: IProduct) {
    return this.selected.findIndex(x => x.id === p.id) > -1;
  }
  onSelect(item: IProduct) {
    if (this.isSelected(item)) {
      this.selected = this.selected.filter(x => x.id !== item.id);
    } else {
      this.selected.push(item);
    }
    this.setSelectAllState();
  }

  setSelectAllState() {
    if (this.selected.length === this.data.length) {
      this.selectAllState = 'checked';
    } else if (this.selected.length !== 0) {
      this.selectAllState = 'indeterminate';
    } else {
      this.selectAllState = '';
    }
  }

  selectAllChange($event) {
    if ($event.target.checked) {
      this.selected = [...this.data];
    } else {
      this.selected = [];
    }
    this.setSelectAllState();
  }

  pageChanged(event: any): void {
    this.loadData(this.itemsPerPage, event.page, this.search, this.orderBy);
  }

  itemsPerPageChange(perPage: number) {
    this.loadData(perPage, 1, this.search, this.orderBy);
  }

  changeOrderBy(item: any) {
    this.loadData(this.itemsPerPage, 1, this.search, item.value);
  }

  searchKeyUp(event){
    const val = event.target.value.toLowerCase().trim();
    this.loadData(this.itemsPerPage, 1, val, this.orderBy);
  }

  onContextMenuClick(action: string, item: IProduct) {
    console.log('onContextMenuClick -> action :  ', action, ', item.title :', item.title);
  }
  addTagFn(addedName) {
    return { name: addedName, tag: true };
  }

  
  createOrder()
  {
    this.authService.logOrder(this.orderForm.value).subscribe(data=> {
      console.log(data)
    })
  }
  //order_Location

  get consultant_Id()
  {
    return this.orderForm.get('consultant_Id')
  }

  get deliver_By_Date()
  {
    return this.orderForm.get('deliver_By_Date')
  }

  get date_placed()
  {
    return this.orderForm.get('date_placed')
  }
  
  get location_Street()
  {
    return this.orderForm.get('location_Street')
  }

  get location_Suburb()
  {
    return this.orderForm.get('location_Suburb')
  }

  get location_City()
  {
    return this.orderForm.get('location_City')
  }

  get location_Zip_Code()
  {
    return this.orderForm.get('location_Zip_Code')
  }

  get location_Latitude()
  {
    return this.orderForm.get('location_Latitude')
  }

  get location_Longitude()
  {
    return this.orderForm.get('location_Longitude')
  }

  //order_client information

  get client_Date_registered()
  {
    return this.orderForm.get('client_Date_registered')
  }

  get client_first_Name()
  {
    return this.orderForm.get('client_first_Name')
  }

  get client_last_Name()
  {
    return this.orderForm.get('client_last_Name')
  }

  get client_Id_Number()
  {
    return this.orderForm.get('client_Id_Number')
  }

  get client_Business_Name()
  {
    return this.orderForm.get('client_Business_Name')
  }

  get client_Business_Reg_Number()
  {
    return this.orderForm.get('client_Business_Reg_Number')
  }
  
  get client_Contact_Number()
  {
    return this.orderForm.get('client_Contact_Number')
  }

  get client_Email()
  {
    return this.orderForm.get('client_Email')
  }

  get comp_street()
  {
    return this.orderForm.get('comp_street')
  }


}
