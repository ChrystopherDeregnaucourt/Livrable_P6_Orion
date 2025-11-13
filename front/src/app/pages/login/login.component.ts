import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Location } from '@angular/common';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private location: Location
  ) { }

  ngOnInit(): void {
    this.initForm();
  }

  private initForm(): void {
    this.loginForm = this.formBuilder.group({
      emailOrUsername: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  onSubmit(): void {
    if (this.loginForm.valid) {
      // Récupération des valeurs du formulaire
      const { emailOrUsername, password } = this.loginForm.value;
      
      // TODO: Implémenter la logique de connexion via un service d'authentification
      console.log('Tentative de connexion:', { emailOrUsername, password });
      
      // Redirection temporaire vers home après connexion
      // À remplacer par la logique d'authentification réelle
      this.router.navigate(['/home']);
    }
  }

  goBack(): void {
    // Retour à la page précédente ou home par défaut
    if (window.history.length > 1) {
      this.location.back();
    } else {
      this.router.navigate(['/home']);
    }
  }

  // Fonction pour afficher la page articles
  navigateToArticles(): void {
    this.router.navigate(['/articles']);
  }
}