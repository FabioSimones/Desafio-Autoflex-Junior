import api from './axios'
import type { MateriaPrima } from '../types/MateriaPrima'

export function listarMateriasPrimas() {
  return api.get<MateriaPrima[]>('/materias-primas')
}

export function buscarMateriaPrima(id: number) {
  return api.get<MateriaPrima>(`/materias-primas/${id}`)
}

export function criarMateriaPrima(payload: { nome: string; quantidadeEstoque: number }) {
  return api.post<MateriaPrima>('/materias-primas', payload)
}

export function atualizarMateriaPrima(id: number, payload: { nome: string; quantidadeEstoque: number }) {
  return api.put<MateriaPrima>(`/materias-primas/${id}`, payload)
}

export function excluirMateriaPrima(id: number) {
  return api.delete<void>(`/materias-primas/${id}`)
}
