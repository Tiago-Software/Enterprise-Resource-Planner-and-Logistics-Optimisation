import { Component, OnInit, Input } from '@angular/core';
import { ChartService } from '../../../components/charts/chart.service';
import {
  conversionChartData
} from '../../../data/charts';
@Component({
  selector: 'app-conversion-rates-chart-card',
  templateUrl: './conversion-rates-chart-card.component.html'
})
export class ConversionRatesChartCardComponent implements OnInit {
  @Input() class = '';
  @Input() control = true;

  chartDataConfig: ChartService;

  conversionChartData = conversionChartData;

  constructor(private chartService: ChartService) {
    this.chartDataConfig = this.chartService;
  }

  ngOnInit() {
  }

}
