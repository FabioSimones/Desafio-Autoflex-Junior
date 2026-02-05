import { createRouter, createWebHistory } from 'vue-router'
import ProdutosPage from '../pages/ProdutosPage.vue'
import MateriasPrimasPage from '../pages/MateriasPrimasPage.vue'
import SugestaoProducaoPage from '../pages/SugestaoProducaoPage.vue'

export default createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', redirect: '/produtos' },
    { path: '/produtos', component: ProdutosPage },
    { path: '/materias-primas', component: MateriasPrimasPage },
    { path: '/sugestao-producao', component: SugestaoProducaoPage }
  ]
})
