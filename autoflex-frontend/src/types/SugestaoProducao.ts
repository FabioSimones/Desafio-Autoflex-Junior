export interface SugestaoProducaoItem {
  produtoId: number
  nomeProduto: string
  valorUnitario: number
  quantidadeProduzivel: number
  valorTotalProduto: number
}

export interface SugestaoProducaoResponse {
  itens: SugestaoProducaoItem[]
  valorTotalProducao: number
}
