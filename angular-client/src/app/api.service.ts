import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';

const API = 'http://localhost:8080/api';

@Injectable({ providedIn: 'root' })
export class ApiService {
  private http = inject(HttpClient);

  accounts()                     { return this.http.get<any[]>(`${API}/accounts`); }
  createAccount(a: any)          { return this.http.post(`${API}/accounts`, a); }
  setStatus(id: string, s: string) { return this.http.patch(`${API}/accounts/${id}/status?value=${s}`, {}); }
  transactions(accountId?: string) {
    return this.http.get<any[]>(`${API}/transactions${accountId ? '?accountId=' + accountId : ''}`);
  }
  createTransaction(t: any)      { return this.http.post(`${API}/transactions`, t); }
  transfer(t: any)               { return this.http.post(`${API}/transactions/transfer`, t); }
}