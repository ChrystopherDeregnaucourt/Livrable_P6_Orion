import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

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

  constructor(private fb: FormBuilder, private router: Router) {}

  ngOnInit(): void {
    this.profileForm = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });

    this.subscriptions = [
      {
        id: 1,
        title: 'Titre du thème',
        description:
          "Description: Lorem ipsum is simply dummy text of the printing and typesetting industry. Lorem ipsum has been the industry's standard dummy text ever since the 1500s"
      },
      {
        id: 2,
        title: 'Titre du thème',
        description:
          "Description: Lorem ipsum is simply dummy text of the printing and typesetting industry. Lorem ipsum has been the industry's standard dummy text ever since the 1500s"
      },
      {
        id: 3,
        title: 'Titre du thème',
        description:
          "Description: Lorem ipsum is simply dummy text of the printing and typesetting industry. Lorem ipsum has been the industry's standard dummy text ever since the 1500s"
      },
      {
        id: 4,
        title: 'Titre du thème',
        description:
          "Description: Lorem ipsum is simply dummy text of the printing and typesetting industry. Lorem ipsum has been the industry's standard dummy text ever since the 1500s"
      }
    ];
  }

  toggleMobileMenu(): void {
    this.isMobileMenuOpen = !this.isMobileMenuOpen;
  }

  onDisconnect(): void {
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
      console.log('Profil sauvegardé', this.profileForm.value);
    }
  }

  onUnsubscribe(subscriptionId: number): void {
    this.subscriptions = this.subscriptions.filter((subscription) => subscription.id !== subscriptionId);
  }

  trackBySubscriptionId(index: number, subscription: SubscriptionItem): number {
    return subscription.id;
  }
}
