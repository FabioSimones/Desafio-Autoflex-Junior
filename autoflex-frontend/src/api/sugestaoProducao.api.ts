import api from './axios'
import type { SugestaoProducaoResponse } from '../types/SugestaoProducao'

export function buscarSugestaoProducao() {
  return api.get<SugestaoProducaoResponse>('/sugestao-producao')
}
