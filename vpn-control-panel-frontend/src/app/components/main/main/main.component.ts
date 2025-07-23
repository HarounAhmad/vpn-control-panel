import { Component } from '@angular/core';
import {RouterOutlet} from "@angular/router";
import {Toolbar} from "primeng/toolbar";
import {Button} from "primeng/button";
import {IconField} from "primeng/iconfield";
import {SplitButton} from "primeng/splitbutton";
import {InputIcon} from "primeng/inputicon";
import {InputText} from "primeng/inputtext";

@Component({
  selector: 'app-main',
  imports: [
    RouterOutlet,
    Toolbar,
    Button,
    IconField,
    SplitButton,
    InputIcon,
    InputText
  ],
  templateUrl: './main.component.html',
  standalone: true,
  styleUrl: './main.component.scss'
})
export class MainComponent {

}
