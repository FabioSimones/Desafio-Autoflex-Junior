import api from './axios'

export interface MateriaPrimaOption {
  id: number
  nome: string
}

export interface ProdutoDetalhadoResponse {
  id: number
  nome: string
  valor: number
  materiasPrimas: Array<{
    materiaPrimaId: number
    nome: string
    quantidadeNecessaria: number
  }>
}

export function listarMateriasPrimasParaSelect() {
  return api.get<MateriaPrimaOption[]>('/materias-primas')
}

export function listarEstruturaProduto(produtoId: number) {
  return api.get<ProdutoDetalhadoResponse>(`/produtos/${produtoId}/materias-primas`)
}

export function atualizarEstruturaProduto(
  produtoId: number,
  payload: {
    itens: Array<{
      materiaPrimaId: number
      quantidadeNecessaria: number
    }>
  }
) {
  return api.put<ProdutoDetalhadoResponse>(
    `/produtos/${produtoId}/materias-primas`,
    payload
  )
}
