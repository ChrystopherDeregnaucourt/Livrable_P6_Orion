import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

interface SubscriptionItem {
  id: number;
  title: string;
  description: string;
}

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  profileForm!: FormGroup;
  isMobileMenuOpen = false;
  subscriptions: SubscriptionItem[] = [];

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.profileForm = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });

    // Charger les données utilisateur depuis l'API
    this.authService.getMe().subscribe({
      next: (user) => {
        this.profileForm.patchValue({
          username: user.username,
          email: user.email,
          password: '******' // Masquer le mot de passe
        });
        
        // Charger les abonnements depuis les données utilisateur
        if (user.subscriptions) {
          this.subscriptions = user.subscriptions;
        }
      },
      error: (error) => {
        console.error('Erreur lors du chargement du profil:', error);
      }
    });
  }

  toggleMobileMenu(): void {
    this.isMobileMenuOpen = !this.isMobileMenuOpen;
  }

  onDisconnect(): void {
    this.authService.logout();
    this.router.navigate(['/']);
  }

  onNavigateToArticles(): void {
    this.isMobileMenuOpen = false;
    this.router.navigate(['/articles']);
  }

  onNavigateToThemes(): void {
    this.isMobileMenuOpen = false;
    this.router.navigate(['/themes']);
  }

  onNavigateToProfile(): void {
    this.isMobileMenuOpen = false;
  }

  onSave(): void {
    if (this.profileForm.valid) {
      const { username, email } = this.profileForm.value;
      
      this.authService.updateMe({ username, email }).subscribe({
        next: (user) => {
          console.log('Profil mis à jour:', user);
          alert('Profil sauvegardé avec succès !');
        },
        error: (error) => {
          console.error('Erreur lors de la sauvegarde:', error);
          alert('Erreur lors de la sauvegarde du profil');
        }
      });
    }
  }

  onUnsubscribe(subscriptionId: number): void {
    this.subscriptions = this.subscriptions.filter((subscription) => subscription.id !== subscriptionId);
  }

  trackBySubscriptionId(index: number, subscription: SubscriptionItem): number {
    return subscription.id;
  }
}
