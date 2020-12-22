import { Component, OnInit } from '@angular/core';
import { blogData } from '../../../../../data/blog';

@Component({
  selector: 'app-blog-list',
  templateUrl: './blog-list.component.html'
})
export class BlogListComponent implements OnInit {
  data = blogData.slice();

  constructor() { }

  ngOnInit() {
  }

}
