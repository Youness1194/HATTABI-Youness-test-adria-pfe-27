import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from './api.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
  <h1>Bank Platform</h1>
  <nav>
    <button *ngFor="let t of tabs" (click)="tab = t" [disabled]="tab === t">{{ t }}</button>
  </nav>
  <p *ngIf="msg" [style.color]="err ? 'crimson' : 'green'">{{ msg }}</p>

  <!-- ACCOUNTS -->
  <section *ngIf="tab === 'Accounts'">
    <h3>New account</h3>
    <input placeholder="Customer ref" [(ngModel)]="acc.customerReference">
    <input placeholder="Currency" [(ngModel)]="acc.currency" size="4">
    <input type="number" placeholder="Initial balance" [(ngModel)]="acc.initialBalance">
    <button (click)="createAccount()">Create</button>
    <table border="1" cellpadding="4">
      <tr><th>Id</th><th>Customer</th><th>Status</th><th>Currency</th><th>Balance</th><th></th></tr>
      <tr *ngFor="let a of accounts">
        <td>{{ a.id }}</td><td>{{ a.customerReference }}</td><td>{{ a.status }}</td>
        <td>{{ a.currency }}</td><td>{{ a.balance | number:'1.2-2' }}</td>
        <td><button (click)="toggle(a)">{{ a.status === 'ACTIVE' ? 'Block' : 'Activate' }}</button></td>
      </tr>
    </table>
  </section>

  <!-- TRANSACTIONS -->
  <section *ngIf="tab === 'Transactions'">
    <h3>New transaction</h3>
    <select [(ngModel)]="tx.accountId">
      <option *ngFor="let a of accounts" [value]="a.id">{{ a.customerReference }} ({{ a.currency }} {{ a.balance }})</option>
    </select>
    <input type="number" placeholder="Amount" [(ngModel)]="tx.amount">
    <input placeholder="Currency" [(ngModel)]="tx.currency" size="4">
    <input placeholder="Merchant category" [(ngModel)]="tx.merchantCategory">
    <input placeholder="Country" [(ngModel)]="tx.country" size="4">
    <button (click)="createTx()">Pay</button>

    <h3>Transfer</h3>
    <select [(ngModel)]="tr.fromAccountId"><option *ngFor="let a of accounts" [value]="a.id">{{ a.customerReference }}</option></select>
    →
    <select [(ngModel)]="tr.toAccountId"><option *ngFor="let a of accounts" [value]="a.id">{{ a.customerReference }}</option></select>
    <input type="number" placeholder="Amount" [(ngModel)]="tr.amount">
    <button (click)="transfer()">Transfer</button>

    <h3>History</h3>
    <table border="1" cellpadding="4">
      <tr><th>Date</th><th>Account</th><th>Type</th><th>Amount</th><th>Cur.</th><th>Category</th><th>Country</th></tr>
      <tr *ngFor="let t of transactions">
        <td>{{ t.createdAt | date:'short' }}</td><td>{{ t.accountId }}</td><td>{{ t.type }}</td>
        <td>{{ t.amount }}</td><td>{{ t.currency }}</td><td>{{ t.merchantCategory }}</td><td>{{ t.country }}</td>
      </tr>
    </table>
  </section>

  `,
})
export class AppComponent implements OnInit {
  private api = inject(ApiService);

  tabs = ['Accounts', 'Transactions'];
  tab = 'Accounts';
  msg = ''; err = false;

  accounts: any[] = []; transactions: any[] = [];
  acc: any = { customerReference: '', currency: 'EUR', initialBalance: 0 };
  tx: any = { accountId: '', amount: 0, currency: 'EUR', merchantCategory: 'GROCERY', country: 'MA' };
  tr: any = { fromAccountId: '', toAccountId: '', amount: 0 };

  chat: { who: string; text: string }[] = [];
  question = ''; thinking = false;

  ngOnInit() { this.reload(); }

  reload() {
    this.api.accounts().subscribe(a => this.accounts = a);
    this.api.transactions().subscribe(t => this.transactions = t);
  }

  private done = (ok: string) => ({
    next: () => { this.show(ok, false); this.reload(); },
    error: (e: any) => this.show(e.error?.error ?? 'Request failed', true),
  });

  private show(m: string, err: boolean) { this.msg = m; this.err = err; }

  createAccount() { this.api.createAccount(this.acc).subscribe(this.done('Account created')); }
  toggle(a: any)  { this.api.setStatus(a.id, a.status === 'ACTIVE' ? 'BLOCKED' : 'ACTIVE').subscribe(this.done('Status updated')); }
  createTx()      { this.api.createTransaction(this.tx).subscribe(this.done('Transaction recorded')); }
  transfer()      { this.api.transfer(this.tr).subscribe(this.done('Transfer done')); }
}