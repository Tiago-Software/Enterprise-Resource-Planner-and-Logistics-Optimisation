import { Component, OnInit, ViewChild, AfterViewInit  } from '@angular/core';
import { WizardComponent , NavigationMode}  from "angular-archwizard";

@Component({
  selector: 'app-wizard',
  templateUrl: './wizard.component.html'
})
export class WizardComponents implements OnInit {


  @ViewChild('wizard')
  public wizard : WizardComponent;

  constructor() {

   }


  ngOnInit() {
  }

  
  ngAfterViewInit(): void {
    
    console.log(this.wizard)
    console.log(this.wizard.currentStepIndex)
    console.log(this.wizard.currentStep)
    this.wizard.goToStep(2);
    console.log(this.wizard.currentStep)
    
  }
  


}
