import api from './axios'
import type { Produto } from '../types/Produto'

export function listarProdutos() {
  return api.get<Produto[]>('/produtos')
}

export function buscarProduto(id: number) {
  return api.get<Produto>(`/produtos/${id}`)
}

export function criarProduto(payload: { nome: string; valor: number }) {
  return api.post<Produto>('/produtos', payload)
}

export function atualizarProduto(id: number, payload: { nome: string; valor: number }) {
  return api.put<Produto>(`/produtos/${id}`, payload)
}

export function excluirProduto(id: number) {
  return api.delete<void>(`/produtos/${id}`)
}
