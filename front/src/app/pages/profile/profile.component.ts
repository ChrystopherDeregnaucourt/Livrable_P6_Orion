import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { AuthService } from '../../services/auth.service';
import { ApiService } from '../../services/api.service';

interface SubscriptionItem {
  id: number;
  title: string;
  description: string;
}

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss'],
  standalone: false
})
export class ProfileComponent implements OnInit, OnDestroy {
  profileForm!: FormGroup;
  isMobileMenuOpen = false;
  subscriptions: SubscriptionItem[] = [];
  private destroy$ = new Subject<void>();

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private authService: AuthService,
    private apiService: ApiService
  ) {}

  ngOnInit(): void {
    this.profileForm = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['']  // Optionnel, sera validé seulement si rempli
    });

    // Charger les données utilisateur depuis l'API
    this.authService.getMe()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
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

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
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
      const { username, email, password } = this.profileForm.value;
      
      // Construire l'objet de mise à jour
      const updateData: any = { username, email };
      
      // N'envoyer le mot de passe que s'il a été modifié
      if (password && password !== '******') {
        updateData.password = password;
      }
      
      this.authService.updateMe(updateData)
        .pipe(takeUntil(this.destroy$))
        .subscribe({
          next: (user) => {
            console.log('Profil mis à jour:', user);
            alert('Profil sauvegardé avec succès !');
            // Réinitialiser le champ password
            this.profileForm.patchValue({ password: '******' });
          },
          error: (error) => {
            console.error('Erreur lors de la sauvegarde:', error);
            alert('Erreur lors de la sauvegarde du profil');
          }
        });
    }
  }

  onUnsubscribe(subscriptionId: number): void {
    this.apiService.unsubscribeFromTopic(subscriptionId)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: () => {
          // Retirer l'abonnement de la liste locale
          this.subscriptions = this.subscriptions.filter((subscription) => subscription.id !== subscriptionId);
          console.log('Désabonnement réussi');
        },
        error: (error) => {
          console.error('Erreur lors du désabonnement:', error);
          alert('Erreur lors du désabonnement');
        }
      });
  }

  trackBySubscriptionId(index: number, subscription: SubscriptionItem): number {
    return subscription.id;
  }
}
