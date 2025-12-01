import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Location } from '@angular/common';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { AuthService } from '../../services/auth.service';
import { passwordValidator } from '../../validators/password.validator';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
  standalone: false
})
export class LoginComponent implements OnInit, OnDestroy {
  loginForm!: FormGroup;
  private destroy$ = new Subject<void>();

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private location: Location,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    this.initForm();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private initForm(): void {
    this.loginForm = this.formBuilder.group({
      emailOrUsername: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(8), passwordValidator()]]
    });
  }

  onSubmit(): void {
    if (this.loginForm.valid) {
      const { emailOrUsername, password } = this.loginForm.value;
      
      this.authService.login({ emailOrUsername, password })
        .pipe(takeUntil(this.destroy$))
        .subscribe({
          next: (response) => {
            console.log('Connexion réussie:', response.user);
            this.router.navigate(['/articles']);
          },
          error: (error) => {
            console.error('Erreur de connexion:', error);
            alert('Identifiants invalides');
          }
        });
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