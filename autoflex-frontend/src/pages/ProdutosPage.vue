<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import type { Produto } from '../types/Produto'
import type { MateriaPrimaOption, ProdutoMateriaPrimaItem } from '../types/ProdutoMateriaPrima'

import {
  listarMateriasPrimasParaSelect,
  listarEstruturaProduto,
  atualizarEstruturaProduto
} from '../api/produtoMateriaPrima.api'

import { listarProdutos, criarProduto, atualizarProduto, excluirProduto } from '../api/produto.api'

type ProdutoForm = {
  id?: number
  nome: string
  valor: number | null
}

const loading = ref(false)
const erro = ref<string | null>(null)
const sucesso = ref<string | null>(null)

const produtos = ref<Produto[]>([])

const modalAberto = ref(false)
const modoEdicao = ref(false)

const form = reactive<ProdutoForm>({
  id: undefined,
  nome: '',
  valor: null
})

const confirmAberto = ref(false)
const confirmTexto = ref('')
const idParaExcluir = ref<number | null>(null)

function limparMensagens() {
  erro.value = null
  sucesso.value = null
}

function abrirCriacao() {
  limparMensagens()
  modoEdicao.value = false
  form.id = undefined
  form.nome = ''
  form.valor = null
  modalAberto.value = true
}

function abrirEdicao(p: Produto) {
  limparMensagens()
  modoEdicao.value = true
  form.id = p.id
  form.nome = p.nome
  form.valor = p.valor
  modalAberto.value = true
}

function fecharModal() {
  modalAberto.value = false
}

function abrirConfirmExclusao(p: Produto) {
  limparMensagens()
  idParaExcluir.value = p.id
  confirmTexto.value = `Tem certeza que deseja excluir o produto "${p.nome}" (ID ${p.id})?`
  confirmAberto.value = true
}

function fecharConfirm() {
  confirmAberto.value = false
  idParaExcluir.value = null
  confirmTexto.value = ''
}

function validarFormulario(): string | null {
  const nome = (form.nome ?? '').trim()
  if (!nome) return 'O nome é obrigatório.'
  if (nome.length > 120) return 'O nome deve ter no máximo 120 caracteres.'

  if (form.valor === null || form.valor === undefined) return 'O valor é obrigatório.'
  if (Number.isNaN(Number(form.valor))) return 'O valor está inválido.'
  if (Number(form.valor) < 0) return 'O valor não pode ser negativo.'

  return null
}

function normalizarValor(v: number | null): number {
  const n = Number(v ?? 0)
  return Math.round(n * 100) / 100
}

async function carregar() {
  loading.value = true
  limparMensagens()
  try {
    const { data } = await listarProdutos()
    produtos.value = data
  } catch (e: any) {
    erro.value = e?.mensagem ?? 'Falha ao carregar produtos.'
  } finally {
    loading.value = false
  }
}

async function salvar() {
  limparMensagens()

  const msg = validarFormulario()
  if (msg) {
    erro.value = msg
    return
  }

  loading.value = true
  try {
    const payload = { nome: form.nome.trim(), valor: normalizarValor(form.valor) }

    if (modoEdicao.value && form.id) {
      await atualizarProduto(form.id, payload)
      sucesso.value = 'Produto atualizado com sucesso.'
    } else {
      await criarProduto(payload)
      sucesso.value = 'Produto criado com sucesso.'
    }

    await carregar()
    fecharModal()
  } catch (e: any) {
    erro.value = e?.mensagem ?? 'Falha ao salvar produto.'
  } finally {
    loading.value = false
  }
}

async function confirmarExclusao() {
  if (!idParaExcluir.value) return
  loading.value = true
  limparMensagens()
  try {
    await excluirProduto(idParaExcluir.value)
    sucesso.value = 'Produto excluído com sucesso.'
    await carregar()
  } catch (e: any) {
    erro.value = e?.mensagem ?? 'Falha ao excluir produto.'
  } finally {
    loading.value = false
    fecharConfirm()
  }
}

function formatarMoeda(valor: number) {
  return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(valor)
}

/* =========================
   Estrutura do Produto (RF007)
========================= */

const estruturaAberta = ref(false)
const estruturaLoading = ref(false)
const estruturaErro = ref<string | null>(null)
const estruturaSucesso = ref<string | null>(null)

const produtoEstruturaId = ref<number | null>(null)
const produtoEstruturaNome = ref<string>('')

const materiasOptions = ref<MateriaPrimaOption[]>([])
const estrutura = ref<ProdutoMateriaPrimaItem[]>([])

function limparMensagensEstrutura() {
  estruturaErro.value = null
  estruturaSucesso.value = null
}

async function abrirEstrutura(produtoId: number, produtoNome: string) {
  limparMensagensEstrutura()
  estruturaAberta.value = true
  produtoEstruturaId.value = produtoId
  produtoEstruturaNome.value = produtoNome

  estruturaLoading.value = true
  try {
    const [mpsResp, estruturaResp] = await Promise.all([
      listarMateriasPrimasParaSelect(),
      listarEstruturaProduto(produtoId)
    ])

    // options do select
    materiasOptions.value = (mpsResp.data as any[]).map((x) => ({
      id: x.id,
      nome: x.nome
    }))

    // ✅ GET retorna ProdutoDetalhadoResponse (objeto), então pegamos data.materiasPrimas
    const lista = (estruturaResp.data as any)?.materiasPrimas ?? []

    estrutura.value = lista.map((x: any) => ({
      materiaPrimaId: x.materiaPrimaId,
      nomeMateriaPrima: x.nome, // backend normalmente devolve "nome"
      quantidadeNecessaria: x.quantidadeNecessaria
    }))
  } catch (e: any) {
    estruturaErro.value = e?.mensagem ?? 'Falha ao carregar estrutura do produto.'
  } finally {
    estruturaLoading.value = false
  }
}

function fecharEstrutura() {
  estruturaAberta.value = false
  produtoEstruturaId.value = null
  produtoEstruturaNome.value = ''
  estrutura.value = []
  materiasOptions.value = []
  limparMensagensEstrutura()
}

function adicionarLinhaEstrutura() {
  if (materiasOptions.value.length === 0) {
    estruturaErro.value = 'Cadastre matérias-primas antes de montar a estrutura.'
    return
  }

  const usadas = new Set(estrutura.value.map((e) => e.materiaPrimaId))
  const disponivel = materiasOptions.value.find((mp) => !usadas.has(mp.id))

  if (!disponivel) {
    estruturaErro.value = 'Todas as matérias-primas já foram adicionadas na estrutura.'
    return
  }

  estrutura.value.push({
    materiaPrimaId: disponivel.id,
    quantidadeNecessaria: 0
  })
}



function removerLinhaEstrutura(idx: number) {
  estrutura.value.splice(idx, 1)
}

function validarEstrutura(): string | null {
  if (estrutura.value.length === 0) return 'Adicione pelo menos uma matéria-prima na estrutura.'

  const ids = estrutura.value.map((x) => x.materiaPrimaId).filter((id) => !!id)
  const unique = new Set(ids)
  if (unique.size !== ids.length) return 'Existe matéria-prima duplicada na estrutura. Remova ou ajuste.'

  for (const item of estrutura.value) {
    if (!item.materiaPrimaId) return 'Selecione uma matéria-prima em todas as linhas.'
    if (item.quantidadeNecessaria === null || item.quantidadeNecessaria === undefined) return 'Informe a quantidade necessária.'
    if (Number.isNaN(Number(item.quantidadeNecessaria))) return 'Quantidade necessária inválida.'
    if (Number(item.quantidadeNecessaria) <= 0) return 'Quantidade necessária deve ser maior que zero.'
  }
  return null
}

async function salvarEstrutura() {
  limparMensagensEstrutura()
  const produtoId = produtoEstruturaId.value
  if (!produtoId) return

  const msg = validarEstrutura()
  if (msg) {
    estruturaErro.value = msg
    return
  }

  estruturaLoading.value = true
  try {
    const itens = estrutura.value.map((x) => ({
      materiaPrimaId: x.materiaPrimaId,
      quantidadeNecessaria: Math.round(Number(x.quantidadeNecessaria) * 1000) / 1000
    }))

    // ✅ PUT espera { itens: [...] } e retorna ProdutoDetalhadoResponse
    const { data } = await atualizarEstruturaProduto(produtoId, { itens } as any)

    const lista = (data as any)?.materiasPrimas ?? []
    estrutura.value = lista.map((x: any) => ({
      materiaPrimaId: x.materiaPrimaId,
      nomeMateriaPrima: x.nome,
      quantidadeNecessaria: x.quantidadeNecessaria
    }))

    estruturaSucesso.value = 'Estrutura atualizada com sucesso.'
  } catch (e: any) {
    estruturaErro.value = e?.mensagem ?? 'Falha ao salvar estrutura.'
  } finally {
    estruturaLoading.value = false
  }
}

onMounted(carregar)
</script>

<template>
  <div class="mx-auto max-w-6xl p-4">
    <header class="flex flex-col gap-3 sm:flex-row sm:items-start sm:justify-between">
      <div>
        <h1 class="text-2xl font-semibold tracking-tight">Produtos</h1>
        <p class="mt-1 text-sm text-slate-600">Cadastro e manutenção de produtos.</p>
      </div>

      <div class="flex flex-wrap gap-2">
        <button
          class="rounded-lg border border-slate-300 bg-white px-3 py-2 text-sm hover:bg-slate-50 disabled:opacity-60"
          @click="carregar" :disabled="loading">
          Recarregar
        </button>
        <button
          class="rounded-lg bg-blue-600 px-3 py-2 text-sm font-medium text-white hover:bg-blue-700 disabled:opacity-60"
          @click="abrirCriacao" :disabled="loading">
          Novo produto
        </button>
      </div>
    </header>

    <div v-if="erro" class="mt-4 rounded-lg border border-red-200 bg-red-50 px-3 py-2 text-sm text-red-800">
      {{ erro }}
    </div>
    <div v-if="sucesso"
      class="mt-4 rounded-lg border border-emerald-200 bg-emerald-50 px-3 py-2 text-sm text-emerald-800">
      {{ sucesso }}
    </div>

    <section class="mt-4 rounded-xl border border-slate-200 bg-white p-3 shadow-sm">
      <div v-if="loading" class="p-4 text-sm text-slate-600">Carregando...</div>

      <div v-else>
        <div v-if="produtos.length === 0" class="p-4 text-sm text-slate-600">Nenhum produto cadastrado.</div>

        <div v-else class="overflow-x-auto">
          <table class="min-w-[720px] w-full border-collapse text-left text-sm">
            <thead>
              <tr class="border-b border-slate-200 text-slate-600">
                <th class="px-2 py-3 w-24 font-medium">ID</th>
                <th class="px-2 py-3 font-medium">Nome</th>
                <th class="px-2 py-3 w-44 font-medium">Valor</th>
                <th class="px-2 py-3 w-72 font-medium">Ações</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="p in produtos" :key="p.id" class="border-b border-slate-100">
                <td class="px-2 py-3">{{ p.id }}</td>
                <td class="px-2 py-3 font-medium text-slate-900">{{ p.nome }}</td>
                <td class="px-2 py-3">{{ formatarMoeda(p.valor) }}</td>
                <td class="px-2 py-3">
                  <div class="flex flex-wrap gap-2">
                    <button class="rounded-lg border border-slate-300 bg-white px-3 py-1.5 text-sm hover:bg-slate-50"
                      @click="abrirEdicao(p)">
                      Editar
                    </button>
                    <button class="rounded-lg border border-slate-300 bg-white px-3 py-1.5 text-sm hover:bg-slate-50"
                      @click="abrirEstrutura(p.id, p.nome)">
                      Estrutura
                    </button>
                    <button
                      class="rounded-lg border border-red-200 bg-red-50 px-3 py-1.5 text-sm text-red-700 hover:bg-red-100"
                      @click="abrirConfirmExclusao(p)">
                      Excluir
                    </button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </section>

    <!-- Modal Estrutura do Produto (✅ ajustado para caber na tela) -->
    <div v-if="estruturaAberta" class="fixed inset-0 z-50 grid place-items-center bg-black/40 p-4"
      @click.self="fecharEstrutura">
      <div class="w-full max-w-3xl rounded-xl border border-slate-200 bg-white shadow-lg" style="max-height: 85vh;">
        <div class="flex items-center justify-between border-b border-slate-200 px-4 py-3">
          <div>
            <h2 class="text-lg font-semibold">Estrutura do produto</h2>
            <p class="text-xs text-slate-500">
              Produto: <span class="font-medium text-slate-800">{{ produtoEstruturaNome }}</span>
              <span v-if="produtoEstruturaId"> (ID {{ produtoEstruturaId }})</span>
            </p>
          </div>
          <button class="rounded-lg px-2 py-1 text-slate-600 hover:bg-slate-100" @click="fecharEstrutura"
            aria-label="Fechar">
            ✕
          </button>
        </div>

        <!-- corpo do modal com scroll vertical -->
        <div class="px-4 py-4 overflow-y-auto" style="max-height: calc(85vh - 120px);">
          <div v-if="estruturaErro"
            class="mb-3 rounded-lg border border-red-200 bg-red-50 px-3 py-2 text-sm text-red-800">
            {{ estruturaErro }}
          </div>
          <div v-if="estruturaSucesso"
            class="mb-3 rounded-lg border border-emerald-200 bg-emerald-50 px-3 py-2 text-sm text-emerald-800">
            {{ estruturaSucesso }}
          </div>

          <div class="flex flex-wrap items-center justify-between gap-2">
            <p class="text-sm text-slate-600">
              Defina as matérias-primas e quantidades necessárias para produzir 1 unidade do produto.
            </p>

            <button
              class="rounded-lg border border-slate-300 bg-white px-3 py-2 text-sm hover:bg-slate-50 disabled:opacity-60"
              :disabled="estruturaLoading" @click="adicionarLinhaEstrutura">
              + Adicionar
            </button>
          </div>

          <!-- ✅ remove a rolagem horizontal forçada: sem min-w gigante -->
          <div class="mt-3 rounded-lg border border-slate-200 overflow-hidden">
            <table class="w-full border-collapse text-left text-sm">
              <thead>
                <tr class="border-b border-slate-200 text-slate-600">
                  <th class="px-2 py-3 font-medium">Matéria-prima</th>
                  <th class="px-2 py-3 w-40 font-medium">Qtd. necessária</th>
                  <th class="px-2 py-3 w-28 font-medium">Ações</th>
                </tr>
              </thead>
              <tbody>
                <tr v-if="estruturaLoading">
                  <td class="px-2 py-3 text-slate-600" colspan="3">Carregando...</td>
                </tr>

                <tr v-else-if="estrutura.length === 0">
                  <td class="px-2 py-3 text-slate-600" colspan="3">Nenhuma matéria-prima na estrutura.</td>
                </tr>

                <tr v-else v-for="(item, idx) in estrutura" :key="idx" class="border-b border-slate-100">
                  <td class="px-2 py-3">
                    <select v-model.number="item.materiaPrimaId"
                      class="w-full rounded-lg border border-slate-300 bg-white px-3 py-2 text-sm outline-none focus:border-blue-500"
                      :disabled="estruturaLoading">
                      <option v-for="mp in materiasOptions" :key="mp.id" :value="mp.id"
                        :disabled="estrutura.some((e, i) => i !== idx && e.materiaPrimaId === mp.id)">
                        {{ mp.nome }}
                      </option>

                    </select>
                  </td>

                  <td class="px-2 py-3">
                    <input v-model.number="item.quantidadeNecessaria" type="number" step="0.001" min="0"
                      class="w-full rounded-lg border border-slate-300 px-3 py-2 text-sm outline-none focus:border-blue-500"
                      :disabled="estruturaLoading" />
                  </td>

                  <td class="px-2 py-3">
                    <button
                      class="rounded-lg border border-red-200 bg-red-50 px-2 py-1.5 text-sm text-red-700 hover:bg-red-100 disabled:opacity-60"
                      :disabled="estruturaLoading" @click="removerLinhaEstrutura(idx)">
                      Remover
                    </button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>

          <p v-if="estrutura.length > 0" class="mt-2 text-xs text-slate-500">
            Dica: evite duplicar matéria-prima; o sistema valida duplicidade antes de salvar.
          </p>
        </div>

        <!-- footer fixo do modal -->
        <div class="flex justify-end gap-2 border-t border-slate-200 px-4 py-3">
          <button
            class="rounded-lg border border-slate-300 bg-white px-3 py-2 text-sm hover:bg-slate-50 disabled:opacity-60"
            :disabled="estruturaLoading" @click="fecharEstrutura">
            Fechar
          </button>

          <button
            class="rounded-lg bg-blue-600 px-3 py-2 text-sm font-medium text-white hover:bg-blue-700 disabled:opacity-60"
            :disabled="estruturaLoading" @click="salvarEstrutura">
            Salvar estrutura
          </button>
        </div>
      </div>
    </div>

    <!-- Modal Criar/Editar -->
    <div v-if="modalAberto" class="fixed inset-0 z-50 grid place-items-center bg-black/40 p-4"
      @click.self="fecharModal">
      <div class="w-full max-w-lg rounded-xl border border-slate-200 bg-white shadow-lg">
        <div class="flex items-center justify-between border-b border-slate-200 px-4 py-3">
          <h2 class="text-lg font-semibold">{{ modoEdicao ? 'Editar produto' : 'Novo produto' }}</h2>
          <button class="rounded-lg px-2 py-1 text-slate-600 hover:bg-slate-100" @click="fecharModal"
            aria-label="Fechar">
            ✕
          </button>
        </div>

        <div class="px-4 py-4">
          <div class="grid grid-cols-1 gap-3 sm:grid-cols-[1fr_180px]">
            <div>
              <label class="mb-1 block text-xs font-medium text-slate-600">Nome</label>
              <input v-model="form.nome" type="text" maxlength="120" placeholder="Ex.: Produto A" :disabled="loading"
                class="w-full rounded-lg border border-slate-300 px-3 py-2 text-sm outline-none focus:border-blue-500" />
            </div>

            <div>
              <label class="mb-1 block text-xs font-medium text-slate-600">Valor</label>
              <input v-model.number="form.valor" type="number" step="0.01" min="0" placeholder="Ex.: 199.90"
                :disabled="loading"
                class="w-full rounded-lg border border-slate-300 px-3 py-2 text-sm outline-none focus:border-blue-500" />
            </div>
          </div>
        </div>

        <div class="flex justify-end gap-2 border-t border-slate-200 px-4 py-3">
          <button
            class="rounded-lg border border-slate-300 bg-white px-3 py-2 text-sm hover:bg-slate-50 disabled:opacity-60"
            @click="fecharModal" :disabled="loading">
            Cancelar
          </button>
          <button
            class="rounded-lg bg-blue-600 px-3 py-2 text-sm font-medium text-white hover:bg-blue-700 disabled:opacity-60"
            @click="salvar" :disabled="loading">
            {{ modoEdicao ? 'Salvar alterações' : 'Cadastrar' }}
          </button>
        </div>
      </div>
    </div>

    <!-- Confirmar Exclusão -->
    <div v-if="confirmAberto" class="fixed inset-0 z-50 grid place-items-center bg-black/40 p-4"
      @click.self="fecharConfirm">
      <div class="w-full max-w-lg rounded-xl border border-slate-200 bg-white shadow-lg">
        <div class="flex items-center justify-between border-b border-slate-200 px-4 py-3">
          <h2 class="text-lg font-semibold">Confirmar exclusão</h2>
          <button class="rounded-lg px-2 py-1 text-slate-600 hover:bg-slate-100" @click="fecharConfirm"
            aria-label="Fechar">
            ✕
          </button>
        </div>

        <div class="px-4 py-4">
          <p class="text-sm text-slate-800">{{ confirmTexto }}</p>
          <p class="mt-2 text-xs text-slate-500">Essa ação não poderá ser desfeita.</p>
        </div>

        <div class="flex justify-end gap-2 border-t border-slate-200 px-4 py-3">
          <button
            class="rounded-lg border border-slate-300 bg-white px-3 py-2 text-sm hover:bg-slate-50 disabled:opacity-60"
            @click="fecharConfirm" :disabled="loading">
            Cancelar
          </button>
          <button
            class="rounded-lg bg-red-600 px-3 py-2 text-sm font-medium text-white hover:bg-red-700 disabled:opacity-60"
            @click="confirmarExclusao" :disabled="loading">
            Excluir
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
