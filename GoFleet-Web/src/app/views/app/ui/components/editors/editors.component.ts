import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-editors',
  templateUrl: './editors.component.html'
})
export class EditorsComponent implements OnInit {

  modulesBubble = {
    toolbar: [
      ['bold', 'italic', 'underline', 'strike', 'blockquote'],
      [
        { list: 'ordered' },
        { list: 'bullet' },
        { indent: '-1' },
        { indent: '+1' }
      ],
      ['link'],
      ['clean']
    ]
  };

  constructor() { }

  ngOnInit() {
  }

}
