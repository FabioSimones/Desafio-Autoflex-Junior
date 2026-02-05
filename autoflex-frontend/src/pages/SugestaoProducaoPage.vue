<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { buscarSugestaoProducao } from '../api/sugestaoProducao.api'
import type { SugestaoProducaoResponse } from '../types/SugestaoProducao'

const loading = ref(false)
const erro = ref<string | null>(null)
const sugestao = ref<SugestaoProducaoResponse | null>(null)

function toNumber(v: unknown): number {
  if (typeof v === 'number') return v
  if (typeof v === 'string') {
    // aceita "199.90" e também "199,90"
    const n = Number(v.replace(',', '.'))
    return Number.isFinite(n) ? n : 0
  }
  return 0
}

function formatarMoeda(valor: unknown) {
  return new Intl.NumberFormat('pt-BR', {
    style: 'currency',
    currency: 'BRL'
  }).format(toNumber(valor))
}

async function carregar() {
  loading.value = true
  erro.value = null
  try {
    const { data } = await buscarSugestaoProducao()
    sugestao.value = data
  } catch (e: any) {
    erro.value = 'Falha ao calcular sugestão de produção.'
  } finally {
    loading.value = false
  }
}

onMounted(carregar)
</script>

<template>
  <div class="mx-auto max-w-6xl p-4">
    <header class="flex flex-col gap-3 sm:flex-row sm:items-start sm:justify-between">
      <div>
        <h1 class="text-2xl font-semibold tracking-tight">Sugestão de Produção</h1>
        <p class="mt-1 text-sm text-slate-600">
          Produção sugerida com base no estoque atual (priorizando produtos de maior valor).
        </p>
      </div>

      <button
        class="rounded-lg bg-blue-600 px-3 py-2 text-sm font-medium text-white hover:bg-blue-700 disabled:opacity-60"
        :disabled="loading"
        @click="carregar"
      >
        Recalcular
      </button>
    </header>

    <div
      v-if="erro"
      class="mt-4 rounded-lg border border-red-200 bg-red-50 px-3 py-2 text-sm text-red-800"
    >
      {{ erro }}
    </div>

    <section class="mt-4 rounded-xl border border-slate-200 bg-white p-3 shadow-sm">
      <div v-if="loading" class="p-4 text-sm text-slate-600">
        Calculando sugestão...
      </div>

      <div v-else-if="!sugestao || sugestao.itens.length === 0" class="p-4 text-sm text-slate-600">
        Nenhum produto pode ser produzido com o estoque atual.
      </div>

      <div v-else class="overflow-x-auto">
        <table class="min-w-[860px] w-full border-collapse text-left text-sm">
          <thead>
            <tr class="border-b border-slate-200 text-slate-600">
              <th class="px-2 py-3 font-medium">Produto</th>
              <th class="px-2 py-3 w-44 font-medium">Valor unitário</th>
              <th class="px-2 py-3 w-44 font-medium">Qtd. sugerida</th>
              <th class="px-2 py-3 w-44 font-medium">Subtotal</th>
            </tr>
          </thead>

          <tbody>
            <tr
              v-for="item in sugestao.itens"
              :key="item.produtoId"
              class="border-b border-slate-100"
            >
              <td class="px-2 py-3 font-medium text-slate-900">
                {{ item.nomeProduto }}
              </td>

              <td class="px-2 py-3">
                {{ formatarMoeda(item.valorUnitario) }}
              </td>

              <td class="px-2 py-3">
                {{ item.quantidadeProduzivel }}
              </td>

              <td class="px-2 py-3 font-medium">
                {{ formatarMoeda(item.valorTotalProduto) }}
              </td>
            </tr>
          </tbody>

          <tfoot>
            <tr class="border-t border-slate-200">
              <td colspan="3" class="px-2 py-3 text-right font-semibold">
                Total estimado
              </td>
              <td class="px-2 py-3 font-semibold text-emerald-700">
                {{ formatarMoeda(sugestao.valorTotalProducao) }}
              </td>
            </tr>
          </tfoot>
        </table>
      </div>
    </section>
  </div>
</template>
